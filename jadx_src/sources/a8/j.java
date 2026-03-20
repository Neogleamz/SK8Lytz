package a8;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.b6;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.u2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j extends p2 implements y3 {
    private static final j zza;
    private int zzd;
    private g zzj;
    private b6 zzm;
    private String zze = BuildConfig.FLAVOR;
    private zzdb zzf = zzdb.f14977b;
    private int zzg = 10;
    private float zzh = 0.5f;
    private float zzi = 0.05f;
    private u2 zzk = p2.q();
    private int zzl = 1;
    private int zzn = 320;
    private int zzo = 4;
    private int zzp = 2;

    static {
        j jVar = new j();
        zza = jVar;
        p2.B(j.class, jVar);
    }

    private j() {
    }

    public static i I() {
        return (i) zza.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void K(j jVar, g gVar) {
        gVar.getClass();
        jVar.zzj = gVar;
        jVar.zzd |= 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void L(j jVar, zzdb zzdbVar) {
        zzdbVar.getClass();
        jVar.zzd |= 2;
        jVar.zzf = zzdbVar;
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
                    return new i(null);
                }
                return new j();
            }
            return p2.y(zza, "\u0001\f\u0000\u0001\u0001\f\f\u0000\u0001\u0000\u0001ဈ\u0000\u0002ည\u0001\u0003ဋ\u0002\u0004ခ\u0003\u0005ခ\u0004\u0006ဉ\u0005\u0007\u0013\bင\u0006\tဉ\u0007\nင\b\u000bင\t\fင\n", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", "zzl", "zzm", "zzn", "zzo", "zzp"});
        }
        return (byte) 1;
    }
}
