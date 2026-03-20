package androidx.transition;

import android.annotation.SuppressLint;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class g0 extends l0 {

    /* renamed from: f  reason: collision with root package name */
    private static boolean f7562f = true;

    @Override // androidx.transition.l0
    public void a(View view) {
    }

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public float c(View view) {
        if (f7562f) {
            try {
                return view.getTransitionAlpha();
            } catch (NoSuchMethodError unused) {
                f7562f = false;
            }
        }
        return view.getAlpha();
    }

    @Override // androidx.transition.l0
    public void d(View view) {
    }

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void g(View view, float f5) {
        if (f7562f) {
            try {
                view.setTransitionAlpha(f5);
                return;
            } catch (NoSuchMethodError unused) {
                f7562f = false;
            }
        }
        view.setAlpha(f5);
    }
}
