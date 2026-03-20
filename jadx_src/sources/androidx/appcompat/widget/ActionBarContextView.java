package androidx.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionBarContextView extends androidx.appcompat.widget.a {

    /* renamed from: j  reason: collision with root package name */
    private CharSequence f1042j;

    /* renamed from: k  reason: collision with root package name */
    private CharSequence f1043k;

    /* renamed from: l  reason: collision with root package name */
    private View f1044l;

    /* renamed from: m  reason: collision with root package name */
    private View f1045m;

    /* renamed from: n  reason: collision with root package name */
    private View f1046n;

    /* renamed from: p  reason: collision with root package name */
    private LinearLayout f1047p;
    private TextView q;

    /* renamed from: t  reason: collision with root package name */
    private TextView f1048t;

    /* renamed from: w  reason: collision with root package name */
    private int f1049w;

    /* renamed from: x  reason: collision with root package name */
    private int f1050x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f1051y;

    /* renamed from: z  reason: collision with root package name */
    private int f1052z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ androidx.appcompat.view.b f1053a;

        a(androidx.appcompat.view.b bVar) {
            this.f1053a = bVar;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.f1053a.c();
        }
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.f19871j);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        j0 v8 = j0.v(context, attributeSet, g.j.f20126y, i8, 0);
        androidx.core.view.c0.x0(this, v8.g(g.j.f20131z));
        this.f1049w = v8.n(g.j.D, 0);
        this.f1050x = v8.n(g.j.C, 0);
        this.f1405e = v8.m(g.j.B, 0);
        this.f1052z = v8.n(g.j.A, g.g.f19964d);
        v8.w();
    }

    private void i() {
        if (this.f1047p == null) {
            LayoutInflater.from(getContext()).inflate(g.g.f19961a, this);
            LinearLayout linearLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.f1047p = linearLayout;
            this.q = (TextView) linearLayout.findViewById(g.f.f19940e);
            this.f1048t = (TextView) this.f1047p.findViewById(g.f.f19939d);
            if (this.f1049w != 0) {
                this.q.setTextAppearance(getContext(), this.f1049w);
            }
            if (this.f1050x != 0) {
                this.f1048t.setTextAppearance(getContext(), this.f1050x);
            }
        }
        this.q.setText(this.f1042j);
        this.f1048t.setText(this.f1043k);
        boolean z4 = !TextUtils.isEmpty(this.f1042j);
        boolean z8 = !TextUtils.isEmpty(this.f1043k);
        int i8 = 0;
        this.f1048t.setVisibility(z8 ? 0 : 8);
        LinearLayout linearLayout2 = this.f1047p;
        if (!z4 && !z8) {
            i8 = 8;
        }
        linearLayout2.setVisibility(i8);
        if (this.f1047p.getParent() == null) {
            addView(this.f1047p);
        }
    }

    @Override // androidx.appcompat.widget.a
    public /* bridge */ /* synthetic */ androidx.core.view.i0 f(int i8, long j8) {
        return super.f(i8, j8);
    }

    public void g() {
        if (this.f1044l == null) {
            k();
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // androidx.appcompat.widget.a
    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    @Override // androidx.appcompat.widget.a
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public CharSequence getSubtitle() {
        return this.f1043k;
    }

    public CharSequence getTitle() {
        return this.f1042j;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void h(androidx.appcompat.view.b r4) {
        /*
            r3 = this;
            android.view.View r0 = r3.f1044l
            if (r0 != 0) goto L19
            android.content.Context r0 = r3.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            int r1 = r3.f1052z
            r2 = 0
            android.view.View r0 = r0.inflate(r1, r3, r2)
            r3.f1044l = r0
        L15:
            r3.addView(r0)
            goto L22
        L19:
            android.view.ViewParent r0 = r0.getParent()
            if (r0 != 0) goto L22
            android.view.View r0 = r3.f1044l
            goto L15
        L22:
            android.view.View r0 = r3.f1044l
            int r1 = g.f.f19944i
            android.view.View r0 = r0.findViewById(r1)
            r3.f1045m = r0
            androidx.appcompat.widget.ActionBarContextView$a r1 = new androidx.appcompat.widget.ActionBarContextView$a
            r1.<init>(r4)
            r0.setOnClickListener(r1)
            android.view.Menu r4 = r4.e()
            androidx.appcompat.view.menu.g r4 = (androidx.appcompat.view.menu.g) r4
            androidx.appcompat.widget.ActionMenuPresenter r0 = r3.f1404d
            if (r0 == 0) goto L41
            r0.C()
        L41:
            androidx.appcompat.widget.ActionMenuPresenter r0 = new androidx.appcompat.widget.ActionMenuPresenter
            android.content.Context r1 = r3.getContext()
            r0.<init>(r1)
            r3.f1404d = r0
            r1 = 1
            r0.N(r1)
            android.view.ViewGroup$LayoutParams r0 = new android.view.ViewGroup$LayoutParams
            r1 = -2
            r2 = -1
            r0.<init>(r1, r2)
            androidx.appcompat.widget.ActionMenuPresenter r1 = r3.f1404d
            android.content.Context r2 = r3.f1402b
            r4.c(r1, r2)
            androidx.appcompat.widget.ActionMenuPresenter r4 = r3.f1404d
            androidx.appcompat.view.menu.n r4 = r4.s(r3)
            androidx.appcompat.widget.ActionMenuView r4 = (androidx.appcompat.widget.ActionMenuView) r4
            r3.f1403c = r4
            r1 = 0
            androidx.core.view.c0.x0(r4, r1)
            androidx.appcompat.widget.ActionMenuView r4 = r3.f1403c
            r3.addView(r4, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarContextView.h(androidx.appcompat.view.b):void");
    }

    public boolean j() {
        return this.f1051y;
    }

    public void k() {
        removeAllViews();
        this.f1046n = null;
        this.f1403c = null;
        this.f1404d = null;
        View view = this.f1045m;
        if (view != null) {
            view.setOnClickListener(null);
        }
    }

    public boolean l() {
        ActionMenuPresenter actionMenuPresenter = this.f1404d;
        if (actionMenuPresenter != null) {
            return actionMenuPresenter.O();
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.f1404d;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.F();
            this.f1404d.G();
        }
    }

    @Override // androidx.appcompat.widget.a, android.view.View
    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        boolean b9 = u0.b(this);
        int paddingRight = b9 ? (i10 - i8) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingTop2 = ((i11 - i9) - getPaddingTop()) - getPaddingBottom();
        View view = this.f1044l;
        if (view != null && view.getVisibility() != 8) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f1044l.getLayoutParams();
            int i12 = b9 ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
            int i13 = b9 ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
            int d8 = androidx.appcompat.widget.a.d(paddingRight, i12, b9);
            paddingRight = androidx.appcompat.widget.a.d(d8 + e(this.f1044l, d8, paddingTop, paddingTop2, b9), i13, b9);
        }
        int i14 = paddingRight;
        LinearLayout linearLayout = this.f1047p;
        if (linearLayout != null && this.f1046n == null && linearLayout.getVisibility() != 8) {
            i14 += e(this.f1047p, i14, paddingTop, paddingTop2, b9);
        }
        int i15 = i14;
        View view2 = this.f1046n;
        if (view2 != null) {
            e(view2, i15, paddingTop, paddingTop2, b9);
        }
        int paddingLeft = b9 ? getPaddingLeft() : (i10 - i8) - getPaddingRight();
        ActionMenuView actionMenuView = this.f1403c;
        if (actionMenuView != null) {
            e(actionMenuView, paddingLeft, paddingTop, paddingTop2, !b9);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        if (View.MeasureSpec.getMode(i8) != 1073741824) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
        } else if (View.MeasureSpec.getMode(i9) == 0) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_height=\"wrap_content\"");
        } else {
            int size = View.MeasureSpec.getSize(i8);
            int i10 = this.f1405e;
            if (i10 <= 0) {
                i10 = View.MeasureSpec.getSize(i9);
            }
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
            int i11 = i10 - paddingTop;
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i11, Integer.MIN_VALUE);
            View view = this.f1044l;
            if (view != null) {
                int c9 = c(view, paddingLeft, makeMeasureSpec, 0);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f1044l.getLayoutParams();
                paddingLeft = c9 - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
            }
            ActionMenuView actionMenuView = this.f1403c;
            if (actionMenuView != null && actionMenuView.getParent() == this) {
                paddingLeft = c(this.f1403c, paddingLeft, makeMeasureSpec, 0);
            }
            LinearLayout linearLayout = this.f1047p;
            if (linearLayout != null && this.f1046n == null) {
                if (this.f1051y) {
                    this.f1047p.measure(View.MeasureSpec.makeMeasureSpec(0, 0), makeMeasureSpec);
                    int measuredWidth = this.f1047p.getMeasuredWidth();
                    boolean z4 = measuredWidth <= paddingLeft;
                    if (z4) {
                        paddingLeft -= measuredWidth;
                    }
                    this.f1047p.setVisibility(z4 ? 0 : 8);
                } else {
                    paddingLeft = c(linearLayout, paddingLeft, makeMeasureSpec, 0);
                }
            }
            View view2 = this.f1046n;
            if (view2 != null) {
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                int i12 = layoutParams.width;
                int i13 = i12 != -2 ? 1073741824 : Integer.MIN_VALUE;
                if (i12 >= 0) {
                    paddingLeft = Math.min(i12, paddingLeft);
                }
                int i14 = layoutParams.height;
                int i15 = i14 == -2 ? Integer.MIN_VALUE : 1073741824;
                if (i14 >= 0) {
                    i11 = Math.min(i14, i11);
                }
                this.f1046n.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, i13), View.MeasureSpec.makeMeasureSpec(i11, i15));
            }
            if (this.f1405e > 0) {
                setMeasuredDimension(size, i10);
                return;
            }
            int childCount = getChildCount();
            int i16 = 0;
            for (int i17 = 0; i17 < childCount; i17++) {
                int measuredHeight = getChildAt(i17).getMeasuredHeight() + paddingTop;
                if (measuredHeight > i16) {
                    i16 = measuredHeight;
                }
            }
            setMeasuredDimension(size, i16);
        }
    }

    @Override // androidx.appcompat.widget.a, android.view.View
    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.appcompat.widget.a
    public void setContentHeight(int i8) {
        this.f1405e = i8;
    }

    public void setCustomView(View view) {
        LinearLayout linearLayout;
        View view2 = this.f1046n;
        if (view2 != null) {
            removeView(view2);
        }
        this.f1046n = view;
        if (view != null && (linearLayout = this.f1047p) != null) {
            removeView(linearLayout);
            this.f1047p = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.f1043k = charSequence;
        i();
    }

    public void setTitle(CharSequence charSequence) {
        this.f1042j = charSequence;
        i();
        androidx.core.view.c0.w0(this, charSequence);
    }

    public void setTitleOptional(boolean z4) {
        if (z4 != this.f1051y) {
            requestLayout();
        }
        this.f1051y = z4;
    }

    @Override // androidx.appcompat.widget.a, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i8) {
        super.setVisibility(i8);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
