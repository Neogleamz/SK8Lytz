package r;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import androidx.camera.core.g0;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.m;
import androidx.camera.core.impl.n;
import androidx.camera.core.impl.o;
import w.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends j {
    public static final Config.a<Integer> B = Config.a.a("camera2.captureRequest.templateType", Integer.TYPE);
    public static final Config.a<Long> C = Config.a.a("camera2.cameraCaptureSession.streamUseCase", Long.TYPE);
    public static final Config.a<CameraDevice.StateCallback> D = Config.a.a("camera2.cameraDevice.stateCallback", CameraDevice.StateCallback.class);
    public static final Config.a<CameraCaptureSession.StateCallback> E = Config.a.a("camera2.cameraCaptureSession.stateCallback", CameraCaptureSession.StateCallback.class);
    public static final Config.a<CameraCaptureSession.CaptureCallback> F = Config.a.a("camera2.cameraCaptureSession.captureCallback", CameraCaptureSession.CaptureCallback.class);
    public static final Config.a<c> G = Config.a.a("camera2.cameraEvent.callback", c.class);
    public static final Config.a<Object> H = Config.a.a("camera2.captureRequest.tag", Object.class);
    public static final Config.a<String> I = Config.a.a("camera2.cameraCaptureSession.physicalCameraId", String.class);

    /* renamed from: r.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0201a implements g0<a> {

        /* renamed from: a  reason: collision with root package name */
        private final n f22639a = n.P();

        @Override // androidx.camera.core.g0
        public m a() {
            return this.f22639a;
        }

        public a c() {
            return new a(o.N(this.f22639a));
        }

        public C0201a d(Config config) {
            for (Config.a<?> aVar : config.e()) {
                this.f22639a.s(aVar, config.a(aVar));
            }
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <ValueT> C0201a e(CaptureRequest.Key<ValueT> key, ValueT valuet) {
            this.f22639a.s(a.L(key), valuet);
            return this;
        }
    }

    public a(Config config) {
        super(config);
    }

    public static Config.a<Object> L(CaptureRequest.Key<?> key) {
        return Config.a.b("camera2.captureRequest.option." + key.getName(), Object.class, key);
    }

    public c M(c cVar) {
        return (c) l().f(G, cVar);
    }

    public j N() {
        return j.a.e(l()).d();
    }

    public Object O(Object obj) {
        return l().f(H, obj);
    }

    public int P(int i8) {
        return ((Integer) l().f(B, Integer.valueOf(i8))).intValue();
    }

    public CameraDevice.StateCallback Q(CameraDevice.StateCallback stateCallback) {
        return (CameraDevice.StateCallback) l().f(D, stateCallback);
    }

    public String R(String str) {
        return (String) l().f(I, str);
    }

    public CameraCaptureSession.CaptureCallback S(CameraCaptureSession.CaptureCallback captureCallback) {
        return (CameraCaptureSession.CaptureCallback) l().f(F, captureCallback);
    }

    public CameraCaptureSession.StateCallback T(CameraCaptureSession.StateCallback stateCallback) {
        return (CameraCaptureSession.StateCallback) l().f(E, stateCallback);
    }

    public long U(long j8) {
        return ((Long) l().f(C, Long.valueOf(j8))).longValue();
    }
}
