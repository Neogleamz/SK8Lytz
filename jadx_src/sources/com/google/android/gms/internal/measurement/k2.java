package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k2 extends w0 implements i2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public k2(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
    }

    @Override // com.google.android.gms.internal.measurement.i2
    public final void g1(String str, String str2, Bundle bundle, long j8) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.d(d8, bundle);
        d8.writeLong(j8);
        f(1, d8);
    }

    @Override // com.google.android.gms.internal.measurement.i2
    public final int zza() {
        Parcel e8 = e(2, d());
        int readInt = e8.readInt();
        e8.recycle();
        return readInt;
    }
}
