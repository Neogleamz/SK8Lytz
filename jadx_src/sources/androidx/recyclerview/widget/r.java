package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.b0;
import androidx.recyclerview.widget.c;
import androidx.recyclerview.widget.d;
import androidx.recyclerview.widget.h;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class r<T, VH extends RecyclerView.b0> extends RecyclerView.g<VH> {

    /* renamed from: c  reason: collision with root package name */
    final d<T> f7023c;

    /* renamed from: d  reason: collision with root package name */
    private final d.b<T> f7024d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements d.b<T> {
        a() {
        }

        @Override // androidx.recyclerview.widget.d.b
        public void a(List<T> list, List<T> list2) {
            r.this.D(list, list2);
        }
    }

    protected r(h.d<T> dVar) {
        a aVar = new a();
        this.f7024d = aVar;
        d<T> dVar2 = new d<>(new b(this), new c.a(dVar).a());
        this.f7023c = dVar2;
        dVar2.a(aVar);
    }

    protected T C(int i8) {
        return this.f7023c.b().get(i8);
    }

    public void D(List<T> list, List<T> list2) {
    }

    public void E(List<T> list) {
        this.f7023c.e(list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public int c() {
        return this.f7023c.b().size();
    }
}
