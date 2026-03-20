package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImageFilterView extends AppCompatImageView {

    /* renamed from: d  reason: collision with root package name */
    private c f3874d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f3875e;

    /* renamed from: f  reason: collision with root package name */
    private float f3876f;

    /* renamed from: g  reason: collision with root package name */
    private float f3877g;

    /* renamed from: h  reason: collision with root package name */
    private float f3878h;

    /* renamed from: j  reason: collision with root package name */
    private Path f3879j;

    /* renamed from: k  reason: collision with root package name */
    ViewOutlineProvider f3880k;

    /* renamed from: l  reason: collision with root package name */
    RectF f3881l;

    /* renamed from: m  reason: collision with root package name */
    Drawable[] f3882m;

    /* renamed from: n  reason: collision with root package name */
    LayerDrawable f3883n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            int width = ImageFilterView.this.getWidth();
            int height = ImageFilterView.this.getHeight();
            outline.setRoundRect(0, 0, width, height, (Math.min(width, height) * ImageFilterView.this.f3877g) / 2.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends ViewOutlineProvider {
        b() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterView.this.getWidth(), ImageFilterView.this.getHeight(), ImageFilterView.this.f3878h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        float[] f3886a = new float[20];

        /* renamed from: b  reason: collision with root package name */
        ColorMatrix f3887b = new ColorMatrix();

        /* renamed from: c  reason: collision with root package name */
        ColorMatrix f3888c = new ColorMatrix();

        /* renamed from: d  reason: collision with root package name */
        float f3889d = 1.0f;

        /* renamed from: e  reason: collision with root package name */
        float f3890e = 1.0f;

        /* renamed from: f  reason: collision with root package name */
        float f3891f = 1.0f;

        /* renamed from: g  reason: collision with root package name */
        float f3892g = 1.0f;

        private void a(float f5) {
            float[] fArr = this.f3886a;
            fArr[0] = f5;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = f5;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = f5;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        private void b(float f5) {
            float f8 = 1.0f - f5;
            float f9 = 0.2999f * f8;
            float f10 = 0.587f * f8;
            float f11 = f8 * 0.114f;
            float[] fArr = this.f3886a;
            fArr[0] = f9 + f5;
            fArr[1] = f10;
            fArr[2] = f11;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = f9;
            fArr[6] = f10 + f5;
            fArr[7] = f11;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = f9;
            fArr[11] = f10;
            fArr[12] = f11 + f5;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        private void d(float f5) {
            float log;
            float f8;
            if (f5 <= 0.0f) {
                f5 = 0.01f;
            }
            float f9 = (5000.0f / f5) / 100.0f;
            if (f9 > 66.0f) {
                double d8 = f9 - 60.0f;
                f8 = ((float) Math.pow(d8, -0.13320475816726685d)) * 329.69873f;
                log = ((float) Math.pow(d8, 0.07551484555006027d)) * 288.12216f;
            } else {
                log = (((float) Math.log(f9)) * 99.4708f) - 161.11957f;
                f8 = 255.0f;
            }
            float log2 = f9 < 66.0f ? f9 > 19.0f ? (((float) Math.log(f9 - 10.0f)) * 138.51773f) - 305.0448f : 0.0f : 255.0f;
            float min = Math.min(255.0f, Math.max(f8, 0.0f));
            float min2 = Math.min(255.0f, Math.max(log, 0.0f));
            float min3 = Math.min(255.0f, Math.max(log2, 0.0f));
            float min4 = Math.min(255.0f, Math.max(255.0f, 0.0f));
            float min5 = Math.min(255.0f, Math.max((((float) Math.log(50.0f)) * 99.4708f) - 161.11957f, 0.0f));
            float min6 = min3 / Math.min(255.0f, Math.max((((float) Math.log(40.0f)) * 138.51773f) - 305.0448f, 0.0f));
            float[] fArr = this.f3886a;
            fArr[0] = min / min4;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = min2 / min5;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = min6;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void c(ImageView imageView) {
            boolean z4;
            this.f3887b.reset();
            float f5 = this.f3890e;
            boolean z8 = true;
            if (f5 != 1.0f) {
                b(f5);
                this.f3887b.set(this.f3886a);
                z4 = true;
            } else {
                z4 = false;
            }
            float f8 = this.f3891f;
            if (f8 != 1.0f) {
                this.f3888c.setScale(f8, f8, f8, 1.0f);
                this.f3887b.postConcat(this.f3888c);
                z4 = true;
            }
            float f9 = this.f3892g;
            if (f9 != 1.0f) {
                d(f9);
                this.f3888c.set(this.f3886a);
                this.f3887b.postConcat(this.f3888c);
                z4 = true;
            }
            float f10 = this.f3889d;
            if (f10 != 1.0f) {
                a(f10);
                this.f3888c.set(this.f3886a);
                this.f3887b.postConcat(this.f3888c);
            } else {
                z8 = z4;
            }
            if (z8) {
                imageView.setColorFilter(new ColorMatrixColorFilter(this.f3887b));
            } else {
                imageView.clearColorFilter();
            }
        }
    }

    public ImageFilterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3874d = new c();
        this.f3875e = true;
        this.f3876f = 0.0f;
        this.f3877g = 0.0f;
        this.f3878h = Float.NaN;
        e(context, attributeSet);
    }

    public ImageFilterView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3874d = new c();
        this.f3875e = true;
        this.f3876f = 0.0f;
        this.f3877g = 0.0f;
        this.f3878h = Float.NaN;
        e(context, attributeSet);
    }

    private void e(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4272r3);
            int indexCount = obtainStyledAttributes.getIndexCount();
            Drawable drawable = obtainStyledAttributes.getDrawable(e.f4280s3);
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4298u3) {
                    this.f3876f = obtainStyledAttributes.getFloat(index, 0.0f);
                } else if (index == e.f4343z3) {
                    setWarmth(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == e.f4334y3) {
                    setSaturation(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == e.f4289t3) {
                    setContrast(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == e.f4316w3) {
                    setRound(obtainStyledAttributes.getDimension(index, 0.0f));
                } else if (index == e.f4325x3) {
                    setRoundPercent(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == e.f4307v3) {
                    setOverlay(obtainStyledAttributes.getBoolean(index, this.f3875e));
                }
            }
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                Drawable[] drawableArr = new Drawable[2];
                this.f3882m = drawableArr;
                drawableArr[0] = getDrawable();
                this.f3882m[1] = drawable;
                LayerDrawable layerDrawable = new LayerDrawable(this.f3882m);
                this.f3883n = layerDrawable;
                layerDrawable.getDrawable(1).setAlpha((int) (this.f3876f * 255.0f));
                super.setImageDrawable(this.f3883n);
            }
        }
    }

    private void setOverlay(boolean z4) {
        this.f3875e = z4;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        boolean z4;
        if (Build.VERSION.SDK_INT >= 21 || this.f3877g == 0.0f || this.f3879j == null) {
            z4 = false;
        } else {
            z4 = true;
            canvas.save();
            canvas.clipPath(this.f3879j);
        }
        super.draw(canvas);
        if (z4) {
            canvas.restore();
        }
    }

    public float getBrightness() {
        return this.f3874d.f3889d;
    }

    public float getContrast() {
        return this.f3874d.f3891f;
    }

    public float getCrossfade() {
        return this.f3876f;
    }

    public float getRound() {
        return this.f3878h;
    }

    public float getRoundPercent() {
        return this.f3877g;
    }

    public float getSaturation() {
        return this.f3874d.f3890e;
    }

    public float getWarmth() {
        return this.f3874d.f3892g;
    }

    public void setBrightness(float f5) {
        c cVar = this.f3874d;
        cVar.f3889d = f5;
        cVar.c(this);
    }

    public void setContrast(float f5) {
        c cVar = this.f3874d;
        cVar.f3891f = f5;
        cVar.c(this);
    }

    public void setCrossfade(float f5) {
        this.f3876f = f5;
        if (this.f3882m != null) {
            if (!this.f3875e) {
                this.f3883n.getDrawable(0).setAlpha((int) ((1.0f - this.f3876f) * 255.0f));
            }
            this.f3883n.getDrawable(1).setAlpha((int) (this.f3876f * 255.0f));
            super.setImageDrawable(this.f3883n);
        }
    }

    public void setRound(float f5) {
        if (Float.isNaN(f5)) {
            this.f3878h = f5;
            float f8 = this.f3877g;
            this.f3877g = -1.0f;
            setRoundPercent(f8);
            return;
        }
        boolean z4 = this.f3878h != f5;
        this.f3878h = f5;
        if (f5 != 0.0f) {
            if (this.f3879j == null) {
                this.f3879j = new Path();
            }
            if (this.f3881l == null) {
                this.f3881l = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.f3880k == null) {
                    b bVar = new b();
                    this.f3880k = bVar;
                    setOutlineProvider(bVar);
                }
                setClipToOutline(true);
            }
            this.f3881l.set(0.0f, 0.0f, getWidth(), getHeight());
            this.f3879j.reset();
            Path path = this.f3879j;
            RectF rectF = this.f3881l;
            float f9 = this.f3878h;
            path.addRoundRect(rectF, f9, f9, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (!z4 || Build.VERSION.SDK_INT < 21) {
            return;
        }
        invalidateOutline();
    }

    public void setRoundPercent(float f5) {
        boolean z4 = this.f3877g != f5;
        this.f3877g = f5;
        if (f5 != 0.0f) {
            if (this.f3879j == null) {
                this.f3879j = new Path();
            }
            if (this.f3881l == null) {
                this.f3881l = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.f3880k == null) {
                    a aVar = new a();
                    this.f3880k = aVar;
                    setOutlineProvider(aVar);
                }
                setClipToOutline(true);
            }
            int width = getWidth();
            int height = getHeight();
            float min = (Math.min(width, height) * this.f3877g) / 2.0f;
            this.f3881l.set(0.0f, 0.0f, width, height);
            this.f3879j.reset();
            this.f3879j.addRoundRect(this.f3881l, min, min, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (!z4 || Build.VERSION.SDK_INT < 21) {
            return;
        }
        invalidateOutline();
    }

    public void setSaturation(float f5) {
        c cVar = this.f3874d;
        cVar.f3890e = f5;
        cVar.c(this);
    }

    public void setWarmth(float f5) {
        c cVar = this.f3874d;
        cVar.f3892g = f5;
        cVar.c(this);
    }
}
