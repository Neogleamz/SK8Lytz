package com.google.android.gms.common.api.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ int f11676a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ r f11677b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(r rVar, int i8) {
        this.f11677b = rVar;
        this.f11676a = i8;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f11677b.k(this.f11676a);
    }
}
