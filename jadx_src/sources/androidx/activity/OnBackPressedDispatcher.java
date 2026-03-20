package androidx.activity;

import android.os.Build;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.activity.OnBackPressedDispatcher;
import androidx.lifecycle.Lifecycle;
import cj.a0;
import java.util.Collection;
import java.util.ListIterator;
import kotlin.jvm.internal.Lambda;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class OnBackPressedDispatcher {

    /* renamed from: a  reason: collision with root package name */
    private final Runnable f372a;

    /* renamed from: b  reason: collision with root package name */
    private final kotlin.collections.i<l> f373b = new kotlin.collections.i<>();

    /* renamed from: c  reason: collision with root package name */
    private mj.a<a0> f374c;

    /* renamed from: d  reason: collision with root package name */
    private OnBackInvokedCallback f375d;

    /* renamed from: e  reason: collision with root package name */
    private OnBackInvokedDispatcher f376e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f377f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class LifecycleOnBackPressedCancellable implements androidx.lifecycle.h, androidx.activity.a {

        /* renamed from: a  reason: collision with root package name */
        private final Lifecycle f378a;

        /* renamed from: b  reason: collision with root package name */
        private final l f379b;

        /* renamed from: c  reason: collision with root package name */
        private androidx.activity.a f380c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ OnBackPressedDispatcher f381d;

        public LifecycleOnBackPressedCancellable(OnBackPressedDispatcher onBackPressedDispatcher, Lifecycle lifecycle, l lVar) {
            kotlin.jvm.internal.p.e(lifecycle, "lifecycle");
            kotlin.jvm.internal.p.e(lVar, "onBackPressedCallback");
            this.f381d = onBackPressedDispatcher;
            this.f378a = lifecycle;
            this.f379b = lVar;
            lifecycle.a(this);
        }

        @Override // androidx.lifecycle.h
        public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
            kotlin.jvm.internal.p.e(jVar, "source");
            kotlin.jvm.internal.p.e(event, "event");
            if (event == Lifecycle.Event.ON_START) {
                this.f380c = this.f381d.c(this.f379b);
            } else if (event != Lifecycle.Event.ON_STOP) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    cancel();
                }
            } else {
                androidx.activity.a aVar = this.f380c;
                if (aVar != null) {
                    aVar.cancel();
                }
            }
        }

        @Override // androidx.activity.a
        public void cancel() {
            this.f378a.c(this);
            this.f379b.e(this);
            androidx.activity.a aVar = this.f380c;
            if (aVar != null) {
                aVar.cancel();
            }
            this.f380c = null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a extends Lambda implements mj.a<a0> {
        a() {
            super(0);
        }

        public final void c() {
            OnBackPressedDispatcher.this.g();
        }

        public /* bridge */ /* synthetic */ Object invoke() {
            c();
            return a0.a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends Lambda implements mj.a<a0> {
        b() {
            super(0);
        }

        public final void c() {
            OnBackPressedDispatcher.this.e();
        }

        public /* bridge */ /* synthetic */ Object invoke() {
            c();
            return a0.a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public static final c f384a = new c();

        private c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void c(mj.a aVar) {
            kotlin.jvm.internal.p.e(aVar, "$onBackInvoked");
            aVar.invoke();
        }

        public final OnBackInvokedCallback b(final mj.a<a0> aVar) {
            kotlin.jvm.internal.p.e(aVar, "onBackInvoked");
            return new OnBackInvokedCallback() { // from class: androidx.activity.m
                public final void onBackInvoked() {
                    OnBackPressedDispatcher.c.c(aVar);
                }
            };
        }

        public final void d(Object obj, int i8, Object obj2) {
            kotlin.jvm.internal.p.e(obj, "dispatcher");
            kotlin.jvm.internal.p.e(obj2, "callback");
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(i8, (OnBackInvokedCallback) obj2);
        }

        public final void e(Object obj, Object obj2) {
            kotlin.jvm.internal.p.e(obj, "dispatcher");
            kotlin.jvm.internal.p.e(obj2, "callback");
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d implements androidx.activity.a {

        /* renamed from: a  reason: collision with root package name */
        private final l f385a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ OnBackPressedDispatcher f386b;

        public d(OnBackPressedDispatcher onBackPressedDispatcher, l lVar) {
            kotlin.jvm.internal.p.e(lVar, "onBackPressedCallback");
            this.f386b = onBackPressedDispatcher;
            this.f385a = lVar;
        }

        @Override // androidx.activity.a
        public void cancel() {
            this.f386b.f373b.remove(this.f385a);
            this.f385a.e(this);
            if (Build.VERSION.SDK_INT >= 33) {
                this.f385a.g(null);
                this.f386b.g();
            }
        }
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this.f372a = runnable;
        if (Build.VERSION.SDK_INT >= 33) {
            this.f374c = new a();
            this.f375d = c.f384a.b(new b());
        }
    }

    public final void b(androidx.lifecycle.j jVar, l lVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
        kotlin.jvm.internal.p.e(lVar, "onBackPressedCallback");
        Lifecycle lifecycle = jVar.getLifecycle();
        if (lifecycle.b() == Lifecycle.State.DESTROYED) {
            return;
        }
        lVar.a(new LifecycleOnBackPressedCancellable(this, lifecycle, lVar));
        if (Build.VERSION.SDK_INT >= 33) {
            g();
            lVar.g(this.f374c);
        }
    }

    public final androidx.activity.a c(l lVar) {
        kotlin.jvm.internal.p.e(lVar, "onBackPressedCallback");
        this.f373b.add(lVar);
        d dVar = new d(this, lVar);
        lVar.a(dVar);
        if (Build.VERSION.SDK_INT >= 33) {
            g();
            lVar.g(this.f374c);
        }
        return dVar;
    }

    public final boolean d() {
        kotlin.collections.i<l> iVar = this.f373b;
        if ((iVar instanceof Collection) && iVar.isEmpty()) {
            return false;
        }
        for (l lVar : iVar) {
            if (lVar.c()) {
                return true;
            }
        }
        return false;
    }

    public final void e() {
        Object obj;
        kotlin.collections.i<l> iVar = this.f373b;
        ListIterator listIterator = iVar.listIterator(iVar.size());
        while (true) {
            if (!listIterator.hasPrevious()) {
                obj = null;
                break;
            }
            obj = listIterator.previous();
            if (((l) obj).c()) {
                break;
            }
        }
        l lVar = (l) obj;
        if (lVar != null) {
            lVar.b();
            return;
        }
        Runnable runnable = this.f372a;
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void f(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        kotlin.jvm.internal.p.e(onBackInvokedDispatcher, "invoker");
        this.f376e = onBackInvokedDispatcher;
        g();
    }

    public final void g() {
        boolean d8 = d();
        OnBackInvokedDispatcher onBackInvokedDispatcher = this.f376e;
        OnBackInvokedCallback onBackInvokedCallback = this.f375d;
        if (onBackInvokedDispatcher == null || onBackInvokedCallback == null) {
            return;
        }
        if (d8 && !this.f377f) {
            c.f384a.d(onBackInvokedDispatcher, 0, onBackInvokedCallback);
            this.f377f = true;
        } else if (d8 || !this.f377f) {
        } else {
            c.f384a.e(onBackInvokedDispatcher, onBackInvokedCallback);
            this.f377f = false;
        }
    }
}
