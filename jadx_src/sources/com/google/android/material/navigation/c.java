package com.google.android.material.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.i;
import androidx.appcompat.view.menu.n;
import androidx.core.util.e;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionSet;
import androidx.transition.s;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.k;
import java.util.HashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c extends ViewGroup implements n {

    /* renamed from: a  reason: collision with root package name */
    private final TransitionSet f18216a;

    /* renamed from: b  reason: collision with root package name */
    private final View.OnClickListener f18217b;

    /* renamed from: c  reason: collision with root package name */
    private final e<com.google.android.material.navigation.a> f18218c;

    /* renamed from: d  reason: collision with root package name */
    private final SparseArray<View.OnTouchListener> f18219d;

    /* renamed from: e  reason: collision with root package name */
    private int f18220e;

    /* renamed from: f  reason: collision with root package name */
    private com.google.android.material.navigation.a[] f18221f;

    /* renamed from: g  reason: collision with root package name */
    private int f18222g;

    /* renamed from: h  reason: collision with root package name */
    private int f18223h;

    /* renamed from: j  reason: collision with root package name */
    private ColorStateList f18224j;

    /* renamed from: k  reason: collision with root package name */
    private int f18225k;

    /* renamed from: l  reason: collision with root package name */
    private ColorStateList f18226l;

    /* renamed from: m  reason: collision with root package name */
    private final ColorStateList f18227m;

    /* renamed from: n  reason: collision with root package name */
    private int f18228n;

    /* renamed from: p  reason: collision with root package name */
    private int f18229p;
    private Drawable q;

    /* renamed from: t  reason: collision with root package name */
    private int f18230t;

    /* renamed from: w  reason: collision with root package name */
    private SparseArray<BadgeDrawable> f18231w;

    /* renamed from: x  reason: collision with root package name */
    private NavigationBarPresenter f18232x;

    /* renamed from: y  reason: collision with root package name */
    private g f18233y;

    /* renamed from: z  reason: collision with root package name */
    private static final int[] f18215z = {16842912};
    private static final int[] A = {-16842910};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            i itemData = ((com.google.android.material.navigation.a) view).getItemData();
            if (c.this.f18233y.O(itemData, c.this.f18232x, 0)) {
                return;
            }
            itemData.setChecked(true);
        }
    }

    public c(Context context) {
        super(context);
        this.f18218c = new androidx.core.util.g(5);
        this.f18219d = new SparseArray<>(5);
        this.f18222g = 0;
        this.f18223h = 0;
        this.f18231w = new SparseArray<>(5);
        this.f18227m = e(16842808);
        AutoTransition autoTransition = new AutoTransition();
        this.f18216a = autoTransition;
        autoTransition.y0(0);
        autoTransition.f0(115L);
        autoTransition.h0(new d1.b());
        autoTransition.q0(new k());
        this.f18217b = new a();
        c0.E0(this, 1);
    }

    private com.google.android.material.navigation.a getNewItem() {
        com.google.android.material.navigation.a b9 = this.f18218c.b();
        return b9 == null ? f(getContext()) : b9;
    }

    private boolean h(int i8) {
        return i8 != -1;
    }

    private void i() {
        HashSet hashSet = new HashSet();
        for (int i8 = 0; i8 < this.f18233y.size(); i8++) {
            hashSet.add(Integer.valueOf(this.f18233y.getItem(i8).getItemId()));
        }
        for (int i9 = 0; i9 < this.f18231w.size(); i9++) {
            int keyAt = this.f18231w.keyAt(i9);
            if (!hashSet.contains(Integer.valueOf(keyAt))) {
                this.f18231w.delete(keyAt);
            }
        }
    }

    private void setBadgeIfNeeded(com.google.android.material.navigation.a aVar) {
        BadgeDrawable badgeDrawable;
        int id = aVar.getId();
        if (h(id) && (badgeDrawable = this.f18231w.get(id)) != null) {
            aVar.setBadge(badgeDrawable);
        }
    }

    @Override // androidx.appcompat.view.menu.n
    public void b(g gVar) {
        this.f18233y = gVar;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void d() {
        removeAllViews();
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                if (aVar != null) {
                    this.f18218c.a(aVar);
                    aVar.h();
                }
            }
        }
        if (this.f18233y.size() == 0) {
            this.f18222g = 0;
            this.f18223h = 0;
            this.f18221f = null;
            return;
        }
        i();
        this.f18221f = new com.google.android.material.navigation.a[this.f18233y.size()];
        boolean g8 = g(this.f18220e, this.f18233y.G().size());
        for (int i8 = 0; i8 < this.f18233y.size(); i8++) {
            this.f18232x.d(true);
            this.f18233y.getItem(i8).setCheckable(true);
            this.f18232x.d(false);
            com.google.android.material.navigation.a newItem = getNewItem();
            this.f18221f[i8] = newItem;
            newItem.setIconTintList(this.f18224j);
            newItem.setIconSize(this.f18225k);
            newItem.setTextColor(this.f18227m);
            newItem.setTextAppearanceInactive(this.f18228n);
            newItem.setTextAppearanceActive(this.f18229p);
            newItem.setTextColor(this.f18226l);
            Drawable drawable = this.q;
            if (drawable != null) {
                newItem.setItemBackground(drawable);
            } else {
                newItem.setItemBackground(this.f18230t);
            }
            newItem.setShifting(g8);
            newItem.setLabelVisibilityMode(this.f18220e);
            i iVar = (i) this.f18233y.getItem(i8);
            newItem.e(iVar, 0);
            newItem.setItemPosition(i8);
            int itemId = iVar.getItemId();
            newItem.setOnTouchListener(this.f18219d.get(itemId));
            newItem.setOnClickListener(this.f18217b);
            int i9 = this.f18222g;
            if (i9 != 0 && itemId == i9) {
                this.f18223h = i8;
            }
            setBadgeIfNeeded(newItem);
            addView(newItem);
        }
        int min = Math.min(this.f18233y.size() - 1, this.f18223h);
        this.f18223h = min;
        this.f18233y.getItem(min).setChecked(true);
    }

    public ColorStateList e(int i8) {
        TypedValue typedValue = new TypedValue();
        if (getContext().getTheme().resolveAttribute(i8, typedValue, true)) {
            ColorStateList a9 = h.a.a(getContext(), typedValue.resourceId);
            if (getContext().getTheme().resolveAttribute(g.a.f19885y, typedValue, true)) {
                int i9 = typedValue.data;
                int defaultColor = a9.getDefaultColor();
                int[] iArr = A;
                return new ColorStateList(new int[][]{iArr, f18215z, ViewGroup.EMPTY_STATE_SET}, new int[]{a9.getColorForState(iArr, defaultColor), i9, defaultColor});
            }
            return null;
        }
        return null;
    }

    protected abstract com.google.android.material.navigation.a f(Context context);

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean g(int i8, int i9) {
        if (i8 == -1) {
            if (i9 > 3) {
                return true;
            }
        } else if (i8 == 0) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<BadgeDrawable> getBadgeDrawables() {
        return this.f18231w;
    }

    public ColorStateList getIconTintList() {
        return this.f18224j;
    }

    public Drawable getItemBackground() {
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        return (aVarArr == null || aVarArr.length <= 0) ? this.q : aVarArr[0].getBackground();
    }

    @Deprecated
    public int getItemBackgroundRes() {
        return this.f18230t;
    }

    public int getItemIconSize() {
        return this.f18225k;
    }

    public int getItemTextAppearanceActive() {
        return this.f18229p;
    }

    public int getItemTextAppearanceInactive() {
        return this.f18228n;
    }

    public ColorStateList getItemTextColor() {
        return this.f18226l;
    }

    public int getLabelVisibilityMode() {
        return this.f18220e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g getMenu() {
        return this.f18233y;
    }

    public int getSelectedItemId() {
        return this.f18222g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSelectedItemPosition() {
        return this.f18223h;
    }

    public int getWindowAnimations() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(int i8) {
        int size = this.f18233y.size();
        for (int i9 = 0; i9 < size; i9++) {
            MenuItem item = this.f18233y.getItem(i9);
            if (i8 == item.getItemId()) {
                this.f18222g = i8;
                this.f18223h = i9;
                item.setChecked(true);
                return;
            }
        }
    }

    public void k() {
        g gVar = this.f18233y;
        if (gVar == null || this.f18221f == null) {
            return;
        }
        int size = gVar.size();
        if (size != this.f18221f.length) {
            d();
            return;
        }
        int i8 = this.f18222g;
        for (int i9 = 0; i9 < size; i9++) {
            MenuItem item = this.f18233y.getItem(i9);
            if (item.isChecked()) {
                this.f18222g = item.getItemId();
                this.f18223h = i9;
            }
        }
        if (i8 != this.f18222g) {
            s.a(this, this.f18216a);
        }
        boolean g8 = g(this.f18220e, this.f18233y.G().size());
        for (int i10 = 0; i10 < size; i10++) {
            this.f18232x.d(true);
            this.f18221f[i10].setLabelVisibilityMode(this.f18220e);
            this.f18221f[i10].setShifting(g8);
            this.f18221f[i10].e((i) this.f18233y.getItem(i10), 0);
            this.f18232x.d(false);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).e0(c.b.b(1, this.f18233y.G().size(), false, 1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBadgeDrawables(SparseArray<BadgeDrawable> sparseArray) {
        this.f18231w = sparseArray;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setBadge(sparseArray.get(aVar.getId()));
            }
        }
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.f18224j = colorStateList;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setIconTintList(colorStateList);
            }
        }
    }

    public void setItemBackground(Drawable drawable) {
        this.q = drawable;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setItemBackground(drawable);
            }
        }
    }

    public void setItemBackgroundRes(int i8) {
        this.f18230t = i8;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setItemBackground(i8);
            }
        }
    }

    public void setItemIconSize(int i8) {
        this.f18225k = i8;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setIconSize(i8);
            }
        }
    }

    public void setItemTextAppearanceActive(int i8) {
        this.f18229p = i8;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setTextAppearanceActive(i8);
                ColorStateList colorStateList = this.f18226l;
                if (colorStateList != null) {
                    aVar.setTextColor(colorStateList);
                }
            }
        }
    }

    public void setItemTextAppearanceInactive(int i8) {
        this.f18228n = i8;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setTextAppearanceInactive(i8);
                ColorStateList colorStateList = this.f18226l;
                if (colorStateList != null) {
                    aVar.setTextColor(colorStateList);
                }
            }
        }
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f18226l = colorStateList;
        com.google.android.material.navigation.a[] aVarArr = this.f18221f;
        if (aVarArr != null) {
            for (com.google.android.material.navigation.a aVar : aVarArr) {
                aVar.setTextColor(colorStateList);
            }
        }
    }

    public void setLabelVisibilityMode(int i8) {
        this.f18220e = i8;
    }

    public void setPresenter(NavigationBarPresenter navigationBarPresenter) {
        this.f18232x = navigationBarPresenter;
    }
}
