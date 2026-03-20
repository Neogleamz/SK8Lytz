package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f16439a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16440b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c8(h7 h7Var, long j8) {
        this.f16439a = j8;
        this.f16440b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16440b.C(this.f16439a, true);
        this.f16440b.r().O(new AtomicReference<>());
    }
}
