/*
 * [Haralick Textural Features](http://earlglynn.github.io/RNotes/package/EBImage/Haralick-Textural-Features.html)
 */
import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import java.util.*;
import ij.gui.*;
import ij.plugin.PlugIn;
import ij.text.*;
import ij.measure.ResultsTable;
import javolution.io.Struct;

enum TextureFeatures {

    ANGULAR_SECOND_MOMENT("Angular Second Moment"),
    CONTRAST("Contrast"),
    CORRELATION("Correlation"),
    SUM_OF_SQUARES("Sum of Squares: Variance"),
    INVERSE_DIFFERENT_MOMENT("Inverse Different Moment"),
    SUM_AVERAGE("Sum Average"),
    SUM_VARIANCE("Sum Variance"),
    SUM_ENTROPY("Sum Entropy"),
    ENTROPY("Entropy"),
    DIFFERENCE_VARIANCE("Difference Variance"),
    DIFFERENCE_ENTROPY("Difference Entropy"),
    F12("Information Measure of Correlation: f12"),
    F13("Information Measure of Correlation: f13"),
    MAXIMAL_CORRELATION_COEFFICIENT("Maximal Correlation Coefficient");

    private String description;

    private TextureFeatures( String description ) {
        this.description= description;
    }

    public String getDescription() {
        return description;
    }
};

public class TextureAnalyzer_AG implements PlugInFilter {

    private static final int STEP= 1, oo= (1<<30);
    private static int step;
    private ResultsTable resultsTable;

    private Map<TextureFeatures,Boolean> checked= null;

    public int setup( String arg, ImagePlus imp ) {
        if ( arg.equals("about") ) {
            showAbout();
            return DONE;
        }
        if ( imp != null && !showDialog() )
            return DONE;
        return DOES_8G+DOES_STACKS+SUPPORTS_MASKING;
        //return DOES_ALL+ROI_REQUIRED;
    }

    void showAbout() {
        IJ.showMessage("About TextureAnalyzerAG:...",
                "This plugin calculates various texture parameters.");
    }

    public void run( ImageProcessor ip ) {
        //TODO
        int m= ip.getHeight(), n= ip.getWidth();
    }

    boolean showDialog() {
        GenericDialog gd= new GenericDialog("Textural Features");
        gd.addMessage("Textural features\n"+" based in Gray-Level-Correlation-Matrices");
        gd.addNumericField("Enter the size of the step in pixels", step, 0);

        gd.addMessage("Select the features:");
        if ( checked == null )
            checked= new HashMap<TextureFeatures,Boolean>();
        for ( TextureFeatures f: TextureFeatures.values() ) {
            if ( !checked.containsKey(f) )
                checked.put(f,Boolean.valueOf(false));
            gd.addCheckbox(f.getDescription(),checked.get(f));
        }

        gd.addCheckbox("row",true);
        gd.addCheckbox("col",false);
        gd.addCheckbox("diag1",false);
        gd.addCheckbox("diag2",false);

        gd.showDialog();
        if ( gd.wasCanceled() )
            return false ;
        step= (int)gd.getNextNumber();
        for ( TextureFeatures f: TextureFeatures.values() )
            checked.put(f,Boolean.valueOf(gd.getNextBoolean()));

        return true ;
    }
}
