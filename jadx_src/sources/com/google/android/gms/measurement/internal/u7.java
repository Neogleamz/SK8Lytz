package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f17025a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f17026b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u7(h7 h7Var, long j8) {
        this.f17025a = j8;
        this.f17026b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f17026b.f().f16611m.b(this.f17025a);
        this.f17026b.i().D().b("Session timeout duration set", Long.valueOf(this.f17025a));
    }
}
