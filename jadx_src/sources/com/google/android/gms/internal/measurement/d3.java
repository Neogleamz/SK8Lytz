package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12136e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12137f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ boolean f12138g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ d2 f12139h;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ p2 f12140j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d3(p2 p2Var, String str, String str2, boolean z4, d2 d2Var) {
        super(p2Var);
        this.f12136e = str;
        this.f12137f = str2;
        this.f12138g = z4;
        this.f12139h = d2Var;
        this.f12140j = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12140j.f12429i;
        ((c2) n6.j.l(c2Var)).getUserProperties(this.f12136e, this.f12137f, this.f12138g, this.f12139h);
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    protected final void b() {
        this.f12139h.c(null);
    }
}
