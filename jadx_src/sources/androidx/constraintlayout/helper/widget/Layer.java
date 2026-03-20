package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Layer extends ConstraintHelper {
    boolean A;
    View[] B;
    private float C;
    private float E;
    private boolean F;
    private boolean G;

    /* renamed from: j  reason: collision with root package name */
    private float f3147j;

    /* renamed from: k  reason: collision with root package name */
    private float f3148k;

    /* renamed from: l  reason: collision with root package name */
    private float f3149l;

    /* renamed from: m  reason: collision with root package name */
    ConstraintLayout f3150m;

    /* renamed from: n  reason: collision with root package name */
    private float f3151n;

    /* renamed from: p  reason: collision with root package name */
    private float f3152p;
    protected float q;

    /* renamed from: t  reason: collision with root package name */
    protected float f3153t;

    /* renamed from: w  reason: collision with root package name */
    protected float f3154w;

    /* renamed from: x  reason: collision with root package name */
    protected float f3155x;

    /* renamed from: y  reason: collision with root package name */
    protected float f3156y;

    /* renamed from: z  reason: collision with root package name */
    protected float f3157z;

    public Layer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3147j = Float.NaN;
        this.f3148k = Float.NaN;
        this.f3149l = Float.NaN;
        this.f3151n = 1.0f;
        this.f3152p = 1.0f;
        this.q = Float.NaN;
        this.f3153t = Float.NaN;
        this.f3154w = Float.NaN;
        this.f3155x = Float.NaN;
        this.f3156y = Float.NaN;
        this.f3157z = Float.NaN;
        this.A = true;
        this.B = null;
        this.C = 0.0f;
        this.E = 0.0f;
    }

    public Layer(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3147j = Float.NaN;
        this.f3148k = Float.NaN;
        this.f3149l = Float.NaN;
        this.f3151n = 1.0f;
        this.f3152p = 1.0f;
        this.q = Float.NaN;
        this.f3153t = Float.NaN;
        this.f3154w = Float.NaN;
        this.f3155x = Float.NaN;
        this.f3156y = Float.NaN;
        this.f3157z = Float.NaN;
        this.A = true;
        this.B = null;
        this.C = 0.0f;
        this.E = 0.0f;
    }

    private void w() {
        int i8;
        if (this.f3150m == null || (i8 = this.f3930b) == 0) {
            return;
        }
        View[] viewArr = this.B;
        if (viewArr == null || viewArr.length != i8) {
            this.B = new View[i8];
        }
        for (int i9 = 0; i9 < this.f3930b; i9++) {
            this.B[i9] = this.f3150m.h(this.f3929a[i9]);
        }
    }

    private void x() {
        if (this.f3150m == null) {
            return;
        }
        if (this.B == null) {
            w();
        }
        v();
        double radians = Math.toRadians(this.f3149l);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        float f5 = this.f3151n;
        float f8 = f5 * cos;
        float f9 = this.f3152p;
        float f10 = (-f9) * sin;
        float f11 = f5 * sin;
        float f12 = f9 * cos;
        for (int i8 = 0; i8 < this.f3930b; i8++) {
            View view = this.B[i8];
            float left = ((view.getLeft() + view.getRight()) / 2) - this.q;
            float top = ((view.getTop() + view.getBottom()) / 2) - this.f3153t;
            view.setTranslationX((((f8 * left) + (f10 * top)) - left) + this.C);
            view.setTranslationY((((left * f11) + (f12 * top)) - top) + this.E);
            view.setScaleY(this.f3152p);
            view.setScaleX(this.f3151n);
            view.setRotation(this.f3149l);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f3933e = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4117a1);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4182h1) {
                    this.F = true;
                } else if (index == e.f4245o1) {
                    this.G = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f3150m = (ConstraintLayout) getParent();
        if (this.F || this.G) {
            int visibility = getVisibility();
            float elevation = Build.VERSION.SDK_INT >= 21 ? getElevation() : 0.0f;
            for (int i8 = 0; i8 < this.f3930b; i8++) {
                View h8 = this.f3150m.h(this.f3929a[i8]);
                if (h8 != null) {
                    if (this.F) {
                        h8.setVisibility(visibility);
                    }
                    if (this.G && elevation > 0.0f && Build.VERSION.SDK_INT >= 21) {
                        h8.setTranslationZ(h8.getTranslationZ() + elevation);
                    }
                }
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void p(ConstraintLayout constraintLayout) {
        w();
        this.q = Float.NaN;
        this.f3153t = Float.NaN;
        ConstraintWidget b9 = ((ConstraintLayout.LayoutParams) getLayoutParams()).b();
        b9.F0(0);
        b9.i0(0);
        v();
        layout(((int) this.f3156y) - getPaddingLeft(), ((int) this.f3157z) - getPaddingTop(), ((int) this.f3154w) + getPaddingRight(), ((int) this.f3155x) + getPaddingBottom());
        if (Float.isNaN(this.f3149l)) {
            return;
        }
        x();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void r(ConstraintLayout constraintLayout) {
        this.f3150m = constraintLayout;
        float rotation = getRotation();
        if (rotation == 0.0f && Float.isNaN(this.f3149l)) {
            return;
        }
        this.f3149l = rotation;
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        g();
    }

    @Override // android.view.View
    public void setPivotX(float f5) {
        this.f3147j = f5;
        x();
    }

    @Override // android.view.View
    public void setPivotY(float f5) {
        this.f3148k = f5;
        x();
    }

    @Override // android.view.View
    public void setRotation(float f5) {
        this.f3149l = f5;
        x();
    }

    @Override // android.view.View
    public void setScaleX(float f5) {
        this.f3151n = f5;
        x();
    }

    @Override // android.view.View
    public void setScaleY(float f5) {
        this.f3152p = f5;
        x();
    }

    @Override // android.view.View
    public void setTranslationX(float f5) {
        this.C = f5;
        x();
    }

    @Override // android.view.View
    public void setTranslationY(float f5) {
        this.E = f5;
        x();
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        g();
    }

    protected void v() {
        if (this.f3150m == null) {
            return;
        }
        if (this.A || Float.isNaN(this.q) || Float.isNaN(this.f3153t)) {
            if (!Float.isNaN(this.f3147j) && !Float.isNaN(this.f3148k)) {
                this.f3153t = this.f3148k;
                this.q = this.f3147j;
                return;
            }
            View[] l8 = l(this.f3150m);
            int left = l8[0].getLeft();
            int top = l8[0].getTop();
            int right = l8[0].getRight();
            int bottom = l8[0].getBottom();
            for (int i8 = 0; i8 < this.f3930b; i8++) {
                View view = l8[i8];
                left = Math.min(left, view.getLeft());
                top = Math.min(top, view.getTop());
                right = Math.max(right, view.getRight());
                bottom = Math.max(bottom, view.getBottom());
            }
            this.f3154w = right;
            this.f3155x = bottom;
            this.f3156y = left;
            this.f3157z = top;
            this.q = Float.isNaN(this.f3147j) ? (left + right) / 2 : this.f3147j;
            this.f3153t = Float.isNaN(this.f3148k) ? (top + bottom) / 2 : this.f3148k;
        }
    }
}
