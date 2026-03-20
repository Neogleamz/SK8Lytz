package androidx.appcompat.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.n;
import androidx.core.view.b;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements s0.b {
    private View A;
    private androidx.core.view.b B;
    private MenuItem.OnActionExpandListener C;
    private ContextMenu.ContextMenuInfo E;

    /* renamed from: a  reason: collision with root package name */
    private final int f959a;

    /* renamed from: b  reason: collision with root package name */
    private final int f960b;

    /* renamed from: c  reason: collision with root package name */
    private final int f961c;

    /* renamed from: d  reason: collision with root package name */
    private final int f962d;

    /* renamed from: e  reason: collision with root package name */
    private CharSequence f963e;

    /* renamed from: f  reason: collision with root package name */
    private CharSequence f964f;

    /* renamed from: g  reason: collision with root package name */
    private Intent f965g;

    /* renamed from: h  reason: collision with root package name */
    private char f966h;

    /* renamed from: j  reason: collision with root package name */
    private char f968j;

    /* renamed from: l  reason: collision with root package name */
    private Drawable f970l;

    /* renamed from: n  reason: collision with root package name */
    g f972n;

    /* renamed from: o  reason: collision with root package name */
    private r f973o;

    /* renamed from: p  reason: collision with root package name */
    private Runnable f974p;
    private MenuItem.OnMenuItemClickListener q;

    /* renamed from: r  reason: collision with root package name */
    private CharSequence f975r;

    /* renamed from: s  reason: collision with root package name */
    private CharSequence f976s;

    /* renamed from: z  reason: collision with root package name */
    private int f983z;

    /* renamed from: i  reason: collision with root package name */
    private int f967i = RecognitionOptions.AZTEC;

    /* renamed from: k  reason: collision with root package name */
    private int f969k = RecognitionOptions.AZTEC;

    /* renamed from: m  reason: collision with root package name */
    private int f971m = 0;

    /* renamed from: t  reason: collision with root package name */
    private ColorStateList f977t = null;

    /* renamed from: u  reason: collision with root package name */
    private PorterDuff.Mode f978u = null;

    /* renamed from: v  reason: collision with root package name */
    private boolean f979v = false;

    /* renamed from: w  reason: collision with root package name */
    private boolean f980w = false;

    /* renamed from: x  reason: collision with root package name */
    private boolean f981x = false;

    /* renamed from: y  reason: collision with root package name */
    private int f982y = 16;
    private boolean D = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements b.InterfaceC0045b {
        a() {
        }

        @Override // androidx.core.view.b.InterfaceC0045b
        public void onActionProviderVisibilityChanged(boolean z4) {
            i iVar = i.this;
            iVar.f972n.L(iVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(g gVar, int i8, int i9, int i10, int i11, CharSequence charSequence, int i12) {
        this.f983z = 0;
        this.f972n = gVar;
        this.f959a = i9;
        this.f960b = i8;
        this.f961c = i10;
        this.f962d = i11;
        this.f963e = charSequence;
        this.f983z = i12;
    }

    private static void d(StringBuilder sb, int i8, int i9, String str) {
        if ((i8 & i9) == i9) {
            sb.append(str);
        }
    }

    private Drawable e(Drawable drawable) {
        if (drawable != null && this.f981x && (this.f979v || this.f980w)) {
            drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
            if (this.f979v) {
                androidx.core.graphics.drawable.a.o(drawable, this.f977t);
            }
            if (this.f980w) {
                androidx.core.graphics.drawable.a.p(drawable, this.f978u);
            }
            this.f981x = false;
        }
        return drawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean A() {
        return this.f972n.J() && g() != 0;
    }

    public boolean B() {
        return (this.f983z & 4) == 4;
    }

    @Override // s0.b
    public s0.b a(androidx.core.view.b bVar) {
        androidx.core.view.b bVar2 = this.B;
        if (bVar2 != null) {
            bVar2.h();
        }
        this.A = null;
        this.B = bVar;
        this.f972n.M(true);
        androidx.core.view.b bVar3 = this.B;
        if (bVar3 != null) {
            bVar3.j(new a());
        }
        return this;
    }

    @Override // s0.b
    public androidx.core.view.b b() {
        return this.B;
    }

    public void c() {
        this.f972n.K(this);
    }

    @Override // s0.b, android.view.MenuItem
    public boolean collapseActionView() {
        if ((this.f983z & 8) == 0) {
            return false;
        }
        if (this.A == null) {
            return true;
        }
        MenuItem.OnActionExpandListener onActionExpandListener = this.C;
        if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionCollapse(this)) {
            return this.f972n.f(this);
        }
        return false;
    }

    @Override // s0.b, android.view.MenuItem
    public boolean expandActionView() {
        if (j()) {
            MenuItem.OnActionExpandListener onActionExpandListener = this.C;
            if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionExpand(this)) {
                return this.f972n.m(this);
            }
            return false;
        }
        return false;
    }

    public int f() {
        return this.f962d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public char g() {
        return this.f972n.I() ? this.f968j : this.f966h;
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }

    @Override // s0.b, android.view.MenuItem
    public View getActionView() {
        View view = this.A;
        if (view != null) {
            return view;
        }
        androidx.core.view.b bVar = this.B;
        if (bVar != null) {
            View d8 = bVar.d(this);
            this.A = d8;
            return d8;
        }
        return null;
    }

    @Override // s0.b, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f969k;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f968j;
    }

    @Override // s0.b, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f975r;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f960b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        Drawable drawable = this.f970l;
        if (drawable != null) {
            return e(drawable);
        }
        if (this.f971m != 0) {
            Drawable b9 = h.a.b(this.f972n.w(), this.f971m);
            this.f971m = 0;
            this.f970l = b9;
            return e(b9);
        }
        return null;
    }

    @Override // s0.b, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f977t;
    }

    @Override // s0.b, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f978u;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f965g;
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public int getItemId() {
        return this.f959a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.E;
    }

    @Override // s0.b, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f967i;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f966h;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f961c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return this.f973o;
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public CharSequence getTitle() {
        return this.f963e;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f964f;
        if (charSequence == null) {
            charSequence = this.f963e;
        }
        return (Build.VERSION.SDK_INT >= 18 || charSequence == null || (charSequence instanceof String)) ? charSequence : charSequence.toString();
    }

    @Override // s0.b, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f976s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String h() {
        int i8;
        char g8 = g();
        if (g8 == 0) {
            return BuildConfig.FLAVOR;
        }
        Resources resources = this.f972n.w().getResources();
        StringBuilder sb = new StringBuilder();
        if (ViewConfiguration.get(this.f972n.w()).hasPermanentMenuKey()) {
            sb.append(resources.getString(g.h.f19995n));
        }
        int i9 = this.f972n.I() ? this.f969k : this.f967i;
        d(sb, i9, 65536, resources.getString(g.h.f19991j));
        d(sb, i9, RecognitionOptions.AZTEC, resources.getString(g.h.f19987f));
        d(sb, i9, 2, resources.getString(g.h.f19986e));
        d(sb, i9, 1, resources.getString(g.h.f19992k));
        d(sb, i9, 4, resources.getString(g.h.f19994m));
        d(sb, i9, 8, resources.getString(g.h.f19990i));
        if (g8 == '\b') {
            i8 = g.h.f19988g;
        } else if (g8 == '\n') {
            i8 = g.h.f19989h;
        } else if (g8 != ' ') {
            sb.append(g8);
            return sb.toString();
        } else {
            i8 = g.h.f19993l;
        }
        sb.append(resources.getString(i8));
        return sb.toString();
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return this.f973o != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence i(n.a aVar) {
        return (aVar == null || !aVar.d()) ? getTitle() : getTitleCondensed();
    }

    @Override // s0.b, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return this.D;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f982y & 1) == 1;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f982y & 2) == 2;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f982y & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        androidx.core.view.b bVar = this.B;
        return (bVar == null || !bVar.g()) ? (this.f982y & 8) == 0 : (this.f982y & 8) == 0 && this.B.b();
    }

    public boolean j() {
        androidx.core.view.b bVar;
        if ((this.f983z & 8) != 0) {
            if (this.A == null && (bVar = this.B) != null) {
                this.A = bVar.d(this);
            }
            return this.A != null;
        }
        return false;
    }

    public boolean k() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.q;
        if (onMenuItemClickListener == null || !onMenuItemClickListener.onMenuItemClick(this)) {
            g gVar = this.f972n;
            if (gVar.h(gVar, this)) {
                return true;
            }
            Runnable runnable = this.f974p;
            if (runnable != null) {
                runnable.run();
                return true;
            }
            if (this.f965g != null) {
                try {
                    this.f972n.w().startActivity(this.f965g);
                    return true;
                } catch (ActivityNotFoundException e8) {
                    Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", e8);
                }
            }
            androidx.core.view.b bVar = this.B;
            return bVar != null && bVar.e();
        }
        return true;
    }

    public boolean l() {
        return (this.f982y & 32) == 32;
    }

    public boolean m() {
        return (this.f982y & 4) != 0;
    }

    public boolean n() {
        return (this.f983z & 1) == 1;
    }

    public boolean o() {
        return (this.f983z & 2) == 2;
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: p */
    public s0.b setActionView(int i8) {
        Context w8 = this.f972n.w();
        setActionView(LayoutInflater.from(w8).inflate(i8, (ViewGroup) new LinearLayout(w8), false));
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: q */
    public s0.b setActionView(View view) {
        int i8;
        this.A = view;
        this.B = null;
        if (view != null && view.getId() == -1 && (i8 = this.f959a) > 0) {
            view.setId(i8);
        }
        this.f972n.K(this);
        return this;
    }

    public void r(boolean z4) {
        this.D = z4;
        this.f972n.M(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(boolean z4) {
        int i8 = this.f982y;
        int i9 = (z4 ? 2 : 0) | (i8 & (-3));
        this.f982y = i9;
        if (i8 != i9) {
            this.f972n.M(false);
        }
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9) {
        if (this.f968j == c9) {
            return this;
        }
        this.f968j = Character.toLowerCase(c9);
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c9, int i8) {
        if (this.f968j == c9 && this.f969k == i8) {
            return this;
        }
        this.f968j = Character.toLowerCase(c9);
        this.f969k = KeyEvent.normalizeMetaState(i8);
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z4) {
        int i8 = this.f982y;
        int i9 = (z4 ? 1 : 0) | (i8 & (-2));
        this.f982y = i9;
        if (i8 != i9) {
            this.f972n.M(false);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z4) {
        if ((this.f982y & 4) != 0) {
            this.f972n.X(this);
        } else {
            s(z4);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public s0.b setContentDescription(CharSequence charSequence) {
        this.f975r = charSequence;
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z4) {
        this.f982y = z4 ? this.f982y | 16 : this.f982y & (-17);
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i8) {
        this.f970l = null;
        this.f971m = i8;
        this.f981x = true;
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f971m = 0;
        this.f970l = drawable;
        this.f981x = true;
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f977t = colorStateList;
        this.f979v = true;
        this.f981x = true;
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f978u = mode;
        this.f980w = true;
        this.f981x = true;
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f965g = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c9) {
        if (this.f966h == c9) {
            return this;
        }
        this.f966h = c9;
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setNumericShortcut(char c9, int i8) {
        if (this.f966h == c9 && this.f967i == i8) {
            return this;
        }
        this.f966h = c9;
        this.f967i = KeyEvent.normalizeMetaState(i8);
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        this.C = onActionExpandListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.q = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10) {
        this.f966h = c9;
        this.f968j = Character.toLowerCase(c10);
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public MenuItem setShortcut(char c9, char c10, int i8, int i9) {
        this.f966h = c9;
        this.f967i = KeyEvent.normalizeMetaState(i8);
        this.f968j = Character.toLowerCase(c10);
        this.f969k = KeyEvent.normalizeMetaState(i9);
        this.f972n.M(false);
        return this;
    }

    @Override // s0.b, android.view.MenuItem
    public void setShowAsAction(int i8) {
        int i9 = i8 & 3;
        if (i9 != 0 && i9 != 1 && i9 != 2) {
            throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        this.f983z = i8;
        this.f972n.K(this);
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i8) {
        return setTitle(this.f972n.w().getString(i8));
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f963e = charSequence;
        this.f972n.M(false);
        r rVar = this.f973o;
        if (rVar != null) {
            rVar.setHeaderTitle(charSequence);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f964f = charSequence;
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public s0.b setTooltipText(CharSequence charSequence) {
        this.f976s = charSequence;
        this.f972n.M(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z4) {
        if (y(z4)) {
            this.f972n.L(this);
        }
        return this;
    }

    public void t(boolean z4) {
        this.f982y = (z4 ? 4 : 0) | (this.f982y & (-5));
    }

    public String toString() {
        CharSequence charSequence = this.f963e;
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    public void u(boolean z4) {
        this.f982y = z4 ? this.f982y | 32 : this.f982y & (-33);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.E = contextMenuInfo;
    }

    @Override // s0.b, android.view.MenuItem
    /* renamed from: w */
    public s0.b setShowAsActionFlags(int i8) {
        setShowAsAction(i8);
        return this;
    }

    public void x(r rVar) {
        this.f973o = rVar;
        rVar.setHeaderTitle(getTitle());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean y(boolean z4) {
        int i8 = this.f982y;
        int i9 = (z4 ? 0 : 8) | (i8 & (-9));
        this.f982y = i9;
        return i8 != i9;
    }

    public boolean z() {
        return this.f972n.C();
    }
}
