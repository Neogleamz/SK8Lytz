package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class la implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ gb f16769a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ Runnable f16770b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public la(ka kaVar, gb gbVar, Runnable runnable) {
        this.f16769a = gbVar;
        this.f16770b = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16769a.p0();
        this.f16769a.x(this.f16770b);
        this.f16769a.u0();
    }
}
