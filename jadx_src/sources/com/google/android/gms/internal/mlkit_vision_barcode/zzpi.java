package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzpi implements i2 {
    TYPE_UNKNOWN(0),
    TYPE_THIN(1),
    TYPE_THICK(2),
    TYPE_GMV(3);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f14388a;

    zzpi(int i8) {
        this.f14388a = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.i2
    public final int zza() {
        return this.f14388a;
    }
}
