package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v3 extends x8<v3, a> implements ka {
    private static final v3 zzc;
    private static volatile qa<v3> zzd;
    private int zze;
    private int zzf;
    private String zzg = BuildConfig.FLAVOR;
    private u3 zzh;
    private boolean zzi;
    private boolean zzj;
    private boolean zzk;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<v3, a> implements ka {
        private a() {
            super(v3.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }

        public final a x(String str) {
            s();
            ((v3) this.f12660b).I(str);
            return this;
        }
    }

    static {
        v3 v3Var = new v3();
        zzc = v3Var;
        x8.v(v3.class, v3Var);
    }

    private v3() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(String str) {
        str.getClass();
        this.zze |= 2;
        this.zzg = str;
    }

    public static a K() {
        return zzc.y();
    }

    public final u3 J() {
        u3 u3Var = this.zzh;
        return u3Var == null ? u3.K() : u3Var;
    }

    public final String M() {
        return this.zzg;
    }

    public final boolean N() {
        return this.zzi;
    }

    public final boolean O() {
        return this.zzj;
    }

    public final boolean P() {
        return this.zzk;
    }

    public final boolean Q() {
        return (this.zze & 1) != 0;
    }

    public final boolean R() {
        return (this.zze & 32) != 0;
    }

    public final int m() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new v3();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001င\u0000\u0002ဈ\u0001\u0003ဉ\u0002\u0004ဇ\u0003\u0005ဇ\u0004\u0006ဇ\u0005", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk"});
            case 4:
                return zzc;
            case 5:
                qa<v3> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (v3.class) {
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
