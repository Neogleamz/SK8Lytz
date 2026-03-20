package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class pa implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f16883a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ na f16884b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public pa(na naVar, long j8) {
        this.f16883a = j8;
        this.f16884b = naVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        na.G(this.f16884b, this.f16883a);
    }
}
