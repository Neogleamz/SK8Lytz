package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Build;
import android.util.Rational;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.f;
import androidx.concurrent.futures.c;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import r.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b2 {

    /* renamed from: v  reason: collision with root package name */
    private static final MeteringRectangle[] f1732v = new MeteringRectangle[0];

    /* renamed from: a  reason: collision with root package name */
    private final t f1733a;

    /* renamed from: b  reason: collision with root package name */
    final Executor f1734b;

    /* renamed from: c  reason: collision with root package name */
    private final ScheduledExecutorService f1735c;

    /* renamed from: f  reason: collision with root package name */
    private final v.k f1738f;

    /* renamed from: i  reason: collision with root package name */
    private ScheduledFuture<?> f1741i;

    /* renamed from: j  reason: collision with root package name */
    private ScheduledFuture<?> f1742j;
    private MeteringRectangle[] q;

    /* renamed from: r  reason: collision with root package name */
    private MeteringRectangle[] f1749r;

    /* renamed from: s  reason: collision with root package name */
    private MeteringRectangle[] f1750s;

    /* renamed from: t  reason: collision with root package name */
    c.a<Object> f1751t;

    /* renamed from: u  reason: collision with root package name */
    c.a<Void> f1752u;

    /* renamed from: d  reason: collision with root package name */
    private volatile boolean f1736d = false;

    /* renamed from: e  reason: collision with root package name */
    private volatile Rational f1737e = null;

    /* renamed from: g  reason: collision with root package name */
    private boolean f1739g = false;

    /* renamed from: h  reason: collision with root package name */
    Integer f1740h = 0;

    /* renamed from: k  reason: collision with root package name */
    long f1743k = 0;

    /* renamed from: l  reason: collision with root package name */
    boolean f1744l = false;

    /* renamed from: m  reason: collision with root package name */
    boolean f1745m = false;

    /* renamed from: n  reason: collision with root package name */
    private int f1746n = 1;

    /* renamed from: o  reason: collision with root package name */
    private t.c f1747o = null;

    /* renamed from: p  reason: collision with root package name */
    private t.c f1748p = null;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends y.h {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f1753a;

        a(c.a aVar) {
            this.f1753a = aVar;
        }

        @Override // y.h
        public void a() {
            c.a aVar = this.f1753a;
            if (aVar != null) {
                aVar.f(new CameraControl.OperationCanceledException("Camera is closed"));
            }
        }

        @Override // y.h
        public void b(y.j jVar) {
            c.a aVar = this.f1753a;
            if (aVar != null) {
                aVar.c(jVar);
            }
        }

        @Override // y.h
        public void c(CameraCaptureFailure cameraCaptureFailure) {
            c.a aVar = this.f1753a;
            if (aVar != null) {
                aVar.f(new CameraControlInternal.CameraControlException(cameraCaptureFailure));
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends y.h {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f1755a;

        b(c.a aVar) {
            this.f1755a = aVar;
        }

        @Override // y.h
        public void a() {
            c.a aVar = this.f1755a;
            if (aVar != null) {
                aVar.f(new CameraControl.OperationCanceledException("Camera is closed"));
            }
        }

        @Override // y.h
        public void b(y.j jVar) {
            c.a aVar = this.f1755a;
            if (aVar != null) {
                aVar.c(null);
            }
        }

        @Override // y.h
        public void c(CameraCaptureFailure cameraCaptureFailure) {
            c.a aVar = this.f1755a;
            if (aVar != null) {
                aVar.f(new CameraControlInternal.CameraControlException(cameraCaptureFailure));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b2(t tVar, ScheduledExecutorService scheduledExecutorService, Executor executor, y.t0 t0Var) {
        MeteringRectangle[] meteringRectangleArr = f1732v;
        this.q = meteringRectangleArr;
        this.f1749r = meteringRectangleArr;
        this.f1750s = meteringRectangleArr;
        this.f1751t = null;
        this.f1752u = null;
        this.f1733a = tVar;
        this.f1734b = executor;
        this.f1735c = scheduledExecutorService;
        this.f1738f = new v.k(t0Var);
    }

    private void f() {
        ScheduledFuture<?> scheduledFuture = this.f1742j;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.f1742j = null;
        }
    }

    private void g() {
        c.a<Void> aVar = this.f1752u;
        if (aVar != null) {
            aVar.c(null);
            this.f1752u = null;
        }
    }

    private void h() {
        ScheduledFuture<?> scheduledFuture = this.f1741i;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.f1741i = null;
        }
    }

    private void i(String str) {
        this.f1733a.Y(this.f1747o);
        c.a<Object> aVar = this.f1751t;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException(str));
            this.f1751t = null;
        }
    }

    private void j(String str) {
        this.f1733a.Y(this.f1748p);
        c.a<Void> aVar = this.f1752u;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException(str));
            this.f1752u = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean l(int i8, long j8, TotalCaptureResult totalCaptureResult) {
        if (((Integer) totalCaptureResult.get(CaptureResult.CONTROL_AF_MODE)).intValue() == i8 && t.M(totalCaptureResult, j8)) {
            g();
            return true;
        }
        return false;
    }

    private boolean p() {
        return this.q.length > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(a.C0201a c0201a) {
        c0201a.e(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.f1733a.D(this.f1739g ? 1 : k())));
        MeteringRectangle[] meteringRectangleArr = this.q;
        if (meteringRectangleArr.length != 0) {
            c0201a.e(CaptureRequest.CONTROL_AF_REGIONS, meteringRectangleArr);
        }
        MeteringRectangle[] meteringRectangleArr2 = this.f1749r;
        if (meteringRectangleArr2.length != 0) {
            c0201a.e(CaptureRequest.CONTROL_AE_REGIONS, meteringRectangleArr2);
        }
        MeteringRectangle[] meteringRectangleArr3 = this.f1750s;
        if (meteringRectangleArr3.length != 0) {
            c0201a.e(CaptureRequest.CONTROL_AWB_REGIONS, meteringRectangleArr3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(boolean z4, boolean z8) {
        if (this.f1736d) {
            f.a aVar = new f.a();
            aVar.q(true);
            aVar.p(this.f1746n);
            a.C0201a c0201a = new a.C0201a();
            if (z4) {
                c0201a.e(CaptureRequest.CONTROL_AF_TRIGGER, 2);
            }
            if (Build.VERSION.SDK_INT >= 23 && z8) {
                c0201a.e(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 2);
            }
            aVar.e(c0201a.c());
            this.f1733a.f0(Collections.singletonList(aVar.h()));
        }
    }

    void d(c.a<Void> aVar) {
        j("Cancelled by another cancelFocusAndMetering()");
        i("Cancelled by cancelFocusAndMetering()");
        this.f1752u = aVar;
        h();
        f();
        if (p()) {
            c(true, false);
        }
        MeteringRectangle[] meteringRectangleArr = f1732v;
        this.q = meteringRectangleArr;
        this.f1749r = meteringRectangleArr;
        this.f1750s = meteringRectangleArr;
        this.f1739g = false;
        final long i02 = this.f1733a.i0();
        if (this.f1752u != null) {
            final int D = this.f1733a.D(k());
            t.c cVar = new t.c() { // from class: androidx.camera.camera2.internal.a2
                @Override // androidx.camera.camera2.internal.t.c
                public final boolean a(TotalCaptureResult totalCaptureResult) {
                    boolean l8;
                    l8 = b2.this.l(D, i02, totalCaptureResult);
                    return l8;
                }
            };
            this.f1748p = cVar;
            this.f1733a.u(cVar);
        }
    }

    void e() {
        d(null);
    }

    int k() {
        return this.f1746n != 3 ? 4 : 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(boolean z4) {
        if (z4 == this.f1736d) {
            return;
        }
        this.f1736d = z4;
        if (this.f1736d) {
            return;
        }
        e();
    }

    public void n(Rational rational) {
        this.f1737e = rational;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(int i8) {
        this.f1746n = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(c.a<Void> aVar) {
        if (!this.f1736d) {
            if (aVar != null) {
                aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
                return;
            }
            return;
        }
        f.a aVar2 = new f.a();
        aVar2.p(this.f1746n);
        aVar2.q(true);
        a.C0201a c0201a = new a.C0201a();
        c0201a.e(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
        aVar2.e(c0201a.c());
        aVar2.c(new b(aVar));
        this.f1733a.f0(Collections.singletonList(aVar2.h()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(c.a<y.j> aVar, boolean z4) {
        if (!this.f1736d) {
            if (aVar != null) {
                aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
                return;
            }
            return;
        }
        f.a aVar2 = new f.a();
        aVar2.p(this.f1746n);
        aVar2.q(true);
        a.C0201a c0201a = new a.C0201a();
        c0201a.e(CaptureRequest.CONTROL_AF_TRIGGER, 1);
        if (z4) {
            c0201a.e(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(this.f1733a.C(1)));
        }
        aVar2.e(c0201a.c());
        aVar2.c(new a(aVar));
        this.f1733a.f0(Collections.singletonList(aVar2.h()));
    }
}
