package kz.ag.textureanalysis.model;

import ij.process.ImageProcessor;
import kz.ag.textureanalysis.utils.Utils;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.Collection;
import java.util.List;

public class FuzzyKMeansImageClusterer {
    private FuzzyKMeansClusterer<AnnotatedPixelWrapper> fuzzyKMeansClusterer;
    private int [][]g;

    public FuzzyKMeansImageClusterer(final ImageProcessor ip, int k, double fuzziness, DistanceMeasure measure ) {
        fuzzyKMeansClusterer= new FuzzyKMeansClusterer<>(k,fuzziness);
        g= ip.getIntArray();
    }

    public List<CentroidCluster<AnnotatedPixelWrapper>> cluster(Collection<AnnotatedPixelWrapper> points ) {
        return fuzzyKMeansClusterer.cluster( Utils.annotateWithSlidingWindow(g,Utils.DEFAULT_WINDOW_SIZE) );
    }
}

