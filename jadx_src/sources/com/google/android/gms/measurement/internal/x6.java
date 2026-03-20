package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzbf f17124a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17125b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f17126c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x6(j6 j6Var, zzbf zzbfVar, String str) {
        this.f17124a = zzbfVar;
        this.f17125b = str;
        this.f17126c = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f17126c.f16700a;
        gbVar.p0();
        gbVar2 = this.f17126c.f16700a;
        gbVar2.t(this.f17124a, this.f17125b);
    }
}
