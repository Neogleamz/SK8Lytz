package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzno f17227a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f17228b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f17229c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z6(j6 j6Var, zzno zznoVar, zzn zznVar) {
        this.f17227a = zznoVar;
        this.f17228b = zznVar;
        this.f17229c = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gb gbVar3;
        gbVar = this.f17229c.f16700a;
        gbVar.p0();
        if (this.f17227a.t() == null) {
            gbVar3 = this.f17229c.f16700a;
            gbVar3.E(this.f17227a.f17308b, this.f17228b);
            return;
        }
        gbVar2 = this.f17229c.f16700a;
        gbVar2.w(this.f17227a, this.f17228b);
    }
}
