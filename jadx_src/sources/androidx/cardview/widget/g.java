package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class g extends Drawable {
    private static final double q = Math.cos(Math.toRadians(45.0d));

    /* renamed from: r  reason: collision with root package name */
    static a f3093r;

    /* renamed from: a  reason: collision with root package name */
    private final int f3094a;

    /* renamed from: c  reason: collision with root package name */
    private Paint f3096c;

    /* renamed from: d  reason: collision with root package name */
    private Paint f3097d;

    /* renamed from: e  reason: collision with root package name */
    private final RectF f3098e;

    /* renamed from: f  reason: collision with root package name */
    private float f3099f;

    /* renamed from: g  reason: collision with root package name */
    private Path f3100g;

    /* renamed from: h  reason: collision with root package name */
    private float f3101h;

    /* renamed from: i  reason: collision with root package name */
    private float f3102i;

    /* renamed from: j  reason: collision with root package name */
    private float f3103j;

    /* renamed from: k  reason: collision with root package name */
    private ColorStateList f3104k;

    /* renamed from: m  reason: collision with root package name */
    private final int f3106m;

    /* renamed from: n  reason: collision with root package name */
    private final int f3107n;

    /* renamed from: l  reason: collision with root package name */
    private boolean f3105l = true;

    /* renamed from: o  reason: collision with root package name */
    private boolean f3108o = true;

    /* renamed from: p  reason: collision with root package name */
    private boolean f3109p = false;

    /* renamed from: b  reason: collision with root package name */
    private Paint f3095b = new Paint(5);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a(Canvas canvas, RectF rectF, float f5, Paint paint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Resources resources, ColorStateList colorStateList, float f5, float f8, float f9) {
        this.f3106m = resources.getColor(j0.b.f20591d);
        this.f3107n = resources.getColor(j0.b.f20590c);
        this.f3094a = resources.getDimensionPixelSize(j0.c.f20592a);
        n(colorStateList);
        Paint paint = new Paint(5);
        this.f3096c = paint;
        paint.setStyle(Paint.Style.FILL);
        this.f3099f = (int) (f5 + 0.5f);
        this.f3098e = new RectF();
        Paint paint2 = new Paint(this.f3096c);
        this.f3097d = paint2;
        paint2.setAntiAlias(false);
        s(f8, f9);
    }

    private void a(Rect rect) {
        float f5 = this.f3101h;
        float f8 = 1.5f * f5;
        this.f3098e.set(rect.left + f5, rect.top + f8, rect.right - f5, rect.bottom - f8);
        b();
    }

    private void b() {
        float f5 = this.f3099f;
        RectF rectF = new RectF(-f5, -f5, f5, f5);
        RectF rectF2 = new RectF(rectF);
        float f8 = this.f3102i;
        rectF2.inset(-f8, -f8);
        Path path = this.f3100g;
        if (path == null) {
            this.f3100g = new Path();
        } else {
            path.reset();
        }
        this.f3100g.setFillType(Path.FillType.EVEN_ODD);
        this.f3100g.moveTo(-this.f3099f, 0.0f);
        this.f3100g.rLineTo(-this.f3102i, 0.0f);
        this.f3100g.arcTo(rectF2, 180.0f, 90.0f, false);
        this.f3100g.arcTo(rectF, 270.0f, -90.0f, false);
        this.f3100g.close();
        float f9 = this.f3099f;
        float f10 = f9 / (this.f3102i + f9);
        Paint paint = this.f3096c;
        float f11 = this.f3099f + this.f3102i;
        int i8 = this.f3106m;
        paint.setShader(new RadialGradient(0.0f, 0.0f, f11, new int[]{i8, i8, this.f3107n}, new float[]{0.0f, f10, 1.0f}, Shader.TileMode.CLAMP));
        Paint paint2 = this.f3097d;
        float f12 = this.f3099f;
        float f13 = this.f3102i;
        int i9 = this.f3106m;
        paint2.setShader(new LinearGradient(0.0f, (-f12) + f13, 0.0f, (-f12) - f13, new int[]{i9, i9, this.f3107n}, new float[]{0.0f, 0.5f, 1.0f}, Shader.TileMode.CLAMP));
        this.f3097d.setAntiAlias(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(float f5, float f8, boolean z4) {
        return z4 ? (float) (f5 + ((1.0d - q) * f8)) : f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(float f5, float f8, boolean z4) {
        float f9 = f5 * 1.5f;
        return z4 ? (float) (f9 + ((1.0d - q) * f8)) : f9;
    }

    private void e(Canvas canvas) {
        float f5 = this.f3099f;
        float f8 = (-f5) - this.f3102i;
        float f9 = f5 + this.f3094a + (this.f3103j / 2.0f);
        float f10 = f9 * 2.0f;
        boolean z4 = this.f3098e.width() - f10 > 0.0f;
        boolean z8 = this.f3098e.height() - f10 > 0.0f;
        int save = canvas.save();
        RectF rectF = this.f3098e;
        canvas.translate(rectF.left + f9, rectF.top + f9);
        canvas.drawPath(this.f3100g, this.f3096c);
        if (z4) {
            canvas.drawRect(0.0f, f8, this.f3098e.width() - f10, -this.f3099f, this.f3097d);
        }
        canvas.restoreToCount(save);
        int save2 = canvas.save();
        RectF rectF2 = this.f3098e;
        canvas.translate(rectF2.right - f9, rectF2.bottom - f9);
        canvas.rotate(180.0f);
        canvas.drawPath(this.f3100g, this.f3096c);
        if (z4) {
            canvas.drawRect(0.0f, f8, this.f3098e.width() - f10, (-this.f3099f) + this.f3102i, this.f3097d);
        }
        canvas.restoreToCount(save2);
        int save3 = canvas.save();
        RectF rectF3 = this.f3098e;
        canvas.translate(rectF3.left + f9, rectF3.bottom - f9);
        canvas.rotate(270.0f);
        canvas.drawPath(this.f3100g, this.f3096c);
        if (z8) {
            canvas.drawRect(0.0f, f8, this.f3098e.height() - f10, -this.f3099f, this.f3097d);
        }
        canvas.restoreToCount(save3);
        int save4 = canvas.save();
        RectF rectF4 = this.f3098e;
        canvas.translate(rectF4.right - f9, rectF4.top + f9);
        canvas.rotate(90.0f);
        canvas.drawPath(this.f3100g, this.f3096c);
        if (z8) {
            canvas.drawRect(0.0f, f8, this.f3098e.height() - f10, -this.f3099f, this.f3097d);
        }
        canvas.restoreToCount(save4);
    }

    private void n(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.f3104k = colorStateList;
        this.f3095b.setColor(colorStateList.getColorForState(getState(), this.f3104k.getDefaultColor()));
    }

    private void s(float f5, float f8) {
        if (f5 < 0.0f) {
            throw new IllegalArgumentException("Invalid shadow size " + f5 + ". Must be >= 0");
        } else if (f8 < 0.0f) {
            throw new IllegalArgumentException("Invalid max shadow size " + f8 + ". Must be >= 0");
        } else {
            float t8 = t(f5);
            float t9 = t(f8);
            if (t8 > t9) {
                if (!this.f3109p) {
                    this.f3109p = true;
                }
                t8 = t9;
            }
            if (this.f3103j == t8 && this.f3101h == t9) {
                return;
            }
            this.f3103j = t8;
            this.f3101h = t9;
            this.f3102i = (int) ((t8 * 1.5f) + this.f3094a + 0.5f);
            this.f3105l = true;
            invalidateSelf();
        }
    }

    private int t(float f5) {
        int i8 = (int) (f5 + 0.5f);
        return i8 % 2 == 1 ? i8 - 1 : i8;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.f3105l) {
            a(getBounds());
            this.f3105l = false;
        }
        canvas.translate(0.0f, this.f3103j / 2.0f);
        e(canvas);
        canvas.translate(0.0f, (-this.f3103j) / 2.0f);
        f3093r.a(canvas, this.f3098e, this.f3099f, this.f3095b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList f() {
        return this.f3104k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float g() {
        return this.f3099f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        int ceil = (int) Math.ceil(d(this.f3101h, this.f3099f, this.f3108o));
        int ceil2 = (int) Math.ceil(c(this.f3101h, this.f3099f, this.f3108o));
        rect.set(ceil2, ceil, ceil2, ceil);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Rect rect) {
        getPadding(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f3101h;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.f3104k;
        return (colorStateList != null && colorStateList.isStateful()) || super.isStateful();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        float f5 = this.f3101h;
        return (Math.max(f5, this.f3099f + this.f3094a + ((f5 * 1.5f) / 2.0f)) * 2.0f) + (((this.f3101h * 1.5f) + this.f3094a) * 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float k() {
        float f5 = this.f3101h;
        return (Math.max(f5, this.f3099f + this.f3094a + (f5 / 2.0f)) * 2.0f) + ((this.f3101h + this.f3094a) * 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float l() {
        return this.f3103j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(boolean z4) {
        this.f3108o = z4;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ColorStateList colorStateList) {
        n(colorStateList);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.f3105l = true;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        ColorStateList colorStateList = this.f3104k;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        if (this.f3095b.getColor() == colorForState) {
            return false;
        }
        this.f3095b.setColor(colorForState);
        this.f3105l = true;
        invalidateSelf();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(float f5) {
        if (f5 < 0.0f) {
            throw new IllegalArgumentException("Invalid radius " + f5 + ". Must be >= 0");
        }
        float f8 = (int) (f5 + 0.5f);
        if (this.f3099f == f8) {
            return;
        }
        this.f3099f = f8;
        this.f3105l = true;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(float f5) {
        s(this.f3103j, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(float f5) {
        s(f5, this.f3101h);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f3095b.setAlpha(i8);
        this.f3096c.setAlpha(i8);
        this.f3097d.setAlpha(i8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f3095b.setColorFilter(colorFilter);
    }
}
