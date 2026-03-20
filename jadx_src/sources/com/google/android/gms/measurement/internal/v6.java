package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v6 implements Callable<zzal> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f17049a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f17050b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v6(j6 j6Var, zzn zznVar) {
        this.f17049a = zznVar;
        this.f17050b = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ zzal call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f17050b.f16700a;
        gbVar.p0();
        gbVar2 = this.f17050b.f16700a;
        return new zzal(gbVar2.d(this.f17049a.f17288a));
    }
}
