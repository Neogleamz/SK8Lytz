package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImageFilterButton extends AppCompatImageButton {

    /* renamed from: d  reason: collision with root package name */
    private ImageFilterView.c f3862d;

    /* renamed from: e  reason: collision with root package name */
    private float f3863e;

    /* renamed from: f  reason: collision with root package name */
    private float f3864f;

    /* renamed from: g  reason: collision with root package name */
    private float f3865g;

    /* renamed from: h  reason: collision with root package name */
    private Path f3866h;

    /* renamed from: j  reason: collision with root package name */
    ViewOutlineProvider f3867j;

    /* renamed from: k  reason: collision with root package name */
    RectF f3868k;

    /* renamed from: l  reason: collision with root package name */
    Drawable[] f3869l;

    /* renamed from: m  reason: collision with root package name */
    LayerDrawable f3870m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f3871n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            int width = ImageFilterButton.this.getWidth();
            int height = ImageFilterButton.this.getHeight();
            outline.setRoundRect(0, 0, width, height, (Math.min(width, height) * ImageFilterButton.this.f3864f) / 2.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends ViewOutlineProvider {
        b() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), ImageFilterButton.this.f3865g);
        }
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3862d = new ImageFilterView.c();
        this.f3863e = 0.0f;
        this.f3864f = 0.0f;
        this.f3865g = Float.NaN;
        this.f3871n = true;
        c(context, attributeSet);
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3862d = new ImageFilterView.c();
        this.f3863e = 0.0f;
        this.f3864f = 0.0f;
        this.f3865g = Float.NaN;
        this.f3871n = true;
        c(context, attributeSet);
    }

    private void c(Context context, AttributeSet attributeSet) {
        setPadding(0, 0, 0, 0);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4272r3);
            int indexCount = obtainStyledAttributes.getIndexCount();
            Drawable drawable = obtainStyledAttributes.getDrawable(e.f4280s3);
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4298u3) {
                    this.f3863e = obtainStyledAttributes.getFloat(index, 0.0f);
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
                    setOverlay(obtainStyledAttributes.getBoolean(index, this.f3871n));
                }
            }
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                Drawable[] drawableArr = new Drawable[2];
                this.f3869l = drawableArr;
                drawableArr[0] = getDrawable();
                this.f3869l[1] = drawable;
                LayerDrawable layerDrawable = new LayerDrawable(this.f3869l);
                this.f3870m = layerDrawable;
                layerDrawable.getDrawable(1).setAlpha((int) (this.f3863e * 255.0f));
                super.setImageDrawable(this.f3870m);
            }
        }
    }

    private void setOverlay(boolean z4) {
        this.f3871n = z4;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        boolean z4;
        if (Build.VERSION.SDK_INT >= 21 || this.f3865g == 0.0f || this.f3866h == null) {
            z4 = false;
        } else {
            z4 = true;
            canvas.save();
            canvas.clipPath(this.f3866h);
        }
        super.draw(canvas);
        if (z4) {
            canvas.restore();
        }
    }

    public float getContrast() {
        return this.f3862d.f3891f;
    }

    public float getCrossfade() {
        return this.f3863e;
    }

    public float getRound() {
        return this.f3865g;
    }

    public float getRoundPercent() {
        return this.f3864f;
    }

    public float getSaturation() {
        return this.f3862d.f3890e;
    }

    public float getWarmth() {
        return this.f3862d.f3892g;
    }

    public void setBrightness(float f5) {
        ImageFilterView.c cVar = this.f3862d;
        cVar.f3889d = f5;
        cVar.c(this);
    }

    public void setContrast(float f5) {
        ImageFilterView.c cVar = this.f3862d;
        cVar.f3891f = f5;
        cVar.c(this);
    }

    public void setCrossfade(float f5) {
        this.f3863e = f5;
        if (this.f3869l != null) {
            if (!this.f3871n) {
                this.f3870m.getDrawable(0).setAlpha((int) ((1.0f - this.f3863e) * 255.0f));
            }
            this.f3870m.getDrawable(1).setAlpha((int) (this.f3863e * 255.0f));
            super.setImageDrawable(this.f3870m);
        }
    }

    public void setRound(float f5) {
        if (Float.isNaN(f5)) {
            this.f3865g = f5;
            float f8 = this.f3864f;
            this.f3864f = -1.0f;
            setRoundPercent(f8);
            return;
        }
        boolean z4 = this.f3865g != f5;
        this.f3865g = f5;
        if (f5 != 0.0f) {
            if (this.f3866h == null) {
                this.f3866h = new Path();
            }
            if (this.f3868k == null) {
                this.f3868k = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.f3867j == null) {
                    b bVar = new b();
                    this.f3867j = bVar;
                    setOutlineProvider(bVar);
                }
                setClipToOutline(true);
            }
            this.f3868k.set(0.0f, 0.0f, getWidth(), getHeight());
            this.f3866h.reset();
            Path path = this.f3866h;
            RectF rectF = this.f3868k;
            float f9 = this.f3865g;
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
        boolean z4 = this.f3864f != f5;
        this.f3864f = f5;
        if (f5 != 0.0f) {
            if (this.f3866h == null) {
                this.f3866h = new Path();
            }
            if (this.f3868k == null) {
                this.f3868k = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.f3867j == null) {
                    a aVar = new a();
                    this.f3867j = aVar;
                    setOutlineProvider(aVar);
                }
                setClipToOutline(true);
            }
            int width = getWidth();
            int height = getHeight();
            float min = (Math.min(width, height) * this.f3864f) / 2.0f;
            this.f3868k.set(0.0f, 0.0f, width, height);
            this.f3866h.reset();
            this.f3866h.addRoundRect(this.f3868k, min, min, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (!z4 || Build.VERSION.SDK_INT < 21) {
            return;
        }
        invalidateOutline();
    }

    public void setSaturation(float f5) {
        ImageFilterView.c cVar = this.f3862d;
        cVar.f3890e = f5;
        cVar.c(this);
    }

    public void setWarmth(float f5) {
        ImageFilterView.c cVar = this.f3862d;
        cVar.f3892g = f5;
        cVar.c(this);
    }
}
