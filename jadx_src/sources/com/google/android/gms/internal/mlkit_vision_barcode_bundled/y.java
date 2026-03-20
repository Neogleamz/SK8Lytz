package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import java.util.List;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class y extends p implements z {
    public y() {
        super("com.google.mlkit.vision.barcode.aidls.IBarcodeScanner");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            a();
        } else if (i8 != 2) {
            if (i8 != 3) {
                return false;
            }
            n0.b(parcel);
            List d22 = d2(a.AbstractBinderC0227a.e(parcel.readStrongBinder()), (zzbu) n0.a(parcel, zzbu.CREATOR));
            parcel2.writeNoException();
            parcel2.writeTypedList(d22);
            return true;
        } else {
            b();
        }
        parcel2.writeNoException();
        return true;
    }
}
