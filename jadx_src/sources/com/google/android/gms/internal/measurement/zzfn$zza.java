package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.x8;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzfn$zza extends x8<zzfn$zza, a> implements ka {
    private static final zzfn$zza zzc;
    private static volatile qa<zzfn$zza> zzd;
    private int zze;
    private boolean zzi;
    private g9<b> zzf = x8.D();
    private g9<c> zzg = x8.D();
    private g9<d> zzh = x8.D();
    private g9<b> zzj = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzfn$zza, a> implements ka {
        private a() {
            super(zzfn$zza.zzc);
        }

        /* synthetic */ a(a4 a4Var) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends x8<b, a> implements ka {
        private static final b zzc;
        private static volatile qa<b> zzd;
        private int zze;
        private int zzf;
        private int zzg;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends x8.a<b, a> implements ka {
            private a() {
                super(b.zzc);
            }

            /* synthetic */ a(a4 a4Var) {
                this();
            }
        }

        static {
            b bVar = new b();
            zzc = bVar;
            x8.v(b.class, bVar);
        }

        private b() {
        }

        public final zzd I() {
            zzd c9 = zzd.c(this.zzg);
            return c9 == null ? zzd.CONSENT_STATUS_UNSPECIFIED : c9;
        }

        public final zze J() {
            zze c9 = zze.c(this.zzf);
            return c9 == null ? zze.CONSENT_TYPE_UNSPECIFIED : c9;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.measurement.x8
        public final Object r(int i8, Object obj, Object obj2) {
            switch (a4.f12057a[i8 - 1]) {
                case 1:
                    return new b();
                case 2:
                    return new a(null);
                case 3:
                    return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001᠌\u0000\u0002᠌\u0001", new Object[]{"zze", "zzf", zze.f(), "zzg", zzd.f()});
                case 4:
                    return zzc;
                case 5:
                    qa<b> qaVar = zzd;
                    if (qaVar == null) {
                        synchronized (b.class) {
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

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends x8<c, a> implements ka {
        private static final c zzc;
        private static volatile qa<c> zzd;
        private int zze;
        private int zzf;
        private int zzg;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends x8.a<c, a> implements ka {
            private a() {
                super(c.zzc);
            }

            /* synthetic */ a(a4 a4Var) {
                this();
            }
        }

        static {
            c cVar = new c();
            zzc = cVar;
            x8.v(c.class, cVar);
        }

        private c() {
        }

        public final zze I() {
            zze c9 = zze.c(this.zzg);
            return c9 == null ? zze.CONSENT_TYPE_UNSPECIFIED : c9;
        }

        public final zze J() {
            zze c9 = zze.c(this.zzf);
            return c9 == null ? zze.CONSENT_TYPE_UNSPECIFIED : c9;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.measurement.x8
        public final Object r(int i8, Object obj, Object obj2) {
            switch (a4.f12057a[i8 - 1]) {
                case 1:
                    return new c();
                case 2:
                    return new a(null);
                case 3:
                    return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001᠌\u0000\u0002᠌\u0001", new Object[]{"zze", "zzf", zze.f(), "zzg", zze.f()});
                case 4:
                    return zzc;
                case 5:
                    qa<c> qaVar = zzd;
                    if (qaVar == null) {
                        synchronized (c.class) {
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

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d extends x8<d, a> implements ka {
        private static final d zzc;
        private static volatile qa<d> zzd;
        private int zze;
        private String zzf = BuildConfig.FLAVOR;
        private String zzg = BuildConfig.FLAVOR;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends x8.a<d, a> implements ka {
            private a() {
                super(d.zzc);
            }

            /* synthetic */ a(a4 a4Var) {
                this();
            }
        }

        static {
            d dVar = new d();
            zzc = dVar;
            x8.v(d.class, dVar);
        }

        private d() {
        }

        public final String I() {
            return this.zzf;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.measurement.x8
        public final Object r(int i8, Object obj, Object obj2) {
            switch (a4.f12057a[i8 - 1]) {
                case 1:
                    return new d();
                case 2:
                    return new a(null);
                case 3:
                    return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001", new Object[]{"zze", "zzf", "zzg"});
                case 4:
                    return zzc;
                case 5:
                    qa<d> qaVar = zzd;
                    if (qaVar == null) {
                        synchronized (d.class) {
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

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zzd implements z8 {
        CONSENT_STATUS_UNSPECIFIED(0),
        GRANTED(1),
        DENIED(2);
        

        /* renamed from: e  reason: collision with root package name */
        private static final c9<zzd> f12814e = new i4();

        /* renamed from: a  reason: collision with root package name */
        private final int f12816a;

        zzd(int i8) {
            this.f12816a = i8;
        }

        public static zzd c(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        return null;
                    }
                    return DENIED;
                }
                return GRANTED;
            }
            return CONSENT_STATUS_UNSPECIFIED;
        }

        public static b9 f() {
            return j4.f12255a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzd.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12816a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12816a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zze implements z8 {
        CONSENT_TYPE_UNSPECIFIED(0),
        AD_STORAGE(1),
        ANALYTICS_STORAGE(2),
        AD_USER_DATA(3),
        AD_PERSONALIZATION(4);
        

        /* renamed from: g  reason: collision with root package name */
        private static final c9<zze> f12822g = new l4();

        /* renamed from: a  reason: collision with root package name */
        private final int f12824a;

        zze(int i8) {
            this.f12824a = i8;
        }

        public static zze c(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        if (i8 != 3) {
                            if (i8 != 4) {
                                return null;
                            }
                            return AD_PERSONALIZATION;
                        }
                        return AD_USER_DATA;
                    }
                    return ANALYTICS_STORAGE;
                }
                return AD_STORAGE;
            }
            return CONSENT_TYPE_UNSPECIFIED;
        }

        public static b9 f() {
            return k4.f12278a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zze.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12824a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12824a;
        }
    }

    static {
        zzfn$zza zzfn_zza = new zzfn$zza();
        zzc = zzfn_zza;
        x8.v(zzfn$zza.class, zzfn_zza);
    }

    private zzfn$zza() {
    }

    public static zzfn$zza I() {
        return zzc;
    }

    public final List<d> J() {
        return this.zzh;
    }

    public final List<b> K() {
        return this.zzf;
    }

    public final List<c> L() {
        return this.zzg;
    }

    public final List<b> M() {
        return this.zzj;
    }

    public final boolean N() {
        return this.zzi;
    }

    public final boolean O() {
        return (this.zze & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (a4.f12057a[i8 - 1]) {
            case 1:
                return new zzfn$zza();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0004\u0000\u0001\u001b\u0002\u001b\u0003\u001b\u0004ဇ\u0000\u0005\u001b", new Object[]{"zze", "zzf", b.class, "zzg", c.class, "zzh", d.class, "zzi", "zzj", b.class});
            case 4:
                return zzc;
            case 5:
                qa<zzfn$zza> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzfn$zza.class) {
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
