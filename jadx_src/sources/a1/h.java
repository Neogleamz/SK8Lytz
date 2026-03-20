package a1;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h implements TransformationMethod {

    /* renamed from: a  reason: collision with root package name */
    private final TransformationMethod f66a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(TransformationMethod transformationMethod) {
        this.f66a = transformationMethod;
    }

    public TransformationMethod a() {
        return this.f66a;
    }

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence charSequence, View view) {
        if (view.isInEditMode()) {
            return charSequence;
        }
        TransformationMethod transformationMethod = this.f66a;
        if (transformationMethod != null) {
            charSequence = transformationMethod.getTransformation(charSequence, view);
        }
        return (charSequence == null || androidx.emoji2.text.e.b().d() != 1) ? charSequence : androidx.emoji2.text.e.b().o(charSequence);
    }

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view, CharSequence charSequence, boolean z4, int i8, Rect rect) {
        TransformationMethod transformationMethod = this.f66a;
        if (transformationMethod != null) {
            transformationMethod.onFocusChanged(view, charSequence, z4, i8, rect);
        }
    }
}
