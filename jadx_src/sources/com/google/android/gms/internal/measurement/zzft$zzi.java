package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.v4;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzft$zzi extends x8<zzft$zzi, a> implements ka {
    private static final zzft$zzi zzc;
    private static volatile qa<zzft$zzi> zzd;
    private int zze;
    private g9<v4> zzf = x8.D();
    private String zzg = BuildConfig.FLAVOR;
    private String zzh = BuildConfig.FLAVOR;
    private int zzi;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzft$zzi, a> implements ka {
        private a() {
            super(zzft$zzi.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final v4 A(int i8) {
            return ((zzft$zzi) this.f12660b).H(0);
        }

        public final int x() {
            return ((zzft$zzi) this.f12660b).m();
        }

        public final a y(v4.a aVar) {
            s();
            ((zzft$zzi) this.f12660b).K((v4) ((x8) aVar.n()));
            return this;
        }

        public final a z(String str) {
            s();
            ((zzft$zzi) this.f12660b).L(str);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zza implements z8 {
        SDK(0),
        SGTM(1);
        

        /* renamed from: d  reason: collision with root package name */
        private static final c9<zza> f12827d = new c5();

        /* renamed from: a  reason: collision with root package name */
        private final int f12829a;

        zza(int i8) {
            this.f12829a = i8;
        }

        public static zza c(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    return null;
                }
                return SGTM;
            }
            return SDK;
        }

        public static b9 f() {
            return b5.f12092a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12829a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12829a;
        }
    }

    static {
        zzft$zzi zzft_zzi = new zzft$zzi();
        zzc = zzft_zzi;
        x8.v(zzft$zzi.class, zzft_zzi);
    }

    private zzft$zzi() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void K(v4 v4Var) {
        v4Var.getClass();
        g9<v4> g9Var = this.zzf;
        if (!g9Var.a()) {
            this.zzf = x8.q(g9Var);
        }
        this.zzf.add(v4Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void L(String str) {
        str.getClass();
        this.zze |= 2;
        this.zzh = str;
    }

    public static a M() {
        return zzc.y();
    }

    public final v4 H(int i8) {
        return this.zzf.get(0);
    }

    public final List<v4> O() {
        return this.zzf;
    }

    public final int m() {
        return this.zzf.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new zzft$zzi();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\t\u0004\u0000\u0001\u0000\u0001\u001b\u0007ဈ\u0000\bဈ\u0001\t᠌\u0002", new Object[]{"zze", "zzf", v4.class, "zzg", "zzh", "zzi", zza.f()});
            case 4:
                return zzc;
            case 5:
                qa<zzft$zzi> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzft$zzi.class) {
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
