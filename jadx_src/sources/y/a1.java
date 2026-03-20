package y;

import android.util.ArrayMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a1 {

    /* renamed from: b  reason: collision with root package name */
    private static final a1 f24281b = new a1(new ArrayMap());

    /* renamed from: a  reason: collision with root package name */
    protected final Map<String, Object> f24282a;

    /* JADX INFO: Access modifiers changed from: protected */
    public a1(Map<String, Object> map) {
        this.f24282a = map;
    }

    public static a1 a() {
        return f24281b;
    }

    public static a1 b(a1 a1Var) {
        ArrayMap arrayMap = new ArrayMap();
        for (String str : a1Var.d()) {
            arrayMap.put(str, a1Var.c(str));
        }
        return new a1(arrayMap);
    }

    public Object c(String str) {
        return this.f24282a.get(str);
    }

    public Set<String> d() {
        return this.f24282a.keySet();
    }

    public final String toString() {
        return "android.hardware.camera2.CaptureRequest.setTag.CX";
    }
}
