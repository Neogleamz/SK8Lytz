package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.td;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends c {

    /* renamed from: g  reason: collision with root package name */
    private com.google.android.gms.internal.measurement.v3 f16325g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ wb f16326h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(wb wbVar, String str, int i8, com.google.android.gms.internal.measurement.v3 v3Var) {
        super(str, i8);
        this.f16326h = wbVar;
        this.f16325g = v3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.c
    public final int a() {
        return this.f16325g.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.c
    public final boolean i() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.measurement.internal.c
    public final boolean j() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean k(Long l8, Long l9, com.google.android.gms.internal.measurement.y4 y4Var, boolean z4) {
        z4 J;
        String g8;
        String str;
        Boolean g9;
        Object[] objArr = (td.a() && this.f16326h.a().D(this.f16354a, c0.f16385i0)) ? 1 : null;
        boolean N = this.f16325g.N();
        boolean O = this.f16325g.O();
        boolean P = this.f16325g.P();
        Object[] objArr2 = (N || O || P) ? 1 : null;
        Boolean bool = null;
        bool = null;
        if (z4 && objArr2 == null) {
            this.f16326h.i().I().c("Property filter already evaluated true and it is not associated with an enhanced audience. audience ID, filter ID", Integer.valueOf(this.f16355b), this.f16325g.Q() ? Integer.valueOf(this.f16325g.m()) : null);
            return true;
        }
        com.google.android.gms.internal.measurement.u3 J2 = this.f16325g.J();
        boolean O2 = J2.O();
        if (y4Var.f0()) {
            if (J2.Q()) {
                g9 = c.c(y4Var.V(), J2.L());
                bool = c.d(g9, O2);
            } else {
                J = this.f16326h.i().J();
                g8 = this.f16326h.e().g(y4Var.b0());
                str = "No number filter for long property. property";
                J.b(str, g8);
            }
        } else if (!y4Var.d0()) {
            if (y4Var.h0()) {
                if (J2.S()) {
                    g9 = c.g(y4Var.c0(), J2.M(), this.f16326h.i());
                } else if (!J2.Q()) {
                    J = this.f16326h.i().J();
                    g8 = this.f16326h.e().g(y4Var.b0());
                    str = "No string or number filter defined. property";
                } else if (nb.g0(y4Var.c0())) {
                    g9 = c.e(y4Var.c0(), J2.L());
                } else {
                    this.f16326h.i().J().c("Invalid user property value for Numeric number filter. property, value", this.f16326h.e().g(y4Var.b0()), y4Var.c0());
                }
                bool = c.d(g9, O2);
            } else {
                J = this.f16326h.i().J();
                g8 = this.f16326h.e().g(y4Var.b0());
                str = "User property has no value, property";
            }
            J.b(str, g8);
        } else if (J2.Q()) {
            g9 = c.b(y4Var.H(), J2.L());
            bool = c.d(g9, O2);
        } else {
            J = this.f16326h.i().J();
            g8 = this.f16326h.e().g(y4Var.b0());
            str = "No number filter for double property. property";
            J.b(str, g8);
        }
        this.f16326h.i().I().b("Property filter result", bool == null ? "null" : bool);
        if (bool == null) {
            return false;
        }
        this.f16356c = Boolean.TRUE;
        if (!P || bool.booleanValue()) {
            if (!z4 || this.f16325g.N()) {
                this.f16357d = bool;
            }
            if (bool.booleanValue() && objArr2 != null && y4Var.g0()) {
                long X = y4Var.X();
                if (l8 != null) {
                    X = l8.longValue();
                }
                if (objArr != null && this.f16325g.N() && !this.f16325g.O() && l9 != null) {
                    X = l9.longValue();
                }
                if (this.f16325g.O()) {
                    this.f16359f = Long.valueOf(X);
                } else {
                    this.f16358e = Long.valueOf(X);
                }
            }
            return true;
        }
        return true;
    }
}
