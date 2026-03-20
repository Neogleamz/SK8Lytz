package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements s0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        return Build.BRAND.equalsIgnoreCase("SAMSUNG") && Build.VERSION.SDK_INT < 33 && ((Integer) yVar.a(CameraCharacteristics.LENS_FACING)).intValue() == 0;
    }
}
