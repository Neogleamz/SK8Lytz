package com.google.common.collect;

import java.util.Comparator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class y1<T> implements Comparator<T> {
    public static <T> y1<T> a(Comparator<T> comparator) {
        return comparator instanceof y1 ? (y1) comparator : new a0(comparator);
    }

    public static <C extends Comparable> y1<C> c() {
        return t1.f19452a;
    }

    public <E extends T> ImmutableList<E> b(Iterable<E> iterable) {
        return ImmutableList.M(this, iterable);
    }

    @Override // java.util.Comparator
    public abstract int compare(T t8, T t9);

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T2 extends T> y1<Map.Entry<T2, ?>> d() {
        return (y1<Map.Entry<T2, ?>>) e(m1.h());
    }

    public <F> y1<F> e(com.google.common.base.g<F, ? extends T> gVar) {
        return new s(gVar, this);
    }

    public <S extends T> y1<S> f() {
        return new l2(this);
    }
}
