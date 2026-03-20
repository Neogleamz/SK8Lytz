package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzte implements i2 {
    UNRECOGNIZED(0),
    CODE_128(1),
    CODE_39(2),
    CODE_93(3),
    CODABAR(4),
    DATA_MATRIX(5),
    EAN_13(6),
    EAN_8(7),
    ITF(8),
    QR_CODE(9),
    UPC_A(10),
    UPC_E(11),
    PDF417(12),
    AZTEC(13),
    DATABAR(14),
    TEZ_CODE(16);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f14603a;

    zzte(int i8) {
        this.f14603a = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.i2
    public final int zza() {
        return this.f14603a;
    }
}
