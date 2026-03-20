package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.internal.b;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements b.c, l6.a0 {

    /* renamed from: a  reason: collision with root package name */
    private final a.f f11697a;

    /* renamed from: b  reason: collision with root package name */
    private final l6.b f11698b;

    /* renamed from: c  reason: collision with root package name */
    private com.google.android.gms.common.internal.e f11699c = null;

    /* renamed from: d  reason: collision with root package name */
    private Set f11700d = null;

    /* renamed from: e  reason: collision with root package name */
    private boolean f11701e = false;

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ b f11702f;

    public u(b bVar, a.f fVar, l6.b bVar2) {
        this.f11702f = bVar;
        this.f11697a = fVar;
        this.f11698b = bVar2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void h() {
        com.google.android.gms.common.internal.e eVar;
        if (!this.f11701e || (eVar = this.f11699c) == null) {
            return;
        }
        this.f11697a.b(eVar, this.f11700d);
    }

    @Override // com.google.android.gms.common.internal.b.c
    public final void a(ConnectionResult connectionResult) {
        Handler handler;
        handler = this.f11702f.f11619t;
        handler.post(new t(this, connectionResult));
    }

    @Override // l6.a0
    public final void b(ConnectionResult connectionResult) {
        Map map;
        map = this.f11702f.f11616m;
        r rVar = (r) map.get(this.f11698b);
        if (rVar != null) {
            rVar.I(connectionResult);
        }
    }

    @Override // l6.a0
    public final void c(com.google.android.gms.common.internal.e eVar, Set set) {
        if (eVar == null || set == null) {
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
            b(new ConnectionResult(4));
            return;
        }
        this.f11699c = eVar;
        this.f11700d = set;
        h();
    }
}
