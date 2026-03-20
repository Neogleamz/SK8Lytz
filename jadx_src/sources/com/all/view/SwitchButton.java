package com.all.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import rc.g7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SwitchButton extends CompoundButton {

    /* renamed from: m0  reason: collision with root package name */
    private static int[] f8652m0 = {16842912, 16842910, 16842919};

    /* renamed from: n0  reason: collision with root package name */
    private static int[] f8653n0 = {-16842912, 16842910, 16842919};
    private RectF A;
    private RectF B;
    private RectF C;
    private RectF E;
    private Paint F;
    private boolean G;
    private boolean H;
    private boolean K;
    private ObjectAnimator L;
    private float O;
    private RectF P;
    private float Q;
    private float R;
    private float T;
    private int W;

    /* renamed from: a  reason: collision with root package name */
    private Drawable f8654a;

    /* renamed from: a0  reason: collision with root package name */
    private int f8655a0;

    /* renamed from: b  reason: collision with root package name */
    private Drawable f8656b;

    /* renamed from: b0  reason: collision with root package name */
    private Paint f8657b0;

    /* renamed from: c  reason: collision with root package name */
    private ColorStateList f8658c;

    /* renamed from: c0  reason: collision with root package name */
    private CharSequence f8659c0;

    /* renamed from: d  reason: collision with root package name */
    private ColorStateList f8660d;

    /* renamed from: d0  reason: collision with root package name */
    private CharSequence f8661d0;

    /* renamed from: e  reason: collision with root package name */
    private float f8662e;

    /* renamed from: e0  reason: collision with root package name */
    private TextPaint f8663e0;

    /* renamed from: f  reason: collision with root package name */
    private float f8664f;

    /* renamed from: f0  reason: collision with root package name */
    private Layout f8665f0;

    /* renamed from: g  reason: collision with root package name */
    private RectF f8666g;

    /* renamed from: g0  reason: collision with root package name */
    private Layout f8667g0;

    /* renamed from: h  reason: collision with root package name */
    private float f8668h;

    /* renamed from: h0  reason: collision with root package name */
    private float f8669h0;

    /* renamed from: i0  reason: collision with root package name */
    private float f8670i0;

    /* renamed from: j  reason: collision with root package name */
    private long f8671j;

    /* renamed from: j0  reason: collision with root package name */
    private float f8672j0;

    /* renamed from: k  reason: collision with root package name */
    private boolean f8673k;

    /* renamed from: k0  reason: collision with root package name */
    private Context f8674k0;

    /* renamed from: l  reason: collision with root package name */
    private int f8675l;

    /* renamed from: l0  reason: collision with root package name */
    private CompoundButton.OnCheckedChangeListener f8676l0;

    /* renamed from: m  reason: collision with root package name */
    private PointF f8677m;

    /* renamed from: n  reason: collision with root package name */
    private int f8678n;

    /* renamed from: p  reason: collision with root package name */
    private int f8679p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f8680t;

    /* renamed from: w  reason: collision with root package name */
    private int f8681w;

    /* renamed from: x  reason: collision with root package name */
    private Drawable f8682x;

    /* renamed from: y  reason: collision with root package name */
    private Drawable f8683y;

    /* renamed from: z  reason: collision with root package name */
    private RectF f8684z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        CharSequence f8685a;

        /* renamed from: b  reason: collision with root package name */
        CharSequence f8686b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f8685a = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f8686b = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            TextUtils.writeToParcel(this.f8685a, parcel, i8);
            TextUtils.writeToParcel(this.f8686b, parcel, i8);
        }
    }

    public SwitchButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.K = false;
        this.f8674k0 = context;
        f(attributeSet);
    }

    public SwitchButton(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.K = false;
        this.f8674k0 = context;
        f(attributeSet);
    }

    private void b() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private int c(double d8) {
        return (int) Math.ceil(d8);
    }

    private ColorStateList d(int i8) {
        int i9 = i8 - (-805306368);
        return new ColorStateList(new int[][]{new int[]{-16842910, 16842912}, new int[]{-16842910}, new int[]{16842912, 16842919}, new int[]{-16842912, 16842919}, new int[]{16842912}, new int[]{-16842912}}, new int[]{i8 - (-520093696), 268435456, i9, 536870912, i9, 536870912});
    }

    private ColorStateList e(int i8) {
        int i9 = i8 - (-1728053248);
        return new ColorStateList(new int[][]{new int[]{-16842910, 16842912}, new int[]{-16842910}, new int[]{16842919, -16842912}, new int[]{16842919, 16842912}, new int[]{16842912}, new int[]{-16842912}}, new int[]{i8 - (-1442840576), -4539718, i9, i9, i8 | (-16777216), -1118482});
    }

    private void f(AttributeSet attributeSet) {
        float f5;
        int i8;
        float f8;
        float f9;
        float f10;
        String str;
        Drawable drawable;
        ColorStateList colorStateList;
        Drawable drawable2;
        ColorStateList colorStateList2;
        float f11;
        int i9;
        float f12;
        float f13;
        float f14;
        boolean z4;
        this.W = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.f8655a0 = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        this.F = new Paint(1);
        Paint paint = new Paint(1);
        this.f8657b0 = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.f8657b0.setStrokeWidth(getResources().getDisplayMetrics().density);
        this.f8663e0 = getPaint();
        this.f8684z = new RectF();
        this.A = new RectF();
        this.B = new RectF();
        this.f8677m = new PointF();
        this.f8666g = new RectF();
        this.C = new RectF();
        this.E = new RectF();
        ObjectAnimator duration = ObjectAnimator.ofFloat(this, "process", 0.0f, 0.0f).setDuration(250L);
        this.L = duration;
        duration.setInterpolator(new AccelerateDecelerateInterpolator());
        this.P = new RectF();
        float f15 = getResources().getDisplayMetrics().density;
        float f16 = f15 * 2.0f;
        float f17 = f15 * 20.0f;
        float f18 = f17 / 2.0f;
        String str2 = null;
        TypedArray obtainStyledAttributes = attributeSet == null ? null : getContext().obtainStyledAttributes(attributeSet, g7.V1);
        if (obtainStyledAttributes != null) {
            Drawable drawable3 = obtainStyledAttributes.getDrawable(10);
            ColorStateList colorStateList3 = obtainStyledAttributes.getColorStateList(9);
            float dimension = obtainStyledAttributes.getDimension(12, f16);
            float dimension2 = obtainStyledAttributes.getDimension(14, dimension);
            float dimension3 = obtainStyledAttributes.getDimension(15, dimension);
            float dimension4 = obtainStyledAttributes.getDimension(16, dimension);
            float dimension5 = obtainStyledAttributes.getDimension(13, dimension);
            float dimension6 = obtainStyledAttributes.getDimension(18, f17);
            float dimension7 = obtainStyledAttributes.getDimension(11, f17);
            float dimension8 = obtainStyledAttributes.getDimension(17, Math.min(dimension6, dimension7) / 2.0f);
            float dimension9 = obtainStyledAttributes.getDimension(4, dimension8 + f16);
            Drawable drawable4 = obtainStyledAttributes.getDrawable(2);
            colorStateList2 = obtainStyledAttributes.getColorStateList(1);
            float f19 = obtainStyledAttributes.getFloat(3, 1.8f);
            int integer = obtainStyledAttributes.getInteger(0, 250);
            boolean z8 = obtainStyledAttributes.getBoolean(5, true);
            int color = obtainStyledAttributes.getColor(19, Integer.MIN_VALUE);
            String string = obtainStyledAttributes.getString(8);
            String string2 = obtainStyledAttributes.getString(7);
            f16 = obtainStyledAttributes.getDimension(6, f16);
            obtainStyledAttributes.recycle();
            f8 = dimension7;
            f11 = dimension3;
            f17 = dimension6;
            i8 = integer;
            drawable2 = drawable4;
            f10 = dimension8;
            f13 = dimension4;
            f9 = dimension9;
            f5 = f19;
            z4 = z8;
            str = string2;
            drawable = drawable3;
            str2 = string;
            i9 = color;
            colorStateList = colorStateList3;
            f12 = dimension2;
            f14 = dimension5;
        } else {
            f5 = 1.8f;
            i8 = 250;
            f8 = f17;
            f9 = f18;
            f10 = f9;
            str = null;
            drawable = null;
            colorStateList = null;
            drawable2 = null;
            colorStateList2 = null;
            f11 = 0.0f;
            i9 = Integer.MIN_VALUE;
            f12 = 0.0f;
            f13 = 0.0f;
            f14 = 0.0f;
            z4 = true;
        }
        this.f8659c0 = str2;
        this.f8661d0 = str;
        this.f8672j0 = f16;
        this.f8654a = drawable;
        this.f8660d = colorStateList;
        boolean z9 = drawable != null;
        this.G = z9;
        this.f8675l = i9;
        if (i9 == Integer.MIN_VALUE) {
            this.f8675l = 3908335;
        }
        if (!z9 && colorStateList == null) {
            ColorStateList e8 = e(this.f8675l);
            this.f8660d = e8;
            this.f8678n = e8.getDefaultColor();
        }
        if (this.G) {
            f17 = Math.max(f17, this.f8654a.getMinimumWidth());
            f8 = Math.max(f8, this.f8654a.getMinimumHeight());
        }
        this.f8677m.set(f17, f8);
        this.f8656b = drawable2;
        this.f8658c = colorStateList2;
        boolean z10 = drawable2 != null;
        this.H = z10;
        if (!z10 && colorStateList2 == null) {
            ColorStateList d8 = d(this.f8675l);
            this.f8658c = d8;
            int defaultColor = d8.getDefaultColor();
            this.f8679p = defaultColor;
            this.q = this.f8658c.getColorForState(f8652m0, defaultColor);
        }
        this.f8666g.set(f12, f13, f11, f14);
        if (this.f8666g.width() >= 0.0f) {
            f5 = Math.max(f5, 1.0f);
        }
        this.f8668h = f5;
        this.f8662e = f10;
        this.f8664f = f9;
        long j8 = i8;
        this.f8671j = j8;
        this.f8673k = z4;
        this.L.setDuration(j8);
        setFocusable(true);
        setClickable(true);
        if (isChecked()) {
            setProcess(1.0f);
        }
    }

    private Layout g(CharSequence charSequence) {
        TextPaint textPaint = this.f8663e0;
        return new StaticLayout(charSequence, textPaint, (int) Math.ceil(Layout.getDesiredWidth(charSequence, textPaint)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    private boolean getStatusBasedOnPos() {
        return getProcess() > 0.5f;
    }

    private int h(int i8) {
        int mode = View.MeasureSpec.getMode(i8);
        int size = View.MeasureSpec.getSize(i8);
        float f5 = this.f8677m.y;
        RectF rectF = this.f8666g;
        int c9 = c(Math.max(f5, rectF.top + f5 + rectF.right));
        Layout layout = this.f8665f0;
        float height = layout != null ? layout.getHeight() : 0.0f;
        Layout layout2 = this.f8667g0;
        float height2 = layout2 != null ? layout2.getHeight() : 0.0f;
        if (height != 0.0f || height2 != 0.0f) {
            float max = Math.max(height, height2);
            this.f8670i0 = max;
            c9 = c(Math.max(c9, max));
        }
        int max2 = Math.max(c9, getSuggestedMinimumHeight());
        int max3 = Math.max(max2, getPaddingTop() + max2 + getPaddingBottom());
        return mode == 1073741824 ? Math.max(max3, size) : mode == Integer.MIN_VALUE ? Math.min(max3, size) : max3;
    }

    private int i(int i8) {
        Layout layout;
        Layout layout2;
        int size = View.MeasureSpec.getSize(i8);
        int mode = View.MeasureSpec.getMode(i8);
        int c9 = c(this.f8677m.x * this.f8668h);
        if (this.H) {
            c9 = Math.max(c9, this.f8656b.getMinimumWidth());
        }
        float width = this.f8665f0 != null ? layout.getWidth() : 0.0f;
        float width2 = this.f8667g0 != null ? layout2.getWidth() : 0.0f;
        if (width != 0.0f || width2 != 0.0f) {
            float max = Math.max(width, width2) + (this.f8672j0 * 2.0f);
            this.f8669h0 = max;
            float f5 = c9;
            float f8 = f5 - this.f8677m.x;
            if (f8 < max) {
                c9 = (int) (f5 + (max - f8));
            }
        }
        RectF rectF = this.f8666g;
        int max2 = Math.max(c9, c(c9 + rectF.left + rectF.right));
        int max3 = Math.max(Math.max(max2, getPaddingLeft() + max2 + getPaddingRight()), getSuggestedMinimumWidth());
        return mode == 1073741824 ? Math.max(max3, size) : mode == Integer.MIN_VALUE ? Math.min(max3, size) : max3;
    }

    private void k() {
        float paddingTop = getPaddingTop() + Math.max(0.0f, this.f8666g.top);
        float paddingLeft = getPaddingLeft() + Math.max(0.0f, this.f8666g.left);
        if (this.f8665f0 != null && this.f8667g0 != null) {
            RectF rectF = this.f8666g;
            if (rectF.top + rectF.bottom > 0.0f) {
                float measuredHeight = ((getMeasuredHeight() - getPaddingBottom()) - getPaddingTop()) - this.f8677m.y;
                RectF rectF2 = this.f8666g;
                paddingTop += ((measuredHeight - rectF2.top) - rectF2.bottom) / 2.0f;
            }
        }
        if (this.G) {
            PointF pointF = this.f8677m;
            pointF.x = Math.max(pointF.x, this.f8654a.getMinimumWidth());
            PointF pointF2 = this.f8677m;
            pointF2.y = Math.max(pointF2.y, this.f8654a.getMinimumHeight());
        }
        RectF rectF3 = this.f8684z;
        PointF pointF3 = this.f8677m;
        rectF3.set(paddingLeft, paddingTop, pointF3.x + paddingLeft, pointF3.y + paddingTop);
        float f5 = this.f8684z.left - this.f8666g.left;
        float f8 = this.f8677m.x;
        float min = Math.min(0.0f, ((Math.max(this.f8668h * f8, f8 + this.f8669h0) - this.f8684z.width()) - this.f8669h0) / 2.0f);
        float height = this.f8684z.height();
        RectF rectF4 = this.f8666g;
        float min2 = Math.min(0.0f, (((height + rectF4.top) + rectF4.bottom) - this.f8670i0) / 2.0f);
        float f9 = this.f8684z.top;
        RectF rectF5 = this.f8666g;
        float f10 = f5 + rectF5.left;
        float f11 = this.f8677m.x;
        float max = f10 + Math.max(this.f8668h * f11, f11 + this.f8669h0);
        RectF rectF6 = this.f8666g;
        this.A.set(f5 + min, (f9 - rectF5.top) + min2, (max + rectF6.right) - min, (this.f8684z.bottom + rectF6.bottom) - min2);
        RectF rectF7 = this.B;
        RectF rectF8 = this.f8684z;
        rectF7.set(rectF8.left, 0.0f, (this.A.right - this.f8666g.right) - rectF8.width(), 0.0f);
        this.f8664f = Math.min(Math.min(this.A.width(), this.A.height()) / 2.0f, this.f8664f);
        Drawable drawable = this.f8656b;
        if (drawable != null) {
            RectF rectF9 = this.A;
            drawable.setBounds((int) rectF9.left, (int) rectF9.top, c(rectF9.right), c(this.A.bottom));
        }
        if (this.f8665f0 != null) {
            RectF rectF10 = this.A;
            float width = rectF10.left + (((rectF10.width() - this.f8684z.width()) - this.f8665f0.getWidth()) / 2.0f);
            float f12 = this.f8666g.left;
            float f13 = (width - f12) + (this.f8672j0 * (f12 > 0.0f ? 1 : -1));
            RectF rectF11 = this.A;
            float height2 = rectF11.top + ((rectF11.height() - this.f8665f0.getHeight()) / 2.0f);
            this.C.set(f13, height2, this.f8665f0.getWidth() + f13, this.f8665f0.getHeight() + height2);
        }
        if (this.f8667g0 != null) {
            RectF rectF12 = this.A;
            float width2 = (((rectF12.right - (((rectF12.width() - this.f8684z.width()) - this.f8667g0.getWidth()) / 2.0f)) + this.f8666g.right) - this.f8667g0.getWidth()) - (this.f8672j0 * (this.f8666g.right <= 0.0f ? -1 : 1));
            RectF rectF13 = this.A;
            float height3 = rectF13.top + ((rectF13.height() - this.f8667g0.getHeight()) / 2.0f);
            this.E.set(width2, height3, this.f8667g0.getWidth() + width2, this.f8667g0.getHeight() + height3);
        }
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    protected void a(boolean z4) {
        ObjectAnimator objectAnimator = this.L;
        if (objectAnimator == null) {
            return;
        }
        if (objectAnimator.isRunning()) {
            this.L.cancel();
        }
        this.L.setDuration(this.f8671j);
        if (z4) {
            this.L.setFloatValues(this.O, 1.0f);
        } else {
            this.L.setFloatValues(this.O, 0.0f);
        }
        this.L.start();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        Drawable drawable;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        super.drawableStateChanged();
        if (this.G || (colorStateList2 = this.f8660d) == null) {
            setDrawableState(this.f8654a);
        } else {
            this.f8678n = colorStateList2.getColorForState(getDrawableState(), this.f8678n);
        }
        int[] iArr = isChecked() ? f8653n0 : f8652m0;
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            int defaultColor = textColors.getDefaultColor();
            this.f8680t = textColors.getColorForState(f8652m0, defaultColor);
            this.f8681w = textColors.getColorForState(f8653n0, defaultColor);
        }
        if (!this.H && (colorStateList = this.f8658c) != null) {
            int colorForState = colorStateList.getColorForState(getDrawableState(), this.f8679p);
            this.f8679p = colorForState;
            this.q = this.f8658c.getColorForState(iArr, colorForState);
            return;
        }
        Drawable drawable2 = this.f8656b;
        if ((drawable2 instanceof StateListDrawable) && this.f8673k) {
            drawable2.setState(iArr);
            drawable = this.f8656b.getCurrent().mutate();
        } else {
            drawable = null;
        }
        this.f8683y = drawable;
        setDrawableState(this.f8656b);
        Drawable drawable3 = this.f8656b;
        if (drawable3 != null) {
            this.f8682x = drawable3.getCurrent().mutate();
        }
    }

    public final float getProcess() {
        return this.O;
    }

    public void j(CharSequence charSequence, CharSequence charSequence2) {
        this.f8659c0 = charSequence;
        this.f8661d0 = charSequence2;
        this.f8665f0 = null;
        this.f8667g0 = null;
        requestLayout();
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0124  */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r15) {
        /*
            Method dump skipped, instructions count: 482
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.all.view.SwitchButton.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i8, int i9) {
        CharSequence charSequence;
        CharSequence charSequence2;
        if (this.f8665f0 == null && (charSequence2 = this.f8659c0) != null) {
            this.f8665f0 = g(charSequence2);
        }
        if (this.f8667g0 == null && (charSequence = this.f8661d0) != null) {
            this.f8667g0 = g(charSequence);
        }
        setMeasuredDimension(i(i8), h(i9));
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        j(savedState.f8685a, savedState.f8686b);
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f8685a = this.f8659c0;
        savedState.f8686b = this.f8661d0;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        if (i8 == i10 && i9 == i11) {
            return;
        }
        k();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r0 != 3) goto L12;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            boolean r0 = r9.isEnabled()
            r1 = 0
            if (r0 == 0) goto L97
            boolean r0 = r9.isClickable()
            if (r0 != 0) goto Lf
            goto L97
        Lf:
            int r0 = r10.getAction()
            float r2 = r10.getX()
            float r3 = r9.Q
            float r2 = r2 - r3
            float r3 = r10.getY()
            float r4 = r9.R
            float r3 = r3 - r4
            r4 = 1
            if (r0 == 0) goto L80
            if (r0 == r4) goto L47
            r5 = 2
            if (r0 == r5) goto L2d
            r5 = 3
            if (r0 == r5) goto L47
            goto L96
        L2d:
            float r10 = r10.getX()
            float r0 = r9.getProcess()
            float r1 = r9.T
            float r1 = r10 - r1
            android.graphics.RectF r2 = r9.B
            float r2 = r2.width()
            float r1 = r1 / r2
            float r0 = r0 + r1
            r9.setProcess(r0)
            r9.T = r10
            goto L96
        L47:
            r9.setPressed(r1)
            boolean r0 = r9.getStatusBasedOnPos()
            long r5 = r10.getEventTime()
            long r7 = r10.getDownTime()
            long r5 = r5 - r7
            float r10 = (float) r5
            int r5 = r9.W
            float r6 = (float) r5
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 >= 0) goto L6f
            float r2 = (float) r5
            int r2 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r2 >= 0) goto L6f
            int r2 = r9.f8655a0
            float r2 = (float) r2
            int r10 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r10 >= 0) goto L6f
            r9.performClick()
            goto L96
        L6f:
            boolean r10 = r9.isChecked()
            if (r0 == r10) goto L7c
            r9.playSoundEffect(r1)
            r9.setChecked(r0)
            goto L96
        L7c:
            r9.a(r0)
            goto L96
        L80:
            r9.b()
            float r0 = r10.getX()
            r9.Q = r0
            float r10 = r10.getY()
            r9.R = r10
            float r10 = r9.Q
            r9.T = r10
            r9.setPressed(r4)
        L96:
            return r4
        L97:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.all.view.SwitchButton.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.CompoundButton, android.view.View
    public boolean performClick() {
        return super.performClick();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z4) {
        if (isChecked() != z4) {
            a(z4);
        }
        super.setChecked(z4);
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        this.f8676l0 = onCheckedChangeListener;
    }

    public final void setProcess(float f5) {
        if (f5 > 1.0f) {
            f5 = 1.0f;
        } else if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        this.O = f5;
        invalidate();
    }
}
