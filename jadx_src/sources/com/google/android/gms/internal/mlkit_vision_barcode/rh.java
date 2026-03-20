package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class rh extends a implements th {
    /* JADX INFO: Access modifiers changed from: package-private */
    public rh(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.barcode.aidls.IBarcodeScannerCreator");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.th
    public final qh i1(x6.a aVar, zzvl zzvlVar) {
        qh qhVar;
        Parcel d8 = d();
        l0.b(d8, aVar);
        l0.a(d8, zzvlVar);
        Parcel e8 = e(1, d8);
        IBinder readStrongBinder = e8.readStrongBinder();
        if (readStrongBinder == null) {
            qhVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.mlkit.vision.barcode.aidls.IBarcodeScanner");
            qhVar = queryLocalInterface instanceof qh ? (qh) queryLocalInterface : new qh(readStrongBinder);
        }
        e8.recycle();
        return qhVar;
    }
}
