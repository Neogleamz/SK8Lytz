package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionBarContainer extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    private boolean f1032a;

    /* renamed from: b  reason: collision with root package name */
    private View f1033b;

    /* renamed from: c  reason: collision with root package name */
    private View f1034c;

    /* renamed from: d  reason: collision with root package name */
    private View f1035d;

    /* renamed from: e  reason: collision with root package name */
    Drawable f1036e;

    /* renamed from: f  reason: collision with root package name */
    Drawable f1037f;

    /* renamed from: g  reason: collision with root package name */
    Drawable f1038g;

    /* renamed from: h  reason: collision with root package name */
    boolean f1039h;

    /* renamed from: j  reason: collision with root package name */
    boolean f1040j;

    /* renamed from: k  reason: collision with root package name */
    private int f1041k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {
        public static void a(ActionBarContainer actionBarContainer) {
            actionBarContainer.invalidateOutline();
        }
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        androidx.core.view.c0.x0(this, new b(this));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.f20003a);
        this.f1036e = obtainStyledAttributes.getDrawable(g.j.f20009b);
        this.f1037f = obtainStyledAttributes.getDrawable(g.j.f20021d);
        this.f1041k = obtainStyledAttributes.getDimensionPixelSize(g.j.f20052j, -1);
        boolean z4 = true;
        if (getId() == g.f.N) {
            this.f1039h = true;
            this.f1038g = obtainStyledAttributes.getDrawable(g.j.f20015c);
        }
        obtainStyledAttributes.recycle();
        if (!this.f1039h ? this.f1036e != null || this.f1037f != null : this.f1038g != null) {
            z4 = false;
        }
        setWillNotDraw(z4);
    }

    private int a(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private boolean b(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.f1036e;
        if (drawable != null && drawable.isStateful()) {
            this.f1036e.setState(getDrawableState());
        }
        Drawable drawable2 = this.f1037f;
        if (drawable2 != null && drawable2.isStateful()) {
            this.f1037f.setState(getDrawableState());
        }
        Drawable drawable3 = this.f1038g;
        if (drawable3 == null || !drawable3.isStateful()) {
            return;
        }
        this.f1038g.setState(getDrawableState());
    }

    public View getTabContainer() {
        return this.f1033b;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f1036e;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.f1037f;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.f1038g;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.f1034c = findViewById(g.f.f19936a);
        this.f1035d = findViewById(g.f.f19941f);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.f1032a || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        Drawable drawable;
        Drawable drawable2;
        int left;
        int top;
        int right;
        View view;
        super.onLayout(z4, i8, i9, i10, i11);
        View view2 = this.f1033b;
        boolean z8 = true;
        boolean z9 = false;
        boolean z10 = (view2 == null || view2.getVisibility() == 8) ? false : true;
        if (view2 != null && view2.getVisibility() != 8) {
            int measuredHeight = getMeasuredHeight();
            int i12 = ((FrameLayout.LayoutParams) view2.getLayoutParams()).bottomMargin;
            view2.layout(i8, (measuredHeight - view2.getMeasuredHeight()) - i12, i10, measuredHeight - i12);
        }
        if (this.f1039h) {
            Drawable drawable3 = this.f1038g;
            if (drawable3 != null) {
                drawable3.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
            z8 = z9;
        } else {
            if (this.f1036e != null) {
                if (this.f1034c.getVisibility() == 0) {
                    drawable2 = this.f1036e;
                    left = this.f1034c.getLeft();
                    top = this.f1034c.getTop();
                    right = this.f1034c.getRight();
                    view = this.f1034c;
                } else {
                    View view3 = this.f1035d;
                    if (view3 == null || view3.getVisibility() != 0) {
                        this.f1036e.setBounds(0, 0, 0, 0);
                        z9 = true;
                    } else {
                        drawable2 = this.f1036e;
                        left = this.f1035d.getLeft();
                        top = this.f1035d.getTop();
                        right = this.f1035d.getRight();
                        view = this.f1035d;
                    }
                }
                drawable2.setBounds(left, top, right, view.getBottom());
                z9 = true;
            }
            this.f1040j = z10;
            if (z10 && (drawable = this.f1037f) != null) {
                drawable.setBounds(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
            }
            z8 = z9;
        }
        if (z8) {
            invalidate();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005a  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMeasure(int r4, int r5) {
        /*
            r3 = this;
            android.view.View r0 = r3.f1034c
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r0 != 0) goto L1c
            int r0 = android.view.View.MeasureSpec.getMode(r5)
            if (r0 != r1) goto L1c
            int r0 = r3.f1041k
            if (r0 < 0) goto L1c
            int r5 = android.view.View.MeasureSpec.getSize(r5)
            int r5 = java.lang.Math.min(r0, r5)
            int r5 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r1)
        L1c:
            super.onMeasure(r4, r5)
            android.view.View r4 = r3.f1034c
            if (r4 != 0) goto L24
            return
        L24:
            int r4 = android.view.View.MeasureSpec.getMode(r5)
            android.view.View r0 = r3.f1033b
            if (r0 == 0) goto L6f
            int r0 = r0.getVisibility()
            r2 = 8
            if (r0 == r2) goto L6f
            r0 = 1073741824(0x40000000, float:2.0)
            if (r4 == r0) goto L6f
            android.view.View r0 = r3.f1034c
            boolean r0 = r3.b(r0)
            if (r0 != 0) goto L47
            android.view.View r0 = r3.f1034c
        L42:
            int r0 = r3.a(r0)
            goto L53
        L47:
            android.view.View r0 = r3.f1035d
            boolean r0 = r3.b(r0)
            if (r0 != 0) goto L52
            android.view.View r0 = r3.f1035d
            goto L42
        L52:
            r0 = 0
        L53:
            if (r4 != r1) goto L5a
            int r4 = android.view.View.MeasureSpec.getSize(r5)
            goto L5d
        L5a:
            r4 = 2147483647(0x7fffffff, float:NaN)
        L5d:
            int r5 = r3.getMeasuredWidth()
            android.view.View r1 = r3.f1033b
            int r1 = r3.a(r1)
            int r0 = r0 + r1
            int r4 = java.lang.Math.min(r0, r4)
            r3.setMeasuredDimension(r5, r4)
        L6f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarContainer.onMeasure(int, int):void");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void setPrimaryBackground(Drawable drawable) {
        Drawable drawable2 = this.f1036e;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.f1036e);
        }
        this.f1036e = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            View view = this.f1034c;
            if (view != null) {
                this.f1036e.setBounds(view.getLeft(), this.f1034c.getTop(), this.f1034c.getRight(), this.f1034c.getBottom());
            }
        }
        boolean z4 = true;
        if (!this.f1039h ? this.f1036e != null || this.f1037f != null : this.f1038g != null) {
            z4 = false;
        }
        setWillNotDraw(z4);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            a.a(this);
        }
    }

    public void setSplitBackground(Drawable drawable) {
        Drawable drawable2;
        Drawable drawable3 = this.f1038g;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            unscheduleDrawable(this.f1038g);
        }
        this.f1038g = drawable;
        boolean z4 = false;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.f1039h && (drawable2 = this.f1038g) != null) {
                drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (!this.f1039h ? !(this.f1036e != null || this.f1037f != null) : this.f1038g == null) {
            z4 = true;
        }
        setWillNotDraw(z4);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            a.a(this);
        }
    }

    public void setStackedBackground(Drawable drawable) {
        Drawable drawable2;
        Drawable drawable3 = this.f1037f;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            unscheduleDrawable(this.f1037f);
        }
        this.f1037f = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.f1040j && (drawable2 = this.f1037f) != null) {
                drawable2.setBounds(this.f1033b.getLeft(), this.f1033b.getTop(), this.f1033b.getRight(), this.f1033b.getBottom());
            }
        }
        boolean z4 = true;
        if (!this.f1039h ? this.f1036e != null || this.f1037f != null : this.f1038g != null) {
            z4 = false;
        }
        setWillNotDraw(z4);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            a.a(this);
        }
    }

    public void setTabContainer(c0 c0Var) {
        View view = this.f1033b;
        if (view != null) {
            removeView(view);
        }
        this.f1033b = c0Var;
        if (c0Var != null) {
            addView(c0Var);
            ViewGroup.LayoutParams layoutParams = c0Var.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            c0Var.setAllowCollapse(false);
        }
    }

    public void setTransitioning(boolean z4) {
        this.f1032a = z4;
        setDescendantFocusability(z4 ? 393216 : 262144);
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        boolean z4 = i8 == 0;
        Drawable drawable = this.f1036e;
        if (drawable != null) {
            drawable.setVisible(z4, false);
        }
        Drawable drawable2 = this.f1037f;
        if (drawable2 != null) {
            drawable2.setVisible(z4, false);
        }
        Drawable drawable3 = this.f1038g;
        if (drawable3 != null) {
            drawable3.setVisible(z4, false);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i8) {
        if (i8 != 0) {
            return super.startActionModeForChild(view, callback, i8);
        }
        return null;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.f1036e && !this.f1039h) || (drawable == this.f1037f && this.f1040j) || ((drawable == this.f1038g && this.f1039h) || super.verifyDrawable(drawable));
    }
}
