package com.google.android.gms.signin.internal;

import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends a7.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    public final void k(zai zaiVar, h7.c cVar) {
        Parcel d8 = d();
        a7.c.c(d8, zaiVar);
        a7.c.d(d8, cVar);
        f(12, d8);
    }
}
