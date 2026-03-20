package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.LinearLayoutCompat;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionMenuView extends LinearLayoutCompat implements g.b, androidx.appcompat.view.menu.n {
    private m.a A;
    g.a B;
    private boolean C;
    private int E;
    private int F;
    private int G;
    d H;

    /* renamed from: t  reason: collision with root package name */
    private androidx.appcompat.view.menu.g f1096t;

    /* renamed from: w  reason: collision with root package name */
    private Context f1097w;

    /* renamed from: x  reason: collision with root package name */
    private int f1098x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f1099y;

    /* renamed from: z  reason: collision with root package name */
    private ActionMenuPresenter f1100z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
        @ViewDebug.ExportedProperty

        /* renamed from: a  reason: collision with root package name */
        public boolean f1101a;
        @ViewDebug.ExportedProperty

        /* renamed from: b  reason: collision with root package name */
        public int f1102b;
        @ViewDebug.ExportedProperty

        /* renamed from: c  reason: collision with root package name */
        public int f1103c;
        @ViewDebug.ExportedProperty

        /* renamed from: d  reason: collision with root package name */
        public boolean f1104d;
        @ViewDebug.ExportedProperty

        /* renamed from: e  reason: collision with root package name */
        public boolean f1105e;

        /* renamed from: f  reason: collision with root package name */
        boolean f1106f;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f1101a = false;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.f1101a = layoutParams.f1101a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        boolean a();

        boolean b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements m.a {
        b() {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public boolean d(androidx.appcompat.view.menu.g gVar) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements g.a {
        c() {
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
            d dVar = ActionMenuView.this.H;
            return dVar != null && dVar.onMenuItemClick(menuItem);
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(androidx.appcompat.view.menu.g gVar) {
            g.a aVar = ActionMenuView.this.B;
            if (aVar != null) {
                aVar.b(gVar);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        float f5 = context.getResources().getDisplayMetrics().density;
        this.F = (int) (56.0f * f5);
        this.G = (int) (f5 * 4.0f);
        this.f1097w = context;
        this.f1098x = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int L(View view, int i8, int i9, int i10, int i11) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i10) - i11, View.MeasureSpec.getMode(i10));
        ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean z4 = true;
        boolean z8 = actionMenuItemView != null && actionMenuItemView.r();
        int i12 = 2;
        if (i9 <= 0 || (z8 && i9 < 2)) {
            i12 = 0;
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(i9 * i8, Integer.MIN_VALUE), makeMeasureSpec);
            int measuredWidth = view.getMeasuredWidth();
            int i13 = measuredWidth / i8;
            if (measuredWidth % i8 != 0) {
                i13++;
            }
            if (!z8 || i13 >= 2) {
                i12 = i13;
            }
        }
        if (layoutParams.f1101a || !z8) {
            z4 = false;
        }
        layoutParams.f1104d = z4;
        layoutParams.f1102b = i12;
        view.measure(View.MeasureSpec.makeMeasureSpec(i8 * i12, 1073741824), makeMeasureSpec);
        return i12;
    }

    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r14v14 */
    private void M(int i8, int i9) {
        int i10;
        int i11;
        boolean z4;
        int i12;
        int i13;
        boolean z8;
        boolean z9;
        int i14;
        ?? r14;
        int mode = View.MeasureSpec.getMode(i9);
        int size = View.MeasureSpec.getSize(i8);
        int size2 = View.MeasureSpec.getSize(i9);
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i9, paddingTop, -2);
        int i15 = size - paddingLeft;
        int i16 = this.F;
        int i17 = i15 / i16;
        int i18 = i15 % i16;
        if (i17 == 0) {
            setMeasuredDimension(i15, 0);
            return;
        }
        int i19 = i16 + (i18 / i17);
        int childCount = getChildCount();
        int i20 = 0;
        int i21 = 0;
        boolean z10 = false;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        long j8 = 0;
        while (i21 < childCount) {
            View childAt = getChildAt(i21);
            int i25 = size2;
            if (childAt.getVisibility() != 8) {
                boolean z11 = childAt instanceof ActionMenuItemView;
                int i26 = i22 + 1;
                if (z11) {
                    int i27 = this.G;
                    i14 = i26;
                    r14 = 0;
                    childAt.setPadding(i27, 0, i27, 0);
                } else {
                    i14 = i26;
                    r14 = 0;
                }
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                layoutParams.f1106f = r14;
                layoutParams.f1103c = r14;
                layoutParams.f1102b = r14;
                layoutParams.f1104d = r14;
                ((LinearLayout.LayoutParams) layoutParams).leftMargin = r14;
                ((LinearLayout.LayoutParams) layoutParams).rightMargin = r14;
                layoutParams.f1105e = z11 && ((ActionMenuItemView) childAt).r();
                int L = L(childAt, i19, layoutParams.f1101a ? 1 : i17, childMeasureSpec, paddingTop);
                i23 = Math.max(i23, L);
                if (layoutParams.f1104d) {
                    i24++;
                }
                if (layoutParams.f1101a) {
                    z10 = true;
                }
                i17 -= L;
                i20 = Math.max(i20, childAt.getMeasuredHeight());
                if (L == 1) {
                    j8 |= 1 << i21;
                    i20 = i20;
                }
                i22 = i14;
            }
            i21++;
            size2 = i25;
        }
        int i28 = size2;
        boolean z12 = z10 && i22 == 2;
        boolean z13 = false;
        while (i24 > 0 && i17 > 0) {
            int i29 = Integer.MAX_VALUE;
            int i30 = 0;
            int i31 = 0;
            long j9 = 0;
            while (i31 < childCount) {
                boolean z14 = z13;
                LayoutParams layoutParams2 = (LayoutParams) getChildAt(i31).getLayoutParams();
                int i32 = i20;
                if (layoutParams2.f1104d) {
                    int i33 = layoutParams2.f1102b;
                    if (i33 < i29) {
                        j9 = 1 << i31;
                        i29 = i33;
                        i30 = 1;
                    } else if (i33 == i29) {
                        i30++;
                        j9 |= 1 << i31;
                    }
                }
                i31++;
                i20 = i32;
                z13 = z14;
            }
            z4 = z13;
            i12 = i20;
            j8 |= j9;
            if (i30 > i17) {
                i10 = mode;
                i11 = i15;
                break;
            }
            int i34 = i29 + 1;
            int i35 = 0;
            while (i35 < childCount) {
                View childAt2 = getChildAt(i35);
                LayoutParams layoutParams3 = (LayoutParams) childAt2.getLayoutParams();
                int i36 = i15;
                int i37 = mode;
                long j10 = 1 << i35;
                if ((j9 & j10) == 0) {
                    if (layoutParams3.f1102b == i34) {
                        j8 |= j10;
                    }
                    z9 = z12;
                } else {
                    if (z12 && layoutParams3.f1105e && i17 == 1) {
                        int i38 = this.G;
                        z9 = z12;
                        childAt2.setPadding(i38 + i19, 0, i38, 0);
                    } else {
                        z9 = z12;
                    }
                    layoutParams3.f1102b++;
                    layoutParams3.f1106f = true;
                    i17--;
                }
                i35++;
                mode = i37;
                i15 = i36;
                z12 = z9;
            }
            i20 = i12;
            z13 = true;
        }
        i10 = mode;
        i11 = i15;
        z4 = z13;
        i12 = i20;
        boolean z15 = !z10 && i22 == 1;
        if (i17 <= 0 || j8 == 0 || (i17 >= i22 - 1 && !z15 && i23 <= 1)) {
            i13 = 0;
            z8 = z4;
        } else {
            float bitCount = Long.bitCount(j8);
            if (z15) {
                i13 = 0;
            } else {
                i13 = 0;
                if ((j8 & 1) != 0 && !((LayoutParams) getChildAt(0).getLayoutParams()).f1105e) {
                    bitCount -= 0.5f;
                }
                int i39 = childCount - 1;
                if ((j8 & (1 << i39)) != 0 && !((LayoutParams) getChildAt(i39).getLayoutParams()).f1105e) {
                    bitCount -= 0.5f;
                }
            }
            int i40 = bitCount > 0.0f ? (int) ((i17 * i19) / bitCount) : i13;
            z8 = z4;
            for (int i41 = i13; i41 < childCount; i41++) {
                if ((j8 & (1 << i41)) != 0) {
                    View childAt3 = getChildAt(i41);
                    LayoutParams layoutParams4 = (LayoutParams) childAt3.getLayoutParams();
                    if (childAt3 instanceof ActionMenuItemView) {
                        layoutParams4.f1103c = i40;
                        layoutParams4.f1106f = true;
                        if (i41 == 0 && !layoutParams4.f1105e) {
                            ((LinearLayout.LayoutParams) layoutParams4).leftMargin = (-i40) / 2;
                        }
                        z8 = true;
                    } else if (layoutParams4.f1101a) {
                        layoutParams4.f1103c = i40;
                        layoutParams4.f1106f = true;
                        ((LinearLayout.LayoutParams) layoutParams4).rightMargin = (-i40) / 2;
                        z8 = true;
                    } else {
                        if (i41 != 0) {
                            ((LinearLayout.LayoutParams) layoutParams4).leftMargin = i40 / 2;
                        }
                        if (i41 != childCount - 1) {
                            ((LinearLayout.LayoutParams) layoutParams4).rightMargin = i40 / 2;
                        }
                    }
                }
            }
        }
        if (z8) {
            for (int i42 = i13; i42 < childCount; i42++) {
                View childAt4 = getChildAt(i42);
                LayoutParams layoutParams5 = (LayoutParams) childAt4.getLayoutParams();
                if (layoutParams5.f1106f) {
                    childAt4.measure(View.MeasureSpec.makeMeasureSpec((layoutParams5.f1102b * i19) + layoutParams5.f1103c, 1073741824), childMeasureSpec);
                }
            }
        }
        setMeasuredDimension(i11, i10 != 1073741824 ? i12 : i28);
    }

    public void B() {
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.C();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat
    /* renamed from: C */
    public LayoutParams m() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams) layoutParams).gravity = 16;
        return layoutParams;
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat
    /* renamed from: D */
    public LayoutParams n(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat
    /* renamed from: E */
    public LayoutParams o(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams != null) {
            LayoutParams layoutParams2 = layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : new LayoutParams(layoutParams);
            if (((LinearLayout.LayoutParams) layoutParams2).gravity <= 0) {
                ((LinearLayout.LayoutParams) layoutParams2).gravity = 16;
            }
            return layoutParams2;
        }
        return m();
    }

    public LayoutParams F() {
        LayoutParams m8 = m();
        m8.f1101a = true;
        return m8;
    }

    protected boolean G(int i8) {
        boolean z4 = false;
        if (i8 == 0) {
            return false;
        }
        View childAt = getChildAt(i8 - 1);
        View childAt2 = getChildAt(i8);
        if (i8 < getChildCount() && (childAt instanceof a)) {
            z4 = false | ((a) childAt).a();
        }
        return (i8 <= 0 || !(childAt2 instanceof a)) ? z4 : z4 | ((a) childAt2).b();
    }

    public boolean H() {
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        return actionMenuPresenter != null && actionMenuPresenter.F();
    }

    public boolean I() {
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        return actionMenuPresenter != null && actionMenuPresenter.H();
    }

    public boolean J() {
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        return actionMenuPresenter != null && actionMenuPresenter.I();
    }

    public boolean K() {
        return this.f1099y;
    }

    public androidx.appcompat.view.menu.g N() {
        return this.f1096t;
    }

    public void O(m.a aVar, g.a aVar2) {
        this.A = aVar;
        this.B = aVar2;
    }

    public boolean P() {
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        return actionMenuPresenter != null && actionMenuPresenter.O();
    }

    @Override // androidx.appcompat.view.menu.g.b
    public boolean a(androidx.appcompat.view.menu.i iVar) {
        return this.f1096t.N(iVar, 0);
    }

    @Override // androidx.appcompat.view.menu.n
    public void b(androidx.appcompat.view.menu.g gVar) {
        this.f1096t = gVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    public Menu getMenu() {
        if (this.f1096t == null) {
            Context context = getContext();
            androidx.appcompat.view.menu.g gVar = new androidx.appcompat.view.menu.g(context);
            this.f1096t = gVar;
            gVar.V(new c());
            ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(context);
            this.f1100z = actionMenuPresenter;
            actionMenuPresenter.N(true);
            ActionMenuPresenter actionMenuPresenter2 = this.f1100z;
            m.a aVar = this.A;
            if (aVar == null) {
                aVar = new b();
            }
            actionMenuPresenter2.j(aVar);
            this.f1096t.c(this.f1100z, this.f1097w);
            this.f1100z.L(this);
        }
        return this.f1096t;
    }

    public Drawable getOverflowIcon() {
        getMenu();
        return this.f1100z.E();
    }

    public int getPopupTheme() {
        return this.f1098x;
    }

    public int getWindowAnimations() {
        return 0;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionMenuPresenter actionMenuPresenter = this.f1100z;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.f(false);
            if (this.f1100z.I()) {
                this.f1100z.F();
                this.f1100z.O();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        B();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int width;
        int i12;
        if (!this.C) {
            super.onLayout(z4, i8, i9, i10, i11);
            return;
        }
        int childCount = getChildCount();
        int i13 = (i11 - i9) / 2;
        int dividerWidth = getDividerWidth();
        int i14 = i10 - i8;
        int paddingRight = (i14 - getPaddingRight()) - getPaddingLeft();
        boolean b9 = u0.b(this);
        int i15 = 0;
        int i16 = 0;
        for (int i17 = 0; i17 < childCount; i17++) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.f1101a) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    if (G(i17)) {
                        measuredWidth += dividerWidth;
                    }
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (b9) {
                        i12 = getPaddingLeft() + ((LinearLayout.LayoutParams) layoutParams).leftMargin;
                        width = i12 + measuredWidth;
                    } else {
                        width = (getWidth() - getPaddingRight()) - ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                        i12 = width - measuredWidth;
                    }
                    int i18 = i13 - (measuredHeight / 2);
                    childAt.layout(i12, i18, width, measuredHeight + i18);
                    paddingRight -= measuredWidth;
                    i15 = 1;
                } else {
                    paddingRight -= (childAt.getMeasuredWidth() + ((LinearLayout.LayoutParams) layoutParams).leftMargin) + ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                    G(i17);
                    i16++;
                }
            }
        }
        if (childCount == 1 && i15 == 0) {
            View childAt2 = getChildAt(0);
            int measuredWidth2 = childAt2.getMeasuredWidth();
            int measuredHeight2 = childAt2.getMeasuredHeight();
            int i19 = (i14 / 2) - (measuredWidth2 / 2);
            int i20 = i13 - (measuredHeight2 / 2);
            childAt2.layout(i19, i20, measuredWidth2 + i19, measuredHeight2 + i20);
            return;
        }
        int i21 = i16 - (i15 ^ 1);
        int max = Math.max(0, i21 > 0 ? paddingRight / i21 : 0);
        if (b9) {
            int width2 = getWidth() - getPaddingRight();
            for (int i22 = 0; i22 < childCount; i22++) {
                View childAt3 = getChildAt(i22);
                LayoutParams layoutParams2 = (LayoutParams) childAt3.getLayoutParams();
                if (childAt3.getVisibility() != 8 && !layoutParams2.f1101a) {
                    int i23 = width2 - ((LinearLayout.LayoutParams) layoutParams2).rightMargin;
                    int measuredWidth3 = childAt3.getMeasuredWidth();
                    int measuredHeight3 = childAt3.getMeasuredHeight();
                    int i24 = i13 - (measuredHeight3 / 2);
                    childAt3.layout(i23 - measuredWidth3, i24, i23, measuredHeight3 + i24);
                    width2 = i23 - ((measuredWidth3 + ((LinearLayout.LayoutParams) layoutParams2).leftMargin) + max);
                }
            }
            return;
        }
        int paddingLeft = getPaddingLeft();
        for (int i25 = 0; i25 < childCount; i25++) {
            View childAt4 = getChildAt(i25);
            LayoutParams layoutParams3 = (LayoutParams) childAt4.getLayoutParams();
            if (childAt4.getVisibility() != 8 && !layoutParams3.f1101a) {
                int i26 = paddingLeft + ((LinearLayout.LayoutParams) layoutParams3).leftMargin;
                int measuredWidth4 = childAt4.getMeasuredWidth();
                int measuredHeight4 = childAt4.getMeasuredHeight();
                int i27 = i13 - (measuredHeight4 / 2);
                childAt4.layout(i26, i27, i26 + measuredWidth4, measuredHeight4 + i27);
                paddingLeft = i26 + measuredWidth4 + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + max;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    public void onMeasure(int i8, int i9) {
        androidx.appcompat.view.menu.g gVar;
        boolean z4 = this.C;
        boolean z8 = View.MeasureSpec.getMode(i8) == 1073741824;
        this.C = z8;
        if (z4 != z8) {
            this.E = 0;
        }
        int size = View.MeasureSpec.getSize(i8);
        if (this.C && (gVar = this.f1096t) != null && size != this.E) {
            this.E = size;
            gVar.M(true);
        }
        int childCount = getChildCount();
        if (this.C && childCount > 0) {
            M(i8, i9);
            return;
        }
        for (int i10 = 0; i10 < childCount; i10++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i10).getLayoutParams();
            ((LinearLayout.LayoutParams) layoutParams).rightMargin = 0;
            ((LinearLayout.LayoutParams) layoutParams).leftMargin = 0;
        }
        super.onMeasure(i8, i9);
    }

    public void setExpandedActionViewsExclusive(boolean z4) {
        this.f1100z.K(z4);
    }

    public void setOnMenuItemClickListener(d dVar) {
        this.H = dVar;
    }

    public void setOverflowIcon(Drawable drawable) {
        getMenu();
        this.f1100z.M(drawable);
    }

    public void setOverflowReserved(boolean z4) {
        this.f1099y = z4;
    }

    public void setPopupTheme(int i8) {
        if (this.f1098x != i8) {
            this.f1098x = i8;
            if (i8 == 0) {
                this.f1097w = getContext();
            } else {
                this.f1097w = new ContextThemeWrapper(getContext(), i8);
            }
        }
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.f1100z = actionMenuPresenter;
        actionMenuPresenter.L(this);
    }
}
