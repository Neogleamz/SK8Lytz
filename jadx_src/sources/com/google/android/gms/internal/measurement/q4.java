package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q4 extends x8<q4, a> implements ka {
    private static final q4 zzc;
    private static volatile qa<q4> zzd;
    private int zze;
    private int zzf;
    private long zzg;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<q4, a> implements ka {
        private a() {
            super(q4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a x(int i8) {
            s();
            ((q4) this.f12660b).H(i8);
            return this;
        }

        public final a y(long j8) {
            s();
            ((q4) this.f12660b).I(j8);
            return this;
        }
    }

    static {
        q4 q4Var = new q4();
        zzc = q4Var;
        x8.v(q4.class, q4Var);
    }

    private q4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void H(int i8) {
        this.zze |= 1;
        this.zzf = i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(long j8) {
        this.zze |= 2;
        this.zzg = j8;
    }

    public static a M() {
        return zzc.y();
    }

    public final long L() {
        return this.zzg;
    }

    public final boolean O() {
        return (this.zze & 2) != 0;
    }

    public final boolean P() {
        return (this.zze & 1) != 0;
    }

    public final int m() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new q4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002ဂ\u0001", new Object[]{"zze", "zzf", "zzg"});
            case 4:
                return zzc;
            case 5:
                qa<q4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (q4.class) {
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
