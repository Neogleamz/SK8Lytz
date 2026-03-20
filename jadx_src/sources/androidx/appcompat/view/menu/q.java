package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.y;
import androidx.core.view.c0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q extends k implements PopupWindow.OnDismissListener, View.OnKeyListener {
    private static final int B = g.g.f19975o;
    private boolean A;

    /* renamed from: b  reason: collision with root package name */
    private final Context f1011b;

    /* renamed from: c  reason: collision with root package name */
    private final g f1012c;

    /* renamed from: d  reason: collision with root package name */
    private final f f1013d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f1014e;

    /* renamed from: f  reason: collision with root package name */
    private final int f1015f;

    /* renamed from: g  reason: collision with root package name */
    private final int f1016g;

    /* renamed from: h  reason: collision with root package name */
    private final int f1017h;

    /* renamed from: j  reason: collision with root package name */
    final y f1018j;

    /* renamed from: m  reason: collision with root package name */
    private PopupWindow.OnDismissListener f1021m;

    /* renamed from: n  reason: collision with root package name */
    private View f1022n;

    /* renamed from: p  reason: collision with root package name */
    View f1023p;
    private m.a q;

    /* renamed from: t  reason: collision with root package name */
    ViewTreeObserver f1024t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f1025w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f1026x;

    /* renamed from: y  reason: collision with root package name */
    private int f1027y;

    /* renamed from: k  reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f1019k = new a();

    /* renamed from: l  reason: collision with root package name */
    private final View.OnAttachStateChangeListener f1020l = new b();

    /* renamed from: z  reason: collision with root package name */
    private int f1028z = 0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!q.this.b() || q.this.f1018j.B()) {
                return;
            }
            View view = q.this.f1023p;
            if (view == null || !view.isShown()) {
                q.this.dismiss();
            } else {
                q.this.f1018j.a();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements View.OnAttachStateChangeListener {
        b() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = q.this.f1024t;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    q.this.f1024t = view.getViewTreeObserver();
                }
                q qVar = q.this;
                qVar.f1024t.removeGlobalOnLayoutListener(qVar.f1019k);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    public q(Context context, g gVar, View view, int i8, int i9, boolean z4) {
        this.f1011b = context;
        this.f1012c = gVar;
        this.f1014e = z4;
        this.f1013d = new f(gVar, LayoutInflater.from(context), z4, B);
        this.f1016g = i8;
        this.f1017h = i9;
        Resources resources = context.getResources();
        this.f1015f = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(g.d.f19899d));
        this.f1022n = view;
        this.f1018j = new y(context, null, i8, i9);
        gVar.c(this, context);
    }

    private boolean C() {
        View view;
        if (b()) {
            return true;
        }
        if (this.f1025w || (view = this.f1022n) == null) {
            return false;
        }
        this.f1023p = view;
        this.f1018j.K(this);
        this.f1018j.L(this);
        this.f1018j.J(true);
        View view2 = this.f1023p;
        boolean z4 = this.f1024t == null;
        ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
        this.f1024t = viewTreeObserver;
        if (z4) {
            viewTreeObserver.addOnGlobalLayoutListener(this.f1019k);
        }
        view2.addOnAttachStateChangeListener(this.f1020l);
        this.f1018j.D(view2);
        this.f1018j.G(this.f1028z);
        if (!this.f1026x) {
            this.f1027y = k.r(this.f1013d, null, this.f1011b, this.f1015f);
            this.f1026x = true;
        }
        this.f1018j.F(this.f1027y);
        this.f1018j.I(2);
        this.f1018j.H(q());
        this.f1018j.a();
        ListView m8 = this.f1018j.m();
        m8.setOnKeyListener(this);
        if (this.A && this.f1012c.z() != null) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.f1011b).inflate(g.g.f19974n, (ViewGroup) m8, false);
            TextView textView = (TextView) frameLayout.findViewById(16908310);
            if (textView != null) {
                textView.setText(this.f1012c.z());
            }
            frameLayout.setEnabled(false);
            m8.addHeaderView(frameLayout, null, false);
        }
        this.f1018j.p(this.f1013d);
        this.f1018j.a();
        return true;
    }

    @Override // androidx.appcompat.view.menu.p
    public void a() {
        if (!C()) {
            throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @Override // androidx.appcompat.view.menu.p
    public boolean b() {
        return !this.f1025w && this.f1018j.b();
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(g gVar, boolean z4) {
        if (gVar != this.f1012c) {
            return;
        }
        dismiss();
        m.a aVar = this.q;
        if (aVar != null) {
            aVar.c(gVar, z4);
        }
    }

    @Override // androidx.appcompat.view.menu.k
    public void d(g gVar) {
    }

    @Override // androidx.appcompat.view.menu.p
    public void dismiss() {
        if (b()) {
            this.f1018j.dismiss();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        this.f1026x = false;
        f fVar = this.f1013d;
        if (fVar != null) {
            fVar.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public void j(m.a aVar) {
        this.q = aVar;
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
    }

    @Override // androidx.appcompat.view.menu.p
    public ListView m() {
        return this.f1018j.m();
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean n(r rVar) {
        if (rVar.hasVisibleItems()) {
            l lVar = new l(this.f1011b, rVar, this.f1023p, this.f1014e, this.f1016g, this.f1017h);
            lVar.j(this.q);
            lVar.g(k.A(rVar));
            lVar.i(this.f1021m);
            this.f1021m = null;
            this.f1012c.e(false);
            int d8 = this.f1018j.d();
            int o5 = this.f1018j.o();
            if ((Gravity.getAbsoluteGravity(this.f1028z, c0.E(this.f1022n)) & 7) == 5) {
                d8 += this.f1022n.getWidth();
            }
            if (lVar.n(d8, o5)) {
                m.a aVar = this.q;
                if (aVar != null) {
                    aVar.d(rVar);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        return null;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        this.f1025w = true;
        this.f1012c.close();
        ViewTreeObserver viewTreeObserver = this.f1024t;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.f1024t = this.f1023p.getViewTreeObserver();
            }
            this.f1024t.removeGlobalOnLayoutListener(this.f1019k);
            this.f1024t = null;
        }
        this.f1023p.removeOnAttachStateChangeListener(this.f1020l);
        PopupWindow.OnDismissListener onDismissListener = this.f1021m;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i8, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && i8 == 82) {
            dismiss();
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.k
    public void s(View view) {
        this.f1022n = view;
    }

    @Override // androidx.appcompat.view.menu.k
    public void u(boolean z4) {
        this.f1013d.d(z4);
    }

    @Override // androidx.appcompat.view.menu.k
    public void v(int i8) {
        this.f1028z = i8;
    }

    @Override // androidx.appcompat.view.menu.k
    public void w(int i8) {
        this.f1018j.f(i8);
    }

    @Override // androidx.appcompat.view.menu.k
    public void x(PopupWindow.OnDismissListener onDismissListener) {
        this.f1021m = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.k
    public void y(boolean z4) {
        this.A = z4;
    }

    @Override // androidx.appcompat.view.menu.k
    public void z(int i8) {
        this.f1018j.k(i8);
    }
}
