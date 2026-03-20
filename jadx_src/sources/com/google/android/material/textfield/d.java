package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.core.view.c0;
import com.google.android.material.internal.l;
import com.google.android.material.textfield.TextInputLayout;
import x7.m;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends com.google.android.material.textfield.e {
    private static final boolean q;

    /* renamed from: d  reason: collision with root package name */
    private final TextWatcher f18628d;

    /* renamed from: e  reason: collision with root package name */
    private final View.OnFocusChangeListener f18629e;

    /* renamed from: f  reason: collision with root package name */
    private final TextInputLayout.e f18630f;

    /* renamed from: g  reason: collision with root package name */
    private final TextInputLayout.f f18631g;
    @SuppressLint({"ClickableViewAccessibility"})

    /* renamed from: h  reason: collision with root package name */
    private final TextInputLayout.g f18632h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f18633i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f18634j;

    /* renamed from: k  reason: collision with root package name */
    private long f18635k;

    /* renamed from: l  reason: collision with root package name */
    private StateListDrawable f18636l;

    /* renamed from: m  reason: collision with root package name */
    private x7.h f18637m;

    /* renamed from: n  reason: collision with root package name */
    private AccessibilityManager f18638n;

    /* renamed from: o  reason: collision with root package name */
    private ValueAnimator f18639o;

    /* renamed from: p  reason: collision with root package name */
    private ValueAnimator f18640p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends l {

        /* renamed from: com.google.android.material.textfield.d$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0143a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ AutoCompleteTextView f18642a;

            RunnableC0143a(AutoCompleteTextView autoCompleteTextView) {
                this.f18642a = autoCompleteTextView;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean isPopupShowing = this.f18642a.isPopupShowing();
                d.this.E(isPopupShowing);
                d.this.f18633i = isPopupShowing;
            }
        }

        a() {
        }

        @Override // com.google.android.material.internal.l, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            AutoCompleteTextView y8 = d.y(d.this.f18656a.getEditText());
            if (d.this.f18638n.isTouchExplorationEnabled() && d.D(y8) && !d.this.f18658c.hasFocus()) {
                y8.dismissDropDown();
            }
            y8.post(new RunnableC0143a(y8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            d.this.f18658c.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements View.OnFocusChangeListener {
        c() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z4) {
            d.this.f18656a.setEndIconActivated(z4);
            if (z4) {
                return;
            }
            d.this.E(false);
            d.this.f18633i = false;
        }
    }

    /* renamed from: com.google.android.material.textfield.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0144d extends TextInputLayout.e {
        C0144d(TextInputLayout textInputLayout) {
            super(textInputLayout);
        }

        @Override // com.google.android.material.textfield.TextInputLayout.e, androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            if (!d.D(d.this.f18656a.getEditText())) {
                cVar.c0(Spinner.class.getName());
            }
            if (cVar.M()) {
                cVar.n0(null);
            }
        }

        @Override // androidx.core.view.a
        public void h(View view, AccessibilityEvent accessibilityEvent) {
            super.h(view, accessibilityEvent);
            AutoCompleteTextView y8 = d.y(d.this.f18656a.getEditText());
            if (accessibilityEvent.getEventType() == 1 && d.this.f18638n.isTouchExplorationEnabled() && !d.D(d.this.f18656a.getEditText())) {
                d.this.H(y8);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e implements TextInputLayout.f {
        e() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            AutoCompleteTextView y8 = d.y(textInputLayout.getEditText());
            d.this.F(y8);
            d.this.v(y8);
            d.this.G(y8);
            y8.setThreshold(0);
            y8.removeTextChangedListener(d.this.f18628d);
            y8.addTextChangedListener(d.this.f18628d);
            textInputLayout.setEndIconCheckable(true);
            textInputLayout.setErrorIconDrawable((Drawable) null);
            if (!d.D(y8)) {
                c0.E0(d.this.f18658c, 2);
            }
            textInputLayout.setTextInputAccessibilityDelegate(d.this.f18630f);
            textInputLayout.setEndIconVisible(true);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f implements TextInputLayout.g {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ AutoCompleteTextView f18649a;

            a(AutoCompleteTextView autoCompleteTextView) {
                this.f18649a = autoCompleteTextView;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f18649a.removeTextChangedListener(d.this.f18628d);
            }
        }

        f() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i8) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
            if (autoCompleteTextView == null || i8 != 3) {
                return;
            }
            autoCompleteTextView.post(new a(autoCompleteTextView));
            if (autoCompleteTextView.getOnFocusChangeListener() == d.this.f18629e) {
                autoCompleteTextView.setOnFocusChangeListener(null);
            }
            autoCompleteTextView.setOnTouchListener(null);
            if (d.q) {
                autoCompleteTextView.setOnDismissListener(null);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class g implements View.OnClickListener {
        g() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            d.this.H((AutoCompleteTextView) d.this.f18656a.getEditText());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements View.OnTouchListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ AutoCompleteTextView f18652a;

        h(AutoCompleteTextView autoCompleteTextView) {
            this.f18652a = autoCompleteTextView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                if (d.this.C()) {
                    d.this.f18633i = false;
                }
                d.this.H(this.f18652a);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements AutoCompleteTextView.OnDismissListener {
        i() {
        }

        @Override // android.widget.AutoCompleteTextView.OnDismissListener
        public void onDismiss() {
            d.this.f18633i = true;
            d.this.f18635k = System.currentTimeMillis();
            d.this.E(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j extends AnimatorListenerAdapter {
        j() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d dVar = d.this;
            dVar.f18658c.setChecked(dVar.f18634j);
            d.this.f18640p.start();
        }
    }

    static {
        q = Build.VERSION.SDK_INT >= 21;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(TextInputLayout textInputLayout) {
        super(textInputLayout);
        this.f18628d = new a();
        this.f18629e = new c();
        this.f18630f = new C0144d(this.f18656a);
        this.f18631g = new e();
        this.f18632h = new f();
        this.f18633i = false;
        this.f18634j = false;
        this.f18635k = Long.MAX_VALUE;
    }

    private x7.h A(float f5, float f8, float f9, int i8) {
        m m8 = m.a().E(f5).I(f5).v(f8).z(f8).m();
        x7.h m9 = x7.h.m(this.f18657b, f9);
        m9.setShapeAppearanceModel(m8);
        m9.c0(0, i8, 0, i8);
        return m9;
    }

    private void B() {
        this.f18640p = z(67, 0.0f, 1.0f);
        ValueAnimator z4 = z(50, 1.0f, 0.0f);
        this.f18639o = z4;
        z4.addListener(new j());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean C() {
        long currentTimeMillis = System.currentTimeMillis() - this.f18635k;
        return currentTimeMillis < 0 || currentTimeMillis > 300;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean D(EditText editText) {
        return editText.getKeyListener() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E(boolean z4) {
        if (this.f18634j != z4) {
            this.f18634j = z4;
            this.f18640p.cancel();
            this.f18639o.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F(AutoCompleteTextView autoCompleteTextView) {
        Drawable drawable;
        if (q) {
            int boxBackgroundMode = this.f18656a.getBoxBackgroundMode();
            if (boxBackgroundMode == 2) {
                drawable = this.f18637m;
            } else if (boxBackgroundMode != 1) {
                return;
            } else {
                drawable = this.f18636l;
            }
            autoCompleteTextView.setDropDownBackgroundDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"ClickableViewAccessibility"})
    public void G(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnTouchListener(new h(autoCompleteTextView));
        autoCompleteTextView.setOnFocusChangeListener(this.f18629e);
        if (q) {
            autoCompleteTextView.setOnDismissListener(new i());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void H(AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView == null) {
            return;
        }
        if (C()) {
            this.f18633i = false;
        }
        if (this.f18633i) {
            this.f18633i = false;
            return;
        }
        if (q) {
            E(!this.f18634j);
        } else {
            this.f18634j = !this.f18634j;
            this.f18658c.toggle();
        }
        if (!this.f18634j) {
            autoCompleteTextView.dismissDropDown();
            return;
        }
        autoCompleteTextView.requestFocus();
        autoCompleteTextView.showDropDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(AutoCompleteTextView autoCompleteTextView) {
        if (D(autoCompleteTextView)) {
            return;
        }
        int boxBackgroundMode = this.f18656a.getBoxBackgroundMode();
        x7.h boxBackground = this.f18656a.getBoxBackground();
        int d8 = n7.a.d(autoCompleteTextView, k7.b.f21062n);
        int[][] iArr = {new int[]{16842919}, new int[0]};
        if (boxBackgroundMode == 2) {
            x(autoCompleteTextView, d8, iArr, boxBackground);
        } else if (boxBackgroundMode == 1) {
            w(autoCompleteTextView, d8, iArr, boxBackground);
        }
    }

    private void w(AutoCompleteTextView autoCompleteTextView, int i8, int[][] iArr, x7.h hVar) {
        int boxBackgroundColor = this.f18656a.getBoxBackgroundColor();
        int[] iArr2 = {n7.a.h(i8, boxBackgroundColor, 0.1f), boxBackgroundColor};
        if (q) {
            c0.x0(autoCompleteTextView, new RippleDrawable(new ColorStateList(iArr, iArr2), hVar, hVar));
            return;
        }
        x7.h hVar2 = new x7.h(hVar.D());
        hVar2.a0(new ColorStateList(iArr, iArr2));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{hVar, hVar2});
        int J = c0.J(autoCompleteTextView);
        int paddingTop = autoCompleteTextView.getPaddingTop();
        int I = c0.I(autoCompleteTextView);
        int paddingBottom = autoCompleteTextView.getPaddingBottom();
        c0.x0(autoCompleteTextView, layerDrawable);
        c0.J0(autoCompleteTextView, J, paddingTop, I, paddingBottom);
    }

    private void x(AutoCompleteTextView autoCompleteTextView, int i8, int[][] iArr, x7.h hVar) {
        LayerDrawable layerDrawable;
        int d8 = n7.a.d(autoCompleteTextView, k7.b.f21066s);
        x7.h hVar2 = new x7.h(hVar.D());
        int h8 = n7.a.h(i8, d8, 0.1f);
        hVar2.a0(new ColorStateList(iArr, new int[]{h8, 0}));
        if (q) {
            hVar2.setTint(d8);
            ColorStateList colorStateList = new ColorStateList(iArr, new int[]{h8, d8});
            x7.h hVar3 = new x7.h(hVar.D());
            hVar3.setTint(-1);
            layerDrawable = new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, hVar2, hVar3), hVar});
        } else {
            layerDrawable = new LayerDrawable(new Drawable[]{hVar2, hVar});
        }
        c0.x0(autoCompleteTextView, layerDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AutoCompleteTextView y(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            return (AutoCompleteTextView) editText;
        }
        throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
    }

    private ValueAnimator z(int i8, float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(l7.a.f21786a);
        ofFloat.setDuration(i8);
        ofFloat.addUpdateListener(new b());
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public void a() {
        float dimensionPixelOffset = this.f18657b.getResources().getDimensionPixelOffset(k7.d.f21117n0);
        float dimensionPixelOffset2 = this.f18657b.getResources().getDimensionPixelOffset(k7.d.f21095c0);
        int dimensionPixelOffset3 = this.f18657b.getResources().getDimensionPixelOffset(k7.d.f21097d0);
        x7.h A = A(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        x7.h A2 = A(0.0f, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        this.f18637m = A;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.f18636l = stateListDrawable;
        stateListDrawable.addState(new int[]{16842922}, A);
        this.f18636l.addState(new int[0], A2);
        this.f18656a.setEndIconDrawable(h.a.b(this.f18657b, q ? k7.e.f21144d : k7.e.f21145e));
        TextInputLayout textInputLayout = this.f18656a;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(k7.j.f21211g));
        this.f18656a.setEndIconOnClickListener(new g());
        this.f18656a.e(this.f18631g);
        this.f18656a.f(this.f18632h);
        B();
        this.f18638n = (AccessibilityManager) this.f18657b.getSystemService("accessibility");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public boolean b(int i8) {
        return i8 != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.e
    public boolean d() {
        return true;
    }
}
