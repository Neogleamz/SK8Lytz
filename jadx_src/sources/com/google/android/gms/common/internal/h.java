package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h extends a7.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ISignInButtonCreator");
    }

    public final x6.a k(x6.a aVar, zax zaxVar) {
        Parcel d8 = d();
        a7.c.d(d8, aVar);
        a7.c.c(d8, zaxVar);
        Parcel e8 = e(2, d8);
        x6.a e9 = a.AbstractBinderC0227a.e(e8.readStrongBinder());
        e8.recycle();
        return e9;
    }
}
