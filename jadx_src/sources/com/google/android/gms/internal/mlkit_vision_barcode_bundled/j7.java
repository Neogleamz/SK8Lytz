package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j7 extends p2 implements y3 {
    private static final j7 zza;
    private int zzd;
    private int zzg;
    private y8 zzh;
    private t8 zzi;
    private o8 zzj;
    private int zzk;
    private byte zzm = 2;
    private int zze = 17;
    private x2 zzf = p2.t();
    private x2 zzl = p2.t();

    static {
        j7 j7Var = new j7();
        zza = j7Var;
        p2.B(j7.class, j7Var);
    }

    private j7() {
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
                        return zza;
                    }
                    return new j5(null);
                }
                return new j7();
            }
            return p2.y(zza, "\u0001\b\u0000\u0001\u0001\u000f\b\u0000\u0002\u0004\u0001᠌\u0000\u0003Л\u0004င\u0001\u0005ᐉ\u0002\u0006ᐉ\u0003\u0007င\u0005\b\u001b\u000fᐉ\u0004", new Object[]{"zzd", "zze", i6.f14792a, "zzf", k.class, "zzg", "zzh", "zzi", "zzk", "zzl", n.class, "zzj"});
        }
        return Byte.valueOf(this.zzm);
    }
}
