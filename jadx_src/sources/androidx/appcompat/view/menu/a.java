package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements s0.b {

    /* renamed from: a  reason: collision with root package name */
    private final int f852a;

    /* renamed from: b  reason: collision with root package name */
    private final int f853b;

    /* renamed from: c  reason: collision with root package name */
    private final int f854c;

    /* renamed from: d  reason: collision with root package name */
    private CharSequence f855d;

    /* renamed from: e  reason: collision with root package name */
    private CharSequence f856e;

    /* renamed from: f  reason: collision with root package name */
    private Intent f857f;

    /* renamed from: g  reason: collision with root package name */
    private char f858g;

    /* renamed from: i  reason: collision with root package name */
    private char f860i;

    /* renamed from: k  reason: collision with root package name */
    private Drawable f862k;

    /* renamed from: l  reason: collision with root package name */
    private Context f863l;

    /* renamed from: m  reason: collision with root package name */
    private MenuItem.OnMenuItemClickListener f864m;

    /* renamed from: n  reason: collision with root package name */
    private CharSequence f865n;

    /* renamed from: o  reason: collision with root package name */
    private CharSequence f866o;

    /* renamed from: h  reason: collision with root package name */
    private int f859h = RecognitionOptions.AZTEC;

    /* renamed from: j  reason: collision with root package name */
    private int f861j = RecognitionOptions.AZTEC;

    /* renamed from: p  reason: collision with root package name */
    private ColorStateList f867p = null;
    private PorterDuff.Mode q = null;

    /* renamed from: r  reason: collision with root package name */
    private boolean f868r = false;

    /* renamed from: s  reason: collision with root package name */
    private boolean f869s = false;

    /* renamed from: t  reason: collision with root package name */
    private int f870t = 16;

    public a(Context context, int i8, int i9, int i10, int i11, CharSequence charSequence) {
        this.f863l = context;
        this.f852a = i9;
        this.f853b = i8;
        this.f854c = i11;
        this.f855d = charSequence;
    }

    private void c() {
        Drawable drawable = this.f862k;
        if (drawable != null) {
            if (this.f868r || this.f869s) {
                Drawable r4 = androidx.core.graphics.drawable.a.r(drawable);
                this.f862k = r4;
                Drawable mutate = r4.mutate();
                this.f862k = mutate;
                if (this.f868r) {
                    androidx.core.graphics.drawable.a.o(mutate, this.f867p);
                }
                if (this.f869s) {
                    androidx.core.graphics.drawable.a.p(this.f862k, this.q);
                }
            }
        }
    }

    @Override // s0.b
    public s0.b a(androidx.core.view.b bVar) {
        throw new UnsupportedOperationException();
    }

    @Override // s0.b
    public androidx.core.view.b b() {
        return null;
    }

    @Override // s0.b, android.view.MenuItem
    public boolean collapseActionView() {
        return false;
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: d */
    public s0.b setActionView(int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: e */
    public s0.b setActionView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override // s0.b, android.view.MenuItem
    public boolean expandActionView() {
        return false;
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: f */
    public s0.b setShowAsActionFlags(int i8) {
        setShowAsAction(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException();
    }

    @Override // s0.b, android.view.MenuItem
    public View getActionView() {
        return null;
    }

    @Override // s0.b, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f861j;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f860i;
    }

    @Override // s0.b, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f865n;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f853b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        return this.f862k;
    }

    @Override // s0.b, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f867p;
    }

    @Override // s0.b, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.q;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f857f;
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.f852a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override // s0.b, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f859h;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f858g;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f854c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return null;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitle() {
        return this.f855d;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f856e;
        return charSequence != null ? charSequence : this.f855d;
    }

    @Override // s0.b, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f866o;
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return false;
    }

    @Override // s0.b, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f870t & 1) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f870t & 2) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f870t & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        return (this.f870t & 8) == 0;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9) {
        this.f860i = Character.toLowerCase(c9);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9, int i8) {
        this.f860i = Character.toLowerCase(c9);
        this.f861j = KeyEvent.normalizeMetaState(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z4) {
        this.f870t = (z4 ? 1 : 0) | (this.f870t & (-2));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z4) {
        this.f870t = (z4 ? 2 : 0) | (this.f870t & (-3));
        return this;
    }

    @Override // android.view.MenuItem
    public s0.b setContentDescription(CharSequence charSequence) {
        this.f865n = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z4) {
        this.f870t = (z4 ? 16 : 0) | (this.f870t & (-17));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i8) {
        this.f862k = androidx.core.content.a.f(this.f863l, i8);
        c();
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f862k = drawable;
        c();
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f867p = colorStateList;
        this.f868r = true;
        c();
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.q = mode;
        this.f869s = true;
        c();
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f857f = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c9) {
        this.f858g = c9;
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setNumericShortcut(char c9, int i8) {
        this.f858g = c9;
        this.f859h = KeyEvent.normalizeMetaState(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f864m = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10) {
        this.f858g = c9;
        this.f860i = Character.toLowerCase(c10);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10, int i8, int i9) {
        this.f858g = c9;
        this.f859h = KeyEvent.normalizeMetaState(i8);
        this.f860i = Character.toLowerCase(c10);
        this.f861j = KeyEvent.normalizeMetaState(i9);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public void setShowAsAction(int i8) {
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i8) {
        this.f855d = this.f863l.getResources().getString(i8);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f855d = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f856e = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public s0.b setTooltipText(CharSequence charSequence) {
        this.f866o = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z4) {
        this.f870t = (this.f870t & 8) | (z4 ? 0 : 8);
        return this;
    }
}
