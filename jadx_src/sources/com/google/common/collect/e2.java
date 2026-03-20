package com.google.common.collect;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e2<E> extends ImmutableList<E> {

    /* renamed from: e  reason: collision with root package name */
    static final ImmutableList<Object> f19252e = new e2(new Object[0], 0);

    /* renamed from: c  reason: collision with root package name */
    final transient Object[] f19253c;

    /* renamed from: d  reason: collision with root package name */
    private final transient int f19254d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e2(Object[] objArr, int i8) {
        this.f19253c = objArr;
        this.f19254d = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        System.arraycopy(this.f19253c, 0, objArr, i8, this.f19254d);
        return i8 + this.f19254d;
    }

    @Override // java.util.List
    public E get(int i8) {
        com.google.common.base.l.l(i8, this.f19254d);
        E e8 = (E) this.f19253c[i8];
        Objects.requireNonNull(e8);
        return e8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public Object[] h() {
        return this.f19253c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int i() {
        return this.f19254d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int k() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.f19254d;
    }
}
