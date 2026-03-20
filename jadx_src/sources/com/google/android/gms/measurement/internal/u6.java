package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u6 implements Callable<List<zzac>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17021a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17022b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f17023c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ j6 f17024d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u6(j6 j6Var, String str, String str2, String str3) {
        this.f17021a = str;
        this.f17022b = str2;
        this.f17023c = str3;
        this.f17024d = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzac> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f17024d.f16700a;
        gbVar.p0();
        gbVar2 = this.f17024d.f16700a;
        return gbVar2.e0().P(this.f17021a, this.f17022b, this.f17023c);
    }
}
