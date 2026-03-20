package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Boolean f16834a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16835b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n8(h7 h7Var, Boolean bool) {
        this.f16834a = bool;
        this.f16835b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16835b.R(this.f16834a, true);
    }
}
