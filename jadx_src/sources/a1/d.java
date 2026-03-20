package a1;

import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import androidx.emoji2.text.e;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d implements InputFilter {

    /* renamed from: a  reason: collision with root package name */
    private final TextView f48a;

    /* renamed from: b  reason: collision with root package name */
    private e.AbstractC0051e f49b;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends e.AbstractC0051e {

        /* renamed from: a  reason: collision with root package name */
        private final Reference<TextView> f50a;

        /* renamed from: b  reason: collision with root package name */
        private final Reference<d> f51b;

        a(TextView textView, d dVar) {
            this.f50a = new WeakReference(textView);
            this.f51b = new WeakReference(dVar);
        }

        private boolean c(TextView textView, InputFilter inputFilter) {
            InputFilter[] filters;
            if (inputFilter == null || textView == null || (filters = textView.getFilters()) == null) {
                return false;
            }
            for (InputFilter inputFilter2 : filters) {
                if (inputFilter2 == inputFilter) {
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.emoji2.text.e.AbstractC0051e
        public void b() {
            CharSequence text;
            CharSequence o5;
            super.b();
            TextView textView = this.f50a.get();
            if (c(textView, this.f51b.get()) && textView.isAttachedToWindow() && text != (o5 = androidx.emoji2.text.e.b().o((text = textView.getText())))) {
                int selectionStart = Selection.getSelectionStart(o5);
                int selectionEnd = Selection.getSelectionEnd(o5);
                textView.setText(o5);
                if (o5 instanceof Spannable) {
                    d.b((Spannable) o5, selectionStart, selectionEnd);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(TextView textView) {
        this.f48a = textView;
    }

    private e.AbstractC0051e a() {
        if (this.f49b == null) {
            this.f49b = new a(this.f48a, this);
        }
        return this.f49b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Spannable spannable, int i8, int i9) {
        if (i8 >= 0 && i9 >= 0) {
            Selection.setSelection(spannable, i8, i9);
        } else if (i8 >= 0) {
            Selection.setSelection(spannable, i8);
        } else if (i9 >= 0) {
            Selection.setSelection(spannable, i9);
        }
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i8, int i9, Spanned spanned, int i10, int i11) {
        if (this.f48a.isInEditMode()) {
            return charSequence;
        }
        int d8 = androidx.emoji2.text.e.b().d();
        if (d8 != 0) {
            boolean z4 = true;
            if (d8 == 1) {
                if (i11 == 0 && i10 == 0 && spanned.length() == 0 && charSequence == this.f48a.getText()) {
                    z4 = false;
                }
                if (!z4 || charSequence == null) {
                    return charSequence;
                }
                if (i8 != 0 || i9 != charSequence.length()) {
                    charSequence = charSequence.subSequence(i8, i9);
                }
                return androidx.emoji2.text.e.b().p(charSequence, 0, charSequence.length());
            } else if (d8 != 3) {
                return charSequence;
            }
        }
        androidx.emoji2.text.e.b().s(a());
        return charSequence;
    }
}
