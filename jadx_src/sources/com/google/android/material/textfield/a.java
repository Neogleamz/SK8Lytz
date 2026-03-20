package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import k7.j;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends com.google.android.material.textfield.e {

    /* renamed from: d  reason: collision with root package name */
    private final TextWatcher f18611d;

    /* renamed from: e  reason: collision with root package name */
    private final View.OnFocusChangeListener f18612e;

    /* renamed from: f  reason: collision with root package name */
    private final TextInputLayout.f f18613f;

    /* renamed from: g  reason: collision with root package name */
    private final TextInputLayout.g f18614g;

    /* renamed from: h  reason: collision with root package name */
    private AnimatorSet f18615h;

    /* renamed from: i  reason: collision with root package name */
    private ValueAnimator f18616i;

    /* renamed from: com.google.android.material.textfield.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0141a implements TextWatcher {
        C0141a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (a.this.f18656a.getSuffixText() != null) {
                return;
            }
            a aVar = a.this;
            aVar.i(aVar.f18656a.hasFocus() && a.l(editable));
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements View.OnFocusChangeListener {
        b() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z4) {
            boolean z8 = true;
            a.this.i(((TextUtils.isEmpty(((EditText) view).getText()) ^ true) && z4) ? false : false);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements TextInputLayout.f {
        c() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            EditText editText = textInputLayout.getEditText();
            textInputLayout.setEndIconVisible(editText.hasFocus() && a.l(editText.getText()));
            textInputLayout.setEndIconCheckable(false);
            editText.setOnFocusChangeListener(a.this.f18612e);
            editText.removeTextChangedListener(a.this.f18611d);
            editText.addTextChangedListener(a.this.f18611d);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements TextInputLayout.g {

        /* renamed from: com.google.android.material.textfield.a$d$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0142a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ EditText f18621a;

            RunnableC0142a(EditText editText) {
                this.f18621a = editText;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f18621a.removeTextChangedListener(a.this.f18611d);
            }
        }

        d() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i8) {
            EditText editText = textInputLayout.getEditText();
            if (editText == null || i8 != 2) {
                return;
            }
            editText.post(new RunnableC0142a(editText));
            if (editText.getOnFocusChangeListener() == a.this.f18612e) {
                editText.setOnFocusChangeListener(null);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e implements View.OnClickListener {
        e() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Editable text = a.this.f18656a.getEditText().getText();
            if (text != null) {
                text.clear();
            }
            a.this.f18656a.V();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            a.this.f18656a.setEndIconVisible(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends AnimatorListenerAdapter {
        g() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            a.this.f18656a.setEndIconVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements ValueAnimator.AnimatorUpdateListener {
        h() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            a.this.f18658c.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {
        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            a.this.f18658c.setScaleX(floatValue);
            a.this.f18658c.setScaleY(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(TextInputLayout textInputLayout) {
        super(textInputLayout);
        this.f18611d = new C0141a();
        this.f18612e = new b();
        this.f18613f = new c();
        this.f18614g = new d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(boolean z4) {
        boolean z8 = this.f18656a.K() == z4;
        if (z4 && !this.f18615h.isRunning()) {
            this.f18616i.cancel();
            this.f18615h.start();
            if (z8) {
                this.f18615h.end();
            }
        } else if (z4) {
        } else {
            this.f18615h.cancel();
            this.f18616i.start();
            if (z8) {
                this.f18616i.end();
            }
        }
    }

    private ValueAnimator j(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(l7.a.f21786a);
        ofFloat.setDuration(100L);
        ofFloat.addUpdateListener(new h());
        return ofFloat;
    }

    private ValueAnimator k() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
        ofFloat.setInterpolator(l7.a.f21789d);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new i());
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean l(Editable editable) {
        return editable.length() > 0;
    }

    private void m() {
        ValueAnimator k8 = k();
        ValueAnimator j8 = j(0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        this.f18615h = animatorSet;
        animatorSet.playTogether(k8, j8);
        this.f18615h.addListener(new f());
        ValueAnimator j9 = j(1.0f, 0.0f);
        this.f18616i = j9;
        j9.addListener(new g());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public void a() {
        this.f18656a.setEndIconDrawable(h.a.b(this.f18657b, k7.e.f21146f));
        TextInputLayout textInputLayout = this.f18656a;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(j.f21209e));
        this.f18656a.setEndIconOnClickListener(new e());
        this.f18656a.e(this.f18613f);
        this.f18656a.f(this.f18614g);
        m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public void c(boolean z4) {
        if (this.f18656a.getSuffixText() == null) {
            return;
        }
        i(z4);
    }
}
