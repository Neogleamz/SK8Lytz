package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16458a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16459b = null;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16460c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ String f16461d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h7 f16462e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d8(h7 h7Var, AtomicReference atomicReference, String str, String str2, String str3) {
        this.f16458a = atomicReference;
        this.f16460c = str2;
        this.f16461d = str3;
        this.f16462e = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16462e.f16485a.H().Q(this.f16458a, null, this.f16460c, this.f16461d);
    }
}
