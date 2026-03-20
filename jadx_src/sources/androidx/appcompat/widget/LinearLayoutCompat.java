package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.example.seedpoint.R;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LinearLayoutCompat extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    private boolean f1229a;

    /* renamed from: b  reason: collision with root package name */
    private int f1230b;

    /* renamed from: c  reason: collision with root package name */
    private int f1231c;

    /* renamed from: d  reason: collision with root package name */
    private int f1232d;

    /* renamed from: e  reason: collision with root package name */
    private int f1233e;

    /* renamed from: f  reason: collision with root package name */
    private int f1234f;

    /* renamed from: g  reason: collision with root package name */
    private float f1235g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1236h;

    /* renamed from: j  reason: collision with root package name */
    private int[] f1237j;

    /* renamed from: k  reason: collision with root package name */
    private int[] f1238k;

    /* renamed from: l  reason: collision with root package name */
    private Drawable f1239l;

    /* renamed from: m  reason: collision with root package name */
    private int f1240m;

    /* renamed from: n  reason: collision with root package name */
    private int f1241n;

    /* renamed from: p  reason: collision with root package name */
    private int f1242p;
    private int q;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(int i8, int i9) {
            super(i8, i9);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1229a = true;
        this.f1230b = -1;
        this.f1231c = 0;
        this.f1233e = 8388659;
        int[] iArr = g.j.f20059k1;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, v8.r(), i8, 0);
        int k8 = v8.k(g.j.f20069m1, -1);
        if (k8 >= 0) {
            setOrientation(k8);
        }
        int k9 = v8.k(g.j.f20064l1, -1);
        if (k9 >= 0) {
            setGravity(k9);
        }
        boolean a9 = v8.a(g.j.f20074n1, true);
        if (!a9) {
            setBaselineAligned(a9);
        }
        this.f1235g = v8.i(g.j.f20084p1, -1.0f);
        this.f1230b = v8.k(g.j.f20079o1, -1);
        this.f1236h = v8.a(g.j.f20098s1, false);
        setDividerDrawable(v8.g(g.j.f20088q1));
        this.f1242p = v8.k(g.j.f20103t1, 0);
        this.q = v8.f(g.j.f20093r1, 0);
        v8.w();
    }

    private void A(View view, int i8, int i9, int i10, int i11) {
        view.layout(i8, i9, i10 + i8, i11 + i9);
    }

    private void k(int i8, int i9) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i10 = 0; i10 < i8; i10++) {
            View s8 = s(i10);
            if (s8.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) s8.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).height == -1) {
                    int i11 = ((LinearLayout.LayoutParams) layoutParams).width;
                    ((LinearLayout.LayoutParams) layoutParams).width = s8.getMeasuredWidth();
                    measureChildWithMargins(s8, i9, 0, makeMeasureSpec, 0);
                    ((LinearLayout.LayoutParams) layoutParams).width = i11;
                }
            }
        }
    }

    private void l(int i8, int i9) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i10 = 0; i10 < i8; i10++) {
            View s8 = s(i10);
            if (s8.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) s8.getLayoutParams();
                if (((LinearLayout.LayoutParams) layoutParams).width == -1) {
                    int i11 = ((LinearLayout.LayoutParams) layoutParams).height;
                    ((LinearLayout.LayoutParams) layoutParams).height = s8.getMeasuredHeight();
                    measureChildWithMargins(s8, makeMeasureSpec, 0, i9, 0);
                    ((LinearLayout.LayoutParams) layoutParams).height = i11;
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void g(Canvas canvas) {
        int right;
        int left;
        int i8;
        int virtualChildCount = getVirtualChildCount();
        boolean b9 = u0.b(this);
        for (int i9 = 0; i9 < virtualChildCount; i9++) {
            View s8 = s(i9);
            if (s8 != null && s8.getVisibility() != 8 && t(i9)) {
                LayoutParams layoutParams = (LayoutParams) s8.getLayoutParams();
                j(canvas, b9 ? s8.getRight() + ((LinearLayout.LayoutParams) layoutParams).rightMargin : (s8.getLeft() - ((LinearLayout.LayoutParams) layoutParams).leftMargin) - this.f1240m);
            }
        }
        if (t(virtualChildCount)) {
            View s9 = s(virtualChildCount - 1);
            if (s9 != null) {
                LayoutParams layoutParams2 = (LayoutParams) s9.getLayoutParams();
                if (b9) {
                    left = s9.getLeft();
                    i8 = ((LinearLayout.LayoutParams) layoutParams2).leftMargin;
                    right = (left - i8) - this.f1240m;
                } else {
                    right = s9.getRight() + ((LinearLayout.LayoutParams) layoutParams2).rightMargin;
                }
            } else if (b9) {
                right = getPaddingLeft();
            } else {
                left = getWidth();
                i8 = getPaddingRight();
                right = (left - i8) - this.f1240m;
            }
            j(canvas, right);
        }
    }

    @Override // android.view.View
    public int getBaseline() {
        int i8;
        if (this.f1230b < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i9 = this.f1230b;
        if (childCount > i9) {
            View childAt = getChildAt(i9);
            int baseline = childAt.getBaseline();
            if (baseline == -1) {
                if (this.f1230b == 0) {
                    return -1;
                }
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
            int i10 = this.f1231c;
            if (this.f1232d == 1 && (i8 = this.f1233e & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != 48) {
                if (i8 == 16) {
                    i10 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.f1234f) / 2;
                } else if (i8 == 80) {
                    i10 = ((getBottom() - getTop()) - getPaddingBottom()) - this.f1234f;
                }
            }
            return i10 + ((LinearLayout.LayoutParams) ((LayoutParams) childAt.getLayoutParams())).topMargin + baseline;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.f1230b;
    }

    public Drawable getDividerDrawable() {
        return this.f1239l;
    }

    public int getDividerPadding() {
        return this.q;
    }

    public int getDividerWidth() {
        return this.f1240m;
    }

    public int getGravity() {
        return this.f1233e;
    }

    public int getOrientation() {
        return this.f1232d;
    }

    public int getShowDividers() {
        return this.f1242p;
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.f1235g;
    }

    void h(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        for (int i8 = 0; i8 < virtualChildCount; i8++) {
            View s8 = s(i8);
            if (s8 != null && s8.getVisibility() != 8 && t(i8)) {
                i(canvas, (s8.getTop() - ((LinearLayout.LayoutParams) ((LayoutParams) s8.getLayoutParams())).topMargin) - this.f1241n);
            }
        }
        if (t(virtualChildCount)) {
            View s9 = s(virtualChildCount - 1);
            i(canvas, s9 == null ? (getHeight() - getPaddingBottom()) - this.f1241n : s9.getBottom() + ((LinearLayout.LayoutParams) ((LayoutParams) s9.getLayoutParams())).bottomMargin);
        }
    }

    void i(Canvas canvas, int i8) {
        this.f1239l.setBounds(getPaddingLeft() + this.q, i8, (getWidth() - getPaddingRight()) - this.q, this.f1241n + i8);
        this.f1239l.draw(canvas);
    }

    void j(Canvas canvas, int i8) {
        this.f1239l.setBounds(i8, getPaddingTop() + this.q, this.f1240m + i8, (getHeight() - getPaddingBottom()) - this.q);
        this.f1239l.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: m */
    public LayoutParams generateDefaultLayoutParams() {
        int i8 = this.f1232d;
        if (i8 == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i8 == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    @Override // android.view.ViewGroup
    /* renamed from: n */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: o */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f1239l == null) {
            return;
        }
        if (this.f1232d == 1) {
            h(canvas);
        } else {
            g(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        if (this.f1232d == 1) {
            v(i8, i9, i10, i11);
        } else {
            u(i8, i9, i10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i8, int i9) {
        if (this.f1232d == 1) {
            z(i8, i9);
        } else {
            x(i8, i9);
        }
    }

    int p(View view, int i8) {
        return 0;
    }

    int q(View view) {
        return 0;
    }

    int r(View view) {
        return 0;
    }

    View s(int i8) {
        return getChildAt(i8);
    }

    public void setBaselineAligned(boolean z4) {
        this.f1229a = z4;
    }

    public void setBaselineAlignedChildIndex(int i8) {
        if (i8 >= 0 && i8 < getChildCount()) {
            this.f1230b = i8;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.f1239l) {
            return;
        }
        this.f1239l = drawable;
        if (drawable != null) {
            this.f1240m = drawable.getIntrinsicWidth();
            this.f1241n = drawable.getIntrinsicHeight();
        } else {
            this.f1240m = 0;
            this.f1241n = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i8) {
        this.q = i8;
    }

    public void setGravity(int i8) {
        if (this.f1233e != i8) {
            if ((8388615 & i8) == 0) {
                i8 |= 8388611;
            }
            if ((i8 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) == 0) {
                i8 |= 48;
            }
            this.f1233e = i8;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i8) {
        int i9 = i8 & 8388615;
        int i10 = this.f1233e;
        if ((8388615 & i10) != i9) {
            this.f1233e = i9 | ((-8388616) & i10);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z4) {
        this.f1236h = z4;
    }

    public void setOrientation(int i8) {
        if (this.f1232d != i8) {
            this.f1232d = i8;
            requestLayout();
        }
    }

    public void setShowDividers(int i8) {
        if (i8 != this.f1242p) {
            requestLayout();
        }
        this.f1242p = i8;
    }

    public void setVerticalGravity(int i8) {
        int i9 = i8 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        int i10 = this.f1233e;
        if ((i10 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != i9) {
            this.f1233e = i9 | (i10 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f5) {
        this.f1235g = Math.max(0.0f, f5);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean t(int i8) {
        if (i8 == 0) {
            return (this.f1242p & 1) != 0;
        } else if (i8 == getChildCount()) {
            return (this.f1242p & 4) != 0;
        } else if ((this.f1242p & 2) != 0) {
            for (int i9 = i8 - 1; i9 >= 0; i9--) {
                if (getChildAt(i9).getVisibility() != 8) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void u(int r25, int r26, int r27, int r28) {
        /*
            Method dump skipped, instructions count: 329
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.LinearLayoutCompat.u(int, int, int, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void v(int r18, int r19, int r20, int r21) {
        /*
            r17 = this;
            r6 = r17
            int r7 = r17.getPaddingLeft()
            int r0 = r20 - r18
            int r1 = r17.getPaddingRight()
            int r8 = r0 - r1
            int r0 = r0 - r7
            int r1 = r17.getPaddingRight()
            int r9 = r0 - r1
            int r10 = r17.getVirtualChildCount()
            int r0 = r6.f1233e
            r1 = r0 & 112(0x70, float:1.57E-43)
            r2 = 8388615(0x800007, float:1.1754953E-38)
            r11 = r0 & r2
            r0 = 16
            if (r1 == r0) goto L3b
            r0 = 80
            if (r1 == r0) goto L2f
            int r0 = r17.getPaddingTop()
            goto L47
        L2f:
            int r0 = r17.getPaddingTop()
            int r0 = r0 + r21
            int r0 = r0 - r19
            int r1 = r6.f1234f
            int r0 = r0 - r1
            goto L47
        L3b:
            int r0 = r17.getPaddingTop()
            int r1 = r21 - r19
            int r2 = r6.f1234f
            int r1 = r1 - r2
            int r1 = r1 / 2
            int r0 = r0 + r1
        L47:
            r1 = 0
            r12 = r1
        L49:
            if (r12 >= r10) goto Lc8
            android.view.View r13 = r6.s(r12)
            r14 = 1
            if (r13 != 0) goto L59
            int r1 = r6.y(r12)
            int r0 = r0 + r1
            goto Lc5
        L59:
            int r1 = r13.getVisibility()
            r2 = 8
            if (r1 == r2) goto Lc5
            int r4 = r13.getMeasuredWidth()
            int r15 = r13.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r1 = r13.getLayoutParams()
            r5 = r1
            androidx.appcompat.widget.LinearLayoutCompat$LayoutParams r5 = (androidx.appcompat.widget.LinearLayoutCompat.LayoutParams) r5
            int r1 = r5.gravity
            if (r1 >= 0) goto L75
            r1 = r11
        L75:
            int r2 = androidx.core.view.c0.E(r17)
            int r1 = androidx.core.view.f.b(r1, r2)
            r1 = r1 & 7
            if (r1 == r14) goto L8b
            r2 = 5
            if (r1 == r2) goto L88
            int r1 = r5.leftMargin
            int r1 = r1 + r7
            goto L96
        L88:
            int r1 = r8 - r4
            goto L93
        L8b:
            int r1 = r9 - r4
            int r1 = r1 / 2
            int r1 = r1 + r7
            int r2 = r5.leftMargin
            int r1 = r1 + r2
        L93:
            int r2 = r5.rightMargin
            int r1 = r1 - r2
        L96:
            r2 = r1
            boolean r1 = r6.t(r12)
            if (r1 == 0) goto La0
            int r1 = r6.f1241n
            int r0 = r0 + r1
        La0:
            int r1 = r5.topMargin
            int r16 = r0 + r1
            int r0 = r6.q(r13)
            int r3 = r16 + r0
            r0 = r17
            r1 = r13
            r14 = r5
            r5 = r15
            r0.A(r1, r2, r3, r4, r5)
            int r0 = r14.bottomMargin
            int r15 = r15 + r0
            int r0 = r6.r(r13)
            int r15 = r15 + r0
            int r16 = r16 + r15
            int r0 = r6.p(r13, r12)
            int r12 = r12 + r0
            r0 = r16
            r1 = 1
            goto Lc6
        Lc5:
            r1 = r14
        Lc6:
            int r12 = r12 + r1
            goto L49
        Lc8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.LinearLayoutCompat.v(int, int, int, int):void");
    }

    void w(View view, int i8, int i9, int i10, int i11, int i12) {
        measureChildWithMargins(view, i9, i10, i11, i12);
    }

    /* JADX WARN: Code restructure failed: missing block: B:164:0x03a5, code lost:
        if (r8 > 0) goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x03b0, code lost:
        if (r8 < 0) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x03b2, code lost:
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x03b3, code lost:
        r14.measure(android.view.View.MeasureSpec.makeMeasureSpec(r8, r3), r0);
        r9 = android.view.View.combineMeasuredStates(r9, r14.getMeasuredState() & (-16777216));
        r0 = r0;
        r3 = r2;
     */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0440  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01d2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void x(int r39, int r40) {
        /*
            Method dump skipped, instructions count: 1275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.LinearLayoutCompat.x(int, int):void");
    }

    int y(int i8) {
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:131:0x02cf, code lost:
        if (r15 > 0) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x02da, code lost:
        if (r15 < 0) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x02dc, code lost:
        r15 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x02dd, code lost:
        r13.measure(r0, android.view.View.MeasureSpec.makeMeasureSpec(r15, r10));
        r1 = android.view.View.combineMeasuredStates(r1, r13.getMeasuredState() & (-256));
        r0 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:148:0x031b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void z(int r34, int r35) {
        /*
            Method dump skipped, instructions count: 904
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.LinearLayoutCompat.z(int, int):void");
    }
}
