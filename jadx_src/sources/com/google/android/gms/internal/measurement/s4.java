package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s4 extends x8<s4, a> implements ka {
    private static final s4 zzc;
    private static volatile qa<s4> zzd;
    private int zze;
    private String zzf = BuildConfig.FLAVOR;
    private long zzg;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<s4, a> implements ka {
        private a() {
            super(s4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a x(long j8) {
            s();
            ((s4) this.f12660b).I(j8);
            return this;
        }

        public final a y(String str) {
            s();
            ((s4) this.f12660b).L(str);
            return this;
        }
    }

    static {
        s4 s4Var = new s4();
        zzc = s4Var;
        x8.v(s4.class, s4Var);
    }

    private s4() {
    }

    public static a H() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(long j8) {
        this.zze |= 2;
        this.zzg = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void L(String str) {
        str.getClass();
        this.zze |= 1;
        this.zzf = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new s4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဂ\u0001", new Object[]{"zze", "zzf", "zzg"});
            case 4:
                return zzc;
            case 5:
                qa<s4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (s4.class) {
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
