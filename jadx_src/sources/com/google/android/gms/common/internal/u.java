package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u extends m {

    /* renamed from: g  reason: collision with root package name */
    final /* synthetic */ b f11866g;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public u(b bVar, int i8, Bundle bundle) {
        super(bVar, i8, null);
        this.f11866g = bVar;
    }

    @Override // com.google.android.gms.common.internal.m
    protected final void f(ConnectionResult connectionResult) {
        if (this.f11866g.r() && b.f0(this.f11866g)) {
            b.b0(this.f11866g, 16);
            return;
        }
        this.f11866g.f11835t.a(connectionResult);
        this.f11866g.J(connectionResult);
    }

    @Override // com.google.android.gms.common.internal.m
    protected final boolean g() {
        this.f11866g.f11835t.a(ConnectionResult.f11523e);
        return true;
    }
}
