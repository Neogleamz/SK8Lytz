package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h {

    /* renamed from: a  reason: collision with root package name */
    private final EditText f1496a;

    /* renamed from: b  reason: collision with root package name */
    private final a1.a f1497b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(EditText editText) {
        this.f1496a = editText;
        this.f1497b = new a1.a(editText, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyListener a(KeyListener keyListener) {
        return b(keyListener) ? this.f1497b.a(keyListener) : keyListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(KeyListener keyListener) {
        return !(keyListener instanceof NumberKeyListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(AttributeSet attributeSet, int i8) {
        TypedArray obtainStyledAttributes = this.f1496a.getContext().obtainStyledAttributes(attributeSet, g.j.f20048i0, i8, 0);
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
    public InputConnection d(InputConnection inputConnection, EditorInfo editorInfo) {
        return this.f1497b.b(inputConnection, editorInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(boolean z4) {
        this.f1497b.c(z4);
    }
}
