package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.s4;
import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzft$zzk extends x8<zzft$zzk, a> implements ka {
    private static final zzft$zzk zzc;
    private static volatile qa<zzft$zzk> zzd;
    private int zze;
    private int zzf = 1;
    private g9<s4> zzg = x8.D();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends x8.a<zzft$zzk, a> implements ka {
        private a() {
            super(zzft$zzk.zzc);
        }

        /* synthetic */ a(m4 m4Var) {
            this();
        }

        public final a x(s4.a aVar) {
            s();
            ((zzft$zzk) this.f12660b).I((s4) ((x8) aVar.n()));
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zzb implements z8 {
        RADS(1),
        PROVISIONING(2);
        

        /* renamed from: d  reason: collision with root package name */
        private static final c9<zzb> f12832d = new d5();

        /* renamed from: a  reason: collision with root package name */
        private final int f12834a;

        zzb(int i8) {
            this.f12834a = i8;
        }

        public static zzb c(int i8) {
            if (i8 != 1) {
                if (i8 != 2) {
                    return null;
                }
                return PROVISIONING;
            }
            return RADS;
        }

        public static b9 f() {
            return f5.f12178a;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.f12834a + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.measurement.z8
        public final int zza() {
            return this.f12834a;
        }
    }

    static {
        zzft$zzk zzft_zzk = new zzft$zzk();
        zzc = zzft_zzk;
        x8.v(zzft$zzk.class, zzft_zzk);
    }

    private zzft$zzk() {
    }

    public static a H() {
        return zzc.y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(s4 s4Var) {
        s4Var.getClass();
        g9<s4> g9Var = this.zzg;
        if (!g9Var.a()) {
            this.zzg = x8.q(g9Var);
        }
        this.zzg.add(s4Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.x8
    public final Object r(int i8, Object obj, Object obj2) {
        switch (m4.f12337a[i8 - 1]) {
            case 1:
                return new zzft$zzk();
            case 2:
                return new a(null);
            case 3:
                return x8.s(zzc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001᠌\u0000\u0002\u001b", new Object[]{"zze", "zzf", zzb.f(), "zzg", s4.class});
            case 4:
                return zzc;
            case 5:
                qa<zzft$zzk> qaVar = zzd;
                if (qaVar == null) {
                    synchronized (zzft$zzk.class) {
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
