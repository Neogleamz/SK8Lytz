package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzbf f17178a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f17179b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f17180c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y6(j6 j6Var, zzbf zzbfVar, zzn zznVar) {
        this.f17178a = zzbfVar;
        this.f17179b = zznVar;
        this.f17180c = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f17180c.x(this.f17180c.q(this.f17178a, this.f17179b), this.f17179b);
    }
}
