package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ca implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ f7.d f16444a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ ba f16445b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ca(ba baVar, f7.d dVar) {
        this.f16444a = dVar;
        this.f16445b = baVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16445b) {
            this.f16445b.f16348a = false;
            if (!this.f16445b.f16350c.a0()) {
                this.f16445b.f16350c.i().D().a("Connected to remote service");
                this.f16445b.f16350c.L(this.f16444a);
            }
        }
    }
}
