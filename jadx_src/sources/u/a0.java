package u;

import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a0 implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f22934a = Arrays.asList("sunfish", "bramble", "redfin", "barbet");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        return "Google".equals(Build.MANUFACTURER) && f22934a.contains(Build.DEVICE.toLowerCase(Locale.getDefault()));
    }
}
