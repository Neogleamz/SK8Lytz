package u;

import android.os.Build;
import java.util.Arrays;
import java.util.List;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u implements s0 {

    /* renamed from: a  reason: collision with root package name */
    public static final List<String> f22950a = Arrays.asList("Pixel 2", "Pixel 2 XL", "Pixel 3", "Pixel 3 XL");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        return f22950a.contains(Build.MODEL) && "Google".equals(Build.MANUFACTURER) && Build.VERSION.SDK_INT >= 26;
    }
}
