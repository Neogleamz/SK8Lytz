package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface n2 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(n2 n2Var) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void o(n2 n2Var) {
        }

        public void p(n2 n2Var) {
        }

        public void q(n2 n2Var) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void r(n2 n2Var) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void s(n2 n2Var) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void t(n2 n2Var) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void u(n2 n2Var, Surface surface) {
        }
    }

    a c();

    void close();

    void d();

    int e(List<CaptureRequest> list, CameraCaptureSession.CaptureCallback captureCallback);

    s.f f();

    void g();

    CameraDevice h();

    int i(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback);

    void k();

    com.google.common.util.concurrent.d<Void> m();
}
