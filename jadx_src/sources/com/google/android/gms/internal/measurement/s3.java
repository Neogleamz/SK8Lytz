package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.t3;
import com.google.android.gms.internal.measurement.v3;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s3 extends x8<s3, a> implements ka {
    private static final s3 zzc;
    private static volatile qa<s3> zzd;
    private int zze;
    private int zzf;
    private g9<v3> zzg = x8.D();
    private g9<t3> zzh = x8.D();
    private boolean zzi;
    private boolean zzj;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<s3, a> implements ka {
        private a() {
            super(s3.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }

        public final t3 A(int i8) {
            return ((s3) this.f12660b).H(i8);
        }

        public final int B() {
            return ((s3) this.f12660b).O();
        }

        public final v3 C(int i8) {
            return ((s3) this.f12660b).N(i8);
        }

        public final int x() {
            return ((s3) this.f12660b).M();
        }

        public final a y(int i8, t3.a aVar) {
            s();
            ((s3) this.f12660b).I(i8, (t3) ((x8) aVar.n()));
            return this;
        }

        public final a z(int i8, v3.a aVar) {
            s();
            ((s3) this.f12660b).J(i8, (v3) ((x8) aVar.n()));
            return this;
        }
    }

    static {
        s3 s3Var = new s3();
        zzc = s3Var;
        x8.v(s3.class, s3Var);
    }

    private s3() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(int i8, t3 t3Var) {
        t3Var.getClass();
        g9<t3> g9Var = this.zzh;
        if (!g9Var.a()) {
            this.zzh = x8.q(g9Var);
        }
        this.zzh.set(i8, t3Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void J(int i8, v3 v3Var) {
        v3Var.getClass();
        g9<v3> g9Var = this.zzg;
        if (!g9Var.a()) {
            this.zzg = x8.q(g9Var);
        }
        this.zzg.set(i8, v3Var);
    }

    public final t3 H(int i8) {
        return this.zzh.get(i8);
    }

    public final int M() {
        return this.zzh.size();
    }

    public final v3 N(int i8) {
        return this.zzg.get(i8);
    }

    public final int O() {
        return this.zzg.size();
    }

    public final List<t3> Q() {
        return this.zzh;
    }

    public final List<v3> R() {
        return this.zzg;
    }

    public final boolean S() {
        return (this.zze & 1) != 0;
    }

    public final int m() {
        return this.zzf;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new s3();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0002\u0000\u0001င\u0000\u0002\u001b\u0003\u001b\u0004ဇ\u0001\u0005ဇ\u0002", new Object[]{"zze", "zzf", "zzg", v3.class, "zzh", t3.class, "zzi", "zzj"});
            case 4:
                return zzc;
            case 5:
                qa<s3> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (s3.class) {
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
