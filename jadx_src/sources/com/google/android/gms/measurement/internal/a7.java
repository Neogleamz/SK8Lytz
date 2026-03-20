package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a7 implements Callable<byte[]> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzbf f16311a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16312b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ j6 f16313c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a7(j6 j6Var, zzbf zzbfVar, String str) {
        this.f16311a = zzbfVar;
        this.f16312b = str;
        this.f16313c = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ byte[] call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16313c.f16700a;
        gbVar.p0();
        gbVar2 = this.f16313c.f16700a;
        return gbVar2.k0().w(this.f16311a, this.f16312b);
    }
}
