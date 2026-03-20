package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ d2 f12154e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2 f12155f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e3(p2 p2Var, d2 d2Var) {
        super(p2Var);
        this.f12154e = d2Var;
        this.f12155f = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12155f.f12429i;
        ((c2) n6.j.l(c2Var)).getCurrentScreenClass(this.f12154e);
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    protected final void b() {
        this.f12154e.c(null);
    }
}
