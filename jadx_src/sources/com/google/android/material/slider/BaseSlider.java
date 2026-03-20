package com.google.android.material.slider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.SeekBar;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.android.material.internal.m;
import com.google.android.material.internal.r;
import com.google.android.material.internal.s;
import com.google.android.material.slider.BaseSlider;
import com.google.android.material.slider.a;
import com.google.android.material.slider.b;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import k7.j;
import k7.k;
import k7.l;
import x7.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class BaseSlider<S extends BaseSlider<S, L, T>, L extends com.google.android.material.slider.a<S>, T extends com.google.android.material.slider.b<S>> extends View {

    /* renamed from: q0  reason: collision with root package name */
    private static final String f18343q0 = BaseSlider.class.getSimpleName();

    /* renamed from: r0  reason: collision with root package name */
    static final int f18344r0 = k.I;
    private int A;
    private int B;
    private int C;
    private int E;
    private int F;
    private int G;
    private int H;
    private float K;
    private MotionEvent L;
    private com.google.android.material.slider.c O;
    private boolean P;
    private float Q;
    private float R;
    private ArrayList<Float> T;
    private int W;

    /* renamed from: a  reason: collision with root package name */
    private final Paint f18345a;

    /* renamed from: a0  reason: collision with root package name */
    private int f18346a0;

    /* renamed from: b  reason: collision with root package name */
    private final Paint f18347b;

    /* renamed from: b0  reason: collision with root package name */
    private float f18348b0;

    /* renamed from: c  reason: collision with root package name */
    private final Paint f18349c;

    /* renamed from: c0  reason: collision with root package name */
    private float[] f18350c0;

    /* renamed from: d  reason: collision with root package name */
    private final Paint f18351d;

    /* renamed from: d0  reason: collision with root package name */
    private boolean f18352d0;

    /* renamed from: e  reason: collision with root package name */
    private final Paint f18353e;

    /* renamed from: e0  reason: collision with root package name */
    private int f18354e0;

    /* renamed from: f  reason: collision with root package name */
    private final Paint f18355f;

    /* renamed from: f0  reason: collision with root package name */
    private boolean f18356f0;

    /* renamed from: g  reason: collision with root package name */
    private final e f18357g;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f18358g0;

    /* renamed from: h  reason: collision with root package name */
    private final AccessibilityManager f18359h;

    /* renamed from: h0  reason: collision with root package name */
    private boolean f18360h0;

    /* renamed from: i0  reason: collision with root package name */
    private ColorStateList f18361i0;

    /* renamed from: j  reason: collision with root package name */
    private BaseSlider<S, L, T>.d f18362j;

    /* renamed from: j0  reason: collision with root package name */
    private ColorStateList f18363j0;

    /* renamed from: k  reason: collision with root package name */
    private final f f18364k;

    /* renamed from: k0  reason: collision with root package name */
    private ColorStateList f18365k0;

    /* renamed from: l  reason: collision with root package name */
    private final List<z7.a> f18366l;

    /* renamed from: l0  reason: collision with root package name */
    private ColorStateList f18367l0;

    /* renamed from: m  reason: collision with root package name */
    private final List<L> f18368m;

    /* renamed from: m0  reason: collision with root package name */
    private ColorStateList f18369m0;

    /* renamed from: n  reason: collision with root package name */
    private final List<T> f18370n;

    /* renamed from: n0  reason: collision with root package name */
    private final h f18371n0;

    /* renamed from: o0  reason: collision with root package name */
    private float f18372o0;

    /* renamed from: p  reason: collision with root package name */
    private boolean f18373p;

    /* renamed from: p0  reason: collision with root package name */
    private int f18374p0;
    private ValueAnimator q;

    /* renamed from: t  reason: collision with root package name */
    private ValueAnimator f18375t;

    /* renamed from: w  reason: collision with root package name */
    private final int f18376w;

    /* renamed from: x  reason: collision with root package name */
    private int f18377x;

    /* renamed from: y  reason: collision with root package name */
    private int f18378y;

    /* renamed from: z  reason: collision with root package name */
    private int f18379z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SliderState extends View.BaseSavedState {
        public static final Parcelable.Creator<SliderState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        float f18380a;

        /* renamed from: b  reason: collision with root package name */
        float f18381b;

        /* renamed from: c  reason: collision with root package name */
        ArrayList<Float> f18382c;

        /* renamed from: d  reason: collision with root package name */
        float f18383d;

        /* renamed from: e  reason: collision with root package name */
        boolean f18384e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.Creator<SliderState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SliderState createFromParcel(Parcel parcel) {
                return new SliderState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SliderState[] newArray(int i8) {
                return new SliderState[i8];
            }
        }

        private SliderState(Parcel parcel) {
            super(parcel);
            this.f18380a = parcel.readFloat();
            this.f18381b = parcel.readFloat();
            ArrayList<Float> arrayList = new ArrayList<>();
            this.f18382c = arrayList;
            parcel.readList(arrayList, Float.class.getClassLoader());
            this.f18383d = parcel.readFloat();
            this.f18384e = parcel.createBooleanArray()[0];
        }

        /* synthetic */ SliderState(Parcel parcel, a aVar) {
            this(parcel);
        }

        SliderState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeFloat(this.f18380a);
            parcel.writeFloat(this.f18381b);
            parcel.writeList(this.f18382c);
            parcel.writeFloat(this.f18383d);
            parcel.writeBooleanArray(new boolean[]{this.f18384e});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ AttributeSet f18385a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f18386b;

        a(AttributeSet attributeSet, int i8) {
            this.f18385a = attributeSet;
            this.f18386b = i8;
        }

        @Override // com.google.android.material.slider.BaseSlider.f
        public z7.a a() {
            TypedArray h8 = m.h(BaseSlider.this.getContext(), this.f18385a, l.E6, this.f18386b, BaseSlider.f18344r0, new int[0]);
            z7.a R = BaseSlider.R(BaseSlider.this.getContext(), h8);
            h8.recycle();
            return R;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            for (z7.a aVar : BaseSlider.this.f18366l) {
                aVar.B0(floatValue);
            }
            c0.j0(BaseSlider.this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            for (z7.a aVar : BaseSlider.this.f18366l) {
                s.e(BaseSlider.this).b(aVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        int f18390a;

        private d() {
            this.f18390a = -1;
        }

        /* synthetic */ d(BaseSlider baseSlider, a aVar) {
            this();
        }

        void a(int i8) {
            this.f18390a = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseSlider.this.f18357g.W(this.f18390a, 4);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class e extends w0.a {
        private final BaseSlider<?, ?, ?> q;

        /* renamed from: r  reason: collision with root package name */
        Rect f18392r;

        e(BaseSlider<?, ?, ?> baseSlider) {
            super(baseSlider);
            this.f18392r = new Rect();
            this.q = baseSlider;
        }

        private String Y(int i8) {
            Context context;
            int i9;
            if (i8 == this.q.getValues().size() - 1) {
                context = this.q.getContext();
                i9 = j.f21213i;
            } else if (i8 != 0) {
                return BuildConfig.FLAVOR;
            } else {
                context = this.q.getContext();
                i9 = j.f21214j;
            }
            return context.getString(i9);
        }

        @Override // w0.a
        protected int B(float f5, float f8) {
            for (int i8 = 0; i8 < this.q.getValues().size(); i8++) {
                this.q.c0(i8, this.f18392r);
                if (this.f18392r.contains((int) f5, (int) f8)) {
                    return i8;
                }
            }
            return -1;
        }

        @Override // w0.a
        protected void C(List<Integer> list) {
            for (int i8 = 0; i8 < this.q.getValues().size(); i8++) {
                list.add(Integer.valueOf(i8));
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x002e, code lost:
            if (r4.q.a0(r5, r7.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) != false) goto L16;
         */
        @Override // w0.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected boolean L(int r5, int r6, android.os.Bundle r7) {
            /*
                r4 = this;
                com.google.android.material.slider.BaseSlider<?, ?, ?> r0 = r4.q
                boolean r0 = r0.isEnabled()
                r1 = 0
                if (r0 != 0) goto La
                return r1
            La:
                r0 = 4096(0x1000, float:5.74E-42)
                r2 = 1
                r3 = 8192(0x2000, float:1.14794E-41)
                if (r6 == r0) goto L3f
                if (r6 == r3) goto L3f
                r0 = 16908349(0x102003d, float:2.38774E-38)
                if (r6 == r0) goto L19
                return r1
            L19:
                if (r7 == 0) goto L3e
                java.lang.String r6 = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"
                boolean r0 = r7.containsKey(r6)
                if (r0 != 0) goto L24
                goto L3e
            L24:
                float r6 = r7.getFloat(r6)
                com.google.android.material.slider.BaseSlider<?, ?, ?> r7 = r4.q
                boolean r6 = com.google.android.material.slider.BaseSlider.e(r7, r5, r6)
                if (r6 == 0) goto L3e
            L30:
                com.google.android.material.slider.BaseSlider<?, ?, ?> r6 = r4.q
                com.google.android.material.slider.BaseSlider.f(r6)
                com.google.android.material.slider.BaseSlider<?, ?, ?> r6 = r4.q
                r6.postInvalidate()
                r4.E(r5)
                return r2
            L3e:
                return r1
            L3f:
                com.google.android.material.slider.BaseSlider<?, ?, ?> r7 = r4.q
                r0 = 20
                float r7 = com.google.android.material.slider.BaseSlider.g(r7, r0)
                if (r6 != r3) goto L4a
                float r7 = -r7
            L4a:
                com.google.android.material.slider.BaseSlider<?, ?, ?> r6 = r4.q
                boolean r6 = r6.F()
                if (r6 == 0) goto L53
                float r7 = -r7
            L53:
                com.google.android.material.slider.BaseSlider<?, ?, ?> r6 = r4.q
                java.util.List r6 = r6.getValues()
                java.lang.Object r6 = r6.get(r5)
                java.lang.Float r6 = (java.lang.Float) r6
                float r6 = r6.floatValue()
                float r6 = r6 + r7
                com.google.android.material.slider.BaseSlider<?, ?, ?> r7 = r4.q
                float r7 = r7.getValueFrom()
                com.google.android.material.slider.BaseSlider<?, ?, ?> r0 = r4.q
                float r0 = r0.getValueTo()
                float r6 = t0.a.b(r6, r7, r0)
                com.google.android.material.slider.BaseSlider<?, ?, ?> r7 = r4.q
                boolean r6 = com.google.android.material.slider.BaseSlider.e(r7, r5, r6)
                if (r6 == 0) goto L7d
                goto L30
            L7d:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.slider.BaseSlider.e.L(int, int, android.os.Bundle):boolean");
        }

        @Override // w0.a
        protected void P(int i8, androidx.core.view.accessibility.c cVar) {
            cVar.b(c.a.L);
            List<Float> values = this.q.getValues();
            float floatValue = values.get(i8).floatValue();
            float valueFrom = this.q.getValueFrom();
            float valueTo = this.q.getValueTo();
            if (this.q.isEnabled()) {
                if (floatValue > valueFrom) {
                    cVar.a(8192);
                }
                if (floatValue < valueTo) {
                    cVar.a(RecognitionOptions.AZTEC);
                }
            }
            cVar.v0(c.d.a(1, valueFrom, valueTo, floatValue));
            cVar.c0(SeekBar.class.getName());
            StringBuilder sb = new StringBuilder();
            if (this.q.getContentDescription() != null) {
                sb.append(this.q.getContentDescription());
                sb.append(",");
            }
            if (values.size() > 1) {
                sb.append(Y(i8));
                sb.append(this.q.y(floatValue));
            }
            cVar.g0(sb.toString());
            this.q.c0(i8, this.f18392r);
            cVar.X(this.f18392r);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        z7.a a();
    }

    public BaseSlider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.N);
    }

    public BaseSlider(Context context, AttributeSet attributeSet, int i8) {
        super(y7.a.c(context, attributeSet, i8, f18344r0), attributeSet, i8);
        this.f18366l = new ArrayList();
        this.f18368m = new ArrayList();
        this.f18370n = new ArrayList();
        this.f18373p = false;
        this.P = false;
        this.T = new ArrayList<>();
        this.W = -1;
        this.f18346a0 = -1;
        this.f18348b0 = 0.0f;
        this.f18352d0 = true;
        this.f18358g0 = false;
        h hVar = new h();
        this.f18371n0 = hVar;
        this.f18374p0 = 0;
        Context context2 = getContext();
        Paint paint = new Paint();
        this.f18345a = paint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.f18347b = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        Paint paint3 = new Paint(1);
        this.f18349c = paint3;
        paint3.setStyle(Paint.Style.FILL);
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Paint paint4 = new Paint(1);
        this.f18351d = paint4;
        paint4.setStyle(Paint.Style.FILL);
        Paint paint5 = new Paint();
        this.f18353e = paint5;
        paint5.setStyle(Paint.Style.STROKE);
        paint5.setStrokeCap(Paint.Cap.ROUND);
        Paint paint6 = new Paint();
        this.f18355f = paint6;
        paint6.setStyle(Paint.Style.STROKE);
        paint6.setStrokeCap(Paint.Cap.ROUND);
        G(context2.getResources());
        this.f18364k = new a(attributeSet, i8);
        U(context2, attributeSet, i8);
        setFocusable(true);
        setClickable(true);
        hVar.i0(2);
        this.f18376w = ViewConfiguration.get(context2).getScaledTouchSlop();
        e eVar = new e(this);
        this.f18357g = eVar;
        c0.t0(this, eVar);
        this.f18359h = (AccessibilityManager) getContext().getSystemService("accessibility");
    }

    private float A(int i8, float f5) {
        float minSeparation = this.f18348b0 == 0.0f ? getMinSeparation() : 0.0f;
        if (this.f18374p0 == 0) {
            minSeparation = p(minSeparation);
        }
        if (F()) {
            minSeparation = -minSeparation;
        }
        int i9 = i8 + 1;
        int i10 = i8 - 1;
        return t0.a.b(f5, i10 < 0 ? this.Q : this.T.get(i10).floatValue() + minSeparation, i9 >= this.T.size() ? this.R : this.T.get(i9).floatValue() - minSeparation);
    }

    private int B(ColorStateList colorStateList) {
        return colorStateList.getColorForState(getDrawableState(), colorStateList.getDefaultColor());
    }

    private void D() {
        this.f18345a.setStrokeWidth(this.B);
        this.f18347b.setStrokeWidth(this.B);
        this.f18353e.setStrokeWidth(this.B / 2.0f);
        this.f18355f.setStrokeWidth(this.B / 2.0f);
    }

    private boolean E() {
        ViewParent parent = getParent();
        while (true) {
            boolean z4 = false;
            if (!(parent instanceof ViewGroup)) {
                return false;
            }
            ViewGroup viewGroup = (ViewGroup) parent;
            if (viewGroup.canScrollVertically(1) || viewGroup.canScrollVertically(-1)) {
                z4 = true;
            }
            if (z4 && viewGroup.shouldDelayChildPressedState()) {
                return true;
            }
            parent = parent.getParent();
        }
    }

    private void G(Resources resources) {
        this.f18379z = resources.getDimensionPixelSize(k7.d.f21126s0);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(k7.d.f21122q0);
        this.f18377x = dimensionPixelOffset;
        this.C = dimensionPixelOffset;
        this.f18378y = resources.getDimensionPixelSize(k7.d.f21121p0);
        this.E = resources.getDimensionPixelOffset(k7.d.f21124r0);
        this.H = resources.getDimensionPixelSize(k7.d.f21119o0);
    }

    private void H() {
        if (this.f18348b0 <= 0.0f) {
            return;
        }
        f0();
        int min = Math.min((int) (((this.R - this.Q) / this.f18348b0) + 1.0f), (this.f18354e0 / (this.B * 2)) + 1);
        float[] fArr = this.f18350c0;
        if (fArr == null || fArr.length != min * 2) {
            this.f18350c0 = new float[min * 2];
        }
        float f5 = this.f18354e0 / (min - 1);
        for (int i8 = 0; i8 < min * 2; i8 += 2) {
            float[] fArr2 = this.f18350c0;
            fArr2[i8] = this.C + ((i8 / 2) * f5);
            fArr2[i8 + 1] = l();
        }
    }

    private void I(Canvas canvas, int i8, int i9) {
        if (X()) {
            int N = (int) (this.C + (N(this.T.get(this.f18346a0).floatValue()) * i8));
            if (Build.VERSION.SDK_INT < 28) {
                int i10 = this.G;
                canvas.clipRect(N - i10, i9 - i10, N + i10, i10 + i9, Region.Op.UNION);
            }
            canvas.drawCircle(N, i9, this.G, this.f18351d);
        }
    }

    private void J(Canvas canvas) {
        if (!this.f18352d0 || this.f18348b0 <= 0.0f) {
            return;
        }
        float[] activeRange = getActiveRange();
        int T = T(this.f18350c0, activeRange[0]);
        int T2 = T(this.f18350c0, activeRange[1]);
        int i8 = T * 2;
        canvas.drawPoints(this.f18350c0, 0, i8, this.f18353e);
        int i9 = T2 * 2;
        canvas.drawPoints(this.f18350c0, i8, i9 - i8, this.f18355f);
        float[] fArr = this.f18350c0;
        canvas.drawPoints(fArr, i9, fArr.length - i9, this.f18353e);
    }

    private void K() {
        this.C = this.f18377x + Math.max(this.F - this.f18378y, 0);
        if (c0.W(this)) {
            e0(getWidth());
        }
    }

    private boolean L(int i8) {
        int i9 = this.f18346a0;
        int d8 = (int) t0.a.d(i9 + i8, 0L, this.T.size() - 1);
        this.f18346a0 = d8;
        if (d8 == i9) {
            return false;
        }
        if (this.W != -1) {
            this.W = d8;
        }
        d0();
        postInvalidate();
        return true;
    }

    private boolean M(int i8) {
        if (F()) {
            i8 = i8 == Integer.MIN_VALUE ? Integer.MAX_VALUE : -i8;
        }
        return L(i8);
    }

    private float N(float f5) {
        float f8 = this.Q;
        float f9 = (f5 - f8) / (this.R - f8);
        return F() ? 1.0f - f9 : f9;
    }

    private Boolean O(int i8, KeyEvent keyEvent) {
        if (i8 == 61) {
            return keyEvent.hasNoModifiers() ? Boolean.valueOf(L(1)) : keyEvent.isShiftPressed() ? Boolean.valueOf(L(-1)) : Boolean.FALSE;
        }
        if (i8 != 66) {
            if (i8 != 81) {
                if (i8 == 69) {
                    L(-1);
                    return Boolean.TRUE;
                } else if (i8 != 70) {
                    switch (i8) {
                        case 21:
                            M(-1);
                            return Boolean.TRUE;
                        case 22:
                            M(1);
                            return Boolean.TRUE;
                        case 23:
                            break;
                        default:
                            return null;
                    }
                }
            }
            L(1);
            return Boolean.TRUE;
        }
        this.W = this.f18346a0;
        postInvalidate();
        return Boolean.TRUE;
    }

    private void P() {
        for (T t8 : this.f18370n) {
            t8.a(this);
        }
    }

    private void Q() {
        for (T t8 : this.f18370n) {
            t8.b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static z7.a R(Context context, TypedArray typedArray) {
        return z7.a.u0(context, null, 0, typedArray.getResourceId(l.M6, k.L));
    }

    private static int T(float[] fArr, float f5) {
        return Math.round(f5 * ((fArr.length / 2) - 1));
    }

    private void U(Context context, AttributeSet attributeSet, int i8) {
        TypedArray h8 = m.h(context, attributeSet, l.E6, i8, f18344r0, new int[0]);
        this.Q = h8.getFloat(l.H6, 0.0f);
        this.R = h8.getFloat(l.I6, 1.0f);
        setValues(Float.valueOf(this.Q));
        this.f18348b0 = h8.getFloat(l.G6, 0.0f);
        int i9 = l.W6;
        boolean hasValue = h8.hasValue(i9);
        int i10 = hasValue ? i9 : l.Y6;
        if (!hasValue) {
            i9 = l.X6;
        }
        ColorStateList a9 = u7.c.a(context, h8, i10);
        if (a9 == null) {
            a9 = h.a.a(context, k7.c.f21084k);
        }
        setTrackInactiveTintList(a9);
        ColorStateList a10 = u7.c.a(context, h8, i9);
        if (a10 == null) {
            a10 = h.a.a(context, k7.c.f21081h);
        }
        setTrackActiveTintList(a10);
        this.f18371n0.a0(u7.c.a(context, h8, l.N6));
        int i11 = l.Q6;
        if (h8.hasValue(i11)) {
            setThumbStrokeColor(u7.c.a(context, h8, i11));
        }
        setThumbStrokeWidth(h8.getDimension(l.R6, 0.0f));
        ColorStateList a11 = u7.c.a(context, h8, l.J6);
        if (a11 == null) {
            a11 = h.a.a(context, k7.c.f21082i);
        }
        setHaloTintList(a11);
        this.f18352d0 = h8.getBoolean(l.V6, true);
        int i12 = l.S6;
        boolean hasValue2 = h8.hasValue(i12);
        int i13 = hasValue2 ? i12 : l.U6;
        if (!hasValue2) {
            i12 = l.T6;
        }
        ColorStateList a12 = u7.c.a(context, h8, i13);
        if (a12 == null) {
            a12 = h.a.a(context, k7.c.f21083j);
        }
        setTickInactiveTintList(a12);
        ColorStateList a13 = u7.c.a(context, h8, i12);
        if (a13 == null) {
            a13 = h.a.a(context, k7.c.f21080g);
        }
        setTickActiveTintList(a13);
        setThumbRadius(h8.getDimensionPixelSize(l.P6, 0));
        setHaloRadius(h8.getDimensionPixelSize(l.K6, 0));
        setThumbElevation(h8.getDimension(l.O6, 0.0f));
        setTrackHeight(h8.getDimensionPixelSize(l.Z6, 0));
        this.A = h8.getInt(l.L6, 0);
        if (!h8.getBoolean(l.F6, true)) {
            setEnabled(false);
        }
        h8.recycle();
    }

    private void V(int i8) {
        BaseSlider<S, L, T>.d dVar = this.f18362j;
        if (dVar == null) {
            this.f18362j = new d(this, null);
        } else {
            removeCallbacks(dVar);
        }
        this.f18362j.a(i8);
        postDelayed(this.f18362j, 200L);
    }

    private void W(z7.a aVar, float f5) {
        aVar.C0(y(f5));
        int N = (this.C + ((int) (N(f5) * this.f18354e0))) - (aVar.getIntrinsicWidth() / 2);
        int l8 = l() - (this.H + this.F);
        aVar.setBounds(N, l8 - aVar.getIntrinsicHeight(), aVar.getIntrinsicWidth() + N, l8);
        Rect rect = new Rect(aVar.getBounds());
        com.google.android.material.internal.c.c(s.d(this), this, rect);
        aVar.setBounds(rect);
        s.e(this).a(aVar);
    }

    private boolean X() {
        return this.f18356f0 || Build.VERSION.SDK_INT < 21 || !(getBackground() instanceof RippleDrawable);
    }

    private boolean Y(float f5) {
        return a0(this.W, f5);
    }

    private double Z(float f5) {
        float f8 = this.f18348b0;
        if (f8 > 0.0f) {
            int i8 = (int) ((this.R - this.Q) / f8);
            return Math.round(f5 * i8) / i8;
        }
        return f5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a0(int i8, float f5) {
        if (Math.abs(f5 - this.T.get(i8).floatValue()) < 1.0E-4d) {
            return false;
        }
        this.T.set(i8, Float.valueOf(A(i8, f5)));
        this.f18346a0 = i8;
        q(i8);
        return true;
    }

    private boolean b0() {
        return Y(getValueOfTouchPosition());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d0() {
        if (X() || getMeasuredWidth() <= 0) {
            return;
        }
        Drawable background = getBackground();
        if (background instanceof RippleDrawable) {
            int N = (int) ((N(this.T.get(this.f18346a0).floatValue()) * this.f18354e0) + this.C);
            int l8 = l();
            int i8 = this.G;
            androidx.core.graphics.drawable.a.l(background, N - i8, l8 - i8, N + i8, l8 + i8);
        }
    }

    private void e0(int i8) {
        this.f18354e0 = Math.max(i8 - (this.C * 2), 0);
        H();
    }

    private void f0() {
        if (this.f18360h0) {
            h0();
            i0();
            g0();
            j0();
            m0();
            this.f18360h0 = false;
        }
    }

    private void g0() {
        if (this.f18348b0 > 0.0f && !k0(this.R)) {
            throw new IllegalStateException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", Float.toString(this.f18348b0), Float.toString(this.Q), Float.toString(this.R)));
        }
    }

    private float[] getActiveRange() {
        float floatValue = ((Float) Collections.max(getValues())).floatValue();
        float floatValue2 = ((Float) Collections.min(getValues())).floatValue();
        if (this.T.size() == 1) {
            floatValue2 = this.Q;
        }
        float N = N(floatValue2);
        float N2 = N(floatValue);
        return F() ? new float[]{N2, N} : new float[]{N, N2};
    }

    private float getValueOfTouchPosition() {
        double Z = Z(this.f18372o0);
        if (F()) {
            Z = 1.0d - Z;
        }
        float f5 = this.R;
        float f8 = this.Q;
        return (float) ((Z * (f5 - f8)) + f8);
    }

    private float getValueOfTouchPositionAbsolute() {
        float f5 = this.f18372o0;
        if (F()) {
            f5 = 1.0f - f5;
        }
        float f8 = this.R;
        float f9 = this.Q;
        return (f5 * (f8 - f9)) + f9;
    }

    private void h(z7.a aVar) {
        aVar.A0(s.d(this));
    }

    private void h0() {
        if (this.Q >= this.R) {
            throw new IllegalStateException(String.format("valueFrom(%s) must be smaller than valueTo(%s)", Float.toString(this.Q), Float.toString(this.R)));
        }
    }

    private Float i(int i8) {
        float k8 = this.f18358g0 ? k(20) : j();
        if (i8 == 21) {
            if (!F()) {
                k8 = -k8;
            }
            return Float.valueOf(k8);
        } else if (i8 == 22) {
            if (F()) {
                k8 = -k8;
            }
            return Float.valueOf(k8);
        } else if (i8 != 69) {
            if (i8 == 70 || i8 == 81) {
                return Float.valueOf(k8);
            }
            return null;
        } else {
            return Float.valueOf(-k8);
        }
    }

    private void i0() {
        if (this.R <= this.Q) {
            throw new IllegalStateException(String.format("valueTo(%s) must be greater than valueFrom(%s)", Float.toString(this.R), Float.toString(this.Q)));
        }
    }

    private float j() {
        float f5 = this.f18348b0;
        if (f5 == 0.0f) {
            return 1.0f;
        }
        return f5;
    }

    private void j0() {
        Iterator<Float> it = this.T.iterator();
        while (it.hasNext()) {
            Float next = it.next();
            if (next.floatValue() < this.Q || next.floatValue() > this.R) {
                throw new IllegalStateException(String.format("Slider value(%s) must be greater or equal to valueFrom(%s), and lower or equal to valueTo(%s)", Float.toString(next.floatValue()), Float.toString(this.Q), Float.toString(this.R)));
            }
            if (this.f18348b0 > 0.0f && !k0(next.floatValue())) {
                throw new IllegalStateException(String.format("Value(%s) must be equal to valueFrom(%s) plus a multiple of stepSize(%s) when using stepSize(%s)", Float.toString(next.floatValue()), Float.toString(this.Q), Float.toString(this.f18348b0), Float.toString(this.f18348b0)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float k(int i8) {
        float f5;
        float f8;
        float j8 = j();
        return (this.R - this.Q) / j8 <= i8 ? j8 : Math.round(f5 / f8) * j8;
    }

    private boolean k0(float f5) {
        double doubleValue = new BigDecimal(Float.toString(f5)).subtract(new BigDecimal(Float.toString(this.Q))).divide(new BigDecimal(Float.toString(this.f18348b0)), MathContext.DECIMAL64).doubleValue();
        return Math.abs(((double) Math.round(doubleValue)) - doubleValue) < 1.0E-4d;
    }

    private int l() {
        return this.E + (this.A == 1 ? this.f18366l.get(0).getIntrinsicHeight() : 0);
    }

    private float l0(float f5) {
        return (N(f5) * this.f18354e0) + this.C;
    }

    private ValueAnimator m(boolean z4) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(z(z4 ? this.f18375t : this.q, z4 ? 0.0f : 1.0f), z4 ? 1.0f : 0.0f);
        ofFloat.setDuration(z4 ? 83L : 117L);
        ofFloat.setInterpolator(z4 ? l7.a.f21790e : l7.a.f21788c);
        ofFloat.addUpdateListener(new b());
        return ofFloat;
    }

    private void m0() {
        float f5 = this.f18348b0;
        if (f5 == 0.0f) {
            return;
        }
        if (((int) f5) != f5) {
            Log.w(f18343q0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", "stepSize", Float.valueOf(f5)));
        }
        float f8 = this.Q;
        if (((int) f8) != f8) {
            Log.w(f18343q0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", "valueFrom", Float.valueOf(f8)));
        }
        float f9 = this.R;
        if (((int) f9) != f9) {
            Log.w(f18343q0, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", "valueTo", Float.valueOf(f9)));
        }
    }

    private void n() {
        if (this.f18366l.size() > this.T.size()) {
            List<z7.a> subList = this.f18366l.subList(this.T.size(), this.f18366l.size());
            for (z7.a aVar : subList) {
                if (c0.V(this)) {
                    o(aVar);
                }
            }
            subList.clear();
        }
        while (this.f18366l.size() < this.T.size()) {
            z7.a a9 = this.f18364k.a();
            this.f18366l.add(a9);
            if (c0.V(this)) {
                h(a9);
            }
        }
        int i8 = this.f18366l.size() == 1 ? 0 : 1;
        for (z7.a aVar2 : this.f18366l) {
            aVar2.m0(i8);
        }
    }

    private void o(z7.a aVar) {
        r e8 = s.e(this);
        if (e8 != null) {
            e8.b(aVar);
            aVar.w0(s.d(this));
        }
    }

    private float p(float f5) {
        if (f5 == 0.0f) {
            return 0.0f;
        }
        float f8 = (f5 - this.C) / this.f18354e0;
        float f9 = this.Q;
        return (f8 * (f9 - this.R)) + f9;
    }

    private void q(int i8) {
        for (L l8 : this.f18368m) {
            l8.a(this, this.T.get(i8).floatValue(), true);
        }
        AccessibilityManager accessibilityManager = this.f18359h;
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return;
        }
        V(i8);
    }

    private void r() {
        for (L l8 : this.f18368m) {
            Iterator<Float> it = this.T.iterator();
            while (it.hasNext()) {
                l8.a(this, it.next().floatValue(), false);
            }
        }
    }

    private void s(Canvas canvas, int i8, int i9) {
        float[] activeRange = getActiveRange();
        int i10 = this.C;
        float f5 = i8;
        float f8 = i9;
        canvas.drawLine(i10 + (activeRange[0] * f5), f8, i10 + (activeRange[1] * f5), f8, this.f18347b);
    }

    private void setValuesInternal(ArrayList<Float> arrayList) {
        if (arrayList.isEmpty()) {
            throw new IllegalArgumentException("At least one value must be set");
        }
        Collections.sort(arrayList);
        if (this.T.size() == arrayList.size() && this.T.equals(arrayList)) {
            return;
        }
        this.T = arrayList;
        this.f18360h0 = true;
        this.f18346a0 = 0;
        d0();
        n();
        r();
        postInvalidate();
    }

    private void t(Canvas canvas, int i8, int i9) {
        int i10;
        float[] activeRange = getActiveRange();
        float f5 = i8;
        float f8 = this.C + (activeRange[1] * f5);
        if (f8 < i10 + i8) {
            float f9 = i9;
            canvas.drawLine(f8, f9, i10 + i8, f9, this.f18345a);
        }
        int i11 = this.C;
        float f10 = i11 + (activeRange[0] * f5);
        if (f10 > i11) {
            float f11 = i9;
            canvas.drawLine(i11, f11, f10, f11, this.f18345a);
        }
    }

    private void u(Canvas canvas, int i8, int i9) {
        if (!isEnabled()) {
            Iterator<Float> it = this.T.iterator();
            while (it.hasNext()) {
                canvas.drawCircle(this.C + (N(it.next().floatValue()) * i8), i9, this.F, this.f18349c);
            }
        }
        Iterator<Float> it2 = this.T.iterator();
        while (it2.hasNext()) {
            canvas.save();
            int N = this.C + ((int) (N(it2.next().floatValue()) * i8));
            int i10 = this.F;
            canvas.translate(N - i10, i9 - i10);
            this.f18371n0.draw(canvas);
            canvas.restore();
        }
    }

    private void v() {
        if (this.A == 2) {
            return;
        }
        if (!this.f18373p) {
            this.f18373p = true;
            ValueAnimator m8 = m(true);
            this.q = m8;
            this.f18375t = null;
            m8.start();
        }
        Iterator<z7.a> it = this.f18366l.iterator();
        for (int i8 = 0; i8 < this.T.size() && it.hasNext(); i8++) {
            if (i8 != this.f18346a0) {
                W(it.next(), this.T.get(i8).floatValue());
            }
        }
        if (!it.hasNext()) {
            throw new IllegalStateException(String.format("Not enough labels(%d) to display all the values(%d)", Integer.valueOf(this.f18366l.size()), Integer.valueOf(this.T.size())));
        }
        W(it.next(), this.T.get(this.f18346a0).floatValue());
    }

    private void w() {
        if (this.f18373p) {
            this.f18373p = false;
            ValueAnimator m8 = m(false);
            this.f18375t = m8;
            this.q = null;
            m8.addListener(new c());
            this.f18375t.start();
        }
    }

    private void x(int i8) {
        if (i8 == 1) {
            L(Integer.MAX_VALUE);
        } else if (i8 == 2) {
            L(Integer.MIN_VALUE);
        } else if (i8 == 17) {
            M(Integer.MAX_VALUE);
        } else if (i8 != 66) {
        } else {
            M(Integer.MIN_VALUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String y(float f5) {
        if (C()) {
            return this.O.a(f5);
        }
        return String.format(((float) ((int) f5)) == f5 ? "%.0f" : "%.2f", Float.valueOf(f5));
    }

    private static float z(ValueAnimator valueAnimator, float f5) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return f5;
        }
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        valueAnimator.cancel();
        return floatValue;
    }

    public boolean C() {
        return this.O != null;
    }

    final boolean F() {
        return c0.E(this) == 1;
    }

    protected boolean S() {
        if (this.W != -1) {
            return true;
        }
        float valueOfTouchPositionAbsolute = getValueOfTouchPositionAbsolute();
        float l02 = l0(valueOfTouchPositionAbsolute);
        this.W = 0;
        float abs = Math.abs(this.T.get(0).floatValue() - valueOfTouchPositionAbsolute);
        for (int i8 = 1; i8 < this.T.size(); i8++) {
            float abs2 = Math.abs(this.T.get(i8).floatValue() - valueOfTouchPositionAbsolute);
            float l03 = l0(this.T.get(i8).floatValue());
            if (Float.compare(abs2, abs) > 1) {
                break;
            }
            boolean z4 = !F() ? l03 - l02 >= 0.0f : l03 - l02 <= 0.0f;
            if (Float.compare(abs2, abs) >= 0) {
                if (Float.compare(abs2, abs) != 0) {
                    continue;
                } else if (Math.abs(l03 - l02) < this.f18376w) {
                    this.W = -1;
                    return false;
                } else if (!z4) {
                }
            }
            this.W = i8;
            abs = abs2;
        }
        return this.W != -1;
    }

    void c0(int i8, Rect rect) {
        int N = this.C + ((int) (N(getValues().get(i8).floatValue()) * this.f18354e0));
        int l8 = l();
        int i9 = this.F;
        rect.set(N - i9, l8 - i9, N + i9, l8 + i9);
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.f18357g.v(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.f18345a.setColor(B(this.f18369m0));
        this.f18347b.setColor(B(this.f18367l0));
        this.f18353e.setColor(B(this.f18365k0));
        this.f18355f.setColor(B(this.f18363j0));
        for (z7.a aVar : this.f18366l) {
            if (aVar.isStateful()) {
                aVar.setState(getDrawableState());
            }
        }
        if (this.f18371n0.isStateful()) {
            this.f18371n0.setState(getDrawableState());
        }
        this.f18351d.setColor(B(this.f18361i0));
        this.f18351d.setAlpha(63);
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    final int getAccessibilityFocusedVirtualViewId() {
        return this.f18357g.x();
    }

    public int getActiveThumbIndex() {
        return this.W;
    }

    public int getFocusedThumbIndex() {
        return this.f18346a0;
    }

    public int getHaloRadius() {
        return this.G;
    }

    public ColorStateList getHaloTintList() {
        return this.f18361i0;
    }

    public int getLabelBehavior() {
        return this.A;
    }

    protected float getMinSeparation() {
        return 0.0f;
    }

    public float getStepSize() {
        return this.f18348b0;
    }

    public float getThumbElevation() {
        return this.f18371n0.w();
    }

    public int getThumbRadius() {
        return this.F;
    }

    public ColorStateList getThumbStrokeColor() {
        return this.f18371n0.E();
    }

    public float getThumbStrokeWidth() {
        return this.f18371n0.G();
    }

    public ColorStateList getThumbTintList() {
        return this.f18371n0.x();
    }

    public ColorStateList getTickActiveTintList() {
        return this.f18363j0;
    }

    public ColorStateList getTickInactiveTintList() {
        return this.f18365k0;
    }

    public ColorStateList getTickTintList() {
        if (this.f18365k0.equals(this.f18363j0)) {
            return this.f18363j0;
        }
        throw new IllegalStateException("The inactive and active ticks are different colors. Use the getTickColorInactive() and getTickColorActive() methods instead.");
    }

    public ColorStateList getTrackActiveTintList() {
        return this.f18367l0;
    }

    public int getTrackHeight() {
        return this.B;
    }

    public ColorStateList getTrackInactiveTintList() {
        return this.f18369m0;
    }

    public int getTrackSidePadding() {
        return this.C;
    }

    public ColorStateList getTrackTintList() {
        if (this.f18369m0.equals(this.f18367l0)) {
            return this.f18367l0;
        }
        throw new IllegalStateException("The inactive and active parts of the track are different colors. Use the getInactiveTrackColor() and getActiveTrackColor() methods instead.");
    }

    public int getTrackWidth() {
        return this.f18354e0;
    }

    public float getValueFrom() {
        return this.Q;
    }

    public float getValueTo() {
        return this.R;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Float> getValues() {
        return new ArrayList(this.T);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (z7.a aVar : this.f18366l) {
            h(aVar);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        BaseSlider<S, L, T>.d dVar = this.f18362j;
        if (dVar != null) {
            removeCallbacks(dVar);
        }
        this.f18373p = false;
        for (z7.a aVar : this.f18366l) {
            o(aVar);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f18360h0) {
            f0();
            H();
        }
        super.onDraw(canvas);
        int l8 = l();
        t(canvas, this.f18354e0, l8);
        if (((Float) Collections.max(getValues())).floatValue() > this.Q) {
            s(canvas, this.f18354e0, l8);
        }
        J(canvas);
        if ((this.P || isFocused()) && isEnabled()) {
            I(canvas, this.f18354e0, l8);
            if (this.W != -1) {
                v();
            }
        }
        u(canvas, this.f18354e0, l8);
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean z4, int i8, Rect rect) {
        super.onFocusChanged(z4, i8, rect);
        if (z4) {
            x(i8);
            this.f18357g.V(this.f18346a0);
            return;
        }
        this.W = -1;
        w();
        this.f18357g.o(this.f18346a0);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i8, KeyEvent keyEvent) {
        if (isEnabled()) {
            if (this.T.size() == 1) {
                this.W = 0;
            }
            if (this.W == -1) {
                Boolean O = O(i8, keyEvent);
                return O != null ? O.booleanValue() : super.onKeyDown(i8, keyEvent);
            }
            this.f18358g0 |= keyEvent.isLongPress();
            Float i9 = i(i8);
            if (i9 != null) {
                if (Y(this.T.get(this.W).floatValue() + i9.floatValue())) {
                    d0();
                    postInvalidate();
                }
                return true;
            }
            if (i8 != 23) {
                if (i8 == 61) {
                    if (keyEvent.hasNoModifiers()) {
                        return L(1);
                    }
                    if (keyEvent.isShiftPressed()) {
                        return L(-1);
                    }
                    return false;
                } else if (i8 != 66) {
                    return super.onKeyDown(i8, keyEvent);
                }
            }
            this.W = -1;
            w();
            postInvalidate();
            return true;
        }
        return super.onKeyDown(i8, keyEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i8, KeyEvent keyEvent) {
        this.f18358g0 = false;
        return super.onKeyUp(i8, keyEvent);
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, View.MeasureSpec.makeMeasureSpec(this.f18379z + (this.A == 1 ? this.f18366l.get(0).getIntrinsicHeight() : 0), 1073741824));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SliderState sliderState = (SliderState) parcelable;
        super.onRestoreInstanceState(sliderState.getSuperState());
        this.Q = sliderState.f18380a;
        this.R = sliderState.f18381b;
        setValuesInternal(sliderState.f18382c);
        this.f18348b0 = sliderState.f18383d;
        if (sliderState.f18384e) {
            requestFocus();
        }
        r();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SliderState sliderState = new SliderState(super.onSaveInstanceState());
        sliderState.f18380a = this.Q;
        sliderState.f18381b = this.R;
        sliderState.f18382c = new ArrayList<>(this.T);
        sliderState.f18383d = this.f18348b0;
        sliderState.f18384e = hasFocus();
        return sliderState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        e0(i8);
        d0();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnabled()) {
            float x8 = motionEvent.getX();
            float f5 = (x8 - this.C) / this.f18354e0;
            this.f18372o0 = f5;
            float max = Math.max(0.0f, f5);
            this.f18372o0 = max;
            this.f18372o0 = Math.min(1.0f, max);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked == 1) {
                    this.P = false;
                    MotionEvent motionEvent2 = this.L;
                    if (motionEvent2 != null && motionEvent2.getActionMasked() == 0 && Math.abs(this.L.getX() - motionEvent.getX()) <= this.f18376w && Math.abs(this.L.getY() - motionEvent.getY()) <= this.f18376w && S()) {
                        P();
                    }
                    if (this.W != -1) {
                        b0();
                        this.W = -1;
                        Q();
                    }
                    w();
                } else if (actionMasked == 2) {
                    if (!this.P) {
                        if (E() && Math.abs(x8 - this.K) < this.f18376w) {
                            return false;
                        }
                        getParent().requestDisallowInterceptTouchEvent(true);
                        P();
                    }
                    if (S()) {
                        this.P = true;
                        b0();
                        d0();
                    }
                }
                invalidate();
            } else {
                this.K = x8;
                if (!E()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (S()) {
                        requestFocus();
                        this.P = true;
                        b0();
                        d0();
                        invalidate();
                        P();
                    }
                }
            }
            setPressed(this.P);
            this.L = MotionEvent.obtain(motionEvent);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setActiveThumbIndex(int i8) {
        this.W = i8;
    }

    @Override // android.view.View
    public void setEnabled(boolean z4) {
        super.setEnabled(z4);
        setLayerType(z4 ? 0 : 2, null);
    }

    public void setFocusedThumbIndex(int i8) {
        if (i8 < 0 || i8 >= this.T.size()) {
            throw new IllegalArgumentException("index out of range");
        }
        this.f18346a0 = i8;
        this.f18357g.V(i8);
        postInvalidate();
    }

    public void setHaloRadius(int i8) {
        if (i8 == this.G) {
            return;
        }
        this.G = i8;
        Drawable background = getBackground();
        if (X() || !(background instanceof RippleDrawable)) {
            postInvalidate();
        } else {
            p7.a.a((RippleDrawable) background, this.G);
        }
    }

    public void setHaloRadiusResource(int i8) {
        setHaloRadius(getResources().getDimensionPixelSize(i8));
    }

    public void setHaloTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18361i0)) {
            return;
        }
        this.f18361i0 = colorStateList;
        Drawable background = getBackground();
        if (!X() && (background instanceof RippleDrawable)) {
            ((RippleDrawable) background).setColor(colorStateList);
            return;
        }
        this.f18351d.setColor(B(colorStateList));
        this.f18351d.setAlpha(63);
        invalidate();
    }

    public void setLabelBehavior(int i8) {
        if (this.A != i8) {
            this.A = i8;
            requestLayout();
        }
    }

    public void setLabelFormatter(com.google.android.material.slider.c cVar) {
        this.O = cVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSeparationUnit(int i8) {
        this.f18374p0 = i8;
    }

    public void setStepSize(float f5) {
        if (f5 < 0.0f) {
            throw new IllegalArgumentException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", Float.toString(f5), Float.toString(this.Q), Float.toString(this.R)));
        }
        if (this.f18348b0 != f5) {
            this.f18348b0 = f5;
            this.f18360h0 = true;
            postInvalidate();
        }
    }

    public void setThumbElevation(float f5) {
        this.f18371n0.Z(f5);
    }

    public void setThumbElevationResource(int i8) {
        setThumbElevation(getResources().getDimension(i8));
    }

    public void setThumbRadius(int i8) {
        if (i8 == this.F) {
            return;
        }
        this.F = i8;
        K();
        this.f18371n0.setShapeAppearanceModel(x7.m.a().q(0, this.F).m());
        h hVar = this.f18371n0;
        int i9 = this.F;
        hVar.setBounds(0, 0, i9 * 2, i9 * 2);
        postInvalidate();
    }

    public void setThumbRadiusResource(int i8) {
        setThumbRadius(getResources().getDimensionPixelSize(i8));
    }

    public void setThumbStrokeColor(ColorStateList colorStateList) {
        this.f18371n0.l0(colorStateList);
        postInvalidate();
    }

    public void setThumbStrokeColorResource(int i8) {
        if (i8 != 0) {
            setThumbStrokeColor(h.a.a(getContext(), i8));
        }
    }

    public void setThumbStrokeWidth(float f5) {
        this.f18371n0.m0(f5);
        postInvalidate();
    }

    public void setThumbStrokeWidthResource(int i8) {
        if (i8 != 0) {
            setThumbStrokeWidth(getResources().getDimension(i8));
        }
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18371n0.x())) {
            return;
        }
        this.f18371n0.a0(colorStateList);
        invalidate();
    }

    public void setTickActiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18363j0)) {
            return;
        }
        this.f18363j0 = colorStateList;
        this.f18355f.setColor(B(colorStateList));
        invalidate();
    }

    public void setTickInactiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18365k0)) {
            return;
        }
        this.f18365k0 = colorStateList;
        this.f18353e.setColor(B(colorStateList));
        invalidate();
    }

    public void setTickTintList(ColorStateList colorStateList) {
        setTickInactiveTintList(colorStateList);
        setTickActiveTintList(colorStateList);
    }

    public void setTickVisible(boolean z4) {
        if (this.f18352d0 != z4) {
            this.f18352d0 = z4;
            postInvalidate();
        }
    }

    public void setTrackActiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18367l0)) {
            return;
        }
        this.f18367l0 = colorStateList;
        this.f18347b.setColor(B(colorStateList));
        invalidate();
    }

    public void setTrackHeight(int i8) {
        if (this.B != i8) {
            this.B = i8;
            D();
            postInvalidate();
        }
    }

    public void setTrackInactiveTintList(ColorStateList colorStateList) {
        if (colorStateList.equals(this.f18369m0)) {
            return;
        }
        this.f18369m0 = colorStateList;
        this.f18345a.setColor(B(colorStateList));
        invalidate();
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        setTrackInactiveTintList(colorStateList);
        setTrackActiveTintList(colorStateList);
    }

    public void setValueFrom(float f5) {
        this.Q = f5;
        this.f18360h0 = true;
        postInvalidate();
    }

    public void setValueTo(float f5) {
        this.R = f5;
        this.f18360h0 = true;
        postInvalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValues(List<Float> list) {
        setValuesInternal(new ArrayList<>(list));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValues(Float... fArr) {
        ArrayList<Float> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, fArr);
        setValuesInternal(arrayList);
    }
}
