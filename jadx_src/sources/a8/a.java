package a8;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends p2<a, l> implements y3 {
    private static final a zza;
    private int zzd;
    private j zze;
    private o zzf;

    static {
        a aVar = new a();
        zza = aVar;
        p2.B(a.class, aVar);
    }

    private a() {
    }

    public static l I() {
        return (l) zza.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void K(a aVar, j jVar) {
        jVar.getClass();
        aVar.zze = jVar;
        aVar.zzd |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void L(a aVar, o oVar) {
        oVar.getClass();
        aVar.zzf = oVar;
        aVar.zzd |= 2;
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
                    return new l(null);
                }
                return new a();
            }
            return p2.y(zza, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001", new Object[]{"zzd", "zze", "zzf"});
        }
        return (byte) 1;
    }
}
