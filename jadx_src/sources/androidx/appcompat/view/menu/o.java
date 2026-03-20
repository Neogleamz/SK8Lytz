package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o extends c implements Menu {

    /* renamed from: d  reason: collision with root package name */
    private final s0.a f1010d;

    public o(Context context, s0.a aVar) {
        super(context);
        if (aVar == null) {
            throw new IllegalArgumentException("Wrapped Object can not be null.");
        }
        this.f1010d = aVar;
    }

    @Override // android.view.Menu
    public MenuItem add(int i8) {
        return c(this.f1010d.add(i8));
    }

    @Override // android.view.Menu
    public MenuItem add(int i8, int i9, int i10, int i11) {
        return c(this.f1010d.add(i8, i9, i10, i11));
    }

    @Override // android.view.Menu
    public MenuItem add(int i8, int i9, int i10, CharSequence charSequence) {
        return c(this.f1010d.add(i8, i9, i10, charSequence));
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return c(this.f1010d.add(charSequence));
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i8, int i9, int i10, ComponentName componentName, Intent[] intentArr, Intent intent, int i11, MenuItem[] menuItemArr) {
        MenuItem[] menuItemArr2 = menuItemArr != null ? new MenuItem[menuItemArr.length] : null;
        int addIntentOptions = this.f1010d.addIntentOptions(i8, i9, i10, componentName, intentArr, intent, i11, menuItemArr2);
        if (menuItemArr2 != null) {
            int length = menuItemArr2.length;
            for (int i12 = 0; i12 < length; i12++) {
                menuItemArr[i12] = c(menuItemArr2[i12]);
            }
        }
        return addIntentOptions;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8) {
        return d(this.f1010d.addSubMenu(i8));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, int i11) {
        return d(this.f1010d.addSubMenu(i8, i9, i10, i11));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, CharSequence charSequence) {
        return d(this.f1010d.addSubMenu(i8, i9, i10, charSequence));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        return d(this.f1010d.addSubMenu(charSequence));
    }

    @Override // android.view.Menu
    public void clear() {
        e();
        this.f1010d.clear();
    }

    @Override // android.view.Menu
    public void close() {
        this.f1010d.close();
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i8) {
        return c(this.f1010d.findItem(i8));
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i8) {
        return c(this.f1010d.getItem(i8));
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        return this.f1010d.hasVisibleItems();
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i8, KeyEvent keyEvent) {
        return this.f1010d.isShortcutKey(i8, keyEvent);
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i8, int i9) {
        return this.f1010d.performIdentifierAction(i8, i9);
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i8, KeyEvent keyEvent, int i9) {
        return this.f1010d.performShortcut(i8, keyEvent, i9);
    }

    @Override // android.view.Menu
    public void removeGroup(int i8) {
        f(i8);
        this.f1010d.removeGroup(i8);
    }

    @Override // android.view.Menu
    public void removeItem(int i8) {
        g(i8);
        this.f1010d.removeItem(i8);
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i8, boolean z4, boolean z8) {
        this.f1010d.setGroupCheckable(i8, z4, z8);
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i8, boolean z4) {
        this.f1010d.setGroupEnabled(i8, z4);
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i8, boolean z4) {
        this.f1010d.setGroupVisible(i8, z4);
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z4) {
        this.f1010d.setQwertyMode(z4);
    }

    @Override // android.view.Menu
    public int size() {
        return this.f1010d.size();
    }
}
