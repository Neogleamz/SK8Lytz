package androidx.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.q;
import androidx.core.view.s;
import androidx.core.view.u;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NestedScrollView extends FrameLayout implements s, androidx.core.view.p {
    private static final a H = new a();
    private static final int[] K = {16843130};
    private int A;
    private SavedState B;
    private final u C;
    private final q E;
    private float F;
    private c G;

    /* renamed from: a  reason: collision with root package name */
    private long f5093a;

    /* renamed from: b  reason: collision with root package name */
    private final Rect f5094b;

    /* renamed from: c  reason: collision with root package name */
    private OverScroller f5095c;

    /* renamed from: d  reason: collision with root package name */
    public EdgeEffect f5096d;

    /* renamed from: e  reason: collision with root package name */
    public EdgeEffect f5097e;

    /* renamed from: f  reason: collision with root package name */
    private int f5098f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f5099g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f5100h;

    /* renamed from: j  reason: collision with root package name */
    private View f5101j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f5102k;

    /* renamed from: l  reason: collision with root package name */
    private VelocityTracker f5103l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f5104m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f5105n;

    /* renamed from: p  reason: collision with root package name */
    private int f5106p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f5107t;

    /* renamed from: w  reason: collision with root package name */
    private int f5108w;

    /* renamed from: x  reason: collision with root package name */
    private final int[] f5109x;

    /* renamed from: y  reason: collision with root package name */
    private final int[] f5110y;

    /* renamed from: z  reason: collision with root package name */
    private int f5111z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        public int f5112a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f5112a = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.f5112a + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f5112a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends androidx.core.view.a {
        a() {
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            super.f(view, accessibilityEvent);
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            accessibilityEvent.setClassName(ScrollView.class.getName());
            accessibilityEvent.setScrollable(nestedScrollView.getScrollRange() > 0);
            accessibilityEvent.setScrollX(nestedScrollView.getScrollX());
            accessibilityEvent.setScrollY(nestedScrollView.getScrollY());
            androidx.core.view.accessibility.e.a(accessibilityEvent, nestedScrollView.getScrollX());
            androidx.core.view.accessibility.e.b(accessibilityEvent, nestedScrollView.getScrollRange());
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            int scrollRange;
            super.g(view, cVar);
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            cVar.c0(ScrollView.class.getName());
            if (!nestedScrollView.isEnabled() || (scrollRange = nestedScrollView.getScrollRange()) <= 0) {
                return;
            }
            cVar.y0(true);
            if (nestedScrollView.getScrollY() > 0) {
                cVar.b(c.a.f4919r);
                cVar.b(c.a.C);
            }
            if (nestedScrollView.getScrollY() < scrollRange) {
                cVar.b(c.a.q);
                cVar.b(c.a.E);
            }
        }

        @Override // androidx.core.view.a
        public boolean j(View view, int i8, Bundle bundle) {
            if (super.j(view, i8, bundle)) {
                return true;
            }
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            if (nestedScrollView.isEnabled()) {
                int height = nestedScrollView.getHeight();
                Rect rect = new Rect();
                if (nestedScrollView.getMatrix().isIdentity() && nestedScrollView.getGlobalVisibleRect(rect)) {
                    height = rect.height();
                }
                if (i8 != 4096) {
                    if (i8 == 8192 || i8 == 16908344) {
                        int max = Math.max(nestedScrollView.getScrollY() - ((height - nestedScrollView.getPaddingBottom()) - nestedScrollView.getPaddingTop()), 0);
                        if (max != nestedScrollView.getScrollY()) {
                            nestedScrollView.Q(0, max, true);
                            return true;
                        }
                        return false;
                    } else if (i8 != 16908346) {
                        return false;
                    }
                }
                int min = Math.min(nestedScrollView.getScrollY() + ((height - nestedScrollView.getPaddingBottom()) - nestedScrollView.getPaddingTop()), nestedScrollView.getScrollRange());
                if (min != nestedScrollView.getScrollY()) {
                    nestedScrollView.Q(0, min, true);
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static boolean a(ViewGroup viewGroup) {
            return viewGroup.getClipToPadding();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(NestedScrollView nestedScrollView, int i8, int i9, int i10, int i11);
    }

    public NestedScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, q0.a.f22439c);
    }

    public NestedScrollView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f5094b = new Rect();
        this.f5099g = true;
        this.f5100h = false;
        this.f5101j = null;
        this.f5102k = false;
        this.f5105n = true;
        this.f5108w = -1;
        this.f5109x = new int[2];
        this.f5110y = new int[2];
        this.f5096d = f.a(context, attributeSet);
        this.f5097e = f.a(context, attributeSet);
        y();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, K, i8, 0);
        setFillViewport(obtainStyledAttributes.getBoolean(0, false));
        obtainStyledAttributes.recycle();
        this.C = new u(this);
        this.E = new q(this);
        setNestedScrollingEnabled(true);
        c0.t0(this, H);
    }

    private boolean A(View view) {
        return !C(view, 0, getHeight());
    }

    private static boolean B(View view, View view2) {
        if (view == view2) {
            return true;
        }
        ViewParent parent = view.getParent();
        return (parent instanceof ViewGroup) && B((View) parent, view2);
    }

    private boolean C(View view, int i8, int i9) {
        view.getDrawingRect(this.f5094b);
        offsetDescendantRectToMyCoords(view, this.f5094b);
        return this.f5094b.bottom + i8 >= getScrollY() && this.f5094b.top - i8 <= getScrollY() + i9;
    }

    private void D(int i8, int i9, int[] iArr) {
        int scrollY = getScrollY();
        scrollBy(0, i8);
        int scrollY2 = getScrollY() - scrollY;
        if (iArr != null) {
            iArr[1] = iArr[1] + scrollY2;
        }
        this.E.e(0, scrollY2, 0, i8 - scrollY2, null, i9, iArr);
    }

    private void E(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f5108w) {
            int i8 = actionIndex == 0 ? 1 : 0;
            this.f5098f = (int) motionEvent.getY(i8);
            this.f5108w = motionEvent.getPointerId(i8);
            VelocityTracker velocityTracker = this.f5103l;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void H() {
        VelocityTracker velocityTracker = this.f5103l;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f5103l = null;
        }
    }

    private int I(int i8, float f5) {
        float d8;
        EdgeEffect edgeEffect;
        float width = f5 / getWidth();
        float height = i8 / getHeight();
        float f8 = 0.0f;
        if (f.b(this.f5096d) != 0.0f) {
            d8 = -f.d(this.f5096d, -height, width);
            if (f.b(this.f5096d) == 0.0f) {
                edgeEffect = this.f5096d;
                edgeEffect.onRelease();
            }
            f8 = d8;
        } else if (f.b(this.f5097e) != 0.0f) {
            d8 = f.d(this.f5097e, height, 1.0f - width);
            if (f.b(this.f5097e) == 0.0f) {
                edgeEffect = this.f5097e;
                edgeEffect.onRelease();
            }
            f8 = d8;
        }
        int round = Math.round(f8 * getHeight());
        if (round != 0) {
            invalidate();
        }
        return round;
    }

    private void J(boolean z4) {
        if (z4) {
            R(2, 1);
        } else {
            T(1);
        }
        this.A = getScrollY();
        c0.j0(this);
    }

    private boolean K(int i8, int i9, int i10) {
        int height = getHeight();
        int scrollY = getScrollY();
        int i11 = height + scrollY;
        boolean z4 = false;
        boolean z8 = i8 == 33;
        View s8 = s(z8, i9, i10);
        if (s8 == null) {
            s8 = this;
        }
        if (i9 < scrollY || i10 > i11) {
            i(z8 ? i9 - scrollY : i10 - i11);
            z4 = true;
        }
        if (s8 != findFocus()) {
            s8.requestFocus(i8);
        }
        return z4;
    }

    private void L(View view) {
        view.getDrawingRect(this.f5094b);
        offsetDescendantRectToMyCoords(view, this.f5094b);
        int f5 = f(this.f5094b);
        if (f5 != 0) {
            scrollBy(0, f5);
        }
    }

    private boolean M(Rect rect, boolean z4) {
        int f5 = f(rect);
        boolean z8 = f5 != 0;
        if (z8) {
            if (z4) {
                scrollBy(0, f5);
            } else {
                N(0, f5);
            }
        }
        return z8;
    }

    private void O(int i8, int i9, int i10, boolean z4) {
        if (getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.f5093a > 250) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int scrollY = getScrollY();
            OverScroller overScroller = this.f5095c;
            int scrollX = getScrollX();
            overScroller.startScroll(scrollX, scrollY, 0, Math.max(0, Math.min(i9 + scrollY, Math.max(0, ((childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom())))) - scrollY, i10);
            J(z4);
        } else {
            if (!this.f5095c.isFinished()) {
                a();
            }
            scrollBy(i8, i9);
        }
        this.f5093a = AnimationUtils.currentAnimationTimeMillis();
    }

    private boolean S(MotionEvent motionEvent) {
        boolean z4;
        if (f.b(this.f5096d) != 0.0f) {
            f.d(this.f5096d, 0.0f, motionEvent.getX() / getWidth());
            z4 = true;
        } else {
            z4 = false;
        }
        if (f.b(this.f5097e) != 0.0f) {
            f.d(this.f5097e, 0.0f, 1.0f - (motionEvent.getX() / getWidth()));
            return true;
        }
        return z4;
    }

    private void a() {
        this.f5095c.abortAnimation();
        T(1);
    }

    private boolean c() {
        int overScrollMode = getOverScrollMode();
        if (overScrollMode != 0) {
            return overScrollMode == 1 && getScrollRange() > 0;
        }
        return true;
    }

    private boolean d() {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            return (childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin > (getHeight() - getPaddingTop()) - getPaddingBottom();
        }
        return false;
    }

    private static int e(int i8, int i9, int i10) {
        if (i9 >= i10 || i8 < 0) {
            return 0;
        }
        return i9 + i8 > i10 ? i10 - i9 : i8;
    }

    private float getVerticalScrollFactorCompat() {
        if (this.F == 0.0f) {
            TypedValue typedValue = new TypedValue();
            Context context = getContext();
            if (!context.getTheme().resolveAttribute(16842829, typedValue, true)) {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            this.F = typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return this.F;
    }

    private void i(int i8) {
        if (i8 != 0) {
            if (this.f5105n) {
                N(0, i8);
            } else {
                scrollBy(0, i8);
            }
        }
    }

    private boolean p(int i8) {
        EdgeEffect edgeEffect;
        if (f.b(this.f5096d) != 0.0f) {
            edgeEffect = this.f5096d;
        } else if (f.b(this.f5097e) == 0.0f) {
            return false;
        } else {
            edgeEffect = this.f5097e;
            i8 = -i8;
        }
        edgeEffect.onAbsorb(i8);
        return true;
    }

    private void q() {
        this.f5102k = false;
        H();
        T(0);
        this.f5096d.onRelease();
        this.f5097e.onRelease();
    }

    private View s(boolean z4, int i8, int i9) {
        ArrayList focusables = getFocusables(2);
        int size = focusables.size();
        View view = null;
        boolean z8 = false;
        for (int i10 = 0; i10 < size; i10++) {
            View view2 = (View) focusables.get(i10);
            int top = view2.getTop();
            int bottom = view2.getBottom();
            if (i8 < bottom && top < i9) {
                boolean z9 = i8 < top && bottom < i9;
                if (view == null) {
                    view = view2;
                    z8 = z9;
                } else {
                    boolean z10 = (z4 && top < view.getTop()) || (!z4 && bottom > view.getBottom());
                    if (z8) {
                        if (z9) {
                            if (!z10) {
                            }
                            view = view2;
                        }
                    } else if (z9) {
                        view = view2;
                        z8 = true;
                    } else {
                        if (!z10) {
                        }
                        view = view2;
                    }
                }
            }
        }
        return view;
    }

    private boolean w(int i8, int i9) {
        if (getChildCount() > 0) {
            int scrollY = getScrollY();
            View childAt = getChildAt(0);
            return i9 >= childAt.getTop() - scrollY && i9 < childAt.getBottom() - scrollY && i8 >= childAt.getLeft() && i8 < childAt.getRight();
        }
        return false;
    }

    private void x() {
        VelocityTracker velocityTracker = this.f5103l;
        if (velocityTracker == null) {
            this.f5103l = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void y() {
        this.f5095c = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.f5106p = viewConfiguration.getScaledTouchSlop();
        this.q = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f5107t = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    private void z() {
        if (this.f5103l == null) {
            this.f5103l = VelocityTracker.obtain();
        }
    }

    boolean F(int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, boolean z4) {
        boolean z8;
        boolean z9;
        int overScrollMode = getOverScrollMode();
        boolean z10 = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        boolean z11 = computeVerticalScrollRange() > computeVerticalScrollExtent();
        boolean z12 = overScrollMode == 0 || (overScrollMode == 1 && z10);
        boolean z13 = overScrollMode == 0 || (overScrollMode == 1 && z11);
        int i16 = i10 + i8;
        int i17 = !z12 ? 0 : i14;
        int i18 = i11 + i9;
        int i19 = !z13 ? 0 : i15;
        int i20 = -i17;
        int i21 = i17 + i12;
        int i22 = -i19;
        int i23 = i19 + i13;
        if (i16 > i21) {
            i16 = i21;
            z8 = true;
        } else if (i16 < i20) {
            z8 = true;
            i16 = i20;
        } else {
            z8 = false;
        }
        if (i18 > i23) {
            i18 = i23;
            z9 = true;
        } else if (i18 < i22) {
            z9 = true;
            i18 = i22;
        } else {
            z9 = false;
        }
        if (z9 && !v(1)) {
            this.f5095c.springBack(i16, i18, 0, 0, 0, getScrollRange());
        }
        onOverScrolled(i16, i18, z8, z9);
        return z8 || z9;
    }

    public boolean G(int i8) {
        boolean z4 = i8 == 130;
        int height = getHeight();
        if (z4) {
            this.f5094b.top = getScrollY() + height;
            int childCount = getChildCount();
            if (childCount > 0) {
                View childAt = getChildAt(childCount - 1);
                int bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
                Rect rect = this.f5094b;
                if (rect.top + height > bottom) {
                    rect.top = bottom - height;
                }
            }
        } else {
            this.f5094b.top = getScrollY() - height;
            Rect rect2 = this.f5094b;
            if (rect2.top < 0) {
                rect2.top = 0;
            }
        }
        Rect rect3 = this.f5094b;
        int i9 = rect3.top;
        int i10 = height + i9;
        rect3.bottom = i10;
        return K(i8, i9, i10);
    }

    public final void N(int i8, int i9) {
        O(i8, i9, 250, false);
    }

    void P(int i8, int i9, int i10, boolean z4) {
        O(i8 - getScrollX(), i9 - getScrollY(), i10, z4);
    }

    void Q(int i8, int i9, boolean z4) {
        P(i8, i9, 250, z4);
    }

    public boolean R(int i8, int i9) {
        return this.E.p(i8, i9);
    }

    public void T(int i8) {
        this.E.r(i8);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, i8);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, i8, layoutParams);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, layoutParams);
    }

    public boolean b(int i8) {
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i8);
        int maxScrollAmount = getMaxScrollAmount();
        if (findNextFocus == null || !C(findNextFocus, maxScrollAmount, getHeight())) {
            if (i8 == 33 && getScrollY() < maxScrollAmount) {
                maxScrollAmount = getScrollY();
            } else if (i8 == 130 && getChildCount() > 0) {
                View childAt = getChildAt(0);
                maxScrollAmount = Math.min((childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin) - ((getScrollY() + getHeight()) - getPaddingBottom()), maxScrollAmount);
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i8 != 130) {
                maxScrollAmount = -maxScrollAmount;
            }
            i(maxScrollAmount);
        } else {
            findNextFocus.getDrawingRect(this.f5094b);
            offsetDescendantRectToMyCoords(findNextFocus, this.f5094b);
            i(f(this.f5094b));
            findNextFocus.requestFocus(i8);
        }
        if (findFocus != null && findFocus.isFocused() && A(findFocus)) {
            int descendantFocusability = getDescendantFocusability();
            setDescendantFocusability(131072);
            requestFocus();
            setDescendantFocusability(descendantFocusability);
            return true;
        }
        return true;
    }

    @Override // android.view.View
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override // android.view.View
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override // android.view.View
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override // android.view.View
    public void computeScroll() {
        EdgeEffect edgeEffect;
        if (this.f5095c.isFinished()) {
            return;
        }
        this.f5095c.computeScrollOffset();
        int currY = this.f5095c.getCurrY();
        int i8 = currY - this.A;
        this.A = currY;
        int[] iArr = this.f5110y;
        boolean z4 = false;
        iArr[1] = 0;
        g(0, i8, iArr, null, 1);
        int i9 = i8 - this.f5110y[1];
        int scrollRange = getScrollRange();
        if (i9 != 0) {
            int scrollY = getScrollY();
            F(0, i9, getScrollX(), scrollY, 0, scrollRange, 0, 0, false);
            int scrollY2 = getScrollY() - scrollY;
            int i10 = i9 - scrollY2;
            int[] iArr2 = this.f5110y;
            iArr2[1] = 0;
            h(0, scrollY2, 0, i10, this.f5109x, 1, iArr2);
            i9 = i10 - this.f5110y[1];
        }
        if (i9 != 0) {
            int overScrollMode = getOverScrollMode();
            if (overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0)) {
                z4 = true;
            }
            if (z4) {
                if (i9 < 0) {
                    if (this.f5096d.isFinished()) {
                        edgeEffect = this.f5096d;
                        edgeEffect.onAbsorb((int) this.f5095c.getCurrVelocity());
                    }
                } else if (this.f5097e.isFinished()) {
                    edgeEffect = this.f5097e;
                    edgeEffect.onAbsorb((int) this.f5095c.getCurrVelocity());
                }
            }
            a();
        }
        if (this.f5095c.isFinished()) {
            T(1);
        } else {
            c0.j0(this);
        }
    }

    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override // android.view.View
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override // android.view.View
    public int computeVerticalScrollRange() {
        int childCount = getChildCount();
        int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (childCount == 0) {
            return height;
        }
        View childAt = getChildAt(0);
        int bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin;
        int scrollY = getScrollY();
        int max = Math.max(0, bottom - height);
        return scrollY < 0 ? bottom - scrollY : scrollY > max ? bottom + (scrollY - max) : bottom;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || r(keyEvent);
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f5, float f8, boolean z4) {
        return this.E.a(f5, f8, z4);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f5, float f8) {
        return this.E.b(f5, f8);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i8, int i9, int[] iArr, int[] iArr2) {
        return g(i8, i9, iArr, iArr2, 0);
    }

    @Override // android.view.View
    public boolean dispatchNestedScroll(int i8, int i9, int i10, int i11, int[] iArr) {
        return this.E.f(i8, i9, i10, i11, iArr);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int paddingLeft;
        super.draw(canvas);
        int scrollY = getScrollY();
        int i8 = 0;
        if (!this.f5096d.isFinished()) {
            int save = canvas.save();
            int width = getWidth();
            int height = getHeight();
            int min = Math.min(0, scrollY);
            int i9 = Build.VERSION.SDK_INT;
            if (i9 < 21 || b.a(this)) {
                width -= getPaddingLeft() + getPaddingRight();
                paddingLeft = getPaddingLeft() + 0;
            } else {
                paddingLeft = 0;
            }
            if (i9 >= 21 && b.a(this)) {
                height -= getPaddingTop() + getPaddingBottom();
                min += getPaddingTop();
            }
            canvas.translate(paddingLeft, min);
            this.f5096d.setSize(width, height);
            if (this.f5096d.draw(canvas)) {
                c0.j0(this);
            }
            canvas.restoreToCount(save);
        }
        if (this.f5097e.isFinished()) {
            return;
        }
        int save2 = canvas.save();
        int width2 = getWidth();
        int height2 = getHeight();
        int max = Math.max(getScrollRange(), scrollY) + height2;
        int i10 = Build.VERSION.SDK_INT;
        if (i10 < 21 || b.a(this)) {
            width2 -= getPaddingLeft() + getPaddingRight();
            i8 = 0 + getPaddingLeft();
        }
        if (i10 >= 21 && b.a(this)) {
            height2 -= getPaddingTop() + getPaddingBottom();
            max -= getPaddingBottom();
        }
        canvas.translate(i8 - width2, max);
        canvas.rotate(180.0f, width2, 0.0f);
        this.f5097e.setSize(width2, height2);
        if (this.f5097e.draw(canvas)) {
            c0.j0(this);
        }
        canvas.restoreToCount(save2);
    }

    protected int f(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int scrollY = getScrollY();
        int i8 = scrollY + height;
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            scrollY += verticalFadingEdgeLength;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        int i9 = rect.bottom < (childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin ? i8 - verticalFadingEdgeLength : i8;
        int i10 = rect.bottom;
        if (i10 > i9 && rect.top > scrollY) {
            return Math.min((rect.height() > height ? rect.top - scrollY : rect.bottom - i9) + 0, (childAt.getBottom() + layoutParams.bottomMargin) - i8);
        } else if (rect.top >= scrollY || i10 >= i9) {
            return 0;
        } else {
            return Math.max(rect.height() > height ? 0 - (i9 - rect.bottom) : 0 - (scrollY - rect.top), -getScrollY());
        }
    }

    public boolean g(int i8, int i9, int[] iArr, int[] iArr2, int i10) {
        return this.E.d(i8, i9, iArr, iArr2, i10);
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        View childAt = getChildAt(0);
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        int bottom = ((childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin) - getScrollY()) - (getHeight() - getPaddingBottom());
        if (bottom < verticalFadingEdgeLength) {
            return bottom / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) (getHeight() * 0.5f);
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.C.a();
    }

    int getScrollRange() {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            return Math.max(0, ((childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
        }
        return 0;
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength();
        int scrollY = getScrollY();
        if (scrollY < verticalFadingEdgeLength) {
            return scrollY / verticalFadingEdgeLength;
        }
        return 1.0f;
    }

    public void h(int i8, int i9, int i10, int i11, int[] iArr, int i12, int[] iArr2) {
        this.E.e(i8, i9, i10, i11, iArr, i12, iArr2);
    }

    @Override // android.view.View
    public boolean hasNestedScrollingParent() {
        return v(0);
    }

    @Override // android.view.View, androidx.core.view.p
    public boolean isNestedScrollingEnabled() {
        return this.E.l();
    }

    @Override // androidx.core.view.s
    public void j(View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        D(i11, i12, iArr);
    }

    @Override // androidx.core.view.r
    public void k(View view, int i8, int i9, int i10, int i11, int i12) {
        D(i11, i12, null);
    }

    @Override // androidx.core.view.r
    public boolean l(View view, View view2, int i8, int i9) {
        return (i8 & 2) != 0;
    }

    @Override // androidx.core.view.r
    public void m(View view, View view2, int i8, int i9) {
        this.C.c(view, view2, i8, i9);
        R(2, i9);
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View view, int i8, int i9) {
        view.measure(FrameLayout.getChildMeasureSpec(i8, getPaddingLeft() + getPaddingRight(), view.getLayoutParams().width), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View view, int i8, int i9, int i10, int i11) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(FrameLayout.getChildMeasureSpec(i8, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i9, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
    }

    @Override // androidx.core.view.r
    public void n(View view, int i8) {
        this.C.e(view, i8);
        T(i8);
    }

    @Override // androidx.core.view.r
    public void o(View view, int i8, int i9, int[] iArr, int i10) {
        g(i8, i9, iArr, null, i10);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f5100h = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x002c  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onGenericMotionEvent(android.view.MotionEvent r9) {
        /*
            r8 = this;
            int r0 = r9.getAction()
            r1 = 0
            r2 = 8
            if (r0 != r2) goto La6
            boolean r0 = r8.f5102k
            if (r0 != 0) goto La6
            r0 = 2
            boolean r0 = androidx.core.view.o.b(r9, r0)
            r2 = 0
            if (r0 == 0) goto L1c
            r0 = 9
        L17:
            float r0 = r9.getAxisValue(r0)
            goto L28
        L1c:
            r0 = 4194304(0x400000, float:5.877472E-39)
            boolean r0 = androidx.core.view.o.b(r9, r0)
            if (r0 == 0) goto L27
            r0 = 26
            goto L17
        L27:
            r0 = r2
        L28:
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto La6
            float r2 = r8.getVerticalScrollFactorCompat()
            float r0 = r0 * r2
            int r0 = (int) r0
            int r2 = r8.getScrollRange()
            int r3 = r8.getScrollY()
            int r0 = r3 - r0
            r4 = 1056964608(0x3f000000, float:0.5)
            r5 = 8194(0x2002, float:1.1482E-41)
            r6 = 1
            if (r0 >= 0) goto L6d
            boolean r2 = r8.c()
            if (r2 == 0) goto L51
            boolean r9 = androidx.core.view.o.b(r9, r5)
            if (r9 != 0) goto L51
            r9 = r6
            goto L52
        L51:
            r9 = r1
        L52:
            if (r9 == 0) goto L6b
            android.widget.EdgeEffect r9 = r8.f5096d
            float r0 = (float) r0
            float r0 = -r0
            int r2 = r8.getHeight()
            float r2 = (float) r2
            float r0 = r0 / r2
            androidx.core.widget.f.d(r9, r0, r4)
            android.widget.EdgeEffect r9 = r8.f5096d
            r9.onRelease()
            r8.invalidate()
            r9 = r6
            goto L9b
        L6b:
            r9 = r1
            goto L9b
        L6d:
            if (r0 <= r2) goto L99
            boolean r7 = r8.c()
            if (r7 == 0) goto L7d
            boolean r9 = androidx.core.view.o.b(r9, r5)
            if (r9 != 0) goto L7d
            r9 = r6
            goto L7e
        L7d:
            r9 = r1
        L7e:
            if (r9 == 0) goto L96
            android.widget.EdgeEffect r9 = r8.f5097e
            int r0 = r0 - r2
            float r0 = (float) r0
            int r1 = r8.getHeight()
            float r1 = (float) r1
            float r0 = r0 / r1
            androidx.core.widget.f.d(r9, r0, r4)
            android.widget.EdgeEffect r9 = r8.f5097e
            r9.onRelease()
            r8.invalidate()
            r1 = r6
        L96:
            r9 = r1
            r1 = r2
            goto L9b
        L99:
            r9 = r1
            r1 = r0
        L9b:
            if (r1 == r3) goto La5
            int r9 = r8.getScrollX()
            super.scrollTo(r9, r1)
            return r6
        La5:
            return r9
        La6:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.NestedScrollView.onGenericMotionEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        boolean z4 = true;
        if (action == 2 && this.f5102k) {
            return true;
        }
        int i8 = action & 255;
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 == 2) {
                    int i9 = this.f5108w;
                    if (i9 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i9);
                        if (findPointerIndex == -1) {
                            Log.e("NestedScrollView", "Invalid pointerId=" + i9 + " in onInterceptTouchEvent");
                        } else {
                            int y8 = (int) motionEvent.getY(findPointerIndex);
                            if (Math.abs(y8 - this.f5098f) > this.f5106p && (2 & getNestedScrollAxes()) == 0) {
                                this.f5102k = true;
                                this.f5098f = y8;
                                z();
                                this.f5103l.addMovement(motionEvent);
                                this.f5111z = 0;
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i8 != 3) {
                    if (i8 == 6) {
                        E(motionEvent);
                    }
                }
            }
            this.f5102k = false;
            this.f5108w = -1;
            H();
            if (this.f5095c.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                c0.j0(this);
            }
            T(0);
        } else {
            int y9 = (int) motionEvent.getY();
            if (w((int) motionEvent.getX(), y9)) {
                this.f5098f = y9;
                this.f5108w = motionEvent.getPointerId(0);
                x();
                this.f5103l.addMovement(motionEvent);
                this.f5095c.computeScrollOffset();
                if (!S(motionEvent) && this.f5095c.isFinished()) {
                    z4 = false;
                }
                this.f5102k = z4;
                R(2, 0);
            } else {
                if (!S(motionEvent) && this.f5095c.isFinished()) {
                    z4 = false;
                }
                this.f5102k = z4;
                H();
            }
        }
        return this.f5102k;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        int i12 = 0;
        this.f5099g = false;
        View view = this.f5101j;
        if (view != null && B(view, this)) {
            L(this.f5101j);
        }
        this.f5101j = null;
        if (!this.f5100h) {
            if (this.B != null) {
                scrollTo(getScrollX(), this.B.f5112a);
                this.B = null;
            }
            if (getChildCount() > 0) {
                View childAt = getChildAt(0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                i12 = childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
            int paddingTop = ((i11 - i9) - getPaddingTop()) - getPaddingBottom();
            int scrollY = getScrollY();
            int e8 = e(scrollY, paddingTop, i12);
            if (e8 != scrollY) {
                scrollTo(getScrollX(), e8);
            }
        }
        scrollTo(getScrollX(), getScrollY());
        this.f5100h = true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (this.f5104m && View.MeasureSpec.getMode(i9) != 0 && getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int measuredHeight2 = (((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - layoutParams.topMargin) - layoutParams.bottomMargin;
            if (measuredHeight < measuredHeight2) {
                childAt.measure(FrameLayout.getChildMeasureSpec(i8, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), View.MeasureSpec.makeMeasureSpec(measuredHeight2, 1073741824));
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedFling(View view, float f5, float f8, boolean z4) {
        if (z4) {
            return false;
        }
        dispatchNestedFling(0.0f, f8, true);
        t((int) f8);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedPreFling(View view, float f5, float f8) {
        return dispatchNestedPreFling(f5, f8);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedPreScroll(View view, int i8, int i9, int[] iArr) {
        o(view, i8, i9, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScroll(View view, int i8, int i9, int i10, int i11) {
        D(i11, 0, null);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScrollAccepted(View view, View view2, int i8) {
        m(view, view2, i8, 0);
    }

    @Override // android.view.View
    protected void onOverScrolled(int i8, int i9, boolean z4, boolean z8) {
        super.scrollTo(i8, i9);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i8, Rect rect) {
        if (i8 == 2) {
            i8 = 130;
        } else if (i8 == 1) {
            i8 = 33;
        }
        FocusFinder focusFinder = FocusFinder.getInstance();
        View findNextFocus = rect == null ? focusFinder.findNextFocus(this, null, i8) : focusFinder.findNextFocusFromRect(this, rect, i8);
        if (findNextFocus == null || A(findNextFocus)) {
            return false;
        }
        return findNextFocus.requestFocus(i8, rect);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.B = savedState;
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f5112a = getScrollY();
        return savedState;
    }

    @Override // android.view.View
    protected void onScrollChanged(int i8, int i9, int i10, int i11) {
        super.onScrollChanged(i8, i9, i10, i11);
        c cVar = this.G;
        if (cVar != null) {
            cVar.a(this, i8, i9, i10, i11);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        View findFocus = findFocus();
        if (findFocus == null || this == findFocus || !C(findFocus, 0, i11)) {
            return;
        }
        findFocus.getDrawingRect(this.f5094b);
        offsetDescendantRectToMyCoords(findFocus, this.f5094b);
        i(f(this.f5094b));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onStartNestedScroll(View view, View view2, int i8) {
        return l(view, view2, i8, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onStopNestedScroll(View view) {
        n(view, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0076, code lost:
        if (r23.f5095c.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()) != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0078, code lost:
        androidx.core.view.c0.j0(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0222, code lost:
        if (r23.f5095c.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()) != false) goto L29;
     */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01d4  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r24) {
        /*
            Method dump skipped, instructions count: 608
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.NestedScrollView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean r(KeyEvent keyEvent) {
        this.f5094b.setEmpty();
        if (!d()) {
            if (!isFocused() || keyEvent.getKeyCode() == 4) {
                return false;
            }
            View findFocus = findFocus();
            if (findFocus == this) {
                findFocus = null;
            }
            View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, 130);
            return (findNextFocus == null || findNextFocus == this || !findNextFocus.requestFocus(130)) ? false : true;
        } else if (keyEvent.getAction() == 0) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode == 19) {
                return !keyEvent.isAltPressed() ? b(33) : u(33);
            } else if (keyCode == 20) {
                return !keyEvent.isAltPressed() ? b(130) : u(130);
            } else if (keyCode != 62) {
                return false;
            } else {
                G(keyEvent.isShiftPressed() ? 33 : 130);
                return false;
            }
        } else {
            return false;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (this.f5099g) {
            this.f5101j = view2;
        } else {
            L(view2);
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z4) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return M(rect, z4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z4) {
        if (z4) {
            H();
        }
        super.requestDisallowInterceptTouchEvent(z4);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.f5099g = true;
        super.requestLayout();
    }

    @Override // android.view.View
    public void scrollTo(int i8, int i9) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            int e8 = e(i8, (getWidth() - getPaddingLeft()) - getPaddingRight(), childAt.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
            int e9 = e(i9, (getHeight() - getPaddingTop()) - getPaddingBottom(), childAt.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            if (e8 == getScrollX() && e9 == getScrollY()) {
                return;
            }
            super.scrollTo(e8, e9);
        }
    }

    public void setFillViewport(boolean z4) {
        if (z4 != this.f5104m) {
            this.f5104m = z4;
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z4) {
        this.E.m(z4);
    }

    public void setOnScrollChangeListener(c cVar) {
        this.G = cVar;
    }

    public void setSmoothScrollingEnabled(boolean z4) {
        this.f5105n = z4;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i8) {
        return R(i8, 0);
    }

    @Override // android.view.View, androidx.core.view.p
    public void stopNestedScroll() {
        T(0);
    }

    public void t(int i8) {
        if (getChildCount() > 0) {
            this.f5095c.fling(getScrollX(), getScrollY(), 0, i8, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            J(true);
        }
    }

    public boolean u(int i8) {
        int childCount;
        boolean z4 = i8 == 130;
        int height = getHeight();
        Rect rect = this.f5094b;
        rect.top = 0;
        rect.bottom = height;
        if (z4 && (childCount = getChildCount()) > 0) {
            View childAt = getChildAt(childCount - 1);
            this.f5094b.bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
            Rect rect2 = this.f5094b;
            rect2.top = rect2.bottom - height;
        }
        Rect rect3 = this.f5094b;
        return K(i8, rect3.top, rect3.bottom);
    }

    public boolean v(int i8) {
        return this.E.k(i8);
    }
}
