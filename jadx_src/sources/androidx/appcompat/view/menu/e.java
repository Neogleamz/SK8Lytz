package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.view.menu.n;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e implements m, AdapterView.OnItemClickListener {

    /* renamed from: a  reason: collision with root package name */
    Context f912a;

    /* renamed from: b  reason: collision with root package name */
    LayoutInflater f913b;

    /* renamed from: c  reason: collision with root package name */
    g f914c;

    /* renamed from: d  reason: collision with root package name */
    ExpandedMenuView f915d;

    /* renamed from: e  reason: collision with root package name */
    int f916e;

    /* renamed from: f  reason: collision with root package name */
    int f917f;

    /* renamed from: g  reason: collision with root package name */
    int f918g;

    /* renamed from: h  reason: collision with root package name */
    private m.a f919h;

    /* renamed from: j  reason: collision with root package name */
    a f920j;

    /* renamed from: k  reason: collision with root package name */
    private int f921k;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends BaseAdapter {

        /* renamed from: a  reason: collision with root package name */
        private int f922a = -1;

        public a() {
            a();
        }

        void a() {
            i x8 = e.this.f914c.x();
            if (x8 != null) {
                ArrayList<i> B = e.this.f914c.B();
                int size = B.size();
                for (int i8 = 0; i8 < size; i8++) {
                    if (B.get(i8) == x8) {
                        this.f922a = i8;
                        return;
                    }
                }
            }
            this.f922a = -1;
        }

        @Override // android.widget.Adapter
        /* renamed from: b */
        public i getItem(int i8) {
            ArrayList<i> B = e.this.f914c.B();
            int i9 = i8 + e.this.f916e;
            int i10 = this.f922a;
            if (i10 >= 0 && i9 >= i10) {
                i9++;
            }
            return B.get(i9);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int size = e.this.f914c.B().size() - e.this.f916e;
            return this.f922a < 0 ? size : size - 1;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i8) {
            return i8;
        }

        @Override // android.widget.Adapter
        public View getView(int i8, View view, ViewGroup viewGroup) {
            if (view == null) {
                e eVar = e.this;
                view = eVar.f913b.inflate(eVar.f918g, viewGroup, false);
            }
            ((n.a) view).e(getItem(i8), 0);
            return view;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            a();
            super.notifyDataSetChanged();
        }
    }

    public e(int i8, int i9) {
        this.f918g = i8;
        this.f917f = i9;
    }

    public e(Context context, int i8) {
        this(i8, 0);
        this.f912a = context;
        this.f913b = LayoutInflater.from(context);
    }

    public ListAdapter a() {
        if (this.f920j == null) {
            this.f920j = new a();
        }
        return this.f920j;
    }

    public n b(ViewGroup viewGroup) {
        if (this.f915d == null) {
            this.f915d = (ExpandedMenuView) this.f913b.inflate(g.g.f19969i, viewGroup, false);
            if (this.f920j == null) {
                this.f920j = new a();
            }
            this.f915d.setAdapter((ListAdapter) this.f920j);
            this.f915d.setOnItemClickListener(this);
        }
        return this.f915d;
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(g gVar, boolean z4) {
        m.a aVar = this.f919h;
        if (aVar != null) {
            aVar.c(gVar, z4);
        }
    }

    public void d(Bundle bundle) {
        SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.f915d.restoreHierarchyState(sparseParcelableArray);
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public int e() {
        return this.f921k;
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        a aVar = this.f920j;
        if (aVar != null) {
            aVar.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean h(g gVar, i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean i(g gVar, i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public void j(m.a aVar) {
        this.f919h = aVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    @Override // androidx.appcompat.view.menu.m
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void k(android.content.Context r3, androidx.appcompat.view.menu.g r4) {
        /*
            r2 = this;
            int r0 = r2.f917f
            if (r0 == 0) goto L14
            android.view.ContextThemeWrapper r0 = new android.view.ContextThemeWrapper
            int r1 = r2.f917f
            r0.<init>(r3, r1)
            r2.f912a = r0
            android.view.LayoutInflater r3 = android.view.LayoutInflater.from(r0)
        L11:
            r2.f913b = r3
            goto L23
        L14:
            android.content.Context r0 = r2.f912a
            if (r0 == 0) goto L23
            r2.f912a = r3
            android.view.LayoutInflater r0 = r2.f913b
            if (r0 != 0) goto L23
            android.view.LayoutInflater r3 = android.view.LayoutInflater.from(r3)
            goto L11
        L23:
            r2.f914c = r4
            androidx.appcompat.view.menu.e$a r3 = r2.f920j
            if (r3 == 0) goto L2c
            r3.notifyDataSetChanged()
        L2c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.menu.e.k(android.content.Context, androidx.appcompat.view.menu.g):void");
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
        d((Bundle) parcelable);
    }

    public void m(Bundle bundle) {
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        ExpandedMenuView expandedMenuView = this.f915d;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray("android:menu:list", sparseArray);
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean n(r rVar) {
        if (rVar.hasVisibleItems()) {
            new h(rVar).b(null);
            m.a aVar = this.f919h;
            if (aVar != null) {
                aVar.d(rVar);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        if (this.f915d == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        m(bundle);
        return bundle;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
        this.f914c.O(this.f920j.getItem(i8), this, 0);
    }
}
