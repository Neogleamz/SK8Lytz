package com.google.android.gms.common.api.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.b;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q implements b.e {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ r f11679a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(r rVar) {
        this.f11679a = rVar;
    }

    @Override // com.google.android.gms.common.internal.b.e
    public final void a() {
        Handler handler;
        handler = this.f11679a.f11692n.f11619t;
        handler.post(new p(this));
    }
}
