package u;

import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e0 implements s0 {

    /* renamed from: a  reason: collision with root package name */
    public static final List<String> f22936a = Arrays.asList("mi a1", "mi a2", "mi a2 lite", "redmi 4x", "redmi 5a", "redmi 6 pro");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        return f22936a.contains(Build.MODEL.toLowerCase(Locale.US));
    }
}
