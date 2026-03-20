package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r implements f0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f22946a = Arrays.asList("PIXEL 3A", "PIXEL 3A XL");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        return f22946a.contains(Build.MODEL.toUpperCase(Locale.US)) && ((Integer) yVar.a(CameraCharacteristics.LENS_FACING)).intValue() == 1;
    }
}
