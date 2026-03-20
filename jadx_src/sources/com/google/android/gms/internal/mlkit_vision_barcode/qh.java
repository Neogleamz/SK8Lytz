package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IBinder;
import android.os.Parcel;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class qh extends a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public qh(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.barcode.aidls.IBarcodeScanner");
    }

    public final List g(x6.a aVar, zzwc zzwcVar) {
        Parcel d8 = d();
        l0.b(d8, aVar);
        l0.a(d8, zzwcVar);
        Parcel e8 = e(3, d8);
        ArrayList createTypedArrayList = e8.createTypedArrayList(zzvj.CREATOR);
        e8.recycle();
        return createTypedArrayList;
    }

    public final void k() {
        f(1, d());
    }

    public final void q() {
        f(2, d());
    }
}
