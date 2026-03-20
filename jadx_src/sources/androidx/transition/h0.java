package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h0 extends g0 {

    /* renamed from: g  reason: collision with root package name */
    private static boolean f7570g = true;

    /* renamed from: h  reason: collision with root package name */
    private static boolean f7571h = true;

    /* renamed from: i  reason: collision with root package name */
    private static boolean f7572i = true;

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void e(View view, Matrix matrix) {
        if (f7570g) {
            try {
                view.setAnimationMatrix(matrix);
            } catch (NoSuchMethodError unused) {
                f7570g = false;
            }
        }
    }

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void i(View view, Matrix matrix) {
        if (f7571h) {
            try {
                view.transformMatrixToGlobal(matrix);
            } catch (NoSuchMethodError unused) {
                f7571h = false;
            }
        }
    }

    @Override // androidx.transition.l0
    @SuppressLint({"NewApi"})
    public void j(View view, Matrix matrix) {
        if (f7572i) {
            try {
                view.transformMatrixToLocal(matrix);
            } catch (NoSuchMethodError unused) {
                f7572i = false;
            }
        }
    }
}
