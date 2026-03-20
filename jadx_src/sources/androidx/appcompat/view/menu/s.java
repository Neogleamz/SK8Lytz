package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s extends o implements SubMenu {

    /* renamed from: e  reason: collision with root package name */
    private final s0.c f1031e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(Context context, s0.c cVar) {
        super(context, cVar);
        this.f1031e = cVar;
    }

    @Override // android.view.SubMenu
    public void clearHeader() {
        this.f1031e.clearHeader();
    }

    @Override // android.view.SubMenu
    public MenuItem getItem() {
        return c(this.f1031e.getItem());
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(int i8) {
        this.f1031e.setHeaderIcon(i8);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(Drawable drawable) {
        this.f1031e.setHeaderIcon(drawable);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(int i8) {
        this.f1031e.setHeaderTitle(i8);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(CharSequence charSequence) {
        this.f1031e.setHeaderTitle(charSequence);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderView(View view) {
        this.f1031e.setHeaderView(view);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(int i8) {
        this.f1031e.setIcon(i8);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(Drawable drawable) {
        this.f1031e.setIcon(drawable);
        return this;
    }
}
