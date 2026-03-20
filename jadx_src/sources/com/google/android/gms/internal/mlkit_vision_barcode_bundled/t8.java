package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t8 extends p2 implements y3 {
    private static final t8 zza;
    private int zzd;
    private o8 zzj;
    private byte zzk = 2;
    private v2 zze = p2.s();
    private u2 zzf = p2.q();
    private boolean zzg = true;
    private String zzh = BuildConfig.FLAVOR;
    private String zzi = BuildConfig.FLAVOR;

    static {
        t8 t8Var = new t8();
        zza = t8Var;
        p2.B(t8.class, t8Var);
    }

    private t8() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zzk = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zza;
                    }
                    return new s8(null);
                }
                return new t8();
            }
            return p2.y(zza, "\u0001\u0006\u0000\u0001\u0001\u000f\u0006\u0000\u0002\u0001\u0001\u0016\u0002\u0013\u0003ဇ\u0000\u0004ဈ\u0001\u0005ဈ\u0002\u000fᐉ\u0003", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj"});
        }
        return Byte.valueOf(this.zzk);
    }
}
