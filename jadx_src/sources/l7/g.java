package l7;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements TypeEvaluator<Matrix> {

    /* renamed from: a  reason: collision with root package name */
    private final float[] f21796a = new float[9];

    /* renamed from: b  reason: collision with root package name */
    private final float[] f21797b = new float[9];

    /* renamed from: c  reason: collision with root package name */
    private final Matrix f21798c = new Matrix();

    public Matrix a(float f5, Matrix matrix, Matrix matrix2) {
        matrix.getValues(this.f21796a);
        matrix2.getValues(this.f21797b);
        for (int i8 = 0; i8 < 9; i8++) {
            float[] fArr = this.f21797b;
            float f8 = fArr[i8];
            float[] fArr2 = this.f21796a;
            fArr[i8] = fArr2[i8] + ((f8 - fArr2[i8]) * f5);
        }
        this.f21798c.setValues(this.f21797b);
        return this.f21798c;
    }
}
