package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k implements p, m, AdapterView.OnItemClickListener {

    /* renamed from: a  reason: collision with root package name */
    private Rect f996a;

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean A(g gVar) {
        int size = gVar.size();
        for (int i8 = 0; i8 < size; i8++) {
            MenuItem item = gVar.getItem(i8);
            if (item.isVisible() && item.getIcon() != null) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static f B(ListAdapter listAdapter) {
        return listAdapter instanceof HeaderViewListAdapter ? (f) ((HeaderViewListAdapter) listAdapter).getWrappedAdapter() : (f) listAdapter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int r(ListAdapter listAdapter, ViewGroup viewGroup, Context context, int i8) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = listAdapter.getCount();
        int i9 = 0;
        int i10 = 0;
        View view = null;
        for (int i11 = 0; i11 < count; i11++) {
            int itemViewType = listAdapter.getItemViewType(i11);
            if (itemViewType != i10) {
                view = null;
                i10 = itemViewType;
            }
            if (viewGroup == null) {
                viewGroup = new FrameLayout(context);
            }
            view = listAdapter.getView(i11, view, viewGroup);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth >= i8) {
                return i8;
            }
            if (measuredWidth > i9) {
                i9 = measuredWidth;
            }
        }
        return i9;
    }

    public abstract void d(g gVar);

    @Override // androidx.appcompat.view.menu.m
    public int e() {
        return 0;
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
    public void k(Context context, g gVar) {
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
        ListAdapter listAdapter = (ListAdapter) adapterView.getAdapter();
        B(listAdapter).f924a.O((MenuItem) listAdapter.getItem(i8), this, p() ? 0 : 4);
    }

    protected boolean p() {
        return true;
    }

    public Rect q() {
        return this.f996a;
    }

    public abstract void s(View view);

    public void t(Rect rect) {
        this.f996a = rect;
    }

    public abstract void u(boolean z4);

    public abstract void v(int i8);

    public abstract void w(int i8);

    public abstract void x(PopupWindow.OnDismissListener onDismissListener);

    public abstract void y(boolean z4);

    public abstract void z(int i8);
}
