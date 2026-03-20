package com.google.common.collect;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y<K, V> extends v<K, V> {

    /* renamed from: l  reason: collision with root package name */
    transient long[] f19511l;

    /* renamed from: m  reason: collision with root package name */
    private transient int f19512m;

    /* renamed from: n  reason: collision with root package name */
    private transient int f19513n;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f19514p;

    y() {
        this(3);
    }

    y(int i8) {
        this(i8, false);
    }

    y(int i8, boolean z4) {
        super(i8);
        this.f19514p = z4;
    }

    public static <K, V> y<K, V> c0() {
        return new y<>();
    }

    public static <K, V> y<K, V> d0(int i8) {
        return new y<>(i8);
    }

    private int e0(int i8) {
        return ((int) (f0(i8) >>> 32)) - 1;
    }

    private long f0(int i8) {
        return g0()[i8];
    }

    private long[] g0() {
        long[] jArr = this.f19511l;
        Objects.requireNonNull(jArr);
        return jArr;
    }

    private void h0(int i8, long j8) {
        g0()[i8] = j8;
    }

    private void i0(int i8, int i9) {
        h0(i8, (f0(i8) & 4294967295L) | ((i9 + 1) << 32));
    }

    private void j0(int i8, int i9) {
        if (i8 == -2) {
            this.f19512m = i9;
        } else {
            k0(i8, i9);
        }
        if (i9 == -2) {
            this.f19513n = i8;
        } else {
            i0(i9, i8);
        }
    }

    private void k0(int i8, int i9) {
        h0(i8, (f0(i8) & (-4294967296L)) | ((i9 + 1) & 4294967295L));
    }

    @Override // com.google.common.collect.v
    int D() {
        return this.f19512m;
    }

    @Override // com.google.common.collect.v
    int E(int i8) {
        return ((int) f0(i8)) - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public void I(int i8) {
        super.I(i8);
        this.f19512m = -2;
        this.f19513n = -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public void J(int i8, K k8, V v8, int i9, int i10) {
        super.J(i8, k8, v8, i9, i10);
        j0(this.f19513n, i8);
        j0(i8, -2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public void M(int i8, int i9) {
        int size = size() - 1;
        super.M(i8, i9);
        j0(e0(i8), E(i8));
        if (i8 < size) {
            j0(e0(size), i8);
            j0(i8, E(size));
        }
        h0(size, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public void T(int i8) {
        super.T(i8);
        this.f19511l = Arrays.copyOf(g0(), i8);
    }

    @Override // com.google.common.collect.v, java.util.AbstractMap, java.util.Map
    public void clear() {
        if (N()) {
            return;
        }
        this.f19512m = -2;
        this.f19513n = -2;
        long[] jArr = this.f19511l;
        if (jArr != null) {
            Arrays.fill(jArr, 0, size(), 0L);
        }
        super.clear();
    }

    @Override // com.google.common.collect.v
    void p(int i8) {
        if (this.f19514p) {
            j0(e0(i8), E(i8));
            j0(this.f19513n, i8);
            j0(i8, -2);
            G();
        }
    }

    @Override // com.google.common.collect.v
    int q(int i8, int i9) {
        return i8 >= size() ? i9 : i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public int r() {
        int r4 = super.r();
        this.f19511l = new long[r4];
        return r4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.v
    public Map<K, V> s() {
        Map<K, V> s8 = super.s();
        this.f19511l = null;
        return s8;
    }

    @Override // com.google.common.collect.v
    Map<K, V> v(int i8) {
        return new LinkedHashMap(i8, 1.0f, this.f19514p);
    }
}
