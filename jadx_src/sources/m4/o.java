package m4;

import android.util.Pair;
import com.google.android.exoplayer2.drm.DrmSession;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o {
    private static long a(Map<String, String> map, String str) {
        if (map != null) {
            try {
                String str2 = map.get(str);
                if (str2 != null) {
                    return Long.parseLong(str2);
                }
                return -9223372036854775807L;
            } catch (NumberFormatException unused) {
                return -9223372036854775807L;
            }
        }
        return -9223372036854775807L;
    }

    public static Pair<Long, Long> b(DrmSession drmSession) {
        Map<String, String> f5 = drmSession.f();
        if (f5 == null) {
            return null;
        }
        return new Pair<>(Long.valueOf(a(f5, "LicenseDurationRemaining")), Long.valueOf(a(f5, "PlaybackDurationRemaining")));
    }
}
