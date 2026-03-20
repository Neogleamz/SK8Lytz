package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.fe;
import com.google.android.gms.internal.measurement.md;
import com.google.android.gms.internal.measurement.zzdq;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f6 implements f7 {
    private static volatile f6 I;
    private volatile Boolean A;
    private Boolean B;
    private Boolean C;
    private volatile boolean D;
    private int E;
    private int F;
    final long H;

    /* renamed from: a  reason: collision with root package name */
    private final Context f16499a;

    /* renamed from: b  reason: collision with root package name */
    private final String f16500b;

    /* renamed from: c  reason: collision with root package name */
    private final String f16501c;

    /* renamed from: d  reason: collision with root package name */
    private final String f16502d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f16503e;

    /* renamed from: f  reason: collision with root package name */
    private final d f16504f;

    /* renamed from: g  reason: collision with root package name */
    private final e f16505g;

    /* renamed from: h  reason: collision with root package name */
    private final h5 f16506h;

    /* renamed from: i  reason: collision with root package name */
    private final x4 f16507i;

    /* renamed from: j  reason: collision with root package name */
    private final a6 f16508j;

    /* renamed from: k  reason: collision with root package name */
    private final na f16509k;

    /* renamed from: l  reason: collision with root package name */
    private final sb f16510l;

    /* renamed from: m  reason: collision with root package name */
    private final s4 f16511m;

    /* renamed from: n  reason: collision with root package name */
    private final u6.d f16512n;

    /* renamed from: o  reason: collision with root package name */
    private final z8 f16513o;

    /* renamed from: p  reason: collision with root package name */
    private final h7 f16514p;
    private final a q;

    /* renamed from: r  reason: collision with root package name */
    private final s8 f16515r;

    /* renamed from: s  reason: collision with root package name */
    private final String f16516s;

    /* renamed from: t  reason: collision with root package name */
    private q4 f16517t;

    /* renamed from: u  reason: collision with root package name */
    private f9 f16518u;

    /* renamed from: v  reason: collision with root package name */
    private x f16519v;

    /* renamed from: w  reason: collision with root package name */
    private r4 f16520w;

    /* renamed from: y  reason: collision with root package name */
    private Boolean f16522y;

    /* renamed from: z  reason: collision with root package name */
    private long f16523z;

    /* renamed from: x  reason: collision with root package name */
    private boolean f16521x = false;
    private AtomicInteger G = new AtomicInteger(0);

    private f6(g7 g7Var) {
        z4 J;
        String str;
        Bundle bundle;
        boolean z4 = false;
        n6.j.l(g7Var);
        d dVar = new d(g7Var.f16549a);
        this.f16504f = dVar;
        o4.f16848a = dVar;
        Context context = g7Var.f16549a;
        this.f16499a = context;
        this.f16500b = g7Var.f16550b;
        this.f16501c = g7Var.f16551c;
        this.f16502d = g7Var.f16552d;
        this.f16503e = g7Var.f16556h;
        this.A = g7Var.f16553e;
        this.f16516s = g7Var.f16558j;
        this.D = true;
        zzdq zzdqVar = g7Var.f16555g;
        if (zzdqVar != null && (bundle = zzdqVar.f12791g) != null) {
            Object obj = bundle.get("measurementEnabled");
            if (obj instanceof Boolean) {
                this.B = (Boolean) obj;
            }
            Object obj2 = zzdqVar.f12791g.get("measurementDeactivated");
            if (obj2 instanceof Boolean) {
                this.C = (Boolean) obj2;
            }
        }
        com.google.android.gms.internal.measurement.n6.l(context);
        u6.d d8 = u6.g.d();
        this.f16512n = d8;
        Long l8 = g7Var.f16557i;
        this.H = l8 != null ? l8.longValue() : d8.a();
        this.f16505g = new e(this);
        h5 h5Var = new h5(this);
        h5Var.o();
        this.f16506h = h5Var;
        x4 x4Var = new x4(this);
        x4Var.o();
        this.f16507i = x4Var;
        sb sbVar = new sb(this);
        sbVar.o();
        this.f16510l = sbVar;
        this.f16511m = new s4(new i7(g7Var, this));
        this.q = new a(this);
        z8 z8Var = new z8(this);
        z8Var.u();
        this.f16513o = z8Var;
        h7 h7Var = new h7(this);
        h7Var.u();
        this.f16514p = h7Var;
        na naVar = new na(this);
        naVar.u();
        this.f16509k = naVar;
        s8 s8Var = new s8(this);
        s8Var.o();
        this.f16515r = s8Var;
        a6 a6Var = new a6(this);
        a6Var.o();
        this.f16508j = a6Var;
        zzdq zzdqVar2 = g7Var.f16555g;
        if (zzdqVar2 != null && zzdqVar2.f12786b != 0) {
            z4 = true;
        }
        boolean z8 = !z4;
        if (context.getApplicationContext() instanceof Application) {
            h7 F = F();
            if (F.zza().getApplicationContext() instanceof Application) {
                Application application = (Application) F.zza().getApplicationContext();
                if (F.f16625c == null) {
                    F.f16625c = new r8(F);
                }
                if (z8) {
                    application.unregisterActivityLifecycleCallbacks(F.f16625c);
                    application.registerActivityLifecycleCallbacks(F.f16625c);
                    J = F.i().I();
                    str = "Registered activity lifecycle callback";
                }
            }
            a6Var.B(new g6(this, g7Var));
        }
        J = i().J();
        str = "Application context is not an Application";
        J.a(str);
        a6Var.B(new g6(this, g7Var));
    }

    public static f6 a(Context context, zzdq zzdqVar, Long l8) {
        Bundle bundle;
        if (zzdqVar != null && (zzdqVar.f12789e == null || zzdqVar.f12790f == null)) {
            zzdqVar = new zzdq(zzdqVar.f12785a, zzdqVar.f12786b, zzdqVar.f12787c, zzdqVar.f12788d, null, null, zzdqVar.f12791g, null);
        }
        n6.j.l(context);
        n6.j.l(context.getApplicationContext());
        if (I == null) {
            synchronized (f6.class) {
                if (I == null) {
                    I = new f6(new g7(context, zzdqVar, l8));
                }
            }
        } else if (zzdqVar != null && (bundle = zzdqVar.f12791g) != null && bundle.containsKey("dataCollectionDefaultEnabled")) {
            n6.j.l(I);
            I.j(zzdqVar.f12791g.getBoolean("dataCollectionDefaultEnabled"));
        }
        n6.j.l(I);
        return I;
    }

    private static void d(v4 v4Var) {
        if (v4Var == null) {
            throw new IllegalStateException("Component not created");
        }
        if (v4Var.x()) {
            return;
        }
        String valueOf = String.valueOf(v4Var.getClass());
        throw new IllegalStateException("Component not initialized: " + valueOf);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void e(f6 f6Var, g7 g7Var) {
        f6Var.l().k();
        x xVar = new x(f6Var);
        xVar.o();
        f6Var.f16519v = xVar;
        r4 r4Var = new r4(f6Var, g7Var.f16554f);
        r4Var.u();
        f6Var.f16520w = r4Var;
        q4 q4Var = new q4(f6Var);
        q4Var.u();
        f6Var.f16517t = q4Var;
        f9 f9Var = new f9(f6Var);
        f9Var.u();
        f6Var.f16518u = f9Var;
        f6Var.f16510l.p();
        f6Var.f16506h.p();
        f6Var.f16520w.v();
        f6Var.i().H().b("App measurement initialized, version", 87000L);
        f6Var.i().H().a("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String D = r4Var.D();
        if (TextUtils.isEmpty(f6Var.f16500b)) {
            if (f6Var.J().C0(D, f6Var.f16505g.P())) {
                f6Var.i().H().a("Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.");
            } else {
                z4 H = f6Var.i().H();
                H.a("To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app " + D);
            }
        }
        f6Var.i().D().a("Debug-level message logging enabled");
        if (f6Var.E != f6Var.G.get()) {
            f6Var.i().E().c("Not all components initialized", Integer.valueOf(f6Var.E), Integer.valueOf(f6Var.G.get()));
        }
        f6Var.f16521x = true;
    }

    private static void f(d7 d7Var) {
        if (d7Var == null) {
            throw new IllegalStateException("Component not created");
        }
        if (d7Var.q()) {
            return;
        }
        String valueOf = String.valueOf(d7Var.getClass());
        throw new IllegalStateException("Component not initialized: " + valueOf);
    }

    private static void g(e7 e7Var) {
        if (e7Var == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private final s8 t() {
        f(this.f16515r);
        return this.f16515r;
    }

    public final q4 A() {
        d(this.f16517t);
        return this.f16517t;
    }

    public final s4 B() {
        return this.f16511m;
    }

    public final x4 C() {
        x4 x4Var = this.f16507i;
        if (x4Var == null || !x4Var.q()) {
            return null;
        }
        return this.f16507i;
    }

    public final h5 D() {
        g(this.f16506h);
        return this.f16506h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final a6 E() {
        return this.f16508j;
    }

    public final h7 F() {
        d(this.f16514p);
        return this.f16514p;
    }

    public final z8 G() {
        d(this.f16513o);
        return this.f16513o;
    }

    public final f9 H() {
        d(this.f16518u);
        return this.f16518u;
    }

    public final na I() {
        d(this.f16509k);
        return this.f16509k;
    }

    public final sb J() {
        g(this.f16510l);
        return this.f16510l;
    }

    public final String K() {
        return this.f16500b;
    }

    public final String L() {
        return this.f16501c;
    }

    public final String M() {
        return this.f16502d;
    }

    public final String N() {
        return this.f16516s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void O() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void P() {
        this.G.incrementAndGet();
    }

    @Override // com.google.android.gms.measurement.internal.f7
    public final d b() {
        return this.f16504f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00e7, code lost:
        if (r1.C() != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x015c, code lost:
        if (r1.C() != false) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0228  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void c(com.google.android.gms.internal.measurement.zzdq r13) {
        /*
            Method dump skipped, instructions count: 1286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.f6.c(com.google.android.gms.internal.measurement.zzdq):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void h(String str, int i8, Throwable th, byte[] bArr, Map map) {
        if (!((i8 == 200 || i8 == 204 || i8 == 304) && th == null)) {
            i().J().c("Network Request for Deferred Deep Link failed. response, exception", Integer.valueOf(i8), th);
            return;
        }
        D().f16619v.a(true);
        if (bArr == null || bArr.length == 0) {
            i().D().a("Deferred Deep Link response empty.");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr));
            String optString = jSONObject.optString("deeplink", BuildConfig.FLAVOR);
            String optString2 = jSONObject.optString("gclid", BuildConfig.FLAVOR);
            String optString3 = jSONObject.optString("gbraid", BuildConfig.FLAVOR);
            double optDouble = jSONObject.optDouble("timestamp", 0.0d);
            if (TextUtils.isEmpty(optString)) {
                i().D().a("Deferred Deep Link is empty.");
                return;
            }
            Bundle bundle = new Bundle();
            if (fe.a() && this.f16505g.r(c0.Y0)) {
                if (!J().K0(optString)) {
                    i().J().d("Deferred Deep Link validation failed. gclid, gbraid, deep link", optString2, optString3, optString);
                    return;
                }
                bundle.putString("gbraid", optString3);
            } else if (!J().K0(optString)) {
                i().J().c("Deferred Deep Link validation failed. gclid, deep link", optString2, optString);
                return;
            }
            bundle.putString("gclid", optString2);
            bundle.putString("_cis", "ddp");
            this.f16514p.A0("auto", "_cmp", bundle);
            sb J = J();
            if (TextUtils.isEmpty(optString) || !J.g0(optString, optDouble)) {
                return;
            }
            J.zza().sendBroadcast(new Intent("android.google.analytics.action.DEEPLINK_ACTION"));
        } catch (JSONException e8) {
            i().E().b("Failed to parse the Deferred Deep Link response. exception", e8);
        }
    }

    @Override // com.google.android.gms.measurement.internal.f7
    public final x4 i() {
        f(this.f16507i);
        return this.f16507i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void j(boolean z4) {
        this.A = Boolean.valueOf(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void k() {
        this.E++;
    }

    @Override // com.google.android.gms.measurement.internal.f7
    public final a6 l() {
        f(this.f16508j);
        return this.f16508j;
    }

    public final boolean m() {
        return this.A != null && this.A.booleanValue();
    }

    public final boolean n() {
        return v() == 0;
    }

    public final boolean o() {
        l().k();
        return this.D;
    }

    public final boolean p() {
        return TextUtils.isEmpty(this.f16500b);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean q() {
        if (this.f16521x) {
            l().k();
            Boolean bool = this.f16522y;
            if (bool == null || this.f16523z == 0 || (bool != null && !bool.booleanValue() && Math.abs(this.f16512n.b() - this.f16523z) > 1000)) {
                this.f16523z = this.f16512n.b();
                boolean z4 = true;
                Boolean valueOf = Boolean.valueOf(J().D0("android.permission.INTERNET") && J().D0("android.permission.ACCESS_NETWORK_STATE") && (w6.c.a(this.f16499a).f() || this.f16505g.T() || (sb.b0(this.f16499a) && sb.c0(this.f16499a, false))));
                this.f16522y = valueOf;
                if (valueOf.booleanValue()) {
                    if (!J().i0(z().E(), z().C()) && TextUtils.isEmpty(z().C())) {
                        z4 = false;
                    }
                    this.f16522y = Boolean.valueOf(z4);
                }
            }
            return this.f16522y.booleanValue();
        }
        throw new IllegalStateException("AppMeasurement is not initialized");
    }

    public final boolean r() {
        return this.f16503e;
    }

    public final boolean s() {
        l().k();
        f(t());
        String D = z().D();
        Pair<String, Boolean> s8 = D().s(D);
        if (!this.f16505g.Q() || ((Boolean) s8.second).booleanValue() || TextUtils.isEmpty((CharSequence) s8.first)) {
            i().D().a("ADID unavailable to retrieve Deferred Deep Link. Skipping");
            return false;
        } else if (!t().u()) {
            i().J().a("Network is not available for Deferred Deep Link request. Skipping");
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            if (md.a() && this.f16505g.r(c0.T0)) {
                f9 H = H();
                H.k();
                H.t();
                if (!H.d0() || H.g().G0() >= 234200) {
                    h7 F = F();
                    F.k();
                    zzal T = F.r().T();
                    Bundle bundle = T != null ? T.f17261a : null;
                    if (bundle == null) {
                        int i8 = this.F;
                        this.F = i8 + 1;
                        boolean z4 = i8 < 10;
                        z4 D2 = i().D();
                        String str = z4 ? "Retrying." : "Skipping.";
                        D2.b("Failed to retrieve DMA consent from the service, " + str + " retryCount", Integer.valueOf(this.F));
                        return z4;
                    }
                    zziq f5 = zziq.f(bundle, 100);
                    sb.append("&gcs=");
                    sb.append(f5.y());
                    v b9 = v.b(bundle, 100);
                    sb.append("&dma=");
                    sb.append(b9.h() == Boolean.FALSE ? 0 : 1);
                    if (!TextUtils.isEmpty(b9.i())) {
                        sb.append("&dma_cps=");
                        sb.append(b9.i());
                    }
                    int i9 = v.e(bundle) == Boolean.TRUE ? 0 : 1;
                    sb.append("&npa=");
                    sb.append(i9);
                    i().I().b("Consent query parameters to Bow", sb);
                }
            }
            sb J = J();
            z();
            URL I2 = J.I(87000L, D, (String) s8.first, D().f16620w.a() - 1, sb.toString());
            if (I2 != null) {
                s8 t8 = t();
                u8 u8Var = new u8() { // from class: com.google.android.gms.measurement.internal.h6
                    @Override // com.google.android.gms.measurement.internal.u8
                    public final void a(String str2, int i10, Throwable th, byte[] bArr, Map map) {
                        f6.this.h(str2, i10, th, bArr, map);
                    }
                };
                t8.k();
                t8.n();
                n6.j.l(I2);
                n6.j.l(u8Var);
                t8.l().x(new t8(t8, D, I2, null, null, u8Var));
            }
            return false;
        }
    }

    public final void u(boolean z4) {
        l().k();
        this.D = z4;
    }

    public final int v() {
        l().k();
        if (this.f16505g.S()) {
            return 1;
        }
        Boolean bool = this.C;
        if (bool == null || !bool.booleanValue()) {
            if (o()) {
                Boolean M = D().M();
                if (M != null) {
                    return M.booleanValue() ? 0 : 3;
                }
                Boolean C = this.f16505g.C("firebase_analytics_collection_enabled");
                if (C != null) {
                    return C.booleanValue() ? 0 : 4;
                }
                Boolean bool2 = this.B;
                return bool2 != null ? bool2.booleanValue() ? 0 : 5 : (this.A == null || this.A.booleanValue()) ? 0 : 7;
            }
            return 8;
        }
        return 2;
    }

    public final a w() {
        a aVar = this.q;
        if (aVar != null) {
            return aVar;
        }
        throw new IllegalStateException("Component not created");
    }

    public final e x() {
        return this.f16505g;
    }

    public final x y() {
        f(this.f16519v);
        return this.f16519v;
    }

    public final r4 z() {
        d(this.f16520w);
        return this.f16520w;
    }

    @Override // com.google.android.gms.measurement.internal.f7
    public final Context zza() {
        return this.f16499a;
    }

    @Override // com.google.android.gms.measurement.internal.f7
    public final u6.d zzb() {
        return this.f16512n;
    }
}
