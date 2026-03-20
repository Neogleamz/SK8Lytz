package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16827a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16828b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16829c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ long f16830d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ j6 f16831e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n6(j6 j6Var, String str, String str2, String str3, long j8) {
        this.f16827a = str;
        this.f16828b = str2;
        this.f16829c = str3;
        this.f16830d = j8;
        this.f16831e = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        String str = this.f16827a;
        if (str == null) {
            gbVar2 = this.f16831e.f16700a;
            gbVar2.D(this.f16828b, null);
            return;
        }
        x8 x8Var = new x8(this.f16829c, str, this.f16830d);
        gbVar = this.f16831e.f16700a;
        gbVar.D(this.f16828b, x8Var);
    }
}
