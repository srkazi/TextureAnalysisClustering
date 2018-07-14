package kz.ag.textureanalysis.utils;

import java.util.NoSuchElementException;

public class RowwiseTraverser implements MatrixTraverser {
    private int m,n,x,y,cnt; //(x,y) is the current cell
    /**
     * @param m: number of rows
     * @param n: number of columns
     */
    public RowwiseTraverser( int m, int n ) {
        x= y= 0; cnt= 0;
        this.m= m; this.n= n;
        assert m >= 0 && n >= 0;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return cnt < m*n;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Pair<Integer, Integer> next() {
        Pair<Integer,Integer> res= new Pair<>(x,y);
        ++cnt;
        if ( hasNext() ) {
            if (++y == n) {
                y = 0;
                ++x;
            }
        }
        assert vc(x,y);
        return res;
    }
    private boolean vc( int x, int y ) {
        return 0<=x && x<m && 0<=y && y<n;
    }
}
