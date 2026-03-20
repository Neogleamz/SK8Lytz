package androidx.appcompat.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.appcompat.view.menu.n;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends BaseAdapter {

    /* renamed from: a  reason: collision with root package name */
    g f924a;

    /* renamed from: b  reason: collision with root package name */
    private int f925b = -1;

    /* renamed from: c  reason: collision with root package name */
    private boolean f926c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f927d;

    /* renamed from: e  reason: collision with root package name */
    private final LayoutInflater f928e;

    /* renamed from: f  reason: collision with root package name */
    private final int f929f;

    public f(g gVar, LayoutInflater layoutInflater, boolean z4, int i8) {
        this.f927d = z4;
        this.f928e = layoutInflater;
        this.f924a = gVar;
        this.f929f = i8;
        a();
    }

    void a() {
        i x8 = this.f924a.x();
        if (x8 != null) {
            ArrayList<i> B = this.f924a.B();
            int size = B.size();
            for (int i8 = 0; i8 < size; i8++) {
                if (B.get(i8) == x8) {
                    this.f925b = i8;
                    return;
                }
            }
        }
        this.f925b = -1;
    }

    public g b() {
        return this.f924a;
    }

    @Override // android.widget.Adapter
    /* renamed from: c */
    public i getItem(int i8) {
        ArrayList<i> B = this.f927d ? this.f924a.B() : this.f924a.G();
        int i9 = this.f925b;
        if (i9 >= 0 && i8 >= i9) {
            i8++;
        }
        return B.get(i8);
    }

    public void d(boolean z4) {
        this.f926c = z4;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        ArrayList<i> B = this.f927d ? this.f924a.B() : this.f924a.G();
        int i8 = this.f925b;
        int size = B.size();
        return i8 < 0 ? size : size - 1;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i8) {
        return i8;
    }

    @Override // android.widget.Adapter
    public View getView(int i8, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.f928e.inflate(this.f929f, viewGroup, false);
        }
        int groupId = getItem(i8).getGroupId();
        int i9 = i8 - 1;
        ListMenuItemView listMenuItemView = (ListMenuItemView) view;
        listMenuItemView.setGroupDividerEnabled(this.f924a.H() && groupId != (i9 >= 0 ? getItem(i9).getGroupId() : groupId));
        n.a aVar = (n.a) view;
        if (this.f926c) {
            listMenuItemView.setForceShowIcon(true);
        }
        aVar.e(getItem(i8), 0);
        return view;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        a();
        super.notifyDataSetChanged();
    }
}
