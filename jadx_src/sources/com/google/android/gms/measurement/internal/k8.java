package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16731a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16732b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k8(h7 h7Var, AtomicReference atomicReference) {
        this.f16731a = atomicReference;
        this.f16732b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16731a) {
            this.f16731a.set(Double.valueOf(this.f16732b.a().m(this.f16732b.n().D(), c0.Q)));
            this.f16731a.notify();
        }
    }
}
