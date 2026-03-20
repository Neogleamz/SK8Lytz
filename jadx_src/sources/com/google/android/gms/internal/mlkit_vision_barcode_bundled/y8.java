package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y8 extends p2 implements y3 {
    public static final o2 zza;
    private static final y8 zzd;
    private int zze;
    private long zzf;
    private long zzg;
    private o8 zzh;
    private byte zzi = 2;

    static {
        y8 y8Var = new y8();
        zzd = y8Var;
        p2.B(y8.class, y8Var);
        zza = p2.l(o8.K(), y8Var, y8Var, null, 13258261, zzho.f15045m, y8.class);
    }

    private y8() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zzi = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zzd;
                    }
                    return new x8(null);
                }
                return new y8();
            }
            return p2.y(zzd, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0003\u0001ᔅ\u0000\u0002ᔅ\u0001\u0003ᐉ\u0002", new Object[]{"zze", "zzf", "zzg", "zzh"});
        }
        return Byte.valueOf(this.zzi);
    }
}
