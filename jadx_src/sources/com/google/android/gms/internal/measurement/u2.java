package com.google.android.gms.internal.measurement;

import android.app.Activity;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Activity f12545e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12546f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ String f12547g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ p2 f12548h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public u2(p2 p2Var, Activity activity, String str, String str2) {
        super(p2Var);
        this.f12545e = activity;
        this.f12546f = str;
        this.f12547g = str2;
        this.f12548h = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12548h.f12429i;
        ((c2) n6.j.l(c2Var)).setCurrentScreen(x6.b.g(this.f12545e), this.f12546f, this.f12547g, this.f12430a);
    }
}
