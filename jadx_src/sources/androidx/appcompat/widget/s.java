package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface s {
    void a(Menu menu, m.a aVar);

    boolean b();

    void c();

    void collapseActionView();

    boolean d();

    boolean e();

    boolean f();

    boolean g();

    Context getContext();

    CharSequence getTitle();

    void h();

    void i(c0 c0Var);

    boolean j();

    void k(int i8);

    Menu l();

    void m(int i8);

    int n();

    androidx.core.view.i0 o(int i8, long j8);

    void p(m.a aVar, g.a aVar2);

    ViewGroup q();

    void r(boolean z4);

    int s();

    void setIcon(int i8);

    void setIcon(Drawable drawable);

    void setTitle(CharSequence charSequence);

    void setVisibility(int i8);

    void setWindowCallback(Window.Callback callback);

    void setWindowTitle(CharSequence charSequence);

    void t(int i8);

    void u();

    void v();

    void w(Drawable drawable);

    void x(boolean z4);
}
