package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q6 implements Callable<List<zzac>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16899a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16900b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16901c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ j6 f16902d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q6(j6 j6Var, String str, String str2, String str3) {
        this.f16899a = str;
        this.f16900b = str2;
        this.f16901c = str3;
        this.f16902d = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzac> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16902d.f16700a;
        gbVar.p0();
        gbVar2 = this.f16902d.f16700a;
        return gbVar2.e0().P(this.f16899a, this.f16900b, this.f16901c);
    }
}
