package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o4 extends x8<o4, a> implements ka {
    private static final o4 zzc;
    private static volatile qa<o4> zzd;
    private int zze;
    private boolean zzf;
    private boolean zzg;
    private boolean zzh;
    private boolean zzi;
    private boolean zzj;
    private boolean zzk;
    private boolean zzl;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<o4, a> implements ka {
        private a() {
            super(o4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(boolean z4) {
            s();
            ((o4) this.f12660b).R(z4);
            return this;
        }

        public final a B(boolean z4) {
            s();
            ((o4) this.f12660b).U(z4);
            return this;
        }

        public final a C(boolean z4) {
            s();
            ((o4) this.f12660b).X(z4);
            return this;
        }

        public final a D(boolean z4) {
            s();
            ((o4) this.f12660b).b0(z4);
            return this;
        }

        public final a x(boolean z4) {
            s();
            ((o4) this.f12660b).J(z4);
            return this;
        }

        public final a y(boolean z4) {
            s();
            ((o4) this.f12660b).M(z4);
            return this;
        }

        public final a z(boolean z4) {
            s();
            ((o4) this.f12660b).P(z4);
            return this;
        }
    }

    static {
        o4 o4Var = new o4();
        zzc = o4Var;
        x8.v(o4.class, o4Var);
    }

    private o4() {
    }

    public static a H() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(boolean z4) {
        this.zze |= 32;
        this.zzk = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void M(boolean z4) {
        this.zze |= 16;
        this.zzj = z4;
    }

    public static o4 N() {
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void P(boolean z4) {
        this.zze |= 1;
        this.zzf = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R(boolean z4) {
        this.zze |= 64;
        this.zzl = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void U(boolean z4) {
        this.zze |= 2;
        this.zzg = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void X(boolean z4) {
        this.zze |= 4;
        this.zzh = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void b0(boolean z4) {
        this.zze |= 8;
        this.zzi = z4;
    }

    public final boolean S() {
        return this.zzk;
    }

    public final boolean V() {
        return this.zzj;
    }

    public final boolean Y() {
        return this.zzf;
    }

    public final boolean c0() {
        return this.zzl;
    }

    public final boolean d0() {
        return this.zzg;
    }

    public final boolean e0() {
        return this.zzh;
    }

    public final boolean f0() {
        return this.zzi;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new o4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0000\u0000\u0001ဇ\u0000\u0002ဇ\u0001\u0003ဇ\u0002\u0004ဇ\u0003\u0005ဇ\u0004\u0006ဇ\u0005\u0007ဇ\u0006", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", "zzl"});
            case 4:
                return zzc;
            case 5:
                qa<o4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (o4.class) {
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
