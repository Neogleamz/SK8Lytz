package com.google.android.gms.common.api.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    /* renamed from: e  reason: collision with root package name */
    private static final a f11596e = new a();

    /* renamed from: a  reason: collision with root package name */
    private final AtomicBoolean f11597a = new AtomicBoolean();

    /* renamed from: b  reason: collision with root package name */
    private final AtomicBoolean f11598b = new AtomicBoolean();

    /* renamed from: c  reason: collision with root package name */
    private final ArrayList f11599c = new ArrayList();

    /* renamed from: d  reason: collision with root package name */
    private boolean f11600d = false;

    /* renamed from: com.google.android.gms.common.api.internal.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0124a {
        void a(boolean z4);
    }

    private a() {
    }

    public static a b() {
        return f11596e;
    }

    public static void c(Application application) {
        a aVar = f11596e;
        synchronized (aVar) {
            if (!aVar.f11600d) {
                application.registerActivityLifecycleCallbacks(aVar);
                application.registerComponentCallbacks(aVar);
                aVar.f11600d = true;
            }
        }
    }

    private final void f(boolean z4) {
        synchronized (f11596e) {
            Iterator it = this.f11599c.iterator();
            while (it.hasNext()) {
                ((InterfaceC0124a) it.next()).a(z4);
            }
        }
    }

    public void a(InterfaceC0124a interfaceC0124a) {
        synchronized (f11596e) {
            this.f11599c.add(interfaceC0124a);
        }
    }

    public boolean d() {
        return this.f11597a.get();
    }

    @TargetApi(16)
    public boolean e(boolean z4) {
        if (!this.f11598b.get()) {
            if (!u6.m.b()) {
                return z4;
            }
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(runningAppProcessInfo);
            if (!this.f11598b.getAndSet(true) && runningAppProcessInfo.importance > 100) {
                this.f11597a.set(true);
            }
        }
        return d();
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        AtomicBoolean atomicBoolean = this.f11598b;
        boolean compareAndSet = this.f11597a.compareAndSet(true, false);
        atomicBoolean.set(true);
        if (compareAndSet) {
            f(false);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityResumed(Activity activity) {
        AtomicBoolean atomicBoolean = this.f11598b;
        boolean compareAndSet = this.f11597a.compareAndSet(true, false);
        atomicBoolean.set(true);
        if (compareAndSet) {
            f(false);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }

    @Override // android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
    }

    @Override // android.content.ComponentCallbacks
    public final void onLowMemory() {
    }

    @Override // android.content.ComponentCallbacks2
    public final void onTrimMemory(int i8) {
        if (i8 == 20 && this.f11597a.compareAndSet(false, true)) {
            this.f11598b.set(true);
            f(true);
        }
    }
}
