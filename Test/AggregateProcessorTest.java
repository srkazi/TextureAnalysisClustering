import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;
import kz.ag.textureanalysis.model.AggregateProcessor;
import kz.ag.textureanalysis.model.TextureFeatures;
import kz.ag.textureanalysis.utils.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AggregateProcessorTest {
private final String picName= "128_6_031.bmp";

    private Map<TextureFeatures,Double> vals= new HashMap<>();

    {
        vals.put(TextureFeatures.ANGULAR_SECOND_MOMENT,0.0106);
        vals.put(TextureFeatures.CORRELATION,0.0074);
        vals.put(TextureFeatures.CONTRAST,225.9907);
        vals.put(TextureFeatures.INVERSE_DIFFERENT_MOMENT,0.1446);
        vals.put(TextureFeatures.SUM_AVERAGE,3.8064);
        vals.put(TextureFeatures.SUM_ENTROPY,5.0954);
        vals.put(TextureFeatures.ENTROPY,6.9731);
        vals.put(TextureFeatures.DIFFERENCE_VARIANCE,3.7148);
        vals.put(TextureFeatures.DIFFERENCE_ENTROPY,1.4030);
        vals.put(TextureFeatures.F12,-0.0883);
    }

    private AggregateProcessor bp;

    /**
     * as explained here: https://stackoverflow.com/questions/11441126/how-to-open-an-image-with-imagej-in-java
     */
    @Before
    public void setup() {
        Opener opener= new Opener();
        String imageFilePath= Utils.RESOURCES_DIRECTORY+picName;
        File file= new File(imageFilePath);
        assert file.exists();
        ImagePlus imagePlus= opener.openImage(imageFilePath);
        assert imagePlus != null;
        ImageProcessor ip= imagePlus.getProcessor();
        bp= new AggregateProcessor(ip);
    }


    @Test
    public void getValueAngularSecondMoment() {
        double diff= bp.getValue(TextureFeatures.ANGULAR_SECOND_MOMENT)-vals.get(TextureFeatures.ANGULAR_SECOND_MOMENT);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueContrast() {
        double diff= bp.getValue(TextureFeatures.CONTRAST)-vals.get(TextureFeatures.CONTRAST);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueCorrelation() {
        double diff= bp.getValue(TextureFeatures.CORRELATION)-vals.get(TextureFeatures.CORRELATION);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueInverseDifferenceMoment() {
        double diff= bp.getValue(TextureFeatures.INVERSE_DIFFERENT_MOMENT)-vals.get(TextureFeatures.INVERSE_DIFFERENT_MOMENT);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueSumAverage() {
        double diff= bp.getValue(TextureFeatures.SUM_AVERAGE)-vals.get(TextureFeatures.SUM_AVERAGE);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueSumEntropy() {
        double diff= bp.getValue(TextureFeatures.SUM_ENTROPY)-vals.get(TextureFeatures.SUM_ENTROPY);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

        @Test
    public void getValueEntropy() {
        double diff= bp.getValue(TextureFeatures.ENTROPY)-vals.get(TextureFeatures.ENTROPY);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueInverseDifferenceVariance() {
        double diff= bp.getValue(TextureFeatures.DIFFERENCE_VARIANCE)-vals.get(TextureFeatures.DIFFERENCE_VARIANCE);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueDifferenceEntropy() {
        double diff= bp.getValue(TextureFeatures.DIFFERENCE_ENTROPY)-vals.get(TextureFeatures.DIFFERENCE_ENTROPY);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }

    @Test
    public void getValueF12() {
        double diff= bp.getValue(TextureFeatures.F12)-vals.get(TextureFeatures.F12);
        assert(Math.abs(diff) < Utils.eps ): String.format("Difference is %f\n",Math.abs(diff));
        assertTrue(Math.abs(diff) < Utils.eps );
    }
}