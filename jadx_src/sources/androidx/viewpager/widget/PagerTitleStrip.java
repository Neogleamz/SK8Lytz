package androidx.viewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.core.widget.k;
import androidx.viewpager.widget.ViewPager;
import com.example.seedpoint.R;
import java.lang.ref.WeakReference;
import java.util.Locale;
@ViewPager.e
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class PagerTitleStrip extends ViewGroup {
    private static final int[] q = {16842804, 16842901, 16842904, 16842927};

    /* renamed from: t  reason: collision with root package name */
    private static final int[] f7753t = {16843660};

    /* renamed from: a  reason: collision with root package name */
    ViewPager f7754a;

    /* renamed from: b  reason: collision with root package name */
    TextView f7755b;

    /* renamed from: c  reason: collision with root package name */
    TextView f7756c;

    /* renamed from: d  reason: collision with root package name */
    TextView f7757d;

    /* renamed from: e  reason: collision with root package name */
    private int f7758e;

    /* renamed from: f  reason: collision with root package name */
    float f7759f;

    /* renamed from: g  reason: collision with root package name */
    private int f7760g;

    /* renamed from: h  reason: collision with root package name */
    private int f7761h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f7762j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f7763k;

    /* renamed from: l  reason: collision with root package name */
    private final a f7764l;

    /* renamed from: m  reason: collision with root package name */
    private WeakReference<androidx.viewpager.widget.a> f7765m;

    /* renamed from: n  reason: collision with root package name */
    private int f7766n;

    /* renamed from: p  reason: collision with root package name */
    int f7767p;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends DataSetObserver implements ViewPager.i, ViewPager.h {

        /* renamed from: a  reason: collision with root package name */
        private int f7768a;

        a() {
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void a(int i8) {
            if (this.f7768a == 0) {
                PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
                pagerTitleStrip.c(pagerTitleStrip.f7754a.getCurrentItem(), PagerTitleStrip.this.f7754a.getAdapter());
                PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
                float f5 = pagerTitleStrip2.f7759f;
                if (f5 < 0.0f) {
                    f5 = 0.0f;
                }
                pagerTitleStrip2.d(pagerTitleStrip2.f7754a.getCurrentItem(), f5, true);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void b(int i8, float f5, int i9) {
            if (f5 > 0.5f) {
                i8++;
            }
            PagerTitleStrip.this.d(i8, f5, false);
        }

        @Override // androidx.viewpager.widget.ViewPager.h
        public void c(ViewPager viewPager, androidx.viewpager.widget.a aVar, androidx.viewpager.widget.a aVar2) {
            PagerTitleStrip.this.b(aVar, aVar2);
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void d(int i8) {
            this.f7768a = i8;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
            pagerTitleStrip.c(pagerTitleStrip.f7754a.getCurrentItem(), PagerTitleStrip.this.f7754a.getAdapter());
            PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
            float f5 = pagerTitleStrip2.f7759f;
            if (f5 < 0.0f) {
                f5 = 0.0f;
            }
            pagerTitleStrip2.d(pagerTitleStrip2.f7754a.getCurrentItem(), f5, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends SingleLineTransformationMethod {

        /* renamed from: a  reason: collision with root package name */
        private Locale f7770a;

        b(Context context) {
            this.f7770a = context.getResources().getConfiguration().locale;
        }

        @Override // android.text.method.ReplacementTransformationMethod, android.text.method.TransformationMethod
        public CharSequence getTransformation(CharSequence charSequence, View view) {
            CharSequence transformation = super.getTransformation(charSequence, view);
            if (transformation != null) {
                return transformation.toString().toUpperCase(this.f7770a);
            }
            return null;
        }
    }

    public PagerTitleStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7758e = -1;
        this.f7759f = -1.0f;
        this.f7764l = new a();
        TextView textView = new TextView(context);
        this.f7755b = textView;
        addView(textView);
        TextView textView2 = new TextView(context);
        this.f7756c = textView2;
        addView(textView2);
        TextView textView3 = new TextView(context);
        this.f7757d = textView3;
        addView(textView3);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q);
        boolean z4 = false;
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId != 0) {
            k.q(this.f7755b, resourceId);
            k.q(this.f7756c, resourceId);
            k.q(this.f7757d, resourceId);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        if (dimensionPixelSize != 0) {
            a(0, dimensionPixelSize);
        }
        if (obtainStyledAttributes.hasValue(2)) {
            int color = obtainStyledAttributes.getColor(2, 0);
            this.f7755b.setTextColor(color);
            this.f7756c.setTextColor(color);
            this.f7757d.setTextColor(color);
        }
        this.f7761h = obtainStyledAttributes.getInteger(3, 80);
        obtainStyledAttributes.recycle();
        this.f7767p = this.f7756c.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(0.6f);
        this.f7755b.setEllipsize(TextUtils.TruncateAt.END);
        this.f7756c.setEllipsize(TextUtils.TruncateAt.END);
        this.f7757d.setEllipsize(TextUtils.TruncateAt.END);
        if (resourceId != 0) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, f7753t);
            z4 = obtainStyledAttributes2.getBoolean(0, false);
            obtainStyledAttributes2.recycle();
        }
        TextView textView4 = this.f7755b;
        if (z4) {
            setSingleLineAllCaps(textView4);
            setSingleLineAllCaps(this.f7756c);
            setSingleLineAllCaps(this.f7757d);
        } else {
            textView4.setSingleLine();
            this.f7756c.setSingleLine();
            this.f7757d.setSingleLine();
        }
        this.f7760g = (int) (context.getResources().getDisplayMetrics().density * 16.0f);
    }

    private static void setSingleLineAllCaps(TextView textView) {
        textView.setTransformationMethod(new b(textView.getContext()));
    }

    public void a(int i8, float f5) {
        this.f7755b.setTextSize(i8, f5);
        this.f7756c.setTextSize(i8, f5);
        this.f7757d.setTextSize(i8, f5);
    }

    void b(androidx.viewpager.widget.a aVar, androidx.viewpager.widget.a aVar2) {
        if (aVar != null) {
            aVar.u(this.f7764l);
            this.f7765m = null;
        }
        if (aVar2 != null) {
            aVar2.m(this.f7764l);
            this.f7765m = new WeakReference<>(aVar2);
        }
        ViewPager viewPager = this.f7754a;
        if (viewPager != null) {
            this.f7758e = -1;
            this.f7759f = -1.0f;
            c(viewPager.getCurrentItem(), aVar2);
            requestLayout();
        }
    }

    void c(int i8, androidx.viewpager.widget.a aVar) {
        int e8 = aVar != null ? aVar.e() : 0;
        this.f7762j = true;
        CharSequence charSequence = null;
        this.f7755b.setText((i8 < 1 || aVar == null) ? null : aVar.g(i8 - 1));
        this.f7756c.setText((aVar == null || i8 >= e8) ? null : aVar.g(i8));
        int i9 = i8 + 1;
        if (i9 < e8 && aVar != null) {
            charSequence = aVar.g(i9);
        }
        this.f7757d.setText(charSequence);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(0, (int) (((getWidth() - getPaddingLeft()) - getPaddingRight()) * 0.8f)), Integer.MIN_VALUE);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(Math.max(0, (getHeight() - getPaddingTop()) - getPaddingBottom()), Integer.MIN_VALUE);
        this.f7755b.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f7756c.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f7757d.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f7758e = i8;
        if (!this.f7763k) {
            d(i8, this.f7759f, false);
        }
        this.f7762j = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i8, float f5, boolean z4) {
        int i9;
        int i10;
        int i11;
        int i12;
        if (i8 != this.f7758e) {
            c(i8, this.f7754a.getAdapter());
        } else if (!z4 && f5 == this.f7759f) {
            return;
        }
        this.f7763k = true;
        int measuredWidth = this.f7755b.getMeasuredWidth();
        int measuredWidth2 = this.f7756c.getMeasuredWidth();
        int measuredWidth3 = this.f7757d.getMeasuredWidth();
        int i13 = measuredWidth2 / 2;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i14 = paddingRight + i13;
        int i15 = (width - (paddingLeft + i13)) - i14;
        float f8 = 0.5f + f5;
        if (f8 > 1.0f) {
            f8 -= 1.0f;
        }
        int i16 = ((width - i14) - ((int) (i15 * f8))) - i13;
        int i17 = measuredWidth2 + i16;
        int baseline = this.f7755b.getBaseline();
        int baseline2 = this.f7756c.getBaseline();
        int baseline3 = this.f7757d.getBaseline();
        int max = Math.max(Math.max(baseline, baseline2), baseline3);
        int i18 = max - baseline;
        int i19 = max - baseline2;
        int i20 = max - baseline3;
        int max2 = Math.max(Math.max(this.f7755b.getMeasuredHeight() + i18, this.f7756c.getMeasuredHeight() + i19), this.f7757d.getMeasuredHeight() + i20);
        int i21 = this.f7761h & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        if (i21 == 16) {
            i9 = (((height - paddingTop) - paddingBottom) - max2) / 2;
        } else if (i21 != 80) {
            i10 = i18 + paddingTop;
            i11 = i19 + paddingTop;
            i12 = paddingTop + i20;
            TextView textView = this.f7756c;
            textView.layout(i16, i11, i17, textView.getMeasuredHeight() + i11);
            int min = Math.min(paddingLeft, (i16 - this.f7760g) - measuredWidth);
            TextView textView2 = this.f7755b;
            textView2.layout(min, i10, measuredWidth + min, textView2.getMeasuredHeight() + i10);
            int max3 = Math.max((width - paddingRight) - measuredWidth3, i17 + this.f7760g);
            TextView textView3 = this.f7757d;
            textView3.layout(max3, i12, max3 + measuredWidth3, textView3.getMeasuredHeight() + i12);
            this.f7759f = f5;
            this.f7763k = false;
        } else {
            i9 = (height - paddingBottom) - max2;
        }
        i10 = i18 + i9;
        i11 = i19 + i9;
        i12 = i9 + i20;
        TextView textView4 = this.f7756c;
        textView4.layout(i16, i11, i17, textView4.getMeasuredHeight() + i11);
        int min2 = Math.min(paddingLeft, (i16 - this.f7760g) - measuredWidth);
        TextView textView22 = this.f7755b;
        textView22.layout(min2, i10, measuredWidth + min2, textView22.getMeasuredHeight() + i10);
        int max32 = Math.max((width - paddingRight) - measuredWidth3, i17 + this.f7760g);
        TextView textView32 = this.f7757d;
        textView32.layout(max32, i12, max32 + measuredWidth3, textView32.getMeasuredHeight() + i12);
        this.f7759f = f5;
        this.f7763k = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinHeight() {
        Drawable background = getBackground();
        if (background != null) {
            return background.getIntrinsicHeight();
        }
        return 0;
    }

    public int getTextSpacing() {
        return this.f7760g;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (!(parent instanceof ViewPager)) {
            throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
        }
        ViewPager viewPager = (ViewPager) parent;
        androidx.viewpager.widget.a adapter = viewPager.getAdapter();
        viewPager.Q(this.f7764l);
        viewPager.b(this.f7764l);
        this.f7754a = viewPager;
        WeakReference<androidx.viewpager.widget.a> weakReference = this.f7765m;
        b(weakReference != null ? weakReference.get() : null, adapter);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewPager viewPager = this.f7754a;
        if (viewPager != null) {
            b(viewPager.getAdapter(), null);
            this.f7754a.Q(null);
            this.f7754a.I(this.f7764l);
            this.f7754a = null;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        if (this.f7754a != null) {
            float f5 = this.f7759f;
            if (f5 < 0.0f) {
                f5 = 0.0f;
            }
            d(this.f7758e, f5, true);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int max;
        if (View.MeasureSpec.getMode(i8) != 1073741824) {
            throw new IllegalStateException("Must measure with an exact width");
        }
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i9, paddingTop, -2);
        int size = View.MeasureSpec.getSize(i8);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i8, (int) (size * 0.2f), -2);
        this.f7755b.measure(childMeasureSpec2, childMeasureSpec);
        this.f7756c.measure(childMeasureSpec2, childMeasureSpec);
        this.f7757d.measure(childMeasureSpec2, childMeasureSpec);
        if (View.MeasureSpec.getMode(i9) == 1073741824) {
            max = View.MeasureSpec.getSize(i9);
        } else {
            max = Math.max(getMinHeight(), this.f7756c.getMeasuredHeight() + paddingTop);
        }
        setMeasuredDimension(size, View.resolveSizeAndState(max, i9, this.f7756c.getMeasuredState() << 16));
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.f7762j) {
            return;
        }
        super.requestLayout();
    }

    public void setGravity(int i8) {
        this.f7761h = i8;
        requestLayout();
    }

    public void setNonPrimaryAlpha(float f5) {
        int i8 = ((int) (f5 * 255.0f)) & 255;
        this.f7766n = i8;
        int i9 = (i8 << 24) | (this.f7767p & 16777215);
        this.f7755b.setTextColor(i9);
        this.f7757d.setTextColor(i9);
    }

    public void setTextColor(int i8) {
        this.f7767p = i8;
        this.f7756c.setTextColor(i8);
        int i9 = (this.f7766n << 24) | (this.f7767p & 16777215);
        this.f7755b.setTextColor(i9);
        this.f7757d.setTextColor(i9);
    }

    public void setTextSpacing(int i8) {
        this.f7760g = i8;
        requestLayout();
    }
}
