package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzgb$zzd extends x8<zzgb$zzd, a> implements ka {
    private static final zzgb$zzd zzc;
    private static volatile qa<zzgb$zzd> zzd;
    private int zze;
    private int zzf;
    private g9<zzgb$zzd> zzg = x8.D();
    private String zzh = BuildConfig.FLAVOR;
    private String zzi = BuildConfig.FLAVOR;
    private boolean zzj;
    private double zzk;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzgb$zzd, a> implements ka {
        private a() {
            super(zzgb$zzd.zzc);
        }

        /* synthetic */ a(j5 j5Var) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zza implements z8 {
        UNKNOWN(0),
        STRING(1),
        NUMBER(2),
        BOOLEAN(3),
        STATEMENT(4);
        

        /* renamed from: g  reason: collision with root package name */
        private static final c9<zza> f12849g = new l5();

        /* renamed from: a  reason: collision with root package name */
        private final int f12851a;

        zza(int i8) {
            this.f12851a = i8;
        }

        public static zza c(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        if (i8 != 3) {
                            if (i8 != 4) {
                                return null;
                            }
                            return STATEMENT;
                        }
                        return BOOLEAN;
                    }
                    return NUMBER;
                }
                return STRING;
            }
            return UNKNOWN;
        }

        public static b9 f() {
            return k5.f12279a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12851a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12851a;
        }
    }

    static {
        zzgb$zzd zzgb_zzd = new zzgb$zzd();
        zzc = zzgb_zzd;
        x8.v(zzgb$zzd.class, zzgb_zzd);
    }

    private zzgb$zzd() {
    }

    public final double H() {
        return this.zzk;
    }

    public final zza I() {
        zza c9 = zza.c(this.zzf);
        return c9 == null ? zza.UNKNOWN : c9;
    }

    public final String K() {
        return this.zzh;
    }

    public final String L() {
        return this.zzi;
    }

    public final List<zzgb$zzd> M() {
        return this.zzg;
    }

    public final boolean N() {
        return this.zzj;
    }

    public final boolean O() {
        return (this.zze & 8) != 0;
    }

    public final boolean P() {
        return (this.zze & 16) != 0;
    }

    public final boolean Q() {
        return (this.zze & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (j5.f12256a[i8 - 1]) {
            case 1:
                return new zzgb$zzd();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0001\u0000\u0001᠌\u0000\u0002\u001b\u0003ဈ\u0001\u0004ဈ\u0002\u0005ဇ\u0003\u0006က\u0004", new Object[]{"zze", "zzf", zza.f(), "zzg", zzgb$zzd.class, "zzh", "zzi", "zzj", "zzk"});
            case 4:
                return zzc;
            case 5:
                qa<zzgb$zzd> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzgb$zzd.class) {
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
