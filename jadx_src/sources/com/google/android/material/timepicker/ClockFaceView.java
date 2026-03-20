package com.google.android.material.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.material.timepicker.ClockHandView;
import java.util.Arrays;
import k7.d;
import k7.f;
import k7.h;
import k7.k;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class ClockFaceView extends RadialViewGroup implements ClockHandView.d {
    private final ClockHandView H;
    private final Rect K;
    private final RectF L;
    private final SparseArray<TextView> O;
    private final androidx.core.view.a P;
    private final int[] Q;
    private final float[] R;
    private final int T;
    private final int W;

    /* renamed from: a0  reason: collision with root package name */
    private final int f18699a0;

    /* renamed from: b0  reason: collision with root package name */
    private final int f18700b0;

    /* renamed from: c0  reason: collision with root package name */
    private String[] f18701c0;

    /* renamed from: d0  reason: collision with root package name */
    private float f18702d0;

    /* renamed from: e0  reason: collision with root package name */
    private final ColorStateList f18703e0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (ClockFaceView.this.isShown()) {
                ClockFaceView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                ClockFaceView.this.B(((ClockFaceView.this.getHeight() / 2) - ClockFaceView.this.H.g()) - ClockFaceView.this.T);
                return true;
            }
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends androidx.core.view.a {
        b() {
        }

        @Override // androidx.core.view.a
        public void g(View view, c cVar) {
            super.g(view, cVar);
            int intValue = ((Integer) view.getTag(f.f21167p)).intValue();
            if (intValue > 0) {
                cVar.F0((View) ClockFaceView.this.O.get(intValue - 1));
            }
            cVar.f0(c.C0043c.a(0, 1, intValue, 1, false, view.isSelected()));
        }
    }

    public ClockFaceView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.H);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public ClockFaceView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.K = new Rect();
        this.L = new RectF();
        this.O = new SparseArray<>();
        this.R = new float[]{0.0f, 0.9f, 1.0f};
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.E1, i8, k.J);
        Resources resources = getResources();
        ColorStateList a9 = u7.c.a(context, obtainStyledAttributes, l.G1);
        this.f18703e0 = a9;
        LayoutInflater.from(context).inflate(h.f21194p, (ViewGroup) this, true);
        ClockHandView clockHandView = (ClockHandView) findViewById(f.f21161j);
        this.H = clockHandView;
        this.T = resources.getDimensionPixelSize(d.f21127t);
        int colorForState = a9.getColorForState(new int[]{16842913}, a9.getDefaultColor());
        this.Q = new int[]{colorForState, colorForState, a9.getDefaultColor()};
        clockHandView.b(this);
        int defaultColor = h.a.a(context, k7.c.f21085l).getDefaultColor();
        ColorStateList a10 = u7.c.a(context, obtainStyledAttributes, l.F1);
        setBackgroundColor(a10 != null ? a10.getDefaultColor() : defaultColor);
        getViewTreeObserver().addOnPreDrawListener(new a());
        setFocusable(true);
        obtainStyledAttributes.recycle();
        this.P = new b();
        String[] strArr = new String[12];
        Arrays.fill(strArr, BuildConfig.FLAVOR);
        L(strArr, 0);
        this.W = resources.getDimensionPixelSize(d.G);
        this.f18699a0 = resources.getDimensionPixelSize(d.H);
        this.f18700b0 = resources.getDimensionPixelSize(d.f21131v);
    }

    private void I() {
        RectF d8 = this.H.d();
        for (int i8 = 0; i8 < this.O.size(); i8++) {
            TextView textView = this.O.get(i8);
            if (textView != null) {
                textView.getDrawingRect(this.K);
                this.K.offset(textView.getPaddingLeft(), textView.getPaddingTop());
                offsetDescendantRectToMyCoords(textView, this.K);
                this.L.set(this.K);
                textView.getPaint().setShader(J(d8, this.L));
                textView.invalidate();
            }
        }
    }

    private RadialGradient J(RectF rectF, RectF rectF2) {
        if (RectF.intersects(rectF, rectF2)) {
            return new RadialGradient(rectF.centerX() - this.L.left, rectF.centerY() - this.L.top, rectF.width() * 0.5f, this.Q, this.R, Shader.TileMode.CLAMP);
        }
        return null;
    }

    private static float K(float f5, float f8, float f9) {
        return Math.max(Math.max(f5, f8), f9);
    }

    private void M(int i8) {
        LayoutInflater from = LayoutInflater.from(getContext());
        int size = this.O.size();
        for (int i9 = 0; i9 < Math.max(this.f18701c0.length, size); i9++) {
            TextView textView = this.O.get(i9);
            if (i9 >= this.f18701c0.length) {
                removeView(textView);
                this.O.remove(i9);
            } else {
                if (textView == null) {
                    textView = (TextView) from.inflate(h.f21193o, (ViewGroup) this, false);
                    this.O.put(i9, textView);
                    addView(textView);
                }
                textView.setVisibility(0);
                textView.setText(this.f18701c0[i9]);
                textView.setTag(f.f21167p, Integer.valueOf(i9));
                c0.t0(textView, this.P);
                textView.setTextColor(this.f18703e0);
                if (i8 != 0) {
                    textView.setContentDescription(getResources().getString(i8, this.f18701c0[i9]));
                }
            }
        }
    }

    @Override // com.google.android.material.timepicker.RadialViewGroup
    public void B(int i8) {
        if (i8 != A()) {
            super.B(i8);
            this.H.j(A());
        }
    }

    public void L(String[] strArr, int i8) {
        this.f18701c0 = strArr;
        M(i8);
    }

    @Override // com.google.android.material.timepicker.ClockHandView.d
    public void a(float f5, boolean z4) {
        if (Math.abs(this.f18702d0 - f5) > 0.001f) {
            this.f18702d0 = f5;
            I();
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        c.I0(accessibilityNodeInfo).e0(c.b.b(1, this.f18701c0.length, false, 1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        I();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i8, int i9) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int K = (int) (this.f18700b0 / K(this.W / displayMetrics.heightPixels, this.f18699a0 / displayMetrics.widthPixels, 1.0f));
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(K, 1073741824);
        setMeasuredDimension(K, K);
        super.onMeasure(makeMeasureSpec, makeMeasureSpec);
    }
}
