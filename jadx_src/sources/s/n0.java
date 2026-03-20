package s;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n0 extends m0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public n0(Context context) {
        super(context);
    }

    @Override // s.m0, s.o0, s.l0.b
    public CameraCharacteristics c(String str) {
        try {
            return this.f22753a.getCameraCharacteristics(str);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }

    @Override // s.m0, s.o0, s.l0.b
    public void e(String str, Executor executor, CameraDevice.StateCallback stateCallback) {
        try {
            this.f22753a.openCamera(str, executor, stateCallback);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
