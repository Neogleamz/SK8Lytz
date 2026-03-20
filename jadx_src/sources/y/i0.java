package y;

import androidx.camera.core.s;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i0 implements androidx.camera.core.r {

    /* renamed from: b  reason: collision with root package name */
    private int f24300b;

    public i0(int i8) {
        this.f24300b = i8;
    }

    @Override // androidx.camera.core.r
    public List<s> b(List<s> list) {
        ArrayList arrayList = new ArrayList();
        for (s sVar : list) {
            androidx.core.util.h.b(sVar instanceof q, "The camera info doesn't contain internal implementation.");
            Integer a9 = ((q) sVar).a();
            if (a9 != null && a9.intValue() == this.f24300b) {
                arrayList.add(sVar);
            }
        }
        return arrayList;
    }

    public int c() {
        return this.f24300b;
    }
}
