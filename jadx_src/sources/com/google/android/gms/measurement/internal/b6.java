package com.google.android.gms.measurement.internal;

import java.lang.Thread;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b6 implements Thread.UncaughtExceptionHandler {

    /* renamed from: a  reason: collision with root package name */
    private final String f16337a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ a6 f16338b;

    public b6(a6 a6Var, String str) {
        this.f16338b = a6Var;
        n6.j.l(str);
        this.f16337a = str;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.f16338b.i().E().b(this.f16337a, th);
    }
}
