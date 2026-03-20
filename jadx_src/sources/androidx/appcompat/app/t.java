package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.b;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.c0;
import androidx.core.view.i0;
import androidx.core.view.j0;
import androidx.core.view.k0;
import androidx.core.view.l0;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t extends ActionBar implements ActionBarOverlayLayout.d {
    private static final Interpolator E = new AccelerateInterpolator();
    private static final Interpolator F = new DecelerateInterpolator();
    boolean A;

    /* renamed from: a  reason: collision with root package name */
    Context f723a;

    /* renamed from: b  reason: collision with root package name */
    private Context f724b;

    /* renamed from: c  reason: collision with root package name */
    private Activity f725c;

    /* renamed from: d  reason: collision with root package name */
    ActionBarOverlayLayout f726d;

    /* renamed from: e  reason: collision with root package name */
    ActionBarContainer f727e;

    /* renamed from: f  reason: collision with root package name */
    androidx.appcompat.widget.s f728f;

    /* renamed from: g  reason: collision with root package name */
    ActionBarContextView f729g;

    /* renamed from: h  reason: collision with root package name */
    View f730h;

    /* renamed from: i  reason: collision with root package name */
    c0 f731i;

    /* renamed from: l  reason: collision with root package name */
    private boolean f734l;

    /* renamed from: m  reason: collision with root package name */
    d f735m;

    /* renamed from: n  reason: collision with root package name */
    androidx.appcompat.view.b f736n;

    /* renamed from: o  reason: collision with root package name */
    b.a f737o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f738p;

    /* renamed from: r  reason: collision with root package name */
    private boolean f739r;

    /* renamed from: u  reason: collision with root package name */
    boolean f742u;

    /* renamed from: v  reason: collision with root package name */
    boolean f743v;

    /* renamed from: w  reason: collision with root package name */
    private boolean f744w;

    /* renamed from: y  reason: collision with root package name */
    androidx.appcompat.view.h f746y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f747z;

    /* renamed from: j  reason: collision with root package name */
    private ArrayList<Object> f732j = new ArrayList<>();

    /* renamed from: k  reason: collision with root package name */
    private int f733k = -1;
    private ArrayList<ActionBar.a> q = new ArrayList<>();

    /* renamed from: s  reason: collision with root package name */
    private int f740s = 0;

    /* renamed from: t  reason: collision with root package name */
    boolean f741t = true;

    /* renamed from: x  reason: collision with root package name */
    private boolean f745x = true;
    final j0 B = new a();
    final j0 C = new b();
    final l0 D = new c();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends k0 {
        a() {
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            View view2;
            t tVar = t.this;
            if (tVar.f741t && (view2 = tVar.f730h) != null) {
                view2.setTranslationY(0.0f);
                t.this.f727e.setTranslationY(0.0f);
            }
            t.this.f727e.setVisibility(8);
            t.this.f727e.setTransitioning(false);
            t tVar2 = t.this;
            tVar2.f746y = null;
            tVar2.C();
            ActionBarOverlayLayout actionBarOverlayLayout = t.this.f726d;
            if (actionBarOverlayLayout != null) {
                androidx.core.view.c0.q0(actionBarOverlayLayout);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends k0 {
        b() {
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            t tVar = t.this;
            tVar.f746y = null;
            tVar.f727e.requestLayout();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements l0 {
        c() {
        }

        @Override // androidx.core.view.l0
        public void a(View view) {
            ((View) t.this.f727e.getParent()).invalidate();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends androidx.appcompat.view.b implements g.a {

        /* renamed from: c  reason: collision with root package name */
        private final Context f751c;

        /* renamed from: d  reason: collision with root package name */
        private final androidx.appcompat.view.menu.g f752d;

        /* renamed from: e  reason: collision with root package name */
        private b.a f753e;

        /* renamed from: f  reason: collision with root package name */
        private WeakReference<View> f754f;

        public d(Context context, b.a aVar) {
            this.f751c = context;
            this.f753e = aVar;
            androidx.appcompat.view.menu.g W = new androidx.appcompat.view.menu.g(context).W(1);
            this.f752d = W;
            W.V(this);
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
            b.a aVar = this.f753e;
            if (aVar != null) {
                return aVar.d(this, menuItem);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(androidx.appcompat.view.menu.g gVar) {
            if (this.f753e == null) {
                return;
            }
            k();
            t.this.f729g.l();
        }

        @Override // androidx.appcompat.view.b
        public void c() {
            t tVar = t.this;
            if (tVar.f735m != this) {
                return;
            }
            if (t.B(tVar.f742u, tVar.f743v, false)) {
                this.f753e.a(this);
            } else {
                t tVar2 = t.this;
                tVar2.f736n = this;
                tVar2.f737o = this.f753e;
            }
            this.f753e = null;
            t.this.A(false);
            t.this.f729g.g();
            t tVar3 = t.this;
            tVar3.f726d.setHideOnContentScrollEnabled(tVar3.A);
            t.this.f735m = null;
        }

        @Override // androidx.appcompat.view.b
        public View d() {
            WeakReference<View> weakReference = this.f754f;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        @Override // androidx.appcompat.view.b
        public Menu e() {
            return this.f752d;
        }

        @Override // androidx.appcompat.view.b
        public MenuInflater f() {
            return new androidx.appcompat.view.g(this.f751c);
        }

        @Override // androidx.appcompat.view.b
        public CharSequence g() {
            return t.this.f729g.getSubtitle();
        }

        @Override // androidx.appcompat.view.b
        public CharSequence i() {
            return t.this.f729g.getTitle();
        }

        @Override // androidx.appcompat.view.b
        public void k() {
            if (t.this.f735m != this) {
                return;
            }
            this.f752d.h0();
            try {
                this.f753e.c(this, this.f752d);
            } finally {
                this.f752d.g0();
            }
        }

        @Override // androidx.appcompat.view.b
        public boolean l() {
            return t.this.f729g.j();
        }

        @Override // androidx.appcompat.view.b
        public void m(View view) {
            t.this.f729g.setCustomView(view);
            this.f754f = new WeakReference<>(view);
        }

        @Override // androidx.appcompat.view.b
        public void n(int i8) {
            o(t.this.f723a.getResources().getString(i8));
        }

        @Override // androidx.appcompat.view.b
        public void o(CharSequence charSequence) {
            t.this.f729g.setSubtitle(charSequence);
        }

        @Override // androidx.appcompat.view.b
        public void q(int i8) {
            r(t.this.f723a.getResources().getString(i8));
        }

        @Override // androidx.appcompat.view.b
        public void r(CharSequence charSequence) {
            t.this.f729g.setTitle(charSequence);
        }

        @Override // androidx.appcompat.view.b
        public void s(boolean z4) {
            super.s(z4);
            t.this.f729g.setTitleOptional(z4);
        }

        public boolean t() {
            this.f752d.h0();
            try {
                return this.f753e.b(this, this.f752d);
            } finally {
                this.f752d.g0();
            }
        }
    }

    public t(Activity activity, boolean z4) {
        this.f725c = activity;
        View decorView = activity.getWindow().getDecorView();
        I(decorView);
        if (z4) {
            return;
        }
        this.f730h = decorView.findViewById(16908290);
    }

    public t(Dialog dialog) {
        I(dialog.getWindow().getDecorView());
    }

    static boolean B(boolean z4, boolean z8, boolean z9) {
        if (z9) {
            return true;
        }
        return (z4 || z8) ? false : true;
    }

    private androidx.appcompat.widget.s F(View view) {
        if (view instanceof androidx.appcompat.widget.s) {
            return (androidx.appcompat.widget.s) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Can't make a decor toolbar out of ");
        sb.append(view != null ? view.getClass().getSimpleName() : "null");
        throw new IllegalStateException(sb.toString());
    }

    private void H() {
        if (this.f744w) {
            this.f744w = false;
            ActionBarOverlayLayout actionBarOverlayLayout = this.f726d;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.setShowingForActionMode(false);
            }
            Q(false);
        }
    }

    private void I(View view) {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) view.findViewById(g.f.q);
        this.f726d = actionBarOverlayLayout;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.f728f = F(view.findViewById(g.f.f19936a));
        this.f729g = (ActionBarContextView) view.findViewById(g.f.f19941f);
        ActionBarContainer actionBarContainer = (ActionBarContainer) view.findViewById(g.f.f19938c);
        this.f727e = actionBarContainer;
        androidx.appcompat.widget.s sVar = this.f728f;
        if (sVar == null || this.f729g == null || actionBarContainer == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
        }
        this.f723a = sVar.getContext();
        boolean z4 = (this.f728f.s() & 4) != 0;
        if (z4) {
            this.f734l = true;
        }
        androidx.appcompat.view.a b9 = androidx.appcompat.view.a.b(this.f723a);
        N(b9.a() || z4);
        L(b9.g());
        TypedArray obtainStyledAttributes = this.f723a.obtainStyledAttributes(null, g.j.f20003a, g.a.f19864c, 0);
        if (obtainStyledAttributes.getBoolean(g.j.f20057k, false)) {
            M(true);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(g.j.f20047i, 0);
        if (dimensionPixelSize != 0) {
            K(dimensionPixelSize);
        }
        obtainStyledAttributes.recycle();
    }

    private void L(boolean z4) {
        this.f739r = z4;
        if (z4) {
            this.f727e.setTabContainer(null);
            this.f728f.i(this.f731i);
        } else {
            this.f728f.i(null);
            this.f727e.setTabContainer(this.f731i);
        }
        boolean z8 = true;
        boolean z9 = G() == 2;
        c0 c0Var = this.f731i;
        if (c0Var != null) {
            if (z9) {
                c0Var.setVisibility(0);
                ActionBarOverlayLayout actionBarOverlayLayout = this.f726d;
                if (actionBarOverlayLayout != null) {
                    androidx.core.view.c0.q0(actionBarOverlayLayout);
                }
            } else {
                c0Var.setVisibility(8);
            }
        }
        this.f728f.x(!this.f739r && z9);
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.f726d;
        if (this.f739r || !z9) {
            z8 = false;
        }
        actionBarOverlayLayout2.setHasNonEmbeddedTabs(z8);
    }

    private boolean O() {
        return androidx.core.view.c0.W(this.f727e);
    }

    private void P() {
        if (this.f744w) {
            return;
        }
        this.f744w = true;
        ActionBarOverlayLayout actionBarOverlayLayout = this.f726d;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setShowingForActionMode(true);
        }
        Q(false);
    }

    private void Q(boolean z4) {
        if (B(this.f742u, this.f743v, this.f744w)) {
            if (this.f745x) {
                return;
            }
            this.f745x = true;
            E(z4);
        } else if (this.f745x) {
            this.f745x = false;
            D(z4);
        }
    }

    public void A(boolean z4) {
        i0 o5;
        i0 f5;
        if (z4) {
            P();
        } else {
            H();
        }
        if (!O()) {
            if (z4) {
                this.f728f.setVisibility(4);
                this.f729g.setVisibility(0);
                return;
            }
            this.f728f.setVisibility(0);
            this.f729g.setVisibility(8);
            return;
        }
        if (z4) {
            f5 = this.f728f.o(4, 100L);
            o5 = this.f729g.f(0, 200L);
        } else {
            o5 = this.f728f.o(0, 200L);
            f5 = this.f729g.f(8, 100L);
        }
        androidx.appcompat.view.h hVar = new androidx.appcompat.view.h();
        hVar.d(f5, o5);
        hVar.h();
    }

    void C() {
        b.a aVar = this.f737o;
        if (aVar != null) {
            aVar.a(this.f736n);
            this.f736n = null;
            this.f737o = null;
        }
    }

    public void D(boolean z4) {
        View view;
        int[] iArr;
        androidx.appcompat.view.h hVar = this.f746y;
        if (hVar != null) {
            hVar.a();
        }
        if (this.f740s != 0 || (!this.f747z && !z4)) {
            this.B.b(null);
            return;
        }
        this.f727e.setAlpha(1.0f);
        this.f727e.setTransitioning(true);
        androidx.appcompat.view.h hVar2 = new androidx.appcompat.view.h();
        float f5 = -this.f727e.getHeight();
        if (z4) {
            this.f727e.getLocationInWindow(new int[]{0, 0});
            f5 -= iArr[1];
        }
        i0 m8 = androidx.core.view.c0.e(this.f727e).m(f5);
        m8.k(this.D);
        hVar2.c(m8);
        if (this.f741t && (view = this.f730h) != null) {
            hVar2.c(androidx.core.view.c0.e(view).m(f5));
        }
        hVar2.f(E);
        hVar2.e(250L);
        hVar2.g(this.B);
        this.f746y = hVar2;
        hVar2.h();
    }

    public void E(boolean z4) {
        View view;
        View view2;
        int[] iArr;
        androidx.appcompat.view.h hVar = this.f746y;
        if (hVar != null) {
            hVar.a();
        }
        this.f727e.setVisibility(0);
        if (this.f740s == 0 && (this.f747z || z4)) {
            this.f727e.setTranslationY(0.0f);
            float f5 = -this.f727e.getHeight();
            if (z4) {
                this.f727e.getLocationInWindow(new int[]{0, 0});
                f5 -= iArr[1];
            }
            this.f727e.setTranslationY(f5);
            androidx.appcompat.view.h hVar2 = new androidx.appcompat.view.h();
            i0 m8 = androidx.core.view.c0.e(this.f727e).m(0.0f);
            m8.k(this.D);
            hVar2.c(m8);
            if (this.f741t && (view2 = this.f730h) != null) {
                view2.setTranslationY(f5);
                hVar2.c(androidx.core.view.c0.e(this.f730h).m(0.0f));
            }
            hVar2.f(F);
            hVar2.e(250L);
            hVar2.g(this.C);
            this.f746y = hVar2;
            hVar2.h();
        } else {
            this.f727e.setAlpha(1.0f);
            this.f727e.setTranslationY(0.0f);
            if (this.f741t && (view = this.f730h) != null) {
                view.setTranslationY(0.0f);
            }
            this.C.b(null);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.f726d;
        if (actionBarOverlayLayout != null) {
            androidx.core.view.c0.q0(actionBarOverlayLayout);
        }
    }

    public int G() {
        return this.f728f.n();
    }

    public void J(int i8, int i9) {
        int s8 = this.f728f.s();
        if ((i9 & 4) != 0) {
            this.f734l = true;
        }
        this.f728f.k((i8 & i9) | ((~i9) & s8));
    }

    public void K(float f5) {
        androidx.core.view.c0.B0(this.f727e, f5);
    }

    public void M(boolean z4) {
        if (z4 && !this.f726d.w()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.A = z4;
        this.f726d.setHideOnContentScrollEnabled(z4);
    }

    public void N(boolean z4) {
        this.f728f.r(z4);
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void a() {
        if (this.f743v) {
            this.f743v = false;
            Q(true);
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void b() {
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void c(boolean z4) {
        this.f741t = z4;
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void d() {
        if (this.f743v) {
            return;
        }
        this.f743v = true;
        Q(true);
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void e() {
        androidx.appcompat.view.h hVar = this.f746y;
        if (hVar != null) {
            hVar.a();
            this.f746y = null;
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void f(int i8) {
        this.f740s = i8;
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean h() {
        androidx.appcompat.widget.s sVar = this.f728f;
        if (sVar == null || !sVar.j()) {
            return false;
        }
        this.f728f.collapseActionView();
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void i(boolean z4) {
        if (z4 == this.f738p) {
            return;
        }
        this.f738p = z4;
        int size = this.q.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.q.get(i8).a(z4);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public int j() {
        return this.f728f.s();
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context k() {
        if (this.f724b == null) {
            TypedValue typedValue = new TypedValue();
            this.f723a.getTheme().resolveAttribute(g.a.f19868g, typedValue, true);
            int i8 = typedValue.resourceId;
            if (i8 != 0) {
                this.f724b = new ContextThemeWrapper(this.f723a, i8);
            } else {
                this.f724b = this.f723a;
            }
        }
        return this.f724b;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void m(Configuration configuration) {
        L(androidx.appcompat.view.a.b(this.f723a).g());
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean o(int i8, KeyEvent keyEvent) {
        Menu e8;
        d dVar = this.f735m;
        if (dVar == null || (e8 = dVar.e()) == null) {
            return false;
        }
        e8.setQwertyMode(KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1);
        return e8.performShortcut(i8, keyEvent, 0);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void r(boolean z4) {
        if (this.f734l) {
            return;
        }
        s(z4);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void s(boolean z4) {
        J(z4 ? 4 : 0, 4);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void t(boolean z4) {
        J(z4 ? 8 : 0, 8);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void u(int i8) {
        this.f728f.t(i8);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void v(Drawable drawable) {
        this.f728f.w(drawable);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void w(boolean z4) {
        androidx.appcompat.view.h hVar;
        this.f747z = z4;
        if (z4 || (hVar = this.f746y) == null) {
            return;
        }
        hVar.a();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void x(CharSequence charSequence) {
        this.f728f.setTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void y(CharSequence charSequence) {
        this.f728f.setWindowTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public androidx.appcompat.view.b z(b.a aVar) {
        d dVar = this.f735m;
        if (dVar != null) {
            dVar.c();
        }
        this.f726d.setHideOnContentScrollEnabled(false);
        this.f729g.k();
        d dVar2 = new d(this.f729g.getContext(), aVar);
        if (dVar2.t()) {
            this.f735m = dVar2;
            dVar2.k();
            this.f729g.h(dVar2);
            A(true);
            return dVar2;
        }
        return null;
    }
}
