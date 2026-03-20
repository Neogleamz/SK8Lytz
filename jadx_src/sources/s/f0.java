package s;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.InputConfiguration;
import android.os.Handler;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import java.util.List;
import s.f;
import s.h0;
import t.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f0 extends e0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(CameraDevice cameraDevice, Object obj) {
        super(cameraDevice, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f0 h(CameraDevice cameraDevice, Handler handler) {
        return new f0(cameraDevice, new h0.a(handler));
    }

    @Override // s.e0, s.h0, s.z.a
    public void a(h hVar) {
        h0.c(this.f22744a, hVar);
        f.c cVar = new f.c(hVar.a(), hVar.e());
        List<t.b> c9 = hVar.c();
        Handler handler = ((h0.a) androidx.core.util.h.h((h0.a) this.f22745b)).f22746a;
        t.a b9 = hVar.b();
        try {
            if (b9 != null) {
                InputConfiguration inputConfiguration = (InputConfiguration) b9.a();
                androidx.core.util.h.h(inputConfiguration);
                this.f22744a.createReprocessableCaptureSessionByConfigurations(inputConfiguration, h.h(c9), cVar, handler);
            } else if (hVar.d() == 1) {
                this.f22744a.createConstrainedHighSpeedCaptureSession(h0.f(c9), cVar, handler);
            } else {
                this.f22744a.createCaptureSessionByOutputConfigurations(h.h(c9), cVar, handler);
            }
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
