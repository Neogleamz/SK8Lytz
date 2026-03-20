package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16948a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16949b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r7(h7 h7Var, AtomicReference atomicReference) {
        this.f16948a = atomicReference;
        this.f16949b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.f16948a) {
            this.f16948a.set(Boolean.valueOf(this.f16949b.a().J(this.f16949b.n().D())));
            this.f16948a.notify();
        }
    }
}
