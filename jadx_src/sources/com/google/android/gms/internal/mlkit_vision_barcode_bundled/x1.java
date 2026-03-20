package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x1 implements y5 {

    /* renamed from: a  reason: collision with root package name */
    private final w1 f14876a;

    private x1(w1 w1Var) {
        byte[] bArr = y2.f14888d;
        this.f14876a = w1Var;
        w1Var.f14875a = this;
    }

    public static x1 L(w1 w1Var) {
        x1 x1Var = w1Var.f14875a;
        return x1Var != null ? x1Var : new x1(w1Var);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void A(int i8, Object obj) {
        if (obj instanceof zzdb) {
            u1 u1Var = (u1) this.f14876a;
            u1Var.s(11);
            u1Var.r(2, i8);
            u1Var.h(3, (zzdb) obj);
            u1Var.s(12);
            return;
        }
        w1 w1Var = this.f14876a;
        x3 x3Var = (x3) obj;
        u1 u1Var2 = (u1) w1Var;
        u1Var2.s(11);
        u1Var2.r(2, i8);
        u1Var2.s(26);
        u1Var2.s(x3Var.b());
        x3Var.d(w1Var);
        u1Var2.s(12);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void B(int i8, int i9) {
        this.f14876a.i(i8, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void C(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                w1 w1Var = this.f14876a;
                long longValue = ((Long) list.get(i9)).longValue();
                w1Var.t(i8, (longValue >> 63) ^ (longValue + longValue));
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            long longValue2 = ((Long) list.get(i11)).longValue();
            i10 += w1.B((longValue2 >> 63) ^ (longValue2 + longValue2));
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            w1 w1Var2 = this.f14876a;
            long longValue3 = ((Long) list.get(i9)).longValue();
            w1Var2.u((longValue3 >> 63) ^ (longValue3 + longValue3));
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void D(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.i(i8, Float.floatToRawIntBits(((Float) list.get(i9)).floatValue()));
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Float) list.get(i11)).floatValue();
            i10 += 4;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.j(Float.floatToRawIntBits(((Float) list.get(i9)).floatValue()));
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void E(int i8, int i9) {
        this.f14876a.r(i8, (i9 >> 31) ^ (i9 + i9));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void F(int i8, long j8) {
        this.f14876a.k(i8, j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void G(int i8, Object obj, r4 r4Var) {
        x3 x3Var = (x3) obj;
        u1 u1Var = (u1) this.f14876a;
        u1Var.s((i8 << 3) | 2);
        u1Var.s(((y0) x3Var).g(r4Var));
        r4Var.e(x3Var, u1Var.f14875a);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void H(int i8, List list) {
        int i9 = 0;
        if (!(list instanceof f3)) {
            while (i9 < list.size()) {
                this.f14876a.p(i8, (String) list.get(i9));
                i9++;
            }
            return;
        }
        f3 f3Var = (f3) list;
        while (i9 < list.size()) {
            Object m8 = f3Var.m(i9);
            if (m8 instanceof String) {
                this.f14876a.p(i8, (String) m8);
            } else {
                this.f14876a.h(i8, (zzdb) m8);
            }
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void I(int i8, long j8) {
        this.f14876a.t(i8, j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    @Deprecated
    public final void J(int i8) {
        this.f14876a.q(i8, 4);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    @Deprecated
    public final void K(int i8) {
        this.f14876a.q(i8, 3);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void a(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.g(i8, ((Boolean) list.get(i9)).booleanValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Boolean) list.get(i11)).booleanValue();
            i10++;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.f(((Boolean) list.get(i9)).booleanValue() ? (byte) 1 : (byte) 0);
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void b(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.i(i8, ((Integer) list.get(i9)).intValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Integer) list.get(i11)).intValue();
            i10 += 4;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.j(((Integer) list.get(i9)).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void c(int i8, List list) {
        for (int i9 = 0; i9 < list.size(); i9++) {
            this.f14876a.h(i8, (zzdb) list.get(i9));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void d(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.m(i8, ((Integer) list.get(i9)).intValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += w1.w(((Integer) list.get(i11)).intValue());
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.n(((Integer) list.get(i9)).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void e(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.k(i8, ((Long) list.get(i9)).longValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Long) list.get(i11)).longValue();
            i10 += 8;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.l(((Long) list.get(i9)).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void f(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.k(i8, Double.doubleToRawLongBits(((Double) list.get(i9)).doubleValue()));
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Double) list.get(i11)).doubleValue();
            i10 += 8;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.l(Double.doubleToRawLongBits(((Double) list.get(i9)).doubleValue()));
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void g(int i8, String str) {
        this.f14876a.p(i8, str);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void h(int i8, zzdb zzdbVar) {
        this.f14876a.h(i8, zzdbVar);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void i(int i8, int i9) {
        this.f14876a.r(i8, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void j(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.i(i8, ((Integer) list.get(i9)).intValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Integer) list.get(i11)).intValue();
            i10 += 4;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.j(((Integer) list.get(i9)).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void k(int i8, int i9) {
        this.f14876a.i(i8, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void l(int i8, boolean z4) {
        this.f14876a.g(i8, z4);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void m(int i8, long j8) {
        this.f14876a.t(i8, j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void n(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.r(i8, ((Integer) list.get(i9)).intValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += w1.A(((Integer) list.get(i11)).intValue());
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.s(((Integer) list.get(i9)).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void o(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.k(i8, ((Long) list.get(i9)).longValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            ((Long) list.get(i11)).longValue();
            i10 += 8;
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.l(((Long) list.get(i9)).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void p(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.m(i8, ((Integer) list.get(i9)).intValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += w1.w(((Integer) list.get(i11)).intValue());
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.n(((Integer) list.get(i9)).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void q(int i8, long j8) {
        this.f14876a.k(i8, j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void r(int i8, int i9) {
        this.f14876a.m(i8, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void s(int i8, Object obj, r4 r4Var) {
        w1 w1Var = this.f14876a;
        w1Var.q(i8, 3);
        r4Var.e((x3) obj, w1Var.f14875a);
        w1Var.q(i8, 4);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void t(int i8, int i9) {
        this.f14876a.m(i8, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void u(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                w1 w1Var = this.f14876a;
                int intValue = ((Integer) list.get(i9)).intValue();
                w1Var.r(i8, (intValue >> 31) ^ (intValue + intValue));
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            int intValue2 = ((Integer) list.get(i11)).intValue();
            i10 += w1.A((intValue2 >> 31) ^ (intValue2 + intValue2));
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            w1 w1Var2 = this.f14876a;
            int intValue3 = ((Integer) list.get(i9)).intValue();
            w1Var2.s((intValue3 >> 31) ^ (intValue3 + intValue3));
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void v(int i8, double d8) {
        this.f14876a.k(i8, Double.doubleToRawLongBits(d8));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void w(int i8, long j8) {
        this.f14876a.t(i8, (j8 >> 63) ^ (j8 + j8));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void x(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.t(i8, ((Long) list.get(i9)).longValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += w1.B(((Long) list.get(i11)).longValue());
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.u(((Long) list.get(i9)).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void y(int i8, float f5) {
        this.f14876a.i(i8, Float.floatToRawIntBits(f5));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y5
    public final void z(int i8, List list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f14876a.t(i8, ((Long) list.get(i9)).longValue());
                i9++;
            }
            return;
        }
        this.f14876a.q(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += w1.B(((Long) list.get(i11)).longValue());
        }
        this.f14876a.s(i10);
        while (i9 < list.size()) {
            this.f14876a.u(((Long) list.get(i9)).longValue());
            i9++;
        }
    }
}
