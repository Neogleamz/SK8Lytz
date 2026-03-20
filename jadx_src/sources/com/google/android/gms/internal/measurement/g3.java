package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12196e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ d2 f12197f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ p2 f12198g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g3(p2 p2Var, String str, d2 d2Var) {
        super(p2Var);
        this.f12196e = str;
        this.f12197f = d2Var;
        this.f12198g = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12198g.f12429i;
        ((c2) n6.j.l(c2Var)).getMaxUserProperties(this.f12196e, this.f12197f);
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    protected final void b() {
        this.f12197f.c(null);
    }
}
