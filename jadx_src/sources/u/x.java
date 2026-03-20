package u;

import android.os.Build;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x implements d0.e {

    /* renamed from: a  reason: collision with root package name */
    private static final Set<String> f22953a = new HashSet(Arrays.asList("heroqltevzw", "heroqltetmo", "k61v1_basic_ref"));

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(s.y yVar) {
        return f22953a.contains(Build.DEVICE.toLowerCase(Locale.US));
    }
}
