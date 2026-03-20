package androidx.camera.camera2.internal;

import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.CameraUnavailableException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l1 {
    public static CameraUnavailableException a(CameraAccessExceptionCompat cameraAccessExceptionCompat) {
        int d8 = cameraAccessExceptionCompat.d();
        int i8 = 5;
        if (d8 == 1) {
            i8 = 1;
        } else if (d8 == 2) {
            i8 = 2;
        } else if (d8 == 3) {
            i8 = 3;
        } else if (d8 == 4) {
            i8 = 4;
        } else if (d8 != 5) {
            i8 = d8 != 10001 ? 0 : 6;
        }
        return new CameraUnavailableException(i8, cameraAccessExceptionCompat);
    }
}
