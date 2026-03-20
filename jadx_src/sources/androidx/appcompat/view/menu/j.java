package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ActionProvider;
import android.view.CollapsibleActionView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.b;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends androidx.appcompat.view.menu.c implements MenuItem {

    /* renamed from: d  reason: collision with root package name */
    private final s0.b f985d;

    /* renamed from: e  reason: collision with root package name */
    private Method f986e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class a extends androidx.core.view.b {

        /* renamed from: d  reason: collision with root package name */
        final ActionProvider f987d;

        a(Context context, ActionProvider actionProvider) {
            super(context);
            this.f987d = actionProvider;
        }

        @Override // androidx.core.view.b
        public boolean a() {
            return this.f987d.hasSubMenu();
        }

        @Override // androidx.core.view.b
        public View c() {
            return this.f987d.onCreateActionView();
        }

        @Override // androidx.core.view.b
        public boolean e() {
            return this.f987d.onPerformDefaultAction();
        }

        @Override // androidx.core.view.b
        public void f(SubMenu subMenu) {
            this.f987d.onPrepareSubMenu(j.this.d(subMenu));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b extends a implements ActionProvider.VisibilityListener {

        /* renamed from: f  reason: collision with root package name */
        private b.InterfaceC0045b f989f;

        b(Context context, ActionProvider actionProvider) {
            super(context, actionProvider);
        }

        @Override // androidx.core.view.b
        public boolean b() {
            return this.f987d.isVisible();
        }

        @Override // androidx.core.view.b
        public View d(MenuItem menuItem) {
            return this.f987d.onCreateActionView(menuItem);
        }

        @Override // androidx.core.view.b
        public boolean g() {
            return this.f987d.overridesItemVisibility();
        }

        @Override // androidx.core.view.b
        public void j(b.InterfaceC0045b interfaceC0045b) {
            this.f989f = interfaceC0045b;
            this.f987d.setVisibilityListener(interfaceC0045b != null ? this : null);
        }

        @Override // android.view.ActionProvider.VisibilityListener
        public void onActionProviderVisibilityChanged(boolean z4) {
            b.InterfaceC0045b interfaceC0045b = this.f989f;
            if (interfaceC0045b != null) {
                interfaceC0045b.onActionProviderVisibilityChanged(z4);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends FrameLayout implements androidx.appcompat.view.c {

        /* renamed from: a  reason: collision with root package name */
        final CollapsibleActionView f991a;

        c(View view) {
            super(view.getContext());
            this.f991a = (CollapsibleActionView) view;
            addView(view);
        }

        View a() {
            return (View) this.f991a;
        }

        @Override // androidx.appcompat.view.c
        public void c() {
            this.f991a.onActionViewExpanded();
        }

        @Override // androidx.appcompat.view.c
        public void f() {
            this.f991a.onActionViewCollapsed();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d implements MenuItem.OnActionExpandListener {

        /* renamed from: a  reason: collision with root package name */
        private final MenuItem.OnActionExpandListener f992a;

        d(MenuItem.OnActionExpandListener onActionExpandListener) {
            this.f992a = onActionExpandListener;
        }

        @Override // android.view.MenuItem.OnActionExpandListener
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            return this.f992a.onMenuItemActionCollapse(j.this.c(menuItem));
        }

        @Override // android.view.MenuItem.OnActionExpandListener
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return this.f992a.onMenuItemActionExpand(j.this.c(menuItem));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class e implements MenuItem.OnMenuItemClickListener {

        /* renamed from: a  reason: collision with root package name */
        private final MenuItem.OnMenuItemClickListener f994a;

        e(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
            this.f994a = onMenuItemClickListener;
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            return this.f994a.onMenuItemClick(j.this.c(menuItem));
        }
    }

    public j(Context context, s0.b bVar) {
        super(context);
        if (bVar == null) {
            throw new IllegalArgumentException("Wrapped Object can not be null.");
        }
        this.f985d = bVar;
    }

    @Override // android.view.MenuItem
    public boolean collapseActionView() {
        return this.f985d.collapseActionView();
    }

    @Override // android.view.MenuItem
    public boolean expandActionView() {
        return this.f985d.expandActionView();
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        androidx.core.view.b b9 = this.f985d.b();
        if (b9 instanceof a) {
            return ((a) b9).f987d;
        }
        return null;
    }

    @Override // android.view.MenuItem
    public View getActionView() {
        View actionView = this.f985d.getActionView();
        return actionView instanceof c ? ((c) actionView).a() : actionView;
    }

    @Override // android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f985d.getAlphabeticModifiers();
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f985d.getAlphabeticShortcut();
    }

    @Override // android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f985d.getContentDescription();
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f985d.getGroupId();
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        return this.f985d.getIcon();
    }

    @Override // android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f985d.getIconTintList();
    }

    @Override // android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f985d.getIconTintMode();
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f985d.getIntent();
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.f985d.getItemId();
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.f985d.getMenuInfo();
    }

    @Override // android.view.MenuItem
    public int getNumericModifiers() {
        return this.f985d.getNumericModifiers();
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f985d.getNumericShortcut();
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f985d.getOrder();
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return d(this.f985d.getSubMenu());
    }

    @Override // android.view.MenuItem
    public CharSequence getTitle() {
        return this.f985d.getTitle();
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        return this.f985d.getTitleCondensed();
    }

    @Override // android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f985d.getTooltipText();
    }

    public void h(boolean z4) {
        try {
            if (this.f986e == null) {
                this.f986e = this.f985d.getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.f986e.invoke(this.f985d, Boolean.valueOf(z4));
        } catch (Exception e8) {
            Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", e8);
        }
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return this.f985d.hasSubMenu();
    }

    @Override // android.view.MenuItem
    public boolean isActionViewExpanded() {
        return this.f985d.isActionViewExpanded();
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return this.f985d.isCheckable();
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return this.f985d.isChecked();
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return this.f985d.isEnabled();
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        return this.f985d.isVisible();
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        androidx.core.view.b bVar = Build.VERSION.SDK_INT >= 16 ? new b(this.f881a, actionProvider) : new a(this.f881a, actionProvider);
        s0.b bVar2 = this.f985d;
        if (actionProvider == null) {
            bVar = null;
        }
        bVar2.a(bVar);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(int i8) {
        this.f985d.setActionView(i8);
        View actionView = this.f985d.getActionView();
        if (actionView instanceof CollapsibleActionView) {
            this.f985d.setActionView(new c(actionView));
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(View view) {
        if (view instanceof CollapsibleActionView) {
            view = new c(view);
        }
        this.f985d.setActionView(view);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9) {
        this.f985d.setAlphabeticShortcut(c9);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9, int i8) {
        this.f985d.setAlphabeticShortcut(c9, i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z4) {
        this.f985d.setCheckable(z4);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z4) {
        this.f985d.setChecked(z4);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setContentDescription(CharSequence charSequence) {
        this.f985d.setContentDescription(charSequence);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z4) {
        this.f985d.setEnabled(z4);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i8) {
        this.f985d.setIcon(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f985d.setIcon(drawable);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f985d.setIconTintList(colorStateList);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f985d.setIconTintMode(mode);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f985d.setIntent(intent);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c9) {
        this.f985d.setNumericShortcut(c9);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c9, int i8) {
        this.f985d.setNumericShortcut(c9, i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        this.f985d.setOnActionExpandListener(onActionExpandListener != null ? new d(onActionExpandListener) : null);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f985d.setOnMenuItemClickListener(onMenuItemClickListener != null ? new e(onMenuItemClickListener) : null);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10) {
        this.f985d.setShortcut(c9, c10);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10, int i8, int i9) {
        this.f985d.setShortcut(c9, c10, i8, i9);
        return this;
    }

    @Override // android.view.MenuItem
    public void setShowAsAction(int i8) {
        this.f985d.setShowAsAction(i8);
    }

    @Override // android.view.MenuItem
    public MenuItem setShowAsActionFlags(int i8) {
        this.f985d.setShowAsActionFlags(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i8) {
        this.f985d.setTitle(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f985d.setTitle(charSequence);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f985d.setTitleCondensed(charSequence);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTooltipText(CharSequence charSequence) {
        this.f985d.setTooltipText(charSequence);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z4) {
        return this.f985d.setVisible(z4);
    }
}
