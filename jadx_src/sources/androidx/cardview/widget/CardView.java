package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CardView extends FrameLayout {

    /* renamed from: h  reason: collision with root package name */
    private static final int[] f3068h = {16842801};

    /* renamed from: j  reason: collision with root package name */
    private static final e f3069j;

    /* renamed from: a  reason: collision with root package name */
    private boolean f3070a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f3071b;

    /* renamed from: c  reason: collision with root package name */
    int f3072c;

    /* renamed from: d  reason: collision with root package name */
    int f3073d;

    /* renamed from: e  reason: collision with root package name */
    final Rect f3074e;

    /* renamed from: f  reason: collision with root package name */
    final Rect f3075f;

    /* renamed from: g  reason: collision with root package name */
    private final d f3076g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements d {

        /* renamed from: a  reason: collision with root package name */
        private Drawable f3077a;

        a() {
        }

        @Override // androidx.cardview.widget.d
        public void a(int i8, int i9, int i10, int i11) {
            CardView.this.f3075f.set(i8, i9, i10, i11);
            CardView cardView = CardView.this;
            Rect rect = cardView.f3074e;
            CardView.super.setPadding(i8 + rect.left, i9 + rect.top, i10 + rect.right, i11 + rect.bottom);
        }

        @Override // androidx.cardview.widget.d
        public void b(int i8, int i9) {
            CardView cardView = CardView.this;
            if (i8 > cardView.f3072c) {
                CardView.super.setMinimumWidth(i8);
            }
            CardView cardView2 = CardView.this;
            if (i9 > cardView2.f3073d) {
                CardView.super.setMinimumHeight(i9);
            }
        }

        @Override // androidx.cardview.widget.d
        public void c(Drawable drawable) {
            this.f3077a = drawable;
            CardView.this.setBackgroundDrawable(drawable);
        }

        @Override // androidx.cardview.widget.d
        public boolean d() {
            return CardView.this.getPreventCornerOverlap();
        }

        @Override // androidx.cardview.widget.d
        public boolean e() {
            return CardView.this.getUseCompatPadding();
        }

        @Override // androidx.cardview.widget.d
        public Drawable f() {
            return this.f3077a;
        }

        @Override // androidx.cardview.widget.d
        public View g() {
            return CardView.this;
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f3069j = i8 >= 21 ? new b() : i8 >= 17 ? new androidx.cardview.widget.a() : new c();
        f3069j.j();
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, j0.a.f20587a);
    }

    public CardView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        Resources resources;
        int i9;
        ColorStateList valueOf;
        Rect rect = new Rect();
        this.f3074e = rect;
        this.f3075f = new Rect();
        a aVar = new a();
        this.f3076g = aVar;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, j0.e.f20594a, i8, j0.d.f20593a);
        int i10 = j0.e.f20597d;
        if (obtainStyledAttributes.hasValue(i10)) {
            valueOf = obtainStyledAttributes.getColorStateList(i10);
        } else {
            TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(f3068h);
            int color = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            float[] fArr = new float[3];
            Color.colorToHSV(color, fArr);
            if (fArr[2] > 0.5f) {
                resources = getResources();
                i9 = j0.b.f20589b;
            } else {
                resources = getResources();
                i9 = j0.b.f20588a;
            }
            valueOf = ColorStateList.valueOf(resources.getColor(i9));
        }
        ColorStateList colorStateList = valueOf;
        float dimension = obtainStyledAttributes.getDimension(j0.e.f20598e, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(j0.e.f20599f, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(j0.e.f20600g, 0.0f);
        this.f3070a = obtainStyledAttributes.getBoolean(j0.e.f20602i, false);
        this.f3071b = obtainStyledAttributes.getBoolean(j0.e.f20601h, true);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20603j, 0);
        rect.left = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20605l, dimensionPixelSize);
        rect.top = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20607n, dimensionPixelSize);
        rect.right = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20606m, dimensionPixelSize);
        rect.bottom = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20604k, dimensionPixelSize);
        float f5 = dimension2 > dimension3 ? dimension2 : dimension3;
        this.f3072c = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20595b, 0);
        this.f3073d = obtainStyledAttributes.getDimensionPixelSize(j0.e.f20596c, 0);
        obtainStyledAttributes.recycle();
        f3069j.a(aVar, context, colorStateList, dimension, dimension2, f5);
    }

    public ColorStateList getCardBackgroundColor() {
        return f3069j.h(this.f3076g);
    }

    public float getCardElevation() {
        return f3069j.c(this.f3076g);
    }

    public int getContentPaddingBottom() {
        return this.f3074e.bottom;
    }

    public int getContentPaddingLeft() {
        return this.f3074e.left;
    }

    public int getContentPaddingRight() {
        return this.f3074e.right;
    }

    public int getContentPaddingTop() {
        return this.f3074e.top;
    }

    public float getMaxCardElevation() {
        return f3069j.g(this.f3076g);
    }

    public boolean getPreventCornerOverlap() {
        return this.f3071b;
    }

    public float getRadius() {
        return f3069j.d(this.f3076g);
    }

    public boolean getUseCompatPadding() {
        return this.f3070a;
    }

    public void h(int i8, int i9, int i10, int i11) {
        this.f3074e.set(i8, i9, i10, i11);
        f3069j.i(this.f3076g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i8, int i9) {
        e eVar = f3069j;
        if (!(eVar instanceof b)) {
            int mode = View.MeasureSpec.getMode(i8);
            if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                i8 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(eVar.l(this.f3076g)), View.MeasureSpec.getSize(i8)), mode);
            }
            int mode2 = View.MeasureSpec.getMode(i9);
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                i9 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(eVar.k(this.f3076g)), View.MeasureSpec.getSize(i9)), mode2);
            }
        }
        super.onMeasure(i8, i9);
    }

    public void setCardBackgroundColor(int i8) {
        f3069j.n(this.f3076g, ColorStateList.valueOf(i8));
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        f3069j.n(this.f3076g, colorStateList);
    }

    public void setCardElevation(float f5) {
        f3069j.f(this.f3076g, f5);
    }

    public void setMaxCardElevation(float f5) {
        f3069j.o(this.f3076g, f5);
    }

    @Override // android.view.View
    public void setMinimumHeight(int i8) {
        this.f3073d = i8;
        super.setMinimumHeight(i8);
    }

    @Override // android.view.View
    public void setMinimumWidth(int i8) {
        this.f3072c = i8;
        super.setMinimumWidth(i8);
    }

    @Override // android.view.View
    public void setPadding(int i8, int i9, int i10, int i11) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int i8, int i9, int i10, int i11) {
    }

    public void setPreventCornerOverlap(boolean z4) {
        if (z4 != this.f3071b) {
            this.f3071b = z4;
            f3069j.m(this.f3076g);
        }
    }

    public void setRadius(float f5) {
        f3069j.b(this.f3076g, f5);
    }

    public void setUseCompatPadding(boolean z4) {
        if (this.f3070a != z4) {
            this.f3070a = z4;
            f3069j.e(this.f3076g);
        }
    }
}
