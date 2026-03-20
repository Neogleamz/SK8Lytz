package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class aa implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ f7.d f16316a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ ba f16317b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public aa(ba baVar, f7.d dVar) {
        this.f16316a = dVar;
        this.f16317b = baVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16317b) {
            this.f16317b.f16348a = false;
            if (!this.f16317b.f16350c.a0()) {
                this.f16317b.f16350c.i().I().a("Connected to service");
                this.f16317b.f16350c.L(this.f16316a);
            }
        }
    }
}
