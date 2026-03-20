package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.e0;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements s0.a {
    private static final int[] A = {1, 4, 5, 3, 2, 0};

    /* renamed from: a  reason: collision with root package name */
    private final Context f930a;

    /* renamed from: b  reason: collision with root package name */
    private final Resources f931b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f932c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f933d;

    /* renamed from: e  reason: collision with root package name */
    private a f934e;

    /* renamed from: m  reason: collision with root package name */
    private ContextMenu.ContextMenuInfo f942m;

    /* renamed from: n  reason: collision with root package name */
    CharSequence f943n;

    /* renamed from: o  reason: collision with root package name */
    Drawable f944o;

    /* renamed from: p  reason: collision with root package name */
    View f945p;

    /* renamed from: x  reason: collision with root package name */
    private i f952x;

    /* renamed from: z  reason: collision with root package name */
    private boolean f954z;

    /* renamed from: l  reason: collision with root package name */
    private int f941l = 0;
    private boolean q = false;

    /* renamed from: r  reason: collision with root package name */
    private boolean f946r = false;

    /* renamed from: s  reason: collision with root package name */
    private boolean f947s = false;

    /* renamed from: t  reason: collision with root package name */
    private boolean f948t = false;

    /* renamed from: u  reason: collision with root package name */
    private boolean f949u = false;

    /* renamed from: v  reason: collision with root package name */
    private ArrayList<i> f950v = new ArrayList<>();

    /* renamed from: w  reason: collision with root package name */
    private CopyOnWriteArrayList<WeakReference<m>> f951w = new CopyOnWriteArrayList<>();

    /* renamed from: y  reason: collision with root package name */
    private boolean f953y = false;

    /* renamed from: f  reason: collision with root package name */
    private ArrayList<i> f935f = new ArrayList<>();

    /* renamed from: g  reason: collision with root package name */
    private ArrayList<i> f936g = new ArrayList<>();

    /* renamed from: h  reason: collision with root package name */
    private boolean f937h = true;

    /* renamed from: i  reason: collision with root package name */
    private ArrayList<i> f938i = new ArrayList<>();

    /* renamed from: j  reason: collision with root package name */
    private ArrayList<i> f939j = new ArrayList<>();

    /* renamed from: k  reason: collision with root package name */
    private boolean f940k = true;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        boolean a(g gVar, MenuItem menuItem);

        void b(g gVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        boolean a(i iVar);
    }

    public g(Context context) {
        this.f930a = context;
        this.f931b = context.getResources();
        f0(true);
    }

    private static int D(int i8) {
        int i9 = ((-65536) & i8) >> 16;
        if (i9 >= 0) {
            int[] iArr = A;
            if (i9 < iArr.length) {
                return (i8 & 65535) | (iArr[i9] << 16);
            }
        }
        throw new IllegalArgumentException("order does not contain a valid category.");
    }

    private void P(int i8, boolean z4) {
        if (i8 < 0 || i8 >= this.f935f.size()) {
            return;
        }
        this.f935f.remove(i8);
        if (z4) {
            M(true);
        }
    }

    private void a0(int i8, CharSequence charSequence, int i9, Drawable drawable, View view) {
        Resources E = E();
        if (view != null) {
            this.f945p = view;
            this.f943n = null;
            this.f944o = null;
        } else {
            if (i8 > 0) {
                this.f943n = E.getText(i8);
            } else if (charSequence != null) {
                this.f943n = charSequence;
            }
            if (i9 > 0) {
                this.f944o = androidx.core.content.a.f(w(), i9);
            } else if (drawable != null) {
                this.f944o = drawable;
            }
            this.f945p = null;
        }
        M(false);
    }

    private void f0(boolean z4) {
        boolean z8 = true;
        this.f933d = (z4 && this.f931b.getConfiguration().keyboard != 1 && e0.e(ViewConfiguration.get(this.f930a), this.f930a)) ? false : false;
    }

    private i g(int i8, int i9, int i10, int i11, CharSequence charSequence, int i12) {
        return new i(this, i8, i9, i10, i11, charSequence, i12);
    }

    private void i(boolean z4) {
        if (this.f951w.isEmpty()) {
            return;
        }
        h0();
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar = next.get();
            if (mVar == null) {
                this.f951w.remove(next);
            } else {
                mVar.f(z4);
            }
        }
        g0();
    }

    private void j(Bundle bundle) {
        Parcelable parcelable;
        SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:presenters");
        if (sparseParcelableArray == null || this.f951w.isEmpty()) {
            return;
        }
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar = next.get();
            if (mVar == null) {
                this.f951w.remove(next);
            } else {
                int e8 = mVar.e();
                if (e8 > 0 && (parcelable = (Parcelable) sparseParcelableArray.get(e8)) != null) {
                    mVar.l(parcelable);
                }
            }
        }
    }

    private void k(Bundle bundle) {
        Parcelable o5;
        if (this.f951w.isEmpty()) {
            return;
        }
        SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar = next.get();
            if (mVar == null) {
                this.f951w.remove(next);
            } else {
                int e8 = mVar.e();
                if (e8 > 0 && (o5 = mVar.o()) != null) {
                    sparseArray.put(e8, o5);
                }
            }
        }
        bundle.putSparseParcelableArray("android:menu:presenters", sparseArray);
    }

    private boolean l(r rVar, m mVar) {
        if (this.f951w.isEmpty()) {
            return false;
        }
        boolean n8 = mVar != null ? mVar.n(rVar) : false;
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar2 = next.get();
            if (mVar2 == null) {
                this.f951w.remove(next);
            } else if (!n8) {
                n8 = mVar2.n(rVar);
            }
        }
        return n8;
    }

    private static int p(ArrayList<i> arrayList, int i8) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size).f() <= i8) {
                return size + 1;
            }
        }
        return 0;
    }

    public View A() {
        return this.f945p;
    }

    public ArrayList<i> B() {
        t();
        return this.f939j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean C() {
        return this.f948t;
    }

    Resources E() {
        return this.f931b;
    }

    public g F() {
        return this;
    }

    public ArrayList<i> G() {
        if (this.f937h) {
            this.f936g.clear();
            int size = this.f935f.size();
            for (int i8 = 0; i8 < size; i8++) {
                i iVar = this.f935f.get(i8);
                if (iVar.isVisible()) {
                    this.f936g.add(iVar);
                }
            }
            this.f937h = false;
            this.f940k = true;
            return this.f936g;
        }
        return this.f936g;
    }

    public boolean H() {
        return this.f953y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean I() {
        return this.f932c;
    }

    public boolean J() {
        return this.f933d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(i iVar) {
        this.f940k = true;
        M(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(i iVar) {
        this.f937h = true;
        M(true);
    }

    public void M(boolean z4) {
        if (this.q) {
            this.f946r = true;
            if (z4) {
                this.f947s = true;
                return;
            }
            return;
        }
        if (z4) {
            this.f937h = true;
            this.f940k = true;
        }
        i(z4);
    }

    public boolean N(MenuItem menuItem, int i8) {
        return O(menuItem, null, i8);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002b, code lost:
        if (r1 != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002d, code lost:
        e(true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003c, code lost:
        if ((r9 & 1) == 0) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0068, code lost:
        if (r1 == false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x006b, code lost:
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean O(android.view.MenuItem r7, androidx.appcompat.view.menu.m r8, int r9) {
        /*
            r6 = this;
            androidx.appcompat.view.menu.i r7 = (androidx.appcompat.view.menu.i) r7
            r0 = 0
            if (r7 == 0) goto L6c
            boolean r1 = r7.isEnabled()
            if (r1 != 0) goto Lc
            goto L6c
        Lc:
            boolean r1 = r7.k()
            androidx.core.view.b r2 = r7.b()
            r3 = 1
            if (r2 == 0) goto L1f
            boolean r4 = r2.a()
            if (r4 == 0) goto L1f
            r4 = r3
            goto L20
        L1f:
            r4 = r0
        L20:
            boolean r5 = r7.j()
            if (r5 == 0) goto L31
            boolean r7 = r7.expandActionView()
            r1 = r1 | r7
            if (r1 == 0) goto L6b
        L2d:
            r6.e(r3)
            goto L6b
        L31:
            boolean r5 = r7.hasSubMenu()
            if (r5 != 0) goto L3f
            if (r4 == 0) goto L3a
            goto L3f
        L3a:
            r7 = r9 & 1
            if (r7 != 0) goto L6b
            goto L2d
        L3f:
            r9 = r9 & 4
            if (r9 != 0) goto L46
            r6.e(r0)
        L46:
            boolean r9 = r7.hasSubMenu()
            if (r9 != 0) goto L58
            androidx.appcompat.view.menu.r r9 = new androidx.appcompat.view.menu.r
            android.content.Context r0 = r6.w()
            r9.<init>(r0, r6, r7)
            r7.x(r9)
        L58:
            android.view.SubMenu r7 = r7.getSubMenu()
            androidx.appcompat.view.menu.r r7 = (androidx.appcompat.view.menu.r) r7
            if (r4 == 0) goto L63
            r2.f(r7)
        L63:
            boolean r7 = r6.l(r7, r8)
            r1 = r1 | r7
            if (r1 != 0) goto L6b
            goto L2d
        L6b:
            return r1
        L6c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.menu.g.O(android.view.MenuItem, androidx.appcompat.view.menu.m, int):boolean");
    }

    public void Q(m mVar) {
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar2 = next.get();
            if (mVar2 == null || mVar2 == mVar) {
                this.f951w.remove(next);
            }
        }
    }

    public void R(Bundle bundle) {
        MenuItem findItem;
        if (bundle == null) {
            return;
        }
        SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray(v());
        int size = size();
        for (int i8 = 0; i8 < size; i8++) {
            MenuItem item = getItem(i8);
            View actionView = item.getActionView();
            if (actionView != null && actionView.getId() != -1) {
                actionView.restoreHierarchyState(sparseParcelableArray);
            }
            if (item.hasSubMenu()) {
                ((r) item.getSubMenu()).R(bundle);
            }
        }
        int i9 = bundle.getInt("android:menu:expandedactionview");
        if (i9 <= 0 || (findItem = findItem(i9)) == null) {
            return;
        }
        findItem.expandActionView();
    }

    public void S(Bundle bundle) {
        j(bundle);
    }

    public void T(Bundle bundle) {
        int size = size();
        SparseArray<? extends Parcelable> sparseArray = null;
        for (int i8 = 0; i8 < size; i8++) {
            MenuItem item = getItem(i8);
            View actionView = item.getActionView();
            if (actionView != null && actionView.getId() != -1) {
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                }
                actionView.saveHierarchyState(sparseArray);
                if (item.isActionViewExpanded()) {
                    bundle.putInt("android:menu:expandedactionview", item.getItemId());
                }
            }
            if (item.hasSubMenu()) {
                ((r) item.getSubMenu()).T(bundle);
            }
        }
        if (sparseArray != null) {
            bundle.putSparseParcelableArray(v(), sparseArray);
        }
    }

    public void U(Bundle bundle) {
        k(bundle);
    }

    public void V(a aVar) {
        this.f934e = aVar;
    }

    public g W(int i8) {
        this.f941l = i8;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X(MenuItem menuItem) {
        int groupId = menuItem.getGroupId();
        int size = this.f935f.size();
        h0();
        for (int i8 = 0; i8 < size; i8++) {
            i iVar = this.f935f.get(i8);
            if (iVar.getGroupId() == groupId && iVar.m() && iVar.isCheckable()) {
                iVar.s(iVar == menuItem);
            }
        }
        g0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g Y(int i8) {
        a0(0, null, i8, null, null);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g Z(Drawable drawable) {
        a0(0, null, 0, drawable, null);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MenuItem a(int i8, int i9, int i10, CharSequence charSequence) {
        int D = D(i10);
        i g8 = g(i8, i9, i10, D, charSequence, this.f941l);
        ContextMenu.ContextMenuInfo contextMenuInfo = this.f942m;
        if (contextMenuInfo != null) {
            g8.v(contextMenuInfo);
        }
        ArrayList<i> arrayList = this.f935f;
        arrayList.add(p(arrayList, D), g8);
        M(true);
        return g8;
    }

    @Override // android.view.Menu
    public MenuItem add(int i8) {
        return a(0, 0, 0, this.f931b.getString(i8));
    }

    @Override // android.view.Menu
    public MenuItem add(int i8, int i9, int i10, int i11) {
        return a(i8, i9, i10, this.f931b.getString(i11));
    }

    @Override // android.view.Menu
    public MenuItem add(int i8, int i9, int i10, CharSequence charSequence) {
        return a(i8, i9, i10, charSequence);
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return a(0, 0, 0, charSequence);
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i8, int i9, int i10, ComponentName componentName, Intent[] intentArr, Intent intent, int i11, MenuItem[] menuItemArr) {
        int i12;
        PackageManager packageManager = this.f930a.getPackageManager();
        List<ResolveInfo> queryIntentActivityOptions = packageManager.queryIntentActivityOptions(componentName, intentArr, intent, 0);
        int size = queryIntentActivityOptions != null ? queryIntentActivityOptions.size() : 0;
        if ((i11 & 1) == 0) {
            removeGroup(i8);
        }
        for (int i13 = 0; i13 < size; i13++) {
            ResolveInfo resolveInfo = queryIntentActivityOptions.get(i13);
            int i14 = resolveInfo.specificIndex;
            Intent intent2 = new Intent(i14 < 0 ? intent : intentArr[i14]);
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            intent2.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
            MenuItem intent3 = add(i8, i9, i10, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent2);
            if (menuItemArr != null && (i12 = resolveInfo.specificIndex) >= 0) {
                menuItemArr[i12] = intent3;
            }
        }
        return size;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8) {
        return addSubMenu(0, 0, 0, this.f931b.getString(i8));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, int i11) {
        return addSubMenu(i8, i9, i10, this.f931b.getString(i11));
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i8, int i9, int i10, CharSequence charSequence) {
        i iVar = (i) a(i8, i9, i10, charSequence);
        r rVar = new r(this.f930a, this, iVar);
        iVar.x(rVar);
        return rVar;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        return addSubMenu(0, 0, 0, charSequence);
    }

    public void b(m mVar) {
        c(mVar, this.f930a);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g b0(int i8) {
        a0(i8, null, 0, null, null);
        return this;
    }

    public void c(m mVar, Context context) {
        this.f951w.add(new WeakReference<>(mVar));
        mVar.k(context, this);
        this.f940k = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g c0(CharSequence charSequence) {
        a0(0, charSequence, 0, null, null);
        return this;
    }

    @Override // android.view.Menu
    public void clear() {
        i iVar = this.f952x;
        if (iVar != null) {
            f(iVar);
        }
        this.f935f.clear();
        M(true);
    }

    public void clearHeader() {
        this.f944o = null;
        this.f943n = null;
        this.f945p = null;
        M(false);
    }

    @Override // android.view.Menu
    public void close() {
        e(true);
    }

    public void d() {
        a aVar = this.f934e;
        if (aVar != null) {
            aVar.b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g d0(View view) {
        a0(0, null, 0, null, view);
        return this;
    }

    public final void e(boolean z4) {
        if (this.f949u) {
            return;
        }
        this.f949u = true;
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar = next.get();
            if (mVar == null) {
                this.f951w.remove(next);
            } else {
                mVar.c(this, z4);
            }
        }
        this.f949u = false;
    }

    public void e0(boolean z4) {
        this.f954z = z4;
    }

    public boolean f(i iVar) {
        boolean z4 = false;
        if (!this.f951w.isEmpty() && this.f952x == iVar) {
            h0();
            Iterator<WeakReference<m>> it = this.f951w.iterator();
            while (it.hasNext()) {
                WeakReference<m> next = it.next();
                m mVar = next.get();
                if (mVar == null) {
                    this.f951w.remove(next);
                } else {
                    z4 = mVar.h(this, iVar);
                    if (z4) {
                        break;
                    }
                }
            }
            g0();
            if (z4) {
                this.f952x = null;
            }
        }
        return z4;
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i8) {
        MenuItem findItem;
        int size = size();
        for (int i9 = 0; i9 < size; i9++) {
            i iVar = this.f935f.get(i9);
            if (iVar.getItemId() == i8) {
                return iVar;
            }
            if (iVar.hasSubMenu() && (findItem = iVar.getSubMenu().findItem(i8)) != null) {
                return findItem;
            }
        }
        return null;
    }

    public void g0() {
        this.q = false;
        if (this.f946r) {
            this.f946r = false;
            M(this.f947s);
        }
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i8) {
        return this.f935f.get(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h(g gVar, MenuItem menuItem) {
        a aVar = this.f934e;
        return aVar != null && aVar.a(gVar, menuItem);
    }

    public void h0() {
        if (this.q) {
            return;
        }
        this.q = true;
        this.f946r = false;
        this.f947s = false;
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        if (this.f954z) {
            return true;
        }
        int size = size();
        for (int i8 = 0; i8 < size; i8++) {
            if (this.f935f.get(i8).isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i8, KeyEvent keyEvent) {
        return r(i8, keyEvent) != null;
    }

    public boolean m(i iVar) {
        boolean z4 = false;
        if (this.f951w.isEmpty()) {
            return false;
        }
        h0();
        Iterator<WeakReference<m>> it = this.f951w.iterator();
        while (it.hasNext()) {
            WeakReference<m> next = it.next();
            m mVar = next.get();
            if (mVar == null) {
                this.f951w.remove(next);
            } else {
                z4 = mVar.i(this, iVar);
                if (z4) {
                    break;
                }
            }
        }
        g0();
        if (z4) {
            this.f952x = iVar;
        }
        return z4;
    }

    public int n(int i8) {
        return o(i8, 0);
    }

    public int o(int i8, int i9) {
        int size = size();
        if (i9 < 0) {
            i9 = 0;
        }
        while (i9 < size) {
            if (this.f935f.get(i9).getGroupId() == i8) {
                return i9;
            }
            i9++;
        }
        return -1;
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i8, int i9) {
        return N(findItem(i8), i9);
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i8, KeyEvent keyEvent, int i9) {
        i r4 = r(i8, keyEvent);
        boolean N = r4 != null ? N(r4, i9) : false;
        if ((i9 & 2) != 0) {
            e(true);
        }
        return N;
    }

    public int q(int i8) {
        int size = size();
        for (int i9 = 0; i9 < size; i9++) {
            if (this.f935f.get(i9).getItemId() == i8) {
                return i9;
            }
        }
        return -1;
    }

    i r(int i8, KeyEvent keyEvent) {
        ArrayList<i> arrayList = this.f950v;
        arrayList.clear();
        s(arrayList, i8, keyEvent);
        if (arrayList.isEmpty()) {
            return null;
        }
        int metaState = keyEvent.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        keyEvent.getKeyData(keyData);
        int size = arrayList.size();
        if (size == 1) {
            return arrayList.get(0);
        }
        boolean I = I();
        for (int i9 = 0; i9 < size; i9++) {
            i iVar = arrayList.get(i9);
            char alphabeticShortcut = I ? iVar.getAlphabeticShortcut() : iVar.getNumericShortcut();
            char[] cArr = keyData.meta;
            if ((alphabeticShortcut == cArr[0] && (metaState & 2) == 0) || ((alphabeticShortcut == cArr[2] && (metaState & 2) != 0) || (I && alphabeticShortcut == '\b' && i8 == 67))) {
                return iVar;
            }
        }
        return null;
    }

    @Override // android.view.Menu
    public void removeGroup(int i8) {
        int n8 = n(i8);
        if (n8 >= 0) {
            int size = this.f935f.size() - n8;
            int i9 = 0;
            while (true) {
                int i10 = i9 + 1;
                if (i9 >= size || this.f935f.get(n8).getGroupId() != i8) {
                    break;
                }
                P(n8, false);
                i9 = i10;
            }
            M(true);
        }
    }

    @Override // android.view.Menu
    public void removeItem(int i8) {
        P(q(i8), true);
    }

    void s(List<i> list, int i8, KeyEvent keyEvent) {
        boolean I = I();
        int modifiers = keyEvent.getModifiers();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        if (keyEvent.getKeyData(keyData) || i8 == 67) {
            int size = this.f935f.size();
            for (int i9 = 0; i9 < size; i9++) {
                i iVar = this.f935f.get(i9);
                if (iVar.hasSubMenu()) {
                    ((g) iVar.getSubMenu()).s(list, i8, keyEvent);
                }
                char alphabeticShortcut = I ? iVar.getAlphabeticShortcut() : iVar.getNumericShortcut();
                if (((modifiers & 69647) == ((I ? iVar.getAlphabeticModifiers() : iVar.getNumericModifiers()) & 69647)) && alphabeticShortcut != 0) {
                    char[] cArr = keyData.meta;
                    if ((alphabeticShortcut == cArr[0] || alphabeticShortcut == cArr[2] || (I && alphabeticShortcut == '\b' && i8 == 67)) && iVar.isEnabled()) {
                        list.add(iVar);
                    }
                }
            }
        }
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i8, boolean z4, boolean z8) {
        int size = this.f935f.size();
        for (int i9 = 0; i9 < size; i9++) {
            i iVar = this.f935f.get(i9);
            if (iVar.getGroupId() == i8) {
                iVar.t(z8);
                iVar.setCheckable(z4);
            }
        }
    }

    @Override // android.view.Menu
    public void setGroupDividerEnabled(boolean z4) {
        this.f953y = z4;
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i8, boolean z4) {
        int size = this.f935f.size();
        for (int i9 = 0; i9 < size; i9++) {
            i iVar = this.f935f.get(i9);
            if (iVar.getGroupId() == i8) {
                iVar.setEnabled(z4);
            }
        }
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i8, boolean z4) {
        int size = this.f935f.size();
        boolean z8 = false;
        for (int i9 = 0; i9 < size; i9++) {
            i iVar = this.f935f.get(i9);
            if (iVar.getGroupId() == i8 && iVar.y(z4)) {
                z8 = true;
            }
        }
        if (z8) {
            M(true);
        }
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z4) {
        this.f932c = z4;
        M(false);
    }

    @Override // android.view.Menu
    public int size() {
        return this.f935f.size();
    }

    public void t() {
        ArrayList<i> G = G();
        if (this.f940k) {
            Iterator<WeakReference<m>> it = this.f951w.iterator();
            boolean z4 = false;
            while (it.hasNext()) {
                WeakReference<m> next = it.next();
                m mVar = next.get();
                if (mVar == null) {
                    this.f951w.remove(next);
                } else {
                    z4 |= mVar.g();
                }
            }
            if (z4) {
                this.f938i.clear();
                this.f939j.clear();
                int size = G.size();
                for (int i8 = 0; i8 < size; i8++) {
                    i iVar = G.get(i8);
                    (iVar.l() ? this.f938i : this.f939j).add(iVar);
                }
            } else {
                this.f938i.clear();
                this.f939j.clear();
                this.f939j.addAll(G());
            }
            this.f940k = false;
        }
    }

    public ArrayList<i> u() {
        t();
        return this.f938i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String v() {
        return "android:menu:actionviewstates";
    }

    public Context w() {
        return this.f930a;
    }

    public i x() {
        return this.f952x;
    }

    public Drawable y() {
        return this.f944o;
    }

    public CharSequence z() {
        return this.f943n;
    }
}
