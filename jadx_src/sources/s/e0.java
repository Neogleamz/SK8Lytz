package s;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.InputConfiguration;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import java.util.List;
import s.f;
import s.h0;
import t.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e0 extends h0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(CameraDevice cameraDevice, Object obj) {
        super(cameraDevice, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e0 g(CameraDevice cameraDevice, Handler handler) {
        return new e0(cameraDevice, new h0.a(handler));
    }

    @Override // s.h0, s.z.a
    public void a(h hVar) {
        h0.c(this.f22744a, hVar);
        f.c cVar = new f.c(hVar.a(), hVar.e());
        List<Surface> f5 = h0.f(hVar.c());
        Handler handler = ((h0.a) androidx.core.util.h.h((h0.a) this.f22745b)).f22746a;
        t.a b9 = hVar.b();
        try {
            if (b9 != null) {
                InputConfiguration inputConfiguration = (InputConfiguration) b9.a();
                androidx.core.util.h.h(inputConfiguration);
                this.f22744a.createReprocessableCaptureSession(inputConfiguration, f5, cVar, handler);
            } else if (hVar.d() == 1) {
                this.f22744a.createConstrainedHighSpeedCaptureSession(f5, cVar, handler);
            } else {
                e(this.f22744a, f5, cVar, handler);
            }
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
