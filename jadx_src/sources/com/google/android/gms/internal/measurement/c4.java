package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c4 extends x8<c4, a> implements ka {
    private static final c4 zzc;
    private static volatile qa<c4> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private boolean zzg;
    private boolean zzh;
    private int zzi;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<c4, a> implements ka {
        private a() {
            super(c4.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }

        public final boolean A() {
            return ((c4) this.f12660b).L();
        }

        public final boolean B() {
            return ((c4) this.f12660b).M();
        }

        public final boolean C() {
            return ((c4) this.f12660b).N();
        }

        public final boolean D() {
            return ((c4) this.f12660b).O();
        }

        public final boolean E() {
            return ((c4) this.f12660b).P();
        }

        public final int x() {
            return ((c4) this.f12660b).m();
        }

        public final a y(String str) {
            s();
            ((c4) this.f12660b).I(str);
            return this;
        }

        public final String z() {
            return ((c4) this.f12660b).K();
        }
    }

    static {
        c4 c4Var = new c4();
        zzc = c4Var;
        x8.v(c4.class, c4Var);
    }

    private c4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(String str) {
        str.getClass();
        this.zze |= 1;
        this.zzf = str;
    }

    public final String K() {
        return this.zzf;
    }

    public final boolean L() {
        return this.zzg;
    }

    public final boolean M() {
        return this.zzh;
    }

    public final boolean N() {
        return (this.zze & 2) != 0;
    }

    public final boolean O() {
        return (this.zze & 4) != 0;
    }

    public final boolean P() {
        return (this.zze & 8) != 0;
    }

    public final int m() {
        return this.zzi;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new c4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဇ\u0001\u0003ဇ\u0002\u0004င\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzc;
            case 5:
                qa<c4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (c4.class) {
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
