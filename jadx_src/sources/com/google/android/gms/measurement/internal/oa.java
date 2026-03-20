package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class oa implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f16863a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ na f16864b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public oa(na naVar, long j8) {
        this.f16863a = j8;
        this.f16864b = naVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        na.A(this.f16864b, this.f16863a);
    }
}
