package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements s0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(s.y yVar) {
        Integer num = (Integer) yVar.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        return num != null && num.intValue() == 2 && Build.VERSION.SDK_INT == 21;
    }

    public int a() {
        return 2;
    }
}
