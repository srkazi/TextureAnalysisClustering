import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import java.util.*;
import ij.gui.*;
import ij.plugin.PlugIn;
import ij.text.*;
import ij.measure.ResultsTable;
import kz.ag.textureanalysis.utils.MatrixTraverser;
import kz.ag.textureanalysis.utils.Pair;
import kz.ag.textureanalysis.utils.TraverserFactory;
import kz.ag.textureanalysis.utils.Utils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class BasicPreprocessor<T extends MatrixTraverser> {

    private final ImageProcessor ip;
    private int m, n, mi, mx, H;
    private double[][] counts, probabilities;
    private double []px,py,p_xpy,p_xmy; //p_{x+y}, p_{x-y}
    private MatrixTraverser traverser;
    private double R,HX,HY,HXY,HXY1,HXY2;
    private DescriptiveStatistics statsPx= new DescriptiveStatistics();
    private DescriptiveStatistics statsPy= new DescriptiveStatistics();
    private DescriptiveStatistics statsPXY= new DescriptiveStatistics();

    BasicPreprocessor( ImageProcessor ip, Class<T> traverserImplClass ) {
        assert ip != null;
        this.ip = ip;
        traverser= TraverserFactory.buildTraverser(traverserImplClass,m= ip.getHeight(),n= ip.getWidth());
        setUp();
    }

    private void setUp() {
        mi= Integer.MAX_VALUE;
        mx= Integer.MIN_VALUE;
        for ( int i= 0; i < m; ++i )
            for ( int j= 0; j < n; ++j ) {
                mi= Math.min(mi,ip.getPixel(i,j));
                mx= Math.max(mx,ip.getPixel(i,j));
            }
        /**
         * Trying to make H = 2^k, and H >= mx+1
         */
        if ( 0 == ((mx+1)&(mx)) ) // if mx is already power of two
            H= mx;
        else {
            for ( H= 0; (1<<H) < (mx+1); ++H ) ;
            H= (1<<H);
        }
        assert 0 == (H & (H-1));
        counts = new double[0][H];
        probabilities = new double[0][H];
        px= new double[m];
        py= new double[n];
        populateCounts();
        calculateEntropies();
        calcSummary();
    }

    private void populateCounts() {
        /**
         * calculate co-occurrence frequency matrix
         */
        Pair<Integer,Integer> cur= null, prev;
        for ( ;traverser.hasNext(); ) {
            prev= cur; cur= traverser.next();
            if ( traverser.areAdjacent(prev,cur) ) {
                int x= ip.getPixel(cur.getX(),cur.getY());
                int y= ip.getPixel(prev.getX(),prev.getY());
                ++counts[x][y];
                ++counts[y][x];
            }
        }
        /**
         * calculate co-occurrence probability matrix
         */
        R= 0;
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; ++j )
                R+= counts[i][j];
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; ++j )
                probabilities[i][j]= counts[i][j]/R;

        /**
         * calculate marginals
         */
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; px[i]+= probabilities[i][j++] ) ;
        for ( int j= 0; j < H; ++j )
            for ( int i= 0; i < H; py[j]+= probabilities[i++][j] ) ;

        for ( int i= 0; i < H; statsPx.addValue(px[i++]) ) ;
        for ( int j= 0; j < H; statsPy.addValue(py[j++]) ) ;

        for ( int k= 0; k < 2*H-1; ++k )
            for ( int i= 0, j; i < H; ++i ) {
                j= k-i;
                if ( 0 <= j && j < H )
                    p_xpy[k]+= probabilities[i][j];
            }
        for ( int k= 0; k < 2*H-1; statsPXY.addValue(p_xpy[k++]) ) ;

        for ( int k= 0; k < H; ++k )
            for ( int i= 0, j; i < H; ++i ) {
                j= i-k;
                if ( 0 <= j && j < H )
                    p_xmy[k]+= probabilities[i][j];
                j= i+k;
                if ( 0 <= j && j < H )
                    p_xmy[k]+= probabilities[i][j];
            }
    }

    private void calculateEntropies() {
        for ( int i= 0; i < H; ++i )
            HX+= px[i]*Math.log(px[i]);
        HX= -HX/Math.log(2);
        for ( int j= 0; j < H; ++j )
            HY+= py[j]*Math.log(py[j]);
        HY= -HY/Math.log(2);
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; ++j )
                HXY+= probabilities[i][j]*Math.log(probabilities[i][j]);
        HXY= -HXY/Math.log(2);
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; ++j )
                HXY1+= probabilities[i][j]*Math.log(px[i]*py[j]);
        HXY1= -HXY1/Math.log(2);
        for ( int i= 0; i < H; ++i )
            for ( int j= 0; j < H; ++j )
                HXY2+= px[i]*py[j]*Math.log(px[i]*py[j]);
        HXY2= -HXY2/Math.log(2);
    }

    private Map<TextureFeatures,Double> summary;

    private void calcSummary() {
        summary= new HashMap<>();
        double s;
        int i,j,k,x,y;
        /**
         * 1. Angular Second Moment [asm]
         */
        for ( s= 0, i= 0; i < H; ++i )
            for ( j= 0; j < H; ++j )
                s+= Math.pow(probabilities[i][j],2);
        summary.put(TextureFeatures.ANGULAR_SECOND_MOMENT,s);
        /**
         * 2. Contrast [con]
         */
        for ( s= 0, k= 0; k < H; ++k )
            s+= Math.pow(k,2)*p_xmy[k];
        summary.put(TextureFeatures.CONTRAST,s);
        /**
         * 3. Correlation [cor]
         */
        for ( s= 0, i= 0; i < H; ++i )
            for ( j= 0; j < H; ++j )
                s+= i*j*probabilities[i][j]-statsPx.getMean()*statsPy.getMean();
        s/= (statsPx.getStandardDeviation()*statsPy.getStandardDeviation());
        summary.put(TextureFeatures.CORRELATION,s);
        /**
         * 4. Sum of squares: Variance [var]
         * TODO: the formula on website is missing something
         */
        /**
         * 5. Inverse Difference Moment [idm]
         */
        for ( s= 0, i= 0; i < H; ++i )
            for ( j= 0; j < H; ++j )
                s+= probabilities[i][j]/(1+Math.pow(i-j,2));
        summary.put(TextureFeatures.INVERSE_DIFFERENT_MOMENT,s);
        /**
         * 6. Sum Average [sav]
         */
        for ( s= 0, k= 0; k < 2*H-1; ++k )
            s+= k*p_xpy[k];
        summary.put(TextureFeatures.SUM_AVERAGE,s);
        /**
         * 7. Sum Variance [sva]
         * TODO:
         */
        /**
         * 8. Sum Entropy [sen]
         */
        for ( s= 0, k= 0; k < 2*H-1; ++k ) {
            double arg= Math.max(Utils.eps,p_xpy[k]);
            s+= Math.log(arg)*arg;
        }
        summary.put(TextureFeatures.SUM_ENTROPY,s/Math.log(2));
        /**
         * 9. Entropy [ent]
         */
        summary.put(TextureFeatures.ENTROPY,HXY);
        /**
         * 10. Difference Variance [dva]
         */
        summary.put(TextureFeatures.DIFFERENCE_VARIANCE,statsPXY.getVariance());
        /**
         * 11. Difference Entropy [den]
         */
        for ( s= 0, k= 0; k < H; ++k ) {
            double arg= Math.max(Utils.eps,p_xmy[k]);
            s += arg*Math.log(arg);
        }
        summary.put(TextureFeatures.DIFFERENCE_ENTROPY,-s/Math.log(2));
        /**
         * 12. Information Measures of Correlation
         */
        summary.put(TextureFeatures.F12,(summary.get(TextureFeatures.ENTROPY)-HXY1)/Math.max(HX,HY));
        /**
         * 13. Information Measures of Correlation
         */
        summary.put(TextureFeatures.F13,Math.sqrt(1-Math.exp(-2*(HXY2-summary.get(TextureFeatures.ENTROPY)))));
        /**
         * 14. Maximal Correlation Coefficient
         * TODO: use JScience library
         */
    }

    public double getValue( TextureFeatures feature ) {
        return summary.get(feature);
    }
}

