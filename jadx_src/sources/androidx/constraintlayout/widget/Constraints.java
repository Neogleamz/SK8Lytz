package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Constraints extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    b f4006a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ConstraintLayout.LayoutParams {
        public float A0;
        public float B0;

        /* renamed from: p0  reason: collision with root package name */
        public float f4007p0;

        /* renamed from: q0  reason: collision with root package name */
        public boolean f4008q0;

        /* renamed from: r0  reason: collision with root package name */
        public float f4009r0;

        /* renamed from: s0  reason: collision with root package name */
        public float f4010s0;

        /* renamed from: t0  reason: collision with root package name */
        public float f4011t0;

        /* renamed from: u0  reason: collision with root package name */
        public float f4012u0;

        /* renamed from: v0  reason: collision with root package name */
        public float f4013v0;

        /* renamed from: w0  reason: collision with root package name */
        public float f4014w0;

        /* renamed from: x0  reason: collision with root package name */
        public float f4015x0;

        /* renamed from: y0  reason: collision with root package name */
        public float f4016y0;

        /* renamed from: z0  reason: collision with root package name */
        public float f4017z0;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f4007p0 = 1.0f;
            this.f4008q0 = false;
            this.f4009r0 = 0.0f;
            this.f4010s0 = 0.0f;
            this.f4011t0 = 0.0f;
            this.f4012u0 = 0.0f;
            this.f4013v0 = 1.0f;
            this.f4014w0 = 1.0f;
            this.f4015x0 = 0.0f;
            this.f4016y0 = 0.0f;
            this.f4017z0 = 0.0f;
            this.A0 = 0.0f;
            this.B0 = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f4007p0 = 1.0f;
            this.f4008q0 = false;
            this.f4009r0 = 0.0f;
            this.f4010s0 = 0.0f;
            this.f4011t0 = 0.0f;
            this.f4012u0 = 0.0f;
            this.f4013v0 = 1.0f;
            this.f4014w0 = 1.0f;
            this.f4015x0 = 0.0f;
            this.f4016y0 = 0.0f;
            this.f4017z0 = 0.0f;
            this.A0 = 0.0f;
            this.B0 = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, e.P2);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.Q2) {
                    this.f4007p0 = obtainStyledAttributes.getFloat(index, this.f4007p0);
                } else if (index == e.f4129b3) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.f4009r0 = obtainStyledAttributes.getFloat(index, this.f4009r0);
                        this.f4008q0 = true;
                    }
                } else if (index == e.Y2) {
                    this.f4011t0 = obtainStyledAttributes.getFloat(index, this.f4011t0);
                } else if (index == e.Z2) {
                    this.f4012u0 = obtainStyledAttributes.getFloat(index, this.f4012u0);
                } else if (index == e.X2) {
                    this.f4010s0 = obtainStyledAttributes.getFloat(index, this.f4010s0);
                } else if (index == e.V2) {
                    this.f4013v0 = obtainStyledAttributes.getFloat(index, this.f4013v0);
                } else if (index == e.W2) {
                    this.f4014w0 = obtainStyledAttributes.getFloat(index, this.f4014w0);
                } else if (index == e.R2) {
                    this.f4015x0 = obtainStyledAttributes.getFloat(index, this.f4015x0);
                } else if (index == e.S2) {
                    this.f4016y0 = obtainStyledAttributes.getFloat(index, this.f4016y0);
                } else if (index == e.T2) {
                    this.f4017z0 = obtainStyledAttributes.getFloat(index, this.f4017z0);
                } else if (index == e.U2) {
                    this.A0 = obtainStyledAttributes.getFloat(index, this.A0);
                } else if (index == e.f4119a3 && Build.VERSION.SDK_INT >= 21) {
                    this.B0 = obtainStyledAttributes.getFloat(index, this.B0);
                }
            }
        }
    }

    public Constraints(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        c(attributeSet);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        c(attributeSet);
        super.setVisibility(8);
    }

    private void c(AttributeSet attributeSet) {
        Log.v("Constraints", " ################# init");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: a */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    /* renamed from: b */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ConstraintLayout.LayoutParams(layoutParams);
    }

    public b getConstraintSet() {
        if (this.f4006a == null) {
            this.f4006a = new b();
        }
        this.f4006a.k(this);
        return this.f4006a;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
    }
}
