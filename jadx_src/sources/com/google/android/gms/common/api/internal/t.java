package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.a;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ ConnectionResult f11695a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ u f11696b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(u uVar, ConnectionResult connectionResult) {
        this.f11696b = uVar;
        this.f11695a = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Map map;
        l6.b bVar;
        a.f fVar;
        a.f fVar2;
        a.f fVar3;
        a.f fVar4;
        u uVar = this.f11696b;
        map = uVar.f11702f.f11616m;
        bVar = uVar.f11698b;
        r rVar = (r) map.get(bVar);
        if (rVar == null) {
            return;
        }
        if (!this.f11695a.E0()) {
            rVar.H(this.f11695a, null);
            return;
        }
        this.f11696b.f11701e = true;
        fVar = this.f11696b.f11697a;
        if (fVar.m()) {
            this.f11696b.h();
            return;
        }
        try {
            u uVar2 = this.f11696b;
            fVar3 = uVar2.f11697a;
            fVar4 = uVar2.f11697a;
            fVar3.b(null, fVar4.a());
        } catch (SecurityException e8) {
            Log.e("GoogleApiManager", "Failed to get service from broker. ", e8);
            fVar2 = this.f11696b.f11697a;
            fVar2.c("Failed to get service from broker.");
            rVar.H(new ConnectionResult(10), null);
        }
    }
}
