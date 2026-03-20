package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzfh$zzd extends x8<zzfh$zzd, a> implements ka {
    private static final zzfh$zzd zzc;
    private static volatile qa<zzfh$zzd> zzd;
    private int zze;
    private int zzf;
    private boolean zzg;
    private String zzh = BuildConfig.FLAVOR;
    private String zzi = BuildConfig.FLAVOR;
    private String zzj = BuildConfig.FLAVOR;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzfh$zzd, a> implements ka {
        private a() {
            super(zzfh$zzd.zzc);
        }

        /* synthetic */ a(r3 r3Var) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zzb implements z8 {
        UNKNOWN_COMPARISON_TYPE(0),
        LESS_THAN(1),
        GREATER_THAN(2),
        EQUAL(3),
        BETWEEN(4);
        

        /* renamed from: g  reason: collision with root package name */
        private static final c9<zzb> f12798g = new w3();

        /* renamed from: a  reason: collision with root package name */
        private final int f12800a;

        zzb(int i8) {
            this.f12800a = i8;
        }

        public static zzb c(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        if (i8 != 3) {
                            if (i8 != 4) {
                                return null;
                            }
                            return BETWEEN;
                        }
                        return EQUAL;
                    }
                    return GREATER_THAN;
                }
                return LESS_THAN;
            }
            return UNKNOWN_COMPARISON_TYPE;
        }

        public static b9 f() {
            return x3.f12649a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12800a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12800a;
        }
    }

    static {
        zzfh$zzd zzfh_zzd = new zzfh$zzd();
        zzc = zzfh_zzd;
        x8.v(zzfh$zzd.class, zzfh_zzd);
    }

    private zzfh$zzd() {
    }

    public static zzfh$zzd J() {
        return zzc;
    }

    public final zzb H() {
        zzb c9 = zzb.c(this.zzf);
        return c9 == null ? zzb.UNKNOWN_COMPARISON_TYPE : c9;
    }

    public final String K() {
        return this.zzh;
    }

    public final String L() {
        return this.zzj;
    }

    public final String M() {
        return this.zzi;
    }

    public final boolean N() {
        return this.zzg;
    }

    public final boolean O() {
        return (this.zze & 1) != 0;
    }

    public final boolean P() {
        return (this.zze & 4) != 0;
    }

    public final boolean Q() {
        return (this.zze & 2) != 0;
    }

    public final boolean R() {
        return (this.zze & 16) != 0;
    }

    public final boolean S() {
        return (this.zze & 8) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (r3.f12474a[i8 - 1]) {
            case 1:
                return new zzfh$zzd();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001᠌\u0000\u0002ဇ\u0001\u0003ဈ\u0002\u0004ဈ\u0003\u0005ဈ\u0004", new Object[]{"zze", "zzf", zzb.f(), "zzg", "zzh", "zzi", "zzj"});
            case 4:
                return zzc;
            case 5:
                qa<zzfh$zzd> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzfh$zzd.class) {
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
