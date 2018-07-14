package kz.ag.textureanalysis.utils;

public class Pair<U,V> {

    private U x;
    private V y;

    Pair( U u, V v ) {
        x= u;
        y= v;
    }

    public U getX() {
        return x;
    }

    public V getY() {
        return y;
    }
}
