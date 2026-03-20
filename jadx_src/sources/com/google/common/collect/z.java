package com.google.common.collect;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class z<E> extends w<E> {

    /* renamed from: f  reason: collision with root package name */
    private transient int[] f19534f;

    /* renamed from: g  reason: collision with root package name */
    private transient int[] f19535g;

    /* renamed from: h  reason: collision with root package name */
    private transient int f19536h;

    /* renamed from: j  reason: collision with root package name */
    private transient int f19537j;

    z() {
    }

    z(int i8) {
        super(i8);
    }

    public static <E> z<E> S(int i8) {
        return new z<>(i8);
    }

    private int T(int i8) {
        return V()[i8] - 1;
    }

    private int[] V() {
        int[] iArr = this.f19534f;
        Objects.requireNonNull(iArr);
        return iArr;
    }

    private int[] W() {
        int[] iArr = this.f19535g;
        Objects.requireNonNull(iArr);
        return iArr;
    }

    private void X(int i8, int i9) {
        V()[i8] = i9 + 1;
    }

    private void Z(int i8, int i9) {
        if (i8 == -2) {
            this.f19536h = i9;
        } else {
            b0(i8, i9);
        }
        if (i9 == -2) {
            this.f19537j = i8;
        } else {
            X(i9, i8);
        }
    }

    private void b0(int i8, int i9) {
        W()[i8] = i9 + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public void D(int i8) {
        super.D(i8);
        this.f19536h = -2;
        this.f19537j = -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public void E(int i8, E e8, int i9, int i10) {
        super.E(i8, e8, i9, i10);
        Z(this.f19537j, i8);
        Z(i8, -2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public void F(int i8, int i9) {
        int size = size() - 1;
        super.F(i8, i9);
        Z(T(i8), x(i8));
        if (i8 < size) {
            Z(T(size), i8);
            Z(i8, x(size));
        }
        V()[size] = 0;
        W()[size] = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public void L(int i8) {
        super.L(i8);
        this.f19534f = Arrays.copyOf(V(), i8);
        this.f19535g = Arrays.copyOf(W(), i8);
    }

    @Override // com.google.common.collect.w, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (G()) {
            return;
        }
        this.f19536h = -2;
        this.f19537j = -2;
        int[] iArr = this.f19534f;
        if (iArr != null && this.f19535g != null) {
            Arrays.fill(iArr, 0, size(), 0);
            Arrays.fill(this.f19535g, 0, size(), 0);
        }
        super.clear();
    }

    @Override // com.google.common.collect.w
    int h(int i8, int i9) {
        return i8 >= size() ? i9 : i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public int i() {
        int i8 = super.i();
        this.f19534f = new int[i8];
        this.f19535g = new int[i8];
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w
    public Set<E> k() {
        Set<E> k8 = super.k();
        this.f19534f = null;
        this.f19535g = null;
        return k8;
    }

    @Override // com.google.common.collect.w, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        return v1.f(this);
    }

    @Override // com.google.common.collect.w, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        return (T[]) v1.g(this, tArr);
    }

    @Override // com.google.common.collect.w
    int v() {
        return this.f19536h;
    }

    @Override // com.google.common.collect.w
    int x(int i8) {
        return W()[i8] - 1;
    }
}
