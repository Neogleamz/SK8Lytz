package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h4 extends x8<h4, a> implements ka {
    private static final h4 zzc;
    private static volatile qa<h4> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private String zzg = BuildConfig.FLAVOR;
    private String zzh = BuildConfig.FLAVOR;
    private int zzi;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<h4, a> implements ka {
        private a() {
            super(h4.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }
    }

    static {
        h4 h4Var = new h4();
        zzc = h4Var;
        x8.v(h4.class, h4Var);
    }

    private h4() {
    }

    public static h4 I() {
        return zzc;
    }

    public final String J() {
        return this.zzg;
    }

    public final String K() {
        return this.zzf;
    }

    public final int m() {
        return this.zzi;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new h4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဈ\u0002\u0004င\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzc;
            case 5:
                qa<h4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (h4.class) {
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
