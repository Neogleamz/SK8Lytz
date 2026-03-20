package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r1 extends t1 {

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f14844b;

    /* renamed from: c  reason: collision with root package name */
    private int f14845c;

    /* renamed from: d  reason: collision with root package name */
    private int f14846d;

    /* renamed from: e  reason: collision with root package name */
    private int f14847e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ r1(byte[] bArr, int i8, int i9, boolean z4, q1 q1Var) {
        super(null);
        this.f14847e = Integer.MAX_VALUE;
        this.f14844b = bArr;
        this.f14845c = 0;
    }

    public final int c(int i8) {
        int i9 = this.f14847e;
        this.f14847e = 0;
        int i10 = this.f14845c + this.f14846d;
        this.f14845c = i10;
        if (i10 > 0) {
            this.f14846d = i10;
            this.f14845c = i10 - i10;
        } else {
            this.f14846d = 0;
        }
        return i9;
    }
}
