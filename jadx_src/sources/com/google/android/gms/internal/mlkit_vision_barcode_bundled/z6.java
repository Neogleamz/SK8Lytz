package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z6 extends p2 implements y3 {
    private static final z6 zza;
    private int zzd;
    private int zze;
    private s6 zzh;
    private int zzj;
    private int zzk;
    private x2 zzf = p2.t();
    private int zzg = -1;
    private String zzi = BuildConfig.FLAVOR;
    private v2 zzl = p2.s();
    private String zzm = BuildConfig.FLAVOR;

    static {
        z6 z6Var = new z6();
        zza = z6Var;
        p2.B(z6.class, z6Var);
    }

    private z6() {
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
                    return new w6(null);
                }
                return new z6();
            }
            return p2.y(zza, "\u0001\t\u0000\u0001\u0001\t\t\u0000\u0002\u0000\u0001᠌\u0000\u0002\u001b\u0003င\u0001\u0004ဉ\u0002\u0005ဈ\u0003\u0006᠌\u0004\u0007᠌\u0005\b'\tဈ\u0006", new Object[]{"zzd", "zze", v6.f14870a, "zzf", u6.class, "zzg", "zzh", "zzi", "zzj", x6.f14881a, "zzk", y6.f14894a, "zzl", "zzm"});
        }
        return (byte) 1;
    }
}
