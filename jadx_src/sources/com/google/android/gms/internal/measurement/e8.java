package com.google.android.gms.internal.measurement;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e8 extends b8 {

    /* renamed from: e  reason: collision with root package name */
    private final byte[] f12157e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f12158f;

    /* renamed from: g  reason: collision with root package name */
    private int f12159g;

    /* renamed from: h  reason: collision with root package name */
    private int f12160h;

    /* renamed from: i  reason: collision with root package name */
    private int f12161i;

    /* renamed from: j  reason: collision with root package name */
    private int f12162j;

    /* renamed from: k  reason: collision with root package name */
    private int f12163k;

    private e8(byte[] bArr, int i8, int i9, boolean z4) {
        super();
        this.f12163k = Integer.MAX_VALUE;
        this.f12157e = bArr;
        this.f12159g = i9 + i8;
        this.f12161i = i8;
        this.f12162j = i8;
        this.f12158f = z4;
    }

    private final void f() {
        int i8 = this.f12159g + this.f12160h;
        this.f12159g = i8;
        int i9 = i8 - this.f12162j;
        int i10 = this.f12163k;
        if (i9 <= i10) {
            this.f12160h = 0;
            return;
        }
        int i11 = i9 - i10;
        this.f12160h = i11;
        this.f12159g = i8 - i11;
    }

    @Override // com.google.android.gms.internal.measurement.b8
    public final int d(int i8) {
        if (i8 >= 0) {
            int e8 = i8 + e();
            if (e8 >= 0) {
                int i9 = this.f12163k;
                if (e8 <= i9) {
                    this.f12163k = e8;
                    f();
                    return i9;
                }
                throw zzkb.f();
            }
            throw zzkb.e();
        }
        throw zzkb.d();
    }

    @Override // com.google.android.gms.internal.measurement.b8
    public final int e() {
        return this.f12161i - this.f12162j;
    }
}
