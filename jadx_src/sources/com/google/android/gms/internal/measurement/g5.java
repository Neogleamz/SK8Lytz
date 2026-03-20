package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g5 extends x8<g5, a> implements ka {
    private static final g5 zzc;
    private static volatile qa<g5> zzd;
    private g9<h5> zze = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<g5, a> implements ka {
        private a() {
            super(g5.zzc);
        }

        /* synthetic */ a(j5 j5Var) {
            this();
        }
    }

    static {
        g5 g5Var = new g5();
        zzc = g5Var;
        x8.v(g5.class, g5Var);
    }

    private g5() {
    }

    public static g5 I() {
        return zzc;
    }

    public final List<h5> J() {
        return this.zze;
    }

    public final int m() {
        return this.zze.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (j5.f12256a[i8 - 1]) {
            case 1:
                return new g5();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zze", h5.class});
            case 4:
                return zzc;
            case 5:
                qa<g5> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (g5.class) {
                        qaVar = zzd;
                        if (qaVar == null) {
                            qaVar = new x8.c<>(zzc);
                            zzd = qaVar;
                        }
                    }
                }
                return qaVar;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
