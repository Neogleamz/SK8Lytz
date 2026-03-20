package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t implements f0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f22948a = Arrays.asList("itel w6004");

    /* renamed from: b  reason: collision with root package name */
    private static final List<String> f22949b = Arrays.asList("sm-j700f", "sm-j710f");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        List<String> list = f22949b;
        String str = Build.MODEL;
        Locale locale = Locale.US;
        return (list.contains(str.toLowerCase(locale)) && ((Integer) yVar.a(CameraCharacteristics.LENS_FACING)).intValue() == 0) || f22948a.contains(str.toLowerCase(locale));
    }
}
