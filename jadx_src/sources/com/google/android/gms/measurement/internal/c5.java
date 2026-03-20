package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c5 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final d5 f16426a;

    /* renamed from: b  reason: collision with root package name */
    private final int f16427b;

    /* renamed from: c  reason: collision with root package name */
    private final Throwable f16428c;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f16429d;

    /* renamed from: e  reason: collision with root package name */
    private final String f16430e;

    /* renamed from: f  reason: collision with root package name */
    private final Map<String, List<String>> f16431f;

    private c5(String str, d5 d5Var, int i8, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        n6.j.l(d5Var);
        this.f16426a = d5Var;
        this.f16427b = i8;
        this.f16428c = th;
        this.f16429d = bArr;
        this.f16430e = str;
        this.f16431f = map;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16426a.a(this.f16430e, this.f16427b, this.f16428c, this.f16429d, this.f16431f);
    }
}
