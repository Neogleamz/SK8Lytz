package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.m;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.w;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f implements androidx.appcompat.view.menu.m {

    /* renamed from: a  reason: collision with root package name */
    private NavigationMenuView f18088a;

    /* renamed from: b  reason: collision with root package name */
    LinearLayout f18089b;

    /* renamed from: c  reason: collision with root package name */
    private m.a f18090c;

    /* renamed from: d  reason: collision with root package name */
    androidx.appcompat.view.menu.g f18091d;

    /* renamed from: e  reason: collision with root package name */
    private int f18092e;

    /* renamed from: f  reason: collision with root package name */
    c f18093f;

    /* renamed from: g  reason: collision with root package name */
    LayoutInflater f18094g;

    /* renamed from: h  reason: collision with root package name */
    int f18095h;

    /* renamed from: j  reason: collision with root package name */
    boolean f18096j;

    /* renamed from: k  reason: collision with root package name */
    ColorStateList f18097k;

    /* renamed from: l  reason: collision with root package name */
    ColorStateList f18098l;

    /* renamed from: m  reason: collision with root package name */
    Drawable f18099m;

    /* renamed from: n  reason: collision with root package name */
    int f18100n;

    /* renamed from: p  reason: collision with root package name */
    int f18101p;
    int q;

    /* renamed from: t  reason: collision with root package name */
    boolean f18102t;

    /* renamed from: x  reason: collision with root package name */
    private int f18104x;

    /* renamed from: y  reason: collision with root package name */
    private int f18105y;

    /* renamed from: z  reason: collision with root package name */
    int f18106z;

    /* renamed from: w  reason: collision with root package name */
    boolean f18103w = true;
    private int A = -1;
    final View.OnClickListener B = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean z4 = true;
            f.this.L(true);
            androidx.appcompat.view.menu.i itemData = ((NavigationMenuItemView) view).getItemData();
            f fVar = f.this;
            boolean O = fVar.f18091d.O(itemData, fVar, 0);
            if (itemData != null && itemData.isCheckable() && O) {
                f.this.f18093f.L(itemData);
            } else {
                z4 = false;
            }
            f.this.L(false);
            if (z4) {
                f.this.f(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends l {
        public b(View view) {
            super(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends RecyclerView.g<l> {

        /* renamed from: c  reason: collision with root package name */
        private final ArrayList<e> f18108c = new ArrayList<>();

        /* renamed from: d  reason: collision with root package name */
        private androidx.appcompat.view.menu.i f18109d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f18110e;

        c() {
            J();
        }

        private void C(int i8, int i9) {
            while (i8 < i9) {
                ((g) this.f18108c.get(i8)).f18115b = true;
                i8++;
            }
        }

        private void J() {
            if (this.f18110e) {
                return;
            }
            boolean z4 = true;
            this.f18110e = true;
            this.f18108c.clear();
            this.f18108c.add(new d());
            int i8 = -1;
            int size = f.this.f18091d.G().size();
            int i9 = 0;
            boolean z8 = false;
            int i10 = 0;
            while (i9 < size) {
                androidx.appcompat.view.menu.i iVar = f.this.f18091d.G().get(i9);
                if (iVar.isChecked()) {
                    L(iVar);
                }
                if (iVar.isCheckable()) {
                    iVar.t(false);
                }
                if (iVar.hasSubMenu()) {
                    SubMenu subMenu = iVar.getSubMenu();
                    if (subMenu.hasVisibleItems()) {
                        if (i9 != 0) {
                            this.f18108c.add(new C0136f(f.this.f18106z, 0));
                        }
                        this.f18108c.add(new g(iVar));
                        int size2 = this.f18108c.size();
                        int size3 = subMenu.size();
                        int i11 = 0;
                        boolean z9 = false;
                        while (i11 < size3) {
                            androidx.appcompat.view.menu.i iVar2 = (androidx.appcompat.view.menu.i) subMenu.getItem(i11);
                            if (iVar2.isVisible()) {
                                if (!z9 && iVar2.getIcon() != null) {
                                    z9 = z4;
                                }
                                if (iVar2.isCheckable()) {
                                    iVar2.t(false);
                                }
                                if (iVar.isChecked()) {
                                    L(iVar);
                                }
                                this.f18108c.add(new g(iVar2));
                            }
                            i11++;
                            z4 = true;
                        }
                        if (z9) {
                            C(size2, this.f18108c.size());
                        }
                    }
                } else {
                    int groupId = iVar.getGroupId();
                    if (groupId != i8) {
                        i10 = this.f18108c.size();
                        z8 = iVar.getIcon() != null;
                        if (i9 != 0) {
                            i10++;
                            ArrayList<e> arrayList = this.f18108c;
                            int i12 = f.this.f18106z;
                            arrayList.add(new C0136f(i12, i12));
                        }
                    } else if (!z8 && iVar.getIcon() != null) {
                        C(i10, this.f18108c.size());
                        z8 = true;
                    }
                    g gVar = new g(iVar);
                    gVar.f18115b = z8;
                    this.f18108c.add(gVar);
                    i8 = groupId;
                }
                i9++;
                z4 = true;
            }
            this.f18110e = false;
        }

        public Bundle D() {
            Bundle bundle = new Bundle();
            androidx.appcompat.view.menu.i iVar = this.f18109d;
            if (iVar != null) {
                bundle.putInt("android:menu:checked", iVar.getItemId());
            }
            SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
            int size = this.f18108c.size();
            for (int i8 = 0; i8 < size; i8++) {
                e eVar = this.f18108c.get(i8);
                if (eVar instanceof g) {
                    androidx.appcompat.view.menu.i a9 = ((g) eVar).a();
                    View actionView = a9 != null ? a9.getActionView() : null;
                    if (actionView != null) {
                        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState(parcelableSparseArray);
                        sparseArray.put(a9.getItemId(), parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
            return bundle;
        }

        public androidx.appcompat.view.menu.i E() {
            return this.f18109d;
        }

        int F() {
            int i8 = f.this.f18089b.getChildCount() == 0 ? 0 : 1;
            for (int i9 = 0; i9 < f.this.f18093f.c(); i9++) {
                if (f.this.f18093f.e(i9) == 0) {
                    i8++;
                }
            }
            return i8;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        /* renamed from: G */
        public void r(l lVar, int i8) {
            int e8 = e(i8);
            if (e8 != 0) {
                if (e8 == 1) {
                    ((TextView) lVar.f6628a).setText(((g) this.f18108c.get(i8)).a().getTitle());
                    return;
                } else if (e8 != 2) {
                    return;
                } else {
                    C0136f c0136f = (C0136f) this.f18108c.get(i8);
                    lVar.f6628a.setPadding(0, c0136f.b(), 0, c0136f.a());
                    return;
                }
            }
            NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) lVar.f6628a;
            navigationMenuItemView.setIconTintList(f.this.f18098l);
            f fVar = f.this;
            if (fVar.f18096j) {
                navigationMenuItemView.setTextAppearance(fVar.f18095h);
            }
            ColorStateList colorStateList = f.this.f18097k;
            if (colorStateList != null) {
                navigationMenuItemView.setTextColor(colorStateList);
            }
            Drawable drawable = f.this.f18099m;
            c0.x0(navigationMenuItemView, drawable != null ? drawable.getConstantState().newDrawable() : null);
            g gVar = (g) this.f18108c.get(i8);
            navigationMenuItemView.setNeedsEmptyIcon(gVar.f18115b);
            navigationMenuItemView.setHorizontalPadding(f.this.f18100n);
            navigationMenuItemView.setIconPadding(f.this.f18101p);
            f fVar2 = f.this;
            if (fVar2.f18102t) {
                navigationMenuItemView.setIconSize(fVar2.q);
            }
            navigationMenuItemView.setMaxLines(f.this.f18104x);
            navigationMenuItemView.e(gVar.a(), 0);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        /* renamed from: H */
        public l t(ViewGroup viewGroup, int i8) {
            if (i8 == 0) {
                f fVar = f.this;
                return new i(fVar.f18094g, viewGroup, fVar.B);
            } else if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        return null;
                    }
                    return new b(f.this.f18089b);
                }
                return new j(f.this.f18094g, viewGroup);
            } else {
                return new k(f.this.f18094g, viewGroup);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        /* renamed from: I */
        public void y(l lVar) {
            if (lVar instanceof i) {
                ((NavigationMenuItemView) lVar.f6628a).D();
            }
        }

        public void K(Bundle bundle) {
            androidx.appcompat.view.menu.i a9;
            View actionView;
            ParcelableSparseArray parcelableSparseArray;
            androidx.appcompat.view.menu.i a10;
            int i8 = bundle.getInt("android:menu:checked", 0);
            if (i8 != 0) {
                this.f18110e = true;
                int size = this.f18108c.size();
                int i9 = 0;
                while (true) {
                    if (i9 >= size) {
                        break;
                    }
                    e eVar = this.f18108c.get(i9);
                    if ((eVar instanceof g) && (a10 = ((g) eVar).a()) != null && a10.getItemId() == i8) {
                        L(a10);
                        break;
                    }
                    i9++;
                }
                this.f18110e = false;
                J();
            }
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:action_views");
            if (sparseParcelableArray != null) {
                int size2 = this.f18108c.size();
                for (int i10 = 0; i10 < size2; i10++) {
                    e eVar2 = this.f18108c.get(i10);
                    if ((eVar2 instanceof g) && (a9 = ((g) eVar2).a()) != null && (actionView = a9.getActionView()) != null && (parcelableSparseArray = (ParcelableSparseArray) sparseParcelableArray.get(a9.getItemId())) != null) {
                        actionView.restoreHierarchyState(parcelableSparseArray);
                    }
                }
            }
        }

        public void L(androidx.appcompat.view.menu.i iVar) {
            if (this.f18109d == iVar || !iVar.isCheckable()) {
                return;
            }
            androidx.appcompat.view.menu.i iVar2 = this.f18109d;
            if (iVar2 != null) {
                iVar2.setChecked(false);
            }
            this.f18109d = iVar;
            iVar.setChecked(true);
        }

        public void M(boolean z4) {
            this.f18110e = z4;
        }

        public void N() {
            J();
            h();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        public int c() {
            return this.f18108c.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        public long d(int i8) {
            return i8;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.g
        public int e(int i8) {
            e eVar = this.f18108c.get(i8);
            if (eVar instanceof C0136f) {
                return 2;
            }
            if (eVar instanceof d) {
                return 3;
            }
            if (eVar instanceof g) {
                return ((g) eVar).a().hasSubMenu() ? 1 : 0;
            }
            throw new RuntimeException("Unknown item type.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d implements e {
        d() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.android.material.internal.f$f  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0136f implements e {

        /* renamed from: a  reason: collision with root package name */
        private final int f18112a;

        /* renamed from: b  reason: collision with root package name */
        private final int f18113b;

        public C0136f(int i8, int i9) {
            this.f18112a = i8;
            this.f18113b = i9;
        }

        public int a() {
            return this.f18113b;
        }

        public int b() {
            return this.f18112a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g implements e {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.appcompat.view.menu.i f18114a;

        /* renamed from: b  reason: collision with root package name */
        boolean f18115b;

        g(androidx.appcompat.view.menu.i iVar) {
            this.f18114a = iVar;
        }

        public androidx.appcompat.view.menu.i a() {
            return this.f18114a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class h extends w {
        h(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override // androidx.recyclerview.widget.w, androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.e0(c.b.a(f.this.f18093f.F(), 0, false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class i extends l {
        public i(LayoutInflater layoutInflater, ViewGroup viewGroup, View.OnClickListener onClickListener) {
            super(layoutInflater.inflate(k7.h.f21185g, viewGroup, false));
            this.f6628a.setOnClickListener(onClickListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j extends l {
        public j(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(k7.h.f21187i, viewGroup, false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k extends l {
        public k(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(k7.h.f21188j, viewGroup, false));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class l extends RecyclerView.b0 {
        public l(View view) {
            super(view);
        }
    }

    private void M() {
        int i8 = (this.f18089b.getChildCount() == 0 && this.f18103w) ? this.f18105y : 0;
        NavigationMenuView navigationMenuView = this.f18088a;
        navigationMenuView.setPadding(0, i8, 0, navigationMenuView.getPaddingBottom());
    }

    public void A(androidx.appcompat.view.menu.i iVar) {
        this.f18093f.L(iVar);
    }

    public void B(int i8) {
        this.f18092e = i8;
    }

    public void C(Drawable drawable) {
        this.f18099m = drawable;
        f(false);
    }

    public void D(int i8) {
        this.f18100n = i8;
        f(false);
    }

    public void E(int i8) {
        this.f18101p = i8;
        f(false);
    }

    public void F(int i8) {
        if (this.q != i8) {
            this.q = i8;
            this.f18102t = true;
            f(false);
        }
    }

    public void G(ColorStateList colorStateList) {
        this.f18098l = colorStateList;
        f(false);
    }

    public void H(int i8) {
        this.f18104x = i8;
        f(false);
    }

    public void I(int i8) {
        this.f18095h = i8;
        this.f18096j = true;
        f(false);
    }

    public void J(ColorStateList colorStateList) {
        this.f18097k = colorStateList;
        f(false);
    }

    public void K(int i8) {
        this.A = i8;
        NavigationMenuView navigationMenuView = this.f18088a;
        if (navigationMenuView != null) {
            navigationMenuView.setOverScrollMode(i8);
        }
    }

    public void L(boolean z4) {
        c cVar = this.f18093f;
        if (cVar != null) {
            cVar.M(z4);
        }
    }

    public void b(View view) {
        this.f18089b.addView(view);
        NavigationMenuView navigationMenuView = this.f18088a;
        navigationMenuView.setPadding(0, 0, 0, navigationMenuView.getPaddingBottom());
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
        m.a aVar = this.f18090c;
        if (aVar != null) {
            aVar.c(gVar, z4);
        }
    }

    public void d(m0 m0Var) {
        int m8 = m0Var.m();
        if (this.f18105y != m8) {
            this.f18105y = m8;
            M();
        }
        NavigationMenuView navigationMenuView = this.f18088a;
        navigationMenuView.setPadding(0, navigationMenuView.getPaddingTop(), 0, m0Var.j());
        c0.i(this.f18089b, m0Var);
    }

    @Override // androidx.appcompat.view.menu.m
    public int e() {
        return this.f18092e;
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        c cVar = this.f18093f;
        if (cVar != null) {
            cVar.N();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean h(androidx.appcompat.view.menu.g gVar, androidx.appcompat.view.menu.i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean i(androidx.appcompat.view.menu.g gVar, androidx.appcompat.view.menu.i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public void k(Context context, androidx.appcompat.view.menu.g gVar) {
        this.f18094g = LayoutInflater.from(context);
        this.f18091d = gVar;
        this.f18106z = context.getResources().getDimensionPixelOffset(k7.d.f21114m);
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.f18088a.restoreHierarchyState(sparseParcelableArray);
            }
            Bundle bundle2 = bundle.getBundle("android:menu:adapter");
            if (bundle2 != null) {
                this.f18093f.K(bundle2);
            }
            SparseArray sparseParcelableArray2 = bundle.getSparseParcelableArray("android:menu:header");
            if (sparseParcelableArray2 != null) {
                this.f18089b.restoreHierarchyState(sparseParcelableArray2);
            }
        }
    }

    public androidx.appcompat.view.menu.i m() {
        return this.f18093f.E();
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean n(androidx.appcompat.view.menu.r rVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        Bundle bundle = new Bundle();
        if (this.f18088a != null) {
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            this.f18088a.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        c cVar = this.f18093f;
        if (cVar != null) {
            bundle.putBundle("android:menu:adapter", cVar.D());
        }
        if (this.f18089b != null) {
            SparseArray<? extends Parcelable> sparseArray2 = new SparseArray<>();
            this.f18089b.saveHierarchyState(sparseArray2);
            bundle.putSparseParcelableArray("android:menu:header", sparseArray2);
        }
        return bundle;
    }

    public int p() {
        return this.f18089b.getChildCount();
    }

    public View q(int i8) {
        return this.f18089b.getChildAt(i8);
    }

    public Drawable r() {
        return this.f18099m;
    }

    public int s() {
        return this.f18100n;
    }

    public int t() {
        return this.f18101p;
    }

    public int u() {
        return this.f18104x;
    }

    public ColorStateList v() {
        return this.f18097k;
    }

    public ColorStateList w() {
        return this.f18098l;
    }

    public androidx.appcompat.view.menu.n x(ViewGroup viewGroup) {
        if (this.f18088a == null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) this.f18094g.inflate(k7.h.f21189k, viewGroup, false);
            this.f18088a = navigationMenuView;
            navigationMenuView.setAccessibilityDelegateCompat(new h(this.f18088a));
            if (this.f18093f == null) {
                this.f18093f = new c();
            }
            int i8 = this.A;
            if (i8 != -1) {
                this.f18088a.setOverScrollMode(i8);
            }
            this.f18089b = (LinearLayout) this.f18094g.inflate(k7.h.f21186h, (ViewGroup) this.f18088a, false);
            this.f18088a.setAdapter(this.f18093f);
        }
        return this.f18088a;
    }

    public View y(int i8) {
        View inflate = this.f18094g.inflate(i8, (ViewGroup) this.f18089b, false);
        b(inflate);
        return inflate;
    }

    public void z(boolean z4) {
        if (this.f18103w != z4) {
            this.f18103w = z4;
            M();
        }
    }
}
