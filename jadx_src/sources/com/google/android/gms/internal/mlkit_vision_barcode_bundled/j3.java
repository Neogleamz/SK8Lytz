package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j3 extends p2 implements y3 {
    public static final o2 zza;
    private static final j3 zzd;
    private int zze;
    private o8 zzj;
    private j3 zzk;
    private b9 zzl;
    private byte zzm = 2;
    private String zzf = BuildConfig.FLAVOR;
    private x2 zzg = p2.t();
    private x2 zzh = p2.t();
    private x2 zzi = p2.t();

    static {
        j3 j3Var = new j3();
        zzd = j3Var;
        p2.B(j3.class, j3Var);
        zza = p2.l(o8.K(), j3Var, j3Var, null, 12208774, zzho.f15045m, j3.class);
    }

    private j3() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zzm = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zzd;
                    }
                    return new m2(null);
                }
                return new j3();
            }
            return p2.y(zzd, "\u0001\u0007\u0000\u0001\u0002Ǵ\u0007\u0000\u0003\u0004\u0002Л\u0005Л\u0006\u001b\bᐉ\u0001\nဈ\u0000\u000bᐉ\u0002Ǵဉ\u0003", new Object[]{"zze", "zzg", j7.class, "zzi", j7.class, "zzh", q8.class, "zzj", "zzf", "zzk", "zzl"});
        }
        return Byte.valueOf(this.zzm);
    }
}
