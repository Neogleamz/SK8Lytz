package x1;

import android.view.View;
import androidx.transition.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e extends d {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f23771a = {"android:visibilityPropagation:visibility", "android:visibilityPropagation:center"};

    private static int d(u uVar, int i8) {
        int[] iArr;
        if (uVar == null || (iArr = (int[]) uVar.f7619a.get("android:visibilityPropagation:center")) == null) {
            return -1;
        }
        return iArr[i8];
    }

    @Override // x1.d
    public void a(u uVar) {
        View view = uVar.f7620b;
        Integer num = (Integer) uVar.f7619a.get("android:visibility:visibility");
        if (num == null) {
            num = Integer.valueOf(view.getVisibility());
        }
        uVar.f7619a.put("android:visibilityPropagation:visibility", num);
        view.getLocationOnScreen(r2);
        int[] iArr = {iArr[0] + Math.round(view.getTranslationX())};
        iArr[0] = iArr[0] + (view.getWidth() / 2);
        iArr[1] = iArr[1] + Math.round(view.getTranslationY());
        iArr[1] = iArr[1] + (view.getHeight() / 2);
        uVar.f7619a.put("android:visibilityPropagation:center", iArr);
    }

    @Override // x1.d
    public String[] b() {
        return f23771a;
    }

    public int e(u uVar) {
        Integer num;
        if (uVar == null || (num = (Integer) uVar.f7619a.get("android:visibilityPropagation:visibility")) == null) {
            return 8;
        }
        return num.intValue();
    }

    public int f(u uVar) {
        return d(uVar, 0);
    }

    public int g(u uVar) {
        return d(uVar, 1);
    }
}
