package androidx.transition;

import android.animation.TypeEvaluator;
import android.graphics.Rect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class o implements TypeEvaluator<Rect> {

    /* renamed from: a  reason: collision with root package name */
    private Rect f7592a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(Rect rect) {
        this.f7592a = rect;
    }

    @Override // android.animation.TypeEvaluator
    /* renamed from: a */
    public Rect evaluate(float f5, Rect rect, Rect rect2) {
        int i8 = rect.left;
        int i9 = i8 + ((int) ((rect2.left - i8) * f5));
        int i10 = rect.top;
        int i11 = i10 + ((int) ((rect2.top - i10) * f5));
        int i12 = rect.right;
        int i13 = i12 + ((int) ((rect2.right - i12) * f5));
        int i14 = rect.bottom;
        int i15 = i14 + ((int) ((rect2.bottom - i14) * f5));
        Rect rect3 = this.f7592a;
        if (rect3 == null) {
            return new Rect(i9, i11, i13, i15);
        }
        rect3.set(i9, i11, i13, i15);
        return this.f7592a;
    }
}
