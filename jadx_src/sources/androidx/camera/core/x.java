package androidx.camera.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.SparseArray;
import androidx.camera.core.impl.CameraValidator;
import androidx.camera.core.impl.MetadataHolderService;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.y;
import androidx.concurrent.futures.c;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import y.o;
import y.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x {

    /* renamed from: o  reason: collision with root package name */
    private static final Object f2846o = new Object();

    /* renamed from: p  reason: collision with root package name */
    private static final SparseArray<Integer> f2847p = new SparseArray<>();

    /* renamed from: c  reason: collision with root package name */
    private final y f2850c;

    /* renamed from: d  reason: collision with root package name */
    private final Executor f2851d;

    /* renamed from: e  reason: collision with root package name */
    private final Handler f2852e;

    /* renamed from: f  reason: collision with root package name */
    private final HandlerThread f2853f;

    /* renamed from: g  reason: collision with root package name */
    private y.p f2854g;

    /* renamed from: h  reason: collision with root package name */
    private y.o f2855h;

    /* renamed from: i  reason: collision with root package name */
    private UseCaseConfigFactory f2856i;

    /* renamed from: j  reason: collision with root package name */
    private Context f2857j;

    /* renamed from: k  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f2858k;

    /* renamed from: n  reason: collision with root package name */
    private final Integer f2861n;

    /* renamed from: a  reason: collision with root package name */
    final y.r f2848a = new y.r();

    /* renamed from: b  reason: collision with root package name */
    private final Object f2849b = new Object();

    /* renamed from: l  reason: collision with root package name */
    private a f2859l = a.UNINITIALIZED;

    /* renamed from: m  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f2860m = a0.f.h(null);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum a {
        UNINITIALIZED,
        INITIALIZING,
        INITIALIZING_ERROR,
        INITIALIZED,
        SHUTDOWN
    }

    public x(Context context, y.b bVar) {
        if (bVar == null && (bVar = f(context)) == null) {
            throw new IllegalStateException("CameraX is not configured properly. The most likely cause is you did not include a default implementation in your build such as 'camera-camera2'.");
        }
        this.f2850c = bVar.getCameraXConfig();
        Executor M = this.f2850c.M(null);
        Handler P = this.f2850c.P(null);
        this.f2851d = M == null ? new q() : M;
        if (P == null) {
            HandlerThread handlerThread = new HandlerThread("CameraX-scheduler", 10);
            this.f2853f = handlerThread;
            handlerThread.start();
            this.f2852e = androidx.core.os.i.a(handlerThread.getLooper());
        } else {
            this.f2853f = null;
            this.f2852e = P;
        }
        Integer num = (Integer) this.f2850c.f(y.G, null);
        this.f2861n = num;
        i(num);
        this.f2858k = k(context);
    }

    private static y.b f(Context context) {
        Application b9 = androidx.camera.core.impl.utils.e.b(context);
        if (b9 instanceof y.b) {
            return (y.b) b9;
        }
        try {
            Context a9 = androidx.camera.core.impl.utils.e.a(context);
            Bundle bundle = a9.getPackageManager().getServiceInfo(new ComponentName(a9, MetadataHolderService.class), 640).metaData;
            String string = bundle != null ? bundle.getString("androidx.camera.core.impl.MetadataHolderService.DEFAULT_CONFIG_PROVIDER") : null;
            if (string == null) {
                p1.c("CameraX", "No default CameraXConfig.Provider specified in meta-data. The most likely cause is you did not include a default implementation in your build such as 'camera-camera2'.");
                return null;
            }
            return (y.b) Class.forName(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (PackageManager.NameNotFoundException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | NullPointerException | InvocationTargetException e8) {
            p1.d("CameraX", "Failed to retrieve default CameraXConfig.Provider from meta-data", e8);
            return null;
        }
    }

    private static void i(Integer num) {
        synchronized (f2846o) {
            if (num == null) {
                return;
            }
            androidx.core.util.h.d(num.intValue(), 3, 6, "minLogLevel");
            SparseArray<Integer> sparseArray = f2847p;
            sparseArray.put(num.intValue(), Integer.valueOf(sparseArray.get(num.intValue()) != null ? 1 + sparseArray.get(num.intValue()).intValue() : 1));
            p();
        }
    }

    private void j(final Executor executor, final long j8, final Context context, final c.a<Void> aVar) {
        executor.execute(new Runnable() { // from class: androidx.camera.core.v
            @Override // java.lang.Runnable
            public final void run() {
                x.this.m(context, executor, aVar, j8);
            }
        });
    }

    private com.google.common.util.concurrent.d<Void> k(final Context context) {
        com.google.common.util.concurrent.d<Void> a9;
        synchronized (this.f2849b) {
            androidx.core.util.h.k(this.f2859l == a.UNINITIALIZED, "CameraX.initInternal() should only be called once per instance");
            this.f2859l = a.INITIALIZING;
            a9 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.u
                @Override // androidx.concurrent.futures.c.InterfaceC0024c
                public final Object a(c.a aVar) {
                    Object n8;
                    n8 = x.this.n(context, aVar);
                    return n8;
                }
            });
        }
        return a9;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l(Executor executor, long j8, c.a aVar) {
        j(executor, j8, this.f2857j, aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m(Context context, final Executor executor, final c.a aVar, final long j8) {
        try {
            Application b9 = androidx.camera.core.impl.utils.e.b(context);
            this.f2857j = b9;
            if (b9 == null) {
                this.f2857j = androidx.camera.core.impl.utils.e.a(context);
            }
            p.a N = this.f2850c.N(null);
            if (N == null) {
                throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing CameraFactory."));
            }
            y.t a9 = y.t.a(this.f2851d, this.f2852e);
            t L = this.f2850c.L(null);
            this.f2854g = N.a(this.f2857j, a9, L);
            o.a O = this.f2850c.O(null);
            if (O == null) {
                throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing CameraDeviceSurfaceManager."));
            }
            this.f2855h = O.a(this.f2857j, this.f2854g.c(), this.f2854g.a());
            UseCaseConfigFactory.b Q = this.f2850c.Q(null);
            if (Q == null) {
                throw new InitializationException(new IllegalArgumentException("Invalid app configuration provided. Missing UseCaseConfigFactory."));
            }
            this.f2856i = Q.a(this.f2857j);
            if (executor instanceof q) {
                ((q) executor).c(this.f2854g);
            }
            this.f2848a.b(this.f2854g);
            CameraValidator.a(this.f2857j, this.f2848a, L);
            o();
            aVar.c(null);
        } catch (InitializationException | CameraValidator.CameraIdListIncorrectException | RuntimeException e8) {
            if (SystemClock.elapsedRealtime() - j8 < 2500) {
                p1.l("CameraX", "Retry init. Start time " + j8 + " current time " + SystemClock.elapsedRealtime(), e8);
                androidx.core.os.i.c(this.f2852e, new Runnable() { // from class: androidx.camera.core.w
                    @Override // java.lang.Runnable
                    public final void run() {
                        x.this.l(executor, j8, aVar);
                    }
                }, "retry_token", 500L);
                return;
            }
            synchronized (this.f2849b) {
                this.f2859l = a.INITIALIZING_ERROR;
            }
            if (e8 instanceof CameraValidator.CameraIdListIncorrectException) {
                p1.c("CameraX", "The device might underreport the amount of the cameras. Finish the initialize task since we are already reaching the maximum number of retries.");
                aVar.c(null);
            } else if (e8 instanceof InitializationException) {
                aVar.f(e8);
            } else {
                aVar.f(new InitializationException(e8));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object n(Context context, c.a aVar) {
        j(this.f2851d, SystemClock.elapsedRealtime(), context, aVar);
        return "CameraX initInternal";
    }

    private void o() {
        synchronized (this.f2849b) {
            this.f2859l = a.INITIALIZED;
        }
    }

    private static void p() {
        SparseArray<Integer> sparseArray = f2847p;
        if (sparseArray.size() == 0) {
            p1.h();
            return;
        }
        int i8 = 3;
        if (sparseArray.get(3) == null) {
            i8 = 4;
            if (sparseArray.get(4) == null) {
                i8 = 5;
                if (sparseArray.get(5) == null) {
                    i8 = 6;
                    if (sparseArray.get(6) == null) {
                        return;
                    }
                }
            }
        }
        p1.i(i8);
    }

    public y.o d() {
        y.o oVar = this.f2855h;
        if (oVar != null) {
            return oVar;
        }
        throw new IllegalStateException("CameraX not initialized yet.");
    }

    public y.r e() {
        return this.f2848a;
    }

    public UseCaseConfigFactory g() {
        UseCaseConfigFactory useCaseConfigFactory = this.f2856i;
        if (useCaseConfigFactory != null) {
            return useCaseConfigFactory;
        }
        throw new IllegalStateException("CameraX not initialized yet.");
    }

    public com.google.common.util.concurrent.d<Void> h() {
        return this.f2858k;
    }
}
