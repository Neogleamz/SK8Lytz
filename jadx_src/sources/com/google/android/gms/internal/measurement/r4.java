package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.t4;
import com.google.android.gms.internal.measurement.x8;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r4 extends x8<r4, a> implements ka {
    private static final r4 zzc;
    private static volatile qa<r4> zzd;
    private int zze;
    private g9<t4> zzf = x8.D();
    private String zzg = BuildConfig.FLAVOR;
    private long zzh;
    private long zzi;
    private int zzj;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<r4, a> implements ka {
        private a() {
            super(r4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(int i8, t4 t4Var) {
            s();
            ((r4) this.f12660b).I(i8, t4Var);
            return this;
        }

        public final a B(long j8) {
            s();
            ((r4) this.f12660b).J(j8);
            return this;
        }

        public final a C(t4.a aVar) {
            s();
            ((r4) this.f12660b).R((t4) ((x8) aVar.n()));
            return this;
        }

        public final a D(t4 t4Var) {
            s();
            ((r4) this.f12660b).R(t4Var);
            return this;
        }

        public final a E(Iterable<? extends t4> iterable) {
            s();
            ((r4) this.f12660b).S(iterable);
            return this;
        }

        public final a F(String str) {
            s();
            ((r4) this.f12660b).T(str);
            return this;
        }

        public final long G() {
            return ((r4) this.f12660b).Y();
        }

        public final a H(long j8) {
            s();
            ((r4) this.f12660b).W(j8);
            return this;
        }

        public final t4 I(int i8) {
            return ((r4) this.f12660b).H(i8);
        }

        public final long J() {
            return ((r4) this.f12660b).a0();
        }

        public final a K() {
            s();
            ((r4) this.f12660b).i0();
            return this;
        }

        public final String L() {
            return ((r4) this.f12660b).d0();
        }

        public final List<t4> M() {
            return Collections.unmodifiableList(((r4) this.f12660b).e0());
        }

        public final boolean N() {
            return ((r4) this.f12660b).h0();
        }

        public final int x() {
            return ((r4) this.f12660b).U();
        }

        public final a y(int i8) {
            s();
            ((r4) this.f12660b).V(i8);
            return this;
        }

        public final a z(int i8, t4.a aVar) {
            s();
            ((r4) this.f12660b).I(i8, (t4) ((x8) aVar.n()));
            return this;
        }
    }

    static {
        r4 r4Var = new r4();
        zzc = r4Var;
        x8.v(r4.class, r4Var);
    }

    private r4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(int i8, t4 t4Var) {
        t4Var.getClass();
        j0();
        this.zzf.set(i8, t4Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(long j8) {
        this.zze |= 4;
        this.zzi = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R(t4 t4Var) {
        t4Var.getClass();
        j0();
        this.zzf.add(t4Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void S(Iterable<? extends t4> iterable) {
        j0();
        g7.h(iterable, this.zzf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void T(String str) {
        str.getClass();
        this.zze |= 1;
        this.zzg = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void V(int i8) {
        j0();
        this.zzf.remove(i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void W(long j8) {
        this.zze |= 2;
        this.zzh = j8;
    }

    public static a b0() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void i0() {
        this.zzf = x8.D();
    }

    private final void j0() {
        g9<t4> g9Var = this.zzf;
        if (g9Var.a()) {
            return;
        }
        this.zzf = x8.q(g9Var);
    }

    public final t4 H(int i8) {
        return this.zzf.get(i8);
    }

    public final int U() {
        return this.zzf.size();
    }

    public final long Y() {
        return this.zzi;
    }

    public final long a0() {
        return this.zzh;
    }

    public final String d0() {
        return this.zzg;
    }

    public final List<t4> e0() {
        return this.zzf;
    }

    public final boolean f0() {
        return (this.zze & 8) != 0;
    }

    public final boolean g0() {
        return (this.zze & 4) != 0;
    }

    public final boolean h0() {
        return (this.zze & 2) != 0;
    }

    public final int m() {
        return this.zzj;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new r4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0001\u0000\u0001\u001b\u0002ဈ\u0000\u0003ဂ\u0001\u0004ဂ\u0002\u0005င\u0003", new Object[]{"zze", "zzf", t4.class, "zzg", "zzh", "zzi", "zzj"});
            case 4:
                return zzc;
            case 5:
                qa<r4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (r4.class) {
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
