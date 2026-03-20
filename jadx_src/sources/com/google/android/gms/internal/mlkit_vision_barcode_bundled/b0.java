package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b0 extends p implements c0 {
    public b0() {
        super("com.google.mlkit.vision.barcode.aidls.IBarcodeScannerCreator");
    }

    public static c0 asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.mlkit.vision.barcode.aidls.IBarcodeScannerCreator");
        return queryLocalInterface instanceof c0 ? (c0) queryLocalInterface : new a0(iBinder);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            n0.b(parcel);
            z newBarcodeScanner = newBarcodeScanner(a.AbstractBinderC0227a.e(parcel.readStrongBinder()), (zzbc) n0.a(parcel, zzbc.CREATOR));
            parcel2.writeNoException();
            parcel2.writeStrongBinder(newBarcodeScanner == null ? null : newBarcodeScanner.asBinder());
            return true;
        }
        return false;
    }
}
