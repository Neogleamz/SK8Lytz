package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.google.android.gms.internal.measurement.bg;
import com.google.android.gms.internal.measurement.c4;
import com.google.android.gms.internal.measurement.d4;
import com.google.android.gms.internal.measurement.kg;
import com.google.android.gms.internal.measurement.mg;
import com.google.android.gms.internal.measurement.zzc;
import com.google.android.gms.internal.measurement.zzfn$zza;
import com.google.android.gms.internal.measurement.zzkb;
import com.google.android.gms.measurement.internal.r5;
import com.google.android.gms.measurement.internal.zziq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r5 extends bb implements g {

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, Map<String, String>> f16932d;

    /* renamed from: e  reason: collision with root package name */
    private final Map<String, Set<String>> f16933e;

    /* renamed from: f  reason: collision with root package name */
    private final Map<String, Map<String, Boolean>> f16934f;

    /* renamed from: g  reason: collision with root package name */
    private final Map<String, Map<String, Boolean>> f16935g;

    /* renamed from: h  reason: collision with root package name */
    private final Map<String, com.google.android.gms.internal.measurement.d4> f16936h;

    /* renamed from: i  reason: collision with root package name */
    private final Map<String, Map<String, Integer>> f16937i;

    /* renamed from: j  reason: collision with root package name */
    final k0.e<String, com.google.android.gms.internal.measurement.b0> f16938j;

    /* renamed from: k  reason: collision with root package name */
    final kg f16939k;

    /* renamed from: l  reason: collision with root package name */
    private final Map<String, String> f16940l;

    /* renamed from: m  reason: collision with root package name */
    private final Map<String, String> f16941m;

    /* renamed from: n  reason: collision with root package name */
    private final Map<String, String> f16942n;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r5(gb gbVar) {
        super(gbVar);
        this.f16932d = new k0.a();
        this.f16933e = new k0.a();
        this.f16934f = new k0.a();
        this.f16935g = new k0.a();
        this.f16936h = new k0.a();
        this.f16940l = new k0.a();
        this.f16941m = new k0.a();
        this.f16942n = new k0.a();
        this.f16937i = new k0.a();
        this.f16938j = new x5(this, 20);
        this.f16939k = new w5(this);
    }

    private static zziq.zza A(zzfn$zza.zze zzeVar) {
        int i8 = y5.f17176b[zzeVar.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 != 3) {
                    if (i8 != 4) {
                        return null;
                    }
                    return zziq.zza.AD_PERSONALIZATION;
                }
                return zziq.zza.AD_USER_DATA;
            }
            return zziq.zza.ANALYTICS_STORAGE;
        }
        return zziq.zza.AD_STORAGE;
    }

    private static Map<String, String> B(com.google.android.gms.internal.measurement.d4 d4Var) {
        k0.a aVar = new k0.a();
        if (d4Var != null) {
            for (com.google.android.gms.internal.measurement.g4 g4Var : d4Var.X()) {
                aVar.put(g4Var.I(), g4Var.J());
            }
        }
        return aVar;
    }

    private final void D(String str, d4.a aVar) {
        HashSet hashSet = new HashSet();
        k0.a aVar2 = new k0.a();
        k0.a aVar3 = new k0.a();
        k0.a aVar4 = new k0.a();
        if (aVar != null) {
            for (com.google.android.gms.internal.measurement.b4 b4Var : aVar.D()) {
                hashSet.add(b4Var.I());
            }
            for (int i8 = 0; i8 < aVar.x(); i8++) {
                c4.a z4 = aVar.y(i8).z();
                if (z4.z().isEmpty()) {
                    i().J().a("EventConfig contained null event name");
                } else {
                    String z8 = z4.z();
                    String b9 = f7.o.b(z4.z());
                    if (!TextUtils.isEmpty(b9)) {
                        z4 = z4.y(b9);
                        aVar.z(i8, z4);
                    }
                    if (z4.C() && z4.A()) {
                        aVar2.put(z8, Boolean.TRUE);
                    }
                    if (z4.D() && z4.B()) {
                        aVar3.put(z4.z(), Boolean.TRUE);
                    }
                    if (z4.E()) {
                        if (z4.x() < 2 || z4.x() > 65535) {
                            i().J().c("Invalid sampling rate. Event name, sample rate", z4.z(), Integer.valueOf(z4.x()));
                        } else {
                            aVar4.put(z4.z(), Integer.valueOf(z4.x()));
                        }
                    }
                }
            }
        }
        this.f16933e.put(str, hashSet);
        this.f16934f.put(str, aVar2);
        this.f16935g.put(str, aVar3);
        this.f16937i.put(str, aVar4);
    }

    private final void E(final String str, com.google.android.gms.internal.measurement.d4 d4Var) {
        if (d4Var.m() == 0) {
            this.f16938j.e(str);
            return;
        }
        i().I().b("EES programs found", Integer.valueOf(d4Var.m()));
        com.google.android.gms.internal.measurement.i5 i5Var = d4Var.W().get(0);
        try {
            com.google.android.gms.internal.measurement.b0 b0Var = new com.google.android.gms.internal.measurement.b0();
            b0Var.c("internal.remoteConfig", new Callable() { // from class: com.google.android.gms.measurement.internal.s5
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return new com.google.android.gms.internal.measurement.za("internal.remoteConfig", new z5(r5.this, str));
                }
            });
            b0Var.c("internal.appMetadata", new Callable() { // from class: f7.k
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    final r5 r5Var = r5.this;
                    final String str2 = str;
                    return new mg("internal.appMetadata", new Callable() { // from class: com.google.android.gms.measurement.internal.u5
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            r5 r5Var2 = r5.this;
                            String str3 = str2;
                            y3 C0 = r5Var2.o().C0(str3);
                            HashMap hashMap = new HashMap();
                            hashMap.put("platform", "android");
                            hashMap.put("package_name", str3);
                            hashMap.put("gmp_version", 87000L);
                            if (C0 != null) {
                                String k8 = C0.k();
                                if (k8 != null) {
                                    hashMap.put("app_version", k8);
                                }
                                hashMap.put("app_version_int", Long.valueOf(C0.O()));
                                hashMap.put("dynamite_version", Long.valueOf(C0.p0()));
                            }
                            return hashMap;
                        }
                    });
                }
            });
            b0Var.c("internal.logger", new Callable() { // from class: com.google.android.gms.measurement.internal.v5
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return new bg(r5.this.f16939k);
                }
            });
            b0Var.b(i5Var);
            this.f16938j.d(str, b0Var);
            i().I().c("EES program loaded for appId, activities", str, Integer.valueOf(i5Var.H().m()));
            for (com.google.android.gms.internal.measurement.h5 h5Var : i5Var.H().J()) {
                i().I().b("EES program activity", h5Var.I());
            }
        } catch (zzc unused) {
            i().E().b("Failed to load EES program. appId", str);
        }
    }

    private final void f0(String str) {
        s();
        k();
        n6.j.f(str);
        if (this.f16936h.get(str) == null) {
            n E0 = o().E0(str);
            if (E0 != null) {
                d4.a z4 = y(str, E0.f16807a).z();
                D(str, z4);
                this.f16932d.put(str, B((com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n())));
                this.f16936h.put(str, (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n()));
                E(str, (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n()));
                this.f16940l.put(str, z4.B());
                this.f16941m.put(str, E0.f16808b);
                this.f16942n.put(str, E0.f16809c);
                return;
            }
            this.f16932d.put(str, null);
            this.f16934f.put(str, null);
            this.f16933e.put(str, null);
            this.f16935g.put(str, null);
            this.f16936h.put(str, null);
            this.f16940l.put(str, null);
            this.f16941m.put(str, null);
            this.f16942n.put(str, null);
            this.f16937i.put(str, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ com.google.android.gms.internal.measurement.b0 x(r5 r5Var, String str) {
        r5Var.s();
        n6.j.f(str);
        if (r5Var.V(str)) {
            if (!r5Var.f16936h.containsKey(str) || r5Var.f16936h.get(str) == null) {
                r5Var.f0(str);
            } else {
                r5Var.E(str, r5Var.f16936h.get(str));
            }
            return r5Var.f16938j.h().get(str);
        }
        return null;
    }

    private final com.google.android.gms.internal.measurement.d4 y(String str, byte[] bArr) {
        if (bArr == null) {
            return com.google.android.gms.internal.measurement.d4.Q();
        }
        try {
            com.google.android.gms.internal.measurement.d4 d4Var = (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) ((d4.a) nb.E(com.google.android.gms.internal.measurement.d4.O(), bArr)).n());
            i().I().c("Parsed config. version, gmp_app_id", d4Var.d0() ? Long.valueOf(d4Var.M()) : null, d4Var.b0() ? d4Var.S() : null);
            return d4Var;
        } catch (zzkb | RuntimeException e8) {
            i().J().c("Unable to merge remote config. appId", x4.t(str), e8);
            return com.google.android.gms.internal.measurement.d4.Q();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean F(String str, byte[] bArr, String str2, String str3) {
        s();
        k();
        n6.j.f(str);
        d4.a z4 = y(str, bArr).z();
        if (z4 == null) {
            return false;
        }
        D(str, z4);
        E(str, (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n()));
        this.f16936h.put(str, (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n()));
        this.f16940l.put(str, z4.B());
        this.f16941m.put(str, str2);
        this.f16942n.put(str, str3);
        this.f16932d.put(str, B((com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n())));
        o().X(str, new ArrayList(z4.C()));
        try {
            z4.A();
            bArr = ((com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n())).k();
        } catch (RuntimeException e8) {
            i().J().c("Unable to serialize reduced-size config. Storing full config instead. appId", x4.t(str), e8);
        }
        l o5 = o();
        n6.j.f(str);
        o5.k();
        o5.s();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr);
        contentValues.put("config_last_modified_time", str2);
        contentValues.put("e_tag", str3);
        try {
            if (o5.z().update("apps", contentValues, "app_id = ?", new String[]{str}) == 0) {
                o5.i().E().b("Failed to update remote config (got 0). appId", x4.t(str));
            }
        } catch (SQLiteException e9) {
            o5.i().E().c("Error storing remote config. appId", x4.t(str), e9);
        }
        this.f16936h.put(str, (com.google.android.gms.internal.measurement.d4) ((com.google.android.gms.internal.measurement.x8) z4.n()));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int G(String str, String str2) {
        Integer num;
        k();
        f0(str);
        Map<String, Integer> map = this.f16937i.get(str);
        if (map == null || (num = map.get(str2)) == null) {
            return 1;
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzfn$zza H(String str) {
        k();
        f0(str);
        com.google.android.gms.internal.measurement.d4 J = J(str);
        if (J == null || !J.a0()) {
            return null;
        }
        return J.N();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zziq.zza I(String str, zziq.zza zzaVar) {
        k();
        f0(str);
        zzfn$zza H = H(str);
        if (H == null) {
            return null;
        }
        for (zzfn$zza.c cVar : H.L()) {
            if (zzaVar == A(cVar.J())) {
                return A(cVar.I());
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final com.google.android.gms.internal.measurement.d4 J(String str) {
        s();
        k();
        n6.j.f(str);
        f0(str);
        return this.f16936h.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean K(String str, zziq.zza zzaVar) {
        k();
        f0(str);
        zzfn$zza H = H(str);
        if (H == null) {
            return false;
        }
        Iterator<zzfn$zza.b> it = H.K().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            zzfn$zza.b next = it.next();
            if (zzaVar == A(next.J())) {
                if (next.I() == zzfn$zza.zzd.GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean L(String str, String str2) {
        Boolean bool;
        k();
        f0(str);
        if ("ecommerce_purchase".equals(str2) || "purchase".equals(str2) || "refund".equals(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.f16935g.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String M(String str) {
        k();
        return this.f16942n.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean N(String str, String str2) {
        Boolean bool;
        k();
        f0(str);
        if (W(str) && sb.H0(str2)) {
            return true;
        }
        if (Y(str) && sb.J0(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.f16934f.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String O(String str) {
        k();
        return this.f16941m.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String P(String str) {
        k();
        f0(str);
        return this.f16940l.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<String> Q(String str) {
        k();
        f0(str);
        return this.f16933e.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SortedSet<String> R(String str) {
        k();
        f0(str);
        TreeSet treeSet = new TreeSet();
        zzfn$zza H = H(str);
        if (H == null) {
            return treeSet;
        }
        for (zzfn$zza.d dVar : H.J()) {
            treeSet.add(dVar.I());
        }
        return treeSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void S(String str) {
        k();
        this.f16941m.put(str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void T(String str) {
        k();
        this.f16936h.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean U(String str) {
        k();
        com.google.android.gms.internal.measurement.d4 J = J(str);
        if (J == null) {
            return false;
        }
        return J.Y();
    }

    public final boolean V(String str) {
        com.google.android.gms.internal.measurement.d4 d4Var;
        return (TextUtils.isEmpty(str) || (d4Var = this.f16936h.get(str)) == null || d4Var.m() == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean W(String str) {
        return "1".equals(d(str, "measurement.upload.blacklist_internal"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean X(String str) {
        k();
        f0(str);
        zzfn$zza H = H(str);
        return H == null || !H.O() || H.N();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean Y(String str) {
        return "1".equals(d(str, "measurement.upload.blacklist_public"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean Z(String str) {
        k();
        f0(str);
        return this.f16933e.get(str) != null && this.f16933e.get(str).contains("app_instance_id");
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a0(String str) {
        k();
        f0(str);
        if (this.f16933e.get(str) != null) {
            return this.f16933e.get(str).contains("device_model") || this.f16933e.get(str).contains("device_info");
        }
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b0(String str) {
        k();
        f0(str);
        return this.f16933e.get(str) != null && this.f16933e.get(str).contains("enhanced_user_id");
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean c0(String str) {
        k();
        f0(str);
        return this.f16933e.get(str) != null && this.f16933e.get(str).contains("google_signals");
    }

    @Override // com.google.android.gms.measurement.internal.g
    public final String d(String str, String str2) {
        k();
        f0(str);
        Map<String, String> map = this.f16932d.get(str);
        if (map != null) {
            return map.get(str2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean d0(String str) {
        k();
        f0(str);
        if (this.f16933e.get(str) != null) {
            return this.f16933e.get(str).contains("os_version") || this.f16933e.get(str).contains("device_info");
        }
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean e0(String str) {
        k();
        f0(str);
        return this.f16933e.get(str) != null && this.f16933e.get(str).contains("user_id");
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ nb m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ wb n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ l o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ r5 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ ia q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ eb r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long w(String str) {
        String d8 = d(str, "measurement.account.time_zone_offset_minutes");
        if (TextUtils.isEmpty(d8)) {
            return 0L;
        }
        try {
            return Long.parseLong(d8);
        } catch (NumberFormatException e8) {
            i().J().c("Unable to parse timezone offset. appId", x4.t(str), e8);
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzip z(String str, zziq.zza zzaVar) {
        k();
        f0(str);
        zzfn$zza H = H(str);
        if (H == null) {
            return zzip.UNINITIALIZED;
        }
        for (zzfn$zza.b bVar : H.M()) {
            if (A(bVar.J()) == zzaVar) {
                int i8 = y5.f17177c[bVar.I().ordinal()];
                return i8 != 1 ? i8 != 2 ? zzip.UNINITIALIZED : zzip.GRANTED : zzip.DENIED;
            }
        }
        return zzip.UNINITIALIZED;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
