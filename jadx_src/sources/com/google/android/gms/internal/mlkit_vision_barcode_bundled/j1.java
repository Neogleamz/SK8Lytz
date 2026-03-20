package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j1 extends m1 {

    /* renamed from: f  reason: collision with root package name */
    private final int f14795f;

    /* renamed from: g  reason: collision with root package name */
    private final int f14796g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j1(byte[] bArr, int i8, int i9) {
        super(bArr);
        zzdb.A(i8, i8 + i9, bArr.length);
        this.f14795f = i8;
        this.f14796g = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m1
    protected final int O() {
        return this.f14795f;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m1, com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final byte e(int i8) {
        zzdb.K(i8, this.f14796g);
        return this.f14809e[this.f14795f + i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m1, com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final byte g(int i8) {
        return this.f14809e[this.f14795f + i8];
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m1, com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int i() {
        return this.f14796g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.m1, com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final void k(byte[] bArr, int i8, int i9, int i10) {
        System.arraycopy(this.f14809e, this.f14795f + i8, bArr, i9, i10);
    }
}
