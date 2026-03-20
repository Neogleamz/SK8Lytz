package r7;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final View f22736a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f22737b = false;

    /* renamed from: c  reason: collision with root package name */
    private int f22738c = 0;

    public b(a aVar) {
        this.f22736a = (View) aVar;
    }

    private void a() {
        ViewParent parent = this.f22736a.getParent();
        if (parent instanceof CoordinatorLayout) {
            ((CoordinatorLayout) parent).p(this.f22736a);
        }
    }

    public int b() {
        return this.f22738c;
    }

    public boolean c() {
        return this.f22737b;
    }

    public void d(Bundle bundle) {
        this.f22737b = bundle.getBoolean("expanded", false);
        this.f22738c = bundle.getInt("expandedComponentIdHint", 0);
        if (this.f22737b) {
            a();
        }
    }

    public Bundle e() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expanded", this.f22737b);
        bundle.putInt("expandedComponentIdHint", this.f22738c);
        return bundle;
    }

    public void f(int i8) {
        this.f22738c = i8;
    }
}
