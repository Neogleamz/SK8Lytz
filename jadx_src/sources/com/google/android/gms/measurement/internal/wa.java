package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class wa {

    /* renamed from: a  reason: collision with root package name */
    private final u6.d f17094a;

    /* renamed from: b  reason: collision with root package name */
    private long f17095b;

    public wa(u6.d dVar) {
        n6.j.l(dVar);
        this.f17094a = dVar;
    }

    public final void a() {
        this.f17095b = 0L;
    }

    public final boolean b(long j8) {
        return this.f17095b == 0 || this.f17094a.b() - this.f17095b >= 3600000;
    }

    public final void c() {
        this.f17095b = this.f17094a.b();
    }
}
