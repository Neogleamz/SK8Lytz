package u;

import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f22954a = Arrays.asList("NEXUS 4");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b() {
        return "GOOGLE".equalsIgnoreCase(Build.BRAND) && Build.VERSION.SDK_INT < 23 && f22954a.contains(Build.MODEL.toUpperCase(Locale.US));
    }

    public int a() {
        return 2;
    }
}
