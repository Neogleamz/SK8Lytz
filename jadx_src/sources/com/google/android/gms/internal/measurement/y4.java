package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y4 extends x8<y4, a> implements ka {
    private static final y4 zzc;
    private static volatile qa<y4> zzd;
    private int zze;
    private long zzf;
    private String zzg = BuildConfig.FLAVOR;
    private String zzh = BuildConfig.FLAVOR;
    private long zzi;
    private float zzj;
    private double zzk;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<y4, a> implements ka {
        private a() {
            super(y4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(String str) {
            s();
            ((y4) this.f12660b).O(str);
            return this;
        }

        public final a B() {
            s();
            ((y4) this.f12660b).j0();
            return this;
        }

        public final a C(long j8) {
            s();
            ((y4) this.f12660b).Q(j8);
            return this;
        }

        public final a D(String str) {
            s();
            ((y4) this.f12660b).U(str);
            return this;
        }

        public final a E() {
            s();
            ((y4) this.f12660b).k0();
            return this;
        }

        public final a x() {
            s();
            ((y4) this.f12660b).i0();
            return this;
        }

        public final a y(double d8) {
            s();
            ((y4) this.f12660b).I(d8);
            return this;
        }

        public final a z(long j8) {
            s();
            ((y4) this.f12660b).J(j8);
            return this;
        }
    }

    static {
        y4 y4Var = new y4();
        zzc = y4Var;
        x8.v(y4.class, y4Var);
    }

    private y4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(double d8) {
        this.zze |= 32;
        this.zzk = d8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(long j8) {
        this.zze |= 8;
        this.zzi = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void O(String str) {
        str.getClass();
        this.zze |= 2;
        this.zzg = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void Q(long j8) {
        this.zze |= 1;
        this.zzf = j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void U(String str) {
        str.getClass();
        this.zze |= 4;
        this.zzh = str;
    }

    public static a Y() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void i0() {
        this.zze &= -33;
        this.zzk = 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void j0() {
        this.zze &= -9;
        this.zzi = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void k0() {
        this.zze &= -5;
        this.zzh = zzc.zzh;
    }

    public final double H() {
        return this.zzk;
    }

    public final float P() {
        return this.zzj;
    }

    public final long V() {
        return this.zzi;
    }

    public final long X() {
        return this.zzf;
    }

    public final String b0() {
        return this.zzg;
    }

    public final String c0() {
        return this.zzh;
    }

    public final boolean d0() {
        return (this.zze & 32) != 0;
    }

    public final boolean e0() {
        return (this.zze & 16) != 0;
    }

    public final boolean f0() {
        return (this.zze & 8) != 0;
    }

    public final boolean g0() {
        return (this.zze & 1) != 0;
    }

    public final boolean h0() {
        return (this.zze & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new y4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဂ\u0000\u0002ဈ\u0001\u0003ဈ\u0002\u0004ဂ\u0003\u0005ခ\u0004\u0006က\u0005", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk"});
            case 4:
                return zzc;
            case 5:
                qa<y4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (y4.class) {
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
