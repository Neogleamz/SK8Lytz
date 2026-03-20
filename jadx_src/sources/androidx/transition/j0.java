package androidx.transition;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class j0 extends i0 {

    /* renamed from: k  reason: collision with root package name */
    private static boolean f7577k = true;

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void h(View view, int i8) {
        if (Build.VERSION.SDK_INT == 28) {
            super.h(view, i8);
        } else if (f7577k) {
            try {
                view.setTransitionVisibility(i8);
            } catch (NoSuchMethodError unused) {
                f7577k = false;
            }
        }
    }
}
