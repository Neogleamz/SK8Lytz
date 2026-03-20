package a8;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o extends p2 implements y3 {
    private static final o zza;
    private int zzd;
    private String zze = BuildConfig.FLAVOR;
    private zzdb zzf;
    private String zzg;
    private zzdb zzh;
    private float zzi;
    private float zzj;
    private float zzk;
    private float zzl;
    private int zzm;

    static {
        o oVar = new o();
        zza = oVar;
        p2.B(o.class, oVar);
    }

    private o() {
        zzdb zzdbVar = zzdb.f14977b;
        this.zzf = zzdbVar;
        this.zzg = BuildConfig.FLAVOR;
        this.zzh = zzdbVar;
        this.zzi = 0.25f;
        this.zzj = 0.25f;
        this.zzk = 0.5f;
        this.zzl = 0.85f;
        this.zzm = 1;
    }

    public static n I() {
        return (n) zza.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void K(o oVar, zzdb zzdbVar) {
        zzdbVar.getClass();
        oVar.zzd |= 2;
        oVar.zzf = zzdbVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void L(o oVar, zzdb zzdbVar) {
        zzdbVar.getClass();
        oVar.zzd |= 8;
        oVar.zzh = zzdbVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
                    return new n(null);
                }
                return new o();
            }
            return p2.y(zza, "\u0001\t\u0000\u0001\u0001\t\t\u0000\u0000\u0000\u0001ဈ\u0000\u0002ည\u0001\u0003ဈ\u0002\u0004ည\u0003\u0005ခ\u0004\u0006ခ\u0005\u0007ခ\u0006\bခ\u0007\tင\b", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", "zzl", "zzm"});
        }
        return (byte) 1;
    }
}
