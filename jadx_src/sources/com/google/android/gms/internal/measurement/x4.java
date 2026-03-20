package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x4 extends x8<x4, a> implements ka {
    private static final x4 zzc;
    private static volatile qa<x4> zzd;
    private int zze;
    private int zzf;
    private d9 zzg = x8.C();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<x4, a> implements ka {
        private a() {
            super(x4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a x(int i8) {
            s();
            ((x4) this.f12660b).M(i8);
            return this;
        }

        public final a y(Iterable<? extends Long> iterable) {
            s();
            ((x4) this.f12660b).K(iterable);
            return this;
        }
    }

    static {
        x4 x4Var = new x4();
        zzc = x4Var;
        x8.v(x4.class, x4Var);
    }

    private x4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void K(Iterable<? extends Long> iterable) {
        d9 d9Var = this.zzg;
        if (!d9Var.a()) {
            this.zzg = x8.p(d9Var);
        }
        g7.h(iterable, this.zzg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void M(int i8) {
        this.zze |= 1;
        this.zzf = i8;
    }

    public static a N() {
        return zzc.y();
    }

    public final long H(int i8) {
        return this.zzg.j(i8);
    }

    public final int L() {
        return this.zzf;
    }

    public final List<Long> P() {
        return this.zzg;
    }

    public final boolean Q() {
        return (this.zze & 1) != 0;
    }

    public final int m() {
        return this.zzg.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new x4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001င\u0000\u0002\u0014", new Object[]{"zze", "zzf", "zzg"});
            case 4:
                return zzc;
            case 5:
                qa<x4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (x4.class) {
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
