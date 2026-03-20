package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v extends z {

    /* renamed from: d  reason: collision with root package name */
    private u f7030d;

    /* renamed from: e  reason: collision with root package name */
    private u f7031e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends p {
        a(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.p, androidx.recyclerview.widget.RecyclerView.x
        protected void o(View view, RecyclerView.y yVar, RecyclerView.x.a aVar) {
            v vVar = v.this;
            int[] c9 = vVar.c(vVar.f7037a.getLayoutManager(), view);
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

        @Override // androidx.recyclerview.widget.p
        protected int x(int i8) {
            return Math.min(100, super.x(i8));
        }
    }

    private int m(RecyclerView.o oVar, View view, u uVar) {
        return (uVar.g(view) + (uVar.e(view) / 2)) - (uVar.m() + (uVar.n() / 2));
    }

    private View n(RecyclerView.o oVar, u uVar) {
        int K = oVar.K();
        View view = null;
        if (K == 0) {
            return null;
        }
        int m8 = uVar.m() + (uVar.n() / 2);
        int i8 = Integer.MAX_VALUE;
        for (int i9 = 0; i9 < K; i9++) {
            View J = oVar.J(i9);
            int abs = Math.abs((uVar.g(J) + (uVar.e(J) / 2)) - m8);
            if (abs < i8) {
                view = J;
                i8 = abs;
            }
        }
        return view;
    }

    private u o(RecyclerView.o oVar) {
        u uVar = this.f7031e;
        if (uVar == null || uVar.f7027a != oVar) {
            this.f7031e = u.a(oVar);
        }
        return this.f7031e;
    }

    private u p(RecyclerView.o oVar) {
        if (oVar.m()) {
            return q(oVar);
        }
        if (oVar.l()) {
            return o(oVar);
        }
        return null;
    }

    private u q(RecyclerView.o oVar) {
        u uVar = this.f7030d;
        if (uVar == null || uVar.f7027a != oVar) {
            this.f7030d = u.c(oVar);
        }
        return this.f7030d;
    }

    private boolean r(RecyclerView.o oVar, int i8, int i9) {
        return oVar.l() ? i8 > 0 : i9 > 0;
    }

    private boolean s(RecyclerView.o oVar) {
        PointF a9;
        int Z = oVar.Z();
        if (!(oVar instanceof RecyclerView.x.b) || (a9 = ((RecyclerView.x.b) oVar).a(Z - 1)) == null) {
            return false;
        }
        return a9.x < 0.0f || a9.y < 0.0f;
    }

    @Override // androidx.recyclerview.widget.z
    public int[] c(RecyclerView.o oVar, View view) {
        int[] iArr = new int[2];
        if (oVar.l()) {
            iArr[0] = m(oVar, view, o(oVar));
        } else {
            iArr[0] = 0;
        }
        if (oVar.m()) {
            iArr[1] = m(oVar, view, q(oVar));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    @Override // androidx.recyclerview.widget.z
    protected p f(RecyclerView.o oVar) {
        if (oVar instanceof RecyclerView.x.b) {
            return new a(this.f7037a.getContext());
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.z
    public View h(RecyclerView.o oVar) {
        u o5;
        if (oVar.m()) {
            o5 = q(oVar);
        } else if (!oVar.l()) {
            return null;
        } else {
            o5 = o(oVar);
        }
        return n(oVar, o5);
    }

    @Override // androidx.recyclerview.widget.z
    public int i(RecyclerView.o oVar, int i8, int i9) {
        u p8;
        int Z = oVar.Z();
        if (Z == 0 || (p8 = p(oVar)) == null) {
            return -1;
        }
        int i10 = Integer.MIN_VALUE;
        int i11 = Integer.MAX_VALUE;
        int K = oVar.K();
        View view = null;
        View view2 = null;
        for (int i12 = 0; i12 < K; i12++) {
            View J = oVar.J(i12);
            if (J != null) {
                int m8 = m(oVar, J, p8);
                if (m8 <= 0 && m8 > i10) {
                    view2 = J;
                    i10 = m8;
                }
                if (m8 >= 0 && m8 < i11) {
                    view = J;
                    i11 = m8;
                }
            }
        }
        boolean r4 = r(oVar, i8, i9);
        if (!r4 || view == null) {
            if (r4 || view2 == null) {
                if (r4) {
                    view = view2;
                }
                if (view == null) {
                    return -1;
                }
                int i02 = oVar.i0(view) + (s(oVar) == r4 ? -1 : 1);
                if (i02 < 0 || i02 >= Z) {
                    return -1;
                }
                return i02;
            }
            return oVar.i0(view2);
        }
        return oVar.i0(view);
    }
}
