package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ga implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ ba f16562a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ga(ba baVar) {
        this.f16562a = baVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f9.I(this.f16562a.f16350c, new ComponentName(this.f16562a.f16350c.zza(), "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
