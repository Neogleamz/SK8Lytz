package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m7 extends p2 implements y3 {
    private static final m7 zza;
    private int zzd;
    private boolean zzf;
    private int zzg;
    private boolean zzj;
    private int zzm;
    private int zzn;
    private boolean zzo;
    private int zze = -1;
    private zzdb zzh = zzdb.f14977b;
    private String zzi = BuildConfig.FLAVOR;
    private boolean zzk = true;
    private boolean zzl = true;

    static {
        m7 m7Var = new m7();
        zza = m7Var;
        p2.B(m7.class, m7Var);
    }

    private m7() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 == 2) {
                t2 t2Var = l7.f14807a;
                return p2.y(zza, "\u0001\u000b\u0000\u0001\u0001\u000b\u000b\u0000\u0000\u0000\u0001င\u0000\u0002ဇ\u0001\u0003᠌\u0002\u0004ည\u0003\u0005ဈ\u0004\u0006ဇ\u0005\u0007ဇ\u0006\bဇ\u0007\t᠌\b\n᠌\t\u000bဇ\n", new Object[]{"zzd", "zze", "zzf", "zzg", k7.f14803a, "zzh", "zzi", "zzj", "zzk", "zzl", "zzm", t2Var, "zzn", t2Var, "zzo"});
            } else if (i9 != 3) {
                if (i9 != 4) {
                    if (i9 != 5) {
                        return null;
                    }
                    return zza;
                }
                return new i7(null);
            } else {
                return new m7();
            }
        }
        return (byte) 1;
    }
}
