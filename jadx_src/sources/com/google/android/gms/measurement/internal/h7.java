package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.gms.internal.measurement.ae;
import com.google.android.gms.internal.measurement.fe;
import com.google.android.gms.internal.measurement.lf;
import com.google.android.gms.internal.measurement.md;
import com.google.android.gms.internal.measurement.uc;
import com.google.android.gms.internal.measurement.ye;
import com.google.android.gms.measurement.internal.h7;
import com.google.android.gms.measurement.internal.zziq;
import com.google.android.gms.measurement.internal.zzmv;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h7 extends v4 {

    /* renamed from: c  reason: collision with root package name */
    protected r8 f16625c;

    /* renamed from: d  reason: collision with root package name */
    private f7.p f16626d;

    /* renamed from: e  reason: collision with root package name */
    private final Set<f7.r> f16627e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f16628f;

    /* renamed from: g  reason: collision with root package name */
    private final AtomicReference<String> f16629g;

    /* renamed from: h  reason: collision with root package name */
    private final Object f16630h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f16631i;

    /* renamed from: j  reason: collision with root package name */
    private PriorityQueue<zzmv> f16632j;

    /* renamed from: k  reason: collision with root package name */
    private zziq f16633k;

    /* renamed from: l  reason: collision with root package name */
    private final AtomicLong f16634l;

    /* renamed from: m  reason: collision with root package name */
    private long f16635m;

    /* renamed from: n  reason: collision with root package name */
    final xb f16636n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f16637o;

    /* renamed from: p  reason: collision with root package name */
    private t f16638p;
    private SharedPreferences.OnSharedPreferenceChangeListener q;

    /* renamed from: r  reason: collision with root package name */
    private t f16639r;

    /* renamed from: s  reason: collision with root package name */
    private final rb f16640s;

    /* JADX INFO: Access modifiers changed from: protected */
    public h7(f6 f6Var) {
        super(f6Var);
        this.f16627e = new CopyOnWriteArraySet();
        this.f16630h = new Object();
        this.f16631i = false;
        this.f16637o = true;
        this.f16640s = new j8(this);
        this.f16629g = new AtomicReference<>();
        this.f16633k = zziq.f17272c;
        this.f16635m = -1L;
        this.f16634l = new AtomicLong(0L);
        this.f16636n = new xb(f6Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void K(h7 h7Var, zziq zziqVar, long j8, boolean z4, boolean z8) {
        h7Var.k();
        h7Var.t();
        zziq J = h7Var.f().J();
        if (j8 <= h7Var.f16635m && zziq.l(J.b(), zziqVar.b())) {
            h7Var.i().H().b("Dropped out-of-date consent setting, proposed settings", zziqVar);
        } else if (!h7Var.f().y(zziqVar)) {
            h7Var.i().H().b("Lower precedence consent source ignored, proposed source", Integer.valueOf(zziqVar.b()));
        } else {
            h7Var.f16635m = j8;
            h7Var.r().S(z4);
            if (z8) {
                h7Var.r().O(new AtomicReference<>());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void L(h7 h7Var, zziq zziqVar, zziq zziqVar2) {
        zziq.zza zzaVar = zziq.zza.ANALYTICS_STORAGE;
        zziq.zza zzaVar2 = zziq.zza.AD_STORAGE;
        boolean n8 = zziqVar.n(zziqVar2, zzaVar, zzaVar2);
        boolean s8 = zziqVar.s(zziqVar2, zzaVar, zzaVar2);
        if (n8 || s8) {
            h7Var.n().G();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R(Boolean bool, boolean z4) {
        k();
        t();
        i().D().b("Setting app measurement enabled (FE)", bool);
        f().t(bool);
        if (z4) {
            f().B(bool);
        }
        if (this.f16485a.o() || !(bool == null || bool.booleanValue())) {
            u0();
        }
    }

    private final void V(String str, String str2, long j8, Object obj) {
        l().B(new y7(this, str, str2, obj, j8));
    }

    @TargetApi(30)
    private final PriorityQueue<zzmv> t0() {
        Comparator comparing;
        if (this.f16632j == null) {
            comparing = Comparator.comparing(new Function() { // from class: f7.t
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Long.valueOf(((zzmv) obj).f17286b);
                }
            }, new Comparator() { // from class: f7.s
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return Long.compare(((Long) obj).longValue(), ((Long) obj2).longValue());
                }
            });
            this.f16632j = new PriorityQueue<>(comparing);
        }
        return this.f16632j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void u0() {
        Long valueOf;
        k();
        String a9 = f().f16613o.a();
        if (a9 != null) {
            if ("unset".equals(a9)) {
                valueOf = null;
            } else {
                valueOf = Long.valueOf("true".equals(a9) ? 1L : 0L);
            }
            Z("app", "_npa", valueOf, zzb().a());
        }
        if (!this.f16485a.n() || !this.f16637o) {
            i().D().a("Updating Scion state (FE)");
            r().Z();
            return;
        }
        i().D().a("Recording app launch after enabling measurement for the first time (FE)");
        m0();
        if (ae.a() && a().r(c0.f16400o0)) {
            s().f16841e.a();
        }
        l().B(new v7(this));
    }

    private final void y0(String str, String str2, long j8, Bundle bundle, boolean z4, boolean z8, boolean z9, String str3) {
        l().B(new w7(this, str, str2, j8, sb.B(bundle), z4, z8, z9, str3));
    }

    public final ArrayList<Bundle> A(String str, String str2) {
        if (l().H()) {
            i().E().a("Cannot get conditional user properties from analytics worker thread");
            return new ArrayList<>(0);
        } else if (d.a()) {
            i().E().a("Cannot get conditional user properties from main thread");
            return new ArrayList<>(0);
        } else {
            AtomicReference atomicReference = new AtomicReference();
            this.f16485a.l().t(atomicReference, 5000L, "get conditional user properties", new d8(this, atomicReference, null, str, str2));
            List list = (List) atomicReference.get();
            if (list == null) {
                i().E().b("Timed out waiting for get conditional user properties", null);
                return new ArrayList<>();
            }
            return sb.r0(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void A0(String str, String str2, Bundle bundle) {
        k();
        T(str, str2, zzb().a(), bundle);
    }

    public final Map<String, Object> B(String str, String str2, boolean z4) {
        z4 E;
        String str3;
        if (l().H()) {
            E = i().E();
            str3 = "Cannot get user properties from analytics worker thread";
        } else if (!d.a()) {
            AtomicReference atomicReference = new AtomicReference();
            this.f16485a.l().t(atomicReference, 5000L, "get user properties", new h8(this, atomicReference, null, str, str2, z4));
            List<zzno> list = (List) atomicReference.get();
            if (list == null) {
                i().E().b("Timed out waiting for handle get user properties, includeInternal", Boolean.valueOf(z4));
                return Collections.emptyMap();
            }
            k0.a aVar = new k0.a(list.size());
            for (zzno zznoVar : list) {
                Object t8 = zznoVar.t();
                if (t8 != null) {
                    aVar.put(zznoVar.f17308b, t8);
                }
            }
            return aVar;
        } else {
            E = i().E();
            str3 = "Cannot get user properties from main thread";
        }
        E.a(str3);
        return Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void C(long j8, boolean z4) {
        k();
        t();
        i().D().a("Resetting analytics data (FE)");
        na s8 = s();
        s8.k();
        s8.f16842f.b();
        if (lf.a() && a().r(c0.f16409t0)) {
            n().G();
        }
        boolean n8 = this.f16485a.n();
        h5 f5 = f();
        f5.f16605g.b(j8);
        if (!TextUtils.isEmpty(f5.f().f16621x.a())) {
            f5.f16621x.b(null);
        }
        if (ae.a() && f5.a().r(c0.f16400o0)) {
            f5.f16615r.b(0L);
        }
        f5.f16616s.b(0L);
        if (!f5.a().S()) {
            f5.D(!n8);
        }
        f5.f16622y.b(null);
        f5.f16623z.b(0L);
        f5.A.b(null);
        if (z4) {
            r().Y();
        }
        if (ae.a() && a().r(c0.f16400o0)) {
            s().f16841e.a();
        }
        this.f16637o = !n8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void D(SharedPreferences sharedPreferences, String str) {
        if ("IABTCF_TCString".equals(str)) {
            i().I().a("IABTCF_TCString change picked up in listener.");
            ((t) n6.j.l(this.f16639r)).b(500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void E(Bundle bundle) {
        if (bundle == null) {
            f().A.b(new Bundle());
            return;
        }
        Bundle a9 = f().A.a();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj != null && !(obj instanceof String) && !(obj instanceof Long) && !(obj instanceof Double)) {
                g();
                if (sb.f0(obj)) {
                    g();
                    sb.W(this.f16640s, 27, null, null, 0);
                }
                i().K().c("Invalid default event parameter type. Name, value", str, obj);
            } else if (sb.H0(str)) {
                i().K().b("Invalid default event parameter name. Name", str);
            } else if (obj == null) {
                a9.remove(str);
            } else if (g().j0("param", str, a().p(null, false), obj)) {
                g().M(a9, str, obj);
            }
        }
        g();
        if (sb.e0(a9, a().E())) {
            g();
            sb.W(this.f16640s, 26, null, null, 0);
            i().K().a("Too many default event parameters set. Discarding beyond event parameter limit");
        }
        f().A.b(a9);
        r().A(a9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void F(Bundle bundle, int i8, long j8) {
        t();
        String k8 = zziq.k(bundle);
        if (k8 != null) {
            i().K().b("Ignoring invalid consent setting", k8);
            i().K().a("Valid consent values are 'granted', 'denied'");
        }
        zziq f5 = zziq.f(bundle, i8);
        if (!md.a() || !a().r(c0.R0)) {
            J(f5, j8, false);
            return;
        }
        if (f5.C()) {
            J(f5, j8, false);
        }
        v b9 = v.b(bundle, i8);
        if (b9.k()) {
            H(b9, false);
        }
        Boolean e8 = v.e(bundle);
        if (e8 != null) {
            a0(i8 == -30 ? "tcf" : "app", "allow_personalized_ads", e8.toString(), false);
        }
    }

    public final void G(Bundle bundle, long j8) {
        n6.j.l(bundle);
        Bundle bundle2 = new Bundle(bundle);
        if (!TextUtils.isEmpty(bundle2.getString("app_id"))) {
            i().J().a("Package name should be null when calling setConditionalUserProperty");
        }
        bundle2.remove("app_id");
        n6.j.l(bundle2);
        f7.m.a(bundle2, "app_id", String.class, null);
        f7.m.a(bundle2, "origin", String.class, null);
        f7.m.a(bundle2, "name", String.class, null);
        f7.m.a(bundle2, "value", Object.class, null);
        f7.m.a(bundle2, "trigger_event_name", String.class, null);
        f7.m.a(bundle2, "trigger_timeout", Long.class, 0L);
        f7.m.a(bundle2, "timed_out_event_name", String.class, null);
        f7.m.a(bundle2, "timed_out_event_params", Bundle.class, null);
        f7.m.a(bundle2, "triggered_event_name", String.class, null);
        f7.m.a(bundle2, "triggered_event_params", Bundle.class, null);
        f7.m.a(bundle2, "time_to_live", Long.class, 0L);
        f7.m.a(bundle2, "expired_event_name", String.class, null);
        f7.m.a(bundle2, "expired_event_params", Bundle.class, null);
        n6.j.f(bundle2.getString("name"));
        n6.j.f(bundle2.getString("origin"));
        n6.j.l(bundle2.get("value"));
        bundle2.putLong("creation_timestamp", j8);
        String string = bundle2.getString("name");
        Object obj = bundle2.get("value");
        if (g().p0(string) != 0) {
            i().E().b("Invalid conditional user property name", e().g(string));
        } else if (g().u(string, obj) != 0) {
            i().E().c("Invalid conditional user property value", e().g(string), obj);
        } else {
            Object y02 = g().y0(string, obj);
            if (y02 == null) {
                i().E().c("Unable to normalize conditional user property value", e().g(string), obj);
                return;
            }
            f7.m.b(bundle2, y02);
            long j9 = bundle2.getLong("trigger_timeout");
            if (!TextUtils.isEmpty(bundle2.getString("trigger_event_name")) && (j9 > 15552000000L || j9 < 1)) {
                i().E().c("Invalid conditional user property timeout", e().g(string), Long.valueOf(j9));
                return;
            }
            long j10 = bundle2.getLong("time_to_live");
            if (j10 > 15552000000L || j10 < 1) {
                i().E().c("Invalid conditional user property time to live", e().g(string), Long.valueOf(j10));
            } else {
                l().B(new b8(this, bundle2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void H(v vVar, boolean z4) {
        m8 m8Var = new m8(this, vVar);
        if (!z4) {
            l().B(m8Var);
            return;
        }
        k();
        m8Var.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void I(zziq zziqVar) {
        k();
        boolean z4 = (zziqVar.B() && zziqVar.A()) || r().c0();
        if (z4 != this.f16485a.o()) {
            this.f16485a.u(z4);
            Boolean L = f().L();
            if (!z4 || L == null || L.booleanValue()) {
                R(Boolean.valueOf(z4), false);
            }
        }
    }

    public final void J(zziq zziqVar, long j8, boolean z4) {
        zziq zziqVar2;
        boolean z8;
        boolean z9;
        boolean z10;
        zziq zziqVar3 = zziqVar;
        t();
        int b9 = zziqVar.b();
        if (uc.a() && a().r(c0.f16377f1)) {
            if (b9 != -10) {
                zzip t8 = zziqVar.t();
                zzip zzipVar = zzip.UNINITIALIZED;
                if (t8 == zzipVar && zziqVar.v() == zzipVar) {
                    i().K().a("Ignoring empty consent settings");
                    return;
                }
            }
        } else if (b9 != -10 && zziqVar.w() == null && zziqVar.x() == null) {
            i().K().a("Discarding empty consent settings");
            return;
        }
        synchronized (this.f16630h) {
            zziqVar2 = this.f16633k;
            z8 = true;
            z9 = false;
            if (zziq.l(b9, zziqVar2.b())) {
                boolean u8 = zziqVar.u(this.f16633k);
                if (zziqVar.B() && !this.f16633k.B()) {
                    z9 = true;
                }
                zziqVar3 = zziqVar.p(this.f16633k);
                this.f16633k = zziqVar3;
                z10 = z9;
                z9 = u8;
            } else {
                z8 = false;
                z10 = false;
            }
        }
        if (!z8) {
            i().H().b("Ignoring lower-priority consent settings, proposed settings", zziqVar3);
            return;
        }
        long andIncrement = this.f16634l.getAndIncrement();
        if (z9) {
            S(null);
            p8 p8Var = new p8(this, zziqVar3, j8, andIncrement, z10, zziqVar2);
            if (!z4) {
                l().E(p8Var);
                return;
            }
            k();
            p8Var.run();
            return;
        }
        o8 o8Var = new o8(this, zziqVar3, andIncrement, z10, zziqVar2);
        if (z4) {
            k();
            o8Var.run();
        } else if (b9 == 30 || b9 == -10) {
            l().E(o8Var);
        } else {
            l().B(o8Var);
        }
    }

    public final void O(f7.p pVar) {
        f7.p pVar2;
        k();
        t();
        if (pVar != null && pVar != (pVar2 = this.f16626d)) {
            n6.j.q(pVar2 == null, "EventInterceptor already set.");
        }
        this.f16626d = pVar;
    }

    public final void P(f7.r rVar) {
        t();
        n6.j.l(rVar);
        if (this.f16627e.add(rVar)) {
            return;
        }
        i().J().a("OnEventListener already registered");
    }

    public final void Q(Boolean bool) {
        t();
        l().B(new n8(this, bool));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void S(String str) {
        this.f16629g.set(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void T(String str, String str2, long j8, Bundle bundle) {
        k();
        U(str, str2, j8, bundle, true, this.f16626d == null || sb.H0(str2), true, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void U(String str, String str2, long j8, Bundle bundle, boolean z4, boolean z8, boolean z9, String str3) {
        String str4;
        long j9;
        String str5;
        String str6;
        boolean z10;
        int length;
        n6.j.f(str);
        n6.j.l(bundle);
        k();
        t();
        if (!this.f16485a.n()) {
            i().D().a("Event not sent since app measurement is disabled");
            return;
        }
        List<String> F = n().F();
        if (F != null && !F.contains(str2)) {
            i().D().c("Dropping non-safelisted event. event name, origin", str2, str);
            return;
        }
        boolean z11 = true;
        if (!this.f16628f) {
            this.f16628f = true;
            try {
                try {
                    (!this.f16485a.r() ? Class.forName("com.google.android.gms.tagmanager.TagManagerService", true, zza().getClassLoader()) : Class.forName("com.google.android.gms.tagmanager.TagManagerService")).getDeclaredMethod("initialize", Context.class).invoke(null, zza());
                } catch (Exception e8) {
                    i().J().b("Failed to invoke Tag Manager's initialize() method", e8);
                }
            } catch (ClassNotFoundException unused) {
                i().H().a("Tag Manager is not found and thus will not be used");
            }
        }
        if ("_cmp".equals(str2)) {
            if (bundle.containsKey("gclid")) {
                Z("auto", "_lgclid", bundle.getString("gclid"), zzb().a());
            }
            if (fe.a() && a().r(c0.Y0) && bundle.containsKey("gbraid")) {
                Z("auto", "_gbraid", bundle.getString("gbraid"), zzb().a());
            }
        }
        if (z4 && sb.L0(str2)) {
            g().L(bundle, f().A.a());
        }
        if (!z9 && !"_iap".equals(str2)) {
            sb J = this.f16485a.J();
            int i8 = 2;
            if (J.A0("event", str2)) {
                if (!J.n0("event", f7.o.f19853a, f7.o.f19854b, str2)) {
                    i8 = 13;
                } else if (J.h0("event", 40, str2)) {
                    i8 = 0;
                }
            }
            if (i8 != 0) {
                i().F().b("Invalid public event name. Event will not be logged (FE)", e().c(str2));
                this.f16485a.J();
                String H = sb.H(str2, 40, true);
                length = str2 != null ? str2.length() : 0;
                this.f16485a.J();
                sb.W(this.f16640s, i8, "_ev", H, length);
                return;
            }
        }
        x8 A = q().A(false);
        if (A != null && !bundle.containsKey("_sc")) {
            A.f17131d = true;
        }
        sb.V(A, bundle, z4 && !z9);
        boolean equals = "am".equals(str);
        boolean H0 = sb.H0(str2);
        if (z4 && this.f16626d != null && !H0 && !equals) {
            i().D().c("Passing event to registered event handler (FE)", e().c(str2), e().a(bundle));
            n6.j.l(this.f16626d);
            this.f16626d.a(str, str2, bundle, j8);
        } else if (this.f16485a.q()) {
            int t8 = g().t(str2);
            if (t8 != 0) {
                i().F().b("Invalid event name. Event will not be logged (FE)", e().c(str2));
                g();
                String H2 = sb.H(str2, 40, true);
                length = str2 != null ? str2.length() : 0;
                this.f16485a.J();
                sb.X(this.f16640s, str3, t8, "_ev", H2, length);
                return;
            }
            Bundle D = g().D(str3, str2, bundle, u6.e.b("_o", "_sn", "_sc", "_si"), z9);
            n6.j.l(D);
            if (q().A(false) != null && "_ae".equals(str2)) {
                sa saVar = s().f16842f;
                long b9 = saVar.f16979d.zzb().b();
                long j10 = b9 - saVar.f16977b;
                saVar.f16977b = b9;
                if (j10 > 0) {
                    g().K(D, j10);
                }
            }
            if (!"auto".equals(str) && "_ssr".equals(str2)) {
                sb g8 = g();
                String string = D.getString("_ffr");
                if (u6.o.a(string)) {
                    string = null;
                } else if (string != null) {
                    string = string.trim();
                }
                if (Objects.equals(string, g8.f().f16621x.a())) {
                    g8.i().D().a("Not logging duplicate session_start_with_rollout event");
                    z10 = false;
                } else {
                    g8.f().f16621x.b(string);
                    z10 = true;
                }
                if (!z10) {
                    return;
                }
            } else if ("_ae".equals(str2)) {
                String a9 = g().f().f16621x.a();
                if (!TextUtils.isEmpty(a9)) {
                    D.putString("_ffr", a9);
                }
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(D);
            boolean D2 = a().r(c0.Q0) ? s().D() : f().f16618u.b();
            if (f().f16615r.a() > 0 && f().w(j8) && D2) {
                i().I().a("Current session is expired, remove the session number, ID, and engagement time");
                j9 = 0;
                str4 = "_ae";
                Z("auto", "_sid", null, zzb().a());
                Z("auto", "_sno", null, zzb().a());
                Z("auto", "_se", null, zzb().a());
                f().f16616s.b(0L);
            } else {
                str4 = "_ae";
                j9 = 0;
            }
            if (D.getLong("extend_session", j9) == 1) {
                i().I().a("EXTEND_SESSION param attached: initiate a new session or extend the current active session");
                this.f16485a.I().f16841e.b(j8, true);
            }
            ArrayList arrayList2 = new ArrayList(D.keySet());
            Collections.sort(arrayList2);
            int size = arrayList2.size();
            int i9 = 0;
            while (i9 < size) {
                Object obj = arrayList2.get(i9);
                i9++;
                String str7 = (String) obj;
                if (str7 != null) {
                    g();
                    Bundle[] v02 = sb.v0(D.get(str7));
                    if (v02 != null) {
                        D.putParcelableArray(str7, v02);
                    }
                }
            }
            int i10 = 0;
            while (i10 < arrayList.size()) {
                Bundle bundle2 = (Bundle) arrayList.get(i10);
                if (i10 != 0 ? z11 : false) {
                    str6 = "_ep";
                    str5 = str;
                } else {
                    str5 = str;
                    str6 = str2;
                }
                bundle2.putString("_o", str5);
                if (z8) {
                    bundle2 = g().C(bundle2, null);
                }
                Bundle bundle3 = bundle2;
                r().G(new zzbf(str6, new zzba(bundle3), str, j8), str3);
                if (!equals) {
                    for (f7.r rVar : this.f16627e) {
                        rVar.a(str, str2, new Bundle(bundle3), j8);
                    }
                }
                i10++;
                z11 = true;
            }
            if (q().A(false) == null || !str4.equals(str2)) {
                return;
            }
            s().C(true, true, zzb().b());
        }
    }

    public final void W(String str, String str2, Bundle bundle) {
        long a9 = zzb().a();
        n6.j.f(str);
        Bundle bundle2 = new Bundle();
        bundle2.putString("name", str);
        bundle2.putLong("creation_timestamp", a9);
        if (str2 != null) {
            bundle2.putString("expired_event_name", str2);
            bundle2.putBundle("expired_event_params", bundle);
        }
        l().B(new e8(this, bundle2));
    }

    public final void X(String str, String str2, Bundle bundle, String str3) {
        j();
        y0(str, str2, zzb().a(), bundle, false, true, true, str3);
    }

    public final void Y(String str, String str2, Bundle bundle, boolean z4, boolean z8, long j8) {
        String str3 = str == null ? "app" : str;
        Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        if (Objects.equals(str2, "screen_view")) {
            q().G(bundle2, j8);
        } else {
            y0(str3, str2, j8, bundle2, z8, !z8 || this.f16626d == null || sb.H0(str2), z4, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void Z(java.lang.String r9, java.lang.String r10, java.lang.Object r11, long r12) {
        /*
            r8 = this;
            n6.j.f(r9)
            n6.j.f(r10)
            r8.k()
            r8.t()
            java.lang.String r0 = "allow_personalized_ads"
            boolean r0 = r0.equals(r10)
            java.lang.String r1 = "_npa"
            if (r0 == 0) goto L60
            boolean r0 = r11 instanceof java.lang.String
            if (r0 == 0) goto L50
            r0 = r11
            java.lang.String r0 = (java.lang.String) r0
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L50
            java.util.Locale r10 = java.util.Locale.ENGLISH
            java.lang.String r10 = r0.toLowerCase(r10)
            java.lang.String r11 = "false"
            boolean r10 = r11.equals(r10)
            r2 = 1
            if (r10 == 0) goto L35
            r4 = r2
            goto L37
        L35:
            r4 = 0
        L37:
            java.lang.Long r10 = java.lang.Long.valueOf(r4)
            com.google.android.gms.measurement.internal.h5 r0 = r8.f()
            com.google.android.gms.measurement.internal.n5 r0 = r0.f16613o
            long r4 = r10.longValue()
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 != 0) goto L4b
            java.lang.String r11 = "true"
        L4b:
            r0.b(r11)
            r6 = r10
            goto L5e
        L50:
            if (r11 != 0) goto L60
            com.google.android.gms.measurement.internal.h5 r10 = r8.f()
            com.google.android.gms.measurement.internal.n5 r10 = r10.f16613o
            java.lang.String r0 = "unset"
            r10.b(r0)
            r6 = r11
        L5e:
            r3 = r1
            goto L62
        L60:
            r3 = r10
            r6 = r11
        L62:
            com.google.android.gms.measurement.internal.f6 r10 = r8.f16485a
            boolean r10 = r10.n()
            if (r10 != 0) goto L78
            com.google.android.gms.measurement.internal.x4 r9 = r8.i()
            com.google.android.gms.measurement.internal.z4 r9 = r9.I()
            java.lang.String r10 = "User property not set since app measurement is disabled"
            r9.a(r10)
            return
        L78:
            com.google.android.gms.measurement.internal.f6 r10 = r8.f16485a
            boolean r10 = r10.q()
            if (r10 != 0) goto L81
            return
        L81:
            com.google.android.gms.measurement.internal.zzno r10 = new com.google.android.gms.measurement.internal.zzno
            r2 = r10
            r4 = r12
            r7 = r9
            r2.<init>(r3, r4, r6, r7)
            com.google.android.gms.measurement.internal.f9 r9 = r8.r()
            r9.K(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.h7.Z(java.lang.String, java.lang.String, java.lang.Object, long):void");
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    public final void a0(String str, String str2, Object obj, boolean z4) {
        b0(str, str2, obj, z4, zzb().a());
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    public final void b0(String str, String str2, Object obj, boolean z4, long j8) {
        if (str == null) {
            str = "app";
        }
        String str3 = str;
        int i8 = 6;
        if (z4) {
            i8 = g().p0(str2);
        } else {
            sb g8 = g();
            if (g8.A0("user property", str2)) {
                if (!g8.m0("user property", f7.q.f19857a, str2)) {
                    i8 = 15;
                } else if (g8.h0("user property", 24, str2)) {
                    i8 = 0;
                }
            }
        }
        if (i8 != 0) {
            g();
            String H = sb.H(str2, 24, true);
            r0 = str2 != null ? str2.length() : 0;
            this.f16485a.J();
            sb.W(this.f16640s, i8, "_ev", H, r0);
        } else if (obj == null) {
            V(str3, str2, j8, null);
        } else {
            int u8 = g().u(str2, obj);
            if (u8 == 0) {
                Object y02 = g().y0(str2, obj);
                if (y02 != null) {
                    V(str3, str2, j8, y02);
                    return;
                }
                return;
            }
            g();
            String H2 = sb.H(str2, 24, true);
            if ((obj instanceof String) || (obj instanceof CharSequence)) {
                r0 = String.valueOf(obj).length();
            }
            this.f16485a.J();
            sb.W(this.f16640s, u8, "_ev", H2, r0);
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void c0(List list) {
        k();
        if (Build.VERSION.SDK_INT >= 30) {
            SparseArray<Long> H = f().H();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                zzmv zzmvVar = (zzmv) it.next();
                if (!H.contains(zzmvVar.f17287c) || H.get(zzmvVar.f17287c).longValue() < zzmvVar.f17286b) {
                    t0().add(zzmvVar);
                }
            }
            r0();
        }
    }

    public final Boolean d0() {
        AtomicReference atomicReference = new AtomicReference();
        return (Boolean) l().t(atomicReference, 15000L, "boolean test flag value", new r7(this, atomicReference));
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    public final Double e0() {
        AtomicReference atomicReference = new AtomicReference();
        return (Double) l().t(atomicReference, 15000L, "double test flag value", new k8(this, atomicReference));
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    public final Integer f0() {
        AtomicReference atomicReference = new AtomicReference();
        return (Integer) l().t(atomicReference, 15000L, "int test flag value", new l8(this, atomicReference));
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    public final Long g0() {
        AtomicReference atomicReference = new AtomicReference();
        return (Long) l().t(atomicReference, 15000L, "long test flag value", new i8(this, atomicReference));
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    public final String h0() {
        return this.f16629g.get();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    public final String i0() {
        x8 N = this.f16485a.G().N();
        if (N != null) {
            return N.f17129b;
        }
        return null;
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    public final String j0() {
        x8 N = this.f16485a.G().N();
        if (N != null) {
            return N.f17128a;
        }
        return null;
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    public final String k0() {
        if (this.f16485a.K() != null) {
            return this.f16485a.K();
        }
        try {
            return new f7.l(zza(), this.f16485a.N()).b("google_app_id");
        } catch (IllegalStateException e8) {
            this.f16485a.i().E().b("getGoogleAppId failed with exception", e8);
            return null;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    public final String l0() {
        AtomicReference atomicReference = new AtomicReference();
        return (String) l().t(atomicReference, 15000L, "String test flag value", new z7(this, atomicReference));
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ a m() {
        return super.m();
    }

    public final void m0() {
        k();
        t();
        if (this.f16485a.q()) {
            Boolean C = a().C("google_analytics_deferred_deep_link_enabled");
            if (C != null && C.booleanValue()) {
                i().D().a("Deferred Deep Link feature enabled.");
                l().B(new Runnable() { // from class: f7.u
                    @Override // java.lang.Runnable
                    public final void run() {
                        h7.this.p0();
                    }
                });
            }
            r().V();
            this.f16637o = false;
            String N = f().N();
            if (TextUtils.isEmpty(N)) {
                return;
            }
            c().n();
            if (N.equals(Build.VERSION.RELEASE)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("_po", N);
            A0("auto", "_ou", bundle);
        }
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ r4 n() {
        return super.n();
    }

    public final void n0() {
        if (!(zza().getApplicationContext() instanceof Application) || this.f16625c == null) {
            return;
        }
        ((Application) zza().getApplicationContext()).unregisterActivityLifecycleCallbacks(this.f16625c);
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ q4 o() {
        return super.o();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void o0() {
        if (ye.a() && a().r(c0.K0)) {
            if (l().H()) {
                i().E().a("Cannot get trigger URIs from analytics worker thread");
            } else if (d.a()) {
                i().E().a("Cannot get trigger URIs from main thread");
            } else {
                t();
                i().I().a("Getting trigger URIs (FE)");
                final AtomicReference atomicReference = new AtomicReference();
                l().t(atomicReference, 5000L, "get trigger URIs", new Runnable() { // from class: com.google.android.gms.measurement.internal.l7
                    @Override // java.lang.Runnable
                    public final void run() {
                        h7 h7Var = h7.this;
                        AtomicReference<List<zzmv>> atomicReference2 = atomicReference;
                        Bundle a9 = h7Var.f().f16614p.a();
                        f9 r4 = h7Var.r();
                        if (a9 == null) {
                            a9 = new Bundle();
                        }
                        r4.P(atomicReference2, a9);
                    }
                });
                final List list = (List) atomicReference.get();
                if (list == null) {
                    i().E().a("Timed out waiting for get trigger URIs");
                } else {
                    l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.k7
                        @Override // java.lang.Runnable
                        public final void run() {
                            h7.this.c0(list);
                        }
                    });
                }
            }
        }
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ h7 p() {
        return super.p();
    }

    public final void p0() {
        k();
        if (f().f16619v.b()) {
            i().D().a("Deferred Deep Link already retrieved. Not fetching again.");
            return;
        }
        long a9 = f().f16620w.a();
        f().f16620w.b(1 + a9);
        if (a9 >= 5) {
            i().J().a("Permanently failed to retrieve Deferred Deep Link. Reached maximum retries.");
            f().f16619v.a(true);
        } else if (!md.a() || !a().r(c0.T0)) {
            this.f16485a.s();
        } else {
            if (this.f16638p == null) {
                this.f16638p = new a8(this, this.f16485a);
            }
            this.f16638p.b(0L);
        }
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ z8 q() {
        return super.q();
    }

    public final void q0() {
        k();
        i().D().a("Handle tcf update.");
        xa c9 = xa.c(f().E());
        i().I().b("Tcf preferences read", c9);
        if (f().z(c9)) {
            Bundle b9 = c9.b();
            i().I().b("Consent generated from Tcf", b9);
            if (b9 != Bundle.EMPTY) {
                F(b9, -30, zzb().a());
            }
            Bundle bundle = new Bundle();
            bundle.putString("_tcfd", c9.e());
            A0("auto", "_tcf", bundle);
        }
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ f9 r() {
        return super.r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(30)
    public final void r0() {
        zzmv poll;
        n1.a Q0;
        k();
        if (t0().isEmpty() || this.f16631i || (poll = t0().poll()) == null || (Q0 = g().Q0()) == null) {
            return;
        }
        this.f16631i = true;
        i().I().b("Registering trigger URI", poll.f17285a);
        com.google.common.util.concurrent.d<cj.a0> c9 = Q0.c(Uri.parse(poll.f17285a));
        if (c9 == null) {
            this.f16631i = false;
            t0().add(poll);
            return;
        }
        SparseArray<Long> H = f().H();
        H.put(poll.f17287c, Long.valueOf(poll.f17286b));
        h5 f5 = f();
        int[] iArr = new int[H.size()];
        long[] jArr = new long[H.size()];
        for (int i8 = 0; i8 < H.size(); i8++) {
            iArr[i8] = H.keyAt(i8);
            jArr[i8] = H.valueAt(i8).longValue();
        }
        Bundle bundle = new Bundle();
        bundle.putIntArray("uriSources", iArr);
        bundle.putLongArray("uriTimestamps", jArr);
        f5.f16614p.b(bundle);
        com.google.common.util.concurrent.b.a(c9, new t7(this, poll), new q7(this));
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ na s() {
        return super.s();
    }

    public final void s0() {
        k();
        i().D().a("Register tcfPrefChangeListener.");
        if (this.q == null) {
            this.f16639r = new x7(this, this.f16485a);
            this.q = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.google.android.gms.measurement.internal.o7
                @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
                public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                    h7.this.D(sharedPreferences, str);
                }
            };
        }
        f().E().registerOnSharedPreferenceChangeListener(this.q);
    }

    public final void v0(Bundle bundle) {
        G(bundle, zzb().a());
    }

    public final void x0(f7.r rVar) {
        t();
        n6.j.l(rVar);
        if (this.f16627e.remove(rVar)) {
            return;
        }
        i().J().a("OnEventListener had not been registered");
    }

    @Override // com.google.android.gms.measurement.internal.v4
    protected final boolean y() {
        return false;
    }

    public final void z0(String str, String str2, Bundle bundle) {
        Y(str, str2, bundle, true, true, zzb().a());
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
