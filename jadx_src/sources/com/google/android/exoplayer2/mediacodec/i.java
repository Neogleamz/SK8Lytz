package com.google.android.exoplayer2.mediacodec;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i {

    /* renamed from: a  reason: collision with root package name */
    private int f10019a = 0;

    /* renamed from: b  reason: collision with root package name */
    private int f10020b = -1;

    /* renamed from: c  reason: collision with root package name */
    private int f10021c = 0;

    /* renamed from: d  reason: collision with root package name */
    private int[] f10022d;

    /* renamed from: e  reason: collision with root package name */
    private int f10023e;

    public i() {
        int[] iArr = new int[16];
        this.f10022d = iArr;
        this.f10023e = iArr.length - 1;
    }

    private void c() {
        int[] iArr = this.f10022d;
        int length = iArr.length << 1;
        if (length < 0) {
            throw new IllegalStateException();
        }
        int[] iArr2 = new int[length];
        int length2 = iArr.length;
        int i8 = this.f10019a;
        int i9 = length2 - i8;
        System.arraycopy(iArr, i8, iArr2, 0, i9);
        System.arraycopy(this.f10022d, 0, iArr2, i9, i8);
        this.f10019a = 0;
        this.f10020b = this.f10021c - 1;
        this.f10022d = iArr2;
        this.f10023e = iArr2.length - 1;
    }

    public void a(int i8) {
        if (this.f10021c == this.f10022d.length) {
            c();
        }
        int i9 = (this.f10020b + 1) & this.f10023e;
        this.f10020b = i9;
        this.f10022d[i9] = i8;
        this.f10021c++;
    }

    public void b() {
        this.f10019a = 0;
        this.f10020b = -1;
        this.f10021c = 0;
    }

    public boolean d() {
        return this.f10021c == 0;
    }

    public int e() {
        int i8 = this.f10021c;
        if (i8 != 0) {
            int[] iArr = this.f10022d;
            int i9 = this.f10019a;
            int i10 = iArr[i9];
            this.f10019a = (i9 + 1) & this.f10023e;
            this.f10021c = i8 - 1;
            return i10;
        }
        throw new NoSuchElementException();
    }
}
