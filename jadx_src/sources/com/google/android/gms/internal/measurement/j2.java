package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j2 extends w0 implements h2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j2(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.api.internal.IBundleReceiver");
    }

    @Override // com.google.android.gms.internal.measurement.h2
    public final void c(Bundle bundle) {
        Parcel d8 = d();
        x0.d(d8, bundle);
        f(1, d8);
    }
}
