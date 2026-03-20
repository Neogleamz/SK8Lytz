package com.google.common.collect;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k2 extends y1<Comparable<?>> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    static final k2 f19337a = new k2();
    private static final long serialVersionUID = 0;

    private k2() {
    }

    private Object readResolve() {
        return f19337a;
    }

    @Override // com.google.common.collect.y1
    public <S extends Comparable<?>> y1<S> f() {
        return y1.c();
    }

    @Override // com.google.common.collect.y1, java.util.Comparator
    /* renamed from: g */
    public int compare(Comparable<?> comparable, Comparable<?> comparable2) {
        com.google.common.base.l.n(comparable);
        if (comparable == comparable2) {
            return 0;
        }
        return comparable2.compareTo(comparable);
    }

    public String toString() {
        return "Ordering.natural().reverse()";
    }
}
