import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import java.util.*;
import ij.gui.*;
import ij.plugin.PlugIn;
import ij.text.*;
import ij.measure.ResultsTable;

public class BasicPreprocessor {

    private ImageProcessor ip;
    private int m, n, mi, mx, H;
    private double[][] counts, probabilities;

    BasicPreprocessor( ImageProcessor ip ) {
        assert ip != null;
        this.ip = ip;
        m= ip.getHeight();
        n= ip.getWidth();
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
        counts = new double[0][H];
        probabilities = new double[0][H];
    }
}
