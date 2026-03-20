package v;

import android.hardware.camera2.CameraCharacteristics;
import androidx.camera.core.p1;
import java.nio.BufferUnderflowException;
import s.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {
    private static boolean a(y yVar) {
        Boolean bool = (Boolean) yVar.a(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        if (bool == null) {
            p1.k("FlashAvailability", "Characteristics did not contain key FLASH_INFO_AVAILABLE. Flash is not available.");
        }
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    private static boolean b(y yVar) {
        try {
            return a(yVar);
        } catch (BufferUnderflowException unused) {
            return false;
        }
    }

    public static boolean c(y yVar) {
        if (u.l.a(u.q.class) != null) {
            p1.a("FlashAvailability", "Device has quirk " + u.q.class.getSimpleName() + ". Checking for flash availability safely...");
            return b(yVar);
        }
        return a(yVar);
    }
}
