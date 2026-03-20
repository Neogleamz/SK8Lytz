package androidx.core.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b {

    /* renamed from: a  reason: collision with root package name */
    private final Context f4938a;

    /* renamed from: b  reason: collision with root package name */
    private a f4939b;

    /* renamed from: c  reason: collision with root package name */
    private InterfaceC0045b f4940c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(boolean z4);
    }

    /* renamed from: androidx.core.view.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0045b {
        void onActionProviderVisibilityChanged(boolean z4);
    }

    public b(Context context) {
        this.f4938a = context;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return true;
    }

    public abstract View c();

    public View d(MenuItem menuItem) {
        return c();
    }

    public boolean e() {
        return false;
    }

    public void f(SubMenu subMenu) {
    }

    public boolean g() {
        return false;
    }

    public void h() {
        this.f4940c = null;
        this.f4939b = null;
    }

    public void i(a aVar) {
        this.f4939b = aVar;
    }

    public void j(InterfaceC0045b interfaceC0045b) {
        if (this.f4940c != null && interfaceC0045b != null) {
            Log.w("ActionProvider(support)", "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + getClass().getSimpleName() + " instance while it is still in use somewhere else?");
        }
        this.f4940c = interfaceC0045b;
    }

    public void k(boolean z4) {
        a aVar = this.f4939b;
        if (aVar != null) {
            aVar.a(z4);
        }
    }
}
