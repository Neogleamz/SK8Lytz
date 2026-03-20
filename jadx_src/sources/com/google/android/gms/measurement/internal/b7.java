package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b7 implements Callable<List<pb>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16339a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f16340b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b7(j6 j6Var, String str) {
        this.f16339a = str;
        this.f16340b = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<pb> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16340b.f16700a;
        gbVar.p0();
        gbVar2 = this.f16340b.f16700a;
        return gbVar2.e0().L0(this.f16339a);
    }
}
