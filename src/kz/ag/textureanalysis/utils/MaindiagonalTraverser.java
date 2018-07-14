package kz.ag.textureanalysis.utils;

import java.util.NoSuchElementException;

public class MaindiagonalTraverser implements MatrixTraverser {
    private int m,n,x,y,k; //(x,y) is the current cell, k == (x+y)
    /**
     * @param m: number of rows
     * @param n: number of columns
     */
    public MaindiagonalTraverser( int m, int n ) {
        x= y= 0; k= x+y;
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
        return !(x == m-1 && y == n-1);
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
        --y; ++x;
        if ( y < 0 || x >= m ) {
            ++k;
            x= 0; y= k-x;
            if ( y >= n ) {
                y= n-1;
                x= k-x;
            }
            assert 0 <= x && x < m && 0 <= y && y < n;
        }
        return res;
    }
}
