package com.google.android.gms.internal.measurement;

import android.app.Activity;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Activity f12435e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2.b f12436f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p3(p2.b bVar, Activity activity) {
        super(p2.this);
        this.f12435e = activity;
        this.f12436f = bVar;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = p2.this.f12429i;
        ((c2) n6.j.l(c2Var)).onActivityStopped(x6.b.g(this.f12435e), this.f12431b);
    }
}
