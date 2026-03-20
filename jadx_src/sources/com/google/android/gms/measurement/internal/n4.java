package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n4<V> {

    /* renamed from: h  reason: collision with root package name */
    private static final Object f16814h = new Object();

    /* renamed from: a  reason: collision with root package name */
    private final String f16815a;

    /* renamed from: b  reason: collision with root package name */
    private final l4<V> f16816b;

    /* renamed from: c  reason: collision with root package name */
    private final V f16817c;

    /* renamed from: d  reason: collision with root package name */
    private final V f16818d;

    /* renamed from: e  reason: collision with root package name */
    private final Object f16819e;

    /* renamed from: f  reason: collision with root package name */
    private volatile V f16820f;

    /* renamed from: g  reason: collision with root package name */
    private volatile V f16821g;

    private n4(String str, V v8, V v9, l4<V> l4Var) {
        this.f16819e = new Object();
        this.f16820f = null;
        this.f16821g = null;
        this.f16815a = str;
        this.f16817c = v8;
        this.f16818d = v9;
        this.f16816b = l4Var;
    }

    public final V a(V v8) {
        synchronized (this.f16819e) {
        }
        if (v8 != null) {
            return v8;
        }
        if (o4.f16848a == null) {
            return this.f16817c;
        }
        synchronized (f16814h) {
            if (d.a()) {
                return this.f16821g == null ? this.f16817c : this.f16821g;
            }
            try {
                for (n4 n4Var : c0.K0()) {
                    if (d.a()) {
                        throw new IllegalStateException("Refreshing flag cache must be done on a worker thread.");
                    }
                    V v9 = null;
                    try {
                        l4<V> l4Var = n4Var.f16816b;
                        if (l4Var != null) {
                            v9 = l4Var.zza();
                        }
                    } catch (IllegalStateException unused) {
                    }
                    synchronized (f16814h) {
                        n4Var.f16821g = v9;
                    }
                }
            } catch (SecurityException unused2) {
            }
            l4<V> l4Var2 = this.f16816b;
            if (l4Var2 == null) {
                return this.f16817c;
            }
            try {
                return l4Var2.zza();
            } catch (IllegalStateException unused3) {
                return this.f16817c;
            } catch (SecurityException unused4) {
                return this.f16817c;
            }
        }
    }

    public final String b() {
        return this.f16815a;
    }
}
