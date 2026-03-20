package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzfh$zzf extends x8<zzfh$zzf, a> implements ka {
    private static final zzfh$zzf zzc;
    private static volatile qa<zzfh$zzf> zzd;
    private int zze;
    private int zzf;
    private boolean zzh;
    private String zzg = BuildConfig.FLAVOR;
    private g9<String> zzi = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzfh$zzf, a> implements ka {
        private a() {
            super(zzfh$zzf.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zza implements z8 {
        UNKNOWN_MATCH_TYPE(0),
        REGEXP(1),
        BEGINS_WITH(2),
        ENDS_WITH(3),
        PARTIAL(4),
        EXACT(5),
        IN_LIST(6);
        

        /* renamed from: j  reason: collision with root package name */
        private static final c9<zza> f12808j = new z3();

        /* renamed from: a  reason: collision with root package name */
        private final int f12810a;

        zza(int i8) {
            this.f12810a = i8;
        }

        public static zza c(int i8) {
            switch (i8) {
                case 0:
                    return UNKNOWN_MATCH_TYPE;
                case 1:
                    return REGEXP;
                case 2:
                    return BEGINS_WITH;
                case 3:
                    return ENDS_WITH;
                case 4:
                    return PARTIAL;
                case 5:
                    return EXACT;
                case 6:
                    return IN_LIST;
                default:
                    return null;
            }
        }

        public static b9 f() {
            return y3.f12680a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12810a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12810a;
        }
    }

    static {
        zzfh$zzf zzfh_zzf = new zzfh$zzf();
        zzc = zzfh_zzf;
        x8.v(zzfh$zzf.class, zzfh_zzf);
    }

    private zzfh$zzf() {
    }

    public static zzfh$zzf J() {
        return zzc;
    }

    public final zza H() {
        zza c9 = zza.c(this.zzf);
        return c9 == null ? zza.UNKNOWN_MATCH_TYPE : c9;
    }

    public final String K() {
        return this.zzg;
    }

    public final List<String> L() {
        return this.zzi;
    }

    public final boolean M() {
        return this.zzh;
    }

    public final boolean N() {
        return (this.zze & 4) != 0;
    }

    public final boolean O() {
        return (this.zze & 2) != 0;
    }

    public final boolean P() {
        return (this.zze & 1) != 0;
    }

    public final int m() {
        return this.zzi.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new zzfh$zzf();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001᠌\u0000\u0002ဈ\u0001\u0003ဇ\u0002\u0004\u001a", new Object[]{"zze", "zzf", zza.f(), "zzg", "zzh", "zzi"});
            case 4:
                return zzc;
            case 5:
                qa<zzfh$zzf> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzfh$zzf.class) {
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
