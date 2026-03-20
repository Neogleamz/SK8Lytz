package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends p2 implements y3 {
    public static final o2 zza;
    private static final w2 zzd = new h();
    private static final k zze;
    private int zzf;
    private b9 zzl;
    private o8 zzm;
    private byte zzn = 2;
    private String zzg = BuildConfig.FLAVOR;
    private String zzh = BuildConfig.FLAVOR;
    private v2 zzi = p2.s();
    private String zzj = BuildConfig.FLAVOR;
    private String zzk = BuildConfig.FLAVOR;

    static {
        k kVar = new k();
        zze = kVar;
        p2.B(k.class, kVar);
        zza = p2.l(o8.K(), kVar, kVar, null, 308676116, zzho.f15045m, k.class);
    }

    private k() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zzn = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zze;
                    }
                    return new i(null);
                }
                return new k();
            }
            return p2.y(zze, "\u0001\u0007\u0000\u0001\u0001Ǵ\u0007\u0000\u0001\u0002\u0001ᔈ\u0000\u0002ဈ\u0001\u0003ࠞ\u0005ဈ\u0002\u0006ဈ\u0003\u000fᐉ\u0005Ǵဉ\u0004", new Object[]{"zzf", "zzg", "zzh", "zzi", j.f14794a, "zzj", "zzk", "zzm", "zzl"});
        }
        return Byte.valueOf(this.zzn);
    }
}
