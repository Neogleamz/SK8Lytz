package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z8 extends v4 {

    /* renamed from: c  reason: collision with root package name */
    private volatile x8 f17232c;

    /* renamed from: d  reason: collision with root package name */
    private volatile x8 f17233d;

    /* renamed from: e  reason: collision with root package name */
    protected x8 f17234e;

    /* renamed from: f  reason: collision with root package name */
    private final Map<Activity, x8> f17235f;

    /* renamed from: g  reason: collision with root package name */
    private Activity f17236g;

    /* renamed from: h  reason: collision with root package name */
    private volatile boolean f17237h;

    /* renamed from: i  reason: collision with root package name */
    private volatile x8 f17238i;

    /* renamed from: j  reason: collision with root package name */
    private x8 f17239j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f17240k;

    /* renamed from: l  reason: collision with root package name */
    private final Object f17241l;

    public z8(f6 f6Var) {
        super(f6Var);
        this.f17241l = new Object();
        this.f17235f = new ConcurrentHashMap();
    }

    private final String B(Class<?> cls, String str) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            return str;
        }
        String[] split = canonicalName.split("\\.");
        String str2 = split.length > 0 ? split[split.length - 1] : BuildConfig.FLAVOR;
        return str2.length() > a().p(null, false) ? str2.substring(0, a().p(null, false)) : str2;
    }

    private final void E(Activity activity, x8 x8Var, boolean z4) {
        x8 x8Var2;
        x8 x8Var3 = this.f17232c == null ? this.f17233d : this.f17232c;
        if (x8Var.f17129b == null) {
            x8Var2 = new x8(x8Var.f17128a, activity != null ? B(activity.getClass(), "Activity") : null, x8Var.f17130c, x8Var.f17132e, x8Var.f17133f);
        } else {
            x8Var2 = x8Var;
        }
        this.f17233d = this.f17232c;
        this.f17232c = x8Var2;
        l().B(new b9(this, x8Var2, x8Var3, zzb().b(), z4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r8v5, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r8v6, types: [long, android.os.Bundle] */
    public final void H(x8 x8Var, x8 x8Var2, long j8, boolean z4, Bundle bundle) {
        long j9;
        k();
        boolean z8 = false;
        boolean z9 = (x8Var2 != null && x8Var2.f17130c == x8Var.f17130c && Objects.equals(x8Var2.f17129b, x8Var.f17129b) && Objects.equals(x8Var2.f17128a, x8Var.f17128a)) ? false : true;
        if (z4 && this.f17234e != null) {
            z8 = true;
        }
        if (z9) {
            sb.V(x8Var, bundle != null ? new Bundle(bundle) : new Bundle(), true);
            if (x8Var2 != null) {
                String str = x8Var2.f17128a;
                if (str != null) {
                    "_pn".putString("_pn", str);
                }
                String str2 = x8Var2.f17129b;
                if (str2 != null) {
                    "_pc".putString("_pc", str2);
                }
                ?? r8 = x8Var2.f17130c;
                r8.putLong("_pi", r8);
            }
            ?? r82 = 0;
            if (z8) {
                long a9 = s().f16842f.a(j8);
                if (a9 > 0) {
                    g().K(null, a9);
                }
            }
            if (!a().R()) {
                r82.putLong("_mst", 1L);
            }
            String str3 = x8Var.f17132e ? "app" : "auto";
            long a10 = zzb().a();
            if (x8Var.f17132e) {
                a10 = x8Var.f17133f;
                if (a10 != 0) {
                    j9 = a10;
                    p().T(str3, "_vs", j9, null);
                }
            }
            j9 = a10;
            p().T(str3, "_vs", j9, null);
        }
        if (z8) {
            I(this.f17234e, true, j8);
        }
        this.f17234e = x8Var;
        if (x8Var.f17132e) {
            this.f17239j = x8Var;
        }
        r().H(x8Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I(x8 x8Var, boolean z4, long j8) {
        m().t(zzb().b());
        if (!s().C(x8Var != null && x8Var.f17131d, z4, j8) || x8Var == null) {
            return;
        }
        x8Var.f17131d = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void J(z8 z8Var, Bundle bundle, x8 x8Var, x8 x8Var2, long j8) {
        if (bundle != null) {
            bundle.remove("screen_name");
            bundle.remove("screen_class");
        }
        z8Var.H(x8Var, x8Var2, j8, true, z8Var.g().D(null, "screen_view", bundle, null, false));
    }

    private final x8 R(Activity activity) {
        n6.j.l(activity);
        x8 x8Var = this.f17235f.get(activity);
        if (x8Var == null) {
            x8 x8Var2 = new x8(null, B(activity.getClass(), "Activity"), g().P0());
            this.f17235f.put(activity, x8Var2);
            x8Var = x8Var2;
        }
        return this.f17238i != null ? this.f17238i : x8Var;
    }

    public final x8 A(boolean z4) {
        t();
        k();
        if (z4) {
            x8 x8Var = this.f17234e;
            return x8Var != null ? x8Var : this.f17239j;
        }
        return this.f17234e;
    }

    public final void C(Activity activity) {
        synchronized (this.f17241l) {
            if (activity == this.f17236g) {
                this.f17236g = null;
            }
        }
        if (a().R()) {
            this.f17235f.remove(activity);
        }
    }

    public final void D(Activity activity, Bundle bundle) {
        Bundle bundle2;
        if (!a().R() || bundle == null || (bundle2 = bundle.getBundle("com.google.app_measurement.screen_service")) == null) {
            return;
        }
        this.f17235f.put(activity, new x8(bundle2.getString("name"), bundle2.getString("referrer_name"), bundle2.getLong("id")));
    }

    @Deprecated
    public final void F(Activity activity, String str, String str2) {
        if (!a().R()) {
            i().K().a("setCurrentScreen cannot be called while screen reporting is disabled.");
            return;
        }
        x8 x8Var = this.f17232c;
        if (x8Var == null) {
            i().K().a("setCurrentScreen cannot be called while no activity active");
        } else if (this.f17235f.get(activity) == null) {
            i().K().a("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = B(activity.getClass(), "Activity");
            }
            boolean equals = Objects.equals(x8Var.f17129b, str2);
            boolean equals2 = Objects.equals(x8Var.f17128a, str);
            if (equals && equals2) {
                i().K().a("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > a().p(null, false))) {
                i().K().b("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 != null && (str2.length() <= 0 || str2.length() > a().p(null, false))) {
                i().K().b("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            } else {
                i().I().c("Setting current screen to name, class", str == null ? "null" : str, str2);
                x8 x8Var2 = new x8(str, str2, g().P0());
                this.f17235f.put(activity, x8Var2);
                E(activity, x8Var2, true);
            }
        }
    }

    public final void G(Bundle bundle, long j8) {
        String str;
        synchronized (this.f17241l) {
            if (!this.f17240k) {
                i().K().a("Cannot log screen view event when the app is in the background.");
                return;
            }
            String str2 = null;
            if (bundle != null) {
                String string = bundle.getString("screen_name");
                if (string != null && (string.length() <= 0 || string.length() > a().p(null, false))) {
                    i().K().b("Invalid screen name length for screen view. Length", Integer.valueOf(string.length()));
                    return;
                }
                String string2 = bundle.getString("screen_class");
                if (string2 != null && (string2.length() <= 0 || string2.length() > a().p(null, false))) {
                    i().K().b("Invalid screen class length for screen view. Length", Integer.valueOf(string2.length()));
                    return;
                } else {
                    str = string;
                    str2 = string2;
                }
            } else {
                str = null;
            }
            if (str2 == null) {
                Activity activity = this.f17236g;
                str2 = activity != null ? B(activity.getClass(), "Activity") : "Activity";
            }
            String str3 = str2;
            x8 x8Var = this.f17232c;
            if (this.f17237h && x8Var != null) {
                this.f17237h = false;
                boolean equals = Objects.equals(x8Var.f17129b, str3);
                boolean equals2 = Objects.equals(x8Var.f17128a, str);
                if (equals && equals2) {
                    i().K().a("Ignoring call to log screen view event with duplicate parameters.");
                    return;
                }
            }
            i().I().c("Logging screen view with name, class", str == null ? "null" : str, str3 == null ? "null" : str3);
            x8 x8Var2 = this.f17232c == null ? this.f17233d : this.f17232c;
            x8 x8Var3 = new x8(str, str3, g().P0(), true, j8);
            this.f17232c = x8Var3;
            this.f17233d = x8Var2;
            this.f17238i = x8Var3;
            l().B(new y8(this, bundle, x8Var3, x8Var2, zzb().b()));
        }
    }

    public final x8 N() {
        return this.f17232c;
    }

    public final void O(Activity activity) {
        synchronized (this.f17241l) {
            this.f17240k = false;
            this.f17237h = true;
        }
        long b9 = zzb().b();
        if (!a().R()) {
            this.f17232c = null;
            l().B(new d9(this, b9));
            return;
        }
        x8 R = R(activity);
        this.f17233d = this.f17232c;
        this.f17232c = null;
        l().B(new c9(this, R, b9));
    }

    public final void P(Activity activity, Bundle bundle) {
        x8 x8Var;
        if (!a().R() || bundle == null || (x8Var = this.f17235f.get(activity)) == null) {
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putLong("id", x8Var.f17130c);
        bundle2.putString("name", x8Var.f17128a);
        bundle2.putString("referrer_name", x8Var.f17129b);
        bundle.putBundle("com.google.app_measurement.screen_service", bundle2);
    }

    public final void Q(Activity activity) {
        synchronized (this.f17241l) {
            this.f17240k = true;
            if (activity != this.f17236g) {
                synchronized (this.f17241l) {
                    this.f17236g = activity;
                    this.f17237h = false;
                }
                if (a().R()) {
                    this.f17238i = null;
                    l().B(new g9(this));
                }
            }
        }
        if (!a().R()) {
            this.f17232c = this.f17238i;
            l().B(new a9(this));
            return;
        }
        E(activity, R(activity), false);
        a m8 = m();
        m8.l().B(new x2(m8, m8.zzb().b()));
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ a m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ r4 n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ q4 o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ h7 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ z8 q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ f9 r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ na s() {
        return super.s();
    }

    @Override // com.google.android.gms.measurement.internal.v4
    protected final boolean y() {
        return false;
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
