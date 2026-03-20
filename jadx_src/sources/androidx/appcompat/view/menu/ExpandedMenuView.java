package androidx.appcompat.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.widget.j0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ExpandedMenuView extends ListView implements g.b, n, AdapterView.OnItemClickListener {

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f833c = {16842964, 16843049};

    /* renamed from: a  reason: collision with root package name */
    private g f834a;

    /* renamed from: b  reason: collision with root package name */
    private int f835b;

    public ExpandedMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842868);
    }

    public ExpandedMenuView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet);
        setOnItemClickListener(this);
        j0 v8 = j0.v(context, attributeSet, f833c, i8, 0);
        if (v8.s(0)) {
            setBackgroundDrawable(v8.g(0));
        }
        if (v8.s(1)) {
            setDivider(v8.g(1));
        }
        v8.w();
    }

    @Override // androidx.appcompat.view.menu.g.b
    public boolean a(i iVar) {
        return this.f834a.N(iVar, 0);
    }

    @Override // androidx.appcompat.view.menu.n
    public void b(g gVar) {
        this.f834a = gVar;
    }

    public int getWindowAnimations() {
        return this.f835b;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setChildrenDrawingCacheEnabled(false);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i8, long j8) {
        a((i) getAdapter().getItem(i8));
    }
}
