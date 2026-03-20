package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.measurement.dynamite.ModuleDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p2 {

    /* renamed from: j  reason: collision with root package name */
    private static volatile p2 f12420j;

    /* renamed from: a  reason: collision with root package name */
    private final String f12421a;

    /* renamed from: b  reason: collision with root package name */
    protected final u6.d f12422b;

    /* renamed from: c  reason: collision with root package name */
    private final ExecutorService f12423c;

    /* renamed from: d  reason: collision with root package name */
    private final e7.a f12424d;

    /* renamed from: e  reason: collision with root package name */
    private final List<Pair<f7.r, Object>> f12425e;

    /* renamed from: f  reason: collision with root package name */
    private int f12426f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f12427g;

    /* renamed from: h  reason: collision with root package name */
    private String f12428h;

    /* renamed from: i  reason: collision with root package name */
    private volatile c2 f12429i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public abstract class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final long f12430a;

        /* renamed from: b  reason: collision with root package name */
        final long f12431b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f12432c;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(p2 p2Var) {
            this(true);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(boolean z4) {
            this.f12430a = p2.this.f12422b.a();
            this.f12431b = p2.this.f12422b.b();
            this.f12432c = z4;
        }

        abstract void a();

        protected void b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (p2.this.f12427g) {
                b();
                return;
            }
            try {
                a();
            } catch (Exception e8) {
                p2.this.p(e8, false, this.f12432c);
                b();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Application.ActivityLifecycleCallbacks {
        b() {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityCreated(Activity activity, Bundle bundle) {
            p2.this.l(new l3(this, bundle, activity));
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityDestroyed(Activity activity) {
            p2.this.l(new q3(this, activity));
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityPaused(Activity activity) {
            p2.this.l(new m3(this, activity));
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityResumed(Activity activity) {
            p2.this.l(new n3(this, activity));
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            d2 d2Var = new d2();
            p2.this.l(new o3(this, activity, d2Var));
            Bundle e8 = d2Var.e(50L);
            if (e8 != null) {
                bundle.putAll(e8);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStarted(Activity activity) {
            p2.this.l(new j3(this, activity));
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStopped(Activity activity) {
            p2.this.l(new p3(this, activity));
        }
    }

    private p2(Context context, String str, String str2, String str3, Bundle bundle) {
        this.f12421a = (str == null || !C(str2, str3)) ? "FA" : "FA";
        this.f12422b = u6.g.d();
        this.f12423c = t1.a().a(new w2(this), y1.f12676a);
        this.f12424d = new e7.a(this);
        this.f12425e = new ArrayList();
        if (!(!z(context) || H())) {
            this.f12428h = null;
            this.f12427g = true;
            Log.w(this.f12421a, "Disabling data collection. Found google_app_id in strings.xml but Google Analytics for Firebase is missing. Remove this value or add Google Analytics for Firebase to resume data collection.");
            return;
        }
        if (C(str2, str3)) {
            this.f12428h = str2;
        } else {
            this.f12428h = "fa";
            if (str2 == null || str3 == null) {
                if ((str2 == null) ^ (str3 == null)) {
                    Log.w(this.f12421a, "Specified origin or custom app id is null. Both parameters will be ignored.");
                }
            } else {
                Log.v(this.f12421a, "Deferring to Google Analytics for Firebase for event data collection. https://firebase.google.com/docs/analytics");
            }
        }
        l(new o2(this, str2, str3, context, bundle));
        Application application = (Application) context.getApplicationContext();
        if (application == null) {
            Log.w(this.f12421a, "Unable to register lifecycle notifications. Application null.");
        } else {
            application.registerActivityLifecycleCallbacks(new b());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean C(String str, String str2) {
        return (str2 == null || str == null || H()) ? false : true;
    }

    private final boolean H() {
        try {
            Class.forName("com.google.firebase.analytics.FirebaseAnalytics", false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    public static p2 e(Context context) {
        return f(context, null, null, null, null);
    }

    public static p2 f(Context context, String str, String str2, String str3, Bundle bundle) {
        n6.j.l(context);
        if (f12420j == null) {
            synchronized (p2.class) {
                if (f12420j == null) {
                    f12420j = new p2(context, str, str2, str3, bundle);
                }
            }
        }
        return f12420j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void l(a aVar) {
        this.f12423c.execute(aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void p(Exception exc, boolean z4, boolean z8) {
        this.f12427g |= z4;
        if (z4) {
            Log.w(this.f12421a, "Data collection startup failed. No data will be collected.", exc);
            return;
        }
        if (z8) {
            i(5, "Error with data collection. Data lost.", exc, null, null);
        }
        Log.w(this.f12421a, "Error with data collection. Data lost.", exc);
    }

    private final void r(String str, String str2, Bundle bundle, boolean z4, boolean z8, Long l8) {
        l(new i3(this, l8, str, str2, bundle, z4, z8));
    }

    private static boolean z(Context context) {
        return new f7.l(context, f7.l.a(context)).b("google_app_id") != null;
    }

    public final void A(String str) {
        l(new x2(this, str));
    }

    public final String D() {
        d2 d2Var = new d2();
        l(new z2(this, d2Var));
        return d2Var.k(50L);
    }

    public final String E() {
        d2 d2Var = new d2();
        l(new e3(this, d2Var));
        return d2Var.k(500L);
    }

    public final String F() {
        d2 d2Var = new d2();
        l(new b3(this, d2Var));
        return d2Var.k(500L);
    }

    public final String G() {
        d2 d2Var = new d2();
        l(new a3(this, d2Var));
        return d2Var.k(500L);
    }

    public final int a(String str) {
        d2 d2Var = new d2();
        l(new g3(this, str, d2Var));
        Integer num = (Integer) d2.f(d2Var.e(10000L), Integer.class);
        if (num == null) {
            return 25;
        }
        return num.intValue();
    }

    public final long b() {
        d2 d2Var = new d2();
        l(new c3(this, d2Var));
        Long g8 = d2Var.g(500L);
        if (g8 == null) {
            long nextLong = new Random(System.nanoTime() ^ this.f12422b.a()).nextLong();
            int i8 = this.f12426f + 1;
            this.f12426f = i8;
            return nextLong + i8;
        }
        return g8.longValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final c2 c(Context context, boolean z4) {
        try {
            return f2.asInterface(DynamiteModule.d(context, DynamiteModule.f12018e, ModuleDescriptor.MODULE_ID).c("com.google.android.gms.measurement.internal.AppMeasurementDynamiteService"));
        } catch (DynamiteModule.LoadingException e8) {
            p(e8, true, false);
            return null;
        }
    }

    public final List<Bundle> g(String str, String str2) {
        d2 d2Var = new d2();
        l(new s2(this, str, str2, d2Var));
        List<Bundle> list = (List) d2.f(d2Var.e(5000L), List.class);
        return list == null ? Collections.emptyList() : list;
    }

    public final Map<String, Object> h(String str, String str2, boolean z4) {
        d2 d2Var = new d2();
        l(new d3(this, str, str2, z4, d2Var));
        Bundle e8 = d2Var.e(5000L);
        if (e8 == null || e8.size() == 0) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap(e8.size());
        for (String str3 : e8.keySet()) {
            Object obj = e8.get(str3);
            if ((obj instanceof Double) || (obj instanceof Long) || (obj instanceof String)) {
                hashMap.put(str3, obj);
            }
        }
        return hashMap;
    }

    public final void i(int i8, String str, Object obj, Object obj2, Object obj3) {
        l(new f3(this, false, 5, str, obj, null, null));
    }

    public final void j(Activity activity, String str, String str2) {
        l(new u2(this, activity, str, str2));
    }

    public final void k(Bundle bundle) {
        l(new q2(this, bundle));
    }

    public final void q(String str, String str2, Bundle bundle) {
        l(new t2(this, str, str2, bundle));
    }

    public final void s(String str, String str2, Object obj, boolean z4) {
        l(new r2(this, str, str2, obj, z4));
    }

    public final void t(boolean z4) {
        l(new h3(this, z4));
    }

    public final e7.a v() {
        return this.f12424d;
    }

    public final void x(String str) {
        l(new y2(this, str));
    }

    public final void y(String str, String str2, Bundle bundle) {
        r(str, str2, bundle, true, true, null);
    }
}
