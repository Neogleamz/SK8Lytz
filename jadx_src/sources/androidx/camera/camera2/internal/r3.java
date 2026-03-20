package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r3 {
    public static boolean a(s.y yVar, int i8) {
        int[] iArr = (int[]) yVar.a(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        if (iArr != null) {
            for (int i9 : iArr) {
                if (i9 == i8) {
                    return true;
                }
            }
        }
        return false;
    }
}
