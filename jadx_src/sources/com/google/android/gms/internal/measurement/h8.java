package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h8 implements rc {

    /* renamed from: a  reason: collision with root package name */
    private final zzja f12227a;

    private h8(zzja zzjaVar) {
        zzja zzjaVar2 = (zzja) a9.f(zzjaVar, "output");
        this.f12227a = zzjaVar2;
        zzjaVar2.f12858a = this;
    }

    public static h8 O(zzja zzjaVar) {
        h8 h8Var = zzjaVar.f12858a;
        return h8Var != null ? h8Var : new h8(zzjaVar);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void A(int i8, List<?> list, xa xaVar) {
        for (int i9 = 0; i9 < list.size(); i9++) {
            o(i8, list.get(i9), xaVar);
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final <K, V> void B(int i8, z9<K, V> z9Var, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.f12227a.A0(i8, 2);
            this.f12227a.B0(aa.a(z9Var, entry.getKey(), entry.getValue()));
            aa.b(this.f12227a, z9Var, entry.getKey(), entry.getValue());
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void C(int i8, int i9) {
        this.f12227a.C0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void D(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.p0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.z(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.s0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void E(int i8, List<?> list, xa xaVar) {
        for (int i9 = 0; i9 < list.size(); i9++) {
            m(i8, list.get(i9), xaVar);
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void F(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.C0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.o0(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.B0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void G(int i8, zzij zzijVar) {
        this.f12227a.X(i8, zzijVar);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void H(int i8, List<String> list) {
        int i9 = 0;
        if (!(list instanceof o9)) {
            while (i9 < list.size()) {
                this.f12227a.O(i8, list.get(i9));
                i9++;
            }
            return;
        }
        o9 o9Var = (o9) list;
        while (i9 < list.size()) {
            Object j8 = o9Var.j(i9);
            if (j8 instanceof String) {
                this.f12227a.O(i8, (String) j8);
            } else {
                this.f12227a.X(i8, (zzij) j8);
            }
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void I(int i8, int i9) {
        this.f12227a.p0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void J(int i8, long j8) {
        this.f12227a.q0(i8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void K(int i8, List<Float> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.M(i8, list.get(i9).floatValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.d(list.get(i11).floatValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.K(list.get(i9).floatValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void L(int i8, int i9) {
        this.f12227a.y0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void M(int i8, long j8) {
        this.f12227a.u0(i8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void N(int i8, List<zzij> list) {
        for (int i9 = 0; i9 < list.size(); i9++) {
            this.f12227a.X(i8, list.get(i9));
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void a(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.t0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.e(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.x0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void b(int i8, List<Long> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.q0(i8, list.get(i9).longValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.e0(list.get(i11).longValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.r0(list.get(i9).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    @Deprecated
    public final void c(int i8) {
        this.f12227a.A0(i8, 4);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void d(int i8, List<Long> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.m0(i8, list.get(i9).longValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.W(list.get(i11).longValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.n0(list.get(i9).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void e(int i8, List<Long> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.u0(i8, list.get(i9).longValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.j0(list.get(i11).longValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.v0(list.get(i9).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void f(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.t0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.T(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.x0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void g(int i8, boolean z4) {
        this.f12227a.P(i8, z4);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void h(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.p0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.b0(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.s0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void i(int i8, long j8) {
        this.f12227a.u0(i8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    @Deprecated
    public final void j(int i8) {
        this.f12227a.A0(i8, 3);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void k(int i8, int i9) {
        this.f12227a.p0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void l(int i8, List<Boolean> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.P(i8, list.get(i9).booleanValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.v(list.get(i11).booleanValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.S(list.get(i9).booleanValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void m(int i8, Object obj, xa xaVar) {
        zzja zzjaVar = this.f12227a;
        zzjaVar.A0(i8, 3);
        xaVar.d((ia) obj, zzjaVar.f12858a);
        zzjaVar.A0(i8, 4);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void n(int i8, List<Long> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.u0(i8, list.get(i9).longValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.F(list.get(i11).longValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.v0(list.get(i9).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void o(int i8, Object obj, xa xaVar) {
        this.f12227a.Y(i8, (ia) obj, xaVar);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void p(int i8, int i9) {
        this.f12227a.t0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void q(int i8, long j8) {
        this.f12227a.m0(i8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void r(int i8, Object obj) {
        if (obj instanceof zzij) {
            this.f12227a.f0(i8, (zzij) obj);
        } else {
            this.f12227a.N(i8, (ia) obj);
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void s(int i8, List<Double> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.L(i8, list.get(i9).doubleValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.c(list.get(i11).doubleValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.J(list.get(i9).doubleValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void t(int i8, String str) {
        this.f12227a.O(i8, str);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void u(int i8, List<Integer> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.y0(i8, list.get(i9).intValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.g0(list.get(i11).intValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.z0(list.get(i9).intValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void v(int i8, int i9) {
        this.f12227a.t0(i8, i9);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void w(int i8, long j8) {
        this.f12227a.m0(i8, j8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void x(int i8, double d8) {
        this.f12227a.L(i8, d8);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void y(int i8, float f5) {
        this.f12227a.M(i8, f5);
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final void z(int i8, List<Long> list, boolean z4) {
        int i9 = 0;
        if (!z4) {
            while (i9 < list.size()) {
                this.f12227a.m0(i8, list.get(i9).longValue());
                i9++;
            }
            return;
        }
        this.f12227a.A0(i8, 2);
        int i10 = 0;
        for (int i11 = 0; i11 < list.size(); i11++) {
            i10 += zzja.p(list.get(i11).longValue());
        }
        this.f12227a.B0(i10);
        while (i9 < list.size()) {
            this.f12227a.n0(list.get(i9).longValue());
            i9++;
        }
    }

    @Override // com.google.android.gms.internal.measurement.rc
    public final int zza() {
        return qc.f12458a;
    }
}
