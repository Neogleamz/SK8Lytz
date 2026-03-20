package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Barrier extends ConstraintHelper {

    /* renamed from: j  reason: collision with root package name */
    private int f3910j;

    /* renamed from: k  reason: collision with root package name */
    private int f3911k;

    /* renamed from: l  reason: collision with root package name */
    private androidx.constraintlayout.solver.widgets.a f3912l;

    public Barrier(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        super.setVisibility(8);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001f, code lost:
        if (r7 == 6) goto L5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0025, code lost:
        if (r7 == 6) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0013, code lost:
        if (r7 == 6) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void w(androidx.constraintlayout.solver.widgets.ConstraintWidget r6, int r7, boolean r8) {
        /*
            r5 = this;
            r5.f3911k = r7
            int r7 = android.os.Build.VERSION.SDK_INT
            r0 = 1
            r1 = 0
            r2 = 6
            r3 = 5
            r4 = 17
            if (r7 >= r4) goto L16
            int r7 = r5.f3910j
            if (r7 != r3) goto L13
        L10:
            r5.f3911k = r1
            goto L28
        L13:
            if (r7 != r2) goto L28
        L15:
            goto L1c
        L16:
            int r7 = r5.f3910j
            if (r8 == 0) goto L22
            if (r7 != r3) goto L1f
        L1c:
            r5.f3911k = r0
            goto L28
        L1f:
            if (r7 != r2) goto L28
            goto L10
        L22:
            if (r7 != r3) goto L25
            goto L10
        L25:
            if (r7 != r2) goto L28
            goto L15
        L28:
            boolean r7 = r6 instanceof androidx.constraintlayout.solver.widgets.a
            if (r7 == 0) goto L33
            androidx.constraintlayout.solver.widgets.a r6 = (androidx.constraintlayout.solver.widgets.a) r6
            int r7 = r5.f3911k
            r6.Q0(r7)
        L33:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.Barrier.w(androidx.constraintlayout.solver.widgets.ConstraintWidget, int, boolean):void");
    }

    public int getMargin() {
        return this.f3912l.N0();
    }

    public int getType() {
        return this.f3910j;
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    protected void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f3912l = new androidx.constraintlayout.solver.widgets.a();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4117a1);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4261q1) {
                    setType(obtainStyledAttributes.getInt(index, 0));
                } else if (index == e.f4253p1) {
                    this.f3912l.P0(obtainStyledAttributes.getBoolean(index, true));
                } else if (index == e.f4270r1) {
                    this.f3912l.R0(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                }
            }
        }
        this.f3932d = this.f3912l;
        u();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void n(b.a aVar, n0.b bVar, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        super.n(aVar, bVar, layoutParams, sparseArray);
        if (bVar instanceof androidx.constraintlayout.solver.widgets.a) {
            androidx.constraintlayout.solver.widgets.a aVar2 = (androidx.constraintlayout.solver.widgets.a) bVar;
            w(aVar2, aVar.f4048d.f4055b0, ((androidx.constraintlayout.solver.widgets.d) bVar.H()).c1());
            aVar2.P0(aVar.f4048d.f4071j0);
            aVar2.R0(aVar.f4048d.f4057c0);
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void o(ConstraintWidget constraintWidget, boolean z4) {
        w(constraintWidget, this.f3910j, z4);
    }

    public void setAllowsGoneWidget(boolean z4) {
        this.f3912l.P0(z4);
    }

    public void setDpMargin(int i8) {
        androidx.constraintlayout.solver.widgets.a aVar = this.f3912l;
        aVar.R0((int) ((i8 * getResources().getDisplayMetrics().density) + 0.5f));
    }

    public void setMargin(int i8) {
        this.f3912l.R0(i8);
    }

    public void setType(int i8) {
        this.f3910j = i8;
    }

    public boolean v() {
        return this.f3912l.L0();
    }
}
