package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16641a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16642b = null;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16643c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ String f16644d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ boolean f16645e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ h7 f16646f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h8(h7 h7Var, AtomicReference atomicReference, String str, String str2, String str3, boolean z4) {
        this.f16641a = atomicReference;
        this.f16643c = str2;
        this.f16644d = str3;
        this.f16645e = z4;
        this.f16646f = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16646f.f16485a.H().R(this.f16641a, null, this.f16643c, this.f16644d, this.f16645e);
    }
}
