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

public class BasicPreprocessor<T extends MatrixTraverser> {

    private final ImageProcessor ip;
    private int m, n, mi, mx, H;
    private double[][] counts, probabilities;
    private double []px,py,p_xpy,p_xmy; //p_{x+y}, p_{x-y}
    private MatrixTraverser traverser;
    private double R,HX,HY,HXY,HXY1,HXY2;

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

        for ( int k= 0; k < 2*H-1; ++k )
            for ( int i= 0, j; i < H; ++i ) {
                j= k-i;
                if ( 0 <= j && j < H )
                    p_xpy[k]+= probabilities[i][j];
            }

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
}

