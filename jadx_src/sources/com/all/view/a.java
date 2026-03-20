package com.all.view;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends Drawable {

    /* renamed from: a  reason: collision with root package name */
    private final RectF f8704a = new RectF();

    /* renamed from: b  reason: collision with root package name */
    private final RectF f8705b = new RectF();

    /* renamed from: c  reason: collision with root package name */
    private final RectF f8706c;

    /* renamed from: d  reason: collision with root package name */
    private final Bitmap f8707d;

    /* renamed from: e  reason: collision with root package name */
    private final Paint f8708e;

    /* renamed from: f  reason: collision with root package name */
    private final int f8709f;

    /* renamed from: g  reason: collision with root package name */
    private final int f8710g;

    /* renamed from: h  reason: collision with root package name */
    private final RectF f8711h;

    /* renamed from: i  reason: collision with root package name */
    private final Paint f8712i;

    /* renamed from: j  reason: collision with root package name */
    private final Matrix f8713j;

    /* renamed from: k  reason: collision with root package name */
    private BitmapShader f8714k;

    /* renamed from: l  reason: collision with root package name */
    private Shader.TileMode f8715l;

    /* renamed from: m  reason: collision with root package name */
    private Shader.TileMode f8716m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f8717n;

    /* renamed from: o  reason: collision with root package name */
    private float f8718o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f8719p;
    private float q;

    /* renamed from: r  reason: collision with root package name */
    private ColorStateList f8720r;

    /* renamed from: s  reason: collision with root package name */
    private ImageView.ScaleType f8721s;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.all.view.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class C0099a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f8722a;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            f8722a = iArr;
            try {
                iArr[ImageView.ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8722a[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8722a[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8722a[ImageView.ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f8722a[ImageView.ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f8722a[ImageView.ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f8722a[ImageView.ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public a(Bitmap bitmap) {
        RectF rectF = new RectF();
        this.f8706c = rectF;
        this.f8711h = new RectF();
        this.f8713j = new Matrix();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        this.f8715l = tileMode;
        this.f8716m = tileMode;
        this.f8717n = true;
        this.f8718o = 0.0f;
        this.f8719p = false;
        this.q = 0.0f;
        this.f8720r = ColorStateList.valueOf(-16777216);
        this.f8721s = ImageView.ScaleType.FIT_CENTER;
        this.f8707d = bitmap;
        int width = bitmap.getWidth();
        this.f8709f = width;
        int height = bitmap.getHeight();
        this.f8710g = height;
        rectF.set(0.0f, 0.0f, width, height);
        Paint paint = new Paint();
        this.f8708e = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Paint paint2 = new Paint();
        this.f8712i = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setAntiAlias(true);
        paint2.setColor(this.f8720r.getColorForState(getState(), -16777216));
        paint2.setStrokeWidth(this.q);
    }

    public static Bitmap a(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 2), Math.max(drawable.getIntrinsicHeight(), 2), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static a b(Bitmap bitmap) {
        if (bitmap != null) {
            return new a(bitmap);
        }
        return null;
    }

    public static Drawable c(Drawable drawable) {
        if (drawable == null || (drawable instanceof a)) {
            return drawable;
        }
        if (!(drawable instanceof LayerDrawable)) {
            Bitmap a9 = a(drawable);
            return a9 != null ? new a(a9) : drawable;
        }
        LayerDrawable layerDrawable = (LayerDrawable) drawable;
        int numberOfLayers = layerDrawable.getNumberOfLayers();
        for (int i8 = 0; i8 < numberOfLayers; i8++) {
            layerDrawable.setDrawableByLayerId(layerDrawable.getId(i8), c(layerDrawable.getDrawable(i8)));
        }
        return layerDrawable;
    }

    private void k() {
        float width;
        float height;
        Matrix matrix;
        RectF rectF;
        RectF rectF2;
        Matrix.ScaleToFit scaleToFit;
        int i8 = C0099a.f8722a[this.f8721s.ordinal()];
        if (i8 == 1) {
            this.f8711h.set(this.f8704a);
            RectF rectF3 = this.f8711h;
            float f5 = this.q;
            rectF3.inset(f5 / 2.0f, f5 / 2.0f);
            this.f8713j.reset();
            this.f8713j.setTranslate((int) (((this.f8711h.width() - this.f8709f) * 0.5f) + 0.5f), (int) (((this.f8711h.height() - this.f8710g) * 0.5f) + 0.5f));
        } else if (i8 != 2) {
            if (i8 != 3) {
                if (i8 == 5) {
                    this.f8711h.set(this.f8706c);
                    matrix = this.f8713j;
                    rectF = this.f8706c;
                    rectF2 = this.f8704a;
                    scaleToFit = Matrix.ScaleToFit.END;
                } else if (i8 == 6) {
                    this.f8711h.set(this.f8706c);
                    matrix = this.f8713j;
                    rectF = this.f8706c;
                    rectF2 = this.f8704a;
                    scaleToFit = Matrix.ScaleToFit.START;
                } else if (i8 != 7) {
                    this.f8711h.set(this.f8706c);
                    matrix = this.f8713j;
                    rectF = this.f8706c;
                    rectF2 = this.f8704a;
                    scaleToFit = Matrix.ScaleToFit.CENTER;
                } else {
                    this.f8711h.set(this.f8704a);
                    RectF rectF4 = this.f8711h;
                    float f8 = this.q;
                    rectF4.inset(f8 / 2.0f, f8 / 2.0f);
                    this.f8713j.reset();
                    this.f8713j.setRectToRect(this.f8706c, this.f8711h, Matrix.ScaleToFit.FILL);
                }
                matrix.setRectToRect(rectF, rectF2, scaleToFit);
            } else {
                this.f8713j.reset();
                float min = (((float) this.f8709f) > this.f8704a.width() || ((float) this.f8710g) > this.f8704a.height()) ? Math.min(this.f8704a.width() / this.f8709f, this.f8704a.height() / this.f8710g) : 1.0f;
                this.f8713j.setScale(min, min);
                this.f8713j.postTranslate((int) (((this.f8704a.width() - (this.f8709f * min)) * 0.5f) + 0.5f), (int) (((this.f8704a.height() - (this.f8710g * min)) * 0.5f) + 0.5f));
                this.f8711h.set(this.f8706c);
            }
            this.f8713j.mapRect(this.f8711h);
            RectF rectF5 = this.f8711h;
            float f9 = this.q;
            rectF5.inset(f9 / 2.0f, f9 / 2.0f);
            this.f8713j.setRectToRect(this.f8706c, this.f8711h, Matrix.ScaleToFit.FILL);
        } else {
            this.f8711h.set(this.f8704a);
            RectF rectF6 = this.f8711h;
            float f10 = this.q;
            rectF6.inset(f10 / 2.0f, f10 / 2.0f);
            this.f8713j.reset();
            float f11 = 0.0f;
            if (this.f8709f * this.f8711h.height() > this.f8711h.width() * this.f8710g) {
                width = this.f8711h.height() / this.f8710g;
                height = 0.0f;
                f11 = (this.f8711h.width() - (this.f8709f * width)) * 0.5f;
            } else {
                width = this.f8711h.width() / this.f8709f;
                height = (this.f8711h.height() - (this.f8710g * width)) * 0.5f;
            }
            this.f8713j.setScale(width, width);
            Matrix matrix2 = this.f8713j;
            float f12 = this.q;
            matrix2.postTranslate(((int) (f11 + 0.5f)) + f12, ((int) (height + 0.5f)) + f12);
        }
        this.f8705b.set(this.f8711h);
    }

    public a d(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.f8720r = colorStateList;
        this.f8712i.setColor(colorStateList.getColorForState(getState(), -16777216));
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        RectF rectF;
        float f5;
        Paint paint;
        RectF rectF2;
        Paint paint2;
        if (this.f8717n) {
            BitmapShader bitmapShader = new BitmapShader(this.f8707d, this.f8715l, this.f8716m);
            this.f8714k = bitmapShader;
            Shader.TileMode tileMode = this.f8715l;
            Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
            if (tileMode == tileMode2 && this.f8716m == tileMode2) {
                bitmapShader.setLocalMatrix(this.f8713j);
            }
            this.f8708e.setShader(this.f8714k);
            this.f8717n = false;
        }
        if (this.f8719p) {
            if (this.q > 0.0f) {
                canvas.drawOval(this.f8705b, this.f8708e);
                rectF2 = this.f8711h;
                paint2 = this.f8712i;
            } else {
                rectF2 = this.f8705b;
                paint2 = this.f8708e;
            }
            canvas.drawOval(rectF2, paint2);
            return;
        }
        if (this.q > 0.0f) {
            canvas.drawRoundRect(this.f8705b, Math.max(this.f8718o, 0.0f), Math.max(this.f8718o, 0.0f), this.f8708e);
            rectF = this.f8711h;
            f5 = this.f8718o;
            paint = this.f8712i;
        } else {
            rectF = this.f8705b;
            f5 = this.f8718o;
            paint = this.f8708e;
        }
        canvas.drawRoundRect(rectF, f5, f5, paint);
    }

    public a e(float f5) {
        this.q = f5;
        this.f8712i.setStrokeWidth(f5);
        return this;
    }

    public a f(float f5) {
        this.f8718o = f5;
        return this;
    }

    public a g(boolean z4) {
        this.f8719p = z4;
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f8710g;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f8709f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public a h(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        if (this.f8721s != scaleType) {
            this.f8721s = scaleType;
            k();
        }
        return this;
    }

    public a i(Shader.TileMode tileMode) {
        if (this.f8715l != tileMode) {
            this.f8715l = tileMode;
            this.f8717n = true;
            invalidateSelf();
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.f8720r.isStateful();
    }

    public a j(Shader.TileMode tileMode) {
        if (this.f8716m != tileMode) {
            this.f8716m = tileMode;
            this.f8717n = true;
            invalidateSelf();
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.f8704a.set(rect);
        k();
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        int colorForState = this.f8720r.getColorForState(iArr, 0);
        if (this.f8712i.getColor() != colorForState) {
            this.f8712i.setColor(colorForState);
            return true;
        }
        return super.onStateChange(iArr);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f8708e.setAlpha(i8);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f8708e.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z4) {
        this.f8708e.setDither(z4);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z4) {
        this.f8708e.setFilterBitmap(z4);
        invalidateSelf();
    }
}
