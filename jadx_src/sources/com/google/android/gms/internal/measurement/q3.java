package com.google.android.gms.internal.measurement;

import android.app.Activity;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Activity f12450e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2.b f12451f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q3(p2.b bVar, Activity activity) {
        super(p2.this);
        this.f12450e = activity;
        this.f12451f = bVar;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = p2.this.f12429i;
        ((c2) n6.j.l(c2Var)).onActivityDestroyed(x6.b.g(this.f12450e), this.f12431b);
    }
}
