package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d3 {

    /* renamed from: c  reason: collision with root package name */
    private static final b2 f14741c = b2.f14728b;

    /* renamed from: a  reason: collision with root package name */
    protected volatile x3 f14742a;

    /* renamed from: b  reason: collision with root package name */
    private volatile zzdb f14743b;

    public final int a() {
        if (this.f14743b != null) {
            return ((m1) this.f14743b).f14809e.length;
        }
        if (this.f14742a != null) {
            return this.f14742a.b();
        }
        return 0;
    }

    public final zzdb b() {
        if (this.f14743b != null) {
            return this.f14743b;
        }
        synchronized (this) {
            if (this.f14743b != null) {
                return this.f14743b;
            }
            this.f14743b = this.f14742a == null ? zzdb.f14977b : this.f14742a.c();
            return this.f14743b;
        }
    }

    public final x3 c(x3 x3Var) {
        x3 x3Var2 = this.f14742a;
        this.f14743b = null;
        this.f14742a = x3Var;
        return x3Var2;
    }

    protected final void d(x3 x3Var) {
        if (this.f14742a != null) {
            return;
        }
        synchronized (this) {
            if (this.f14742a == null) {
                try {
                    this.f14742a = x3Var;
                    this.f14743b = zzdb.f14977b;
                } catch (zzeo unused) {
                    this.f14742a = x3Var;
                    this.f14743b = zzdb.f14977b;
                }
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof d3) {
            d3 d3Var = (d3) obj;
            x3 x3Var = this.f14742a;
            x3 x3Var2 = d3Var.f14742a;
            if (x3Var == null && x3Var2 == null) {
                return b().equals(d3Var.b());
            }
            if (x3Var == null || x3Var2 == null) {
                if (x3Var != null) {
                    d3Var.d(x3Var.v());
                    return x3Var.equals(d3Var.f14742a);
                }
                d(x3Var2.v());
                return this.f14742a.equals(x3Var2);
            }
            return x3Var.equals(x3Var2);
        }
        return false;
    }

    public int hashCode() {
        return 1;
    }
}
