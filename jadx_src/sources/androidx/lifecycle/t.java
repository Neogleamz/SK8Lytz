package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t implements j {

    /* renamed from: j  reason: collision with root package name */
    public static final b f5906j = new b(null);

    /* renamed from: k  reason: collision with root package name */
    private static final t f5907k = new t();

    /* renamed from: a  reason: collision with root package name */
    private int f5908a;

    /* renamed from: b  reason: collision with root package name */
    private int f5909b;

    /* renamed from: e  reason: collision with root package name */
    private Handler f5912e;

    /* renamed from: c  reason: collision with root package name */
    private boolean f5910c = true;

    /* renamed from: d  reason: collision with root package name */
    private boolean f5911d = true;

    /* renamed from: f  reason: collision with root package name */
    private final k f5913f = new k(this);

    /* renamed from: g  reason: collision with root package name */
    private final Runnable f5914g = new Runnable() { // from class: androidx.lifecycle.s
        @Override // java.lang.Runnable
        public final void run() {
            t.i(t.this);
        }
    };

    /* renamed from: h  reason: collision with root package name */
    private final u.a f5915h = new d();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public static final a f5916a = new a();

        private a() {
        }

        public static final void a(Activity activity, Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
            kotlin.jvm.internal.p.e(activity, "activity");
            kotlin.jvm.internal.p.e(activityLifecycleCallbacks, "callback");
            activity.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(kotlin.jvm.internal.i iVar) {
            this();
        }

        public final j a() {
            return t.f5907k;
        }

        public final void b(Context context) {
            kotlin.jvm.internal.p.e(context, "context");
            t.f5907k.h(context);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends androidx.lifecycle.c {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends androidx.lifecycle.c {
            final /* synthetic */ t this$0;

            a(t tVar) {
                this.this$0 = tVar;
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostResumed(Activity activity) {
                kotlin.jvm.internal.p.e(activity, "activity");
                this.this$0.e();
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostStarted(Activity activity) {
                kotlin.jvm.internal.p.e(activity, "activity");
                this.this$0.f();
            }
        }

        c() {
        }

        @Override // androidx.lifecycle.c, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            kotlin.jvm.internal.p.e(activity, "activity");
            if (Build.VERSION.SDK_INT < 29) {
                u.f5918b.b(activity).f(t.this.f5915h);
            }
        }

        @Override // androidx.lifecycle.c, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            kotlin.jvm.internal.p.e(activity, "activity");
            t.this.d();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPreCreated(Activity activity, Bundle bundle) {
            kotlin.jvm.internal.p.e(activity, "activity");
            a.a(activity, new a(t.this));
        }

        @Override // androidx.lifecycle.c, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
            kotlin.jvm.internal.p.e(activity, "activity");
            t.this.g();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements u.a {
        d() {
        }

        @Override // androidx.lifecycle.u.a
        public void onCreate() {
        }

        @Override // androidx.lifecycle.u.a
        public void onResume() {
            t.this.e();
        }

        @Override // androidx.lifecycle.u.a
        public void onStart() {
            t.this.f();
        }
    }

    private t() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void i(t tVar) {
        kotlin.jvm.internal.p.e(tVar, "this$0");
        tVar.j();
        tVar.k();
    }

    public static final j l() {
        return f5906j.a();
    }

    public final void d() {
        int i8 = this.f5909b - 1;
        this.f5909b = i8;
        if (i8 == 0) {
            Handler handler = this.f5912e;
            kotlin.jvm.internal.p.b(handler);
            handler.postDelayed(this.f5914g, 700L);
        }
    }

    public final void e() {
        int i8 = this.f5909b + 1;
        this.f5909b = i8;
        if (i8 == 1) {
            if (this.f5910c) {
                this.f5913f.h(Lifecycle.Event.ON_RESUME);
                this.f5910c = false;
                return;
            }
            Handler handler = this.f5912e;
            kotlin.jvm.internal.p.b(handler);
            handler.removeCallbacks(this.f5914g);
        }
    }

    public final void f() {
        int i8 = this.f5908a + 1;
        this.f5908a = i8;
        if (i8 == 1 && this.f5911d) {
            this.f5913f.h(Lifecycle.Event.ON_START);
            this.f5911d = false;
        }
    }

    public final void g() {
        this.f5908a--;
        k();
    }

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return this.f5913f;
    }

    public final void h(Context context) {
        kotlin.jvm.internal.p.e(context, "context");
        this.f5912e = new Handler();
        this.f5913f.h(Lifecycle.Event.ON_CREATE);
        Context applicationContext = context.getApplicationContext();
        kotlin.jvm.internal.p.c(applicationContext, "null cannot be cast to non-null type android.app.Application");
        ((Application) applicationContext).registerActivityLifecycleCallbacks(new c());
    }

    public final void j() {
        if (this.f5909b == 0) {
            this.f5910c = true;
            this.f5913f.h(Lifecycle.Event.ON_PAUSE);
        }
    }

    public final void k() {
        if (this.f5908a == 0 && this.f5910c) {
            this.f5913f.h(Lifecycle.Event.ON_STOP);
            this.f5911d = true;
        }
    }
}
