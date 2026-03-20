package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o8 extends l2 {
    private static final o8 zzd;
    private byte zze = 2;

    static {
        o8 o8Var = new o8();
        zzd = o8Var;
        p2.B(o8.class, o8Var);
    }

    private o8() {
    }

    public static o8 K() {
        return zzd;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zze = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zzd;
                    }
                    return new n8(null);
                }
                return new o8();
            }
            return p2.y(zzd, "\u0003\u0000", null);
        }
        return Byte.valueOf(this.zze);
    }
}
