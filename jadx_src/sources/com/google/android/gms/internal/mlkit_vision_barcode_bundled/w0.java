package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w0 extends p2 implements y3 {
    private static final w0 zza;
    private int zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;

    static {
        w0 w0Var = new w0();
        zza = w0Var;
        p2.B(w0.class, w0Var);
    }

    private w0() {
    }

    public static w0 J() {
        return zza;
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
                    return new s0(null);
                }
                return new w0();
            }
            return p2.y(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001᠌\u0000\u0002ဈ\u0001", new Object[]{"zzd", "zze", u0.f14865a, "zzf"});
        }
        return (byte) 1;
    }

    public final String K() {
        return this.zzf;
    }

    public final int L() {
        int a9 = v0.a(this.zze);
        if (a9 == 0) {
            return 1;
        }
        return a9;
    }
}
