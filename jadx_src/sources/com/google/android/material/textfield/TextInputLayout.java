package com.google.android.material.textfield;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.t;
import androidx.core.view.c0;
import androidx.core.view.i;
import androidx.customview.view.AbsSavedState;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.android.material.internal.CheckableImageButton;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import k7.j;
import k7.k;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TextInputLayout extends LinearLayout {

    /* renamed from: a1  reason: collision with root package name */
    private static final int f18555a1 = k.q;
    private ColorStateList A;
    private boolean A0;
    private ColorStateList B;
    private Drawable B0;
    private CharSequence C;
    private int C0;
    private Drawable D0;
    private final TextView E;
    private View.OnLongClickListener E0;
    private CharSequence F;
    private View.OnLongClickListener F0;
    private final TextView G;
    private final CheckableImageButton G0;
    private boolean H;
    private ColorStateList H0;
    private ColorStateList I0;
    private ColorStateList J0;
    private CharSequence K;
    private int K0;
    private boolean L;
    private int L0;
    private int M0;
    private ColorStateList N0;
    private x7.h O;
    private int O0;
    private x7.h P;
    private int P0;
    private m Q;
    private int Q0;
    private final int R;
    private int R0;
    private int S0;
    private int T;
    private boolean T0;
    final com.google.android.material.internal.a U0;
    private boolean V0;
    private int W;
    private boolean W0;
    private ValueAnimator X0;
    private boolean Y0;
    private boolean Z0;

    /* renamed from: a  reason: collision with root package name */
    private final FrameLayout f18556a;

    /* renamed from: a0  reason: collision with root package name */
    private int f18557a0;

    /* renamed from: b  reason: collision with root package name */
    private final LinearLayout f18558b;

    /* renamed from: b0  reason: collision with root package name */
    private int f18559b0;

    /* renamed from: c  reason: collision with root package name */
    private final LinearLayout f18560c;

    /* renamed from: c0  reason: collision with root package name */
    private int f18561c0;

    /* renamed from: d  reason: collision with root package name */
    private final FrameLayout f18562d;

    /* renamed from: d0  reason: collision with root package name */
    private int f18563d0;

    /* renamed from: e  reason: collision with root package name */
    EditText f18564e;

    /* renamed from: e0  reason: collision with root package name */
    private int f18565e0;

    /* renamed from: f  reason: collision with root package name */
    private CharSequence f18566f;

    /* renamed from: f0  reason: collision with root package name */
    private int f18567f0;

    /* renamed from: g  reason: collision with root package name */
    private int f18568g;

    /* renamed from: g0  reason: collision with root package name */
    private final Rect f18569g0;

    /* renamed from: h  reason: collision with root package name */
    private int f18570h;

    /* renamed from: h0  reason: collision with root package name */
    private final Rect f18571h0;

    /* renamed from: i0  reason: collision with root package name */
    private final RectF f18572i0;

    /* renamed from: j  reason: collision with root package name */
    private final com.google.android.material.textfield.f f18573j;

    /* renamed from: j0  reason: collision with root package name */
    private Typeface f18574j0;

    /* renamed from: k  reason: collision with root package name */
    boolean f18575k;

    /* renamed from: k0  reason: collision with root package name */
    private final CheckableImageButton f18576k0;

    /* renamed from: l  reason: collision with root package name */
    private int f18577l;

    /* renamed from: l0  reason: collision with root package name */
    private ColorStateList f18578l0;

    /* renamed from: m  reason: collision with root package name */
    private boolean f18579m;

    /* renamed from: m0  reason: collision with root package name */
    private boolean f18580m0;

    /* renamed from: n  reason: collision with root package name */
    private TextView f18581n;

    /* renamed from: n0  reason: collision with root package name */
    private PorterDuff.Mode f18582n0;

    /* renamed from: o0  reason: collision with root package name */
    private boolean f18583o0;

    /* renamed from: p  reason: collision with root package name */
    private int f18584p;

    /* renamed from: p0  reason: collision with root package name */
    private Drawable f18585p0;
    private int q;

    /* renamed from: q0  reason: collision with root package name */
    private int f18586q0;

    /* renamed from: r0  reason: collision with root package name */
    private View.OnLongClickListener f18587r0;

    /* renamed from: s0  reason: collision with root package name */
    private final LinkedHashSet<f> f18588s0;

    /* renamed from: t  reason: collision with root package name */
    private CharSequence f18589t;

    /* renamed from: t0  reason: collision with root package name */
    private int f18590t0;

    /* renamed from: u0  reason: collision with root package name */
    private final SparseArray<com.google.android.material.textfield.e> f18591u0;

    /* renamed from: v0  reason: collision with root package name */
    private final CheckableImageButton f18592v0;

    /* renamed from: w  reason: collision with root package name */
    private boolean f18593w;

    /* renamed from: w0  reason: collision with root package name */
    private final LinkedHashSet<g> f18594w0;

    /* renamed from: x  reason: collision with root package name */
    private TextView f18595x;

    /* renamed from: x0  reason: collision with root package name */
    private ColorStateList f18596x0;

    /* renamed from: y  reason: collision with root package name */
    private ColorStateList f18597y;

    /* renamed from: y0  reason: collision with root package name */
    private boolean f18598y0;

    /* renamed from: z  reason: collision with root package name */
    private int f18599z;

    /* renamed from: z0  reason: collision with root package name */
    private PorterDuff.Mode f18600z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        CharSequence f18601c;

        /* renamed from: d  reason: collision with root package name */
        boolean f18602d;

        /* renamed from: e  reason: collision with root package name */
        CharSequence f18603e;

        /* renamed from: f  reason: collision with root package name */
        CharSequence f18604f;

        /* renamed from: g  reason: collision with root package name */
        CharSequence f18605g;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f18601c = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f18602d = parcel.readInt() == 1;
            this.f18603e = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f18604f = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f18605g = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "TextInputLayout.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " error=" + ((Object) this.f18601c) + " hint=" + ((Object) this.f18603e) + " helperText=" + ((Object) this.f18604f) + " placeholderText=" + ((Object) this.f18605g) + "}";
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            TextUtils.writeToParcel(this.f18601c, parcel, i8);
            parcel.writeInt(this.f18602d ? 1 : 0);
            TextUtils.writeToParcel(this.f18603e, parcel, i8);
            TextUtils.writeToParcel(this.f18604f, parcel, i8);
            TextUtils.writeToParcel(this.f18605g, parcel, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            TextInputLayout textInputLayout = TextInputLayout.this;
            textInputLayout.v0(!textInputLayout.Z0);
            TextInputLayout textInputLayout2 = TextInputLayout.this;
            if (textInputLayout2.f18575k) {
                textInputLayout2.n0(editable.length());
            }
            if (TextInputLayout.this.f18593w) {
                TextInputLayout.this.z0(editable.length());
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout.this.f18592v0.performClick();
            TextInputLayout.this.f18592v0.jumpDrawablesToCurrentState();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout.this.f18564e.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            TextInputLayout.this.U0.p0(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends androidx.core.view.a {

        /* renamed from: d  reason: collision with root package name */
        private final TextInputLayout f18610d;

        public e(TextInputLayout textInputLayout) {
            this.f18610d = textInputLayout;
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x0083, code lost:
            if (r3 != null) goto L46;
         */
        @Override // androidx.core.view.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void g(android.view.View r14, androidx.core.view.accessibility.c r15) {
            /*
                r13 = this;
                super.g(r14, r15)
                com.google.android.material.textfield.TextInputLayout r14 = r13.f18610d
                android.widget.EditText r14 = r14.getEditText()
                if (r14 == 0) goto L10
                android.text.Editable r0 = r14.getText()
                goto L11
            L10:
                r0 = 0
            L11:
                com.google.android.material.textfield.TextInputLayout r1 = r13.f18610d
                java.lang.CharSequence r1 = r1.getHint()
                com.google.android.material.textfield.TextInputLayout r2 = r13.f18610d
                java.lang.CharSequence r2 = r2.getError()
                com.google.android.material.textfield.TextInputLayout r3 = r13.f18610d
                java.lang.CharSequence r3 = r3.getPlaceholderText()
                com.google.android.material.textfield.TextInputLayout r4 = r13.f18610d
                int r4 = r4.getCounterMaxLength()
                com.google.android.material.textfield.TextInputLayout r5 = r13.f18610d
                java.lang.CharSequence r5 = r5.getCounterOverflowDescription()
                boolean r6 = android.text.TextUtils.isEmpty(r0)
                r7 = 1
                r6 = r6 ^ r7
                boolean r8 = android.text.TextUtils.isEmpty(r1)
                r8 = r8 ^ r7
                com.google.android.material.textfield.TextInputLayout r9 = r13.f18610d
                boolean r9 = r9.N()
                r9 = r9 ^ r7
                boolean r10 = android.text.TextUtils.isEmpty(r2)
                r10 = r10 ^ r7
                if (r10 != 0) goto L51
                boolean r11 = android.text.TextUtils.isEmpty(r5)
                if (r11 != 0) goto L4f
                goto L51
            L4f:
                r11 = 0
                goto L52
            L51:
                r11 = r7
            L52:
                if (r8 == 0) goto L59
                java.lang.String r1 = r1.toString()
                goto L5b
            L59:
                java.lang.String r1 = ""
            L5b:
                java.lang.String r8 = ", "
                if (r6 == 0) goto L63
                r15.E0(r0)
                goto L88
            L63:
                boolean r12 = android.text.TextUtils.isEmpty(r1)
                if (r12 != 0) goto L83
                r15.E0(r1)
                if (r9 == 0) goto L88
                if (r3 == 0) goto L88
                java.lang.StringBuilder r9 = new java.lang.StringBuilder
                r9.<init>()
                r9.append(r1)
                r9.append(r8)
                r9.append(r3)
                java.lang.String r3 = r9.toString()
                goto L85
            L83:
                if (r3 == 0) goto L88
            L85:
                r15.E0(r3)
            L88:
                boolean r3 = android.text.TextUtils.isEmpty(r1)
                if (r3 != 0) goto Lb4
                int r3 = android.os.Build.VERSION.SDK_INT
                r9 = 26
                if (r3 < r9) goto L98
                r15.n0(r1)
                goto Laf
            L98:
                if (r6 == 0) goto Lac
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                r3.append(r0)
                r3.append(r8)
                r3.append(r1)
                java.lang.String r1 = r3.toString()
            Lac:
                r15.E0(r1)
            Laf:
                r1 = r6 ^ 1
                r15.A0(r1)
            Lb4:
                if (r0 == 0) goto Lbd
                int r0 = r0.length()
                if (r0 != r4) goto Lbd
                goto Lbe
            Lbd:
                r4 = -1
            Lbe:
                r15.p0(r4)
                if (r11 == 0) goto Lca
                if (r10 == 0) goto Lc6
                goto Lc7
            Lc6:
                r2 = r5
            Lc7:
                r15.j0(r2)
            Lca:
                int r15 = android.os.Build.VERSION.SDK_INT
                r0 = 17
                if (r15 < r0) goto Ld7
                if (r14 == 0) goto Ld7
                int r15 = k7.f.X
                r14.setLabelFor(r15)
            Ld7:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.TextInputLayout.e.g(android.view.View, androidx.core.view.accessibility.c):void");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a(TextInputLayout textInputLayout);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void a(TextInputLayout textInputLayout, int i8);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.Z);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v150 */
    /* JADX WARN: Type inference failed for: r2v46 */
    /* JADX WARN: Type inference failed for: r2v47, types: [int, boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TextInputLayout(android.content.Context r27, android.util.AttributeSet r28, int r29) {
        /*
            Method dump skipped, instructions count: 1566
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.TextInputLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private boolean A() {
        return this.H && !TextUtils.isEmpty(this.K) && (this.O instanceof com.google.android.material.textfield.c);
    }

    private void A0() {
        if (this.f18564e == null) {
            return;
        }
        c0.J0(this.E, Q() ? 0 : c0.J(this.f18564e), this.f18564e.getCompoundPaddingTop(), getContext().getResources().getDimensionPixelSize(k7.d.F), this.f18564e.getCompoundPaddingBottom());
    }

    private void B() {
        Iterator<f> it = this.f18588s0.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    private void B0() {
        this.E.setVisibility((this.C == null || N()) ? 8 : 0);
        r0();
    }

    private void C(int i8) {
        Iterator<g> it = this.f18594w0.iterator();
        while (it.hasNext()) {
            it.next().a(this, i8);
        }
    }

    private void C0(boolean z4, boolean z8) {
        int defaultColor = this.N0.getDefaultColor();
        int colorForState = this.N0.getColorForState(new int[]{16843623, 16842910}, defaultColor);
        int colorForState2 = this.N0.getColorForState(new int[]{16843518, 16842910}, defaultColor);
        if (z4) {
            this.f18565e0 = colorForState2;
        } else if (z8) {
            this.f18565e0 = colorForState;
        } else {
            this.f18565e0 = defaultColor;
        }
    }

    private void D(Canvas canvas) {
        x7.h hVar = this.P;
        if (hVar != null) {
            Rect bounds = hVar.getBounds();
            bounds.top = bounds.bottom - this.f18559b0;
            this.P.draw(canvas);
        }
    }

    private void D0() {
        if (this.f18564e == null) {
            return;
        }
        c0.J0(this.G, getContext().getResources().getDimensionPixelSize(k7.d.F), this.f18564e.getPaddingTop(), (K() || L()) ? 0 : c0.I(this.f18564e), this.f18564e.getPaddingBottom());
    }

    private void E(Canvas canvas) {
        if (this.H) {
            this.U0.m(canvas);
        }
    }

    private void E0() {
        int visibility = this.G.getVisibility();
        boolean z4 = (this.F == null || N()) ? false : true;
        this.G.setVisibility(z4 ? 0 : 8);
        if (visibility != this.G.getVisibility()) {
            getEndIconDelegate().c(z4);
        }
        r0();
    }

    private void F(boolean z4) {
        ValueAnimator valueAnimator = this.X0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.X0.cancel();
        }
        if (z4 && this.W0) {
            i(0.0f);
        } else {
            this.U0.p0(0.0f);
        }
        if (A() && ((com.google.android.material.textfield.c) this.O).q0()) {
            y();
        }
        this.T0 = true;
        J();
        B0();
        E0();
    }

    private int G(int i8, boolean z4) {
        int compoundPaddingLeft = i8 + this.f18564e.getCompoundPaddingLeft();
        return (this.C == null || z4) ? compoundPaddingLeft : (compoundPaddingLeft - this.E.getMeasuredWidth()) + this.E.getPaddingLeft();
    }

    private int H(int i8, boolean z4) {
        int compoundPaddingRight = i8 - this.f18564e.getCompoundPaddingRight();
        return (this.C == null || !z4) ? compoundPaddingRight : compoundPaddingRight + (this.E.getMeasuredWidth() - this.E.getPaddingRight());
    }

    private boolean I() {
        return this.f18590t0 != 0;
    }

    private void J() {
        TextView textView = this.f18595x;
        if (textView == null || !this.f18593w) {
            return;
        }
        textView.setText((CharSequence) null);
        this.f18595x.setVisibility(4);
    }

    private boolean L() {
        return this.G0.getVisibility() == 0;
    }

    private boolean P() {
        return this.W == 1 && (Build.VERSION.SDK_INT < 16 || this.f18564e.getMinLines() <= 1);
    }

    private int[] R(CheckableImageButton checkableImageButton) {
        int[] drawableState = getDrawableState();
        int[] drawableState2 = checkableImageButton.getDrawableState();
        int length = drawableState.length;
        int[] copyOf = Arrays.copyOf(drawableState, drawableState.length + drawableState2.length);
        System.arraycopy(drawableState2, 0, copyOf, length, drawableState2.length);
        return copyOf;
    }

    private void S() {
        p();
        a0();
        F0();
        k0();
        h();
        if (this.W != 0) {
            u0();
        }
    }

    private void T() {
        if (A()) {
            RectF rectF = this.f18572i0;
            this.U0.p(rectF, this.f18564e.getWidth(), this.f18564e.getGravity());
            l(rectF);
            int i8 = this.f18559b0;
            this.T = i8;
            rectF.top = 0.0f;
            rectF.bottom = i8;
            rectF.offset(-getPaddingLeft(), 0.0f);
            ((com.google.android.material.textfield.c) this.O).w0(rectF);
        }
    }

    private static void U(ViewGroup viewGroup, boolean z4) {
        int childCount = viewGroup.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = viewGroup.getChildAt(i8);
            childAt.setEnabled(z4);
            if (childAt instanceof ViewGroup) {
                U((ViewGroup) childAt, z4);
            }
        }
    }

    private void X(CheckableImageButton checkableImageButton, ColorStateList colorStateList) {
        Drawable drawable = checkableImageButton.getDrawable();
        if (checkableImageButton.getDrawable() == null || colorStateList == null || !colorStateList.isStateful()) {
            return;
        }
        int colorForState = colorStateList.getColorForState(R(checkableImageButton), colorStateList.getDefaultColor());
        Drawable mutate = androidx.core.graphics.drawable.a.r(drawable).mutate();
        androidx.core.graphics.drawable.a.o(mutate, ColorStateList.valueOf(colorForState));
        checkableImageButton.setImageDrawable(mutate);
    }

    private void Z() {
        TextView textView = this.f18595x;
        if (textView != null) {
            textView.setVisibility(8);
        }
    }

    private void a0() {
        if (h0()) {
            c0.x0(this.f18564e, this.O);
        }
    }

    private static void b0(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        boolean R = c0.R(checkableImageButton);
        boolean z4 = false;
        boolean z8 = onLongClickListener != null;
        if (R || z8) {
            z4 = true;
        }
        checkableImageButton.setFocusable(z4);
        checkableImageButton.setClickable(R);
        checkableImageButton.setPressable(R);
        checkableImageButton.setLongClickable(z8);
        c0.E0(checkableImageButton, z4 ? 1 : 2);
    }

    private static void c0(CheckableImageButton checkableImageButton, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnClickListener(onClickListener);
        b0(checkableImageButton, onLongClickListener);
    }

    private static void d0(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        b0(checkableImageButton, onLongClickListener);
    }

    private boolean f0() {
        return (this.G0.getVisibility() == 0 || ((I() && K()) || this.F != null)) && this.f18560c.getMeasuredWidth() > 0;
    }

    private void g() {
        TextView textView = this.f18595x;
        if (textView != null) {
            this.f18556a.addView(textView);
            this.f18595x.setVisibility(0);
        }
    }

    private boolean g0() {
        return !(getStartIconDrawable() == null && this.C == null) && this.f18558b.getMeasuredWidth() > 0;
    }

    private com.google.android.material.textfield.e getEndIconDelegate() {
        com.google.android.material.textfield.e eVar = this.f18591u0.get(this.f18590t0);
        return eVar != null ? eVar : this.f18591u0.get(0);
    }

    private CheckableImageButton getEndIconToUpdateDummyDrawable() {
        if (this.G0.getVisibility() == 0) {
            return this.G0;
        }
        if (I() && K()) {
            return this.f18592v0;
        }
        return null;
    }

    private void h() {
        EditText editText;
        int J;
        int dimensionPixelSize;
        int I;
        Resources resources;
        int i8;
        if (this.f18564e == null || this.W != 1) {
            return;
        }
        if (u7.c.h(getContext())) {
            editText = this.f18564e;
            J = c0.J(editText);
            dimensionPixelSize = getResources().getDimensionPixelSize(k7.d.f21139z);
            I = c0.I(this.f18564e);
            resources = getResources();
            i8 = k7.d.f21137y;
        } else if (!u7.c.g(getContext())) {
            return;
        } else {
            editText = this.f18564e;
            J = c0.J(editText);
            dimensionPixelSize = getResources().getDimensionPixelSize(k7.d.f21135x);
            I = c0.I(this.f18564e);
            resources = getResources();
            i8 = k7.d.f21133w;
        }
        c0.J0(editText, J, dimensionPixelSize, I, resources.getDimensionPixelSize(i8));
    }

    private boolean h0() {
        EditText editText = this.f18564e;
        return (editText == null || this.O == null || editText.getBackground() != null || this.W == 0) ? false : true;
    }

    private void i0() {
        TextView textView = this.f18595x;
        if (textView == null || !this.f18593w) {
            return;
        }
        textView.setText(this.f18589t);
        this.f18595x.setVisibility(0);
        this.f18595x.bringToFront();
    }

    private void j() {
        x7.h hVar = this.O;
        if (hVar == null) {
            return;
        }
        hVar.setShapeAppearanceModel(this.Q);
        if (w()) {
            this.O.j0(this.f18559b0, this.f18565e0);
        }
        int q = q();
        this.f18567f0 = q;
        this.O.a0(ColorStateList.valueOf(q));
        if (this.f18590t0 == 3) {
            this.f18564e.getBackground().invalidateSelf();
        }
        k();
        invalidate();
    }

    private void j0(boolean z4) {
        if (!z4 || getEndIconDrawable() == null) {
            m();
            return;
        }
        Drawable mutate = androidx.core.graphics.drawable.a.r(getEndIconDrawable()).mutate();
        androidx.core.graphics.drawable.a.n(mutate, this.f18573j.o());
        this.f18592v0.setImageDrawable(mutate);
    }

    private void k() {
        if (this.P == null) {
            return;
        }
        if (x()) {
            this.P.a0(ColorStateList.valueOf(this.f18565e0));
        }
        invalidate();
    }

    private void k0() {
        Resources resources;
        int i8;
        if (this.W == 1) {
            if (u7.c.h(getContext())) {
                resources = getResources();
                i8 = k7.d.B;
            } else if (!u7.c.g(getContext())) {
                return;
            } else {
                resources = getResources();
                i8 = k7.d.A;
            }
            this.f18557a0 = resources.getDimensionPixelSize(i8);
        }
    }

    private void l(RectF rectF) {
        float f5 = rectF.left;
        int i8 = this.R;
        rectF.left = f5 - i8;
        rectF.right += i8;
    }

    private void l0(Rect rect) {
        x7.h hVar = this.P;
        if (hVar != null) {
            int i8 = rect.bottom;
            hVar.setBounds(rect.left, i8 - this.f18563d0, rect.right, i8);
        }
    }

    private void m() {
        n(this.f18592v0, this.f18598y0, this.f18596x0, this.A0, this.f18600z0);
    }

    private void m0() {
        if (this.f18581n != null) {
            EditText editText = this.f18564e;
            n0(editText == null ? 0 : editText.getText().length());
        }
    }

    private void n(CheckableImageButton checkableImageButton, boolean z4, ColorStateList colorStateList, boolean z8, PorterDuff.Mode mode) {
        Drawable drawable = checkableImageButton.getDrawable();
        if (drawable != null && (z4 || z8)) {
            drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
            if (z4) {
                androidx.core.graphics.drawable.a.o(drawable, colorStateList);
            }
            if (z8) {
                androidx.core.graphics.drawable.a.p(drawable, mode);
            }
        }
        if (checkableImageButton.getDrawable() != drawable) {
            checkableImageButton.setImageDrawable(drawable);
        }
    }

    private void o() {
        n(this.f18576k0, this.f18580m0, this.f18578l0, this.f18583o0, this.f18582n0);
    }

    private static void o0(Context context, TextView textView, int i8, int i9, boolean z4) {
        textView.setContentDescription(context.getString(z4 ? j.f21207c : j.f21206b, Integer.valueOf(i8), Integer.valueOf(i9)));
    }

    private void p() {
        int i8 = this.W;
        if (i8 == 0) {
            this.O = null;
        } else if (i8 == 1) {
            this.O = new x7.h(this.Q);
            this.P = new x7.h();
            return;
        } else if (i8 != 2) {
            throw new IllegalArgumentException(this.W + " is illegal; only @BoxBackgroundMode constants are supported.");
        } else {
            this.O = (!this.H || (this.O instanceof com.google.android.material.textfield.c)) ? new x7.h(this.Q) : new com.google.android.material.textfield.c(this.Q);
        }
        this.P = null;
    }

    private void p0() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        TextView textView = this.f18581n;
        if (textView != null) {
            e0(textView, this.f18579m ? this.f18584p : this.q);
            if (!this.f18579m && (colorStateList2 = this.A) != null) {
                this.f18581n.setTextColor(colorStateList2);
            }
            if (!this.f18579m || (colorStateList = this.B) == null) {
                return;
            }
            this.f18581n.setTextColor(colorStateList);
        }
    }

    private int q() {
        return this.W == 1 ? n7.a.g(n7.a.e(this, k7.b.f21066s, 0), this.f18567f0) : this.f18567f0;
    }

    private void q0() {
        if (!A() || this.T0 || this.T == this.f18559b0) {
            return;
        }
        y();
        T();
    }

    private Rect r(Rect rect) {
        int i8;
        int i9;
        if (this.f18564e != null) {
            Rect rect2 = this.f18571h0;
            boolean z4 = c0.E(this) == 1;
            rect2.bottom = rect.bottom;
            int i10 = this.W;
            if (i10 == 1) {
                rect2.left = G(rect.left, z4);
                i8 = rect.top + this.f18557a0;
            } else if (i10 == 2) {
                rect2.left = rect.left + this.f18564e.getPaddingLeft();
                rect2.top = rect.top - v();
                i9 = rect.right - this.f18564e.getPaddingRight();
                rect2.right = i9;
                return rect2;
            } else {
                rect2.left = G(rect.left, z4);
                i8 = getPaddingTop();
            }
            rect2.top = i8;
            i9 = H(rect.right, z4);
            rect2.right = i9;
            return rect2;
        }
        throw new IllegalStateException();
    }

    private boolean r0() {
        boolean z4;
        if (this.f18564e == null) {
            return false;
        }
        boolean z8 = true;
        if (g0()) {
            int measuredWidth = this.f18558b.getMeasuredWidth() - this.f18564e.getPaddingLeft();
            if (this.f18585p0 == null || this.f18586q0 != measuredWidth) {
                ColorDrawable colorDrawable = new ColorDrawable();
                this.f18585p0 = colorDrawable;
                this.f18586q0 = measuredWidth;
                colorDrawable.setBounds(0, 0, measuredWidth, 1);
            }
            Drawable[] a9 = androidx.core.widget.k.a(this.f18564e);
            Drawable drawable = a9[0];
            Drawable drawable2 = this.f18585p0;
            if (drawable != drawable2) {
                androidx.core.widget.k.l(this.f18564e, drawable2, a9[1], a9[2], a9[3]);
                z4 = true;
            }
            z4 = false;
        } else {
            if (this.f18585p0 != null) {
                Drawable[] a10 = androidx.core.widget.k.a(this.f18564e);
                androidx.core.widget.k.l(this.f18564e, null, a10[1], a10[2], a10[3]);
                this.f18585p0 = null;
                z4 = true;
            }
            z4 = false;
        }
        if (f0()) {
            int measuredWidth2 = this.G.getMeasuredWidth() - this.f18564e.getPaddingRight();
            CheckableImageButton endIconToUpdateDummyDrawable = getEndIconToUpdateDummyDrawable();
            if (endIconToUpdateDummyDrawable != null) {
                measuredWidth2 = measuredWidth2 + endIconToUpdateDummyDrawable.getMeasuredWidth() + i.b((ViewGroup.MarginLayoutParams) endIconToUpdateDummyDrawable.getLayoutParams());
            }
            Drawable[] a11 = androidx.core.widget.k.a(this.f18564e);
            Drawable drawable3 = this.B0;
            if (drawable3 == null || this.C0 == measuredWidth2) {
                if (drawable3 == null) {
                    ColorDrawable colorDrawable2 = new ColorDrawable();
                    this.B0 = colorDrawable2;
                    this.C0 = measuredWidth2;
                    colorDrawable2.setBounds(0, 0, measuredWidth2, 1);
                }
                Drawable drawable4 = a11[2];
                Drawable drawable5 = this.B0;
                if (drawable4 != drawable5) {
                    this.D0 = a11[2];
                    androidx.core.widget.k.l(this.f18564e, a11[0], a11[1], drawable5, a11[3]);
                } else {
                    z8 = z4;
                }
            } else {
                this.C0 = measuredWidth2;
                drawable3.setBounds(0, 0, measuredWidth2, 1);
                androidx.core.widget.k.l(this.f18564e, a11[0], a11[1], this.B0, a11[3]);
            }
        } else if (this.B0 == null) {
            return z4;
        } else {
            Drawable[] a12 = androidx.core.widget.k.a(this.f18564e);
            if (a12[2] == this.B0) {
                androidx.core.widget.k.l(this.f18564e, a12[0], a12[1], this.D0, a12[3]);
            } else {
                z8 = z4;
            }
            this.B0 = null;
        }
        return z8;
    }

    private int s(Rect rect, Rect rect2, float f5) {
        return P() ? (int) (rect2.top + f5) : rect.bottom - this.f18564e.getCompoundPaddingBottom();
    }

    private void setEditText(EditText editText) {
        if (this.f18564e != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }
        if (this.f18590t0 != 3 && !(editText instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
        }
        this.f18564e = editText;
        setMinWidth(this.f18568g);
        setMaxWidth(this.f18570h);
        S();
        setTextInputAccessibilityDelegate(new e(this));
        this.U0.C0(this.f18564e.getTypeface());
        this.U0.m0(this.f18564e.getTextSize());
        int gravity = this.f18564e.getGravity();
        this.U0.c0((gravity & (-113)) | 48);
        this.U0.l0(gravity);
        this.f18564e.addTextChangedListener(new a());
        if (this.I0 == null) {
            this.I0 = this.f18564e.getHintTextColors();
        }
        if (this.H) {
            if (TextUtils.isEmpty(this.K)) {
                CharSequence hint = this.f18564e.getHint();
                this.f18566f = hint;
                setHint(hint);
                this.f18564e.setHint((CharSequence) null);
            }
            this.L = true;
        }
        if (this.f18581n != null) {
            n0(this.f18564e.getText().length());
        }
        s0();
        this.f18573j.e();
        this.f18558b.bringToFront();
        this.f18560c.bringToFront();
        this.f18562d.bringToFront();
        this.G0.bringToFront();
        B();
        A0();
        D0();
        if (!isEnabled()) {
            editText.setEnabled(false);
        }
        w0(false, true);
    }

    private void setErrorIconVisible(boolean z4) {
        this.G0.setVisibility(z4 ? 0 : 8);
        this.f18562d.setVisibility(z4 ? 8 : 0);
        D0();
        if (I()) {
            return;
        }
        r0();
    }

    private void setHintInternal(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.K)) {
            return;
        }
        this.K = charSequence;
        this.U0.A0(charSequence);
        if (this.T0) {
            return;
        }
        T();
    }

    private void setPlaceholderTextEnabled(boolean z4) {
        if (this.f18593w == z4) {
            return;
        }
        if (z4) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
            this.f18595x = appCompatTextView;
            appCompatTextView.setId(k7.f.Y);
            c0.v0(this.f18595x, 1);
            setPlaceholderTextAppearance(this.f18599z);
            setPlaceholderTextColor(this.f18597y);
            g();
        } else {
            Z();
            this.f18595x = null;
        }
        this.f18593w = z4;
    }

    private int t(Rect rect, float f5) {
        return P() ? (int) (rect.centerY() - (f5 / 2.0f)) : rect.top + this.f18564e.getCompoundPaddingTop();
    }

    private boolean t0() {
        int max;
        if (this.f18564e != null && this.f18564e.getMeasuredHeight() < (max = Math.max(this.f18560c.getMeasuredHeight(), this.f18558b.getMeasuredHeight()))) {
            this.f18564e.setMinimumHeight(max);
            return true;
        }
        return false;
    }

    private Rect u(Rect rect) {
        if (this.f18564e != null) {
            Rect rect2 = this.f18571h0;
            float B = this.U0.B();
            rect2.left = rect.left + this.f18564e.getCompoundPaddingLeft();
            rect2.top = t(rect, B);
            rect2.right = rect.right - this.f18564e.getCompoundPaddingRight();
            rect2.bottom = s(rect, rect2, B);
            return rect2;
        }
        throw new IllegalStateException();
    }

    private void u0() {
        if (this.W != 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f18556a.getLayoutParams();
            int v8 = v();
            if (v8 != layoutParams.topMargin) {
                layoutParams.topMargin = v8;
                this.f18556a.requestLayout();
            }
        }
    }

    private int v() {
        float s8;
        if (this.H) {
            int i8 = this.W;
            if (i8 == 0 || i8 == 1) {
                s8 = this.U0.s();
            } else if (i8 != 2) {
                return 0;
            } else {
                s8 = this.U0.s() / 2.0f;
            }
            return (int) s8;
        }
        return 0;
    }

    private boolean w() {
        return this.W == 2 && x();
    }

    private void w0(boolean z4, boolean z8) {
        ColorStateList colorStateList;
        com.google.android.material.internal.a aVar;
        TextView textView;
        boolean isEnabled = isEnabled();
        EditText editText = this.f18564e;
        boolean z9 = (editText == null || TextUtils.isEmpty(editText.getText())) ? false : true;
        EditText editText2 = this.f18564e;
        boolean z10 = editText2 != null && editText2.hasFocus();
        boolean k8 = this.f18573j.k();
        ColorStateList colorStateList2 = this.I0;
        if (colorStateList2 != null) {
            this.U0.b0(colorStateList2);
            this.U0.k0(this.I0);
        }
        if (!isEnabled) {
            ColorStateList colorStateList3 = this.I0;
            int colorForState = colorStateList3 != null ? colorStateList3.getColorForState(new int[]{-16842910}, this.S0) : this.S0;
            this.U0.b0(ColorStateList.valueOf(colorForState));
            this.U0.k0(ColorStateList.valueOf(colorForState));
        } else if (k8) {
            this.U0.b0(this.f18573j.p());
        } else {
            if (this.f18579m && (textView = this.f18581n) != null) {
                aVar = this.U0;
                colorStateList = textView.getTextColors();
            } else if (z10 && (colorStateList = this.J0) != null) {
                aVar = this.U0;
            }
            aVar.b0(colorStateList);
        }
        if (z9 || !this.V0 || (isEnabled() && z10)) {
            if (z8 || this.T0) {
                z(z4);
            }
        } else if (z8 || !this.T0) {
            F(z4);
        }
    }

    private boolean x() {
        return this.f18559b0 > -1 && this.f18565e0 != 0;
    }

    private void x0() {
        EditText editText;
        if (this.f18595x == null || (editText = this.f18564e) == null) {
            return;
        }
        this.f18595x.setGravity(editText.getGravity());
        this.f18595x.setPadding(this.f18564e.getCompoundPaddingLeft(), this.f18564e.getCompoundPaddingTop(), this.f18564e.getCompoundPaddingRight(), this.f18564e.getCompoundPaddingBottom());
    }

    private void y() {
        if (A()) {
            ((com.google.android.material.textfield.c) this.O).t0();
        }
    }

    private void y0() {
        EditText editText = this.f18564e;
        z0(editText == null ? 0 : editText.getText().length());
    }

    private void z(boolean z4) {
        ValueAnimator valueAnimator = this.X0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.X0.cancel();
        }
        if (z4 && this.W0) {
            i(1.0f);
        } else {
            this.U0.p0(1.0f);
        }
        this.T0 = false;
        if (A()) {
            T();
        }
        y0();
        B0();
        E0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z0(int i8) {
        if (i8 != 0 || this.T0) {
            J();
        } else {
            i0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void F0() {
        /*
            Method dump skipped, instructions count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.TextInputLayout.F0():void");
    }

    public boolean K() {
        return this.f18562d.getVisibility() == 0 && this.f18592v0.getVisibility() == 0;
    }

    public boolean M() {
        return this.f18573j.y();
    }

    final boolean N() {
        return this.T0;
    }

    public boolean O() {
        return this.L;
    }

    public boolean Q() {
        return this.f18576k0.getVisibility() == 0;
    }

    public void V() {
        X(this.f18592v0, this.f18596x0);
    }

    public void W() {
        X(this.G0, this.H0);
    }

    public void Y() {
        X(this.f18576k0, this.f18578l0);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof EditText)) {
            super.addView(view, i8, layoutParams);
            return;
        }
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
        layoutParams2.gravity = (layoutParams2.gravity & (-113)) | 16;
        this.f18556a.addView(view, layoutParams2);
        this.f18556a.setLayoutParams(layoutParams);
        u0();
        setEditText((EditText) view);
    }

    @Override // android.view.ViewGroup, android.view.View
    @TargetApi(26)
    public void dispatchProvideAutofillStructure(ViewStructure viewStructure, int i8) {
        EditText editText = this.f18564e;
        if (editText == null) {
            super.dispatchProvideAutofillStructure(viewStructure, i8);
            return;
        }
        if (this.f18566f != null) {
            boolean z4 = this.L;
            this.L = false;
            CharSequence hint = editText.getHint();
            this.f18564e.setHint(this.f18566f);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, i8);
                return;
            } finally {
                this.f18564e.setHint(hint);
                this.L = z4;
            }
        }
        viewStructure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(viewStructure, i8);
        onProvideAutofillVirtualStructure(viewStructure, i8);
        viewStructure.setChildCount(this.f18556a.getChildCount());
        for (int i9 = 0; i9 < this.f18556a.getChildCount(); i9++) {
            View childAt = this.f18556a.getChildAt(i9);
            ViewStructure newChild = viewStructure.newChild(i9);
            childAt.dispatchProvideAutofillStructure(newChild, i8);
            if (childAt == this.f18564e) {
                newChild.setHint(getHint());
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.Z0 = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.Z0 = false;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        E(canvas);
        D(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (this.Y0) {
            return;
        }
        boolean z4 = true;
        this.Y0 = true;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        com.google.android.material.internal.a aVar = this.U0;
        boolean z02 = aVar != null ? aVar.z0(drawableState) | false : false;
        if (this.f18564e != null) {
            if (!c0.W(this) || !isEnabled()) {
                z4 = false;
            }
            v0(z4);
        }
        s0();
        F0();
        if (z02) {
            invalidate();
        }
        this.Y0 = false;
    }

    public void e(f fVar) {
        this.f18588s0.add(fVar);
        if (this.f18564e != null) {
            fVar.a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0015, code lost:
        if (r3.getTextColors().getDefaultColor() == (-65281)) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void e0(android.widget.TextView r3, int r4) {
        /*
            r2 = this;
            r0 = 1
            androidx.core.widget.k.q(r3, r4)     // Catch: java.lang.Exception -> L1a
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> L1a
            r1 = 23
            if (r4 < r1) goto L18
            android.content.res.ColorStateList r4 = r3.getTextColors()     // Catch: java.lang.Exception -> L1a
            int r4 = r4.getDefaultColor()     // Catch: java.lang.Exception -> L1a
            r1 = -65281(0xffffffffffff00ff, float:NaN)
            if (r4 != r1) goto L18
            goto L1a
        L18:
            r4 = 0
            r0 = r4
        L1a:
            if (r0 == 0) goto L2e
            int r4 = k7.k.f21231b
            androidx.core.widget.k.q(r3, r4)
            android.content.Context r4 = r2.getContext()
            int r0 = k7.c.f21075b
            int r4 = androidx.core.content.a.d(r4, r0)
            r3.setTextColor(r4)
        L2e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.TextInputLayout.e0(android.widget.TextView, int):void");
    }

    public void f(g gVar) {
        this.f18594w0.add(gVar);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public int getBaseline() {
        EditText editText = this.f18564e;
        return editText != null ? editText.getBaseline() + getPaddingTop() + v() : super.getBaseline();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public x7.h getBoxBackground() {
        int i8 = this.W;
        if (i8 == 1 || i8 == 2) {
            return this.O;
        }
        throw new IllegalStateException();
    }

    public int getBoxBackgroundColor() {
        return this.f18567f0;
    }

    public int getBoxBackgroundMode() {
        return this.W;
    }

    public float getBoxCornerRadiusBottomEnd() {
        return this.O.s();
    }

    public float getBoxCornerRadiusBottomStart() {
        return this.O.t();
    }

    public float getBoxCornerRadiusTopEnd() {
        return this.O.J();
    }

    public float getBoxCornerRadiusTopStart() {
        return this.O.I();
    }

    public int getBoxStrokeColor() {
        return this.M0;
    }

    public ColorStateList getBoxStrokeErrorColor() {
        return this.N0;
    }

    public int getBoxStrokeWidth() {
        return this.f18561c0;
    }

    public int getBoxStrokeWidthFocused() {
        return this.f18563d0;
    }

    public int getCounterMaxLength() {
        return this.f18577l;
    }

    CharSequence getCounterOverflowDescription() {
        TextView textView;
        if (this.f18575k && this.f18579m && (textView = this.f18581n) != null) {
            return textView.getContentDescription();
        }
        return null;
    }

    public ColorStateList getCounterOverflowTextColor() {
        return this.A;
    }

    public ColorStateList getCounterTextColor() {
        return this.A;
    }

    public ColorStateList getDefaultHintTextColor() {
        return this.I0;
    }

    public EditText getEditText() {
        return this.f18564e;
    }

    public CharSequence getEndIconContentDescription() {
        return this.f18592v0.getContentDescription();
    }

    public Drawable getEndIconDrawable() {
        return this.f18592v0.getDrawable();
    }

    public int getEndIconMode() {
        return this.f18590t0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CheckableImageButton getEndIconView() {
        return this.f18592v0;
    }

    public CharSequence getError() {
        if (this.f18573j.x()) {
            return this.f18573j.n();
        }
        return null;
    }

    public CharSequence getErrorContentDescription() {
        return this.f18573j.m();
    }

    public int getErrorCurrentTextColors() {
        return this.f18573j.o();
    }

    public Drawable getErrorIconDrawable() {
        return this.G0.getDrawable();
    }

    final int getErrorTextCurrentColor() {
        return this.f18573j.o();
    }

    public CharSequence getHelperText() {
        if (this.f18573j.y()) {
            return this.f18573j.q();
        }
        return null;
    }

    public int getHelperTextCurrentTextColor() {
        return this.f18573j.r();
    }

    public CharSequence getHint() {
        if (this.H) {
            return this.K;
        }
        return null;
    }

    final float getHintCollapsedTextHeight() {
        return this.U0.s();
    }

    final int getHintCurrentCollapsedTextColor() {
        return this.U0.w();
    }

    public ColorStateList getHintTextColor() {
        return this.J0;
    }

    public int getMaxWidth() {
        return this.f18570h;
    }

    public int getMinWidth() {
        return this.f18568g;
    }

    @Deprecated
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.f18592v0.getContentDescription();
    }

    @Deprecated
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.f18592v0.getDrawable();
    }

    public CharSequence getPlaceholderText() {
        if (this.f18593w) {
            return this.f18589t;
        }
        return null;
    }

    public int getPlaceholderTextAppearance() {
        return this.f18599z;
    }

    public ColorStateList getPlaceholderTextColor() {
        return this.f18597y;
    }

    public CharSequence getPrefixText() {
        return this.C;
    }

    public ColorStateList getPrefixTextColor() {
        return this.E.getTextColors();
    }

    public TextView getPrefixTextView() {
        return this.E;
    }

    public CharSequence getStartIconContentDescription() {
        return this.f18576k0.getContentDescription();
    }

    public Drawable getStartIconDrawable() {
        return this.f18576k0.getDrawable();
    }

    public CharSequence getSuffixText() {
        return this.F;
    }

    public ColorStateList getSuffixTextColor() {
        return this.G.getTextColors();
    }

    public TextView getSuffixTextView() {
        return this.G;
    }

    public Typeface getTypeface() {
        return this.f18574j0;
    }

    void i(float f5) {
        if (this.U0.D() == f5) {
            return;
        }
        if (this.X0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.X0 = valueAnimator;
            valueAnimator.setInterpolator(l7.a.f21787b);
            this.X0.setDuration(167L);
            this.X0.addUpdateListener(new d());
        }
        this.X0.setFloatValues(this.U0.D(), f5);
        this.X0.start();
    }

    void n0(int i8) {
        boolean z4 = this.f18579m;
        int i9 = this.f18577l;
        if (i9 == -1) {
            this.f18581n.setText(String.valueOf(i8));
            this.f18581n.setContentDescription(null);
            this.f18579m = false;
        } else {
            this.f18579m = i8 > i9;
            o0(getContext(), this.f18581n, i8, this.f18577l, this.f18579m);
            if (z4 != this.f18579m) {
                p0();
            }
            this.f18581n.setText(androidx.core.text.a.c().j(getContext().getString(j.f21208d, Integer.valueOf(i8), Integer.valueOf(this.f18577l))));
        }
        if (this.f18564e == null || z4 == this.f18579m) {
            return;
        }
        v0(false);
        F0();
        s0();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        EditText editText = this.f18564e;
        if (editText != null) {
            Rect rect = this.f18569g0;
            com.google.android.material.internal.c.a(this, editText, rect);
            l0(rect);
            if (this.H) {
                this.U0.m0(this.f18564e.getTextSize());
                int gravity = this.f18564e.getGravity();
                this.U0.c0((gravity & (-113)) | 48);
                this.U0.l0(gravity);
                this.U0.Y(r(rect));
                this.U0.h0(u(rect));
                this.U0.U();
                if (!A() || this.T0) {
                    return;
                }
                T();
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        boolean t02 = t0();
        boolean r02 = r0();
        if (t02 || r02) {
            this.f18564e.post(new c());
        }
        x0();
        A0();
        D0();
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        setError(savedState.f18601c);
        if (savedState.f18602d) {
            this.f18592v0.post(new b());
        }
        setHint(savedState.f18603e);
        setHelperText(savedState.f18604f);
        setPlaceholderText(savedState.f18605g);
        requestLayout();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.f18573j.k()) {
            savedState.f18601c = getError();
        }
        savedState.f18602d = I() && this.f18592v0.isChecked();
        savedState.f18603e = getHint();
        savedState.f18604f = getHelperText();
        savedState.f18605g = getPlaceholderText();
        return savedState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s0() {
        Drawable background;
        TextView textView;
        int currentTextColor;
        EditText editText = this.f18564e;
        if (editText == null || this.W != 0 || (background = editText.getBackground()) == null) {
            return;
        }
        if (t.a(background)) {
            background = background.mutate();
        }
        if (this.f18573j.k()) {
            currentTextColor = this.f18573j.o();
        } else if (!this.f18579m || (textView = this.f18581n) == null) {
            androidx.core.graphics.drawable.a.c(background);
            this.f18564e.refreshDrawableState();
            return;
        } else {
            currentTextColor = textView.getCurrentTextColor();
        }
        background.setColorFilter(androidx.appcompat.widget.g.e(currentTextColor, PorterDuff.Mode.SRC_IN));
    }

    public void setBoxBackgroundColor(int i8) {
        if (this.f18567f0 != i8) {
            this.f18567f0 = i8;
            this.O0 = i8;
            this.Q0 = i8;
            this.R0 = i8;
            j();
        }
    }

    public void setBoxBackgroundColorResource(int i8) {
        setBoxBackgroundColor(androidx.core.content.a.d(getContext(), i8));
    }

    public void setBoxBackgroundColorStateList(ColorStateList colorStateList) {
        int defaultColor = colorStateList.getDefaultColor();
        this.O0 = defaultColor;
        this.f18567f0 = defaultColor;
        this.P0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
        this.Q0 = colorStateList.getColorForState(new int[]{16842908, 16842910}, -1);
        this.R0 = colorStateList.getColorForState(new int[]{16843623, 16842910}, -1);
        j();
    }

    public void setBoxBackgroundMode(int i8) {
        if (i8 == this.W) {
            return;
        }
        this.W = i8;
        if (this.f18564e != null) {
            S();
        }
    }

    public void setBoxStrokeColor(int i8) {
        if (this.M0 != i8) {
            this.M0 = i8;
            F0();
        }
    }

    public void setBoxStrokeColorStateList(ColorStateList colorStateList) {
        int defaultColor;
        if (!colorStateList.isStateful()) {
            if (this.M0 != colorStateList.getDefaultColor()) {
                defaultColor = colorStateList.getDefaultColor();
            }
            F0();
        }
        this.K0 = colorStateList.getDefaultColor();
        this.S0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
        this.L0 = colorStateList.getColorForState(new int[]{16843623, 16842910}, -1);
        defaultColor = colorStateList.getColorForState(new int[]{16842908, 16842910}, -1);
        this.M0 = defaultColor;
        F0();
    }

    public void setBoxStrokeErrorColor(ColorStateList colorStateList) {
        if (this.N0 != colorStateList) {
            this.N0 = colorStateList;
            F0();
        }
    }

    public void setBoxStrokeWidth(int i8) {
        this.f18561c0 = i8;
        F0();
    }

    public void setBoxStrokeWidthFocused(int i8) {
        this.f18563d0 = i8;
        F0();
    }

    public void setBoxStrokeWidthFocusedResource(int i8) {
        setBoxStrokeWidthFocused(getResources().getDimensionPixelSize(i8));
    }

    public void setBoxStrokeWidthResource(int i8) {
        setBoxStrokeWidth(getResources().getDimensionPixelSize(i8));
    }

    public void setCounterEnabled(boolean z4) {
        if (this.f18575k != z4) {
            if (z4) {
                AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
                this.f18581n = appCompatTextView;
                appCompatTextView.setId(k7.f.V);
                Typeface typeface = this.f18574j0;
                if (typeface != null) {
                    this.f18581n.setTypeface(typeface);
                }
                this.f18581n.setMaxLines(1);
                this.f18573j.d(this.f18581n, 2);
                i.d((ViewGroup.MarginLayoutParams) this.f18581n.getLayoutParams(), getResources().getDimensionPixelOffset(k7.d.f21138y0));
                p0();
                m0();
            } else {
                this.f18573j.z(this.f18581n, 2);
                this.f18581n = null;
            }
            this.f18575k = z4;
        }
    }

    public void setCounterMaxLength(int i8) {
        if (this.f18577l != i8) {
            if (i8 <= 0) {
                i8 = -1;
            }
            this.f18577l = i8;
            if (this.f18575k) {
                m0();
            }
        }
    }

    public void setCounterOverflowTextAppearance(int i8) {
        if (this.f18584p != i8) {
            this.f18584p = i8;
            p0();
        }
    }

    public void setCounterOverflowTextColor(ColorStateList colorStateList) {
        if (this.B != colorStateList) {
            this.B = colorStateList;
            p0();
        }
    }

    public void setCounterTextAppearance(int i8) {
        if (this.q != i8) {
            this.q = i8;
            p0();
        }
    }

    public void setCounterTextColor(ColorStateList colorStateList) {
        if (this.A != colorStateList) {
            this.A = colorStateList;
            p0();
        }
    }

    public void setDefaultHintTextColor(ColorStateList colorStateList) {
        this.I0 = colorStateList;
        this.J0 = colorStateList;
        if (this.f18564e != null) {
            v0(false);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z4) {
        U(this, z4);
        super.setEnabled(z4);
    }

    public void setEndIconActivated(boolean z4) {
        this.f18592v0.setActivated(z4);
    }

    public void setEndIconCheckable(boolean z4) {
        this.f18592v0.setCheckable(z4);
    }

    public void setEndIconContentDescription(int i8) {
        setEndIconContentDescription(i8 != 0 ? getResources().getText(i8) : null);
    }

    public void setEndIconContentDescription(CharSequence charSequence) {
        if (getEndIconContentDescription() != charSequence) {
            this.f18592v0.setContentDescription(charSequence);
        }
    }

    public void setEndIconDrawable(int i8) {
        setEndIconDrawable(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    public void setEndIconDrawable(Drawable drawable) {
        this.f18592v0.setImageDrawable(drawable);
        V();
    }

    public void setEndIconMode(int i8) {
        int i9 = this.f18590t0;
        this.f18590t0 = i8;
        C(i9);
        setEndIconVisible(i8 != 0);
        if (getEndIconDelegate().b(this.W)) {
            getEndIconDelegate().a();
            m();
            return;
        }
        throw new IllegalStateException("The current box background mode " + this.W + " is not supported by the end icon mode " + i8);
    }

    public void setEndIconOnClickListener(View.OnClickListener onClickListener) {
        c0(this.f18592v0, onClickListener, this.E0);
    }

    public void setEndIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.E0 = onLongClickListener;
        d0(this.f18592v0, onLongClickListener);
    }

    public void setEndIconTintList(ColorStateList colorStateList) {
        if (this.f18596x0 != colorStateList) {
            this.f18596x0 = colorStateList;
            this.f18598y0 = true;
            m();
        }
    }

    public void setEndIconTintMode(PorterDuff.Mode mode) {
        if (this.f18600z0 != mode) {
            this.f18600z0 = mode;
            this.A0 = true;
            m();
        }
    }

    public void setEndIconVisible(boolean z4) {
        if (K() != z4) {
            this.f18592v0.setVisibility(z4 ? 0 : 8);
            D0();
            r0();
        }
    }

    public void setError(CharSequence charSequence) {
        if (!this.f18573j.x()) {
            if (TextUtils.isEmpty(charSequence)) {
                return;
            }
            setErrorEnabled(true);
        }
        if (TextUtils.isEmpty(charSequence)) {
            this.f18573j.t();
        } else {
            this.f18573j.M(charSequence);
        }
    }

    public void setErrorContentDescription(CharSequence charSequence) {
        this.f18573j.B(charSequence);
    }

    public void setErrorEnabled(boolean z4) {
        this.f18573j.C(z4);
    }

    public void setErrorIconDrawable(int i8) {
        setErrorIconDrawable(i8 != 0 ? h.a.b(getContext(), i8) : null);
        W();
    }

    public void setErrorIconDrawable(Drawable drawable) {
        this.G0.setImageDrawable(drawable);
        setErrorIconVisible(drawable != null && this.f18573j.x());
    }

    public void setErrorIconOnClickListener(View.OnClickListener onClickListener) {
        c0(this.G0, onClickListener, this.F0);
    }

    public void setErrorIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.F0 = onLongClickListener;
        d0(this.G0, onLongClickListener);
    }

    public void setErrorIconTintList(ColorStateList colorStateList) {
        this.H0 = colorStateList;
        Drawable drawable = this.G0.getDrawable();
        if (drawable != null) {
            drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
            androidx.core.graphics.drawable.a.o(drawable, colorStateList);
        }
        if (this.G0.getDrawable() != drawable) {
            this.G0.setImageDrawable(drawable);
        }
    }

    public void setErrorIconTintMode(PorterDuff.Mode mode) {
        Drawable drawable = this.G0.getDrawable();
        if (drawable != null) {
            drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
            androidx.core.graphics.drawable.a.p(drawable, mode);
        }
        if (this.G0.getDrawable() != drawable) {
            this.G0.setImageDrawable(drawable);
        }
    }

    public void setErrorTextAppearance(int i8) {
        this.f18573j.D(i8);
    }

    public void setErrorTextColor(ColorStateList colorStateList) {
        this.f18573j.E(colorStateList);
    }

    public void setExpandedHintEnabled(boolean z4) {
        if (this.V0 != z4) {
            this.V0 = z4;
            v0(false);
        }
    }

    public void setHelperText(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            if (M()) {
                setHelperTextEnabled(false);
                return;
            }
            return;
        }
        if (!M()) {
            setHelperTextEnabled(true);
        }
        this.f18573j.N(charSequence);
    }

    public void setHelperTextColor(ColorStateList colorStateList) {
        this.f18573j.H(colorStateList);
    }

    public void setHelperTextEnabled(boolean z4) {
        this.f18573j.G(z4);
    }

    public void setHelperTextTextAppearance(int i8) {
        this.f18573j.F(i8);
    }

    public void setHint(int i8) {
        setHint(i8 != 0 ? getResources().getText(i8) : null);
    }

    public void setHint(CharSequence charSequence) {
        if (this.H) {
            setHintInternal(charSequence);
            sendAccessibilityEvent(RecognitionOptions.PDF417);
        }
    }

    public void setHintAnimationEnabled(boolean z4) {
        this.W0 = z4;
    }

    public void setHintEnabled(boolean z4) {
        if (z4 != this.H) {
            this.H = z4;
            if (z4) {
                CharSequence hint = this.f18564e.getHint();
                if (!TextUtils.isEmpty(hint)) {
                    if (TextUtils.isEmpty(this.K)) {
                        setHint(hint);
                    }
                    this.f18564e.setHint((CharSequence) null);
                }
                this.L = true;
            } else {
                this.L = false;
                if (!TextUtils.isEmpty(this.K) && TextUtils.isEmpty(this.f18564e.getHint())) {
                    this.f18564e.setHint(this.K);
                }
                setHintInternal(null);
            }
            if (this.f18564e != null) {
                u0();
            }
        }
    }

    public void setHintTextAppearance(int i8) {
        this.U0.Z(i8);
        this.J0 = this.U0.q();
        if (this.f18564e != null) {
            v0(false);
            u0();
        }
    }

    public void setHintTextColor(ColorStateList colorStateList) {
        if (this.J0 != colorStateList) {
            if (this.I0 == null) {
                this.U0.b0(colorStateList);
            }
            this.J0 = colorStateList;
            if (this.f18564e != null) {
                v0(false);
            }
        }
    }

    public void setMaxWidth(int i8) {
        this.f18570h = i8;
        EditText editText = this.f18564e;
        if (editText == null || i8 == -1) {
            return;
        }
        editText.setMaxWidth(i8);
    }

    public void setMaxWidthResource(int i8) {
        setMaxWidth(getContext().getResources().getDimensionPixelSize(i8));
    }

    public void setMinWidth(int i8) {
        this.f18568g = i8;
        EditText editText = this.f18564e;
        if (editText == null || i8 == -1) {
            return;
        }
        editText.setMinWidth(i8);
    }

    public void setMinWidthResource(int i8) {
        setMinWidth(getContext().getResources().getDimensionPixelSize(i8));
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(int i8) {
        setPasswordVisibilityToggleContentDescription(i8 != 0 ? getResources().getText(i8) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(CharSequence charSequence) {
        this.f18592v0.setContentDescription(charSequence);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(int i8) {
        setPasswordVisibilityToggleDrawable(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(Drawable drawable) {
        this.f18592v0.setImageDrawable(drawable);
    }

    @Deprecated
    public void setPasswordVisibilityToggleEnabled(boolean z4) {
        if (z4 && this.f18590t0 != 1) {
            setEndIconMode(1);
        } else if (z4) {
        } else {
            setEndIconMode(0);
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintList(ColorStateList colorStateList) {
        this.f18596x0 = colorStateList;
        this.f18598y0 = true;
        m();
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode mode) {
        this.f18600z0 = mode;
        this.A0 = true;
        m();
    }

    public void setPlaceholderText(CharSequence charSequence) {
        if (this.f18593w && TextUtils.isEmpty(charSequence)) {
            setPlaceholderTextEnabled(false);
        } else {
            if (!this.f18593w) {
                setPlaceholderTextEnabled(true);
            }
            this.f18589t = charSequence;
        }
        y0();
    }

    public void setPlaceholderTextAppearance(int i8) {
        this.f18599z = i8;
        TextView textView = this.f18595x;
        if (textView != null) {
            androidx.core.widget.k.q(textView, i8);
        }
    }

    public void setPlaceholderTextColor(ColorStateList colorStateList) {
        if (this.f18597y != colorStateList) {
            this.f18597y = colorStateList;
            TextView textView = this.f18595x;
            if (textView == null || colorStateList == null) {
                return;
            }
            textView.setTextColor(colorStateList);
        }
    }

    public void setPrefixText(CharSequence charSequence) {
        this.C = TextUtils.isEmpty(charSequence) ? null : charSequence;
        this.E.setText(charSequence);
        B0();
    }

    public void setPrefixTextAppearance(int i8) {
        androidx.core.widget.k.q(this.E, i8);
    }

    public void setPrefixTextColor(ColorStateList colorStateList) {
        this.E.setTextColor(colorStateList);
    }

    public void setStartIconCheckable(boolean z4) {
        this.f18576k0.setCheckable(z4);
    }

    public void setStartIconContentDescription(int i8) {
        setStartIconContentDescription(i8 != 0 ? getResources().getText(i8) : null);
    }

    public void setStartIconContentDescription(CharSequence charSequence) {
        if (getStartIconContentDescription() != charSequence) {
            this.f18576k0.setContentDescription(charSequence);
        }
    }

    public void setStartIconDrawable(int i8) {
        setStartIconDrawable(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    public void setStartIconDrawable(Drawable drawable) {
        this.f18576k0.setImageDrawable(drawable);
        if (drawable != null) {
            setStartIconVisible(true);
            Y();
            return;
        }
        setStartIconVisible(false);
        setStartIconOnClickListener(null);
        setStartIconOnLongClickListener(null);
        setStartIconContentDescription((CharSequence) null);
    }

    public void setStartIconOnClickListener(View.OnClickListener onClickListener) {
        c0(this.f18576k0, onClickListener, this.f18587r0);
    }

    public void setStartIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.f18587r0 = onLongClickListener;
        d0(this.f18576k0, onLongClickListener);
    }

    public void setStartIconTintList(ColorStateList colorStateList) {
        if (this.f18578l0 != colorStateList) {
            this.f18578l0 = colorStateList;
            this.f18580m0 = true;
            o();
        }
    }

    public void setStartIconTintMode(PorterDuff.Mode mode) {
        if (this.f18582n0 != mode) {
            this.f18582n0 = mode;
            this.f18583o0 = true;
            o();
        }
    }

    public void setStartIconVisible(boolean z4) {
        if (Q() != z4) {
            this.f18576k0.setVisibility(z4 ? 0 : 8);
            A0();
            r0();
        }
    }

    public void setSuffixText(CharSequence charSequence) {
        this.F = TextUtils.isEmpty(charSequence) ? null : charSequence;
        this.G.setText(charSequence);
        E0();
    }

    public void setSuffixTextAppearance(int i8) {
        androidx.core.widget.k.q(this.G, i8);
    }

    public void setSuffixTextColor(ColorStateList colorStateList) {
        this.G.setTextColor(colorStateList);
    }

    public void setTextInputAccessibilityDelegate(e eVar) {
        EditText editText = this.f18564e;
        if (editText != null) {
            c0.t0(editText, eVar);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface != this.f18574j0) {
            this.f18574j0 = typeface;
            this.U0.C0(typeface);
            this.f18573j.J(typeface);
            TextView textView = this.f18581n;
            if (textView != null) {
                textView.setTypeface(typeface);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v0(boolean z4) {
        w0(z4, false);
    }
}
