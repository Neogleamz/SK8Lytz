package androidx.appcompat.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.b;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.view.menu.o;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends ActionMode {

    /* renamed from: a  reason: collision with root package name */
    final Context f772a;

    /* renamed from: b  reason: collision with root package name */
    final b f773b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements b.a {

        /* renamed from: a  reason: collision with root package name */
        final ActionMode.Callback f774a;

        /* renamed from: b  reason: collision with root package name */
        final Context f775b;

        /* renamed from: c  reason: collision with root package name */
        final ArrayList<f> f776c = new ArrayList<>();

        /* renamed from: d  reason: collision with root package name */
        final k0.g<Menu, Menu> f777d = new k0.g<>();

        public a(Context context, ActionMode.Callback callback) {
            this.f775b = context;
            this.f774a = callback;
        }

        private Menu f(Menu menu) {
            Menu menu2 = this.f777d.get(menu);
            if (menu2 == null) {
                o oVar = new o(this.f775b, (s0.a) menu);
                this.f777d.put(menu, oVar);
                return oVar;
            }
            return menu2;
        }

        @Override // androidx.appcompat.view.b.a
        public void a(b bVar) {
            this.f774a.onDestroyActionMode(e(bVar));
        }

        @Override // androidx.appcompat.view.b.a
        public boolean b(b bVar, Menu menu) {
            return this.f774a.onCreateActionMode(e(bVar), f(menu));
        }

        @Override // androidx.appcompat.view.b.a
        public boolean c(b bVar, Menu menu) {
            return this.f774a.onPrepareActionMode(e(bVar), f(menu));
        }

        @Override // androidx.appcompat.view.b.a
        public boolean d(b bVar, MenuItem menuItem) {
            return this.f774a.onActionItemClicked(e(bVar), new j(this.f775b, (s0.b) menuItem));
        }

        public ActionMode e(b bVar) {
            int size = this.f776c.size();
            for (int i8 = 0; i8 < size; i8++) {
                f fVar = this.f776c.get(i8);
                if (fVar != null && fVar.f773b == bVar) {
                    return fVar;
                }
            }
            f fVar2 = new f(this.f775b, bVar);
            this.f776c.add(fVar2);
            return fVar2;
        }
    }

    public f(Context context, b bVar) {
        this.f772a = context;
        this.f773b = bVar;
    }

    @Override // android.view.ActionMode
    public void finish() {
        this.f773b.c();
    }

    @Override // android.view.ActionMode
    public View getCustomView() {
        return this.f773b.d();
    }

    @Override // android.view.ActionMode
    public Menu getMenu() {
        return new o(this.f772a, (s0.a) this.f773b.e());
    }

    @Override // android.view.ActionMode
    public MenuInflater getMenuInflater() {
        return this.f773b.f();
    }

    @Override // android.view.ActionMode
    public CharSequence getSubtitle() {
        return this.f773b.g();
    }

    @Override // android.view.ActionMode
    public Object getTag() {
        return this.f773b.h();
    }

    @Override // android.view.ActionMode
    public CharSequence getTitle() {
        return this.f773b.i();
    }

    @Override // android.view.ActionMode
    public boolean getTitleOptionalHint() {
        return this.f773b.j();
    }

    @Override // android.view.ActionMode
    public void invalidate() {
        this.f773b.k();
    }

    @Override // android.view.ActionMode
    public boolean isTitleOptional() {
        return this.f773b.l();
    }

    @Override // android.view.ActionMode
    public void setCustomView(View view) {
        this.f773b.m(view);
    }

    @Override // android.view.ActionMode
    public void setSubtitle(int i8) {
        this.f773b.n(i8);
    }

    @Override // android.view.ActionMode
    public void setSubtitle(CharSequence charSequence) {
        this.f773b.o(charSequence);
    }

    @Override // android.view.ActionMode
    public void setTag(Object obj) {
        this.f773b.p(obj);
    }

    @Override // android.view.ActionMode
    public void setTitle(int i8) {
        this.f773b.q(i8);
    }

    @Override // android.view.ActionMode
    public void setTitle(CharSequence charSequence) {
        this.f773b.r(charSequence);
    }

    @Override // android.view.ActionMode
    public void setTitleOptionalHint(boolean z4) {
        this.f773b.s(z4);
    }
}
