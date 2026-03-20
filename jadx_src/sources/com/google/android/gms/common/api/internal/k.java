package com.google.android.gms.common.api.internal;

import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.util.VisibleForTesting;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends k0 {

    /* renamed from: f  reason: collision with root package name */
    private final k0.b f11665f;

    /* renamed from: g  reason: collision with root package name */
    private final b f11666g;

    @VisibleForTesting
    k(l6.f fVar, b bVar, com.google.android.gms.common.a aVar) {
        super(fVar, aVar);
        this.f11665f = new k0.b();
        this.f11666g = bVar;
        this.f11595a.a("ConnectionlessLifecycleHelper", this);
    }

    public static void u(Activity activity, b bVar, l6.b bVar2) {
        l6.f c9 = LifecycleCallback.c(activity);
        k kVar = (k) c9.b("ConnectionlessLifecycleHelper", k.class);
        if (kVar == null) {
            kVar = new k(c9, bVar, com.google.android.gms.common.a.m());
        }
        n6.j.m(bVar2, "ApiKey cannot be null");
        kVar.f11665f.add(bVar2);
        bVar.c(kVar);
    }

    private final void v() {
        if (this.f11665f.isEmpty()) {
            return;
        }
        this.f11666g.c(this);
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public final void h() {
        super.h();
        v();
    }

    @Override // com.google.android.gms.common.api.internal.k0, com.google.android.gms.common.api.internal.LifecycleCallback
    public final void j() {
        super.j();
        v();
    }

    @Override // com.google.android.gms.common.api.internal.k0, com.google.android.gms.common.api.internal.LifecycleCallback
    public final void k() {
        super.k();
        this.f11666g.d(this);
    }

    @Override // com.google.android.gms.common.api.internal.k0
    protected final void m(ConnectionResult connectionResult, int i8) {
        this.f11666g.H(connectionResult, i8);
    }

    @Override // com.google.android.gms.common.api.internal.k0
    protected final void n() {
        this.f11666g.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final k0.b t() {
        return this.f11665f;
    }
}
