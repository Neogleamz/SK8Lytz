package y;

import android.util.Range;
import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {
    public static a a(SurfaceConfig surfaceConfig, int i8, Size size, Range<Integer> range) {
        return new b(surfaceConfig, i8, size, range);
    }

    public abstract int b();

    public abstract Size c();

    public abstract SurfaceConfig d();

    public abstract Range<Integer> e();
}
