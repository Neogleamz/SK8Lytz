package u;

import android.hardware.camera2.CameraCharacteristics;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j implements s0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        Integer num = (Integer) yVar.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        return num != null && num.intValue() == 2;
    }
}
