package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.internal.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u4 extends com.google.android.gms.common.internal.b<f7.d> {
    public u4(Context context, Looper looper, b.a aVar, b.InterfaceC0125b interfaceC0125b) {
        super(context, looper, 93, aVar, interfaceC0125b, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.b
    public final String C() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }

    @Override // com.google.android.gms.common.internal.b
    protected final String D() {
        return "com.google.android.gms.measurement.START";
    }

    @Override // com.google.android.gms.common.internal.b, com.google.android.gms.common.api.a.f
    public final int j() {
        return com.google.android.gms.common.d.f11721a;
    }

    @Override // com.google.android.gms.common.internal.b
    public final /* synthetic */ f7.d q(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
        return queryLocalInterface instanceof f7.d ? (f7.d) queryLocalInterface : new p4(iBinder);
    }
}
