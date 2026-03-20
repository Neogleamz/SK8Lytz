package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.le;
import com.google.android.gms.internal.measurement.me;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.reflect.InvocationTargetException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends e7 {

    /* renamed from: b  reason: collision with root package name */
    private Boolean f16469b;

    /* renamed from: c  reason: collision with root package name */
    private String f16470c;

    /* renamed from: d  reason: collision with root package name */
    private g f16471d;

    /* renamed from: e  reason: collision with root package name */
    private Boolean f16472e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(f6 f6Var) {
        super(f6Var);
        this.f16471d = new g() { // from class: com.google.android.gms.measurement.internal.h
            @Override // com.google.android.gms.measurement.internal.g
            public final String d(String str, String str2) {
                return null;
            }
        };
    }

    public static long G() {
        return c0.f16375f.a(null).longValue();
    }

    public static long M() {
        return c0.F.a(null).longValue();
    }

    private final Bundle V() {
        try {
            if (zza().getPackageManager() == null) {
                i().E().a("Failed to load metadata: PackageManager is null");
                return null;
            }
            ApplicationInfo c9 = w6.c.a(zza()).c(zza().getPackageName(), RecognitionOptions.ITF);
            if (c9 == null) {
                i().E().a("Failed to load metadata: ApplicationInfo is null");
                return null;
            }
            return c9.metaData;
        } catch (PackageManager.NameNotFoundException e8) {
            i().E().b("Failed to load metadata: Package name not found", e8);
            return null;
        }
    }

    private final String d(String str, String str2) {
        z4 E;
        String str3;
        try {
            String str4 = (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, str, str2);
            n6.j.l(str4);
            return str4;
        } catch (ClassNotFoundException e8) {
            e = e8;
            E = i().E();
            str3 = "Could not find SystemProperties class";
            E.b(str3, e);
            return str2;
        } catch (IllegalAccessException e9) {
            e = e9;
            E = i().E();
            str3 = "Could not access SystemProperties.get()";
            E.b(str3, e);
            return str2;
        } catch (NoSuchMethodException e10) {
            e = e10;
            E = i().E();
            str3 = "Could not find SystemProperties.get() method";
            E.b(str3, e);
            return str2;
        } catch (InvocationTargetException e11) {
            e = e11;
            E = i().E();
            str3 = "SystemProperties.get() threw an exception";
            E.b(str3, e);
            return str2;
        }
    }

    public final zzip A(String str) {
        Object obj;
        n6.j.f(str);
        Bundle V = V();
        if (V == null) {
            i().E().a("Failed to load metadata: Metadata bundle is null");
            obj = null;
        } else {
            obj = V.get(str);
        }
        if (obj == null) {
            return zzip.UNINITIALIZED;
        }
        if (Boolean.TRUE.equals(obj)) {
            return zzip.GRANTED;
        }
        if (Boolean.FALSE.equals(obj)) {
            return zzip.DENIED;
        }
        if ("default".equals(obj)) {
            return zzip.DEFAULT;
        }
        i().J().b("Invalid manifest metadata for", str);
        return zzip.UNINITIALIZED;
    }

    public final boolean B(String str, n4<Boolean> n4Var) {
        return D(str, n4Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Boolean C(String str) {
        n6.j.f(str);
        Bundle V = V();
        if (V == null) {
            i().E().a("Failed to load metadata: Metadata bundle is null");
            return null;
        } else if (V.containsKey(str)) {
            return Boolean.valueOf(V.getBoolean(str));
        } else {
            return null;
        }
    }

    public final boolean D(String str, n4<Boolean> n4Var) {
        Boolean a9;
        if (str != null) {
            String d8 = this.f16471d.d(str, n4Var.b());
            if (!TextUtils.isEmpty(d8)) {
                a9 = n4Var.a(Boolean.valueOf("1".equals(d8)));
                return a9.booleanValue();
            }
        }
        a9 = n4Var.a(null);
        return a9.booleanValue();
    }

    public final int E() {
        return g().a0(201500000, true) ? 100 : 25;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String F(String str) {
        return z(str, c0.N);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x002a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x002b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<java.lang.String> H(java.lang.String r4) {
        /*
            r3 = this;
            n6.j.f(r4)
            android.os.Bundle r0 = r3.V()
            r1 = 0
            if (r0 != 0) goto L19
            com.google.android.gms.measurement.internal.x4 r4 = r3.i()
            com.google.android.gms.measurement.internal.z4 r4 = r4.E()
            java.lang.String r0 = "Failed to load metadata: Metadata bundle is null"
            r4.a(r0)
        L17:
            r4 = r1
            goto L28
        L19:
            boolean r2 = r0.containsKey(r4)
            if (r2 != 0) goto L20
            goto L17
        L20:
            int r4 = r0.getInt(r4)
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
        L28:
            if (r4 != 0) goto L2b
            return r1
        L2b:
            android.content.Context r0 = r3.zza()     // Catch: android.content.res.Resources.NotFoundException -> L43
            android.content.res.Resources r0 = r0.getResources()     // Catch: android.content.res.Resources.NotFoundException -> L43
            int r4 = r4.intValue()     // Catch: android.content.res.Resources.NotFoundException -> L43
            java.lang.String[] r4 = r0.getStringArray(r4)     // Catch: android.content.res.Resources.NotFoundException -> L43
            if (r4 != 0) goto L3e
            return r1
        L3e:
            java.util.List r4 = java.util.Arrays.asList(r4)     // Catch: android.content.res.Resources.NotFoundException -> L43
            return r4
        L43:
            r4 = move-exception
            com.google.android.gms.measurement.internal.x4 r0 = r3.i()
            com.google.android.gms.measurement.internal.z4 r0 = r0.E()
            java.lang.String r2 = "Failed to load string array from metadata: resource not found"
            r0.b(r2, r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.e.H(java.lang.String):java.util.List");
    }

    public final void I(String str) {
        this.f16470c = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean J(String str) {
        return D(str, c0.M);
    }

    public final boolean K(String str) {
        return "1".equals(this.f16471d.d(str, "gaia_collection_enabled"));
    }

    public final boolean L(String str) {
        return "1".equals(this.f16471d.d(str, "measurement.event_sampling_enabled"));
    }

    public final String N() {
        return d("debug.firebase.analytics.app", BuildConfig.FLAVOR);
    }

    public final String O() {
        return d("debug.deferred.deeplink", BuildConfig.FLAVOR);
    }

    public final String P() {
        return this.f16470c;
    }

    public final boolean Q() {
        Boolean C = C("google_analytics_adid_collection_enabled");
        return C == null || C.booleanValue();
    }

    public final boolean R() {
        Boolean C = C("google_analytics_automatic_screen_reporting_enabled");
        return C == null || C.booleanValue();
    }

    public final boolean S() {
        Boolean C = C("firebase_analytics_collection_deactivated");
        return C != null && C.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean T() {
        if (this.f16469b == null) {
            Boolean C = C("app_measurement_lite");
            this.f16469b = C;
            if (C == null) {
                this.f16469b = Boolean.FALSE;
            }
        }
        return this.f16469b.booleanValue() || !this.f16485a.r();
    }

    public final boolean U() {
        if (this.f16472e == null) {
            synchronized (this) {
                if (this.f16472e == null) {
                    ApplicationInfo applicationInfo = zza().getApplicationInfo();
                    String a9 = u6.n.a();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.f16472e = Boolean.valueOf(str != null && str.equals(a9));
                    }
                    if (this.f16472e == null) {
                        this.f16472e = Boolean.TRUE;
                        i().E().a("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.f16472e.booleanValue();
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

    public final double m(String str, n4<Double> n4Var) {
        if (str != null) {
            String d8 = this.f16471d.d(str, n4Var.b());
            if (!TextUtils.isEmpty(d8)) {
                try {
                    return n4Var.a(Double.valueOf(Double.parseDouble(d8))).doubleValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return n4Var.a(null).doubleValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int n(String str) {
        return o(str, c0.J, 500, 2000);
    }

    public final int o(String str, n4<Integer> n4Var, int i8, int i9) {
        return Math.max(Math.min(t(str, n4Var), i9), i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int p(String str, boolean z4) {
        if (le.a() && a().D(null, c0.f16362a1)) {
            if (z4) {
                return o(str, c0.T, 100, 500);
            }
            return 500;
        }
        return 100;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void q(g gVar) {
        this.f16471d = gVar;
    }

    public final boolean r(n4<Boolean> n4Var) {
        return D(null, n4Var);
    }

    public final int s(String str) {
        return o(str, c0.K, 25, 100);
    }

    public final int t(String str, n4<Integer> n4Var) {
        if (str != null) {
            String d8 = this.f16471d.d(str, n4Var.b());
            if (!TextUtils.isEmpty(d8)) {
                try {
                    return n4Var.a(Integer.valueOf(Integer.parseInt(d8))).intValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return n4Var.a(null).intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int u(String str, boolean z4) {
        return Math.max(p(str, z4), (int) RecognitionOptions.QR_CODE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int v() {
        return (me.a() && a().D(null, c0.G0) && g().a0(231100000, true)) ? 35 : 0;
    }

    public final int w(String str) {
        return t(str, c0.q);
    }

    public final long x(String str, n4<Long> n4Var) {
        if (str != null) {
            String d8 = this.f16471d.d(str, n4Var.b());
            if (!TextUtils.isEmpty(d8)) {
                try {
                    return n4Var.a(Long.valueOf(Long.parseLong(d8))).longValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return n4Var.a(null).longValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long y(String str) {
        return x(str, c0.f16366c);
    }

    public final String z(String str, n4<String> n4Var) {
        return n4Var.a(str == null ? null : this.f16471d.d(str, n4Var.b()));
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
