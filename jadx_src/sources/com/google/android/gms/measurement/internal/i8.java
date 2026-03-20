package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16679a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16680b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i8(h7 h7Var, AtomicReference atomicReference) {
        this.f16679a = atomicReference;
        this.f16680b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16679a) {
            this.f16679a.set(Long.valueOf(this.f16680b.a().x(this.f16680b.n().D(), c0.O)));
            this.f16679a.notify();
        }
    }
}
