package i;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import g.i;
import g.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends Drawable {

    /* renamed from: m  reason: collision with root package name */
    private static final float f20399m = (float) Math.toRadians(45.0d);

    /* renamed from: a  reason: collision with root package name */
    private final Paint f20400a;

    /* renamed from: b  reason: collision with root package name */
    private float f20401b;

    /* renamed from: c  reason: collision with root package name */
    private float f20402c;

    /* renamed from: d  reason: collision with root package name */
    private float f20403d;

    /* renamed from: e  reason: collision with root package name */
    private float f20404e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f20405f;

    /* renamed from: g  reason: collision with root package name */
    private final Path f20406g;

    /* renamed from: h  reason: collision with root package name */
    private final int f20407h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f20408i;

    /* renamed from: j  reason: collision with root package name */
    private float f20409j;

    /* renamed from: k  reason: collision with root package name */
    private float f20410k;

    /* renamed from: l  reason: collision with root package name */
    private int f20411l;

    public d(Context context) {
        Paint paint = new Paint();
        this.f20400a = paint;
        this.f20406g = new Path();
        this.f20408i = false;
        this.f20411l = 2;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(null, j.f20011b1, g.a.B, i.f19998b);
        c(obtainStyledAttributes.getColor(j.f20034f1, 0));
        b(obtainStyledAttributes.getDimension(j.f20054j1, 0.0f));
        e(obtainStyledAttributes.getBoolean(j.f20049i1, true));
        d(Math.round(obtainStyledAttributes.getDimension(j.f20044h1, 0.0f)));
        this.f20407h = obtainStyledAttributes.getDimensionPixelSize(j.f20039g1, 0);
        this.f20402c = Math.round(obtainStyledAttributes.getDimension(j.f20029e1, 0.0f));
        this.f20401b = Math.round(obtainStyledAttributes.getDimension(j.f20017c1, 0.0f));
        this.f20403d = obtainStyledAttributes.getDimension(j.f20023d1, 0.0f);
        obtainStyledAttributes.recycle();
    }

    private static float a(float f5, float f8, float f9) {
        return f5 + ((f8 - f5) * f9);
    }

    public void b(float f5) {
        if (this.f20400a.getStrokeWidth() != f5) {
            this.f20400a.setStrokeWidth(f5);
            this.f20410k = (float) ((f5 / 2.0f) * Math.cos(f20399m));
            invalidateSelf();
        }
    }

    public void c(int i8) {
        if (i8 != this.f20400a.getColor()) {
            this.f20400a.setColor(i8);
            invalidateSelf();
        }
    }

    public void d(float f5) {
        if (f5 != this.f20404e) {
            this.f20404e = f5;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        float f5;
        Rect bounds = getBounds();
        int i8 = this.f20411l;
        boolean z4 = false;
        if (i8 != 0 && (i8 == 1 || (i8 == 3 ? androidx.core.graphics.drawable.a.f(this) == 0 : androidx.core.graphics.drawable.a.f(this) == 1))) {
            z4 = true;
        }
        float f8 = this.f20401b;
        float a9 = a(this.f20402c, (float) Math.sqrt(f8 * f8 * 2.0f), this.f20409j);
        float a10 = a(this.f20402c, this.f20403d, this.f20409j);
        float round = Math.round(a(0.0f, this.f20410k, this.f20409j));
        float a11 = a(0.0f, f20399m, this.f20409j);
        float a12 = a(z4 ? 0.0f : -180.0f, z4 ? 180.0f : 0.0f, this.f20409j);
        double d8 = a9;
        double d9 = a11;
        boolean z8 = z4;
        float round2 = (float) Math.round(Math.cos(d9) * d8);
        float round3 = (float) Math.round(d8 * Math.sin(d9));
        this.f20406g.rewind();
        float a13 = a(this.f20404e + this.f20400a.getStrokeWidth(), -this.f20410k, this.f20409j);
        float f9 = (-a10) / 2.0f;
        this.f20406g.moveTo(f9 + round, 0.0f);
        this.f20406g.rLineTo(a10 - (round * 2.0f), 0.0f);
        this.f20406g.moveTo(f9, a13);
        this.f20406g.rLineTo(round2, round3);
        this.f20406g.moveTo(f9, -a13);
        this.f20406g.rLineTo(round2, -round3);
        this.f20406g.close();
        canvas.save();
        float strokeWidth = this.f20400a.getStrokeWidth();
        float height = bounds.height() - (3.0f * strokeWidth);
        canvas.translate(bounds.centerX(), ((((int) (height - (2.0f * f5))) / 4) * 2) + (strokeWidth * 1.5f) + this.f20404e);
        if (this.f20405f) {
            canvas.rotate(a12 * (this.f20408i ^ z8 ? -1 : 1));
        } else if (z8) {
            canvas.rotate(180.0f);
        }
        canvas.drawPath(this.f20406g, this.f20400a);
        canvas.restore();
    }

    public void e(boolean z4) {
        if (this.f20405f != z4) {
            this.f20405f = z4;
            invalidateSelf();
        }
    }

    public void f(boolean z4) {
        if (this.f20408i != z4) {
            this.f20408i = z4;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f20407h;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f20407h;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        if (i8 != this.f20400a.getAlpha()) {
            this.f20400a.setAlpha(i8);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f20400a.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setProgress(float f5) {
        if (this.f20409j != f5) {
            this.f20409j = f5;
            invalidateSelf();
        }
    }
}
