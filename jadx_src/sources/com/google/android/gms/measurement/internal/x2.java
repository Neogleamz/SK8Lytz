package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x2 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f17108a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ a f17109b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x2(a aVar, long j8) {
        this.f17108a = j8;
        this.f17109b = aVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f17109b.z(this.f17108a);
    }
}
