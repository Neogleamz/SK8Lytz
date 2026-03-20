package u;

import android.os.Build;
import android.util.Pair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private static final Set<Pair<String, String>> f22945a = new HashSet(Arrays.asList(new Pair("sprd", "lemp")));

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        Set<Pair<String, String>> set = f22945a;
        String str = Build.MANUFACTURER;
        Locale locale = Locale.US;
        return set.contains(new Pair(str.toLowerCase(locale), Build.MODEL.toLowerCase(locale)));
    }
}
