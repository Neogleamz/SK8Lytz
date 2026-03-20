package androidx.transition;

import android.annotation.SuppressLint;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class i0 extends h0 {

    /* renamed from: j  reason: collision with root package name */
    private static boolean f7573j = true;

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void f(View view, int i8, int i9, int i10, int i11) {
        if (f7573j) {
            try {
                view.setLeftTopRightBottom(i8, i9, i10, i11);
            } catch (NoSuchMethodError unused) {
                f7573j = false;
            }
        }
    }
}
