package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f17230a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f17231b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z7(h7 h7Var, AtomicReference atomicReference) {
        this.f17230a = atomicReference;
        this.f17231b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f17230a) {
            this.f17230a.set(this.f17231b.a().F(this.f17231b.n().D()));
            this.f17230a.notify();
        }
    }
}
