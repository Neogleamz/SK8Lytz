package androidx.recyclerview.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class z extends RecyclerView.q {

    /* renamed from: a  reason: collision with root package name */
    RecyclerView f7037a;

    /* renamed from: b  reason: collision with root package name */
    private Scroller f7038b;

    /* renamed from: c  reason: collision with root package name */
    private final RecyclerView.s f7039c = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends RecyclerView.s {

        /* renamed from: a  reason: collision with root package name */
        boolean f7040a = false;

        a() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.s
        public void a(RecyclerView recyclerView, int i8) {
            super.a(recyclerView, i8);
            if (i8 == 0 && this.f7040a) {
                this.f7040a = false;
                z.this.l();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.s
        public void b(RecyclerView recyclerView, int i8, int i9) {
            if (i8 == 0 && i9 == 0) {
                return;
            }
            this.f7040a = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends p {
        b(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.p, androidx.recyclerview.widget.RecyclerView.x
        protected void o(View view, RecyclerView.y yVar, RecyclerView.x.a aVar) {
            z zVar = z.this;
            RecyclerView recyclerView = zVar.f7037a;
            if (recyclerView == null) {
                return;
            }
            int[] c9 = zVar.c(recyclerView.getLayoutManager(), view);
            int i8 = c9[0];
            int i9 = c9[1];
            int w8 = w(Math.max(Math.abs(i8), Math.abs(i9)));
            if (w8 > 0) {
                aVar.d(i8, i9, w8, this.f7014j);
            }
        }

        @Override // androidx.recyclerview.widget.p
        protected float v(DisplayMetrics displayMetrics) {
            return 100.0f / displayMetrics.densityDpi;
        }
    }

    private void g() {
        this.f7037a.c1(this.f7039c);
        this.f7037a.setOnFlingListener(null);
    }

    private void j() {
        if (this.f7037a.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        this.f7037a.l(this.f7039c);
        this.f7037a.setOnFlingListener(this);
    }

    private boolean k(RecyclerView.o oVar, int i8, int i9) {
        RecyclerView.x e8;
        int i10;
        if (!(oVar instanceof RecyclerView.x.b) || (e8 = e(oVar)) == null || (i10 = i(oVar, i8, i9)) == -1) {
            return false;
        }
        e8.p(i10);
        oVar.K1(e8);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.q
    public boolean a(int i8, int i9) {
        RecyclerView.o layoutManager = this.f7037a.getLayoutManager();
        if (layoutManager == null || this.f7037a.getAdapter() == null) {
            return false;
        }
        int minFlingVelocity = this.f7037a.getMinFlingVelocity();
        return (Math.abs(i9) > minFlingVelocity || Math.abs(i8) > minFlingVelocity) && k(layoutManager, i8, i9);
    }

    public void b(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f7037a;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            g();
        }
        this.f7037a = recyclerView;
        if (recyclerView != null) {
            j();
            this.f7038b = new Scroller(this.f7037a.getContext(), new DecelerateInterpolator());
            l();
        }
    }

    public abstract int[] c(RecyclerView.o oVar, View view);

    public int[] d(int i8, int i9) {
        this.f7038b.fling(0, 0, i8, i9, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{this.f7038b.getFinalX(), this.f7038b.getFinalY()};
    }

    protected RecyclerView.x e(RecyclerView.o oVar) {
        return f(oVar);
    }

    @Deprecated
    protected p f(RecyclerView.o oVar) {
        if (oVar instanceof RecyclerView.x.b) {
            return new b(this.f7037a.getContext());
        }
        return null;
    }

    public abstract View h(RecyclerView.o oVar);

    public abstract int i(RecyclerView.o oVar, int i8, int i9);

    void l() {
        RecyclerView.o layoutManager;
        View h8;
        RecyclerView recyclerView = this.f7037a;
        if (recyclerView == null || (layoutManager = recyclerView.getLayoutManager()) == null || (h8 = h(layoutManager)) == null) {
            return;
        }
        int[] c9 = c(layoutManager, h8);
        if (c9[0] == 0 && c9[1] == 0) {
            return;
        }
        this.f7037a.p1(c9[0], c9[1]);
    }
}
