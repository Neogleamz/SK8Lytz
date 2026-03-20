package x1;

import android.graphics.Rect;
import android.view.ViewGroup;
import androidx.transition.Transition;
import androidx.transition.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends e {

    /* renamed from: b  reason: collision with root package name */
    private float f23759b = 3.0f;

    private static float h(float f5, float f8, float f9, float f10) {
        float f11 = f9 - f5;
        float f12 = f10 - f8;
        return (float) Math.sqrt((f11 * f11) + (f12 * f12));
    }

    @Override // x1.d
    public long c(ViewGroup viewGroup, Transition transition, u uVar, u uVar2) {
        int i8;
        int[] iArr;
        int round;
        int i9;
        if (uVar == null && uVar2 == null) {
            return 0L;
        }
        if (uVar2 == null || e(uVar) == 0) {
            i8 = -1;
        } else {
            uVar = uVar2;
            i8 = 1;
        }
        int f5 = f(uVar);
        int g8 = g(uVar);
        Rect y8 = transition.y();
        if (y8 != null) {
            i9 = y8.centerX();
            round = y8.centerY();
        } else {
            viewGroup.getLocationOnScreen(new int[2]);
            int round2 = Math.round(iArr[0] + (viewGroup.getWidth() / 2) + viewGroup.getTranslationX());
            round = Math.round(iArr[1] + (viewGroup.getHeight() / 2) + viewGroup.getTranslationY());
            i9 = round2;
        }
        float h8 = h(f5, g8, i9, round) / h(0.0f, 0.0f, viewGroup.getWidth(), viewGroup.getHeight());
        long x8 = transition.x();
        if (x8 < 0) {
            x8 = 300;
        }
        return Math.round((((float) (x8 * i8)) / this.f23759b) * h8);
    }
}
