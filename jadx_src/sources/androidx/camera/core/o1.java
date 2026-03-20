package androidx.camera.core;

import android.graphics.Matrix;
import androidx.camera.core.impl.utils.ExifData;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o1 implements i1 {
    public static i1 f(y.a1 a1Var, long j8, int i8, Matrix matrix) {
        return new h(a1Var, j8, i8, matrix);
    }

    @Override // androidx.camera.core.i1
    public abstract y.a1 a();

    @Override // androidx.camera.core.i1
    public abstract int b();

    @Override // androidx.camera.core.i1
    public void c(ExifData.b bVar) {
        bVar.m(b());
    }

    @Override // androidx.camera.core.i1
    public abstract long d();

    @Override // androidx.camera.core.i1
    public abstract Matrix e();
}
