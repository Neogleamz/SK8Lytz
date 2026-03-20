package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import v.g;
import v.v;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y2 extends t2 {

    /* renamed from: o  reason: collision with root package name */
    private final Object f2179o;

    /* renamed from: p  reason: collision with root package name */
    private List<DeferrableSurface> f2180p;
    com.google.common.util.concurrent.d<Void> q;

    /* renamed from: r  reason: collision with root package name */
    private final v.h f2181r;

    /* renamed from: s  reason: collision with root package name */
    private final v.v f2182s;

    /* renamed from: t  reason: collision with root package name */
    private final v.g f2183t;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y2(y.t0 t0Var, y.t0 t0Var2, v1 v1Var, Executor executor, ScheduledExecutorService scheduledExecutorService, Handler handler) {
        super(v1Var, executor, scheduledExecutorService, handler);
        this.f2179o = new Object();
        this.f2181r = new v.h(t0Var, t0Var2);
        this.f2182s = new v.v(t0Var);
        this.f2183t = new v.g(t0Var2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void O() {
        N("Session call super.close()");
        super.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void P(n2 n2Var) {
        super.r(n2Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d Q(CameraDevice cameraDevice, t.h hVar, List list) {
        return super.n(cameraDevice, hVar, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int R(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback) {
        return super.i(captureRequest, captureCallback);
    }

    void N(String str) {
        androidx.camera.core.p1.a("SyncCaptureSessionImpl", "[" + this + "] " + str);
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.n2
    public void close() {
        N("Session call close()");
        this.f2182s.f();
        this.f2182s.c().c(new Runnable() { // from class: androidx.camera.camera2.internal.u2
            @Override // java.lang.Runnable
            public final void run() {
                y2.this.O();
            }
        }, b());
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.n2
    public int i(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback) {
        return this.f2182s.h(captureRequest, captureCallback, new v.c() { // from class: androidx.camera.camera2.internal.x2
            @Override // v.v.c
            public final int a(CaptureRequest captureRequest2, CameraCaptureSession.CaptureCallback captureCallback2) {
                int R;
                R = y2.this.R(captureRequest2, captureCallback2);
                return R;
            }
        });
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.z2.b
    public com.google.common.util.concurrent.d<List<Surface>> l(List<DeferrableSurface> list, long j8) {
        com.google.common.util.concurrent.d<List<Surface>> l8;
        synchronized (this.f2179o) {
            this.f2180p = list;
            l8 = super.l(list, j8);
        }
        return l8;
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.n2
    public com.google.common.util.concurrent.d<Void> m() {
        return this.f2182s.c();
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.z2.b
    public com.google.common.util.concurrent.d<Void> n(CameraDevice cameraDevice, t.h hVar, List<DeferrableSurface> list) {
        com.google.common.util.concurrent.d<Void> j8;
        synchronized (this.f2179o) {
            com.google.common.util.concurrent.d<Void> g8 = this.f2182s.g(cameraDevice, hVar, list, this.f2115b.e(), new v.b() { // from class: androidx.camera.camera2.internal.w2
                @Override // v.v.b
                public final com.google.common.util.concurrent.d a(CameraDevice cameraDevice2, t.h hVar2, List list2) {
                    com.google.common.util.concurrent.d Q;
                    Q = y2.this.Q(cameraDevice2, hVar2, list2);
                    return Q;
                }
            });
            this.q = g8;
            j8 = a0.f.j(g8);
        }
        return j8;
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.n2.a
    public void p(n2 n2Var) {
        synchronized (this.f2179o) {
            this.f2181r.a(this.f2180p);
        }
        N("onClosed()");
        super.p(n2Var);
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.n2.a
    public void r(n2 n2Var) {
        N("Session onConfigured()");
        this.f2183t.c(n2Var, this.f2115b.f(), this.f2115b.d(), new g.a() { // from class: androidx.camera.camera2.internal.v2
            @Override // v.g.a
            public final void a(n2 n2Var2) {
                y2.this.P(n2Var2);
            }
        });
    }

    @Override // androidx.camera.camera2.internal.t2, androidx.camera.camera2.internal.z2.b
    public boolean stop() {
        boolean stop;
        synchronized (this.f2179o) {
            if (C()) {
                this.f2181r.a(this.f2180p);
            } else {
                com.google.common.util.concurrent.d<Void> dVar = this.q;
                if (dVar != null) {
                    dVar.cancel(true);
                }
            }
            stop = super.stop();
        }
        return stop;
    }
}
