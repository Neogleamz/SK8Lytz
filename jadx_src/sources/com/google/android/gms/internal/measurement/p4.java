package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.w4;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p4 extends x8<p4, a> implements ka {
    private static final p4 zzc;
    private static volatile qa<p4> zzd;
    private int zze;
    private int zzf;
    private w4 zzg;
    private w4 zzh;
    private boolean zzi;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<p4, a> implements ka {
        private a() {
            super(p4.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a A(boolean z4) {
            s();
            ((p4) this.f12660b).M(z4);
            return this;
        }

        public final a x(int i8) {
            s();
            ((p4) this.f12660b).H(i8);
            return this;
        }

        public final a y(w4.a aVar) {
            s();
            ((p4) this.f12660b).L((w4) ((x8) aVar.n()));
            return this;
        }

        public final a z(w4 w4Var) {
            s();
            ((p4) this.f12660b).P(w4Var);
            return this;
        }
    }

    static {
        p4 p4Var = new p4();
        zzc = p4Var;
        x8.v(p4.class, p4Var);
    }

    private p4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void H(int i8) {
        this.zze |= 1;
        this.zzf = i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void L(w4 w4Var) {
        w4Var.getClass();
        this.zzg = w4Var;
        this.zze |= 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void M(boolean z4) {
        this.zze |= 8;
        this.zzi = z4;
    }

    public static a N() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void P(w4 w4Var) {
        w4Var.getClass();
        this.zzh = w4Var;
        this.zze |= 4;
    }

    public final w4 R() {
        w4 w4Var = this.zzg;
        return w4Var == null ? w4.Y() : w4Var;
    }

    public final w4 S() {
        w4 w4Var = this.zzh;
        return w4Var == null ? w4.Y() : w4Var;
    }

    public final boolean T() {
        return this.zzi;
    }

    public final boolean U() {
        return (this.zze & 1) != 0;
    }

    public final boolean V() {
        return (this.zze & 8) != 0;
    }

    public final boolean W() {
        return (this.zze & 4) != 0;
    }

    public final int m() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new p4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001င\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဇ\u0003", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzc;
            case 5:
                qa<p4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (p4.class) {
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
