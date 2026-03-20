package androidx.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegateImpl;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.n0;
import androidx.core.view.c0;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q extends ActionBar {

    /* renamed from: a  reason: collision with root package name */
    final androidx.appcompat.widget.s f698a;

    /* renamed from: b  reason: collision with root package name */
    final Window.Callback f699b;

    /* renamed from: c  reason: collision with root package name */
    final AppCompatDelegateImpl.i f700c;

    /* renamed from: d  reason: collision with root package name */
    boolean f701d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f702e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f703f;

    /* renamed from: g  reason: collision with root package name */
    private ArrayList<ActionBar.a> f704g = new ArrayList<>();

    /* renamed from: h  reason: collision with root package name */
    private final Runnable f705h = new a();

    /* renamed from: i  reason: collision with root package name */
    private final Toolbar.g f706i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            q.this.B();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Toolbar.g {
        b() {
        }

        @Override // androidx.appcompat.widget.Toolbar.g
        public boolean onMenuItemClick(MenuItem menuItem) {
            return q.this.f699b.onMenuItemSelected(0, menuItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c implements m.a {

        /* renamed from: a  reason: collision with root package name */
        private boolean f709a;

        c() {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
            if (this.f709a) {
                return;
            }
            this.f709a = true;
            q.this.f698a.h();
            q.this.f699b.onPanelClosed(108, gVar);
            this.f709a = false;
        }

        @Override // androidx.appcompat.view.menu.m.a
        public boolean d(androidx.appcompat.view.menu.g gVar) {
            q.this.f699b.onMenuOpened(108, gVar);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d implements g.a {
        d() {
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
            return false;
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(androidx.appcompat.view.menu.g gVar) {
            if (q.this.f698a.b()) {
                q.this.f699b.onPanelClosed(108, gVar);
            } else if (q.this.f699b.onPreparePanel(0, null, gVar)) {
                q.this.f699b.onMenuOpened(108, gVar);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class e implements AppCompatDelegateImpl.i {
        e() {
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.i
        public boolean a(int i8) {
            if (i8 == 0) {
                q qVar = q.this;
                if (qVar.f701d) {
                    return false;
                }
                qVar.f698a.c();
                q.this.f701d = true;
                return false;
            }
            return false;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.i
        public View onCreatePanelView(int i8) {
            if (i8 == 0) {
                return new View(q.this.f698a.getContext());
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(Toolbar toolbar, CharSequence charSequence, Window.Callback callback) {
        b bVar = new b();
        this.f706i = bVar;
        androidx.core.util.h.h(toolbar);
        n0 n0Var = new n0(toolbar, false);
        this.f698a = n0Var;
        this.f699b = (Window.Callback) androidx.core.util.h.h(callback);
        n0Var.setWindowCallback(callback);
        toolbar.setOnMenuItemClickListener(bVar);
        n0Var.setWindowTitle(charSequence);
        this.f700c = new e();
    }

    private Menu A() {
        if (!this.f702e) {
            this.f698a.p(new c(), new d());
            this.f702e = true;
        }
        return this.f698a.l();
    }

    void B() {
        Menu A = A();
        androidx.appcompat.view.menu.g gVar = A instanceof androidx.appcompat.view.menu.g ? (androidx.appcompat.view.menu.g) A : null;
        if (gVar != null) {
            gVar.h0();
        }
        try {
            A.clear();
            if (!this.f699b.onCreatePanelMenu(0, A) || !this.f699b.onPreparePanel(0, null, A)) {
                A.clear();
            }
        } finally {
            if (gVar != null) {
                gVar.g0();
            }
        }
    }

    public void C(int i8, int i9) {
        this.f698a.k((i8 & i9) | ((~i9) & this.f698a.s()));
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean g() {
        return this.f698a.f();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean h() {
        if (this.f698a.j()) {
            this.f698a.collapseActionView();
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void i(boolean z4) {
        if (z4 == this.f703f) {
            return;
        }
        this.f703f = z4;
        int size = this.f704g.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f704g.get(i8).a(z4);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public int j() {
        return this.f698a.s();
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context k() {
        return this.f698a.getContext();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean l() {
        this.f698a.q().removeCallbacks(this.f705h);
        c0.l0(this.f698a.q(), this.f705h);
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void m(Configuration configuration) {
        super.m(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.appcompat.app.ActionBar
    public void n() {
        this.f698a.q().removeCallbacks(this.f705h);
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean o(int i8, KeyEvent keyEvent) {
        Menu A = A();
        if (A != null) {
            A.setQwertyMode(KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1);
            return A.performShortcut(i8, keyEvent, 0);
        }
        return false;
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean p(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            q();
        }
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean q() {
        return this.f698a.g();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void r(boolean z4) {
    }

    @Override // androidx.appcompat.app.ActionBar
    public void s(boolean z4) {
        C(z4 ? 4 : 0, 4);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void t(boolean z4) {
        C(z4 ? 8 : 0, 8);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void u(int i8) {
        this.f698a.t(i8);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void v(Drawable drawable) {
        this.f698a.w(drawable);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void w(boolean z4) {
    }

    @Override // androidx.appcompat.app.ActionBar
    public void x(CharSequence charSequence) {
        this.f698a.setTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void y(CharSequence charSequence) {
        this.f698a.setWindowTitle(charSequence);
    }
}
