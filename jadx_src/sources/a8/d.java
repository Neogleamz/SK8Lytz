package a8;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.u2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends p2 implements y3 {
    private static final d zza;
    private int zzd;
    private u2 zze = p2.q();
    private u2 zzf = p2.q();
    private int zzg;
    private int zzh;
    private int zzi;
    private int zzj;

    static {
        d dVar = new d();
        zza = dVar;
        p2.B(d.class, dVar);
    }

    private d() {
    }

    public static c I() {
        return (c) zza.j();
    }

    public static /* synthetic */ void K(d dVar, int i8) {
        dVar.zzd |= 2;
        dVar.zzh = i8;
    }

    public static /* synthetic */ void L(d dVar, float f5) {
        u2 u2Var = dVar.zze;
        if (!u2Var.a()) {
            dVar.zze = p2.r(u2Var);
        }
        dVar.zze.K0(f5);
    }

    public static /* synthetic */ void M(d dVar, float f5) {
        u2 u2Var = dVar.zzf;
        if (!u2Var.a()) {
            dVar.zzf = p2.r(u2Var);
        }
        dVar.zzf.K0(f5);
    }

    public static /* synthetic */ void N(d dVar, int i8) {
        dVar.zzd |= 1;
        dVar.zzg = i8;
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
                    return new c(null);
                }
                return new d();
            }
            return p2.y(zza, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0002\u0000\u0001\u0013\u0002\u0013\u0003ဋ\u0000\u0004ဋ\u0001\u0005ဋ\u0002\u0006ဋ\u0003", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj"});
        }
        return (byte) 1;
    }
}
