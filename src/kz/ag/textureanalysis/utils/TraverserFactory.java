package kz.ag.textureanalysis.utils;

public class TraverserFactory {
    public static <T extends MatrixTraverser> MatrixTraverser buildTraverser( Class<T> cl, int m, int n ) {
        if ( cl.equals(RowwiseTraverser.class) )
            return new RowwiseTraverser(m,n);
        if ( cl.equals(ColumnwiseTraverser.class) )
            return new ColumnwiseTraverser(m,n);
        if ( cl.equals(MaindiagonalTraverser.class) )
            return new MaindiagonalTraverser(m,n);
        if ( cl.equals(AuxiliarydiagonalTraversal.class) )
            return new AuxiliarydiagonalTraversal(m,n);
        return null;
    }
}
