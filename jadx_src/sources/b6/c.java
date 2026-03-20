package b6;

import android.os.Bundle;
import android.util.SparseArray;
import com.google.android.exoplayer2.g;
import com.google.common.collect.ImmutableList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {
    private c() {
    }

    public static void a(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader((ClassLoader) l0.j(c.class.getClassLoader()));
        }
    }

    public static <T extends com.google.android.exoplayer2.g> ImmutableList<T> b(g.a<T> aVar, List<Bundle> list) {
        ImmutableList.a u8 = ImmutableList.u();
        for (int i8 = 0; i8 < list.size(); i8++) {
            u8.a(aVar.a((Bundle) a.e(list.get(i8))));
        }
        return u8.k();
    }

    public static <T extends com.google.android.exoplayer2.g> SparseArray<T> c(g.a<T> aVar, SparseArray<Bundle> sparseArray) {
        SparseArray<T> sparseArray2 = new SparseArray<>(sparseArray.size());
        for (int i8 = 0; i8 < sparseArray.size(); i8++) {
            sparseArray2.put(sparseArray.keyAt(i8), aVar.a(sparseArray.valueAt(i8)));
        }
        return sparseArray2;
    }
}
