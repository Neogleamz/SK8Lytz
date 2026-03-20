package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i5 extends x8<i5, a> implements ka {
    private static final i5 zzc;
    private static volatile qa<i5> zzd;
    private int zze;
    private g9<zzgb$zzd> zzf = x8.D();
    private g5 zzg;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<i5, a> implements ka {
        private a() {
            super(i5.zzc);
        }

        /* synthetic */ a(j5 j5Var) {
            this();
        }
    }

    static {
        i5 i5Var = new i5();
        zzc = i5Var;
        x8.v(i5.class, i5Var);
    }

    private i5() {
    }

    public final g5 H() {
        g5 g5Var = this.zzg;
        return g5Var == null ? g5.I() : g5Var;
    }

    public final List<zzgb$zzd> J() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (j5.f12256a[i8 - 1]) {
            case 1:
                return new i5();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\u001b\u0002ဉ\u0000", new Object[]{"zze", "zzf", zzgb$zzd.class, "zzg"});
            case 4:
                return zzc;
            case 5:
                qa<i5> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (i5.class) {
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
