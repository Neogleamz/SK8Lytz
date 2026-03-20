package androidx.fragment.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.lifecycle.j0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    private final h<?> f5625a;

    private f(h<?> hVar) {
        this.f5625a = hVar;
    }

    public static f b(h<?> hVar) {
        return new f((h) androidx.core.util.h.i(hVar, "callbacks == null"));
    }

    public void a(Fragment fragment) {
        h<?> hVar = this.f5625a;
        hVar.f5631e.j(hVar, hVar, fragment);
    }

    public void c() {
        this.f5625a.f5631e.y();
    }

    public void d(Configuration configuration) {
        this.f5625a.f5631e.A(configuration);
    }

    public boolean e(MenuItem menuItem) {
        return this.f5625a.f5631e.B(menuItem);
    }

    public void f() {
        this.f5625a.f5631e.C();
    }

    public boolean g(Menu menu, MenuInflater menuInflater) {
        return this.f5625a.f5631e.D(menu, menuInflater);
    }

    public void h() {
        this.f5625a.f5631e.E();
    }

    public void i() {
        this.f5625a.f5631e.G();
    }

    public void j(boolean z4) {
        this.f5625a.f5631e.H(z4);
    }

    public boolean k(MenuItem menuItem) {
        return this.f5625a.f5631e.J(menuItem);
    }

    public void l(Menu menu) {
        this.f5625a.f5631e.K(menu);
    }

    public void m() {
        this.f5625a.f5631e.M();
    }

    public void n(boolean z4) {
        this.f5625a.f5631e.N(z4);
    }

    public boolean o(Menu menu) {
        return this.f5625a.f5631e.O(menu);
    }

    public void p() {
        this.f5625a.f5631e.Q();
    }

    public void q() {
        this.f5625a.f5631e.R();
    }

    public void r() {
        this.f5625a.f5631e.T();
    }

    public boolean s() {
        return this.f5625a.f5631e.a0(true);
    }

    public FragmentManager t() {
        return this.f5625a.f5631e;
    }

    public void u() {
        this.f5625a.f5631e.T0();
    }

    public View v(View view, String str, Context context, AttributeSet attributeSet) {
        return this.f5625a.f5631e.u0().onCreateView(view, str, context, attributeSet);
    }

    public void w(Parcelable parcelable) {
        h<?> hVar = this.f5625a;
        if (!(hVar instanceof j0)) {
            throw new IllegalStateException("Your FragmentHostCallback must implement ViewModelStoreOwner to call restoreSaveState(). Call restoreAllState()  if you're still using retainNestedNonConfig().");
        }
        hVar.f5631e.g1(parcelable);
    }

    public Parcelable x() {
        return this.f5625a.f5631e.i1();
    }
}
