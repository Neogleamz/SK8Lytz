package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12518e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12519f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ Bundle f12520g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ p2 f12521h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public t2(p2 p2Var, String str, String str2, Bundle bundle) {
        super(p2Var);
        this.f12518e = str;
        this.f12519f = str2;
        this.f12520g = bundle;
        this.f12521h = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12521h.f12429i;
        ((c2) n6.j.l(c2Var)).clearConditionalUserProperty(this.f12518e, this.f12519f, this.f12520g);
    }
}
