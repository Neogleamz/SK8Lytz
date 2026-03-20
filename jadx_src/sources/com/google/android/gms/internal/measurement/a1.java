package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a1 extends w0 implements z0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public a1(IBinder iBinder) {
        super(iBinder, "com.google.android.finsky.externalreferrer.IGetInstallReferrerService");
    }

    @Override // com.google.android.gms.internal.measurement.z0
    public final Bundle c(Bundle bundle) {
        Parcel d8 = d();
        x0.d(d8, bundle);
        Parcel e8 = e(1, d8);
        Bundle bundle2 = (Bundle) x0.a(e8, Bundle.CREATOR);
        e8.recycle();
        return bundle2;
    }
}
