package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.view.menu.n;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b implements m {

    /* renamed from: a  reason: collision with root package name */
    protected Context f871a;

    /* renamed from: b  reason: collision with root package name */
    protected Context f872b;

    /* renamed from: c  reason: collision with root package name */
    protected g f873c;

    /* renamed from: d  reason: collision with root package name */
    protected LayoutInflater f874d;

    /* renamed from: e  reason: collision with root package name */
    protected LayoutInflater f875e;

    /* renamed from: f  reason: collision with root package name */
    private m.a f876f;

    /* renamed from: g  reason: collision with root package name */
    private int f877g;

    /* renamed from: h  reason: collision with root package name */
    private int f878h;

    /* renamed from: j  reason: collision with root package name */
    protected n f879j;

    /* renamed from: k  reason: collision with root package name */
    private int f880k;

    public b(Context context, int i8, int i9) {
        this.f871a = context;
        this.f874d = LayoutInflater.from(context);
        this.f877g = i8;
        this.f878h = i9;
    }

    protected void b(View view, int i8) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        ((ViewGroup) this.f879j).addView(view, i8);
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(g gVar, boolean z4) {
        m.a aVar = this.f876f;
        if (aVar != null) {
            aVar.c(gVar, z4);
        }
    }

    public abstract void d(i iVar, n.a aVar);

    @Override // androidx.appcompat.view.menu.m
    public int e() {
        return this.f880k;
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        ViewGroup viewGroup = (ViewGroup) this.f879j;
        if (viewGroup == null) {
            return;
        }
        g gVar = this.f873c;
        int i8 = 0;
        if (gVar != null) {
            gVar.t();
            ArrayList<i> G = this.f873c.G();
            int size = G.size();
            int i9 = 0;
            for (int i10 = 0; i10 < size; i10++) {
                i iVar = G.get(i10);
                if (u(i9, iVar)) {
                    View childAt = viewGroup.getChildAt(i9);
                    i itemData = childAt instanceof n.a ? ((n.a) childAt).getItemData() : null;
                    View r4 = r(iVar, childAt, viewGroup);
                    if (iVar != itemData) {
                        r4.setPressed(false);
                        r4.jumpDrawablesToCurrentState();
                    }
                    if (r4 != childAt) {
                        b(r4, i9);
                    }
                    i9++;
                }
            }
            i8 = i9;
        }
        while (i8 < viewGroup.getChildCount()) {
            if (!p(viewGroup, i8)) {
                i8++;
            }
        }
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
        this.f876f = aVar;
    }

    @Override // androidx.appcompat.view.menu.m
    public void k(Context context, g gVar) {
        this.f872b = context;
        this.f875e = LayoutInflater.from(context);
        this.f873c = gVar;
    }

    public n.a m(ViewGroup viewGroup) {
        return (n.a) this.f874d.inflate(this.f878h, viewGroup, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [androidx.appcompat.view.menu.g] */
    @Override // androidx.appcompat.view.menu.m
    public boolean n(r rVar) {
        m.a aVar = this.f876f;
        r rVar2 = rVar;
        if (aVar != null) {
            if (rVar == null) {
                rVar2 = this.f873c;
            }
            return aVar.d(rVar2);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean p(ViewGroup viewGroup, int i8) {
        viewGroup.removeViewAt(i8);
        return true;
    }

    public m.a q() {
        return this.f876f;
    }

    public View r(i iVar, View view, ViewGroup viewGroup) {
        n.a m8 = view instanceof n.a ? (n.a) view : m(viewGroup);
        d(iVar, m8);
        return (View) m8;
    }

    public n s(ViewGroup viewGroup) {
        if (this.f879j == null) {
            n nVar = (n) this.f874d.inflate(this.f877g, viewGroup, false);
            this.f879j = nVar;
            nVar.b(this.f873c);
            f(true);
        }
        return this.f879j;
    }

    public void t(int i8) {
        this.f880k = i8;
    }

    public abstract boolean u(int i8, i iVar);
}
