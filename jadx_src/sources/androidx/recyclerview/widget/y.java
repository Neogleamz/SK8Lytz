package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class y extends RecyclerView.l {

    /* renamed from: g  reason: collision with root package name */
    boolean f7036g = true;

    public abstract boolean A(RecyclerView.b0 b0Var);

    public final void B(RecyclerView.b0 b0Var) {
        J(b0Var);
        h(b0Var);
    }

    public final void C(RecyclerView.b0 b0Var) {
        K(b0Var);
    }

    public final void D(RecyclerView.b0 b0Var, boolean z4) {
        L(b0Var, z4);
        h(b0Var);
    }

    public final void E(RecyclerView.b0 b0Var, boolean z4) {
        M(b0Var, z4);
    }

    public final void F(RecyclerView.b0 b0Var) {
        N(b0Var);
        h(b0Var);
    }

    public final void G(RecyclerView.b0 b0Var) {
        O(b0Var);
    }

    public final void H(RecyclerView.b0 b0Var) {
        P(b0Var);
        h(b0Var);
    }

    public final void I(RecyclerView.b0 b0Var) {
        Q(b0Var);
    }

    public void J(RecyclerView.b0 b0Var) {
    }

    public void K(RecyclerView.b0 b0Var) {
    }

    public void L(RecyclerView.b0 b0Var, boolean z4) {
    }

    public void M(RecyclerView.b0 b0Var, boolean z4) {
    }

    public void N(RecyclerView.b0 b0Var) {
    }

    public void O(RecyclerView.b0 b0Var) {
    }

    public void P(RecyclerView.b0 b0Var) {
    }

    public void Q(RecyclerView.b0 b0Var) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean a(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2) {
        int i8;
        int i9;
        return (cVar == null || ((i8 = cVar.f6656a) == (i9 = cVar2.f6656a) && cVar.f6657b == cVar2.f6657b)) ? x(b0Var) : z(b0Var, i8, cVar.f6657b, i9, cVar2.f6657b);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean b(RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2, RecyclerView.l.c cVar, RecyclerView.l.c cVar2) {
        int i8;
        int i9;
        int i10 = cVar.f6656a;
        int i11 = cVar.f6657b;
        if (b0Var2.J()) {
            int i12 = cVar.f6656a;
            i9 = cVar.f6657b;
            i8 = i12;
        } else {
            i8 = cVar2.f6656a;
            i9 = cVar2.f6657b;
        }
        return y(b0Var, b0Var2, i10, i11, i8, i9);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean c(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2) {
        int i8 = cVar.f6656a;
        int i9 = cVar.f6657b;
        View view = b0Var.f6628a;
        int left = cVar2 == null ? view.getLeft() : cVar2.f6656a;
        int top = cVar2 == null ? view.getTop() : cVar2.f6657b;
        if (b0Var.v() || (i8 == left && i9 == top)) {
            return A(b0Var);
        }
        view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
        return z(b0Var, i8, i9, left, top);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean d(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2) {
        int i8 = cVar.f6656a;
        int i9 = cVar2.f6656a;
        if (i8 == i9 && cVar.f6657b == cVar2.f6657b) {
            F(b0Var);
            return false;
        }
        return z(b0Var, i8, cVar.f6657b, i9, cVar2.f6657b);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean f(RecyclerView.b0 b0Var) {
        return !this.f7036g || b0Var.t();
    }

    public abstract boolean x(RecyclerView.b0 b0Var);

    public abstract boolean y(RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2, int i8, int i9, int i10, int i11);

    public abstract boolean z(RecyclerView.b0 b0Var, int i8, int i9, int i10, int i11);
}
