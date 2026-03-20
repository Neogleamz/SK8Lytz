package u;

import android.os.Build;
import android.util.Range;
import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private static final Map<String, Range<Integer>> f22940a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f22941a;

        static {
            int[] iArr = new int[SurfaceConfig.ConfigType.values().length];
            f22941a = iArr;
            try {
                iArr[SurfaceConfig.ConfigType.PRIV.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f22941a[SurfaceConfig.ConfigType.YUV.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f22941a[SurfaceConfig.ConfigType.JPEG.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    static {
        HashMap hashMap = new HashMap();
        f22940a = hashMap;
        hashMap.put("SM-T580", null);
        hashMap.put("SM-J710MN", new Range(21, 26));
        hashMap.put("SM-A320FL", null);
        hashMap.put("SM-G570M", null);
        hashMap.put("SM-G610F", null);
        hashMap.put("SM-G610M", new Range(21, 26));
    }

    private static boolean b() {
        if ("samsung".equalsIgnoreCase(Build.BRAND) && f22940a.containsKey(Build.MODEL.toUpperCase(Locale.US))) {
            Range<Integer> range = f22940a.get(Build.MODEL.toUpperCase(Locale.US));
            if (range == null) {
                return true;
            }
            return range.contains((Range<Integer>) Integer.valueOf(Build.VERSION.SDK_INT));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c() {
        return b();
    }

    public Size a(SurfaceConfig.ConfigType configType) {
        if (b()) {
            int i8 = a.f22941a[configType.ordinal()];
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        return null;
                    }
                    return new Size(3264, 1836);
                }
                return new Size(1280, 720);
            }
            return new Size(1920, 1080);
        }
        return null;
    }
}
