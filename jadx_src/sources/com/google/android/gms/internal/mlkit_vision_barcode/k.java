package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends a implements m {
    /* JADX INFO: Access modifiers changed from: package-private */
    public k(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetectorCreator");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.m
    public final j E1(x6.a aVar, zzah zzahVar) {
        j jVar;
        Parcel d8 = d();
        l0.b(d8, aVar);
        l0.a(d8, zzahVar);
        Parcel e8 = e(1, d8);
        IBinder readStrongBinder = e8.readStrongBinder();
        if (readStrongBinder == null) {
            jVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetector");
            jVar = queryLocalInterface instanceof j ? (j) queryLocalInterface : new j(readStrongBinder);
        }
        e8.recycle();
        return jVar;
    }
}
