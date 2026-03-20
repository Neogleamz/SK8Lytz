package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Bundle f12448e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2 f12449f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q2(p2 p2Var, Bundle bundle) {
        super(p2Var);
        this.f12448e = bundle;
        this.f12449f = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12449f.f12429i;
        ((c2) n6.j.l(c2Var)).setConditionalUserProperty(this.f12448e, this.f12430a);
    }
}
