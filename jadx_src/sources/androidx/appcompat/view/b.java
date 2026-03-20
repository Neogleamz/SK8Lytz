package androidx.appcompat.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b {

    /* renamed from: a  reason: collision with root package name */
    private Object f757a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f758b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(b bVar);

        boolean b(b bVar, Menu menu);

        boolean c(b bVar, Menu menu);

        boolean d(b bVar, MenuItem menuItem);
    }

    public abstract void c();

    public abstract View d();

    public abstract Menu e();

    public abstract MenuInflater f();

    public abstract CharSequence g();

    public Object h() {
        return this.f757a;
    }

    public abstract CharSequence i();

    public boolean j() {
        return this.f758b;
    }

    public abstract void k();

    public abstract boolean l();

    public abstract void m(View view);

    public abstract void n(int i8);

    public abstract void o(CharSequence charSequence);

    public void p(Object obj) {
        this.f757a = obj;
    }

    public abstract void q(int i8);

    public abstract void r(CharSequence charSequence);

    public void s(boolean z4) {
        this.f758b = z4;
    }
}
