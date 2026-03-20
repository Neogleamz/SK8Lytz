package androidx.transition;

import android.animation.TypeEvaluator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class c implements TypeEvaluator<float[]> {

    /* renamed from: a  reason: collision with root package name */
    private float[] f7523a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(float[] fArr) {
        this.f7523a = fArr;
    }

    @Override // android.animation.TypeEvaluator
    /* renamed from: a */
    public float[] evaluate(float f5, float[] fArr, float[] fArr2) {
        float[] fArr3 = this.f7523a;
        if (fArr3 == null) {
            fArr3 = new float[fArr.length];
        }
        for (int i8 = 0; i8 < fArr3.length; i8++) {
            float f8 = fArr[i8];
            fArr3[i8] = f8 + ((fArr2[i8] - f8) * f5);
        }
        return fArr3;
    }
}
