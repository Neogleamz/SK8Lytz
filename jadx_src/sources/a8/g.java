package a8;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g extends p2 implements y3 {
    private static final g zza;
    private x2 zzd = p2.t();

    static {
        g gVar = new g();
        zza = gVar;
        p2.B(g.class, gVar);
    }

    private g() {
    }

    public static f I() {
        return (f) zza.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void K(g gVar, d dVar) {
        dVar.getClass();
        x2 x2Var = gVar.zzd;
        if (!x2Var.a()) {
            gVar.zzd = p2.w(x2Var);
        }
        gVar.zzd.add(dVar);
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
                    return new f(null);
                }
                return new g();
            }
            return p2.y(zza, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zzd", d.class});
        }
        return (byte) 1;
    }
}
