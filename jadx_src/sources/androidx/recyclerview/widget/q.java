package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q extends z {

    /* renamed from: d  reason: collision with root package name */
    private u f7021d;

    /* renamed from: e  reason: collision with root package name */
    private u f7022e;

    private float m(RecyclerView.o oVar, u uVar) {
        int K = oVar.K();
        if (K == 0) {
            return 1.0f;
        }
        View view = null;
        int i8 = Integer.MIN_VALUE;
        int i9 = Integer.MAX_VALUE;
        View view2 = null;
        for (int i10 = 0; i10 < K; i10++) {
            View J = oVar.J(i10);
            int i02 = oVar.i0(J);
            if (i02 != -1) {
                if (i02 < i9) {
                    view = J;
                    i9 = i02;
                }
                if (i02 > i8) {
                    view2 = J;
                    i8 = i02;
                }
            }
        }
        if (view == null || view2 == null) {
            return 1.0f;
        }
        int max = Math.max(uVar.d(view), uVar.d(view2)) - Math.min(uVar.g(view), uVar.g(view2));
        if (max == 0) {
            return 1.0f;
        }
        return (max * 1.0f) / ((i8 - i9) + 1);
    }

    private int n(RecyclerView.o oVar, View view, u uVar) {
        return (uVar.g(view) + (uVar.e(view) / 2)) - (uVar.m() + (uVar.n() / 2));
    }

    private int o(RecyclerView.o oVar, u uVar, int i8, int i9) {
        int[] d8 = d(i8, i9);
        float m8 = m(oVar, uVar);
        if (m8 <= 0.0f) {
            return 0;
        }
        return Math.round((Math.abs(d8[0]) > Math.abs(d8[1]) ? d8[0] : d8[1]) / m8);
    }

    private View p(RecyclerView.o oVar, u uVar) {
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

    private u q(RecyclerView.o oVar) {
        u uVar = this.f7022e;
        if (uVar == null || uVar.f7027a != oVar) {
            this.f7022e = u.a(oVar);
        }
        return this.f7022e;
    }

    private u r(RecyclerView.o oVar) {
        u uVar = this.f7021d;
        if (uVar == null || uVar.f7027a != oVar) {
            this.f7021d = u.c(oVar);
        }
        return this.f7021d;
    }

    @Override // androidx.recyclerview.widget.z
    public int[] c(RecyclerView.o oVar, View view) {
        int[] iArr = new int[2];
        if (oVar.l()) {
            iArr[0] = n(oVar, view, q(oVar));
        } else {
            iArr[0] = 0;
        }
        if (oVar.m()) {
            iArr[1] = n(oVar, view, r(oVar));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    @Override // androidx.recyclerview.widget.z
    public View h(RecyclerView.o oVar) {
        u q;
        if (oVar.m()) {
            q = r(oVar);
        } else if (!oVar.l()) {
            return null;
        } else {
            q = q(oVar);
        }
        return p(oVar, q);
    }

    @Override // androidx.recyclerview.widget.z
    public int i(RecyclerView.o oVar, int i8, int i9) {
        int Z;
        View h8;
        int i02;
        int i10;
        PointF a9;
        int i11;
        int i12;
        if (!(oVar instanceof RecyclerView.x.b) || (Z = oVar.Z()) == 0 || (h8 = h(oVar)) == null || (i02 = oVar.i0(h8)) == -1 || (a9 = ((RecyclerView.x.b) oVar).a(Z - 1)) == null) {
            return -1;
        }
        if (oVar.l()) {
            i11 = o(oVar, q(oVar), i8, 0);
            if (a9.x < 0.0f) {
                i11 = -i11;
            }
        } else {
            i11 = 0;
        }
        if (oVar.m()) {
            i12 = o(oVar, r(oVar), 0, i9);
            if (a9.y < 0.0f) {
                i12 = -i12;
            }
        } else {
            i12 = 0;
        }
        if (oVar.m()) {
            i11 = i12;
        }
        if (i11 == 0) {
            return -1;
        }
        int i13 = i02 + i11;
        int i14 = i13 >= 0 ? i13 : 0;
        return i14 >= Z ? i10 : i14;
    }
}
