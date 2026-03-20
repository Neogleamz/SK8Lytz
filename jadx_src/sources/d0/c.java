package d0;

import android.os.Build;
import androidx.camera.core.impl.Config;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements s0 {
    private static boolean a() {
        return "HONOR".equalsIgnoreCase(Build.BRAND) && "STK-LX1".equalsIgnoreCase(Build.MODEL);
    }

    private static boolean b() {
        return "HUAWEI".equalsIgnoreCase(Build.BRAND) && "SNE-LX1".equalsIgnoreCase(Build.MODEL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d() {
        return b() || a();
    }

    public boolean c(Config.a<?> aVar) {
        return aVar != androidx.camera.core.impl.f.f2555h;
    }
}
