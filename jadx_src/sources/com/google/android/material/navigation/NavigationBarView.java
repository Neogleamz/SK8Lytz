package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.j0;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.internal.m;
import com.google.android.material.internal.s;
import k7.l;
import x7.h;
import x7.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class NavigationBarView extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    private final com.google.android.material.navigation.b f18176a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.android.material.navigation.c f18177b;

    /* renamed from: c  reason: collision with root package name */
    private final NavigationBarPresenter f18178c;

    /* renamed from: d  reason: collision with root package name */
    private ColorStateList f18179d;

    /* renamed from: e  reason: collision with root package name */
    private MenuInflater f18180e;

    /* renamed from: f  reason: collision with root package name */
    private d f18181f;

    /* renamed from: g  reason: collision with root package name */
    private c f18182g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        Bundle f18183c;

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
            b(parcel, classLoader == null ? getClass().getClassLoader() : classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void b(Parcel parcel, ClassLoader classLoader) {
            this.f18183c = parcel.readBundle(classLoader);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeBundle(this.f18183c);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements g.a {
        a() {
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(g gVar, MenuItem menuItem) {
            if (NavigationBarView.this.f18182g == null || menuItem.getItemId() != NavigationBarView.this.getSelectedItemId()) {
                return (NavigationBarView.this.f18181f == null || NavigationBarView.this.f18181f.a(menuItem)) ? false : true;
            }
            NavigationBarView.this.f18182g.a(menuItem);
            return true;
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(g gVar) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements s.e {
        b() {
        }

        @Override // com.google.android.material.internal.s.e
        public m0 a(View view, m0 m0Var, s.f fVar) {
            fVar.f18169d += m0Var.j();
            boolean z4 = c0.E(view) == 1;
            int k8 = m0Var.k();
            int l8 = m0Var.l();
            fVar.f18166a += z4 ? l8 : k8;
            int i8 = fVar.f18168c;
            if (!z4) {
                k8 = l8;
            }
            fVar.f18168c = i8 + k8;
            fVar.a(view);
            return m0Var;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(MenuItem menuItem);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        boolean a(MenuItem menuItem);
    }

    public NavigationBarView(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(y7.a.c(context, attributeSet, i8, i9), attributeSet, i8);
        NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter();
        this.f18178c = navigationBarPresenter;
        Context context2 = getContext();
        int[] iArr = l.f21326h5;
        int i10 = l.f21397p5;
        int i11 = l.o5;
        j0 i12 = m.i(context2, attributeSet, iArr, i8, i9, i10, i11);
        com.google.android.material.navigation.b bVar = new com.google.android.material.navigation.b(context2, getClass(), getMaxItemCount());
        this.f18176a = bVar;
        com.google.android.material.navigation.c e8 = e(context2);
        this.f18177b = e8;
        navigationBarPresenter.b(e8);
        navigationBarPresenter.a(1);
        e8.setPresenter(navigationBarPresenter);
        bVar.b(navigationBarPresenter);
        navigationBarPresenter.k(getContext(), bVar);
        int i13 = l.f21371m5;
        e8.setIconTintList(i12.s(i13) ? i12.c(i13) : e8.e(16842808));
        setItemIconSize(i12.f(l.f21362l5, getResources().getDimensionPixelSize(k7.d.f21103g0)));
        if (i12.s(i10)) {
            setItemTextAppearanceInactive(i12.n(i10, 0));
        }
        if (i12.s(i11)) {
            setItemTextAppearanceActive(i12.n(i11, 0));
        }
        int i14 = l.f21405q5;
        if (i12.s(i14)) {
            setItemTextColor(i12.c(i14));
        }
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            c0.x0(this, d(context2));
        }
        int i15 = l.f21344j5;
        if (i12.s(i15)) {
            setElevation(i12.f(i15, 0));
        }
        androidx.core.graphics.drawable.a.o(getBackground().mutate(), u7.c.b(context2, i12, l.f21335i5));
        setLabelVisibilityMode(i12.l(l.f21413r5, -1));
        int n8 = i12.n(l.f21353k5, 0);
        if (n8 != 0) {
            e8.setItemBackgroundRes(n8);
        } else {
            setItemRippleColor(u7.c.b(context2, i12, l.f21380n5));
        }
        int i16 = l.f21422s5;
        if (i12.s(i16)) {
            f(i12.n(i16, 0));
        }
        i12.w();
        addView(e8);
        bVar.V(new a());
        c();
    }

    private void c() {
        s.b(this, new b());
    }

    private h d(Context context) {
        h hVar = new h();
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            hVar.a0(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
        }
        hVar.P(context);
        return hVar;
    }

    private MenuInflater getMenuInflater() {
        if (this.f18180e == null) {
            this.f18180e = new androidx.appcompat.view.g(getContext());
        }
        return this.f18180e;
    }

    protected abstract com.google.android.material.navigation.c e(Context context);

    public void f(int i8) {
        this.f18178c.d(true);
        getMenuInflater().inflate(i8, this.f18176a);
        this.f18178c.d(false);
        this.f18178c.f(true);
    }

    public Drawable getItemBackground() {
        return this.f18177b.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.f18177b.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.f18177b.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.f18177b.getIconTintList();
    }

    public ColorStateList getItemRippleColor() {
        return this.f18179d;
    }

    public int getItemTextAppearanceActive() {
        return this.f18177b.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.f18177b.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.f18177b.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.f18177b.getLabelVisibilityMode();
    }

    public abstract int getMaxItemCount();

    public Menu getMenu() {
        return this.f18176a;
    }

    public n getMenuView() {
        return this.f18177b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public NavigationBarPresenter getPresenter() {
        return this.f18178c;
    }

    public int getSelectedItemId() {
        return this.f18177b.getSelectedItemId();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.e(this);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        this.f18176a.S(savedState.f18183c);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Bundle bundle = new Bundle();
        savedState.f18183c = bundle;
        this.f18176a.U(bundle);
        return savedState;
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setElevation(f5);
        }
        i.d(this, f5);
    }

    public void setItemBackground(Drawable drawable) {
        this.f18177b.setItemBackground(drawable);
        this.f18179d = null;
    }

    public void setItemBackgroundResource(int i8) {
        this.f18177b.setItemBackgroundRes(i8);
        this.f18179d = null;
    }

    public void setItemIconSize(int i8) {
        this.f18177b.setItemIconSize(i8);
    }

    public void setItemIconSizeRes(int i8) {
        setItemIconSize(getResources().getDimensionPixelSize(i8));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.f18177b.setIconTintList(colorStateList);
    }

    public void setItemRippleColor(ColorStateList colorStateList) {
        if (this.f18179d == colorStateList) {
            if (colorStateList != null || this.f18177b.getItemBackground() == null) {
                return;
            }
            this.f18177b.setItemBackground(null);
            return;
        }
        this.f18179d = colorStateList;
        if (colorStateList == null) {
            this.f18177b.setItemBackground(null);
            return;
        }
        ColorStateList a9 = v7.b.a(colorStateList);
        if (Build.VERSION.SDK_INT >= 21) {
            this.f18177b.setItemBackground(new RippleDrawable(a9, null, null));
            return;
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1.0E-5f);
        Drawable r4 = androidx.core.graphics.drawable.a.r(gradientDrawable);
        androidx.core.graphics.drawable.a.o(r4, a9);
        this.f18177b.setItemBackground(r4);
    }

    public void setItemTextAppearanceActive(int i8) {
        this.f18177b.setItemTextAppearanceActive(i8);
    }

    public void setItemTextAppearanceInactive(int i8) {
        this.f18177b.setItemTextAppearanceInactive(i8);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f18177b.setItemTextColor(colorStateList);
    }

    public void setLabelVisibilityMode(int i8) {
        if (this.f18177b.getLabelVisibilityMode() != i8) {
            this.f18177b.setLabelVisibilityMode(i8);
            this.f18178c.f(false);
        }
    }

    public void setOnItemReselectedListener(c cVar) {
        this.f18182g = cVar;
    }

    public void setOnItemSelectedListener(d dVar) {
        this.f18181f = dVar;
    }

    public void setSelectedItemId(int i8) {
        MenuItem findItem = this.f18176a.findItem(i8);
        if (findItem == null || this.f18176a.O(findItem, this.f18178c, 0)) {
            return;
        }
        findItem.setChecked(true);
    }
}
