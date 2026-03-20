package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class fb implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ ob f16536a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ gb f16537b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public fb(gb gbVar, ob obVar) {
        this.f16536a = obVar;
        this.f16537b = gbVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb.v(this.f16537b, this.f16536a);
        this.f16537b.t0();
    }
}
