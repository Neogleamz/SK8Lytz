package s;

import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Handler;
import java.util.concurrent.Executor;
import t.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {

    /* renamed from: a  reason: collision with root package name */
    private final a f22763a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a(h hVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends CameraDevice.StateCallback {

        /* renamed from: a  reason: collision with root package name */
        final CameraDevice.StateCallback f22764a;

        /* renamed from: b  reason: collision with root package name */
        private final Executor f22765b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(Executor executor, CameraDevice.StateCallback stateCallback) {
            this.f22765b = executor;
            this.f22764a = stateCallback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(CameraDevice cameraDevice) {
            this.f22764a.onClosed(cameraDevice);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(CameraDevice cameraDevice) {
            this.f22764a.onDisconnected(cameraDevice);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void g(CameraDevice cameraDevice, int i8) {
            this.f22764a.onError(cameraDevice, i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void h(CameraDevice cameraDevice) {
            this.f22764a.onOpened(cameraDevice);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            this.f22765b.execute(new c0(this, cameraDevice));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            this.f22765b.execute(new a0(this, cameraDevice));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i8) {
            this.f22765b.execute(new d0(this, cameraDevice, i8));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            this.f22765b.execute(new b0(this, cameraDevice));
        }
    }

    private z(CameraDevice cameraDevice, Handler handler) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 28) {
            this.f22763a = new g0(cameraDevice);
        } else {
            this.f22763a = i8 >= 24 ? f0.h(cameraDevice, handler) : i8 >= 23 ? e0.g(cameraDevice, handler) : h0.d(cameraDevice, handler);
        }
    }

    public static z b(CameraDevice cameraDevice, Handler handler) {
        return new z(cameraDevice, handler);
    }

    public void a(h hVar) {
        this.f22763a.a(hVar);
    }
}
