package s;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.SessionConfiguration;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.core.util.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g0 extends f0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public g0(CameraDevice cameraDevice) {
        super((CameraDevice) h.h(cameraDevice), null);
    }

    @Override // s.f0, s.e0, s.h0, s.z.a
    public void a(t.h hVar) {
        SessionConfiguration sessionConfiguration = (SessionConfiguration) hVar.j();
        h.h(sessionConfiguration);
        try {
            this.f22744a.createCaptureSession(sessionConfiguration);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
