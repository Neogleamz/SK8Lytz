package com.google.common.collect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o0<E> extends j0<E> implements r1<E> {
    @Override // com.google.common.collect.r1
    public int B(Object obj, int i8) {
        return v().B(obj, i8);
    }

    @Override // com.google.common.collect.r1
    public int J(E e8, int i8) {
        return v().J(e8, i8);
    }

    @Override // com.google.common.collect.r1
    public int U(E e8, int i8) {
        return v().U(e8, i8);
    }

    @Override // com.google.common.collect.r1
    public boolean Y(E e8, int i8, int i9) {
        return v().Y(e8, i8, i9);
    }

    @Override // java.util.Collection, com.google.common.collect.r1
    public boolean equals(Object obj) {
        return obj == this || v().equals(obj);
    }

    @Override // java.util.Collection, com.google.common.collect.r1
    public int hashCode() {
        return v().hashCode();
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        return v().m0(obj);
    }

    protected abstract r1<E> v();
}
