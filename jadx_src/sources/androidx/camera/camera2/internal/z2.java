package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.camera2.internal.n2;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z2 {

    /* renamed from: a  reason: collision with root package name */
    private final b f2192a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {

        /* renamed from: a  reason: collision with root package name */
        private final Executor f2193a;

        /* renamed from: b  reason: collision with root package name */
        private final ScheduledExecutorService f2194b;

        /* renamed from: c  reason: collision with root package name */
        private final Handler f2195c;

        /* renamed from: d  reason: collision with root package name */
        private final v1 f2196d;

        /* renamed from: e  reason: collision with root package name */
        private final y.t0 f2197e;

        /* renamed from: f  reason: collision with root package name */
        private final y.t0 f2198f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean f2199g;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Executor executor, ScheduledExecutorService scheduledExecutorService, Handler handler, v1 v1Var, y.t0 t0Var, y.t0 t0Var2) {
            this.f2193a = executor;
            this.f2194b = scheduledExecutorService;
            this.f2195c = handler;
            this.f2196d = v1Var;
            this.f2197e = t0Var;
            this.f2198f = t0Var2;
            this.f2199g = new v.h(t0Var, t0Var2).b() || new v.v(t0Var).i() || new v.g(t0Var2).d();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public z2 a() {
            return new z2(this.f2199g ? new y2(this.f2197e, this.f2198f, this.f2196d, this.f2193a, this.f2194b, this.f2195c) : new t2(this.f2196d, this.f2193a, this.f2194b, this.f2195c));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface b {
        Executor b();

        t.h j(int i8, List<t.b> list, n2.a aVar);

        com.google.common.util.concurrent.d<List<Surface>> l(List<DeferrableSurface> list, long j8);

        com.google.common.util.concurrent.d<Void> n(CameraDevice cameraDevice, t.h hVar, List<DeferrableSurface> list);

        boolean stop();
    }

    z2(b bVar) {
        this.f2192a = bVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t.h a(int i8, List<t.b> list, n2.a aVar) {
        return this.f2192a.j(i8, list, aVar);
    }

    public Executor b() {
        return this.f2192a.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> c(CameraDevice cameraDevice, t.h hVar, List<DeferrableSurface> list) {
        return this.f2192a.n(cameraDevice, hVar, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<List<Surface>> d(List<DeferrableSurface> list, long j8) {
        return this.f2192a.l(list, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return this.f2192a.stop();
    }
}
