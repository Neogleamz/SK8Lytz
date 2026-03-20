package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16999a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f17000b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t6(j6 j6Var, zzn zznVar) {
        this.f16999a = zznVar;
        this.f17000b = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f17000b.f16700a;
        gbVar.p0();
        gbVar2 = this.f17000b.f16700a;
        gbVar2.b0(this.f16999a);
    }
}
