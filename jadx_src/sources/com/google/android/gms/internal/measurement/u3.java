package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u3 extends x8<u3, a> implements ka {
    private static final u3 zzc;
    private static volatile qa<u3> zzd;
    private int zze;
    private zzfh$zzf zzf;
    private zzfh$zzd zzg;
    private boolean zzh;
    private String zzi = BuildConfig.FLAVOR;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<u3, a> implements ka {
        private a() {
            super(u3.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }

        public final a x(String str) {
            s();
            ((u3) this.f12660b).J(str);
            return this;
        }
    }

    static {
        u3 u3Var = new u3();
        zzc = u3Var;
        x8.v(u3.class, u3Var);
    }

    private u3() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(String str) {
        str.getClass();
        this.zze |= 8;
        this.zzi = str;
    }

    public static u3 K() {
        return zzc;
    }

    public final zzfh$zzd L() {
        zzfh$zzd zzfh_zzd = this.zzg;
        return zzfh_zzd == null ? zzfh$zzd.J() : zzfh_zzd;
    }

    public final zzfh$zzf M() {
        zzfh$zzf zzfh_zzf = this.zzf;
        return zzfh_zzf == null ? zzfh$zzf.J() : zzfh_zzf;
    }

    public final String N() {
        return this.zzi;
    }

    public final boolean O() {
        return this.zzh;
    }

    public final boolean P() {
        return (this.zze & 4) != 0;
    }

    public final boolean Q() {
        return (this.zze & 2) != 0;
    }

    public final boolean R() {
        return (this.zze & 8) != 0;
    }

    public final boolean S() {
        return (this.zze & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new u3();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဇ\u0002\u0004ဈ\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzc;
            case 5:
                qa<u3> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (u3.class) {
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
