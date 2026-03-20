package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b4 extends x8<b4, a> implements ka {
    private static final b4 zzc;
    private static volatile qa<b4> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private g9<f4> zzg = x8.D();
    private boolean zzh;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<b4, a> implements ka {
        private a() {
            super(b4.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }
    }

    static {
        b4 b4Var = new b4();
        zzc = b4Var;
        x8.v(b4.class, b4Var);
    }

    private b4() {
    }

    public final String I() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new b4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001ဈ\u0000\u0002\u001b\u0003ဇ\u0001", new Object[]{"zze", "zzf", "zzg", f4.class, "zzh"});
            case 4:
                return zzc;
            case 5:
                qa<b4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (b4.class) {
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
