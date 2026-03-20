package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ boolean f16972a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16973b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s7(h7 h7Var, boolean z4) {
        this.f16972a = z4;
        this.f16973b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean n8 = this.f16973b.f16485a.n();
        boolean m8 = this.f16973b.f16485a.m();
        this.f16973b.f16485a.j(this.f16972a);
        if (m8 == this.f16972a) {
            this.f16973b.f16485a.i().I().b("Default data collection state already set to", Boolean.valueOf(this.f16972a));
        }
        if (this.f16973b.f16485a.n() == n8 || this.f16973b.f16485a.n() != this.f16973b.f16485a.m()) {
            this.f16973b.f16485a.i().K().c("Default data collection is different than actual status", Boolean.valueOf(this.f16972a), Boolean.valueOf(n8));
        }
        this.f16973b.u0();
    }
}
