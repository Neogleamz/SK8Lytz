package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f0 extends b0 {

    /* renamed from: c  reason: collision with root package name */
    public final c.a f11645c;

    public f0(c.a aVar, j7.k kVar) {
        super(4, kVar);
        this.f11645c = aVar;
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final /* bridge */ /* synthetic */ void d(j jVar, boolean z4) {
    }

    @Override // l6.q
    public final boolean f(r rVar) {
        l6.u uVar = (l6.u) rVar.x().get(this.f11645c);
        return uVar != null && uVar.f21779a.f();
    }

    @Override // l6.q
    public final Feature[] g(r rVar) {
        l6.u uVar = (l6.u) rVar.x().get(this.f11645c);
        if (uVar == null) {
            return null;
        }
        return uVar.f21779a.c();
    }

    @Override // com.google.android.gms.common.api.internal.b0
    public final void h(r rVar) {
        l6.u uVar = (l6.u) rVar.x().remove(this.f11645c);
        if (uVar == null) {
            this.f11621b.e(Boolean.FALSE);
            return;
        }
        uVar.f21780b.b(rVar.v(), this.f11621b);
        uVar.f21779a.a();
    }
}
