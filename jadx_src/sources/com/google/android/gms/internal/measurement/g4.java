package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g4 extends x8<g4, a> implements ka {
    private static final g4 zzc;
    private static volatile qa<g4> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private String zzg = BuildConfig.FLAVOR;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<g4, a> implements ka {
        private a() {
            super(g4.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }
    }

    static {
        g4 g4Var = new g4();
        zzc = g4Var;
        x8.v(g4.class, g4Var);
    }

    private g4() {
    }

    public final String I() {
        return this.zzf;
    }

    public final String J() {
        return this.zzg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new g4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001", new Object[]{"zze", "zzf", "zzg"});
            case 4:
                return zzc;
            case 5:
                qa<g4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (g4.class) {
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
