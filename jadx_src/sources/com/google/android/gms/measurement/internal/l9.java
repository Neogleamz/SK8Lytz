package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16765a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ boolean f16766b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ zzno f16767c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ f9 f16768d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l9(f9 f9Var, zzn zznVar, boolean z4, zzno zznoVar) {
        this.f16765a = zznVar;
        this.f16766b = z4;
        this.f16767c = zznoVar;
        this.f16768d = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f16768d.f16529d;
        if (dVar == null) {
            this.f16768d.i().E().a("Discarding data. Failed to set user property");
            return;
        }
        n6.j.l(this.f16765a);
        this.f16768d.M(dVar, this.f16766b ? null : this.f16767c, this.f16765a);
        this.f16768d.f0();
    }
}
