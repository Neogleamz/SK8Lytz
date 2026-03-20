package u;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f22947a = Arrays.asList("sm-j700f", "sm-j710f");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        return f22947a.contains(Build.MODEL.toLowerCase(Locale.US)) && ((Integer) yVar.a(CameraCharacteristics.LENS_FACING)).intValue() == 0;
    }
}
