package y;

import android.util.ArrayMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o0 extends a1 {
    private o0(Map<String, Object> map) {
        super(map);
    }

    public static o0 f() {
        return new o0(new ArrayMap());
    }

    public static o0 g(a1 a1Var) {
        ArrayMap arrayMap = new ArrayMap();
        for (String str : a1Var.d()) {
            arrayMap.put(str, a1Var.c(str));
        }
        return new o0(arrayMap);
    }

    public void e(a1 a1Var) {
        Map<String, Object> map;
        Map<String, Object> map2 = this.f24282a;
        if (map2 == null || (map = a1Var.f24282a) == null) {
            return;
        }
        map2.putAll(map);
    }

    public void h(String str, Object obj) {
        this.f24282a.put(str, obj);
    }
}
