package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c {

    /* renamed from: a  reason: collision with root package name */
    final Context f881a;

    /* renamed from: b  reason: collision with root package name */
    private k0.g<s0.b, MenuItem> f882b;

    /* renamed from: c  reason: collision with root package name */
    private k0.g<s0.c, SubMenu> f883c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Context context) {
        this.f881a = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MenuItem c(MenuItem menuItem) {
        if (menuItem instanceof s0.b) {
            s0.b bVar = (s0.b) menuItem;
            if (this.f882b == null) {
                this.f882b = new k0.g<>();
            }
            MenuItem menuItem2 = this.f882b.get(bVar);
            if (menuItem2 == null) {
                j jVar = new j(this.f881a, bVar);
                this.f882b.put(bVar, jVar);
                return jVar;
            }
            return menuItem2;
        }
        return menuItem;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SubMenu d(SubMenu subMenu) {
        if (subMenu instanceof s0.c) {
            s0.c cVar = (s0.c) subMenu;
            if (this.f883c == null) {
                this.f883c = new k0.g<>();
            }
            SubMenu subMenu2 = this.f883c.get(cVar);
            if (subMenu2 == null) {
                s sVar = new s(this.f881a, cVar);
                this.f883c.put(cVar, sVar);
                return sVar;
            }
            return subMenu2;
        }
        return subMenu;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        k0.g<s0.b, MenuItem> gVar = this.f882b;
        if (gVar != null) {
            gVar.clear();
        }
        k0.g<s0.c, SubMenu> gVar2 = this.f883c;
        if (gVar2 != null) {
            gVar2.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f(int i8) {
        if (this.f882b == null) {
            return;
        }
        int i9 = 0;
        while (i9 < this.f882b.size()) {
            if (this.f882b.k(i9).getGroupId() == i8) {
                this.f882b.m(i9);
                i9--;
            }
            i9++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g(int i8) {
        if (this.f882b == null) {
            return;
        }
        for (int i9 = 0; i9 < this.f882b.size(); i9++) {
            if (this.f882b.k(i9).getItemId() == i8) {
                this.f882b.m(i9);
                return;
            }
        }
    }
}
