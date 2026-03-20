package a1;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.e;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g implements TextWatcher {

    /* renamed from: a  reason: collision with root package name */
    private final EditText f59a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f60b;

    /* renamed from: c  reason: collision with root package name */
    private e.AbstractC0051e f61c;

    /* renamed from: d  reason: collision with root package name */
    private int f62d = Integer.MAX_VALUE;

    /* renamed from: e  reason: collision with root package name */
    private int f63e = 0;

    /* renamed from: f  reason: collision with root package name */
    private boolean f64f = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends e.AbstractC0051e {

        /* renamed from: a  reason: collision with root package name */
        private final Reference<EditText> f65a;

        a(EditText editText) {
            this.f65a = new WeakReference(editText);
        }

        @Override // androidx.emoji2.text.e.AbstractC0051e
        public void b() {
            super.b();
            g.b(this.f65a.get(), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(EditText editText, boolean z4) {
        this.f59a = editText;
        this.f60b = z4;
    }

    private e.AbstractC0051e a() {
        if (this.f61c == null) {
            this.f61c = new a(this.f59a);
        }
        return this.f61c;
    }

    static void b(EditText editText, int i8) {
        if (i8 == 1 && editText != null && editText.isAttachedToWindow()) {
            Editable editableText = editText.getEditableText();
            int selectionStart = Selection.getSelectionStart(editableText);
            int selectionEnd = Selection.getSelectionEnd(editableText);
            androidx.emoji2.text.e.b().o(editableText);
            d.b(editableText, selectionStart, selectionEnd);
        }
    }

    private boolean d() {
        return (this.f64f && (this.f60b || androidx.emoji2.text.e.h())) ? false : true;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
    }

    public void c(boolean z4) {
        if (this.f64f != z4) {
            if (this.f61c != null) {
                androidx.emoji2.text.e.b().t(this.f61c);
            }
            this.f64f = z4;
            if (z4) {
                b(this.f59a, androidx.emoji2.text.e.b().d());
            }
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        if (this.f59a.isInEditMode() || d() || i9 > i10 || !(charSequence instanceof Spannable)) {
            return;
        }
        int d8 = androidx.emoji2.text.e.b().d();
        if (d8 != 0) {
            if (d8 == 1) {
                androidx.emoji2.text.e.b().r((Spannable) charSequence, i8, i8 + i10, this.f62d, this.f63e);
                return;
            } else if (d8 != 3) {
                return;
            }
        }
        androidx.emoji2.text.e.b().s(a());
    }
}
