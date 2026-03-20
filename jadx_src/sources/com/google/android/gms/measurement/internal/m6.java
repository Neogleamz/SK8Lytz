package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzac f16788a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f16789b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f16790c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m6(j6 j6Var, zzac zzacVar, zzn zznVar) {
        this.f16788a = zzacVar;
        this.f16789b = zznVar;
        this.f16790c = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gb gbVar3;
        gbVar = this.f16790c.f16700a;
        gbVar.p0();
        if (this.f16788a.f17252c.t() == null) {
            gbVar3 = this.f16790c.f16700a;
            gbVar3.r(this.f16788a, this.f16789b);
            return;
        }
        gbVar2 = this.f16790c.f16700a;
        gbVar2.T(this.f16788a, this.f16789b);
    }
}
