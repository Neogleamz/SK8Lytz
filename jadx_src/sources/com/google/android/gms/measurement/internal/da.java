package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class da implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ ComponentName f16465a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ ba f16466b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public da(ba baVar, ComponentName componentName) {
        this.f16465a = componentName;
        this.f16466b = baVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f9.I(this.f16466b.f16350c, this.f16465a);
    }
}
