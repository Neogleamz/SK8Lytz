package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l6 extends p2 implements y3 {
    private static final l6 zza;
    private int zzd;
    private String zze = BuildConfig.FLAVOR;
    private int zzf = 1;
    private boolean zzg;
    private int zzh;

    static {
        l6 l6Var = new l6();
        zza = l6Var;
        p2.B(l6.class, l6Var);
    }

    private l6() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            return null;
                        }
                        return zza;
                    }
                    return new j6(null);
                }
                return new l6();
            }
            return p2.y(zza, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002᠌\u0001\u0003ဇ\u0002\u0004င\u0003", new Object[]{"zzd", "zze", "zzf", k6.f14802a, "zzg", "zzh"});
        }
        return (byte) 1;
    }
}
