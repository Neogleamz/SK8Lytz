package androidx.camera.camera2.internal;

import android.os.Build;
import androidx.camera.core.impl.SessionConfig;
import java.util.Collection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l2 {
    public static long a(Collection<androidx.camera.core.impl.v<?>> collection, Collection<SessionConfig> collection2) {
        if (Build.VERSION.SDK_INT < 33) {
            return -1L;
        }
        if (collection.isEmpty()) {
            return 0L;
        }
        for (SessionConfig sessionConfig : collection2) {
            if (sessionConfig.l() == 5) {
                return 0L;
            }
        }
        boolean z4 = false;
        boolean z8 = false;
        boolean z9 = false;
        for (androidx.camera.core.impl.v<?> vVar : collection) {
            if (vVar instanceof androidx.camera.core.impl.i) {
                return 0L;
            }
            if (vVar instanceof androidx.camera.core.impl.p) {
                z9 = true;
            } else if (vVar instanceof androidx.camera.core.impl.j) {
                if (z8) {
                    return 4L;
                }
                z4 = true;
            } else if (!(vVar instanceof androidx.camera.core.impl.w)) {
                continue;
            } else if (z4) {
                return 4L;
            } else {
                z8 = true;
            }
        }
        if (z4) {
            return 2L;
        }
        if (z8) {
            return 3L;
        }
        return !z9 ? 0L : 1L;
    }
}
