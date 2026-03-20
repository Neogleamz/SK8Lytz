package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.Toolbar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n0 implements s {

    /* renamed from: a  reason: collision with root package name */
    Toolbar f1525a;

    /* renamed from: b  reason: collision with root package name */
    private int f1526b;

    /* renamed from: c  reason: collision with root package name */
    private View f1527c;

    /* renamed from: d  reason: collision with root package name */
    private View f1528d;

    /* renamed from: e  reason: collision with root package name */
    private Drawable f1529e;

    /* renamed from: f  reason: collision with root package name */
    private Drawable f1530f;

    /* renamed from: g  reason: collision with root package name */
    private Drawable f1531g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1532h;

    /* renamed from: i  reason: collision with root package name */
    CharSequence f1533i;

    /* renamed from: j  reason: collision with root package name */
    private CharSequence f1534j;

    /* renamed from: k  reason: collision with root package name */
    private CharSequence f1535k;

    /* renamed from: l  reason: collision with root package name */
    Window.Callback f1536l;

    /* renamed from: m  reason: collision with root package name */
    boolean f1537m;

    /* renamed from: n  reason: collision with root package name */
    private ActionMenuPresenter f1538n;

    /* renamed from: o  reason: collision with root package name */
    private int f1539o;

    /* renamed from: p  reason: collision with root package name */
    private int f1540p;
    private Drawable q;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final androidx.appcompat.view.menu.a f1541a;

        a() {
            this.f1541a = new androidx.appcompat.view.menu.a(n0.this.f1525a.getContext(), 0, 16908332, 0, 0, n0.this.f1533i);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            n0 n0Var = n0.this;
            Window.Callback callback = n0Var.f1536l;
            if (callback == null || !n0Var.f1537m) {
                return;
            }
            callback.onMenuItemSelected(0, this.f1541a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends androidx.core.view.k0 {

        /* renamed from: a  reason: collision with root package name */
        private boolean f1543a = false;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f1544b;

        b(int i8) {
            this.f1544b = i8;
        }

        @Override // androidx.core.view.k0, androidx.core.view.j0
        public void a(View view) {
            this.f1543a = true;
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            if (this.f1543a) {
                return;
            }
            n0.this.f1525a.setVisibility(this.f1544b);
        }

        @Override // androidx.core.view.k0, androidx.core.view.j0
        public void c(View view) {
            n0.this.f1525a.setVisibility(0);
        }
    }

    public n0(Toolbar toolbar, boolean z4) {
        this(toolbar, z4, g.h.f19982a, g.e.f19924n);
    }

    public n0(Toolbar toolbar, boolean z4, int i8, int i9) {
        Drawable drawable;
        this.f1539o = 0;
        this.f1540p = 0;
        this.f1525a = toolbar;
        this.f1533i = toolbar.getTitle();
        this.f1534j = toolbar.getSubtitle();
        this.f1532h = this.f1533i != null;
        this.f1531g = toolbar.getNavigationIcon();
        j0 v8 = j0.v(toolbar.getContext(), null, g.j.f20003a, g.a.f19864c, 0);
        this.q = v8.g(g.j.f20062l);
        if (z4) {
            CharSequence p8 = v8.p(g.j.f20091r);
            if (!TextUtils.isEmpty(p8)) {
                setTitle(p8);
            }
            CharSequence p9 = v8.p(g.j.f20082p);
            if (!TextUtils.isEmpty(p9)) {
                D(p9);
            }
            Drawable g8 = v8.g(g.j.f20072n);
            if (g8 != null) {
                B(g8);
            }
            Drawable g9 = v8.g(g.j.f20067m);
            if (g9 != null) {
                setIcon(g9);
            }
            if (this.f1531g == null && (drawable = this.q) != null) {
                w(drawable);
            }
            k(v8.k(g.j.f20042h, 0));
            int n8 = v8.n(g.j.f20037g, 0);
            if (n8 != 0) {
                z(LayoutInflater.from(this.f1525a.getContext()).inflate(n8, (ViewGroup) this.f1525a, false));
                k(this.f1526b | 16);
            }
            int m8 = v8.m(g.j.f20052j, 0);
            if (m8 > 0) {
                ViewGroup.LayoutParams layoutParams = this.f1525a.getLayoutParams();
                layoutParams.height = m8;
                this.f1525a.setLayoutParams(layoutParams);
            }
            int e8 = v8.e(g.j.f20032f, -1);
            int e9 = v8.e(g.j.f20027e, -1);
            if (e8 >= 0 || e9 >= 0) {
                this.f1525a.J(Math.max(e8, 0), Math.max(e9, 0));
            }
            int n9 = v8.n(g.j.f20096s, 0);
            if (n9 != 0) {
                Toolbar toolbar2 = this.f1525a;
                toolbar2.N(toolbar2.getContext(), n9);
            }
            int n10 = v8.n(g.j.q, 0);
            if (n10 != 0) {
                Toolbar toolbar3 = this.f1525a;
                toolbar3.M(toolbar3.getContext(), n10);
            }
            int n11 = v8.n(g.j.f20077o, 0);
            if (n11 != 0) {
                this.f1525a.setPopupTheme(n11);
            }
        } else {
            this.f1526b = y();
        }
        v8.w();
        A(i8);
        this.f1535k = this.f1525a.getNavigationContentDescription();
        this.f1525a.setNavigationOnClickListener(new a());
    }

    private void E(CharSequence charSequence) {
        this.f1533i = charSequence;
        if ((this.f1526b & 8) != 0) {
            this.f1525a.setTitle(charSequence);
            if (this.f1532h) {
                androidx.core.view.c0.w0(this.f1525a.getRootView(), charSequence);
            }
        }
    }

    private void F() {
        if ((this.f1526b & 4) != 0) {
            if (TextUtils.isEmpty(this.f1535k)) {
                this.f1525a.setNavigationContentDescription(this.f1540p);
            } else {
                this.f1525a.setNavigationContentDescription(this.f1535k);
            }
        }
    }

    private void G() {
        Toolbar toolbar;
        Drawable drawable;
        if ((this.f1526b & 4) != 0) {
            toolbar = this.f1525a;
            drawable = this.f1531g;
            if (drawable == null) {
                drawable = this.q;
            }
        } else {
            toolbar = this.f1525a;
            drawable = null;
        }
        toolbar.setNavigationIcon(drawable);
    }

    private void H() {
        Drawable drawable;
        int i8 = this.f1526b;
        if ((i8 & 2) == 0) {
            drawable = null;
        } else if ((i8 & 1) == 0 || (drawable = this.f1530f) == null) {
            drawable = this.f1529e;
        }
        this.f1525a.setLogo(drawable);
    }

    private int y() {
        if (this.f1525a.getNavigationIcon() != null) {
            this.q = this.f1525a.getNavigationIcon();
            return 15;
        }
        return 11;
    }

    public void A(int i8) {
        if (i8 == this.f1540p) {
            return;
        }
        this.f1540p = i8;
        if (TextUtils.isEmpty(this.f1525a.getNavigationContentDescription())) {
            t(this.f1540p);
        }
    }

    public void B(Drawable drawable) {
        this.f1530f = drawable;
        H();
    }

    public void C(CharSequence charSequence) {
        this.f1535k = charSequence;
        F();
    }

    public void D(CharSequence charSequence) {
        this.f1534j = charSequence;
        if ((this.f1526b & 8) != 0) {
            this.f1525a.setSubtitle(charSequence);
        }
    }

    @Override // androidx.appcompat.widget.s
    public void a(Menu menu, m.a aVar) {
        if (this.f1538n == null) {
            ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(this.f1525a.getContext());
            this.f1538n = actionMenuPresenter;
            actionMenuPresenter.t(g.f.f19942g);
        }
        this.f1538n.j(aVar);
        this.f1525a.K((androidx.appcompat.view.menu.g) menu, this.f1538n);
    }

    @Override // androidx.appcompat.widget.s
    public boolean b() {
        return this.f1525a.B();
    }

    @Override // androidx.appcompat.widget.s
    public void c() {
        this.f1537m = true;
    }

    @Override // androidx.appcompat.widget.s
    public void collapseActionView() {
        this.f1525a.e();
    }

    @Override // androidx.appcompat.widget.s
    public boolean d() {
        return this.f1525a.d();
    }

    @Override // androidx.appcompat.widget.s
    public boolean e() {
        return this.f1525a.A();
    }

    @Override // androidx.appcompat.widget.s
    public boolean f() {
        return this.f1525a.w();
    }

    @Override // androidx.appcompat.widget.s
    public boolean g() {
        return this.f1525a.Q();
    }

    @Override // androidx.appcompat.widget.s
    public Context getContext() {
        return this.f1525a.getContext();
    }

    @Override // androidx.appcompat.widget.s
    public CharSequence getTitle() {
        return this.f1525a.getTitle();
    }

    @Override // androidx.appcompat.widget.s
    public void h() {
        this.f1525a.f();
    }

    @Override // androidx.appcompat.widget.s
    public void i(c0 c0Var) {
        View view = this.f1527c;
        if (view != null) {
            ViewParent parent = view.getParent();
            Toolbar toolbar = this.f1525a;
            if (parent == toolbar) {
                toolbar.removeView(this.f1527c);
            }
        }
        this.f1527c = c0Var;
        if (c0Var == null || this.f1539o != 2) {
            return;
        }
        this.f1525a.addView(c0Var, 0);
        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) this.f1527c.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
        ((ViewGroup.MarginLayoutParams) layoutParams).height = -2;
        layoutParams.f469a = 8388691;
        c0Var.setAllowCollapse(true);
    }

    @Override // androidx.appcompat.widget.s
    public boolean j() {
        return this.f1525a.v();
    }

    @Override // androidx.appcompat.widget.s
    public void k(int i8) {
        View view;
        CharSequence charSequence;
        Toolbar toolbar;
        int i9 = this.f1526b ^ i8;
        this.f1526b = i8;
        if (i9 != 0) {
            if ((i9 & 4) != 0) {
                if ((i8 & 4) != 0) {
                    F();
                }
                G();
            }
            if ((i9 & 3) != 0) {
                H();
            }
            if ((i9 & 8) != 0) {
                if ((i8 & 8) != 0) {
                    this.f1525a.setTitle(this.f1533i);
                    toolbar = this.f1525a;
                    charSequence = this.f1534j;
                } else {
                    charSequence = null;
                    this.f1525a.setTitle((CharSequence) null);
                    toolbar = this.f1525a;
                }
                toolbar.setSubtitle(charSequence);
            }
            if ((i9 & 16) == 0 || (view = this.f1528d) == null) {
                return;
            }
            if ((i8 & 16) != 0) {
                this.f1525a.addView(view);
            } else {
                this.f1525a.removeView(view);
            }
        }
    }

    @Override // androidx.appcompat.widget.s
    public Menu l() {
        return this.f1525a.getMenu();
    }

    @Override // androidx.appcompat.widget.s
    public void m(int i8) {
        B(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    @Override // androidx.appcompat.widget.s
    public int n() {
        return this.f1539o;
    }

    @Override // androidx.appcompat.widget.s
    public androidx.core.view.i0 o(int i8, long j8) {
        return androidx.core.view.c0.e(this.f1525a).b(i8 == 0 ? 1.0f : 0.0f).f(j8).h(new b(i8));
    }

    @Override // androidx.appcompat.widget.s
    public void p(m.a aVar, g.a aVar2) {
        this.f1525a.L(aVar, aVar2);
    }

    @Override // androidx.appcompat.widget.s
    public ViewGroup q() {
        return this.f1525a;
    }

    @Override // androidx.appcompat.widget.s
    public void r(boolean z4) {
    }

    @Override // androidx.appcompat.widget.s
    public int s() {
        return this.f1526b;
    }

    @Override // androidx.appcompat.widget.s
    public void setIcon(int i8) {
        setIcon(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    @Override // androidx.appcompat.widget.s
    public void setIcon(Drawable drawable) {
        this.f1529e = drawable;
        H();
    }

    @Override // androidx.appcompat.widget.s
    public void setTitle(CharSequence charSequence) {
        this.f1532h = true;
        E(charSequence);
    }

    @Override // androidx.appcompat.widget.s
    public void setVisibility(int i8) {
        this.f1525a.setVisibility(i8);
    }

    @Override // androidx.appcompat.widget.s
    public void setWindowCallback(Window.Callback callback) {
        this.f1536l = callback;
    }

    @Override // androidx.appcompat.widget.s
    public void setWindowTitle(CharSequence charSequence) {
        if (this.f1532h) {
            return;
        }
        E(charSequence);
    }

    @Override // androidx.appcompat.widget.s
    public void t(int i8) {
        C(i8 == 0 ? null : getContext().getString(i8));
    }

    @Override // androidx.appcompat.widget.s
    public void u() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.s
    public void v() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.s
    public void w(Drawable drawable) {
        this.f1531g = drawable;
        G();
    }

    @Override // androidx.appcompat.widget.s
    public void x(boolean z4) {
        this.f1525a.setCollapsible(z4);
    }

    public void z(View view) {
        View view2 = this.f1528d;
        if (view2 != null && (this.f1526b & 16) != 0) {
            this.f1525a.removeView(view2);
        }
        this.f1528d = view;
        if (view == null || (this.f1526b & 16) == 0) {
            return;
        }
        this.f1525a.addView(view);
    }
}
