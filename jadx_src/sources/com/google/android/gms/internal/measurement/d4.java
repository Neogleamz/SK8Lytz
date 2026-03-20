package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.c4;
import com.google.android.gms.internal.measurement.x8;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d4 extends x8<d4, a> implements ka {
    private static final d4 zzc;
    private static volatile qa<d4> zzd;
    private int zze;
    private long zzf;
    private int zzh;
    private boolean zzm;
    private zzfn$zza zzr;
    private e4 zzs;
    private h4 zzt;
    private f4 zzu;
    private String zzg = BuildConfig.FLAVOR;
    private g9<g4> zzi = x8.D();
    private g9<c4> zzj = x8.D();
    private g9<s3> zzk = x8.D();
    private String zzl = BuildConfig.FLAVOR;
    private g9<i5> zzn = x8.D();
    private g9<b4> zzo = x8.D();
    private String zzp = BuildConfig.FLAVOR;
    private String zzq = BuildConfig.FLAVOR;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<d4, a> implements ka {
        private a() {
            super(d4.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }

        public final a A() {
            s();
            ((d4) this.f12660b).e0();
            return this;
        }

        public final String B() {
            return ((d4) this.f12660b).T();
        }

        public final List<s3> C() {
            return Collections.unmodifiableList(((d4) this.f12660b).U());
        }

        public final List<b4> D() {
            return Collections.unmodifiableList(((d4) this.f12660b).V());
        }

        public final int x() {
            return ((d4) this.f12660b).L();
        }

        public final c4 y(int i8) {
            return ((d4) this.f12660b).H(i8);
        }

        public final a z(int i8, c4.a aVar) {
            s();
            ((d4) this.f12660b).I(i8, (c4) ((x8) aVar.n()));
            return this;
        }
    }

    static {
        d4 d4Var = new d4();
        zzc = d4Var;
        x8.v(d4.class, d4Var);
    }

    private d4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(int i8, c4 c4Var) {
        c4Var.getClass();
        g9<c4> g9Var = this.zzj;
        if (!g9Var.a()) {
            this.zzj = x8.q(g9Var);
        }
        this.zzj.set(i8, c4Var);
    }

    public static a O() {
        return zzc.y();
    }

    public static d4 Q() {
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void e0() {
        this.zzk = x8.D();
    }

    public final c4 H(int i8) {
        return this.zzj.get(i8);
    }

    public final int L() {
        return this.zzj.size();
    }

    public final long M() {
        return this.zzf;
    }

    public final zzfn$zza N() {
        zzfn$zza zzfn_zza = this.zzr;
        return zzfn_zza == null ? zzfn$zza.I() : zzfn_zza;
    }

    public final h4 R() {
        h4 h4Var = this.zzt;
        return h4Var == null ? h4.I() : h4Var;
    }

    public final String S() {
        return this.zzg;
    }

    public final String T() {
        return this.zzp;
    }

    public final List<s3> U() {
        return this.zzk;
    }

    public final List<b4> V() {
        return this.zzo;
    }

    public final List<i5> W() {
        return this.zzn;
    }

    public final List<g4> X() {
        return this.zzi;
    }

    public final boolean Y() {
        return this.zzm;
    }

    public final boolean a0() {
        return (this.zze & RecognitionOptions.ITF) != 0;
    }

    public final boolean b0() {
        return (this.zze & 2) != 0;
    }

    public final boolean c0() {
        return (this.zze & RecognitionOptions.UPC_A) != 0;
    }

    public final boolean d0() {
        return (this.zze & 1) != 0;
    }

    public final int m() {
        return this.zzn.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new d4();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0010\u0000\u0001\u0001\u0012\u0010\u0000\u0005\u0000\u0001ဂ\u0000\u0002ဈ\u0001\u0003င\u0002\u0004\u001b\u0005\u001b\u0006\u001b\u0007ဈ\u0003\bဇ\u0004\t\u001b\n\u001b\u000bဈ\u0005\u000eဈ\u0006\u000fဉ\u0007\u0010ဉ\b\u0011ဉ\t\u0012ဉ\n", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", g4.class, "zzj", c4.class, "zzk", s3.class, "zzl", "zzm", "zzn", i5.class, "zzo", b4.class, "zzp", "zzq", "zzr", "zzs", "zzt", "zzu"});
            case 4:
                return zzc;
            case 5:
                qa<d4> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (d4.class) {
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
