package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12469e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12470f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ Object f12471g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ boolean f12472h;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ p2 f12473j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r2(p2 p2Var, String str, String str2, Object obj, boolean z4) {
        super(p2Var);
        this.f12469e = str;
        this.f12470f = str2;
        this.f12471g = obj;
        this.f12472h = z4;
        this.f12473j = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12473j.f12429i;
        ((c2) n6.j.l(c2Var)).setUserProperty(this.f12469e, this.f12470f, x6.b.g(this.f12471g), this.f12472h, this.f12430a);
    }
}
