package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 extends b0 {

    /* renamed from: c  reason: collision with root package name */
    public final l6.u f11627c;

    public d0(l6.u uVar, j7.k kVar) {
        super(3, kVar);
        this.f11627c = uVar;
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final /* bridge */ /* synthetic */ void d(j jVar, boolean z4) {
    }

    @Override // l6.q
    public final boolean f(r rVar) {
        return this.f11627c.f21779a.f();
    }

    @Override // l6.q
    public final Feature[] g(r rVar) {
        return this.f11627c.f21779a.c();
    }

    @Override // com.google.android.gms.common.api.internal.b0
    public final void h(r rVar) {
        this.f11627c.f21779a.d(rVar.v(), this.f11621b);
        c.a b9 = this.f11627c.f21779a.b();
        if (b9 != null) {
            rVar.x().put(b9, this.f11627c);
        }
    }
}
