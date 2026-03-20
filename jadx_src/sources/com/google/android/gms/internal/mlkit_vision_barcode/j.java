package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j extends a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetector");
    }

    public final void b() {
        f(3, d());
    }

    public final zzu[] g(x6.a aVar, zzan zzanVar) {
        Parcel d8 = d();
        l0.b(d8, aVar);
        l0.a(d8, zzanVar);
        Parcel e8 = e(1, d8);
        zzu[] zzuVarArr = (zzu[]) e8.createTypedArray(zzu.CREATOR);
        e8.recycle();
        return zzuVarArr;
    }

    public final zzu[] k(x6.a aVar, zzan zzanVar) {
        Parcel d8 = d();
        l0.b(d8, aVar);
        l0.a(d8, zzanVar);
        Parcel e8 = e(2, d8);
        zzu[] zzuVarArr = (zzu[]) e8.createTypedArray(zzu.CREATOR);
        e8.recycle();
        return zzuVarArr;
    }
}
