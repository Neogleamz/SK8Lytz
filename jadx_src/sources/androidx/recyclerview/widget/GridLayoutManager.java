package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.c;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class GridLayoutManager extends LinearLayoutManager {
    boolean I;
    int J;
    int[] K;
    View[] L;
    final SparseIntArray M;
    final SparseIntArray N;
    b O;
    final Rect P;
    private boolean Q;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {

        /* renamed from: e  reason: collision with root package name */
        int f6529e;

        /* renamed from: f  reason: collision with root package name */
        int f6530f;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f6529e = -1;
            this.f6530f = 0;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f6529e = -1;
            this.f6530f = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f6529e = -1;
            this.f6530f = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f6529e = -1;
            this.f6530f = 0;
        }

        public int e() {
            return this.f6529e;
        }

        public int f() {
            return this.f6530f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends b {
        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int e(int i8, int i9) {
            return i8 % i9;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int f(int i8) {
            return 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {

        /* renamed from: a  reason: collision with root package name */
        final SparseIntArray f6531a = new SparseIntArray();

        /* renamed from: b  reason: collision with root package name */
        final SparseIntArray f6532b = new SparseIntArray();

        /* renamed from: c  reason: collision with root package name */
        private boolean f6533c = false;

        /* renamed from: d  reason: collision with root package name */
        private boolean f6534d = false;

        static int a(SparseIntArray sparseIntArray, int i8) {
            int size = sparseIntArray.size() - 1;
            int i9 = 0;
            while (i9 <= size) {
                int i10 = (i9 + size) >>> 1;
                if (sparseIntArray.keyAt(i10) < i8) {
                    i9 = i10 + 1;
                } else {
                    size = i10 - 1;
                }
            }
            int i11 = i9 - 1;
            if (i11 < 0 || i11 >= sparseIntArray.size()) {
                return -1;
            }
            return sparseIntArray.keyAt(i11);
        }

        int b(int i8, int i9) {
            if (this.f6534d) {
                int i10 = this.f6532b.get(i8, -1);
                if (i10 != -1) {
                    return i10;
                }
                int d8 = d(i8, i9);
                this.f6532b.put(i8, d8);
                return d8;
            }
            return d(i8, i9);
        }

        int c(int i8, int i9) {
            if (this.f6533c) {
                int i10 = this.f6531a.get(i8, -1);
                if (i10 != -1) {
                    return i10;
                }
                int e8 = e(i8, i9);
                this.f6531a.put(i8, e8);
                return e8;
            }
            return e(i8, i9);
        }

        public int d(int i8, int i9) {
            int i10;
            int i11;
            int i12;
            int a9;
            if (!this.f6534d || (a9 = a(this.f6532b, i8)) == -1) {
                i10 = 0;
                i11 = 0;
                i12 = 0;
            } else {
                i10 = this.f6532b.get(a9);
                i11 = a9 + 1;
                i12 = c(a9, i9) + f(a9);
                if (i12 == i9) {
                    i10++;
                    i12 = 0;
                }
            }
            int f5 = f(i8);
            while (i11 < i8) {
                int f8 = f(i11);
                i12 += f8;
                if (i12 == i9) {
                    i10++;
                    i12 = 0;
                } else if (i12 > i9) {
                    i10++;
                    i12 = f8;
                }
                i11++;
            }
            return i12 + f5 > i9 ? i10 + 1 : i10;
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0024  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x002b -> B:17:0x0030). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x002d -> B:17:0x0030). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x002f -> B:17:0x0030). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int e(int r6, int r7) {
            /*
                r5 = this;
                int r0 = r5.f(r6)
                r1 = 0
                if (r0 != r7) goto L8
                return r1
            L8:
                boolean r2 = r5.f6533c
                if (r2 == 0) goto L20
                android.util.SparseIntArray r2 = r5.f6531a
                int r2 = a(r2, r6)
                if (r2 < 0) goto L20
                android.util.SparseIntArray r3 = r5.f6531a
                int r3 = r3.get(r2)
                int r4 = r5.f(r2)
                int r3 = r3 + r4
                goto L30
            L20:
                r2 = r1
                r3 = r2
            L22:
                if (r2 >= r6) goto L33
                int r4 = r5.f(r2)
                int r3 = r3 + r4
                if (r3 != r7) goto L2d
                r3 = r1
                goto L30
            L2d:
                if (r3 <= r7) goto L30
                r3 = r4
            L30:
                int r2 = r2 + 1
                goto L22
            L33:
                int r0 = r0 + r3
                if (r0 > r7) goto L37
                return r3
            L37:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.b.e(int, int):int");
        }

        public abstract int f(int i8);

        public void g() {
            this.f6532b.clear();
        }

        public void h() {
            this.f6531a.clear();
        }
    }

    public GridLayoutManager(Context context, int i8) {
        super(context);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        g3(i8);
    }

    public GridLayoutManager(Context context, int i8, int i9, boolean z4) {
        super(context, i9, z4);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        g3(i8);
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        g3(RecyclerView.o.j0(context, attributeSet, i8, i9).f6681b);
    }

    private void P2(RecyclerView.u uVar, RecyclerView.y yVar, int i8, boolean z4) {
        int i9;
        int i10;
        int i11 = 0;
        int i12 = -1;
        if (z4) {
            i10 = 1;
            i12 = i8;
            i9 = 0;
        } else {
            i9 = i8 - 1;
            i10 = -1;
        }
        while (i9 != i12) {
            View view = this.L[i9];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int c32 = c3(uVar, yVar, i0(view));
            layoutParams.f6530f = c32;
            layoutParams.f6529e = i11;
            i11 += c32;
            i9 += i10;
        }
    }

    private void Q2() {
        int K = K();
        for (int i8 = 0; i8 < K; i8++) {
            LayoutParams layoutParams = (LayoutParams) J(i8).getLayoutParams();
            int a9 = layoutParams.a();
            this.M.put(a9, layoutParams.f());
            this.N.put(a9, layoutParams.e());
        }
    }

    private void R2(int i8) {
        this.K = S2(this.K, this.J, i8);
    }

    static int[] S2(int[] iArr, int i8, int i9) {
        int i10;
        if (iArr == null || iArr.length != i8 + 1 || iArr[iArr.length - 1] != i9) {
            iArr = new int[i8 + 1];
        }
        int i11 = 0;
        iArr[0] = 0;
        int i12 = i9 / i8;
        int i13 = i9 % i8;
        int i14 = 0;
        for (int i15 = 1; i15 <= i8; i15++) {
            i11 += i13;
            if (i11 <= 0 || i8 - i11 >= i13) {
                i10 = i12;
            } else {
                i10 = i12 + 1;
                i11 -= i8;
            }
            i14 += i10;
            iArr[i15] = i14;
        }
        return iArr;
    }

    private void T2() {
        this.M.clear();
        this.N.clear();
    }

    private int U2(RecyclerView.y yVar) {
        if (K() != 0 && yVar.b() != 0) {
            U1();
            boolean s22 = s2();
            View Z1 = Z1(!s22, true);
            View Y1 = Y1(!s22, true);
            if (Z1 != null && Y1 != null) {
                int b9 = this.O.b(i0(Z1), this.J);
                int b10 = this.O.b(i0(Y1), this.J);
                int min = Math.min(b9, b10);
                int max = this.f6540x ? Math.max(0, ((this.O.b(yVar.b() - 1, this.J) + 1) - Math.max(b9, b10)) - 1) : Math.max(0, min);
                if (s22) {
                    return Math.round((max * (Math.abs(this.f6537u.d(Y1) - this.f6537u.g(Z1)) / ((this.O.b(i0(Y1), this.J) - this.O.b(i0(Z1), this.J)) + 1))) + (this.f6537u.m() - this.f6537u.g(Z1)));
                }
                return max;
            }
        }
        return 0;
    }

    private int V2(RecyclerView.y yVar) {
        if (K() != 0 && yVar.b() != 0) {
            U1();
            View Z1 = Z1(!s2(), true);
            View Y1 = Y1(!s2(), true);
            if (Z1 != null && Y1 != null) {
                if (s2()) {
                    int d8 = this.f6537u.d(Y1) - this.f6537u.g(Z1);
                    int b9 = this.O.b(i0(Z1), this.J);
                    return (int) ((d8 / ((this.O.b(i0(Y1), this.J) - b9) + 1)) * (this.O.b(yVar.b() - 1, this.J) + 1));
                }
                return this.O.b(yVar.b() - 1, this.J) + 1;
            }
        }
        return 0;
    }

    private void W2(RecyclerView.u uVar, RecyclerView.y yVar, LinearLayoutManager.a aVar, int i8) {
        boolean z4 = i8 == 1;
        int b32 = b3(uVar, yVar, aVar.f6547b);
        if (z4) {
            while (b32 > 0) {
                int i9 = aVar.f6547b;
                if (i9 <= 0) {
                    return;
                }
                int i10 = i9 - 1;
                aVar.f6547b = i10;
                b32 = b3(uVar, yVar, i10);
            }
            return;
        }
        int b9 = yVar.b() - 1;
        int i11 = aVar.f6547b;
        while (i11 < b9) {
            int i12 = i11 + 1;
            int b33 = b3(uVar, yVar, i12);
            if (b33 <= b32) {
                break;
            }
            i11 = i12;
            b32 = b33;
        }
        aVar.f6547b = i11;
    }

    private void X2() {
        View[] viewArr = this.L;
        if (viewArr == null || viewArr.length != this.J) {
            this.L = new View[this.J];
        }
    }

    private int a3(RecyclerView.u uVar, RecyclerView.y yVar, int i8) {
        if (yVar.e()) {
            int f5 = uVar.f(i8);
            if (f5 == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + i8);
                return 0;
            }
            return this.O.b(f5, this.J);
        }
        return this.O.b(i8, this.J);
    }

    private int b3(RecyclerView.u uVar, RecyclerView.y yVar, int i8) {
        if (yVar.e()) {
            int i9 = this.N.get(i8, -1);
            if (i9 != -1) {
                return i9;
            }
            int f5 = uVar.f(i8);
            if (f5 == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i8);
                return 0;
            }
            return this.O.c(f5, this.J);
        }
        return this.O.c(i8, this.J);
    }

    private int c3(RecyclerView.u uVar, RecyclerView.y yVar, int i8) {
        if (yVar.e()) {
            int i9 = this.M.get(i8, -1);
            if (i9 != -1) {
                return i9;
            }
            int f5 = uVar.f(i8);
            if (f5 == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i8);
                return 1;
            }
            return this.O.f(f5);
        }
        return this.O.f(i8);
    }

    private void d3(float f5, int i8) {
        R2(Math.max(Math.round(f5 * this.J), i8));
    }

    private void e3(View view, int i8, boolean z4) {
        int i9;
        int i10;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.f6614b;
        int i11 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        int i12 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        int Y2 = Y2(layoutParams.f6529e, layoutParams.f6530f);
        if (this.f6535s == 1) {
            i10 = RecyclerView.o.L(Y2, i8, i12, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
            i9 = RecyclerView.o.L(this.f6537u.n(), Y(), i11, ((ViewGroup.MarginLayoutParams) layoutParams).height, true);
        } else {
            int L = RecyclerView.o.L(Y2, i8, i11, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
            int L2 = RecyclerView.o.L(this.f6537u.n(), q0(), i12, ((ViewGroup.MarginLayoutParams) layoutParams).width, true);
            i9 = L;
            i10 = L2;
        }
        f3(view, i10, i9, z4);
    }

    private void f3(View view, int i8, int i9, boolean z4) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (z4 ? I1(view, i8, i9, layoutParams) : G1(view, i8, i9, layoutParams)) {
            view.measure(i8, i9);
        }
    }

    private void i3() {
        int X;
        int h02;
        if (q2() == 1) {
            X = p0() - g0();
            h02 = f0();
        } else {
            X = X() - e0();
            h02 = h0();
        }
        R2(X - h02);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void D1(Rect rect, int i8, int i9) {
        int o5;
        int o8;
        if (this.K == null) {
            super.D1(rect, i8, i9);
        }
        int f02 = f0() + g0();
        int h02 = h0() + e0();
        if (this.f6535s == 1) {
            o8 = RecyclerView.o.o(i9, rect.height() + h02, c0());
            int[] iArr = this.K;
            o5 = RecyclerView.o.o(i8, iArr[iArr.length - 1] + f02, d0());
        } else {
            o5 = RecyclerView.o.o(i8, rect.width() + f02, d0());
            int[] iArr2 = this.K;
            o8 = RecyclerView.o.o(i9, iArr2[iArr2.length - 1] + h02, c0());
        }
        C1(o5, o8);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams E() {
        return this.f6535s == 0 ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams F(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams G(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void G2(boolean z4) {
        if (z4) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.G2(false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x00d6, code lost:
        if (r13 == (r2 > r15)) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00f6, code lost:
        if (r13 == (r2 > r7)) goto L51;
     */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0107  */
    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View K0(android.view.View r24, int r25, androidx.recyclerview.widget.RecyclerView.u r26, androidx.recyclerview.widget.RecyclerView.y r27) {
        /*
            Method dump skipped, instructions count: 337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.K0(android.view.View, int, androidx.recyclerview.widget.RecyclerView$u, androidx.recyclerview.widget.RecyclerView$y):android.view.View");
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public boolean M1() {
        return this.D == null && !this.I;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int O(RecyclerView.u uVar, RecyclerView.y yVar) {
        if (this.f6535s == 1) {
            return this.J;
        }
        if (yVar.b() < 1) {
            return 0;
        }
        return a3(uVar, yVar, yVar.b() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void O1(RecyclerView.y yVar, LinearLayoutManager.c cVar, RecyclerView.o.c cVar2) {
        int i8 = this.J;
        for (int i9 = 0; i9 < this.J && cVar.c(yVar) && i8 > 0; i9++) {
            int i10 = cVar.f6558d;
            cVar2.a(i10, Math.max(0, cVar.f6561g));
            i8 -= this.O.f(i10);
            cVar.f6558d += cVar.f6559e;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Q0(RecyclerView.u uVar, RecyclerView.y yVar, View view, androidx.core.view.accessibility.c cVar) {
        int i8;
        int e8;
        int f5;
        boolean z4;
        boolean z8;
        int i9;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.P0(view, cVar);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        int a32 = a3(uVar, yVar, layoutParams2.a());
        if (this.f6535s == 0) {
            i9 = layoutParams2.e();
            i8 = layoutParams2.f();
            f5 = 1;
            z4 = false;
            z8 = false;
            e8 = a32;
        } else {
            i8 = 1;
            e8 = layoutParams2.e();
            f5 = layoutParams2.f();
            z4 = false;
            z8 = false;
            i9 = a32;
        }
        cVar.f0(c.C0043c.a(i9, i8, e8, f5, z4, z8));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void S0(RecyclerView recyclerView, int i8, int i9) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void T0(RecyclerView recyclerView) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void U0(RecyclerView recyclerView, int i8, int i9, int i10) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void V0(RecyclerView recyclerView, int i8, int i9) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void X0(RecyclerView recyclerView, int i8, int i9, Object obj) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public void Y0(RecyclerView.u uVar, RecyclerView.y yVar) {
        if (yVar.e()) {
            Q2();
        }
        super.Y0(uVar, yVar);
        T2();
    }

    int Y2(int i8, int i9) {
        if (this.f6535s != 1 || !r2()) {
            int[] iArr = this.K;
            return iArr[i9 + i8] - iArr[i8];
        }
        int[] iArr2 = this.K;
        int i10 = this.J;
        return iArr2[i10 - i8] - iArr2[(i10 - i8) - i9];
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public void Z0(RecyclerView.y yVar) {
        super.Z0(yVar);
        this.I = false;
    }

    public int Z2() {
        return this.J;
    }

    public void g3(int i8) {
        if (i8 == this.J) {
            return;
        }
        this.I = true;
        if (i8 >= 1) {
            this.J = i8;
            this.O.h();
            u1();
            return;
        }
        throw new IllegalArgumentException("Span count should be at least 1. Provided " + i8);
    }

    public void h3(b bVar) {
        this.O = bVar;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    View i2(RecyclerView.u uVar, RecyclerView.y yVar, int i8, int i9, int i10) {
        U1();
        int m8 = this.f6537u.m();
        int i11 = this.f6537u.i();
        int i12 = i9 > i8 ? 1 : -1;
        View view = null;
        View view2 = null;
        while (i8 != i9) {
            View J = J(i8);
            int i02 = i0(J);
            if (i02 >= 0 && i02 < i10 && b3(uVar, yVar, i02) == 0) {
                if (((RecyclerView.LayoutParams) J.getLayoutParams()).c()) {
                    if (view2 == null) {
                        view2 = J;
                    }
                } else if (this.f6537u.g(J) < i11 && this.f6537u.d(J) >= m8) {
                    return J;
                } else {
                    if (view == null) {
                        view = J;
                    }
                }
            }
            i8 += i12;
        }
        return view != null ? view : view2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int l0(RecyclerView.u uVar, RecyclerView.y yVar) {
        if (this.f6535s == 0) {
            return this.J;
        }
        if (yVar.b() < 1) {
            return 0;
        }
        return a3(uVar, yVar, yVar.b() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean n(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int s(RecyclerView.y yVar) {
        return this.Q ? U2(yVar) : super.s(yVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int t(RecyclerView.y yVar) {
        return this.Q ? V2(yVar) : super.t(yVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x009f, code lost:
        r21.f6552b = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a1, code lost:
        return;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r5v19 */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void t2(androidx.recyclerview.widget.RecyclerView.u r18, androidx.recyclerview.widget.RecyclerView.y r19, androidx.recyclerview.widget.LinearLayoutManager.c r20, androidx.recyclerview.widget.LinearLayoutManager.b r21) {
        /*
            Method dump skipped, instructions count: 563
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.t2(androidx.recyclerview.widget.RecyclerView$u, androidx.recyclerview.widget.RecyclerView$y, androidx.recyclerview.widget.LinearLayoutManager$c, androidx.recyclerview.widget.LinearLayoutManager$b):void");
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int v(RecyclerView.y yVar) {
        return this.Q ? U2(yVar) : super.v(yVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void v2(RecyclerView.u uVar, RecyclerView.y yVar, LinearLayoutManager.a aVar, int i8) {
        super.v2(uVar, yVar, aVar, i8);
        i3();
        if (yVar.b() > 0 && !yVar.e()) {
            W2(uVar, yVar, aVar, i8);
        }
        X2();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int w(RecyclerView.y yVar) {
        return this.Q ? V2(yVar) : super.w(yVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int x1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        i3();
        X2();
        return super.x1(i8, uVar, yVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.o
    public int z1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        i3();
        X2();
        return super.z1(i8, uVar, yVar);
    }
}
