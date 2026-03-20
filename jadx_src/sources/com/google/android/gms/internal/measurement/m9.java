package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m9 {

    /* renamed from: d  reason: collision with root package name */
    private static final l8 f12348d = l8.f12297c;

    /* renamed from: a  reason: collision with root package name */
    private zzij f12349a;

    /* renamed from: b  reason: collision with root package name */
    private volatile ia f12350b;

    /* renamed from: c  reason: collision with root package name */
    private volatile zzij f12351c;

    private final ia c(ia iaVar) {
        if (this.f12350b == null) {
            synchronized (this) {
                if (this.f12350b == null) {
                    try {
                        this.f12350b = iaVar;
                        this.f12351c = zzij.f12852b;
                    } catch (zzkb unused) {
                        this.f12350b = iaVar;
                        this.f12351c = zzij.f12852b;
                    }
                }
            }
        }
        return this.f12350b;
    }

    public final ia a(ia iaVar) {
        ia iaVar2 = this.f12350b;
        this.f12349a = null;
        this.f12351c = null;
        this.f12350b = iaVar;
        return iaVar2;
    }

    public final int b() {
        if (this.f12351c != null) {
            return this.f12351c.v();
        }
        if (this.f12350b != null) {
            return this.f12350b.f();
        }
        return 0;
    }

    public final zzij d() {
        if (this.f12351c != null) {
            return this.f12351c;
        }
        synchronized (this) {
            if (this.f12351c != null) {
                return this.f12351c;
            }
            this.f12351c = this.f12350b == null ? zzij.f12852b : this.f12350b.j();
            return this.f12351c;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof m9) {
            m9 m9Var = (m9) obj;
            ia iaVar = this.f12350b;
            ia iaVar2 = m9Var.f12350b;
            return (iaVar == null && iaVar2 == null) ? d().equals(m9Var.d()) : (iaVar == null || iaVar2 == null) ? iaVar != null ? iaVar.equals(m9Var.c(iaVar.c())) : c(iaVar2.c()).equals(iaVar2) : iaVar.equals(iaVar2);
        }
        return false;
    }

    public int hashCode() {
        return 1;
    }
}
