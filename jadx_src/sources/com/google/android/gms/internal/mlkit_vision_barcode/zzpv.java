package com.google.android.gms.internal.mlkit_vision_barcode;

import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzpv implements i2 {
    FORMAT_UNKNOWN(0),
    FORMAT_CODE_128(1),
    FORMAT_CODE_39(2),
    FORMAT_CODE_93(4),
    FORMAT_CODABAR(8),
    FORMAT_DATA_MATRIX(16),
    FORMAT_EAN_13(32),
    FORMAT_EAN_8(64),
    FORMAT_ITF(RecognitionOptions.ITF),
    FORMAT_QR_CODE(RecognitionOptions.QR_CODE),
    FORMAT_UPC_A(RecognitionOptions.UPC_A),
    FORMAT_UPC_E(RecognitionOptions.UPC_E),
    FORMAT_PDF417(RecognitionOptions.PDF417),
    FORMAT_AZTEC(RecognitionOptions.AZTEC);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f14563a;

    zzpv(int i8) {
        this.f14563a = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.i2
    public final int zza() {
        return this.f14563a;
    }
}
