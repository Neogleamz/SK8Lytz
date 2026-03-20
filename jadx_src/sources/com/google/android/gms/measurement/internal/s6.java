package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s6 implements Callable<List<pb>> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16968a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16969b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16970c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ j6 f16971d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s6(j6 j6Var, String str, String str2, String str3) {
        this.f16968a = str;
        this.f16969b = str2;
        this.f16970c = str3;
        this.f16971d = j6Var;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<pb> call() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16971d.f16700a;
        gbVar.p0();
        gbVar2 = this.f16971d.f16700a;
        return gbVar2.e0().w0(this.f16968a, this.f16969b, this.f16970c);
    }
}
