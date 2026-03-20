package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzoq implements i2 {
    UNKNOWN_FORMAT(0),
    NV16(1),
    NV21(2),
    YV12(3),
    YUV_420_888(7),
    JPEG(8),
    BITMAP(4),
    CM_SAMPLE_BUFFER_REF(5),
    UI_IMAGE(6),
    CV_PIXEL_BUFFER_REF(9);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f14375a;

    zzoq(int i8) {
        this.f14375a = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.i2
    public final int zza() {
        return this.f14375a;
    }
}
