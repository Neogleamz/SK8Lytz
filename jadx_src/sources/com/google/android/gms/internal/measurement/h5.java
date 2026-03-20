package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h5 extends x8<h5, a> implements ka {
    private static final h5 zzc;
    private static volatile qa<h5> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private g9<zzgb$zzd> zzg = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<h5, a> implements ka {
        private a() {
            super(h5.zzc);
        }

        /* synthetic */ a(j5 j5Var) {
            this();
        }
    }

    static {
        h5 h5Var = new h5();
        zzc = h5Var;
        x8.v(h5.class, h5Var);
    }

    private h5() {
    }

    public final String I() {
        return this.zzf;
    }

    public final List<zzgb$zzd> J() {
        return this.zzg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (j5.f12256a[i8 - 1]) {
            case 1:
                return new h5();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001ဈ\u0000\u0002\u001b", new Object[]{"zze", "zzf", "zzg", zzgb$zzd.class});
            case 4:
                return zzc;
            case 5:
                qa<h5> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (h5.class) {
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
