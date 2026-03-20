package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f3 extends p2.a {

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12173f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ Object f12174g;

    /* renamed from: k  reason: collision with root package name */
    private final /* synthetic */ p2 f12177k;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ int f12172e = 5;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ Object f12175h = null;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ Object f12176j = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f3(p2 p2Var, boolean z4, int i8, String str, Object obj, Object obj2, Object obj3) {
        super(false);
        this.f12173f = str;
        this.f12174g = obj;
        this.f12177k = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12177k.f12429i;
        ((c2) n6.j.l(c2Var)).logHealthData(this.f12172e, this.f12173f, x6.b.g(this.f12174g), x6.b.g(null), x6.b.g(null));
    }
}
