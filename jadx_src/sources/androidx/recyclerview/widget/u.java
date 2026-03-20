package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class u {

    /* renamed from: a  reason: collision with root package name */
    protected final RecyclerView.o f7027a;

    /* renamed from: b  reason: collision with root package name */
    private int f7028b;

    /* renamed from: c  reason: collision with root package name */
    final Rect f7029c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends u {
        a(RecyclerView.o oVar) {
            super(oVar, null);
        }

        @Override // androidx.recyclerview.widget.u
        public int d(View view) {
            return this.f7027a.U(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).rightMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int e(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f7027a.T(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int f(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f7027a.S(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int g(View view) {
            return this.f7027a.R(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).leftMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int h() {
            return this.f7027a.p0();
        }

        @Override // androidx.recyclerview.widget.u
        public int i() {
            return this.f7027a.p0() - this.f7027a.g0();
        }

        @Override // androidx.recyclerview.widget.u
        public int j() {
            return this.f7027a.g0();
        }

        @Override // androidx.recyclerview.widget.u
        public int k() {
            return this.f7027a.q0();
        }

        @Override // androidx.recyclerview.widget.u
        public int l() {
            return this.f7027a.Y();
        }

        @Override // androidx.recyclerview.widget.u
        public int m() {
            return this.f7027a.f0();
        }

        @Override // androidx.recyclerview.widget.u
        public int n() {
            return (this.f7027a.p0() - this.f7027a.f0()) - this.f7027a.g0();
        }

        @Override // androidx.recyclerview.widget.u
        public int p(View view) {
            this.f7027a.o0(view, true, this.f7029c);
            return this.f7029c.right;
        }

        @Override // androidx.recyclerview.widget.u
        public int q(View view) {
            this.f7027a.o0(view, true, this.f7029c);
            return this.f7029c.left;
        }

        @Override // androidx.recyclerview.widget.u
        public void r(int i8) {
            this.f7027a.D0(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends u {
        b(RecyclerView.o oVar) {
            super(oVar, null);
        }

        @Override // androidx.recyclerview.widget.u
        public int d(View view) {
            return this.f7027a.P(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int e(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f7027a.S(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int f(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return this.f7027a.T(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int g(View view) {
            return this.f7027a.V(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).topMargin;
        }

        @Override // androidx.recyclerview.widget.u
        public int h() {
            return this.f7027a.X();
        }

        @Override // androidx.recyclerview.widget.u
        public int i() {
            return this.f7027a.X() - this.f7027a.e0();
        }

        @Override // androidx.recyclerview.widget.u
        public int j() {
            return this.f7027a.e0();
        }

        @Override // androidx.recyclerview.widget.u
        public int k() {
            return this.f7027a.Y();
        }

        @Override // androidx.recyclerview.widget.u
        public int l() {
            return this.f7027a.q0();
        }

        @Override // androidx.recyclerview.widget.u
        public int m() {
            return this.f7027a.h0();
        }

        @Override // androidx.recyclerview.widget.u
        public int n() {
            return (this.f7027a.X() - this.f7027a.h0()) - this.f7027a.e0();
        }

        @Override // androidx.recyclerview.widget.u
        public int p(View view) {
            this.f7027a.o0(view, true, this.f7029c);
            return this.f7029c.bottom;
        }

        @Override // androidx.recyclerview.widget.u
        public int q(View view) {
            this.f7027a.o0(view, true, this.f7029c);
            return this.f7029c.top;
        }

        @Override // androidx.recyclerview.widget.u
        public void r(int i8) {
            this.f7027a.E0(i8);
        }
    }

    private u(RecyclerView.o oVar) {
        this.f7028b = Integer.MIN_VALUE;
        this.f7029c = new Rect();
        this.f7027a = oVar;
    }

    /* synthetic */ u(RecyclerView.o oVar, a aVar) {
        this(oVar);
    }

    public static u a(RecyclerView.o oVar) {
        return new a(oVar);
    }

    public static u b(RecyclerView.o oVar, int i8) {
        if (i8 != 0) {
            if (i8 == 1) {
                return c(oVar);
            }
            throw new IllegalArgumentException("invalid orientation");
        }
        return a(oVar);
    }

    public static u c(RecyclerView.o oVar) {
        return new b(oVar);
    }

    public abstract int d(View view);

    public abstract int e(View view);

    public abstract int f(View view);

    public abstract int g(View view);

    public abstract int h();

    public abstract int i();

    public abstract int j();

    public abstract int k();

    public abstract int l();

    public abstract int m();

    public abstract int n();

    public int o() {
        if (Integer.MIN_VALUE == this.f7028b) {
            return 0;
        }
        return n() - this.f7028b;
    }

    public abstract int p(View view);

    public abstract int q(View view);

    public abstract void r(int i8);

    public void s() {
        this.f7028b = n();
    }
}
