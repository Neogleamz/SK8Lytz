package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16727a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f16728b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k6(j6 j6Var, zzn zznVar) {
        this.f16727a = zznVar;
        this.f16728b = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16728b.f16700a;
        gbVar.p0();
        gbVar2 = this.f16728b.f16700a;
        gbVar2.Z(this.f16727a);
    }
}
