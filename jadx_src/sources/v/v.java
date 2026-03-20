package v;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.internal.k0;
import androidx.camera.camera2.internal.n2;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.List;
import y.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23137a;

    /* renamed from: c  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f23139c;

    /* renamed from: d  reason: collision with root package name */
    c.a<Void> f23140d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23141e;

    /* renamed from: b  reason: collision with root package name */
    private final Object f23138b = new Object();

    /* renamed from: f  reason: collision with root package name */
    private final CameraCaptureSession.CaptureCallback f23142f = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends CameraCaptureSession.CaptureCallback {
        a() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession cameraCaptureSession, int i8) {
            c.a<Void> aVar = v.this.f23140d;
            if (aVar != null) {
                aVar.d();
                v.this.f23140d = null;
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j8, long j9) {
            c.a<Void> aVar = v.this.f23140d;
            if (aVar != null) {
                aVar.c(null);
                v.this.f23140d = null;
            }
        }
    }

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        com.google.common.util.concurrent.d<Void> a(CameraDevice cameraDevice, t.h hVar, List<DeferrableSurface> list);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        int a(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback);
    }

    public v(t0 t0Var) {
        this.f23137a = t0Var.a(u.i.class);
        this.f23139c = i() ? androidx.concurrent.futures.c.a(new u(this)) : a0.f.h(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object d(c.a aVar) {
        this.f23140d = aVar;
        return "WaitForRepeatingRequestStart[" + this + "]";
    }

    public com.google.common.util.concurrent.d<Void> c() {
        return a0.f.j(this.f23139c);
    }

    public void f() {
        synchronized (this.f23138b) {
            if (i() && !this.f23141e) {
                this.f23139c.cancel(true);
            }
        }
    }

    public com.google.common.util.concurrent.d<Void> g(CameraDevice cameraDevice, t.h hVar, List<DeferrableSurface> list, List<n2> list2, b bVar) {
        ArrayList arrayList = new ArrayList();
        for (n2 n2Var : list2) {
            arrayList.add(n2Var.m());
        }
        return a0.d.a(a0.f.n(arrayList)).f(new t(bVar, cameraDevice, hVar, list), z.a.a());
    }

    public int h(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback, c cVar) {
        int a9;
        synchronized (this.f23138b) {
            if (i()) {
                captureCallback = k0.b(this.f23142f, captureCallback);
                this.f23141e = true;
            }
            a9 = cVar.a(captureRequest, captureCallback);
        }
        return a9;
    }

    public boolean i() {
        return this.f23137a;
    }
}
