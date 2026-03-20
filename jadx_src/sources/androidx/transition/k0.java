package androidx.transition;

import android.graphics.Matrix;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class k0 extends j0 {
    @Override // androidx.transition.g0, androidx.transition.l0
    public float c(View view) {
        return view.getTransitionAlpha();
    }

    @Override // androidx.transition.h0, androidx.transition.l0
    public void e(View view, Matrix matrix) {
        view.setAnimationMatrix(matrix);
    }

    @Override // androidx.transition.i0, androidx.transition.l0
    public void f(View view, int i8, int i9, int i10, int i11) {
        view.setLeftTopRightBottom(i8, i9, i10, i11);
    }

    @Override // androidx.transition.g0, androidx.transition.l0
    public void g(View view, float f5) {
        view.setTransitionAlpha(f5);
    }

    @Override // androidx.transition.j0, androidx.transition.l0
    public void h(View view, int i8) {
        view.setTransitionVisibility(i8);
    }

    @Override // androidx.transition.h0, androidx.transition.l0
    public void i(View view, Matrix matrix) {
        view.transformMatrixToGlobal(matrix);
    }

    @Override // androidx.transition.h0, androidx.transition.l0
    public void j(View view, Matrix matrix) {
        view.transformMatrixToLocal(matrix);
    }
}
