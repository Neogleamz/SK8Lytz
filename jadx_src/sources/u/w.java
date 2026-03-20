package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w implements f0 {

    /* renamed from: a  reason: collision with root package name */
    public static final List<String> f22952a = Arrays.asList("sm-a260f", "sm-j530f", "sm-j600g", "sm-j701f", "sm-g610f", "sm-j710mn");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        return f22952a.contains(Build.MODEL.toLowerCase(Locale.US)) && ((Integer) yVar.a(CameraCharacteristics.LENS_FACING)).intValue() == 1;
    }
}
