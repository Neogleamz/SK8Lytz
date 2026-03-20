package com.google.android.exoplayer2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a2 extends a {

    /* renamed from: j  reason: collision with root package name */
    private final int f9225j;

    /* renamed from: k  reason: collision with root package name */
    private final int f9226k;

    /* renamed from: l  reason: collision with root package name */
    private final int[] f9227l;

    /* renamed from: m  reason: collision with root package name */
    private final int[] f9228m;

    /* renamed from: n  reason: collision with root package name */
    private final h2[] f9229n;

    /* renamed from: p  reason: collision with root package name */
    private final Object[] f9230p;
    private final HashMap<Object, Integer> q;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a2(Collection<? extends f1> collection, com.google.android.exoplayer2.source.x xVar) {
        super(false, xVar);
        int i8 = 0;
        int size = collection.size();
        this.f9227l = new int[size];
        this.f9228m = new int[size];
        this.f9229n = new h2[size];
        this.f9230p = new Object[size];
        this.q = new HashMap<>();
        int i9 = 0;
        int i10 = 0;
        for (f1 f1Var : collection) {
            this.f9229n[i10] = f1Var.b();
            this.f9228m[i10] = i8;
            this.f9227l[i10] = i9;
            i8 += this.f9229n[i10].t();
            i9 += this.f9229n[i10].m();
            this.f9230p[i10] = f1Var.a();
            this.q.put(this.f9230p[i10], Integer.valueOf(i10));
            i10++;
        }
        this.f9225j = i8;
        this.f9226k = i9;
    }

    @Override // com.google.android.exoplayer2.a
    protected Object B(int i8) {
        return this.f9230p[i8];
    }

    @Override // com.google.android.exoplayer2.a
    protected int D(int i8) {
        return this.f9227l[i8];
    }

    @Override // com.google.android.exoplayer2.a
    protected int E(int i8) {
        return this.f9228m[i8];
    }

    @Override // com.google.android.exoplayer2.a
    protected h2 H(int i8) {
        return this.f9229n[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<h2> I() {
        return Arrays.asList(this.f9229n);
    }

    @Override // com.google.android.exoplayer2.h2
    public int m() {
        return this.f9226k;
    }

    @Override // com.google.android.exoplayer2.h2
    public int t() {
        return this.f9225j;
    }

    @Override // com.google.android.exoplayer2.a
    protected int w(Object obj) {
        Integer num = this.q.get(obj);
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    @Override // com.google.android.exoplayer2.a
    protected int x(int i8) {
        return b6.l0.h(this.f9227l, i8 + 1, false, false);
    }

    @Override // com.google.android.exoplayer2.a
    protected int y(int i8) {
        return b6.l0.h(this.f9228m, i8 + 1, false, false);
    }
}
