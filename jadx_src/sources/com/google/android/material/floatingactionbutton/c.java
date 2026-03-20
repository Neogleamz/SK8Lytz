package com.google.android.material.floatingactionbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import x7.m;
import x7.n;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends Drawable {

    /* renamed from: b  reason: collision with root package name */
    private final Paint f17946b;

    /* renamed from: h  reason: collision with root package name */
    float f17952h;

    /* renamed from: i  reason: collision with root package name */
    private int f17953i;

    /* renamed from: j  reason: collision with root package name */
    private int f17954j;

    /* renamed from: k  reason: collision with root package name */
    private int f17955k;

    /* renamed from: l  reason: collision with root package name */
    private int f17956l;

    /* renamed from: m  reason: collision with root package name */
    private int f17957m;

    /* renamed from: o  reason: collision with root package name */
    private m f17959o;

    /* renamed from: p  reason: collision with root package name */
    private ColorStateList f17960p;

    /* renamed from: a  reason: collision with root package name */
    private final n f17945a = n.k();

    /* renamed from: c  reason: collision with root package name */
    private final Path f17947c = new Path();

    /* renamed from: d  reason: collision with root package name */
    private final Rect f17948d = new Rect();

    /* renamed from: e  reason: collision with root package name */
    private final RectF f17949e = new RectF();

    /* renamed from: f  reason: collision with root package name */
    private final RectF f17950f = new RectF();

    /* renamed from: g  reason: collision with root package name */
    private final b f17951g = new b();

    /* renamed from: n  reason: collision with root package name */
    private boolean f17958n = true;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b extends Drawable.ConstantState {
        private b() {
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return c.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(m mVar) {
        this.f17959o = mVar;
        Paint paint = new Paint(1);
        this.f17946b = paint;
        paint.setStyle(Paint.Style.STROKE);
    }

    private Shader a() {
        Rect rect = this.f17948d;
        copyBounds(rect);
        float height = this.f17952h / rect.height();
        return new LinearGradient(0.0f, rect.top, 0.0f, rect.bottom, new int[]{androidx.core.graphics.b.k(this.f17953i, this.f17957m), androidx.core.graphics.b.k(this.f17954j, this.f17957m), androidx.core.graphics.b.k(androidx.core.graphics.b.p(this.f17954j, 0), this.f17957m), androidx.core.graphics.b.k(androidx.core.graphics.b.p(this.f17956l, 0), this.f17957m), androidx.core.graphics.b.k(this.f17956l, this.f17957m), androidx.core.graphics.b.k(this.f17955k, this.f17957m)}, new float[]{0.0f, height, 0.5f, 0.5f, 1.0f - height, 1.0f}, Shader.TileMode.CLAMP);
    }

    protected RectF b() {
        this.f17950f.set(getBounds());
        return this.f17950f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f17957m = colorStateList.getColorForState(getState(), this.f17957m);
        }
        this.f17960p = colorStateList;
        this.f17958n = true;
        invalidateSelf();
    }

    public void d(float f5) {
        if (this.f17952h != f5) {
            this.f17952h = f5;
            this.f17946b.setStrokeWidth(f5 * 1.3333f);
            this.f17958n = true;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.f17958n) {
            this.f17946b.setShader(a());
            this.f17958n = false;
        }
        float strokeWidth = this.f17946b.getStrokeWidth() / 2.0f;
        copyBounds(this.f17948d);
        this.f17949e.set(this.f17948d);
        float min = Math.min(this.f17959o.r().a(b()), this.f17949e.width() / 2.0f);
        if (this.f17959o.u(b())) {
            this.f17949e.inset(strokeWidth, strokeWidth);
            canvas.drawRoundRect(this.f17949e, min, min, this.f17946b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(int i8, int i9, int i10, int i11) {
        this.f17953i = i8;
        this.f17954j = i9;
        this.f17955k = i10;
        this.f17956l = i11;
    }

    public void f(m mVar) {
        this.f17959o = mVar;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f17951g;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f17952h > 0.0f ? -3 : -2;
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.f17959o.u(b())) {
            outline.setRoundRect(getBounds(), this.f17959o.r().a(b()));
            return;
        }
        copyBounds(this.f17948d);
        this.f17949e.set(this.f17948d);
        this.f17945a.d(this.f17959o, 1.0f, this.f17949e, this.f17947c);
        if (this.f17947c.isConvex()) {
            outline.setConvexPath(this.f17947c);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        if (this.f17959o.u(b())) {
            int round = Math.round(this.f17952h);
            rect.set(round, round, round, round);
            return true;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.f17960p;
        return (colorStateList != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.f17958n = true;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        int colorForState;
        ColorStateList colorStateList = this.f17960p;
        if (colorStateList != null && (colorForState = colorStateList.getColorForState(iArr, this.f17957m)) != this.f17957m) {
            this.f17958n = true;
            this.f17957m = colorForState;
        }
        if (this.f17958n) {
            invalidateSelf();
        }
        return this.f17958n;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f17946b.setAlpha(i8);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f17946b.setColorFilter(colorFilter);
        invalidateSelf();
    }
}
