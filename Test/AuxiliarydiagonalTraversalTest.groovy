import kz.ag.textureanalysis.utils.AuxiliarydiagonalTraversal
import kz.ag.textureanalysis.utils.MatrixTraverser
import kz.ag.textureanalysis.utils.Pair
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith (JUnit4.class)
class AuxiliarydiagonalTraversalTest extends GroovyTestCase {
    private int m= 3, n= 4;
    MatrixTraverser mt= new AuxiliarydiagonalTraversal(m,n);
    private int[]ex= [2,1,2,0,1,2,0,1,2,0,1,0] as int[];
    private int[]ey= [0,0,1,0,1,2,1,2,3,2,3,3] as int[];

    public AuxiliarydiagonalTraversalTest() {}

    @Test
    void testHasNext() {
        for ( int i= 0; i < m; ++i )
            for ( int j= 0; j < n; ++j ) {
                assertEquals(true,mt.hasNext());
                mt.next();
            }
        assertEquals(false,mt.hasNext());
    }

    @Test
    void testNext() {
        int k= 0;
        for ( int i= 0; i < m; ++i )
            for ( int j= 0; j < n; ++j ) {
                assertEquals(true,mt.hasNext());
                Pair<Integer,Integer> p= mt.next();
                assertEquals(p.getX(),ex[k]);
                assertEquals(p.getY(),ey[k]);
                ++k;
            }
    }

    void setUp() {
        super.setUp()
    }
}
