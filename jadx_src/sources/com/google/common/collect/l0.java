package com.google.common.collect;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class l0<T> extends p0 implements Iterator<T> {
    @Override // java.util.Iterator
    public boolean hasNext() {
        return i().hasNext();
    }

    protected abstract Iterator<T> i();

    public T next() {
        return i().next();
    }
}
