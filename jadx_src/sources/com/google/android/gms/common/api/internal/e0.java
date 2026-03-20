package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Status;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e0 extends l6.q {

    /* renamed from: b  reason: collision with root package name */
    private final g f11632b;

    /* renamed from: c  reason: collision with root package name */
    private final j7.k f11633c;

    /* renamed from: d  reason: collision with root package name */
    private final l6.j f11634d;

    public e0(int i8, g gVar, j7.k kVar, l6.j jVar) {
        super(i8);
        this.f11633c = kVar;
        this.f11632b = gVar;
        this.f11634d = jVar;
        if (i8 == 2 && gVar.c()) {
            throw new IllegalArgumentException("Best-effort write calls cannot pass methods that should auto-resolve missing features.");
        }
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void a(Status status) {
        this.f11633c.d(this.f11634d.a(status));
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void b(Exception exc) {
        this.f11633c.d(exc);
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void c(r rVar) {
        try {
            this.f11632b.b(rVar.v(), this.f11633c);
        } catch (DeadObjectException e8) {
            throw e8;
        } catch (RemoteException e9) {
            a(g0.e(e9));
        } catch (RuntimeException e10) {
            this.f11633c.d(e10);
        }
    }

    @Override // com.google.android.gms.common.api.internal.g0
    public final void d(j jVar, boolean z4) {
        jVar.b(this.f11633c, z4);
    }

    @Override // l6.q
    public final boolean f(r rVar) {
        return this.f11632b.c();
    }

    @Override // l6.q
    public final Feature[] g(r rVar) {
        return this.f11632b.e();
    }
}
