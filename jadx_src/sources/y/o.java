package y;

import android.content.Context;
import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface o {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        o a(Context context, Object obj, Set<String> set);
    }

    SurfaceConfig a(String str, int i8, Size size);

    Map<androidx.camera.core.impl.v<?>, Size> b(String str, List<y.a> list, List<androidx.camera.core.impl.v<?>> list2);
}
