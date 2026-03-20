package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f extends Drawable {

    /* renamed from: a  reason: collision with root package name */
    private float f3082a;

    /* renamed from: c  reason: collision with root package name */
    private final RectF f3084c;

    /* renamed from: d  reason: collision with root package name */
    private final Rect f3085d;

    /* renamed from: e  reason: collision with root package name */
    private float f3086e;

    /* renamed from: h  reason: collision with root package name */
    private ColorStateList f3089h;

    /* renamed from: i  reason: collision with root package name */
    private PorterDuffColorFilter f3090i;

    /* renamed from: j  reason: collision with root package name */
    private ColorStateList f3091j;

    /* renamed from: f  reason: collision with root package name */
    private boolean f3087f = false;

    /* renamed from: g  reason: collision with root package name */
    private boolean f3088g = true;

    /* renamed from: k  reason: collision with root package name */
    private PorterDuff.Mode f3092k = PorterDuff.Mode.SRC_IN;

    /* renamed from: b  reason: collision with root package name */
    private final Paint f3083b = new Paint(5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(ColorStateList colorStateList, float f5) {
        this.f3082a = f5;
        e(colorStateList);
        this.f3084c = new RectF();
        this.f3085d = new Rect();
    }

    private PorterDuffColorFilter a(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    private void e(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.f3089h = colorStateList;
        this.f3083b.setColor(colorStateList.getColorForState(getState(), this.f3089h.getDefaultColor()));
    }

    private void i(Rect rect) {
        if (rect == null) {
            rect = getBounds();
        }
        this.f3084c.set(rect.left, rect.top, rect.right, rect.bottom);
        this.f3085d.set(rect);
        if (this.f3087f) {
            float d8 = g.d(this.f3086e, this.f3082a, this.f3088g);
            this.f3085d.inset((int) Math.ceil(g.c(this.f3086e, this.f3082a, this.f3088g)), (int) Math.ceil(d8));
            this.f3084c.set(this.f3085d);
        }
    }

    public ColorStateList b() {
        return this.f3089h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return this.f3086e;
    }

    public float d() {
        return this.f3082a;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean z4;
        Paint paint = this.f3083b;
        if (this.f3090i == null || paint.getColorFilter() != null) {
            z4 = false;
        } else {
            paint.setColorFilter(this.f3090i);
            z4 = true;
        }
        RectF rectF = this.f3084c;
        float f5 = this.f3082a;
        canvas.drawRoundRect(rectF, f5, f5, paint);
        if (z4) {
            paint.setColorFilter(null);
        }
    }

    public void f(ColorStateList colorStateList) {
        e(colorStateList);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(float f5, boolean z4, boolean z8) {
        if (f5 == this.f3086e && this.f3087f == z4 && this.f3088g == z8) {
            return;
        }
        this.f3086e = f5;
        this.f3087f = z4;
        this.f3088g = z8;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(this.f3085d, this.f3082a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(float f5) {
        if (f5 == this.f3082a) {
            return;
        }
        this.f3082a = f5;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2 = this.f3091j;
        return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.f3089h) != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        i(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        PorterDuff.Mode mode;
        ColorStateList colorStateList = this.f3089h;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        boolean z4 = colorForState != this.f3083b.getColor();
        if (z4) {
            this.f3083b.setColor(colorForState);
        }
        ColorStateList colorStateList2 = this.f3091j;
        if (colorStateList2 == null || (mode = this.f3092k) == null) {
            return z4;
        }
        this.f3090i = a(colorStateList2, mode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f3083b.setAlpha(i8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f3083b.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.f3091j = colorStateList;
        this.f3090i = a(colorStateList, this.f3092k);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.f3092k = mode;
        this.f3090i = a(this.f3091j, mode);
        invalidateSelf();
    }
}
