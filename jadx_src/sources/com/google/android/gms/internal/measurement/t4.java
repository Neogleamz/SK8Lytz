package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t4 extends x8<t4, a> implements ka {
    private static final t4 zzc;
    private static volatile qa<t4> zzd;
    private int zze;
    private long zzh;
    private float zzi;
    private double zzj;
    private String zzf = BuildConfig.FLAVOR;
    private String zzg = BuildConfig.FLAVOR;
    private g9<t4> zzk = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<t4, a> implements ka {
        private a() {
            super(t4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(a aVar) {
            s();
            ((t4) this.f12660b).c0((t4) ((x8) aVar.n()));
            return this;
        }

        public final a B(Iterable<? extends t4> iterable) {
            s();
            ((t4) this.f12660b).Q(iterable);
            return this;
        }

        public final a C(String str) {
            s();
            ((t4) this.f12660b).R(str);
            return this;
        }

        public final a D() {
            s();
            ((t4) this.f12660b).m0();
            return this;
        }

        public final a E(String str) {
            s();
            ((t4) this.f12660b).V(str);
            return this;
        }

        public final a F() {
            s();
            ((t4) this.f12660b).n0();
            return this;
        }

        public final a G() {
            s();
            ((t4) this.f12660b).o0();
            return this;
        }

        public final a H() {
            s();
            ((t4) this.f12660b).p0();
            return this;
        }

        public final String I() {
            return ((t4) this.f12660b).e0();
        }

        public final String J() {
            return ((t4) this.f12660b).f0();
        }

        public final int x() {
            return ((t4) this.f12660b).W();
        }

        public final a y(double d8) {
            s();
            ((t4) this.f12660b).I(d8);
            return this;
        }

        public final a z(long j8) {
            s();
            ((t4) this.f12660b).J(j8);
            return this;
        }
    }

    static {
        t4 t4Var = new t4();
        zzc = t4Var;
        x8.v(t4.class, t4Var);
    }

    private t4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(double d8) {
        this.zze |= 16;
        this.zzj = d8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(long j8) {
        this.zze |= 4;
        this.zzh = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void Q(Iterable<? extends t4> iterable) {
        q0();
        g7.h(iterable, this.zzk);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R(String str) {
        str.getClass();
        this.zze |= 1;
        this.zzf = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void V(String str) {
        str.getClass();
        this.zze |= 2;
        this.zzg = str;
    }

    public static a b0() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void c0(t4 t4Var) {
        t4Var.getClass();
        q0();
        this.zzk.add(t4Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void m0() {
        this.zze &= -17;
        this.zzj = 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void n0() {
        this.zze &= -5;
        this.zzh = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void o0() {
        this.zzk = x8.D();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void p0() {
        this.zze &= -3;
        this.zzg = zzc.zzg;
    }

    private final void q0() {
        g9<t4> g9Var = this.zzk;
        if (g9Var.a()) {
            return;
        }
        this.zzk = x8.q(g9Var);
    }

    public final double H() {
        return this.zzj;
    }

    public final float S() {
        return this.zzi;
    }

    public final int W() {
        return this.zzk.size();
    }

    public final long Y() {
        return this.zzh;
    }

    public final String e0() {
        return this.zzf;
    }

    public final String f0() {
        return this.zzg;
    }

    public final List<t4> g0() {
        return this.zzk;
    }

    public final boolean h0() {
        return (this.zze & 16) != 0;
    }

    public final boolean i0() {
        return (this.zze & 8) != 0;
    }

    public final boolean j0() {
        return (this.zze & 4) != 0;
    }

    public final boolean k0() {
        return (this.zze & 1) != 0;
    }

    public final boolean l0() {
        return (this.zze & 2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new t4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0001\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဂ\u0002\u0004ခ\u0003\u0005က\u0004\u0006\u001b", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", t4.class});
            case 4:
                return zzc;
            case 5:
                qa<t4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (t4.class) {
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
