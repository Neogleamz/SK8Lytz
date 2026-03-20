package com.google.common.collect;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t1 extends y1<Comparable<?>> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    static final t1 f19452a = new t1();
    private static final long serialVersionUID = 0;

    private t1() {
    }

    private Object readResolve() {
        return f19452a;
    }

    @Override // com.google.common.collect.y1
    public <S extends Comparable<?>> y1<S> f() {
        return k2.f19337a;
    }

    @Override // com.google.common.collect.y1, java.util.Comparator
    /* renamed from: g */
    public int compare(Comparable<?> comparable, Comparable<?> comparable2) {
        com.google.common.base.l.n(comparable);
        com.google.common.base.l.n(comparable2);
        return comparable.compareTo(comparable2);
    }

    public String toString() {
        return "Ordering.natural()";
    }
}
