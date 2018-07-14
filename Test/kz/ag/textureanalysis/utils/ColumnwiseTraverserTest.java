package kz.ag.textureanalysis.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColumnwiseTraverserTest {
    private int m= 3, n= 4;
    private  MatrixTraverser matrixTraverser= new ColumnwiseTraverser(m,n);
    private int []ex= {0,1,2,0,1,2,0,1,2,0,1,2};
    private int []ey= {0,0,0,1,1,1,2,2,2,3,3,3};

    @Test
    public void hasNext() {
        for ( int i= 0; i < m; ++i )
            for ( int j= 0; j < n; ++j ) {
                assertEquals(true,matrixTraverser.hasNext());
                matrixTraverser.next();
            }
    }

    @Test
    public void next() {
        int k= 0;
        for ( int i= 0; i < m; ++i )
            for ( int j= 0; j < n; ++j, ++k ) {
                assertEquals(true,matrixTraverser.hasNext());
                Pair<Integer,Integer> p= matrixTraverser.next();
                assertEquals((int)ex[k],(int)p.getX());
                assertEquals((int)ey[k],(int)p.getY());
            }
    }
}