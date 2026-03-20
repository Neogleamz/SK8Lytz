package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends View {
    private Bitmap A;
    private int B;
    private int C;
    private int E;
    private float F;
    private int G;
    private int H;
    private boolean K;
    private String L;
    private String O;
    private String P;
    private int Q;
    private int R;
    private boolean T;
    private int W;

    /* renamed from: a  reason: collision with root package name */
    private int f8592a;

    /* renamed from: a0  reason: collision with root package name */
    private boolean f8593a0;

    /* renamed from: b  reason: collision with root package name */
    private int f8594b;

    /* renamed from: b0  reason: collision with root package name */
    private int f8595b0;

    /* renamed from: c  reason: collision with root package name */
    private Rect f8596c;

    /* renamed from: c0  reason: collision with root package name */
    private boolean f8597c0;

    /* renamed from: d  reason: collision with root package name */
    private float f8598d;

    /* renamed from: d0  reason: collision with root package name */
    private boolean f8599d0;

    /* renamed from: e  reason: collision with root package name */
    private float f8600e;

    /* renamed from: e0  reason: collision with root package name */
    private boolean f8601e0;

    /* renamed from: f  reason: collision with root package name */
    private Paint f8602f;

    /* renamed from: f0  reason: collision with root package name */
    private Drawable f8603f0;

    /* renamed from: g  reason: collision with root package name */
    private TextPaint f8604g;

    /* renamed from: g0  reason: collision with root package name */
    private Bitmap f8605g0;

    /* renamed from: h  reason: collision with root package name */
    private int f8606h;

    /* renamed from: h0  reason: collision with root package name */
    private float f8607h0;

    /* renamed from: i0  reason: collision with root package name */
    private float f8608i0;

    /* renamed from: j  reason: collision with root package name */
    private int f8609j;

    /* renamed from: j0  reason: collision with root package name */
    private Bitmap f8610j0;

    /* renamed from: k  reason: collision with root package name */
    private int f8611k;

    /* renamed from: k0  reason: collision with root package name */
    private Bitmap f8612k0;

    /* renamed from: l  reason: collision with root package name */
    private int f8613l;

    /* renamed from: l0  reason: collision with root package name */
    private Bitmap f8614l0;

    /* renamed from: m  reason: collision with root package name */
    private int f8615m;

    /* renamed from: m0  reason: collision with root package name */
    private Bitmap f8616m0;

    /* renamed from: n  reason: collision with root package name */
    private int f8617n;

    /* renamed from: n0  reason: collision with root package name */
    private float f8618n0;

    /* renamed from: o0  reason: collision with root package name */
    private StaticLayout f8619o0;

    /* renamed from: p  reason: collision with root package name */
    private int f8620p;

    /* renamed from: p0  reason: collision with root package name */
    private int f8621p0;
    private int q;

    /* renamed from: q0  reason: collision with root package name */
    private boolean f8622q0;

    /* renamed from: r0  reason: collision with root package name */
    private boolean f8623r0;

    /* renamed from: s0  reason: collision with root package name */
    private boolean f8624s0;

    /* renamed from: t  reason: collision with root package name */
    private int f8625t;

    /* renamed from: t0  reason: collision with root package name */
    private QRCodeView f8626t0;

    /* renamed from: w  reason: collision with root package name */
    private int f8627w;

    /* renamed from: x  reason: collision with root package name */
    private int f8628x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f8629y;

    /* renamed from: z  reason: collision with root package name */
    private Drawable f8630z;

    public h(Context context) {
        super(context);
        Paint paint = new Paint();
        this.f8602f = paint;
        paint.setAntiAlias(true);
        this.f8606h = Color.parseColor("#33FFFFFF");
        this.f8609j = -1;
        this.f8611k = a.g(context, 20.0f);
        this.f8613l = a.g(context, 3.0f);
        this.f8625t = a.g(context, 1.0f);
        this.f8627w = -1;
        this.q = a.g(context, 90.0f);
        this.f8615m = a.g(context, 200.0f);
        this.f8620p = a.g(context, 140.0f);
        this.f8628x = 0;
        this.f8629y = false;
        this.f8630z = null;
        this.A = null;
        this.B = a.g(context, 1.0f);
        this.C = -1;
        this.E = 1000;
        this.F = -1.0f;
        this.G = 1;
        this.H = 0;
        this.K = false;
        this.f8592a = a.g(context, 2.0f);
        this.P = null;
        this.Q = a.p(context, 14.0f);
        this.R = -1;
        this.T = false;
        this.W = a.g(context, 20.0f);
        this.f8593a0 = false;
        this.f8595b0 = Color.parseColor("#22000000");
        this.f8597c0 = false;
        this.f8599d0 = false;
        this.f8601e0 = false;
        TextPaint textPaint = new TextPaint();
        this.f8604g = textPaint;
        textPaint.setAntiAlias(true);
        this.f8621p0 = a.g(context, 4.0f);
        this.f8622q0 = false;
        this.f8623r0 = false;
        this.f8624s0 = false;
    }

    private void a() {
        Drawable drawable = this.f8603f0;
        if (drawable != null) {
            this.f8614l0 = ((BitmapDrawable) drawable).getBitmap();
        }
        if (this.f8614l0 == null) {
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), f.f8565a);
            this.f8614l0 = decodeResource;
            this.f8614l0 = a.n(decodeResource, this.f8627w);
        }
        Bitmap a9 = a.a(this.f8614l0, 90);
        this.f8616m0 = a9;
        Bitmap a10 = a.a(a9, 90);
        this.f8616m0 = a10;
        this.f8616m0 = a.a(a10, 90);
        Drawable drawable2 = this.f8630z;
        if (drawable2 != null) {
            this.f8610j0 = ((BitmapDrawable) drawable2).getBitmap();
        }
        if (this.f8610j0 == null) {
            Bitmap decodeResource2 = BitmapFactory.decodeResource(getResources(), f.f8566b);
            this.f8610j0 = decodeResource2;
            this.f8610j0 = a.n(decodeResource2, this.f8627w);
        }
        this.f8612k0 = a.a(this.f8610j0, 90);
        this.q += this.H;
        this.f8618n0 = (this.f8613l * 1.0f) / 2.0f;
        this.f8604g.setTextSize(this.Q);
        this.f8604g.setColor(this.R);
        setIsBarcode(this.K);
    }

    private void b() {
        int width = (getWidth() - this.f8615m) / 2;
        int i8 = this.q;
        Rect rect = new Rect(width, i8, this.f8615m + width, this.f8617n + i8);
        this.f8596c = rect;
        if (this.K) {
            float f5 = rect.left + this.f8618n0 + 0.5f;
            this.f8600e = f5;
            this.f8608i0 = f5;
        } else {
            float f8 = rect.top + this.f8618n0 + 0.5f;
            this.f8598d = f8;
            this.f8607h0 = f8;
        }
        if (this.f8626t0 == null || !l()) {
            return;
        }
        this.f8626t0.m(new Rect(this.f8596c));
    }

    private void c(Canvas canvas) {
        if (this.B > 0) {
            this.f8602f.setStyle(Paint.Style.STROKE);
            this.f8602f.setColor(this.C);
            this.f8602f.setStrokeWidth(this.B);
            canvas.drawRect(this.f8596c, this.f8602f);
        }
    }

    private void d(Canvas canvas) {
        if (this.f8618n0 > 0.0f) {
            this.f8602f.setStyle(Paint.Style.STROKE);
            this.f8602f.setColor(this.f8609j);
            this.f8602f.setStrokeWidth(this.f8613l);
            int i8 = this.G;
            if (i8 == 1) {
                Rect rect = this.f8596c;
                int i9 = rect.left;
                float f5 = this.f8618n0;
                int i10 = rect.top;
                canvas.drawLine(i9 - f5, i10, (i9 - f5) + this.f8611k, i10, this.f8602f);
                Rect rect2 = this.f8596c;
                int i11 = rect2.left;
                int i12 = rect2.top;
                float f8 = this.f8618n0;
                canvas.drawLine(i11, i12 - f8, i11, (i12 - f8) + this.f8611k, this.f8602f);
                Rect rect3 = this.f8596c;
                int i13 = rect3.right;
                float f9 = this.f8618n0;
                int i14 = rect3.top;
                canvas.drawLine(i13 + f9, i14, (i13 + f9) - this.f8611k, i14, this.f8602f);
                Rect rect4 = this.f8596c;
                int i15 = rect4.right;
                int i16 = rect4.top;
                float f10 = this.f8618n0;
                canvas.drawLine(i15, i16 - f10, i15, (i16 - f10) + this.f8611k, this.f8602f);
                Rect rect5 = this.f8596c;
                int i17 = rect5.left;
                float f11 = this.f8618n0;
                int i18 = rect5.bottom;
                canvas.drawLine(i17 - f11, i18, (i17 - f11) + this.f8611k, i18, this.f8602f);
                Rect rect6 = this.f8596c;
                int i19 = rect6.left;
                int i20 = rect6.bottom;
                float f12 = this.f8618n0;
                canvas.drawLine(i19, i20 + f12, i19, (i20 + f12) - this.f8611k, this.f8602f);
                Rect rect7 = this.f8596c;
                int i21 = rect7.right;
                float f13 = this.f8618n0;
                int i22 = rect7.bottom;
                canvas.drawLine(i21 + f13, i22, (i21 + f13) - this.f8611k, i22, this.f8602f);
                Rect rect8 = this.f8596c;
                int i23 = rect8.right;
                int i24 = rect8.bottom;
                float f14 = this.f8618n0;
                canvas.drawLine(i23, i24 + f14, i23, (i24 + f14) - this.f8611k, this.f8602f);
            } else if (i8 == 2) {
                Rect rect9 = this.f8596c;
                int i25 = rect9.left;
                int i26 = rect9.top;
                float f15 = this.f8618n0;
                canvas.drawLine(i25, i26 + f15, i25 + this.f8611k, i26 + f15, this.f8602f);
                Rect rect10 = this.f8596c;
                int i27 = rect10.left;
                float f16 = this.f8618n0;
                int i28 = rect10.top;
                canvas.drawLine(i27 + f16, i28, i27 + f16, i28 + this.f8611k, this.f8602f);
                Rect rect11 = this.f8596c;
                int i29 = rect11.right;
                int i30 = rect11.top;
                float f17 = this.f8618n0;
                canvas.drawLine(i29, i30 + f17, i29 - this.f8611k, i30 + f17, this.f8602f);
                Rect rect12 = this.f8596c;
                int i31 = rect12.right;
                float f18 = this.f8618n0;
                int i32 = rect12.top;
                canvas.drawLine(i31 - f18, i32, i31 - f18, i32 + this.f8611k, this.f8602f);
                Rect rect13 = this.f8596c;
                int i33 = rect13.left;
                int i34 = rect13.bottom;
                float f19 = this.f8618n0;
                canvas.drawLine(i33, i34 - f19, i33 + this.f8611k, i34 - f19, this.f8602f);
                Rect rect14 = this.f8596c;
                int i35 = rect14.left;
                float f20 = this.f8618n0;
                int i36 = rect14.bottom;
                canvas.drawLine(i35 + f20, i36, i35 + f20, i36 - this.f8611k, this.f8602f);
                Rect rect15 = this.f8596c;
                int i37 = rect15.right;
                int i38 = rect15.bottom;
                float f21 = this.f8618n0;
                canvas.drawLine(i37, i38 - f21, i37 - this.f8611k, i38 - f21, this.f8602f);
                Rect rect16 = this.f8596c;
                int i39 = rect16.right;
                float f22 = this.f8618n0;
                int i40 = rect16.bottom;
                canvas.drawLine(i39 - f22, i40, i39 - f22, i40 - this.f8611k, this.f8602f);
            }
        }
    }

    private void e(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (this.f8606h != 0) {
            this.f8602f.setStyle(Paint.Style.FILL);
            this.f8602f.setColor(this.f8606h);
            float f5 = width;
            canvas.drawRect(0.0f, 0.0f, f5, this.f8596c.top, this.f8602f);
            Rect rect = this.f8596c;
            canvas.drawRect(0.0f, rect.top, rect.left, rect.bottom + 1, this.f8602f);
            Rect rect2 = this.f8596c;
            canvas.drawRect(rect2.right + 1, rect2.top, f5, rect2.bottom + 1, this.f8602f);
            canvas.drawRect(0.0f, this.f8596c.bottom + 1, f5, height, this.f8602f);
        }
    }

    private void f(Canvas canvas) {
        RectF rectF;
        Bitmap bitmap;
        Rect rect = null;
        if (this.K) {
            if (this.f8605g0 != null) {
                Rect rect2 = this.f8596c;
                float f5 = this.f8618n0;
                int i8 = this.f8628x;
                rectF = new RectF(rect2.left + f5 + 0.5f, rect2.top + f5 + i8, this.f8608i0, (rect2.bottom - f5) - i8);
                rect = new Rect((int) (this.f8605g0.getWidth() - rectF.width()), 0, this.f8605g0.getWidth(), this.f8605g0.getHeight());
                if (rect.left < 0) {
                    rect.left = 0;
                    rectF.left = rectF.right - rect.width();
                }
                bitmap = this.f8605g0;
            } else if (this.A == null) {
                this.f8602f.setStyle(Paint.Style.FILL);
                this.f8602f.setColor(this.f8627w);
                float f8 = this.f8600e;
                Rect rect3 = this.f8596c;
                float f9 = this.f8618n0;
                int i9 = this.f8628x;
                canvas.drawRect(f8, rect3.top + f9 + i9, this.f8625t + f8, (rect3.bottom - f9) - i9, this.f8602f);
                return;
            } else {
                float f10 = this.f8600e;
                rectF = new RectF(f10, this.f8596c.top + this.f8618n0 + this.f8628x, this.A.getWidth() + f10, (this.f8596c.bottom - this.f8618n0) - this.f8628x);
                bitmap = this.A;
            }
        } else if (this.f8605g0 != null) {
            Rect rect4 = this.f8596c;
            float f11 = this.f8618n0;
            int i10 = this.f8628x;
            rectF = new RectF(rect4.left + f11 + i10, rect4.top + f11 + 0.5f, (rect4.right - f11) - i10, this.f8607h0);
            rect = new Rect(0, (int) (this.f8605g0.getHeight() - rectF.height()), this.f8605g0.getWidth(), this.f8605g0.getHeight());
            if (rect.top < 0) {
                rect.top = 0;
                rectF.top = rectF.bottom - rect.height();
            }
            bitmap = this.f8605g0;
        } else if (this.A == null) {
            this.f8602f.setStyle(Paint.Style.FILL);
            this.f8602f.setColor(this.f8627w);
            Rect rect5 = this.f8596c;
            float f12 = this.f8618n0;
            int i11 = this.f8628x;
            float f13 = this.f8598d;
            canvas.drawRect(rect5.left + f12 + i11, f13, (rect5.right - f12) - i11, f13 + this.f8625t, this.f8602f);
            return;
        } else {
            Rect rect6 = this.f8596c;
            float f14 = this.f8618n0;
            int i12 = this.f8628x;
            float f15 = rect6.left + f14 + i12;
            float f16 = this.f8598d;
            rectF = new RectF(f15, f16, (rect6.right - f14) - i12, this.A.getHeight() + f16);
            bitmap = this.A;
        }
        canvas.drawBitmap(bitmap, rect, rectF, this.f8602f);
    }

    private void g(Canvas canvas) {
        float f5;
        int height;
        int height2;
        Rect rect;
        Rect rect2;
        if (TextUtils.isEmpty(this.P) || this.f8619o0 == null) {
            return;
        }
        if (this.T) {
            if (this.f8597c0) {
                this.f8602f.setColor(this.f8595b0);
                this.f8602f.setStyle(Paint.Style.FILL);
                if (this.f8593a0) {
                    Rect rect3 = new Rect();
                    TextPaint textPaint = this.f8604g;
                    String str = this.P;
                    textPaint.getTextBounds(str, 0, str.length(), rect3);
                    float width = ((canvas.getWidth() - rect3.width()) / 2) - this.f8621p0;
                    RectF rectF = new RectF(width, (this.f8596c.bottom + this.W) - this.f8621p0, rect3.width() + width + (this.f8621p0 * 2), this.f8596c.bottom + this.W + this.f8619o0.getHeight() + this.f8621p0);
                    int i8 = this.f8621p0;
                    canvas.drawRoundRect(rectF, i8, i8, this.f8602f);
                } else {
                    int i9 = this.f8596c.bottom;
                    int i10 = this.W;
                    RectF rectF2 = new RectF(rect2.left, (i9 + i10) - this.f8621p0, rect2.right, i9 + i10 + this.f8619o0.getHeight() + this.f8621p0);
                    int i11 = this.f8621p0;
                    canvas.drawRoundRect(rectF2, i11, i11, this.f8602f);
                }
            }
            canvas.save();
            if (this.f8593a0) {
                height2 = this.f8596c.bottom + this.W;
                canvas.translate(0.0f, height2);
            } else {
                Rect rect4 = this.f8596c;
                f5 = rect4.left + this.f8621p0;
                height = rect4.bottom + this.W;
                canvas.translate(f5, height);
            }
        } else {
            if (this.f8597c0) {
                this.f8602f.setColor(this.f8595b0);
                this.f8602f.setStyle(Paint.Style.FILL);
                if (this.f8593a0) {
                    Rect rect5 = new Rect();
                    TextPaint textPaint2 = this.f8604g;
                    String str2 = this.P;
                    textPaint2.getTextBounds(str2, 0, str2.length(), rect5);
                    float width2 = ((canvas.getWidth() - rect5.width()) / 2) - this.f8621p0;
                    int i12 = this.f8621p0;
                    RectF rectF3 = new RectF(width2, ((this.f8596c.top - this.W) - this.f8619o0.getHeight()) - this.f8621p0, rect5.width() + width2 + (i12 * 2), (this.f8596c.top - this.W) + i12);
                    int i13 = this.f8621p0;
                    canvas.drawRoundRect(rectF3, i13, i13, this.f8602f);
                } else {
                    int height3 = (this.f8596c.top - this.W) - this.f8619o0.getHeight();
                    int i14 = this.f8621p0;
                    Rect rect6 = this.f8596c;
                    RectF rectF4 = new RectF(rect.left, height3 - i14, rect6.right, (rect6.top - this.W) + i14);
                    int i15 = this.f8621p0;
                    canvas.drawRoundRect(rectF4, i15, i15, this.f8602f);
                }
            }
            canvas.save();
            if (this.f8593a0) {
                height2 = (this.f8596c.top - this.W) - this.f8619o0.getHeight();
                canvas.translate(0.0f, height2);
            } else {
                Rect rect7 = this.f8596c;
                f5 = rect7.left + this.f8621p0;
                height = (rect7.top - this.W) - this.f8619o0.getHeight();
                canvas.translate(f5, height);
            }
        }
        this.f8619o0.draw(canvas);
        canvas.restore();
    }

    private void j(int i8, TypedArray typedArray) {
        if (i8 == g.H) {
            this.q = typedArray.getDimensionPixelSize(i8, this.q);
        } else if (i8 == g.f8576j) {
            this.f8613l = typedArray.getDimensionPixelSize(i8, this.f8613l);
        } else if (i8 == g.f8575i) {
            this.f8611k = typedArray.getDimensionPixelSize(i8, this.f8611k);
        } else if (i8 == g.B) {
            this.f8625t = typedArray.getDimensionPixelSize(i8, this.f8625t);
        } else if (i8 == g.f8590y) {
            this.f8615m = typedArray.getDimensionPixelSize(i8, this.f8615m);
        } else if (i8 == g.f8588w) {
            this.f8606h = typedArray.getColor(i8, this.f8606h);
        } else if (i8 == g.f8573g) {
            this.f8609j = typedArray.getColor(i8, this.f8609j);
        } else if (i8 == g.f8591z) {
            this.f8627w = typedArray.getColor(i8, this.f8627w);
        } else if (i8 == g.A) {
            this.f8628x = typedArray.getDimensionPixelSize(i8, this.f8628x);
        } else if (i8 == g.f8583r) {
            this.f8629y = typedArray.getBoolean(i8, this.f8629y);
        } else if (i8 == g.f8578l) {
            this.f8630z = typedArray.getDrawable(i8);
        } else if (i8 == g.f8572f) {
            this.B = typedArray.getDimensionPixelSize(i8, this.B);
        } else if (i8 == g.f8571e) {
            this.C = typedArray.getColor(i8, this.C);
        } else if (i8 == g.f8568b) {
            this.E = typedArray.getInteger(i8, this.E);
        } else if (i8 == g.I) {
            this.F = typedArray.getFloat(i8, this.F);
        } else if (i8 == g.f8574h) {
            this.G = typedArray.getInteger(i8, this.G);
        } else if (i8 == g.G) {
            this.H = typedArray.getDimensionPixelSize(i8, this.H);
        } else if (i8 == g.f8570d) {
            this.f8620p = typedArray.getDimensionPixelSize(i8, this.f8620p);
        } else if (i8 == g.f8580n) {
            this.K = typedArray.getBoolean(i8, this.K);
        } else if (i8 == g.f8569c) {
            this.O = typedArray.getString(i8);
        } else if (i8 == g.f8589x) {
            this.L = typedArray.getString(i8);
        } else if (i8 == g.F) {
            this.Q = typedArray.getDimensionPixelSize(i8, this.Q);
        } else if (i8 == g.D) {
            this.R = typedArray.getColor(i8, this.R);
        } else if (i8 == g.f8587v) {
            this.T = typedArray.getBoolean(i8, this.T);
        } else if (i8 == g.E) {
            this.W = typedArray.getDimensionPixelSize(i8, this.W);
        } else if (i8 == g.f8586u) {
            this.f8593a0 = typedArray.getBoolean(i8, this.f8593a0);
        } else if (i8 == g.f8585t) {
            this.f8597c0 = typedArray.getBoolean(i8, this.f8597c0);
        } else if (i8 == g.C) {
            this.f8595b0 = typedArray.getColor(i8, this.f8595b0);
        } else if (i8 == g.f8582p) {
            this.f8599d0 = typedArray.getBoolean(i8, this.f8599d0);
        } else if (i8 == g.q) {
            this.f8601e0 = typedArray.getBoolean(i8, this.f8601e0);
        } else if (i8 == g.f8577k) {
            this.f8603f0 = typedArray.getDrawable(i8);
        } else if (i8 == g.f8581o) {
            this.f8622q0 = typedArray.getBoolean(i8, this.f8622q0);
        } else if (i8 == g.f8584s) {
            this.f8623r0 = typedArray.getBoolean(i8, this.f8623r0);
        } else if (i8 == g.f8579m) {
            this.f8624s0 = typedArray.getBoolean(i8, this.f8624s0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0036, code lost:
        if (r1 >= (r2.left + r4)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a4, code lost:
        if (r1 >= (r2.top + r4)) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void n() {
        /*
            Method dump skipped, instructions count: 236
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.bingoogolapple.qrcode.core.h.n():void");
    }

    private void o() {
        int i8;
        if (this.f8603f0 != null || this.f8601e0) {
            this.f8605g0 = this.K ? this.f8616m0 : this.f8614l0;
        } else if (this.f8630z != null || this.f8629y) {
            this.A = this.K ? this.f8612k0 : this.f8610j0;
        }
        if (this.K) {
            this.P = this.O;
            this.f8617n = this.f8620p;
            i8 = (int) (((this.E * 1.0f) * this.f8592a) / this.f8615m);
        } else {
            this.P = this.L;
            int i9 = this.f8615m;
            this.f8617n = i9;
            i8 = (int) (((this.E * 1.0f) * this.f8592a) / i9);
        }
        this.f8594b = i8;
        if (!TextUtils.isEmpty(this.P)) {
            this.f8619o0 = this.f8593a0 ? new StaticLayout(this.P, this.f8604g, a.j(getContext()).x, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true) : new StaticLayout(this.P, this.f8604g, this.f8615m - (this.f8621p0 * 2), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        }
        if (this.F != -1.0f) {
            int k8 = a.j(getContext()).y - a.k(getContext());
            int i10 = this.H;
            if (i10 == 0) {
                this.q = (int) ((k8 * this.F) - (this.f8617n / 2));
            } else {
                this.q = i10 + ((int) (((k8 - i10) * this.F) - (this.f8617n / 2)));
            }
        }
        b();
        postInvalidate();
    }

    public int getAnimTime() {
        return this.E;
    }

    public String getBarCodeTipText() {
        return this.O;
    }

    public int getBarcodeRectHeight() {
        return this.f8620p;
    }

    public int getBorderColor() {
        return this.C;
    }

    public int getBorderSize() {
        return this.B;
    }

    public int getCornerColor() {
        return this.f8609j;
    }

    public int getCornerLength() {
        return this.f8611k;
    }

    public int getCornerSize() {
        return this.f8613l;
    }

    public Drawable getCustomScanLineDrawable() {
        return this.f8630z;
    }

    public float getHalfCornerSize() {
        return this.f8618n0;
    }

    public boolean getIsBarcode() {
        return this.K;
    }

    public int getMaskColor() {
        return this.f8606h;
    }

    public String getQRCodeTipText() {
        return this.L;
    }

    public int getRectHeight() {
        return this.f8617n;
    }

    public int getRectWidth() {
        return this.f8615m;
    }

    public Bitmap getScanLineBitmap() {
        return this.A;
    }

    public int getScanLineColor() {
        return this.f8627w;
    }

    public int getScanLineMargin() {
        return this.f8628x;
    }

    public int getScanLineSize() {
        return this.f8625t;
    }

    public int getTipBackgroundColor() {
        return this.f8595b0;
    }

    public int getTipBackgroundRadius() {
        return this.f8621p0;
    }

    public String getTipText() {
        return this.P;
    }

    public int getTipTextColor() {
        return this.R;
    }

    public int getTipTextMargin() {
        return this.W;
    }

    public int getTipTextSize() {
        return this.Q;
    }

    public StaticLayout getTipTextSl() {
        return this.f8619o0;
    }

    public int getToolbarHeight() {
        return this.H;
    }

    public int getTopOffset() {
        return this.q;
    }

    public float getVerticalBias() {
        return this.F;
    }

    public Rect h(int i8) {
        if (this.f8622q0 && getVisibility() == 0) {
            Rect rect = new Rect(this.f8596c);
            float measuredHeight = (i8 * 1.0f) / getMeasuredHeight();
            float exactCenterX = rect.exactCenterX() * measuredHeight;
            float exactCenterY = rect.exactCenterY() * measuredHeight;
            float width = (rect.width() / 2.0f) * measuredHeight;
            float height = (rect.height() / 2.0f) * measuredHeight;
            rect.left = (int) (exactCenterX - width);
            rect.right = (int) (exactCenterX + width);
            rect.top = (int) (exactCenterY - height);
            rect.bottom = (int) (exactCenterY + height);
            return rect;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(QRCodeView qRCodeView, AttributeSet attributeSet) {
        this.f8626t0 = qRCodeView;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, g.f8567a);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i8 = 0; i8 < indexCount; i8++) {
            j(obtainStyledAttributes.getIndex(i8), obtainStyledAttributes);
        }
        obtainStyledAttributes.recycle();
        a();
    }

    public boolean k() {
        return this.f8624s0;
    }

    public boolean l() {
        return this.f8622q0;
    }

    public boolean m() {
        return this.f8623r0;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.f8596c == null) {
            return;
        }
        e(canvas);
        c(canvas);
        d(canvas);
        f(canvas);
        g(canvas);
        n();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        b();
    }

    public void setAnimTime(int i8) {
        this.E = i8;
        o();
    }

    public void setAutoZoom(boolean z4) {
        this.f8624s0 = z4;
    }

    public void setBarCodeTipText(String str) {
        this.O = str;
        o();
    }

    public void setBarcodeRectHeight(int i8) {
        this.f8620p = i8;
        o();
    }

    public void setBorderColor(int i8) {
        this.C = i8;
        o();
    }

    public void setBorderSize(int i8) {
        this.B = i8;
        o();
    }

    public void setCornerColor(int i8) {
        this.f8609j = i8;
        o();
    }

    public void setCornerLength(int i8) {
        this.f8611k = i8;
        o();
    }

    public void setCornerSize(int i8) {
        this.f8613l = i8;
        o();
    }

    public void setCustomScanLineDrawable(Drawable drawable) {
        this.f8630z = drawable;
        o();
    }

    public void setHalfCornerSize(float f5) {
        this.f8618n0 = f5;
        o();
    }

    public void setIsBarcode(boolean z4) {
        this.K = z4;
        o();
    }

    public void setMaskColor(int i8) {
        this.f8606h = i8;
        o();
    }

    public void setOnlyDecodeScanBoxArea(boolean z4) {
        this.f8622q0 = z4;
        b();
    }

    public void setQRCodeTipText(String str) {
        this.L = str;
        o();
    }

    public void setRectHeight(int i8) {
        this.f8617n = i8;
        o();
    }

    public void setRectWidth(int i8) {
        this.f8615m = i8;
        o();
    }

    public void setScanLineBitmap(Bitmap bitmap) {
        this.A = bitmap;
        o();
    }

    public void setScanLineColor(int i8) {
        this.f8627w = i8;
        o();
    }

    public void setScanLineMargin(int i8) {
        this.f8628x = i8;
        o();
    }

    public void setScanLineReverse(boolean z4) {
        this.f8599d0 = z4;
        o();
    }

    public void setScanLineSize(int i8) {
        this.f8625t = i8;
        o();
    }

    public void setShowDefaultGridScanLineDrawable(boolean z4) {
        this.f8601e0 = z4;
        o();
    }

    public void setShowDefaultScanLineDrawable(boolean z4) {
        this.f8629y = z4;
        o();
    }

    public void setShowLocationPoint(boolean z4) {
        this.f8623r0 = z4;
    }

    public void setShowTipBackground(boolean z4) {
        this.f8597c0 = z4;
        o();
    }

    public void setShowTipTextAsSingleLine(boolean z4) {
        this.f8593a0 = z4;
        o();
    }

    public void setTipBackgroundColor(int i8) {
        this.f8595b0 = i8;
        o();
    }

    public void setTipBackgroundRadius(int i8) {
        this.f8621p0 = i8;
        o();
    }

    public void setTipText(String str) {
        if (this.K) {
            this.O = str;
        } else {
            this.L = str;
        }
        o();
    }

    public void setTipTextBelowRect(boolean z4) {
        this.T = z4;
        o();
    }

    public void setTipTextColor(int i8) {
        this.R = i8;
        this.f8604g.setColor(i8);
        o();
    }

    public void setTipTextMargin(int i8) {
        this.W = i8;
        o();
    }

    public void setTipTextSize(int i8) {
        this.Q = i8;
        this.f8604g.setTextSize(i8);
        o();
    }

    public void setTipTextSl(StaticLayout staticLayout) {
        this.f8619o0 = staticLayout;
        o();
    }

    public void setToolbarHeight(int i8) {
        this.H = i8;
        o();
    }

    public void setTopOffset(int i8) {
        this.q = i8;
        o();
    }

    public void setVerticalBias(float f5) {
        this.F = f5;
        o();
    }
}
