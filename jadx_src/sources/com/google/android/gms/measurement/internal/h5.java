package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.measurement.internal.zziq;
import e6.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h5 extends d7 {
    static final Pair<String, Long> B = new Pair<>(BuildConfig.FLAVOR, 0L);
    public final j5 A;

    /* renamed from: c  reason: collision with root package name */
    private SharedPreferences f16601c;

    /* renamed from: d  reason: collision with root package name */
    private Object f16602d;

    /* renamed from: e  reason: collision with root package name */
    private SharedPreferences f16603e;

    /* renamed from: f  reason: collision with root package name */
    public l5 f16604f;

    /* renamed from: g  reason: collision with root package name */
    public final m5 f16605g;

    /* renamed from: h  reason: collision with root package name */
    public final m5 f16606h;

    /* renamed from: i  reason: collision with root package name */
    public final n5 f16607i;

    /* renamed from: j  reason: collision with root package name */
    private String f16608j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f16609k;

    /* renamed from: l  reason: collision with root package name */
    private long f16610l;

    /* renamed from: m  reason: collision with root package name */
    public final m5 f16611m;

    /* renamed from: n  reason: collision with root package name */
    public final k5 f16612n;

    /* renamed from: o  reason: collision with root package name */
    public final n5 f16613o;

    /* renamed from: p  reason: collision with root package name */
    public final j5 f16614p;
    public final k5 q;

    /* renamed from: r  reason: collision with root package name */
    public final m5 f16615r;

    /* renamed from: s  reason: collision with root package name */
    public final m5 f16616s;

    /* renamed from: t  reason: collision with root package name */
    public boolean f16617t;

    /* renamed from: u  reason: collision with root package name */
    public k5 f16618u;

    /* renamed from: v  reason: collision with root package name */
    public k5 f16619v;

    /* renamed from: w  reason: collision with root package name */
    public m5 f16620w;

    /* renamed from: x  reason: collision with root package name */
    public final n5 f16621x;

    /* renamed from: y  reason: collision with root package name */
    public final n5 f16622y;

    /* renamed from: z  reason: collision with root package name */
    public final m5 f16623z;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h5(f6 f6Var) {
        super(f6Var);
        this.f16602d = new Object();
        this.f16611m = new m5(this, "session_timeout", 1800000L);
        this.f16612n = new k5(this, "start_new_session", true);
        this.f16615r = new m5(this, "last_pause_time", 0L);
        this.f16616s = new m5(this, "session_id", 0L);
        this.f16613o = new n5(this, "non_personalized_ads", null);
        this.f16614p = new j5(this, "last_received_uri_timestamps_by_source", null);
        this.q = new k5(this, "allow_remote_dynamite", false);
        this.f16605g = new m5(this, "first_open_time", 0L);
        this.f16606h = new m5(this, "app_install_time", 0L);
        this.f16607i = new n5(this, "app_instance_id", null);
        this.f16618u = new k5(this, "app_backgrounded", false);
        this.f16619v = new k5(this, "deep_link_retrieval_complete", false);
        this.f16620w = new m5(this, "deep_link_retrieval_attempts", 0L);
        this.f16621x = new n5(this, "firebase_feature_rollouts", null);
        this.f16622y = new n5(this, "deferred_attribution_cache", null);
        this.f16623z = new m5(this, "deferred_attribution_cache_timestamp", 0L);
        this.A = new j5(this, "default_event_parameters", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean A() {
        SharedPreferences sharedPreferences = this.f16601c;
        if (sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.contains("deferred_analytics_collection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void B(Boolean bool) {
        k();
        SharedPreferences.Editor edit = G().edit();
        if (bool != null) {
            edit.putBoolean("measurement_enabled_from_api", bool.booleanValue());
        } else {
            edit.remove("measurement_enabled_from_api");
        }
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void C(String str) {
        k();
        SharedPreferences.Editor edit = G().edit();
        edit.putString("admob_app_id", str);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void D(boolean z4) {
        k();
        i().I().b("App measurement setting deferred collection", Boolean.valueOf(z4));
        SharedPreferences.Editor edit = G().edit();
        edit.putBoolean("deferred_analytics_collection", z4);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final SharedPreferences E() {
        k();
        n();
        if (this.f16603e == null) {
            synchronized (this.f16602d) {
                if (this.f16603e == null) {
                    String str = zza().getPackageName() + "_preferences";
                    i().I().b("Default prefs file", str);
                    this.f16603e = zza().getSharedPreferences(str, 0);
                }
            }
        }
        return this.f16603e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void F(String str) {
        k();
        SharedPreferences.Editor edit = G().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final SharedPreferences G() {
        k();
        n();
        n6.j.l(this.f16601c);
        return this.f16601c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SparseArray<Long> H() {
        Bundle a9 = this.f16614p.a();
        if (a9 == null) {
            return new SparseArray<>();
        }
        int[] intArray = a9.getIntArray("uriSources");
        long[] longArray = a9.getLongArray("uriTimestamps");
        if (intArray == null || longArray == null) {
            return new SparseArray<>();
        }
        if (intArray.length != longArray.length) {
            i().E().a("Trigger URI source and timestamp array lengths do not match");
            return new SparseArray<>();
        }
        SparseArray<Long> sparseArray = new SparseArray<>();
        for (int i8 = 0; i8 < intArray.length; i8++) {
            sparseArray.put(intArray[i8], Long.valueOf(longArray[i8]));
        }
        return sparseArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final v I() {
        k();
        return v.d(G().getString("dma_consent_settings", null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zziq J() {
        k();
        return zziq.i(G().getString("consent_settings", "G1"), G().getInt("consent_source", 100));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Boolean K() {
        k();
        if (G().contains("use_service")) {
            return Boolean.valueOf(G().getBoolean("use_service", false));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Boolean L() {
        k();
        if (G().contains("measurement_enabled_from_api")) {
            return Boolean.valueOf(G().getBoolean("measurement_enabled_from_api", true));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Boolean M() {
        k();
        if (G().contains("measurement_enabled")) {
            return Boolean.valueOf(G().getBoolean("measurement_enabled", true));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String N() {
        k();
        String string = G().getString("previous_os_version", null);
        c().n();
        String str = Build.VERSION.RELEASE;
        if (!TextUtils.isEmpty(str) && !str.equals(string)) {
            SharedPreferences.Editor edit = G().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String O() {
        k();
        return G().getString("admob_app_id", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String P() {
        k();
        return G().getString("gmp_app_id", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void Q() {
        k();
        Boolean M = M();
        SharedPreferences.Editor edit = G().edit();
        edit.clear();
        edit.apply();
        if (M != null) {
            t(M);
        }
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final void m() {
        SharedPreferences sharedPreferences = zza().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.f16601c = sharedPreferences;
        boolean z4 = sharedPreferences.getBoolean("has_been_opened", false);
        this.f16617t = z4;
        if (!z4) {
            SharedPreferences.Editor edit = this.f16601c.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.f16604f = new l5(this, "health_monitor", Math.max(0L, c0.f16372e.a(null).longValue()));
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final boolean r() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Pair<String, Boolean> s(String str) {
        k();
        if (J().m(zziq.zza.AD_STORAGE)) {
            long b9 = zzb().b();
            if (this.f16608j == null || b9 >= this.f16610l) {
                this.f16610l = b9 + a().y(str);
                e6.a.b(true);
                try {
                    a.C0166a a9 = e6.a.a(zza());
                    this.f16608j = BuildConfig.FLAVOR;
                    String a10 = a9.a();
                    if (a10 != null) {
                        this.f16608j = a10;
                    }
                    this.f16609k = a9.b();
                } catch (Exception e8) {
                    i().D().b("Unable to get advertising id", e8);
                    this.f16608j = BuildConfig.FLAVOR;
                }
                e6.a.b(false);
                return new Pair<>(this.f16608j, Boolean.valueOf(this.f16609k));
            }
            return new Pair<>(this.f16608j, Boolean.valueOf(this.f16609k));
        }
        return new Pair<>(BuildConfig.FLAVOR, Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void t(Boolean bool) {
        k();
        SharedPreferences.Editor edit = G().edit();
        if (bool != null) {
            edit.putBoolean("measurement_enabled", bool.booleanValue());
        } else {
            edit.remove("measurement_enabled");
        }
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void u(boolean z4) {
        k();
        SharedPreferences.Editor edit = G().edit();
        edit.putBoolean("use_service", z4);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean v(int i8) {
        return zziq.l(i8, G().getInt("consent_source", 100));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean w(long j8) {
        return j8 - this.f16611m.a() > this.f16615r.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean x(v vVar) {
        k();
        if (zziq.l(vVar.a(), I().a())) {
            SharedPreferences.Editor edit = G().edit();
            edit.putString("dma_consent_settings", vVar.j());
            edit.apply();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean y(zziq zziqVar) {
        k();
        int b9 = zziqVar.b();
        if (v(b9)) {
            SharedPreferences.Editor edit = G().edit();
            edit.putString("consent_settings", zziqVar.z());
            edit.putInt("consent_source", b9);
            edit.apply();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean z(xa xaVar) {
        k();
        String string = G().getString("stored_tcf_param", BuildConfig.FLAVOR);
        String g8 = xaVar.g();
        if (g8.equals(string)) {
            return false;
        }
        SharedPreferences.Editor edit = G().edit();
        edit.putString("stored_tcf_param", g8);
        edit.apply();
        return true;
    }
}
