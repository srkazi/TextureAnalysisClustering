package kz.ag.textureanalysis.utils;

public class Utils {
    public static final double eps= 1e-7;
    /*
    * This is useless code, already served by the implementation-classes themselves
    public static <T extends MatrixTraverser> boolean areAdjacent( Pair<Integer,Integer> a, Pair<Integer,Integer> b, Class<T> cl ) {
        if ( cl == RowwiseTraverser.class ) {
            boolean res= a.getX() == b.getX();
        }
        if ( cl == ColumnwiseTraverser.class ) {
            boolean res= a.getY() == b.getY();
        }
        if ( cl == MaindiagonalTraverser.class ) {
            boolean res= a.getX()+a.getY() == b.getX()+b.getY();
        }
        if ( cl == AuxiliarydiagonalTraverser.class ) {
            boolean res= a.getX()-a.getY() == b.getX()-b.getY();
        }
        return false ;
    }
    */
}
