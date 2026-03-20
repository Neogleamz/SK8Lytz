package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class i {

    /* renamed from: a  reason: collision with root package name */
    private final TextView f1502a;

    /* renamed from: b  reason: collision with root package name */
    private final a1.f f1503b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(TextView textView) {
        this.f1502a = textView;
        this.f1503b = new a1.f(textView, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f1503b.a(inputFilterArr);
    }

    public boolean b() {
        return this.f1503b.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(AttributeSet attributeSet, int i8) {
        TypedArray obtainStyledAttributes = this.f1502a.getContext().obtainStyledAttributes(attributeSet, g.j.f20048i0, i8, 0);
        try {
            int i9 = g.j.f20117w0;
            boolean z4 = obtainStyledAttributes.hasValue(i9) ? obtainStyledAttributes.getBoolean(i9, true) : true;
            obtainStyledAttributes.recycle();
            e(z4);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(boolean z4) {
        this.f1503b.c(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(boolean z4) {
        this.f1503b.d(z4);
    }

    public TransformationMethod f(TransformationMethod transformationMethod) {
        return this.f1503b.e(transformationMethod);
    }
}
