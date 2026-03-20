package com.google.android.gms.common.moduleinstall.internal;

import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends a7.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.moduleinstall.internal.IModuleInstallService");
    }

    public final void k(r6.e eVar, ApiFeatureRequest apiFeatureRequest) {
        Parcel d8 = d();
        a7.c.d(d8, eVar);
        a7.c.c(d8, apiFeatureRequest);
        f(1, d8);
    }

    public final void q(r6.e eVar, ApiFeatureRequest apiFeatureRequest, r6.g gVar) {
        Parcel d8 = d();
        a7.c.d(d8, eVar);
        a7.c.c(d8, apiFeatureRequest);
        a7.c.d(d8, gVar);
        f(2, d8);
    }

    public final void v(l6.d dVar, r6.g gVar) {
        Parcel d8 = d();
        a7.c.d(d8, dVar);
        a7.c.d(d8, gVar);
        f(6, d8);
    }
}
