package androidx.constraintlayout.motion.widget;

import java.util.Arrays;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: a  reason: collision with root package name */
    HashMap<Object, HashMap<String, float[]>> f3270a = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a(Object obj, String str, int i8) {
        if (this.f3270a.containsKey(obj)) {
            HashMap<String, float[]> hashMap = this.f3270a.get(obj);
            if (hashMap.containsKey(str)) {
                float[] fArr = hashMap.get(str);
                if (fArr.length > i8) {
                    return fArr[i8];
                }
                return Float.NaN;
            }
            return Float.NaN;
        }
        return Float.NaN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Object obj, String str, int i8, float f5) {
        HashMap<String, float[]> hashMap;
        if (this.f3270a.containsKey(obj)) {
            hashMap = this.f3270a.get(obj);
            if (hashMap.containsKey(str)) {
                float[] fArr = hashMap.get(str);
                if (fArr.length <= i8) {
                    fArr = Arrays.copyOf(fArr, i8 + 1);
                }
                fArr[i8] = f5;
                hashMap.put(str, fArr);
                return;
            }
            float[] fArr2 = new float[i8 + 1];
            fArr2[i8] = f5;
            hashMap.put(str, fArr2);
        } else {
            hashMap = new HashMap<>();
            float[] fArr3 = new float[i8 + 1];
            fArr3[i8] = f5;
            hashMap.put(str, fArr3);
        }
        this.f3270a.put(obj, hashMap);
    }
}
