package androidx.constraintlayout.helper.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.e;
import androidx.constraintlayout.solver.widgets.h;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.VirtualLayout;
import androidx.constraintlayout.widget.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Flow extends VirtualLayout {

    /* renamed from: l  reason: collision with root package name */
    private e f3146l;

    public Flow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Flow(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.VirtualLayout, androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f3146l = new e();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.f4117a1);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == androidx.constraintlayout.widget.e.f4127b1) {
                    this.f3146l.T1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4137c1) {
                    this.f3146l.Z0(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4227m1) {
                    this.f3146l.e1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4236n1) {
                    this.f3146l.b1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4147d1) {
                    this.f3146l.c1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4156e1) {
                    this.f3146l.f1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4165f1) {
                    this.f3146l.d1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4173g1) {
                    this.f3146l.a1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.L1) {
                    this.f3146l.Y1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.B1) {
                    this.f3146l.N1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.K1) {
                    this.f3146l.X1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4305v1) {
                    this.f3146l.H1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.D1) {
                    this.f3146l.P1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4323x1) {
                    this.f3146l.J1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.F1) {
                    this.f3146l.R1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.f4341z1) {
                    this.f3146l.L1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.f4296u1) {
                    this.f3146l.G1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.C1) {
                    this.f3146l.O1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.f4314w1) {
                    this.f3146l.I1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.E1) {
                    this.f3146l.Q1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.I1) {
                    this.f3146l.V1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == androidx.constraintlayout.widget.e.f4332y1) {
                    this.f3146l.K1(obtainStyledAttributes.getInt(index, 2));
                } else if (index == androidx.constraintlayout.widget.e.H1) {
                    this.f3146l.U1(obtainStyledAttributes.getInt(index, 2));
                } else if (index == androidx.constraintlayout.widget.e.A1) {
                    this.f3146l.M1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.J1) {
                    this.f3146l.W1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == androidx.constraintlayout.widget.e.G1) {
                    this.f3146l.S1(obtainStyledAttributes.getInt(index, -1));
                }
            }
        }
        this.f3932d = this.f3146l;
        u();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void n(b.a aVar, n0.b bVar, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        super.n(aVar, bVar, layoutParams, sparseArray);
        if (bVar instanceof e) {
            e eVar = (e) bVar;
            int i8 = layoutParams.S;
            if (i8 != -1) {
                eVar.T1(i8);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void o(ConstraintWidget constraintWidget, boolean z4) {
        this.f3146l.L0(z4);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper, android.view.View
    @SuppressLint({"WrongCall"})
    protected void onMeasure(int i8, int i9) {
        v(this.f3146l, i8, i9);
    }

    public void setFirstHorizontalBias(float f5) {
        this.f3146l.G1(f5);
        requestLayout();
    }

    public void setFirstHorizontalStyle(int i8) {
        this.f3146l.H1(i8);
        requestLayout();
    }

    public void setFirstVerticalBias(float f5) {
        this.f3146l.I1(f5);
        requestLayout();
    }

    public void setFirstVerticalStyle(int i8) {
        this.f3146l.J1(i8);
        requestLayout();
    }

    public void setHorizontalAlign(int i8) {
        this.f3146l.K1(i8);
        requestLayout();
    }

    public void setHorizontalBias(float f5) {
        this.f3146l.L1(f5);
        requestLayout();
    }

    public void setHorizontalGap(int i8) {
        this.f3146l.M1(i8);
        requestLayout();
    }

    public void setHorizontalStyle(int i8) {
        this.f3146l.N1(i8);
        requestLayout();
    }

    public void setMaxElementsWrap(int i8) {
        this.f3146l.S1(i8);
        requestLayout();
    }

    public void setOrientation(int i8) {
        this.f3146l.T1(i8);
        requestLayout();
    }

    public void setPadding(int i8) {
        this.f3146l.Z0(i8);
        requestLayout();
    }

    public void setPaddingBottom(int i8) {
        this.f3146l.a1(i8);
        requestLayout();
    }

    public void setPaddingLeft(int i8) {
        this.f3146l.c1(i8);
        requestLayout();
    }

    public void setPaddingRight(int i8) {
        this.f3146l.d1(i8);
        requestLayout();
    }

    public void setPaddingTop(int i8) {
        this.f3146l.f1(i8);
        requestLayout();
    }

    public void setVerticalAlign(int i8) {
        this.f3146l.U1(i8);
        requestLayout();
    }

    public void setVerticalBias(float f5) {
        this.f3146l.V1(f5);
        requestLayout();
    }

    public void setVerticalGap(int i8) {
        this.f3146l.W1(i8);
        requestLayout();
    }

    public void setVerticalStyle(int i8) {
        this.f3146l.X1(i8);
        requestLayout();
    }

    public void setWrapMode(int i8) {
        this.f3146l.Y1(i8);
        requestLayout();
    }

    @Override // androidx.constraintlayout.widget.VirtualLayout
    public void v(h hVar, int i8, int i9) {
        int mode = View.MeasureSpec.getMode(i8);
        int size = View.MeasureSpec.getSize(i8);
        int mode2 = View.MeasureSpec.getMode(i9);
        int size2 = View.MeasureSpec.getSize(i9);
        if (hVar == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        hVar.T0(mode, size, mode2, size2);
        setMeasuredDimension(hVar.O0(), hVar.N0());
    }
}
