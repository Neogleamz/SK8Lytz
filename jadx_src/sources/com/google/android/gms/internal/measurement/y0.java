package com.google.android.gms.internal.measurement;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y0 extends Binder implements IInterface {
    /* JADX INFO: Access modifiers changed from: protected */
    public y0(String str) {
        attachInterface(this, str);
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    protected boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        throw null;
    }

    @Override // android.os.Binder
    public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
        boolean z4;
        if (i8 > 16777215) {
            z4 = super.onTransact(i8, parcel, parcel2, i9);
        } else {
            parcel.enforceInterface(getInterfaceDescriptor());
            z4 = false;
        }
        if (z4) {
            return true;
        }
        return d(i8, parcel, parcel2, i9);
    }
}
