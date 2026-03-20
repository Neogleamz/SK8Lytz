package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o6 implements Callable<List<pb>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16851a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16852b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16853c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ j6 f16854d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o6(j6 j6Var, String str, String str2, String str3) {
        this.f16851a = str;
        this.f16852b = str2;
        this.f16853c = str3;
        this.f16854d = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<pb> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16854d.f16700a;
        gbVar.p0();
        gbVar2 = this.f16854d.f16700a;
        return gbVar2.e0().w0(this.f16851a, this.f16852b, this.f16853c);
    }
}
