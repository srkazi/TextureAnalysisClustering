import ij.process.ImageProcessor;
import kz.ag.textureanalysis.utils.*;

import java.util.Map;

public class AggregateProcessor {
    private BasicPreprocessor bpRows, bpCols, bpMainDiag, bpAuxDiag;

    AggregateProcessor( ImageProcessor ip ) {
        bpRows= new BasicPreprocessor(ip, RowwiseTraverser.class);
        bpCols= new BasicPreprocessor(ip, ColumnwiseTraverser.class);
        bpMainDiag= new BasicPreprocessor(ip, MaindiagonalTraverser.class);
        bpAuxDiag= new BasicPreprocessor(ip, AuxiliarydiagonalTraverser.class);
    }

    public double getValue( TextureFeatures feature ) {
        return (bpRows.getValue(feature)+bpCols.getValue(feature)+bpMainDiag.getValue(feature)+bpAuxDiag.getValue(feature))/4.00;
    }
}
