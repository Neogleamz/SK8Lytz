package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class kb implements Callable<String> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16736a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ gb f16737b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public kb(gb gbVar, zzn zznVar) {
        this.f16736a = zznVar;
        this.f16737b = gbVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ String call() {
        z4 I;
        String str;
        if (this.f16737b.Q((String) n6.j.l(this.f16736a.f17288a)).B() && zziq.q(this.f16736a.B).B()) {
            y3 f5 = this.f16737b.f(this.f16736a);
            if (f5 != null) {
                return f5.i();
            }
            I = this.f16737b.i().J();
            str = "App info was null when attempting to get app instance id";
        } else {
            I = this.f16737b.i().I();
            str = "Analytics storage consent denied. Returning null app instance id";
        }
        I.a(str);
        return null;
    }
}
