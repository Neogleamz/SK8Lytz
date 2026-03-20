package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class m extends q {

    /* renamed from: d  reason: collision with root package name */
    public final int f11852d;

    /* renamed from: e  reason: collision with root package name */
    public final Bundle f11853e;

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ b f11854f;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m(b bVar, int i8, Bundle bundle) {
        super(bVar, Boolean.TRUE);
        this.f11854f = bVar;
        this.f11852d = i8;
        this.f11853e = bundle;
    }

    @Override // com.google.android.gms.common.internal.q
    protected final /* bridge */ /* synthetic */ void a(Object obj) {
        if (this.f11852d != 0) {
            this.f11854f.g0(1, null);
            Bundle bundle = this.f11853e;
            f(new ConnectionResult(this.f11852d, bundle != null ? (PendingIntent) bundle.getParcelable("pendingIntent") : null));
        } else if (g()) {
        } else {
            this.f11854f.g0(1, null);
            f(new ConnectionResult(8, null));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.q
    public final void b() {
    }

    protected abstract void f(ConnectionResult connectionResult);

    protected abstract boolean g();
}
