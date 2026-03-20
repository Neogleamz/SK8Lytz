package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Looper;
import android.util.Range;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.concurrent.futures.c;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
import r.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j3 {

    /* renamed from: a  reason: collision with root package name */
    private final t f1901a;

    /* renamed from: b  reason: collision with root package name */
    private final Executor f1902b;

    /* renamed from: c  reason: collision with root package name */
    private final k3 f1903c;

    /* renamed from: d  reason: collision with root package name */
    private final androidx.lifecycle.p<androidx.camera.core.h3> f1904d;

    /* renamed from: e  reason: collision with root package name */
    final b f1905e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f1906f = false;

    /* renamed from: g  reason: collision with root package name */
    private t.c f1907g = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements t.c {
        a() {
        }

        @Override // androidx.camera.camera2.internal.t.c
        public boolean a(TotalCaptureResult totalCaptureResult) {
            j3.this.f1905e.a(totalCaptureResult);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(TotalCaptureResult totalCaptureResult);

        void b(a.C0201a c0201a);

        void c(float f5, c.a<Void> aVar);

        float d();

        float e();

        void f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j3(t tVar, s.y yVar, Executor executor) {
        this.f1901a = tVar;
        this.f1902b = executor;
        b f5 = f(yVar);
        this.f1905e = f5;
        k3 k3Var = new k3(f5.d(), f5.e());
        this.f1903c = k3Var;
        k3Var.h(1.0f);
        this.f1904d = new androidx.lifecycle.p<>(b0.f.e(k3Var));
        tVar.u(this.f1907g);
    }

    private static b f(s.y yVar) {
        return j(yVar) ? new androidx.camera.camera2.internal.a(yVar) : new w1(yVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static androidx.camera.core.h3 g(s.y yVar) {
        b f5 = f(yVar);
        k3 k3Var = new k3(f5.d(), f5.e());
        k3Var.h(1.0f);
        return b0.f.e(k3Var);
    }

    private static Range<Float> h(s.y yVar) {
        try {
            return (Range) yVar.a(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE);
        } catch (AssertionError e8) {
            androidx.camera.core.p1.l("ZoomControl", "AssertionError, fail to get camera characteristic.", e8);
            return null;
        }
    }

    static boolean j(s.y yVar) {
        return Build.VERSION.SDK_INT >= 30 && h(yVar) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object l(final androidx.camera.core.h3 h3Var, final c.a aVar) {
        this.f1902b.execute(new Runnable() { // from class: androidx.camera.camera2.internal.i3
            @Override // java.lang.Runnable
            public final void run() {
                j3.this.k(aVar, h3Var);
            }
        });
        return "setLinearZoom";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object n(final androidx.camera.core.h3 h3Var, final c.a aVar) {
        this.f1902b.execute(new Runnable() { // from class: androidx.camera.camera2.internal.h3
            @Override // java.lang.Runnable
            public final void run() {
                j3.this.m(aVar, h3Var);
            }
        });
        return "setZoomRatio";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: r */
    public void m(c.a<Void> aVar, androidx.camera.core.h3 h3Var) {
        androidx.camera.core.h3 e8;
        if (this.f1906f) {
            s(h3Var);
            this.f1905e.c(h3Var.c(), aVar);
            this.f1901a.i0();
            return;
        }
        synchronized (this.f1903c) {
            this.f1903c.h(1.0f);
            e8 = b0.f.e(this.f1903c);
        }
        s(e8);
        aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
    }

    private void s(androidx.camera.core.h3 h3Var) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.f1904d.o(h3Var);
        } else {
            this.f1904d.l(h3Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(a.C0201a c0201a) {
        this.f1905e.b(c0201a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LiveData<androidx.camera.core.h3> i() {
        return this.f1904d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(boolean z4) {
        androidx.camera.core.h3 e8;
        if (this.f1906f == z4) {
            return;
        }
        this.f1906f = z4;
        if (z4) {
            return;
        }
        synchronized (this.f1903c) {
            this.f1903c.h(1.0f);
            e8 = b0.f.e(this.f1903c);
        }
        s(e8);
        this.f1905e.f();
        this.f1901a.i0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> p(float f5) {
        final androidx.camera.core.h3 e8;
        synchronized (this.f1903c) {
            try {
                this.f1903c.g(f5);
                e8 = b0.f.e(this.f1903c);
            } catch (IllegalArgumentException e9) {
                return a0.f.f(e9);
            }
        }
        s(e8);
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.g3
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object l8;
                l8 = j3.this.l(e8, aVar);
                return l8;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> q(float f5) {
        final androidx.camera.core.h3 e8;
        synchronized (this.f1903c) {
            try {
                this.f1903c.h(f5);
                e8 = b0.f.e(this.f1903c);
            } catch (IllegalArgumentException e9) {
                return a0.f.f(e9);
            }
        }
        s(e8);
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.f3
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object n8;
                n8 = j3.this.n(e8, aVar);
                return n8;
            }
        });
    }
}
