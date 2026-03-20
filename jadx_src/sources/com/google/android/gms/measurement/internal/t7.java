package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t7 implements com.google.common.util.concurrent.a<Object> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzmv f17001a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f17002b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t7(h7 h7Var, zzmv zzmvVar) {
        this.f17001a = zzmvVar;
        this.f17002b = h7Var;
    }

    @Override // com.google.common.util.concurrent.a
    public final void c(Object obj) {
        this.f17002b.k();
        this.f17002b.f16631i = false;
        this.f17002b.r0();
        this.f17002b.i().D().b("registerTriggerAsync ran. uri", this.f17001a.f17285a);
    }

    @Override // com.google.common.util.concurrent.a
    public final void onFailure(Throwable th) {
        this.f17002b.k();
        this.f17002b.f16631i = false;
        this.f17002b.r0();
        this.f17002b.i().E().b("registerTriggerAsync failed with throwable", th);
    }
}
