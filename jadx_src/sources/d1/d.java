package d1;

import android.view.animation.Interpolator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class d implements Interpolator {

    /* renamed from: a  reason: collision with root package name */
    private final float[] f19696a;

    /* renamed from: b  reason: collision with root package name */
    private final float f19697b;

    /* JADX INFO: Access modifiers changed from: protected */
    public d(float[] fArr) {
        this.f19696a = fArr;
        this.f19697b = 1.0f / (fArr.length - 1);
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f5) {
        if (f5 >= 1.0f) {
            return 1.0f;
        }
        if (f5 <= 0.0f) {
            return 0.0f;
        }
        float[] fArr = this.f19696a;
        int min = Math.min((int) ((fArr.length - 1) * f5), fArr.length - 2);
        float f8 = this.f19697b;
        float f9 = (f5 - (min * f8)) / f8;
        float[] fArr2 = this.f19696a;
        return fArr2[min] + (f9 * (fArr2[min + 1] - fArr2[min]));
    }
}
