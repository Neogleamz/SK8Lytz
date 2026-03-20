package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Bundle f17186a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ x8 f17187b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ x8 f17188c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ long f17189d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ z8 f17190e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y8(z8 z8Var, Bundle bundle, x8 x8Var, x8 x8Var2, long j8) {
        this.f17186a = bundle;
        this.f17187b = x8Var;
        this.f17188c = x8Var2;
        this.f17189d = j8;
        this.f17190e = z8Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        z8.J(this.f17190e, this.f17186a, this.f17187b, this.f17188c, this.f17189d);
    }
}
