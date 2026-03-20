package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t3 extends x8<t3, a> implements ka {
    private static final t3 zzc;
    private static volatile qa<t3> zzd;
    private int zze;
    private int zzf;
    private String zzg = BuildConfig.FLAVOR;
    private g9<u3> zzh = x8.D();
    private boolean zzi;
    private zzfh$zzd zzj;
    private boolean zzk;
    private boolean zzl;
    private boolean zzm;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<t3, a> implements ka {
        private a() {
            super(t3.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }

        public final u3 A(int i8) {
            return ((t3) this.f12660b).H(i8);
        }

        public final String B() {
            return ((t3) this.f12660b).Q();
        }

        public final int x() {
            return ((t3) this.f12660b).m();
        }

        public final a y(int i8, u3 u3Var) {
            s();
            ((t3) this.f12660b).I(i8, u3Var);
            return this;
        }

        public final a z(String str) {
            s();
            ((t3) this.f12660b).L(str);
            return this;
        }
    }

    static {
        t3 t3Var = new t3();
        zzc = t3Var;
        x8.v(t3.class, t3Var);
    }

    private t3() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(int i8, u3 u3Var) {
        u3Var.getClass();
        g9<u3> g9Var = this.zzh;
        if (!g9Var.a()) {
            this.zzh = x8.q(g9Var);
        }
        this.zzh.set(i8, u3Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void L(String str) {
        str.getClass();
        this.zze |= 2;
        this.zzg = str;
    }

    public static a N() {
        return zzc.y();
    }

    public final u3 H(int i8) {
        return this.zzh.get(i8);
    }

    public final int M() {
        return this.zzf;
    }

    public final zzfh$zzd P() {
        zzfh$zzd zzfh_zzd = this.zzj;
        return zzfh_zzd == null ? zzfh$zzd.J() : zzfh_zzd;
    }

    public final String Q() {
        return this.zzg;
    }

    public final List<u3> R() {
        return this.zzh;
    }

    public final boolean S() {
        return this.zzk;
    }

    public final boolean T() {
        return this.zzl;
    }

    public final boolean U() {
        return this.zzm;
    }

    public final boolean V() {
        return (this.zze & 8) != 0;
    }

    public final boolean W() {
        return (this.zze & 1) != 0;
    }

    public final boolean X() {
        return (this.zze & 64) != 0;
    }

    public final int m() {
        return this.zzh.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new t3();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0001\u0000\u0001င\u0000\u0002ဈ\u0001\u0003\u001b\u0004ဇ\u0002\u0005ဉ\u0003\u0006ဇ\u0004\u0007ဇ\u0005\bဇ\u0006", new Object[]{"zze", "zzf", "zzg", "zzh", u3.class, "zzi", "zzj", "zzk", "zzl", "zzm"});
            case 4:
                return zzc;
            case 5:
                qa<t3> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (t3.class) {
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
