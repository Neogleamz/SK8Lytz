package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.view.b;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionMenuPresenter extends androidx.appcompat.view.menu.b implements b.a {
    private boolean A;
    private boolean B;
    private int C;
    private final SparseBooleanArray E;
    e F;
    a G;
    c H;
    private b K;
    final f L;
    int O;

    /* renamed from: l  reason: collision with root package name */
    d f1077l;

    /* renamed from: m  reason: collision with root package name */
    private Drawable f1078m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f1079n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f1080p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private int f1081t;

    /* renamed from: w  reason: collision with root package name */
    private int f1082w;

    /* renamed from: x  reason: collision with root package name */
    private int f1083x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f1084y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f1085z;

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        public int f1086a;

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

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.f1086a = parcel.readInt();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f1086a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends androidx.appcompat.view.menu.l {
        public a(Context context, androidx.appcompat.view.menu.r rVar, View view) {
            super(context, rVar, view, false, g.a.f19873l);
            if (!((androidx.appcompat.view.menu.i) rVar.getItem()).l()) {
                View view2 = ActionMenuPresenter.this.f1077l;
                f(view2 == null ? (View) ((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f879j : view2);
            }
            j(ActionMenuPresenter.this.L);
        }

        @Override // androidx.appcompat.view.menu.l
        protected void e() {
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            actionMenuPresenter.G = null;
            actionMenuPresenter.O = 0;
            super.e();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b extends ActionMenuItemView.b {
        b() {
        }

        @Override // androidx.appcompat.view.menu.ActionMenuItemView.b
        public androidx.appcompat.view.menu.p a() {
            a aVar = ActionMenuPresenter.this.G;
            if (aVar != null) {
                return aVar.c();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private e f1089a;

        public c(e eVar) {
            this.f1089a = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f873c != null) {
                ((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f873c.d();
            }
            View view = (View) ((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f879j;
            if (view != null && view.getWindowToken() != null && this.f1089a.m()) {
                ActionMenuPresenter.this.F = this.f1089a;
            }
            ActionMenuPresenter.this.H = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AppCompatImageView implements ActionMenuView.a {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends w {

            /* renamed from: k  reason: collision with root package name */
            final /* synthetic */ ActionMenuPresenter f1092k;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(View view, ActionMenuPresenter actionMenuPresenter) {
                super(view);
                this.f1092k = actionMenuPresenter;
            }

            @Override // androidx.appcompat.widget.w
            public androidx.appcompat.view.menu.p b() {
                e eVar = ActionMenuPresenter.this.F;
                if (eVar == null) {
                    return null;
                }
                return eVar.c();
            }

            @Override // androidx.appcompat.widget.w
            public boolean c() {
                ActionMenuPresenter.this.O();
                return true;
            }

            @Override // androidx.appcompat.widget.w
            public boolean d() {
                ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
                if (actionMenuPresenter.H != null) {
                    return false;
                }
                actionMenuPresenter.F();
                return true;
            }
        }

        public d(Context context) {
            super(context, null, g.a.f19872k);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            o0.a(this, getContentDescription());
            setOnTouchListener(new a(this, ActionMenuPresenter.this));
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean a() {
            return false;
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean b() {
            return false;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            ActionMenuPresenter.this.O();
            return true;
        }

        @Override // android.widget.ImageView
        protected boolean setFrame(int i8, int i9, int i10, int i11) {
            boolean frame = super.setFrame(i8, i9, i10, i11);
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null && background != null) {
                int width = getWidth();
                int height = getHeight();
                int max = Math.max(width, height) / 2;
                int paddingLeft = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                int paddingTop = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                androidx.core.graphics.drawable.a.l(background, paddingLeft - max, paddingTop - max, paddingLeft + max, paddingTop + max);
            }
            return frame;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends androidx.appcompat.view.menu.l {
        public e(Context context, androidx.appcompat.view.menu.g gVar, View view, boolean z4) {
            super(context, gVar, view, z4, g.a.f19873l);
            h(8388613);
            j(ActionMenuPresenter.this.L);
        }

        @Override // androidx.appcompat.view.menu.l
        protected void e() {
            if (((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f873c != null) {
                ((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f873c.close();
            }
            ActionMenuPresenter.this.F = null;
            super.e();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class f implements m.a {
        f() {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
            if (gVar instanceof androidx.appcompat.view.menu.r) {
                gVar.F().e(false);
            }
            m.a q = ActionMenuPresenter.this.q();
            if (q != null) {
                q.c(gVar, z4);
            }
        }

        @Override // androidx.appcompat.view.menu.m.a
        public boolean d(androidx.appcompat.view.menu.g gVar) {
            if (gVar == ((androidx.appcompat.view.menu.b) ActionMenuPresenter.this).f873c) {
                return false;
            }
            ActionMenuPresenter.this.O = ((androidx.appcompat.view.menu.r) gVar).getItem().getItemId();
            m.a q = ActionMenuPresenter.this.q();
            if (q != null) {
                return q.d(gVar);
            }
            return false;
        }
    }

    public ActionMenuPresenter(Context context) {
        super(context, g.g.f19963c, g.g.f19962b);
        this.E = new SparseBooleanArray();
        this.L = new f();
    }

    private View D(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.f879j;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = viewGroup.getChildAt(i8);
            if ((childAt instanceof n.a) && ((n.a) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    public boolean C() {
        return F() | G();
    }

    public Drawable E() {
        d dVar = this.f1077l;
        if (dVar != null) {
            return dVar.getDrawable();
        }
        if (this.f1079n) {
            return this.f1078m;
        }
        return null;
    }

    public boolean F() {
        androidx.appcompat.view.menu.n nVar;
        c cVar = this.H;
        if (cVar != null && (nVar = this.f879j) != null) {
            ((View) nVar).removeCallbacks(cVar);
            this.H = null;
            return true;
        }
        e eVar = this.F;
        if (eVar != null) {
            eVar.b();
            return true;
        }
        return false;
    }

    public boolean G() {
        a aVar = this.G;
        if (aVar != null) {
            aVar.b();
            return true;
        }
        return false;
    }

    public boolean H() {
        return this.H != null || I();
    }

    public boolean I() {
        e eVar = this.F;
        return eVar != null && eVar.d();
    }

    public void J(Configuration configuration) {
        if (!this.f1084y) {
            this.f1083x = androidx.appcompat.view.a.b(this.f872b).d();
        }
        androidx.appcompat.view.menu.g gVar = this.f873c;
        if (gVar != null) {
            gVar.M(true);
        }
    }

    public void K(boolean z4) {
        this.B = z4;
    }

    public void L(ActionMenuView actionMenuView) {
        this.f879j = actionMenuView;
        actionMenuView.b(this.f873c);
    }

    public void M(Drawable drawable) {
        d dVar = this.f1077l;
        if (dVar != null) {
            dVar.setImageDrawable(drawable);
            return;
        }
        this.f1079n = true;
        this.f1078m = drawable;
    }

    public void N(boolean z4) {
        this.f1080p = z4;
        this.q = true;
    }

    public boolean O() {
        androidx.appcompat.view.menu.g gVar;
        if (!this.f1080p || I() || (gVar = this.f873c) == null || this.f879j == null || this.H != null || gVar.B().isEmpty()) {
            return false;
        }
        c cVar = new c(new e(this.f872b, this.f873c, this.f1077l, true));
        this.H = cVar;
        ((View) this.f879j).post(cVar);
        return true;
    }

    @Override // androidx.core.view.b.a
    public void a(boolean z4) {
        if (z4) {
            super.n(null);
            return;
        }
        androidx.appcompat.view.menu.g gVar = this.f873c;
        if (gVar != null) {
            gVar.e(false);
        }
    }

    @Override // androidx.appcompat.view.menu.b, androidx.appcompat.view.menu.m
    public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
        C();
        super.c(gVar, z4);
    }

    @Override // androidx.appcompat.view.menu.b
    public void d(androidx.appcompat.view.menu.i iVar, n.a aVar) {
        aVar.e(iVar, 0);
        ActionMenuItemView actionMenuItemView = (ActionMenuItemView) aVar;
        actionMenuItemView.setItemInvoker((ActionMenuView) this.f879j);
        if (this.K == null) {
            this.K = new b();
        }
        actionMenuItemView.setPopupCallback(this.K);
    }

    @Override // androidx.appcompat.view.menu.b, androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        super.f(z4);
        ((View) this.f879j).requestLayout();
        androidx.appcompat.view.menu.g gVar = this.f873c;
        boolean z8 = false;
        if (gVar != null) {
            ArrayList<androidx.appcompat.view.menu.i> u8 = gVar.u();
            int size = u8.size();
            for (int i8 = 0; i8 < size; i8++) {
                androidx.core.view.b b9 = u8.get(i8).b();
                if (b9 != null) {
                    b9.i(this);
                }
            }
        }
        androidx.appcompat.view.menu.g gVar2 = this.f873c;
        ArrayList<androidx.appcompat.view.menu.i> B = gVar2 != null ? gVar2.B() : null;
        if (this.f1080p && B != null) {
            int size2 = B.size();
            if (size2 == 1) {
                z8 = !B.get(0).isActionViewExpanded();
            } else if (size2 > 0) {
                z8 = true;
            }
        }
        d dVar = this.f1077l;
        if (z8) {
            if (dVar == null) {
                this.f1077l = new d(this.f871a);
            }
            ViewGroup viewGroup = (ViewGroup) this.f1077l.getParent();
            if (viewGroup != this.f879j) {
                if (viewGroup != null) {
                    viewGroup.removeView(this.f1077l);
                }
                ActionMenuView actionMenuView = (ActionMenuView) this.f879j;
                actionMenuView.addView(this.f1077l, actionMenuView.F());
            }
        } else if (dVar != null) {
            ViewParent parent = dVar.getParent();
            androidx.appcompat.view.menu.n nVar = this.f879j;
            if (parent == nVar) {
                ((ViewGroup) nVar).removeView(this.f1077l);
            }
        }
        ((ActionMenuView) this.f879j).setOverflowReserved(this.f1080p);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v12 */
    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        ArrayList<androidx.appcompat.view.menu.i> arrayList;
        int i8;
        int i9;
        int i10;
        boolean z4;
        int i11;
        ActionMenuPresenter actionMenuPresenter = this;
        androidx.appcompat.view.menu.g gVar = actionMenuPresenter.f873c;
        View view = null;
        ?? r32 = 0;
        if (gVar != null) {
            arrayList = gVar.G();
            i8 = arrayList.size();
        } else {
            arrayList = null;
            i8 = 0;
        }
        int i12 = actionMenuPresenter.f1083x;
        int i13 = actionMenuPresenter.f1082w;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup viewGroup = (ViewGroup) actionMenuPresenter.f879j;
        boolean z8 = false;
        int i14 = 0;
        int i15 = 0;
        for (int i16 = 0; i16 < i8; i16++) {
            androidx.appcompat.view.menu.i iVar = arrayList.get(i16);
            if (iVar.o()) {
                i14++;
            } else if (iVar.n()) {
                i15++;
            } else {
                z8 = true;
            }
            if (actionMenuPresenter.B && iVar.isActionViewExpanded()) {
                i12 = 0;
            }
        }
        if (actionMenuPresenter.f1080p && (z8 || i15 + i14 > i12)) {
            i12--;
        }
        int i17 = i12 - i14;
        SparseBooleanArray sparseBooleanArray = actionMenuPresenter.E;
        sparseBooleanArray.clear();
        if (actionMenuPresenter.f1085z) {
            int i18 = actionMenuPresenter.C;
            i10 = i13 / i18;
            i9 = i18 + ((i13 % i18) / i10);
        } else {
            i9 = 0;
            i10 = 0;
        }
        int i19 = 0;
        int i20 = 0;
        while (i19 < i8) {
            androidx.appcompat.view.menu.i iVar2 = arrayList.get(i19);
            if (iVar2.o()) {
                View r4 = actionMenuPresenter.r(iVar2, view, viewGroup);
                if (actionMenuPresenter.f1085z) {
                    i10 -= ActionMenuView.L(r4, i9, i10, makeMeasureSpec, r32);
                } else {
                    r4.measure(makeMeasureSpec, makeMeasureSpec);
                }
                int measuredWidth = r4.getMeasuredWidth();
                i13 -= measuredWidth;
                if (i20 == 0) {
                    i20 = measuredWidth;
                }
                int groupId = iVar2.getGroupId();
                if (groupId != 0) {
                    sparseBooleanArray.put(groupId, true);
                }
                iVar2.u(true);
                z4 = r32;
                i11 = i8;
            } else if (iVar2.n()) {
                int groupId2 = iVar2.getGroupId();
                boolean z9 = sparseBooleanArray.get(groupId2);
                boolean z10 = (i17 > 0 || z9) && i13 > 0 && (!actionMenuPresenter.f1085z || i10 > 0);
                boolean z11 = z10;
                i11 = i8;
                if (z10) {
                    View r8 = actionMenuPresenter.r(iVar2, null, viewGroup);
                    if (actionMenuPresenter.f1085z) {
                        int L = ActionMenuView.L(r8, i9, i10, makeMeasureSpec, 0);
                        i10 -= L;
                        if (L == 0) {
                            z11 = false;
                        }
                    } else {
                        r8.measure(makeMeasureSpec, makeMeasureSpec);
                    }
                    boolean z12 = z11;
                    int measuredWidth2 = r8.getMeasuredWidth();
                    i13 -= measuredWidth2;
                    if (i20 == 0) {
                        i20 = measuredWidth2;
                    }
                    z10 = z12 & (!actionMenuPresenter.f1085z ? i13 + i20 <= 0 : i13 < 0);
                }
                if (z10 && groupId2 != 0) {
                    sparseBooleanArray.put(groupId2, true);
                } else if (z9) {
                    sparseBooleanArray.put(groupId2, false);
                    for (int i21 = 0; i21 < i19; i21++) {
                        androidx.appcompat.view.menu.i iVar3 = arrayList.get(i21);
                        if (iVar3.getGroupId() == groupId2) {
                            if (iVar3.l()) {
                                i17++;
                            }
                            iVar3.u(false);
                        }
                    }
                }
                if (z10) {
                    i17--;
                }
                iVar2.u(z10);
                z4 = false;
            } else {
                z4 = r32;
                i11 = i8;
                iVar2.u(z4);
            }
            i19++;
            r32 = z4;
            i8 = i11;
            view = null;
            actionMenuPresenter = this;
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.b, androidx.appcompat.view.menu.m
    public void k(Context context, androidx.appcompat.view.menu.g gVar) {
        super.k(context, gVar);
        Resources resources = context.getResources();
        androidx.appcompat.view.a b9 = androidx.appcompat.view.a.b(context);
        if (!this.q) {
            this.f1080p = b9.h();
        }
        if (!this.A) {
            this.f1081t = b9.c();
        }
        if (!this.f1084y) {
            this.f1083x = b9.d();
        }
        int i8 = this.f1081t;
        if (this.f1080p) {
            if (this.f1077l == null) {
                d dVar = new d(this.f871a);
                this.f1077l = dVar;
                if (this.f1079n) {
                    dVar.setImageDrawable(this.f1078m);
                    this.f1078m = null;
                    this.f1079n = false;
                }
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.f1077l.measure(makeMeasureSpec, makeMeasureSpec);
            }
            i8 -= this.f1077l.getMeasuredWidth();
        } else {
            this.f1077l = null;
        }
        this.f1082w = i8;
        this.C = (int) (resources.getDisplayMetrics().density * 56.0f);
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
        int i8;
        MenuItem findItem;
        if ((parcelable instanceof SavedState) && (i8 = ((SavedState) parcelable).f1086a) > 0 && (findItem = this.f873c.findItem(i8)) != null) {
            n((androidx.appcompat.view.menu.r) findItem.getSubMenu());
        }
    }

    @Override // androidx.appcompat.view.menu.b, androidx.appcompat.view.menu.m
    public boolean n(androidx.appcompat.view.menu.r rVar) {
        boolean z4 = false;
        if (rVar.hasVisibleItems()) {
            androidx.appcompat.view.menu.r rVar2 = rVar;
            while (rVar2.i0() != this.f873c) {
                rVar2 = (androidx.appcompat.view.menu.r) rVar2.i0();
            }
            View D = D(rVar2.getItem());
            if (D == null) {
                return false;
            }
            this.O = rVar.getItem().getItemId();
            int size = rVar.size();
            int i8 = 0;
            while (true) {
                if (i8 >= size) {
                    break;
                }
                MenuItem item = rVar.getItem(i8);
                if (item.isVisible() && item.getIcon() != null) {
                    z4 = true;
                    break;
                }
                i8++;
            }
            a aVar = new a(this.f872b, rVar, D);
            this.G = aVar;
            aVar.g(z4);
            this.G.k();
            super.n(rVar);
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        SavedState savedState = new SavedState();
        savedState.f1086a = this.O;
        return savedState;
    }

    @Override // androidx.appcompat.view.menu.b
    public boolean p(ViewGroup viewGroup, int i8) {
        if (viewGroup.getChildAt(i8) == this.f1077l) {
            return false;
        }
        return super.p(viewGroup, i8);
    }

    @Override // androidx.appcompat.view.menu.b
    public View r(androidx.appcompat.view.menu.i iVar, View view, ViewGroup viewGroup) {
        View actionView = iVar.getActionView();
        if (actionView == null || iVar.j()) {
            actionView = super.r(iVar, view, viewGroup);
        }
        actionView.setVisibility(iVar.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        ViewGroup.LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.o(layoutParams));
        }
        return actionView;
    }

    @Override // androidx.appcompat.view.menu.b
    public androidx.appcompat.view.menu.n s(ViewGroup viewGroup) {
        androidx.appcompat.view.menu.n nVar = this.f879j;
        androidx.appcompat.view.menu.n s8 = super.s(viewGroup);
        if (nVar != s8) {
            ((ActionMenuView) s8).setPresenter(this);
        }
        return s8;
    }

    @Override // androidx.appcompat.view.menu.b
    public boolean u(int i8, androidx.appcompat.view.menu.i iVar) {
        return iVar.l();
    }
}
