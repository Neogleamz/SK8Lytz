package com.google.android.material.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.widget.j0;
import androidx.core.view.m0;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.e;
import com.google.android.material.internal.f;
import k7.k;
import k7.l;
import x7.h;
import x7.i;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NavigationView extends ScrimInsetsFrameLayout {

    /* renamed from: n  reason: collision with root package name */
    private static final int[] f18186n = {16842912};

    /* renamed from: p  reason: collision with root package name */
    private static final int[] f18187p = {-16842910};
    private static final int q = k.f21242m;

    /* renamed from: f  reason: collision with root package name */
    private final e f18188f;

    /* renamed from: g  reason: collision with root package name */
    private final f f18189g;

    /* renamed from: h  reason: collision with root package name */
    c f18190h;

    /* renamed from: j  reason: collision with root package name */
    private final int f18191j;

    /* renamed from: k  reason: collision with root package name */
    private final int[] f18192k;

    /* renamed from: l  reason: collision with root package name */
    private MenuInflater f18193l;

    /* renamed from: m  reason: collision with root package name */
    private ViewTreeObserver.OnGlobalLayoutListener f18194m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        public Bundle f18195c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f18195c = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeBundle(this.f18195c);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements g.a {
        a() {
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(g gVar, MenuItem menuItem) {
            c cVar = NavigationView.this.f18190h;
            return cVar != null && cVar.a(menuItem);
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(g gVar) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            NavigationView navigationView = NavigationView.this;
            navigationView.getLocationOnScreen(navigationView.f18192k);
            boolean z4 = true;
            boolean z8 = NavigationView.this.f18192k[1] == 0;
            NavigationView.this.f18189g.z(z8);
            NavigationView.this.setDrawTopInsetForeground(z8);
            Activity a9 = com.google.android.material.internal.b.a(NavigationView.this.getContext());
            if (a9 == null || Build.VERSION.SDK_INT < 21) {
                return;
            }
            boolean z9 = a9.findViewById(16908290).getHeight() == NavigationView.this.getHeight();
            boolean z10 = Color.alpha(a9.getWindow().getNavigationBarColor()) != 0;
            NavigationView navigationView2 = NavigationView.this;
            if (!z9 || !z10) {
                z4 = false;
            }
            navigationView2.setDrawBottomInsetForeground(z4);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        boolean a(MenuItem menuItem);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.K);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NavigationView(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            Method dump skipped, instructions count: 358
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.navigation.NavigationView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private ColorStateList d(int i8) {
        TypedValue typedValue = new TypedValue();
        if (getContext().getTheme().resolveAttribute(i8, typedValue, true)) {
            ColorStateList a9 = h.a.a(getContext(), typedValue.resourceId);
            if (getContext().getTheme().resolveAttribute(g.a.f19885y, typedValue, true)) {
                int i9 = typedValue.data;
                int defaultColor = a9.getDefaultColor();
                int[] iArr = f18187p;
                return new ColorStateList(new int[][]{iArr, f18186n, FrameLayout.EMPTY_STATE_SET}, new int[]{a9.getColorForState(iArr, defaultColor), i9, defaultColor});
            }
            return null;
        }
        return null;
    }

    private final Drawable e(j0 j0Var) {
        h hVar = new h(m.b(getContext(), j0Var.n(l.I5, 0), j0Var.n(l.J5, 0)).m());
        hVar.a0(u7.c.b(getContext(), j0Var, l.K5));
        return new InsetDrawable((Drawable) hVar, j0Var.f(l.N5, 0), j0Var.f(l.O5, 0), j0Var.f(l.M5, 0), j0Var.f(l.L5, 0));
    }

    private boolean g(j0 j0Var) {
        return j0Var.s(l.I5) || j0Var.s(l.J5);
    }

    private MenuInflater getMenuInflater() {
        if (this.f18193l == null) {
            this.f18193l = new androidx.appcompat.view.g(getContext());
        }
        return this.f18193l;
    }

    private void j() {
        this.f18194m = new b();
        getViewTreeObserver().addOnGlobalLayoutListener(this.f18194m);
    }

    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout
    protected void a(m0 m0Var) {
        this.f18189g.d(m0Var);
    }

    public View f(int i8) {
        return this.f18189g.q(i8);
    }

    public MenuItem getCheckedItem() {
        return this.f18189g.m();
    }

    public int getHeaderCount() {
        return this.f18189g.p();
    }

    public Drawable getItemBackground() {
        return this.f18189g.r();
    }

    public int getItemHorizontalPadding() {
        return this.f18189g.s();
    }

    public int getItemIconPadding() {
        return this.f18189g.t();
    }

    public ColorStateList getItemIconTintList() {
        return this.f18189g.w();
    }

    public int getItemMaxLines() {
        return this.f18189g.u();
    }

    public ColorStateList getItemTextColor() {
        return this.f18189g.v();
    }

    public Menu getMenu() {
        return this.f18188f;
    }

    public View h(int i8) {
        return this.f18189g.y(i8);
    }

    public void i(int i8) {
        this.f18189g.L(true);
        getMenuInflater().inflate(i8, this.f18188f);
        this.f18189g.L(false);
        this.f18189g.f(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.e(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT < 16) {
            getViewTreeObserver().removeGlobalOnLayoutListener(this.f18194m);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(this.f18194m);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        int min;
        int mode = View.MeasureSpec.getMode(i8);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 0) {
                min = this.f18191j;
            }
            super.onMeasure(i8, i9);
        }
        min = Math.min(View.MeasureSpec.getSize(i8), this.f18191j);
        i8 = View.MeasureSpec.makeMeasureSpec(min, 1073741824);
        super.onMeasure(i8, i9);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        this.f18188f.S(savedState.f18195c);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Bundle bundle = new Bundle();
        savedState.f18195c = bundle;
        this.f18188f.U(bundle);
        return savedState;
    }

    public void setCheckedItem(int i8) {
        MenuItem findItem = this.f18188f.findItem(i8);
        if (findItem != null) {
            this.f18189g.A((androidx.appcompat.view.menu.i) findItem);
        }
    }

    public void setCheckedItem(MenuItem menuItem) {
        MenuItem findItem = this.f18188f.findItem(menuItem.getItemId());
        if (findItem == null) {
            throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
        }
        this.f18189g.A((androidx.appcompat.view.menu.i) findItem);
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setElevation(f5);
        }
        i.d(this, f5);
    }

    public void setItemBackground(Drawable drawable) {
        this.f18189g.C(drawable);
    }

    public void setItemBackgroundResource(int i8) {
        setItemBackground(androidx.core.content.a.f(getContext(), i8));
    }

    public void setItemHorizontalPadding(int i8) {
        this.f18189g.D(i8);
    }

    public void setItemHorizontalPaddingResource(int i8) {
        this.f18189g.D(getResources().getDimensionPixelSize(i8));
    }

    public void setItemIconPadding(int i8) {
        this.f18189g.E(i8);
    }

    public void setItemIconPaddingResource(int i8) {
        this.f18189g.E(getResources().getDimensionPixelSize(i8));
    }

    public void setItemIconSize(int i8) {
        this.f18189g.F(i8);
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.f18189g.G(colorStateList);
    }

    public void setItemMaxLines(int i8) {
        this.f18189g.H(i8);
    }

    public void setItemTextAppearance(int i8) {
        this.f18189g.I(i8);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f18189g.J(colorStateList);
    }

    public void setNavigationItemSelectedListener(c cVar) {
        this.f18190h = cVar;
    }

    @Override // android.view.View
    public void setOverScrollMode(int i8) {
        super.setOverScrollMode(i8);
        f fVar = this.f18189g;
        if (fVar != null) {
            fVar.K(i8);
        }
    }
}
