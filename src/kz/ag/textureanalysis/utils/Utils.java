package kz.ag.textureanalysis.utils;

public class Utils {
    public static <T extends MatrixTraverser> boolean areAdjacent( Pair<Integer,Integer> a, Pair<Integer,Integer> b, Class<T> cl ) {
        //TODO: coreect adjacency test; now it checks only if they are on the same row, col, or diagonal
        if ( cl == RowwiseTraverser.class ) {
            return a.getX() == b.getX();
        }
        if ( cl == ColumnwiseTraverser.class ) {
            return a.getY() == b.getY();
        }
        if ( cl == MaindiagonalTraverser.class ) {
            return a.getX()+a.getY() == b.getX()+b.getY();
        }
        if ( cl == AuxiliarydiagonalTraversal.class ) {
            return a.getX()-a.getY() == b.getX()-b.getY();
        }
        return false ;
    }
}
