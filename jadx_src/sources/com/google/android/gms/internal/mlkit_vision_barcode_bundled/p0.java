package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p0 extends p2 implements y3 {
    private static final p0 zza;
    private int zzd;
    private int zze;
    private j3 zzg;
    private byte zzh = 2;
    private x2 zzf = p2.t();

    static {
        p0 p0Var = new p0();
        zza = p0Var;
        p2.B(p0.class, p0Var);
    }

    private p0() {
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2
    public final Object H(int i8, Object obj, Object obj2) {
        int i9 = i8 - 1;
        if (i9 != 0) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 4) {
                        if (i9 != 5) {
                            this.zzh = obj == null ? (byte) 0 : (byte) 1;
                            return null;
                        }
                        return zza;
                    }
                    return new k0(null);
                }
                return new p0();
            }
            return p2.y(zza, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0001\u0001᠌\u0000\u0002\u001a\u0003ᐉ\u0001", new Object[]{"zzd", "zze", m0.f14808a, "zzf", "zzg"});
        }
        return Byte.valueOf(this.zzh);
    }

    public final List J() {
        return this.zzf;
    }

    public final int K() {
        int a9 = o0.a(this.zze);
        if (a9 == 0) {
            return 1;
        }
        return a9;
    }
}
