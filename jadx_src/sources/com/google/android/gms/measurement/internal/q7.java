package com.google.android.gms.measurement.internal;

import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q7 implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ h7 f16903a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q7(h7 h7Var) {
        this.f16903a = h7Var;
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.f16903a.l().B(runnable);
    }
}
