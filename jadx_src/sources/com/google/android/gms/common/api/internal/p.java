package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ q f11678a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(q qVar) {
        this.f11678a = qVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.f fVar;
        a.f fVar2;
        r rVar = this.f11678a.f11679a;
        fVar = rVar.f11681b;
        fVar2 = rVar.f11681b;
        fVar.c(fVar2.getClass().getName().concat(" disconnecting because it was signed out."));
    }
}
