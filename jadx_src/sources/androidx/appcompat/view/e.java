package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.b;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.widget.ActionBarContextView;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends b implements g.a {

    /* renamed from: c  reason: collision with root package name */
    private Context f765c;

    /* renamed from: d  reason: collision with root package name */
    private ActionBarContextView f766d;

    /* renamed from: e  reason: collision with root package name */
    private b.a f767e;

    /* renamed from: f  reason: collision with root package name */
    private WeakReference<View> f768f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f769g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f770h;

    /* renamed from: j  reason: collision with root package name */
    private androidx.appcompat.view.menu.g f771j;

    public e(Context context, ActionBarContextView actionBarContextView, b.a aVar, boolean z4) {
        this.f765c = context;
        this.f766d = actionBarContextView;
        this.f767e = aVar;
        androidx.appcompat.view.menu.g W = new androidx.appcompat.view.menu.g(actionBarContextView.getContext()).W(1);
        this.f771j = W;
        W.V(this);
        this.f770h = z4;
    }

    @Override // androidx.appcompat.view.menu.g.a
    public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
        return this.f767e.d(this, menuItem);
    }

    @Override // androidx.appcompat.view.menu.g.a
    public void b(androidx.appcompat.view.menu.g gVar) {
        k();
        this.f766d.l();
    }

    @Override // androidx.appcompat.view.b
    public void c() {
        if (this.f769g) {
            return;
        }
        this.f769g = true;
        this.f767e.a(this);
    }

    @Override // androidx.appcompat.view.b
    public View d() {
        WeakReference<View> weakReference = this.f768f;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    @Override // androidx.appcompat.view.b
    public Menu e() {
        return this.f771j;
    }

    @Override // androidx.appcompat.view.b
    public MenuInflater f() {
        return new g(this.f766d.getContext());
    }

    @Override // androidx.appcompat.view.b
    public CharSequence g() {
        return this.f766d.getSubtitle();
    }

    @Override // androidx.appcompat.view.b
    public CharSequence i() {
        return this.f766d.getTitle();
    }

    @Override // androidx.appcompat.view.b
    public void k() {
        this.f767e.c(this, this.f771j);
    }

    @Override // androidx.appcompat.view.b
    public boolean l() {
        return this.f766d.j();
    }

    @Override // androidx.appcompat.view.b
    public void m(View view) {
        this.f766d.setCustomView(view);
        this.f768f = view != null ? new WeakReference<>(view) : null;
    }

    @Override // androidx.appcompat.view.b
    public void n(int i8) {
        o(this.f765c.getString(i8));
    }

    @Override // androidx.appcompat.view.b
    public void o(CharSequence charSequence) {
        this.f766d.setSubtitle(charSequence);
    }

    @Override // androidx.appcompat.view.b
    public void q(int i8) {
        r(this.f765c.getString(i8));
    }

    @Override // androidx.appcompat.view.b
    public void r(CharSequence charSequence) {
        this.f766d.setTitle(charSequence);
    }

    @Override // androidx.appcompat.view.b
    public void s(boolean z4) {
        super.s(z4);
        this.f766d.setTitleOptional(z4);
    }
}
