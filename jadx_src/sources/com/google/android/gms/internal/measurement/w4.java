package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w4 extends x8<w4, a> implements ka {
    private static final w4 zzc;
    private static volatile qa<w4> zzd;
    private d9 zze = x8.C();
    private d9 zzf = x8.C();
    private g9<q4> zzg = x8.D();
    private g9<x4> zzh = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<w4, a> implements ka {
        private a() {
            super(w4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(Iterable<? extends Long> iterable) {
            s();
            ((w4) this.f12660b).N(iterable);
            return this;
        }

        public final a B() {
            s();
            ((w4) this.f12660b).g0();
            return this;
        }

        public final a C(Iterable<? extends x4> iterable) {
            s();
            ((w4) this.f12660b).R(iterable);
            return this;
        }

        public final a D() {
            s();
            ((w4) this.f12660b).h0();
            return this;
        }

        public final a E(Iterable<? extends Long> iterable) {
            s();
            ((w4) this.f12660b).V(iterable);
            return this;
        }

        public final a x() {
            s();
            ((w4) this.f12660b).e0();
            return this;
        }

        public final a y(Iterable<? extends q4> iterable) {
            s();
            ((w4) this.f12660b).J(iterable);
            return this;
        }

        public final a z() {
            s();
            ((w4) this.f12660b).f0();
            return this;
        }
    }

    static {
        w4 w4Var = new w4();
        zzc = w4Var;
        x8.v(w4.class, w4Var);
    }

    private w4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(Iterable<? extends q4> iterable) {
        g9<q4> g9Var = this.zzg;
        if (!g9Var.a()) {
            this.zzg = x8.q(g9Var);
        }
        g7.h(iterable, this.zzg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void N(Iterable<? extends Long> iterable) {
        d9 d9Var = this.zzf;
        if (!d9Var.a()) {
            this.zzf = x8.p(d9Var);
        }
        g7.h(iterable, this.zzf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R(Iterable<? extends x4> iterable) {
        g9<x4> g9Var = this.zzh;
        if (!g9Var.a()) {
            this.zzh = x8.q(g9Var);
        }
        g7.h(iterable, this.zzh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void V(Iterable<? extends Long> iterable) {
        d9 d9Var = this.zze;
        if (!d9Var.a()) {
            this.zze = x8.p(d9Var);
        }
        g7.h(iterable, this.zze);
    }

    public static a W() {
        return zzc.y();
    }

    public static w4 Y() {
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void e0() {
        this.zzg = x8.D();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void f0() {
        this.zzf = x8.C();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void g0() {
        this.zzh = x8.D();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void h0() {
        this.zze = x8.C();
    }

    public final int K() {
        return this.zzf.size();
    }

    public final int O() {
        return this.zzh.size();
    }

    public final int S() {
        return this.zze.size();
    }

    public final List<q4> a0() {
        return this.zzg;
    }

    public final List<Long> b0() {
        return this.zzf;
    }

    public final List<x4> c0() {
        return this.zzh;
    }

    public final List<Long> d0() {
        return this.zze;
    }

    public final int m() {
        return this.zzg.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new w4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0004\u0000\u0001\u0015\u0002\u0015\u0003\u001b\u0004\u001b", new Object[]{"zze", "zzf", "zzg", q4.class, "zzh", x4.class});
            case 4:
                return zzc;
            case 5:
                qa<w4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (w4.class) {
                        qaVar = zzd;
                        if (qaVar == null) {
                            qaVar = new x8.c<>(zzc);
                            zzd = qaVar;
                        }
                    }
                }
                return qaVar;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
