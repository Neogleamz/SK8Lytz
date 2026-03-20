package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewParent;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MotionTelltales extends MockView {

    /* renamed from: m  reason: collision with root package name */
    private Paint f3904m;

    /* renamed from: n  reason: collision with root package name */
    MotionLayout f3905n;

    /* renamed from: p  reason: collision with root package name */
    float[] f3906p;
    Matrix q;

    /* renamed from: t  reason: collision with root package name */
    int f3907t;

    /* renamed from: w  reason: collision with root package name */
    int f3908w;

    /* renamed from: x  reason: collision with root package name */
    float f3909x;

    public MotionTelltales(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3904m = new Paint();
        this.f3906p = new float[2];
        this.q = new Matrix();
        this.f3907t = 0;
        this.f3908w = -65281;
        this.f3909x = 0.25f;
        a(context, attributeSet);
    }

    public MotionTelltales(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3904m = new Paint();
        this.f3906p = new float[2];
        this.q = new Matrix();
        this.f3907t = 0;
        this.f3908w = -65281;
        this.f3909x = 0.25f;
        a(context, attributeSet);
    }

    private void a(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, e.R6);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.S6) {
                    this.f3908w = obtainStyledAttributes.getColor(index, this.f3908w);
                } else if (index == e.U6) {
                    this.f3907t = obtainStyledAttributes.getInt(index, this.f3907t);
                } else if (index == e.T6) {
                    this.f3909x = obtainStyledAttributes.getFloat(index, this.f3909x);
                }
            }
        }
        this.f3904m.setColor(this.f3908w);
        this.f3904m.setStrokeWidth(5.0f);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // androidx.constraintlayout.utils.widget.MockView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getMatrix().invert(this.q);
        if (this.f3905n == null) {
            ViewParent parent = getParent();
            if (parent instanceof MotionLayout) {
                this.f3905n = (MotionLayout) parent;
                return;
            }
            return;
        }
        int width = getWidth();
        int height = getHeight();
        float[] fArr = {0.1f, 0.25f, 0.5f, 0.75f, 0.9f};
        for (int i8 = 0; i8 < 5; i8++) {
            float f5 = fArr[i8];
            for (int i9 = 0; i9 < 5; i9++) {
                float f8 = fArr[i9];
                this.f3905n.g0(this, f8, f5, this.f3906p, this.f3907t);
                this.q.mapVectors(this.f3906p);
                float f9 = width * f8;
                float f10 = height * f5;
                float[] fArr2 = this.f3906p;
                float f11 = fArr2[0];
                float f12 = this.f3909x;
                float f13 = f10 - (fArr2[1] * f12);
                this.q.mapVectors(fArr2);
                canvas.drawLine(f9, f10, f9 - (f11 * f12), f13, this.f3904m);
            }
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        postInvalidate();
    }

    public void setText(CharSequence charSequence) {
        this.f3898f = charSequence.toString();
        requestLayout();
    }
}
