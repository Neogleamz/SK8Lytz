package com.google.android.material.textfield;

import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.internal.l;
import com.google.android.material.textfield.TextInputLayout;
import k7.j;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends e {

    /* renamed from: d  reason: collision with root package name */
    private final TextWatcher f18684d;

    /* renamed from: e  reason: collision with root package name */
    private final TextInputLayout.f f18685e;

    /* renamed from: f  reason: collision with root package name */
    private final TextInputLayout.g f18686f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends l {
        a() {
        }

        @Override // com.google.android.material.internal.l, android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
            h hVar = h.this;
            hVar.f18658c.setChecked(!hVar.g());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements TextInputLayout.f {
        b() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            EditText editText = textInputLayout.getEditText();
            textInputLayout.setEndIconVisible(true);
            textInputLayout.setEndIconCheckable(true);
            h hVar = h.this;
            hVar.f18658c.setChecked(!hVar.g());
            editText.removeTextChangedListener(h.this.f18684d);
            editText.addTextChangedListener(h.this.f18684d);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements TextInputLayout.g {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ EditText f18690a;

            a(EditText editText) {
                this.f18690a = editText;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f18690a.removeTextChangedListener(h.this.f18684d);
            }
        }

        c() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i8) {
            EditText editText = textInputLayout.getEditText();
            if (editText == null || i8 != 1) {
                return;
            }
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.post(new a(editText));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EditText editText = h.this.f18656a.getEditText();
            if (editText == null) {
                return;
            }
            int selectionEnd = editText.getSelectionEnd();
            editText.setTransformationMethod(h.this.g() ? null : PasswordTransformationMethod.getInstance());
            if (selectionEnd >= 0) {
                editText.setSelection(selectionEnd);
            }
            h.this.f18656a.V();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(TextInputLayout textInputLayout) {
        super(textInputLayout);
        this.f18684d = new a();
        this.f18685e = new b();
        this.f18686f = new c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        EditText editText = this.f18656a.getEditText();
        return editText != null && (editText.getTransformationMethod() instanceof PasswordTransformationMethod);
    }

    private static boolean h(EditText editText) {
        return editText != null && (editText.getInputType() == 16 || editText.getInputType() == 128 || editText.getInputType() == 144 || editText.getInputType() == 224);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public void a() {
        this.f18656a.setEndIconDrawable(h.a.b(this.f18657b, k7.e.f21141a));
        TextInputLayout textInputLayout = this.f18656a;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(j.J));
        this.f18656a.setEndIconOnClickListener(new d());
        this.f18656a.e(this.f18685e);
        this.f18656a.f(this.f18686f);
        EditText editText = this.f18656a.getEditText();
        if (h(editText)) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
