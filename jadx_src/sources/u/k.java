package u;

import android.os.Build;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k implements s0 {

    /* renamed from: a  reason: collision with root package name */
    static final List<String> f22938a = Arrays.asList("SM-A3000", "SM-A3009", "SM-A300F", "SM-A300FU", "SM-A300G", "SM-A300H", "SM-A300M", "SM-A300X", "SM-A300XU", "SM-A300XZ", "SM-A300Y", "SM-A300YZ", "SM-J510FN", "5059X");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        return f22938a.contains(Build.MODEL.toUpperCase(Locale.US));
    }
}
