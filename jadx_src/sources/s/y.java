package s;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y {

    /* renamed from: a  reason: collision with root package name */
    private final Map<CameraCharacteristics.Key<?>, Object> f22761a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final a f22762b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        <T> T a(CameraCharacteristics.Key<T> key);
    }

    private y(CameraCharacteristics cameraCharacteristics) {
        this.f22762b = Build.VERSION.SDK_INT >= 28 ? new w(cameraCharacteristics) : new x(cameraCharacteristics);
    }

    private boolean b(CameraCharacteristics.Key<?> key) {
        return key.equals(CameraCharacteristics.SENSOR_ORIENTATION);
    }

    public static y c(CameraCharacteristics cameraCharacteristics) {
        return new y(cameraCharacteristics);
    }

    public <T> T a(CameraCharacteristics.Key<T> key) {
        if (b(key)) {
            return (T) this.f22762b.a(key);
        }
        synchronized (this) {
            T t8 = (T) this.f22761a.get(key);
            if (t8 != null) {
                return t8;
            }
            T t9 = (T) this.f22762b.a(key);
            if (t9 != null) {
                this.f22761a.put(key, t9);
            }
            return t9;
        }
    }
}
