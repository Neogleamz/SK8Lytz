package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MotionHelper extends ConstraintHelper implements MotionLayout.i {

    /* renamed from: j  reason: collision with root package name */
    private boolean f3158j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f3159k;

    /* renamed from: l  reason: collision with root package name */
    private float f3160l;

    /* renamed from: m  reason: collision with root package name */
    protected View[] f3161m;

    public MotionHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3158j = false;
        this.f3159k = false;
        m(attributeSet);
    }

    public MotionHelper(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3158j = false;
        this.f3159k = false;
        m(attributeSet);
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void a(MotionLayout motionLayout, int i8, int i9, float f5) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void b(MotionLayout motionLayout, int i8, int i9) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void c(MotionLayout motionLayout, int i8, boolean z4, float f5) {
    }

    @Override // androidx.constraintlayout.motion.widget.MotionLayout.i
    public void d(MotionLayout motionLayout, int i8) {
    }

    public float getProgress() {
        return this.f3160l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.E6);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == androidx.constraintlayout.widget.e.G6) {
                    this.f3158j = obtainStyledAttributes.getBoolean(index, this.f3158j);
                } else if (index == androidx.constraintlayout.widget.e.F6) {
                    this.f3159k = obtainStyledAttributes.getBoolean(index, this.f3159k);
                }
            }
        }
    }

    public void setProgress(float f5) {
        this.f3160l = f5;
        int i8 = 0;
        if (this.f3930b > 0) {
            this.f3161m = l((ConstraintLayout) getParent());
            while (i8 < this.f3930b) {
                x(this.f3161m[i8], f5);
                i8++;
            }
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        int childCount = viewGroup.getChildCount();
        while (i8 < childCount) {
            View childAt = viewGroup.getChildAt(i8);
            if (!(childAt instanceof MotionHelper)) {
                x(childAt, f5);
            }
            i8++;
        }
    }

    public boolean v() {
        return this.f3159k;
    }

    public boolean w() {
        return this.f3158j;
    }

    public void x(View view, float f5) {
    }
}
