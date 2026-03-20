package y;

import androidx.camera.core.impl.SurfaceConfig;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y0 {

    /* renamed from: a  reason: collision with root package name */
    private final List<SurfaceConfig> f24318a = new ArrayList();

    private static void b(List<int[]> list, int i8, int[] iArr, int i9) {
        boolean z4;
        if (i9 >= iArr.length) {
            list.add((int[]) iArr.clone());
            return;
        }
        for (int i10 = 0; i10 < i8; i10++) {
            int i11 = 0;
            while (true) {
                if (i11 >= i9) {
                    z4 = false;
                    break;
                } else if (i10 == iArr[i11]) {
                    z4 = true;
                    break;
                } else {
                    i11++;
                }
            }
            if (!z4) {
                iArr[i9] = i10;
                b(list, i8, iArr, i9 + 1);
            }
        }
    }

    private List<int[]> c(int i8) {
        ArrayList arrayList = new ArrayList();
        b(arrayList, i8, new int[i8], 0);
        return arrayList;
    }

    public boolean a(SurfaceConfig surfaceConfig) {
        return this.f24318a.add(surfaceConfig);
    }

    public boolean d(List<SurfaceConfig> list) {
        if (list.isEmpty()) {
            return true;
        }
        if (list.size() > this.f24318a.size()) {
            return false;
        }
        for (int[] iArr : c(this.f24318a.size())) {
            boolean z4 = true;
            for (int i8 = 0; i8 < this.f24318a.size() && (iArr[i8] >= list.size() || ((z4 = z4 & this.f24318a.get(i8).e(list.get(iArr[i8]))))); i8++) {
            }
            if (z4) {
                return true;
            }
        }
        return false;
    }
}
