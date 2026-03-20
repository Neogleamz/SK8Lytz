package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12647e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2 f12648f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x2(p2 p2Var, String str) {
        super(p2Var);
        this.f12647e = str;
        this.f12648f = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12648f.f12429i;
        ((c2) n6.j.l(c2Var)).endAdUnitExposure(this.f12647e, this.f12431b);
    }
}
