package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.x;
import androidx.appcompat.widget.y;
import androidx.core.view.c0;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends k implements View.OnKeyListener, PopupWindow.OnDismissListener {
    private static final int K = g.g.f19967g;
    private int A;
    private boolean C;
    private m.a E;
    ViewTreeObserver F;
    private PopupWindow.OnDismissListener G;
    boolean H;

    /* renamed from: b  reason: collision with root package name */
    private final Context f884b;

    /* renamed from: c  reason: collision with root package name */
    private final int f885c;

    /* renamed from: d  reason: collision with root package name */
    private final int f886d;

    /* renamed from: e  reason: collision with root package name */
    private final int f887e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f888f;

    /* renamed from: g  reason: collision with root package name */
    final Handler f889g;
    private View q;

    /* renamed from: t  reason: collision with root package name */
    View f897t;

    /* renamed from: x  reason: collision with root package name */
    private boolean f899x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f900y;

    /* renamed from: z  reason: collision with root package name */
    private int f901z;

    /* renamed from: h  reason: collision with root package name */
    private final List<g> f890h = new ArrayList();

    /* renamed from: j  reason: collision with root package name */
    final List<C0012d> f891j = new ArrayList();

    /* renamed from: k  reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f892k = new a();

    /* renamed from: l  reason: collision with root package name */
    private final View.OnAttachStateChangeListener f893l = new b();

    /* renamed from: m  reason: collision with root package name */
    private final x f894m = new c();

    /* renamed from: n  reason: collision with root package name */
    private int f895n = 0;

    /* renamed from: p  reason: collision with root package name */
    private int f896p = 0;
    private boolean B = false;

    /* renamed from: w  reason: collision with root package name */
    private int f898w = G();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!d.this.b() || d.this.f891j.size() <= 0 || d.this.f891j.get(0).f909a.B()) {
                return;
            }
            View view = d.this.f897t;
            if (view == null || !view.isShown()) {
                d.this.dismiss();
                return;
            }
            for (C0012d c0012d : d.this.f891j) {
                c0012d.f909a.a();
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
            ViewTreeObserver viewTreeObserver = d.this.F;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    d.this.F = view.getViewTreeObserver();
                }
                d dVar = d.this;
                dVar.F.removeGlobalOnLayoutListener(dVar.f892k);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements x {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ C0012d f905a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ MenuItem f906b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ g f907c;

            a(C0012d c0012d, MenuItem menuItem, g gVar) {
                this.f905a = c0012d;
                this.f906b = menuItem;
                this.f907c = gVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                C0012d c0012d = this.f905a;
                if (c0012d != null) {
                    d.this.H = true;
                    c0012d.f910b.e(false);
                    d.this.H = false;
                }
                if (this.f906b.isEnabled() && this.f906b.hasSubMenu()) {
                    this.f907c.N(this.f906b, 4);
                }
            }
        }

        c() {
        }

        @Override // androidx.appcompat.widget.x
        public void e(g gVar, MenuItem menuItem) {
            d.this.f889g.removeCallbacksAndMessages(null);
            int size = d.this.f891j.size();
            int i8 = 0;
            while (true) {
                if (i8 >= size) {
                    i8 = -1;
                    break;
                } else if (gVar == d.this.f891j.get(i8).f910b) {
                    break;
                } else {
                    i8++;
                }
            }
            if (i8 == -1) {
                return;
            }
            int i9 = i8 + 1;
            d.this.f889g.postAtTime(new a(i9 < d.this.f891j.size() ? d.this.f891j.get(i9) : null, menuItem, gVar), gVar, SystemClock.uptimeMillis() + 200);
        }

        @Override // androidx.appcompat.widget.x
        public void h(g gVar, MenuItem menuItem) {
            d.this.f889g.removeCallbacksAndMessages(gVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: androidx.appcompat.view.menu.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0012d {

        /* renamed from: a  reason: collision with root package name */
        public final y f909a;

        /* renamed from: b  reason: collision with root package name */
        public final g f910b;

        /* renamed from: c  reason: collision with root package name */
        public final int f911c;

        public C0012d(y yVar, g gVar, int i8) {
            this.f909a = yVar;
            this.f910b = gVar;
            this.f911c = i8;
        }

        public ListView a() {
            return this.f909a.m();
        }
    }

    public d(Context context, View view, int i8, int i9, boolean z4) {
        this.f884b = context;
        this.q = view;
        this.f886d = i8;
        this.f887e = i9;
        this.f888f = z4;
        Resources resources = context.getResources();
        this.f885c = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(g.d.f19899d));
        this.f889g = new Handler();
    }

    private y C() {
        y yVar = new y(this.f884b, null, this.f886d, this.f887e);
        yVar.T(this.f894m);
        yVar.L(this);
        yVar.K(this);
        yVar.D(this.q);
        yVar.G(this.f896p);
        yVar.J(true);
        yVar.I(2);
        return yVar;
    }

    private int D(g gVar) {
        int size = this.f891j.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (gVar == this.f891j.get(i8).f910b) {
                return i8;
            }
        }
        return -1;
    }

    private MenuItem E(g gVar, g gVar2) {
        int size = gVar.size();
        for (int i8 = 0; i8 < size; i8++) {
            MenuItem item = gVar.getItem(i8);
            if (item.hasSubMenu() && gVar2 == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    private View F(C0012d c0012d, g gVar) {
        f fVar;
        int i8;
        int firstVisiblePosition;
        MenuItem E = E(c0012d.f910b, gVar);
        if (E == null) {
            return null;
        }
        ListView a9 = c0012d.a();
        ListAdapter adapter = a9.getAdapter();
        int i9 = 0;
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            i8 = headerViewListAdapter.getHeadersCount();
            fVar = (f) headerViewListAdapter.getWrappedAdapter();
        } else {
            fVar = (f) adapter;
            i8 = 0;
        }
        int count = fVar.getCount();
        while (true) {
            if (i9 >= count) {
                i9 = -1;
                break;
            } else if (E == fVar.getItem(i9)) {
                break;
            } else {
                i9++;
            }
        }
        if (i9 != -1 && (firstVisiblePosition = (i9 + i8) - a9.getFirstVisiblePosition()) >= 0 && firstVisiblePosition < a9.getChildCount()) {
            return a9.getChildAt(firstVisiblePosition);
        }
        return null;
    }

    private int G() {
        return c0.E(this.q) == 1 ? 0 : 1;
    }

    private int H(int i8) {
        List<C0012d> list = this.f891j;
        ListView a9 = list.get(list.size() - 1).a();
        int[] iArr = new int[2];
        a9.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        this.f897t.getWindowVisibleDisplayFrame(rect);
        return this.f898w == 1 ? (iArr[0] + a9.getWidth()) + i8 > rect.right ? 0 : 1 : iArr[0] - i8 < 0 ? 1 : 0;
    }

    private void I(g gVar) {
        C0012d c0012d;
        View view;
        int i8;
        int i9;
        int i10;
        LayoutInflater from = LayoutInflater.from(this.f884b);
        f fVar = new f(gVar, from, this.f888f, K);
        if (!b() && this.B) {
            fVar.d(true);
        } else if (b()) {
            fVar.d(k.A(gVar));
        }
        int r4 = k.r(fVar, null, this.f884b, this.f885c);
        y C = C();
        C.p(fVar);
        C.F(r4);
        C.G(this.f896p);
        if (this.f891j.size() > 0) {
            List<C0012d> list = this.f891j;
            c0012d = list.get(list.size() - 1);
            view = F(c0012d, gVar);
        } else {
            c0012d = null;
            view = null;
        }
        if (view != null) {
            C.U(false);
            C.R(null);
            int H = H(r4);
            boolean z4 = H == 1;
            this.f898w = H;
            if (Build.VERSION.SDK_INT >= 26) {
                C.D(view);
                i9 = 0;
                i8 = 0;
            } else {
                int[] iArr = new int[2];
                this.q.getLocationOnScreen(iArr);
                int[] iArr2 = new int[2];
                view.getLocationOnScreen(iArr2);
                if ((this.f896p & 7) == 5) {
                    iArr[0] = iArr[0] + this.q.getWidth();
                    iArr2[0] = iArr2[0] + view.getWidth();
                }
                i8 = iArr2[0] - iArr[0];
                i9 = iArr2[1] - iArr[1];
            }
            if ((this.f896p & 5) == 5) {
                if (!z4) {
                    r4 = view.getWidth();
                    i10 = i8 - r4;
                }
                i10 = i8 + r4;
            } else {
                if (z4) {
                    r4 = view.getWidth();
                    i10 = i8 + r4;
                }
                i10 = i8 - r4;
            }
            C.f(i10);
            C.M(true);
            C.k(i9);
        } else {
            if (this.f899x) {
                C.f(this.f901z);
            }
            if (this.f900y) {
                C.k(this.A);
            }
            C.H(q());
        }
        this.f891j.add(new C0012d(C, gVar, this.f898w));
        C.a();
        ListView m8 = C.m();
        m8.setOnKeyListener(this);
        if (c0012d == null && this.C && gVar.z() != null) {
            FrameLayout frameLayout = (FrameLayout) from.inflate(g.g.f19974n, (ViewGroup) m8, false);
            frameLayout.setEnabled(false);
            ((TextView) frameLayout.findViewById(16908310)).setText(gVar.z());
            m8.addHeaderView(frameLayout, null, false);
            C.a();
        }
    }

    @Override // androidx.appcompat.view.menu.p
    public void a() {
        if (b()) {
            return;
        }
        for (g gVar : this.f890h) {
            I(gVar);
        }
        this.f890h.clear();
        View view = this.q;
        this.f897t = view;
        if (view != null) {
            boolean z4 = this.F == null;
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.F = viewTreeObserver;
            if (z4) {
                viewTreeObserver.addOnGlobalLayoutListener(this.f892k);
            }
            this.f897t.addOnAttachStateChangeListener(this.f893l);
        }
    }

    @Override // androidx.appcompat.view.menu.p
    public boolean b() {
        return this.f891j.size() > 0 && this.f891j.get(0).f909a.b();
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(g gVar, boolean z4) {
        int D = D(gVar);
        if (D < 0) {
            return;
        }
        int i8 = D + 1;
        if (i8 < this.f891j.size()) {
            this.f891j.get(i8).f910b.e(false);
        }
        C0012d remove = this.f891j.remove(D);
        remove.f910b.Q(this);
        if (this.H) {
            remove.f909a.S(null);
            remove.f909a.E(0);
        }
        remove.f909a.dismiss();
        int size = this.f891j.size();
        this.f898w = size > 0 ? this.f891j.get(size - 1).f911c : G();
        if (size != 0) {
            if (z4) {
                this.f891j.get(0).f910b.e(false);
                return;
            }
            return;
        }
        dismiss();
        m.a aVar = this.E;
        if (aVar != null) {
            aVar.c(gVar, true);
        }
        ViewTreeObserver viewTreeObserver = this.F;
        if (viewTreeObserver != null) {
            if (viewTreeObserver.isAlive()) {
                this.F.removeGlobalOnLayoutListener(this.f892k);
            }
            this.F = null;
        }
        this.f897t.removeOnAttachStateChangeListener(this.f893l);
        this.G.onDismiss();
    }

    @Override // androidx.appcompat.view.menu.k
    public void d(g gVar) {
        gVar.c(this, this.f884b);
        if (b()) {
            I(gVar);
        } else {
            this.f890h.add(gVar);
        }
    }

    @Override // androidx.appcompat.view.menu.p
    public void dismiss() {
        int size = this.f891j.size();
        if (size > 0) {
            C0012d[] c0012dArr = (C0012d[]) this.f891j.toArray(new C0012d[size]);
            for (int i8 = size - 1; i8 >= 0; i8--) {
                C0012d c0012d = c0012dArr[i8];
                if (c0012d.f909a.b()) {
                    c0012d.f909a.dismiss();
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        for (C0012d c0012d : this.f891j) {
            k.B(c0012d.a().getAdapter()).notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public void j(m.a aVar) {
        this.E = aVar;
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
    }

    @Override // androidx.appcompat.view.menu.p
    public ListView m() {
        if (this.f891j.isEmpty()) {
            return null;
        }
        List<C0012d> list = this.f891j;
        return list.get(list.size() - 1).a();
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean n(r rVar) {
        for (C0012d c0012d : this.f891j) {
            if (rVar == c0012d.f910b) {
                c0012d.a().requestFocus();
                return true;
            }
        }
        if (rVar.hasVisibleItems()) {
            d(rVar);
            m.a aVar = this.E;
            if (aVar != null) {
                aVar.d(rVar);
            }
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        return null;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        C0012d c0012d;
        int size = this.f891j.size();
        int i8 = 0;
        while (true) {
            if (i8 >= size) {
                c0012d = null;
                break;
            }
            c0012d = this.f891j.get(i8);
            if (!c0012d.f909a.b()) {
                break;
            }
            i8++;
        }
        if (c0012d != null) {
            c0012d.f910b.e(false);
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
    protected boolean p() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.k
    public void s(View view) {
        if (this.q != view) {
            this.q = view;
            this.f896p = androidx.core.view.f.b(this.f895n, c0.E(view));
        }
    }

    @Override // androidx.appcompat.view.menu.k
    public void u(boolean z4) {
        this.B = z4;
    }

    @Override // androidx.appcompat.view.menu.k
    public void v(int i8) {
        if (this.f895n != i8) {
            this.f895n = i8;
            this.f896p = androidx.core.view.f.b(i8, c0.E(this.q));
        }
    }

    @Override // androidx.appcompat.view.menu.k
    public void w(int i8) {
        this.f899x = true;
        this.f901z = i8;
    }

    @Override // androidx.appcompat.view.menu.k
    public void x(PopupWindow.OnDismissListener onDismissListener) {
        this.G = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.k
    public void y(boolean z4) {
        this.C = z4;
    }

    @Override // androidx.appcompat.view.menu.k
    public void z(int i8) {
        this.f900y = true;
        this.A = i8;
    }
}
