package com.google.common.collect;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class x1<K> extends w1<K> {

    /* renamed from: i  reason: collision with root package name */
    transient long[] f19507i;

    /* renamed from: j  reason: collision with root package name */
    private transient int f19508j;

    /* renamed from: k  reason: collision with root package name */
    private transient int f19509k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x1(int i8) {
        this(i8, 1.0f);
    }

    x1(int i8, float f5) {
        super(i8, f5);
    }

    private int B(int i8) {
        return (int) (this.f19507i[i8] >>> 32);
    }

    private int C(int i8) {
        return (int) this.f19507i[i8];
    }

    private void D(int i8, int i9) {
        long[] jArr = this.f19507i;
        jArr[i8] = (jArr[i8] & 4294967295L) | (i9 << 32);
    }

    private void E(int i8, int i9) {
        if (i8 == -2) {
            this.f19508j = i9;
        } else {
            F(i8, i9);
        }
        if (i9 == -2) {
            this.f19509k = i8;
        } else {
            D(i9, i8);
        }
    }

    private void F(int i8, int i9) {
        long[] jArr = this.f19507i;
        jArr[i8] = (jArr[i8] & (-4294967296L)) | (i9 & 4294967295L);
    }

    @Override // com.google.common.collect.w1
    public void a() {
        super.a();
        this.f19508j = -2;
        this.f19509k = -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public int b() {
        int i8 = this.f19508j;
        if (i8 == -2) {
            return -1;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public void k(int i8, float f5) {
        super.k(i8, f5);
        this.f19508j = -2;
        this.f19509k = -2;
        long[] jArr = new long[i8];
        this.f19507i = jArr;
        Arrays.fill(jArr, -1L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public void l(int i8, K k8, int i9, int i10) {
        super.l(i8, k8, i9, i10);
        E(this.f19509k, i8);
        E(i8, -2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public void m(int i8) {
        int z4 = z() - 1;
        E(B(i8), C(i8));
        if (i8 < z4) {
            E(B(z4), i8);
            E(i8, C(z4));
        }
        super.m(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public int p(int i8) {
        int C = C(i8);
        if (C == -2) {
            return -1;
        }
        return C;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public int q(int i8, int i9) {
        return i8 == z() ? i9 : i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.w1
    public void v(int i8) {
        super.v(i8);
        long[] jArr = this.f19507i;
        int length = jArr.length;
        long[] copyOf = Arrays.copyOf(jArr, i8);
        this.f19507i = copyOf;
        Arrays.fill(copyOf, length, i8, -1L);
    }
}
