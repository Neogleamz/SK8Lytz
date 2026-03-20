package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16763a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16764b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l8(h7 h7Var, AtomicReference atomicReference) {
        this.f16763a = atomicReference;
        this.f16764b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16763a) {
            this.f16763a.set(Integer.valueOf(this.f16764b.a().t(this.f16764b.n().D(), c0.P)));
            this.f16763a.notify();
        }
    }
}
