package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.l;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LinearLayoutManager extends RecyclerView.o implements l.i, RecyclerView.x.b {
    int A;
    int B;
    private boolean C;
    SavedState D;
    final a E;
    private final b F;
    private int G;
    private int[] H;

    /* renamed from: s  reason: collision with root package name */
    int f6535s;

    /* renamed from: t  reason: collision with root package name */
    private c f6536t;

    /* renamed from: u  reason: collision with root package name */
    u f6537u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f6538v;

    /* renamed from: w  reason: collision with root package name */
    private boolean f6539w;

    /* renamed from: x  reason: collision with root package name */
    boolean f6540x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f6541y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f6542z;

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        int f6543a;

        /* renamed from: b  reason: collision with root package name */
        int f6544b;

        /* renamed from: c  reason: collision with root package name */
        boolean f6545c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState() {
        }

        SavedState(Parcel parcel) {
            this.f6543a = parcel.readInt();
            this.f6544b = parcel.readInt();
            this.f6545c = parcel.readInt() == 1;
        }

        public SavedState(SavedState savedState) {
            this.f6543a = savedState.f6543a;
            this.f6544b = savedState.f6544b;
            this.f6545c = savedState.f6545c;
        }

        boolean a() {
            return this.f6543a >= 0;
        }

        void b() {
            this.f6543a = -1;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f6543a);
            parcel.writeInt(this.f6544b);
            parcel.writeInt(this.f6545c ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        u f6546a;

        /* renamed from: b  reason: collision with root package name */
        int f6547b;

        /* renamed from: c  reason: collision with root package name */
        int f6548c;

        /* renamed from: d  reason: collision with root package name */
        boolean f6549d;

        /* renamed from: e  reason: collision with root package name */
        boolean f6550e;

        a() {
            e();
        }

        void a() {
            this.f6548c = this.f6549d ? this.f6546a.i() : this.f6546a.m();
        }

        public void b(View view, int i8) {
            this.f6548c = this.f6549d ? this.f6546a.d(view) + this.f6546a.o() : this.f6546a.g(view);
            this.f6547b = i8;
        }

        public void c(View view, int i8) {
            int o5 = this.f6546a.o();
            if (o5 >= 0) {
                b(view, i8);
                return;
            }
            this.f6547b = i8;
            if (this.f6549d) {
                int i9 = (this.f6546a.i() - o5) - this.f6546a.d(view);
                this.f6548c = this.f6546a.i() - i9;
                if (i9 > 0) {
                    int e8 = this.f6548c - this.f6546a.e(view);
                    int m8 = this.f6546a.m();
                    int min = e8 - (m8 + Math.min(this.f6546a.g(view) - m8, 0));
                    if (min < 0) {
                        this.f6548c += Math.min(i9, -min);
                        return;
                    }
                    return;
                }
                return;
            }
            int g8 = this.f6546a.g(view);
            int m9 = g8 - this.f6546a.m();
            this.f6548c = g8;
            if (m9 > 0) {
                int i10 = (this.f6546a.i() - Math.min(0, (this.f6546a.i() - o5) - this.f6546a.d(view))) - (g8 + this.f6546a.e(view));
                if (i10 < 0) {
                    this.f6548c -= Math.min(m9, -i10);
                }
            }
        }

        boolean d(View view, RecyclerView.y yVar) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return !layoutParams.c() && layoutParams.a() >= 0 && layoutParams.a() < yVar.b();
        }

        void e() {
            this.f6547b = -1;
            this.f6548c = Integer.MIN_VALUE;
            this.f6549d = false;
            this.f6550e = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.f6547b + ", mCoordinate=" + this.f6548c + ", mLayoutFromEnd=" + this.f6549d + ", mValid=" + this.f6550e + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public int f6551a;

        /* renamed from: b  reason: collision with root package name */
        public boolean f6552b;

        /* renamed from: c  reason: collision with root package name */
        public boolean f6553c;

        /* renamed from: d  reason: collision with root package name */
        public boolean f6554d;

        protected b() {
        }

        void a() {
            this.f6551a = 0;
            this.f6552b = false;
            this.f6553c = false;
            this.f6554d = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: b  reason: collision with root package name */
        int f6556b;

        /* renamed from: c  reason: collision with root package name */
        int f6557c;

        /* renamed from: d  reason: collision with root package name */
        int f6558d;

        /* renamed from: e  reason: collision with root package name */
        int f6559e;

        /* renamed from: f  reason: collision with root package name */
        int f6560f;

        /* renamed from: g  reason: collision with root package name */
        int f6561g;

        /* renamed from: k  reason: collision with root package name */
        int f6565k;

        /* renamed from: m  reason: collision with root package name */
        boolean f6567m;

        /* renamed from: a  reason: collision with root package name */
        boolean f6555a = true;

        /* renamed from: h  reason: collision with root package name */
        int f6562h = 0;

        /* renamed from: i  reason: collision with root package name */
        int f6563i = 0;

        /* renamed from: j  reason: collision with root package name */
        boolean f6564j = false;

        /* renamed from: l  reason: collision with root package name */
        List<RecyclerView.b0> f6566l = null;

        c() {
        }

        private View e() {
            int size = this.f6566l.size();
            for (int i8 = 0; i8 < size; i8++) {
                View view = this.f6566l.get(i8).f6628a;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!layoutParams.c() && this.f6558d == layoutParams.a()) {
                    b(view);
                    return view;
                }
            }
            return null;
        }

        public void a() {
            b(null);
        }

        public void b(View view) {
            View f5 = f(view);
            this.f6558d = f5 == null ? -1 : ((RecyclerView.LayoutParams) f5.getLayoutParams()).a();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean c(RecyclerView.y yVar) {
            int i8 = this.f6558d;
            return i8 >= 0 && i8 < yVar.b();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public View d(RecyclerView.u uVar) {
            if (this.f6566l != null) {
                return e();
            }
            View o5 = uVar.o(this.f6558d);
            this.f6558d += this.f6559e;
            return o5;
        }

        public View f(View view) {
            int a9;
            int size = this.f6566l.size();
            View view2 = null;
            int i8 = Integer.MAX_VALUE;
            for (int i9 = 0; i9 < size; i9++) {
                View view3 = this.f6566l.get(i9).f6628a;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view3.getLayoutParams();
                if (view3 != view && !layoutParams.c() && (a9 = (layoutParams.a() - this.f6558d) * this.f6559e) >= 0 && a9 < i8) {
                    view2 = view3;
                    if (a9 == 0) {
                        break;
                    }
                    i8 = a9;
                }
            }
            return view2;
        }
    }

    public LinearLayoutManager(Context context) {
        this(context, 1, false);
    }

    public LinearLayoutManager(Context context, int i8, boolean z4) {
        this.f6535s = 1;
        this.f6539w = false;
        this.f6540x = false;
        this.f6541y = false;
        this.f6542z = true;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.D = null;
        this.E = new a();
        this.F = new b();
        this.G = 2;
        this.H = new int[2];
        E2(i8);
        F2(z4);
    }

    public LinearLayoutManager(Context context, AttributeSet attributeSet, int i8, int i9) {
        this.f6535s = 1;
        this.f6539w = false;
        this.f6540x = false;
        this.f6541y = false;
        this.f6542z = true;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.D = null;
        this.E = new a();
        this.F = new b();
        this.G = 2;
        this.H = new int[2];
        RecyclerView.o.d j02 = RecyclerView.o.j0(context, attributeSet, i8, i9);
        E2(j02.f6680a);
        F2(j02.f6682c);
        G2(j02.f6683d);
    }

    private void B2() {
        this.f6540x = (this.f6535s == 1 || !r2()) ? this.f6539w : !this.f6539w;
    }

    private boolean H2(RecyclerView.u uVar, RecyclerView.y yVar, a aVar) {
        boolean z4 = false;
        if (K() == 0) {
            return false;
        }
        View W = W();
        if (W != null && aVar.d(W, yVar)) {
            aVar.c(W, i0(W));
            return true;
        } else if (this.f6538v != this.f6541y) {
            return false;
        } else {
            View j22 = aVar.f6549d ? j2(uVar, yVar) : k2(uVar, yVar);
            if (j22 != null) {
                aVar.b(j22, i0(j22));
                if (!yVar.e() && M1()) {
                    if (this.f6537u.g(j22) >= this.f6537u.i() || this.f6537u.d(j22) < this.f6537u.m()) {
                        z4 = true;
                    }
                    if (z4) {
                        aVar.f6548c = aVar.f6549d ? this.f6537u.i() : this.f6537u.m();
                    }
                }
                return true;
            }
            return false;
        }
    }

    private boolean I2(RecyclerView.y yVar, a aVar) {
        int i8;
        if (!yVar.e() && (i8 = this.A) != -1) {
            if (i8 >= 0 && i8 < yVar.b()) {
                aVar.f6547b = this.A;
                SavedState savedState = this.D;
                if (savedState != null && savedState.a()) {
                    boolean z4 = this.D.f6545c;
                    aVar.f6549d = z4;
                    aVar.f6548c = z4 ? this.f6537u.i() - this.D.f6544b : this.f6537u.m() + this.D.f6544b;
                    return true;
                } else if (this.B != Integer.MIN_VALUE) {
                    boolean z8 = this.f6540x;
                    aVar.f6549d = z8;
                    aVar.f6548c = z8 ? this.f6537u.i() - this.B : this.f6537u.m() + this.B;
                    return true;
                } else {
                    View D = D(this.A);
                    if (D == null) {
                        if (K() > 0) {
                            aVar.f6549d = (this.A < i0(J(0))) == this.f6540x;
                        }
                        aVar.a();
                    } else if (this.f6537u.e(D) > this.f6537u.n()) {
                        aVar.a();
                        return true;
                    } else if (this.f6537u.g(D) - this.f6537u.m() < 0) {
                        aVar.f6548c = this.f6537u.m();
                        aVar.f6549d = false;
                        return true;
                    } else if (this.f6537u.i() - this.f6537u.d(D) < 0) {
                        aVar.f6548c = this.f6537u.i();
                        aVar.f6549d = true;
                        return true;
                    } else {
                        aVar.f6548c = aVar.f6549d ? this.f6537u.d(D) + this.f6537u.o() : this.f6537u.g(D);
                    }
                    return true;
                }
            }
            this.A = -1;
            this.B = Integer.MIN_VALUE;
        }
        return false;
    }

    private void J2(RecyclerView.u uVar, RecyclerView.y yVar, a aVar) {
        if (I2(yVar, aVar) || H2(uVar, yVar, aVar)) {
            return;
        }
        aVar.a();
        aVar.f6547b = this.f6541y ? yVar.b() - 1 : 0;
    }

    private void K2(int i8, int i9, boolean z4, RecyclerView.y yVar) {
        int m8;
        this.f6536t.f6567m = A2();
        this.f6536t.f6560f = i8;
        int[] iArr = this.H;
        iArr[0] = 0;
        iArr[1] = 0;
        N1(yVar, iArr);
        int max = Math.max(0, this.H[0]);
        int max2 = Math.max(0, this.H[1]);
        boolean z8 = i8 == 1;
        c cVar = this.f6536t;
        int i10 = z8 ? max2 : max;
        cVar.f6562h = i10;
        if (!z8) {
            max = max2;
        }
        cVar.f6563i = max;
        if (z8) {
            cVar.f6562h = i10 + this.f6537u.j();
            View n22 = n2();
            c cVar2 = this.f6536t;
            cVar2.f6559e = this.f6540x ? -1 : 1;
            int i02 = i0(n22);
            c cVar3 = this.f6536t;
            cVar2.f6558d = i02 + cVar3.f6559e;
            cVar3.f6556b = this.f6537u.d(n22);
            m8 = this.f6537u.d(n22) - this.f6537u.i();
        } else {
            View o22 = o2();
            this.f6536t.f6562h += this.f6537u.m();
            c cVar4 = this.f6536t;
            cVar4.f6559e = this.f6540x ? 1 : -1;
            int i03 = i0(o22);
            c cVar5 = this.f6536t;
            cVar4.f6558d = i03 + cVar5.f6559e;
            cVar5.f6556b = this.f6537u.g(o22);
            m8 = (-this.f6537u.g(o22)) + this.f6537u.m();
        }
        c cVar6 = this.f6536t;
        cVar6.f6557c = i9;
        if (z4) {
            cVar6.f6557c = i9 - m8;
        }
        cVar6.f6561g = m8;
    }

    private void L2(int i8, int i9) {
        this.f6536t.f6557c = this.f6537u.i() - i9;
        c cVar = this.f6536t;
        cVar.f6559e = this.f6540x ? -1 : 1;
        cVar.f6558d = i8;
        cVar.f6560f = 1;
        cVar.f6556b = i9;
        cVar.f6561g = Integer.MIN_VALUE;
    }

    private void M2(a aVar) {
        L2(aVar.f6547b, aVar.f6548c);
    }

    private void N2(int i8, int i9) {
        this.f6536t.f6557c = i9 - this.f6537u.m();
        c cVar = this.f6536t;
        cVar.f6558d = i8;
        cVar.f6559e = this.f6540x ? 1 : -1;
        cVar.f6560f = -1;
        cVar.f6556b = i9;
        cVar.f6561g = Integer.MIN_VALUE;
    }

    private void O2(a aVar) {
        N2(aVar.f6547b, aVar.f6548c);
    }

    private int P1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        U1();
        return x.a(yVar, this.f6537u, Z1(!this.f6542z, true), Y1(!this.f6542z, true), this, this.f6542z);
    }

    private int Q1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        U1();
        return x.b(yVar, this.f6537u, Z1(!this.f6542z, true), Y1(!this.f6542z, true), this, this.f6542z, this.f6540x);
    }

    private int R1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        U1();
        return x.c(yVar, this.f6537u, Z1(!this.f6542z, true), Y1(!this.f6542z, true), this, this.f6542z);
    }

    private View W1() {
        return e2(0, K());
    }

    private View X1(RecyclerView.u uVar, RecyclerView.y yVar) {
        return i2(uVar, yVar, 0, K(), yVar.b());
    }

    private View b2() {
        return e2(K() - 1, -1);
    }

    private View c2(RecyclerView.u uVar, RecyclerView.y yVar) {
        return i2(uVar, yVar, K() - 1, -1, yVar.b());
    }

    private View g2() {
        return this.f6540x ? W1() : b2();
    }

    private View h2() {
        return this.f6540x ? b2() : W1();
    }

    private View j2(RecyclerView.u uVar, RecyclerView.y yVar) {
        return this.f6540x ? X1(uVar, yVar) : c2(uVar, yVar);
    }

    private View k2(RecyclerView.u uVar, RecyclerView.y yVar) {
        return this.f6540x ? c2(uVar, yVar) : X1(uVar, yVar);
    }

    private int l2(int i8, RecyclerView.u uVar, RecyclerView.y yVar, boolean z4) {
        int i9;
        int i10 = this.f6537u.i() - i8;
        if (i10 > 0) {
            int i11 = -C2(-i10, uVar, yVar);
            int i12 = i8 + i11;
            if (!z4 || (i9 = this.f6537u.i() - i12) <= 0) {
                return i11;
            }
            this.f6537u.r(i9);
            return i9 + i11;
        }
        return 0;
    }

    private int m2(int i8, RecyclerView.u uVar, RecyclerView.y yVar, boolean z4) {
        int m8;
        int m9 = i8 - this.f6537u.m();
        if (m9 > 0) {
            int i9 = -C2(m9, uVar, yVar);
            int i10 = i8 + i9;
            if (!z4 || (m8 = i10 - this.f6537u.m()) <= 0) {
                return i9;
            }
            this.f6537u.r(-m8);
            return i9 - m8;
        }
        return 0;
    }

    private View n2() {
        return J(this.f6540x ? 0 : K() - 1);
    }

    private View o2() {
        return J(this.f6540x ? K() - 1 : 0);
    }

    private void u2(RecyclerView.u uVar, RecyclerView.y yVar, int i8, int i9) {
        if (!yVar.g() || K() == 0 || yVar.e() || !M1()) {
            return;
        }
        List<RecyclerView.b0> k8 = uVar.k();
        int size = k8.size();
        int i02 = i0(J(0));
        int i10 = 0;
        int i11 = 0;
        for (int i12 = 0; i12 < size; i12++) {
            RecyclerView.b0 b0Var = k8.get(i12);
            if (!b0Var.v()) {
                boolean z4 = (b0Var.m() < i02) != this.f6540x ? true : true;
                int e8 = this.f6537u.e(b0Var.f6628a);
                if (z4) {
                    i10 += e8;
                } else {
                    i11 += e8;
                }
            }
        }
        this.f6536t.f6566l = k8;
        if (i10 > 0) {
            N2(i0(o2()), i8);
            c cVar = this.f6536t;
            cVar.f6562h = i10;
            cVar.f6557c = 0;
            cVar.a();
            V1(uVar, this.f6536t, yVar, false);
        }
        if (i11 > 0) {
            L2(i0(n2()), i9);
            c cVar2 = this.f6536t;
            cVar2.f6562h = i11;
            cVar2.f6557c = 0;
            cVar2.a();
            V1(uVar, this.f6536t, yVar, false);
        }
        this.f6536t.f6566l = null;
    }

    private void w2(RecyclerView.u uVar, c cVar) {
        if (!cVar.f6555a || cVar.f6567m) {
            return;
        }
        int i8 = cVar.f6561g;
        int i9 = cVar.f6563i;
        if (cVar.f6560f == -1) {
            y2(uVar, i8, i9);
        } else {
            z2(uVar, i8, i9);
        }
    }

    private void x2(RecyclerView.u uVar, int i8, int i9) {
        if (i8 == i9) {
            return;
        }
        if (i9 <= i8) {
            while (i8 > i9) {
                o1(i8, uVar);
                i8--;
            }
            return;
        }
        for (int i10 = i9 - 1; i10 >= i8; i10--) {
            o1(i10, uVar);
        }
    }

    private void y2(RecyclerView.u uVar, int i8, int i9) {
        int K = K();
        if (i8 < 0) {
            return;
        }
        int h8 = (this.f6537u.h() - i8) + i9;
        if (this.f6540x) {
            for (int i10 = 0; i10 < K; i10++) {
                View J = J(i10);
                if (this.f6537u.g(J) < h8 || this.f6537u.q(J) < h8) {
                    x2(uVar, 0, i10);
                    return;
                }
            }
            return;
        }
        int i11 = K - 1;
        for (int i12 = i11; i12 >= 0; i12--) {
            View J2 = J(i12);
            if (this.f6537u.g(J2) < h8 || this.f6537u.q(J2) < h8) {
                x2(uVar, i11, i12);
                return;
            }
        }
    }

    private void z2(RecyclerView.u uVar, int i8, int i9) {
        if (i8 < 0) {
            return;
        }
        int i10 = i8 - i9;
        int K = K();
        if (!this.f6540x) {
            for (int i11 = 0; i11 < K; i11++) {
                View J = J(i11);
                if (this.f6537u.d(J) > i10 || this.f6537u.p(J) > i10) {
                    x2(uVar, 0, i11);
                    return;
                }
            }
            return;
        }
        int i12 = K - 1;
        for (int i13 = i12; i13 >= 0; i13--) {
            View J2 = J(i13);
            if (this.f6537u.d(J2) > i10 || this.f6537u.p(J2) > i10) {
                x2(uVar, i12, i13);
                return;
            }
        }
    }

    boolean A2() {
        return this.f6537u.k() == 0 && this.f6537u.h() == 0;
    }

    int C2(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        if (K() == 0 || i8 == 0) {
            return 0;
        }
        U1();
        this.f6536t.f6555a = true;
        int i9 = i8 > 0 ? 1 : -1;
        int abs = Math.abs(i8);
        K2(i9, abs, true, yVar);
        c cVar = this.f6536t;
        int V1 = cVar.f6561g + V1(uVar, cVar, yVar, false);
        if (V1 < 0) {
            return 0;
        }
        if (abs > V1) {
            i8 = i9 * V1;
        }
        this.f6537u.r(-i8);
        this.f6536t.f6565k = i8;
        return i8;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public View D(int i8) {
        int K = K();
        if (K == 0) {
            return null;
        }
        int i02 = i8 - i0(J(0));
        if (i02 >= 0 && i02 < K) {
            View J = J(i02);
            if (i0(J) == i8) {
                return J;
            }
        }
        return super.D(i8);
    }

    public void D2(int i8, int i9) {
        this.A = i8;
        this.B = i9;
        SavedState savedState = this.D;
        if (savedState != null) {
            savedState.b();
        }
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams E() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public void E2(int i8) {
        if (i8 != 0 && i8 != 1) {
            throw new IllegalArgumentException("invalid orientation:" + i8);
        }
        h(null);
        if (i8 != this.f6535s || this.f6537u == null) {
            u b9 = u.b(this, i8);
            this.f6537u = b9;
            this.E.f6546a = b9;
            this.f6535s = i8;
            u1();
        }
    }

    public void F2(boolean z4) {
        h(null);
        if (z4 == this.f6539w) {
            return;
        }
        this.f6539w = z4;
        u1();
    }

    public void G2(boolean z4) {
        h(null);
        if (this.f6541y == z4) {
            return;
        }
        this.f6541y = z4;
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    boolean H1() {
        return (Y() == 1073741824 || q0() == 1073741824 || !r0()) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void J0(RecyclerView recyclerView, RecyclerView.u uVar) {
        super.J0(recyclerView, uVar);
        if (this.C) {
            l1(uVar);
            uVar.c();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void J1(RecyclerView recyclerView, RecyclerView.y yVar, int i8) {
        p pVar = new p(recyclerView.getContext());
        pVar.p(i8);
        K1(pVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public View K0(View view, int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        int S1;
        B2();
        if (K() == 0 || (S1 = S1(i8)) == Integer.MIN_VALUE) {
            return null;
        }
        U1();
        K2(S1, (int) (this.f6537u.n() * 0.33333334f), false, yVar);
        c cVar = this.f6536t;
        cVar.f6561g = Integer.MIN_VALUE;
        cVar.f6555a = false;
        V1(uVar, cVar, yVar, true);
        View h22 = S1 == -1 ? h2() : g2();
        View o22 = S1 == -1 ? o2() : n2();
        if (o22.hasFocusable()) {
            if (h22 == null) {
                return null;
            }
            return o22;
        }
        return h22;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void L0(AccessibilityEvent accessibilityEvent) {
        super.L0(accessibilityEvent);
        if (K() > 0) {
            accessibilityEvent.setFromIndex(a2());
            accessibilityEvent.setToIndex(d2());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean M1() {
        return this.D == null && this.f6538v == this.f6541y;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void N1(RecyclerView.y yVar, int[] iArr) {
        int i8;
        int p22 = p2(yVar);
        if (this.f6536t.f6560f == -1) {
            i8 = 0;
        } else {
            i8 = p22;
            p22 = 0;
        }
        iArr[0] = p22;
        iArr[1] = i8;
    }

    void O1(RecyclerView.y yVar, c cVar, RecyclerView.o.c cVar2) {
        int i8 = cVar.f6558d;
        if (i8 < 0 || i8 >= yVar.b()) {
            return;
        }
        cVar2.a(i8, Math.max(0, cVar.f6561g));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int S1(int i8) {
        return i8 != 1 ? i8 != 2 ? i8 != 17 ? i8 != 33 ? i8 != 66 ? (i8 == 130 && this.f6535s == 1) ? 1 : Integer.MIN_VALUE : this.f6535s == 0 ? 1 : Integer.MIN_VALUE : this.f6535s == 1 ? -1 : Integer.MIN_VALUE : this.f6535s == 0 ? -1 : Integer.MIN_VALUE : (this.f6535s != 1 && r2()) ? -1 : 1 : (this.f6535s != 1 && r2()) ? 1 : -1;
    }

    c T1() {
        return new c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U1() {
        if (this.f6536t == null) {
            this.f6536t = T1();
        }
    }

    int V1(RecyclerView.u uVar, c cVar, RecyclerView.y yVar, boolean z4) {
        int i8 = cVar.f6557c;
        int i9 = cVar.f6561g;
        if (i9 != Integer.MIN_VALUE) {
            if (i8 < 0) {
                cVar.f6561g = i9 + i8;
            }
            w2(uVar, cVar);
        }
        int i10 = cVar.f6557c + cVar.f6562h;
        b bVar = this.F;
        while (true) {
            if ((!cVar.f6567m && i10 <= 0) || !cVar.c(yVar)) {
                break;
            }
            bVar.a();
            t2(uVar, yVar, cVar, bVar);
            if (!bVar.f6552b) {
                cVar.f6556b += bVar.f6551a * cVar.f6560f;
                if (!bVar.f6553c || cVar.f6566l != null || !yVar.e()) {
                    int i11 = cVar.f6557c;
                    int i12 = bVar.f6551a;
                    cVar.f6557c = i11 - i12;
                    i10 -= i12;
                }
                int i13 = cVar.f6561g;
                if (i13 != Integer.MIN_VALUE) {
                    int i14 = i13 + bVar.f6551a;
                    cVar.f6561g = i14;
                    int i15 = cVar.f6557c;
                    if (i15 < 0) {
                        cVar.f6561g = i14 + i15;
                    }
                    w2(uVar, cVar);
                }
                if (z4 && bVar.f6554d) {
                    break;
                }
            } else {
                break;
            }
        }
        return i8 - cVar.f6557c;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Y0(RecyclerView.u uVar, RecyclerView.y yVar) {
        int i8;
        int i9;
        int i10;
        int i11;
        int l22;
        int i12;
        View D;
        int g8;
        int i13;
        int i14 = -1;
        if (!(this.D == null && this.A == -1) && yVar.b() == 0) {
            l1(uVar);
            return;
        }
        SavedState savedState = this.D;
        if (savedState != null && savedState.a()) {
            this.A = this.D.f6543a;
        }
        U1();
        this.f6536t.f6555a = false;
        B2();
        View W = W();
        a aVar = this.E;
        if (!aVar.f6550e || this.A != -1 || this.D != null) {
            aVar.e();
            a aVar2 = this.E;
            aVar2.f6549d = this.f6540x ^ this.f6541y;
            J2(uVar, yVar, aVar2);
            this.E.f6550e = true;
        } else if (W != null && (this.f6537u.g(W) >= this.f6537u.i() || this.f6537u.d(W) <= this.f6537u.m())) {
            this.E.c(W, i0(W));
        }
        c cVar = this.f6536t;
        cVar.f6560f = cVar.f6565k >= 0 ? 1 : -1;
        int[] iArr = this.H;
        iArr[0] = 0;
        iArr[1] = 0;
        N1(yVar, iArr);
        int max = Math.max(0, this.H[0]) + this.f6537u.m();
        int max2 = Math.max(0, this.H[1]) + this.f6537u.j();
        if (yVar.e() && (i12 = this.A) != -1 && this.B != Integer.MIN_VALUE && (D = D(i12)) != null) {
            if (this.f6540x) {
                i13 = this.f6537u.i() - this.f6537u.d(D);
                g8 = this.B;
            } else {
                g8 = this.f6537u.g(D) - this.f6537u.m();
                i13 = this.B;
            }
            int i15 = i13 - g8;
            if (i15 > 0) {
                max += i15;
            } else {
                max2 -= i15;
            }
        }
        a aVar3 = this.E;
        if (!aVar3.f6549d ? !this.f6540x : this.f6540x) {
            i14 = 1;
        }
        v2(uVar, yVar, aVar3, i14);
        x(uVar);
        this.f6536t.f6567m = A2();
        this.f6536t.f6564j = yVar.e();
        this.f6536t.f6563i = 0;
        a aVar4 = this.E;
        if (aVar4.f6549d) {
            O2(aVar4);
            c cVar2 = this.f6536t;
            cVar2.f6562h = max;
            V1(uVar, cVar2, yVar, false);
            c cVar3 = this.f6536t;
            i9 = cVar3.f6556b;
            int i16 = cVar3.f6558d;
            int i17 = cVar3.f6557c;
            if (i17 > 0) {
                max2 += i17;
            }
            M2(this.E);
            c cVar4 = this.f6536t;
            cVar4.f6562h = max2;
            cVar4.f6558d += cVar4.f6559e;
            V1(uVar, cVar4, yVar, false);
            c cVar5 = this.f6536t;
            i8 = cVar5.f6556b;
            int i18 = cVar5.f6557c;
            if (i18 > 0) {
                N2(i16, i9);
                c cVar6 = this.f6536t;
                cVar6.f6562h = i18;
                V1(uVar, cVar6, yVar, false);
                i9 = this.f6536t.f6556b;
            }
        } else {
            M2(aVar4);
            c cVar7 = this.f6536t;
            cVar7.f6562h = max2;
            V1(uVar, cVar7, yVar, false);
            c cVar8 = this.f6536t;
            i8 = cVar8.f6556b;
            int i19 = cVar8.f6558d;
            int i20 = cVar8.f6557c;
            if (i20 > 0) {
                max += i20;
            }
            O2(this.E);
            c cVar9 = this.f6536t;
            cVar9.f6562h = max;
            cVar9.f6558d += cVar9.f6559e;
            V1(uVar, cVar9, yVar, false);
            c cVar10 = this.f6536t;
            i9 = cVar10.f6556b;
            int i21 = cVar10.f6557c;
            if (i21 > 0) {
                L2(i19, i8);
                c cVar11 = this.f6536t;
                cVar11.f6562h = i21;
                V1(uVar, cVar11, yVar, false);
                i8 = this.f6536t.f6556b;
            }
        }
        if (K() > 0) {
            if (this.f6540x ^ this.f6541y) {
                int l23 = l2(i8, uVar, yVar, true);
                i10 = i9 + l23;
                i11 = i8 + l23;
                l22 = m2(i10, uVar, yVar, false);
            } else {
                int m22 = m2(i9, uVar, yVar, true);
                i10 = i9 + m22;
                i11 = i8 + m22;
                l22 = l2(i11, uVar, yVar, false);
            }
            i9 = i10 + l22;
            i8 = i11 + l22;
        }
        u2(uVar, yVar, i9, i8);
        if (yVar.e()) {
            this.E.e();
        } else {
            this.f6537u.s();
        }
        this.f6538v = this.f6541y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View Y1(boolean z4, boolean z8) {
        int K;
        int i8;
        if (this.f6540x) {
            K = 0;
            i8 = K();
        } else {
            K = K() - 1;
            i8 = -1;
        }
        return f2(K, i8, z4, z8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Z0(RecyclerView.y yVar) {
        super.Z0(yVar);
        this.D = null;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.E.e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View Z1(boolean z4, boolean z8) {
        int i8;
        int K;
        if (this.f6540x) {
            i8 = K() - 1;
            K = -1;
        } else {
            i8 = 0;
            K = K();
        }
        return f2(i8, K, z4, z8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x.b
    public PointF a(int i8) {
        if (K() == 0) {
            return null;
        }
        int i9 = (i8 < i0(J(0))) != this.f6540x ? -1 : 1;
        return this.f6535s == 0 ? new PointF(i9, 0.0f) : new PointF(0.0f, i9);
    }

    public int a2() {
        View f22 = f2(0, K(), false, true);
        if (f22 == null) {
            return -1;
        }
        return i0(f22);
    }

    @Override // androidx.recyclerview.widget.l.i
    public void b(View view, View view2, int i8, int i9) {
        int g8;
        h("Cannot drop a view during a scroll or layout calculation");
        U1();
        B2();
        int i02 = i0(view);
        int i03 = i0(view2);
        boolean z4 = i02 < i03 ? true : true;
        if (this.f6540x) {
            if (z4) {
                D2(i03, this.f6537u.i() - (this.f6537u.g(view2) + this.f6537u.e(view)));
                return;
            }
            g8 = this.f6537u.i() - this.f6537u.d(view2);
        } else if (!z4) {
            D2(i03, this.f6537u.d(view2) - this.f6537u.e(view));
            return;
        } else {
            g8 = this.f6537u.g(view2);
        }
        D2(i03, g8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void d1(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.D = (SavedState) parcelable;
            u1();
        }
    }

    public int d2() {
        View f22 = f2(K() - 1, -1, false, true);
        if (f22 == null) {
            return -1;
        }
        return i0(f22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public Parcelable e1() {
        if (this.D != null) {
            return new SavedState(this.D);
        }
        SavedState savedState = new SavedState();
        if (K() > 0) {
            U1();
            boolean z4 = this.f6538v ^ this.f6540x;
            savedState.f6545c = z4;
            if (z4) {
                View n22 = n2();
                savedState.f6544b = this.f6537u.i() - this.f6537u.d(n22);
                savedState.f6543a = i0(n22);
            } else {
                View o22 = o2();
                savedState.f6543a = i0(o22);
                savedState.f6544b = this.f6537u.g(o22) - this.f6537u.m();
            }
        } else {
            savedState.b();
        }
        return savedState;
    }

    View e2(int i8, int i9) {
        int i10;
        int i11;
        U1();
        if ((i9 > i8 ? (char) 1 : i9 < i8 ? (char) 65535 : (char) 0) == 0) {
            return J(i8);
        }
        if (this.f6537u.g(J(i8)) < this.f6537u.m()) {
            i10 = 16644;
            i11 = 16388;
        } else {
            i10 = 4161;
            i11 = 4097;
        }
        return (this.f6535s == 0 ? this.f6665e : this.f6666f).a(i8, i9, i10, i11);
    }

    View f2(int i8, int i9, boolean z4, boolean z8) {
        U1();
        return (this.f6535s == 0 ? this.f6665e : this.f6666f).a(i8, i9, z4 ? 24579 : 320, z8 ? 320 : 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void h(String str) {
        if (this.D == null) {
            super.h(str);
        }
    }

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
            if (i02 >= 0 && i02 < i10) {
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
    public boolean l() {
        return this.f6535s == 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean m() {
        return this.f6535s == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void p(int i8, int i9, RecyclerView.y yVar, RecyclerView.o.c cVar) {
        if (this.f6535s != 0) {
            i8 = i9;
        }
        if (K() == 0 || i8 == 0) {
            return;
        }
        U1();
        K2(i8 > 0 ? 1 : -1, Math.abs(i8), true, yVar);
        O1(yVar, this.f6536t, cVar);
    }

    @Deprecated
    protected int p2(RecyclerView.y yVar) {
        if (yVar.d()) {
            return this.f6537u.n();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void q(int i8, RecyclerView.o.c cVar) {
        boolean z4;
        int i9;
        SavedState savedState = this.D;
        if (savedState == null || !savedState.a()) {
            B2();
            z4 = this.f6540x;
            i9 = this.A;
            if (i9 == -1) {
                i9 = z4 ? i8 - 1 : 0;
            }
        } else {
            SavedState savedState2 = this.D;
            z4 = savedState2.f6545c;
            i9 = savedState2.f6543a;
        }
        int i10 = z4 ? -1 : 1;
        for (int i11 = 0; i11 < this.G && i9 >= 0 && i9 < i8; i11++) {
            cVar.a(i9, 0);
            i9 += i10;
        }
    }

    public int q2() {
        return this.f6535s;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int r(RecyclerView.y yVar) {
        return P1(yVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean r2() {
        return a0() == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int s(RecyclerView.y yVar) {
        return Q1(yVar);
    }

    public boolean s2() {
        return this.f6542z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int t(RecyclerView.y yVar) {
        return R1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean t0() {
        return true;
    }

    void t2(RecyclerView.u uVar, RecyclerView.y yVar, c cVar, b bVar) {
        int i8;
        int i9;
        int i10;
        int i11;
        int f5;
        View d8 = cVar.d(uVar);
        if (d8 == null) {
            bVar.f6552b = true;
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) d8.getLayoutParams();
        if (cVar.f6566l == null) {
            if (this.f6540x == (cVar.f6560f == -1)) {
                e(d8);
            } else {
                f(d8, 0);
            }
        } else {
            if (this.f6540x == (cVar.f6560f == -1)) {
                c(d8);
            } else {
                d(d8, 0);
            }
        }
        B0(d8, 0, 0);
        bVar.f6551a = this.f6537u.e(d8);
        if (this.f6535s == 1) {
            if (r2()) {
                f5 = p0() - g0();
                i11 = f5 - this.f6537u.f(d8);
            } else {
                i11 = f0();
                f5 = this.f6537u.f(d8) + i11;
            }
            int i12 = cVar.f6560f;
            int i13 = cVar.f6556b;
            if (i12 == -1) {
                i10 = i13;
                i9 = f5;
                i8 = i13 - bVar.f6551a;
            } else {
                i8 = i13;
                i9 = f5;
                i10 = bVar.f6551a + i13;
            }
        } else {
            int h02 = h0();
            int f8 = this.f6537u.f(d8) + h02;
            int i14 = cVar.f6560f;
            int i15 = cVar.f6556b;
            if (i14 == -1) {
                i9 = i15;
                i8 = h02;
                i10 = f8;
                i11 = i15 - bVar.f6551a;
            } else {
                i8 = h02;
                i9 = bVar.f6551a + i15;
                i10 = f8;
                i11 = i15;
            }
        }
        A0(d8, i11, i8, i9, i10);
        if (layoutParams.c() || layoutParams.b()) {
            bVar.f6553c = true;
        }
        bVar.f6554d = d8.hasFocusable();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int u(RecyclerView.y yVar) {
        return P1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int v(RecyclerView.y yVar) {
        return Q1(yVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v2(RecyclerView.u uVar, RecyclerView.y yVar, a aVar, int i8) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int w(RecyclerView.y yVar) {
        return R1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int x1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        if (this.f6535s == 1) {
            return 0;
        }
        return C2(i8, uVar, yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void y1(int i8) {
        this.A = i8;
        this.B = Integer.MIN_VALUE;
        SavedState savedState = this.D;
        if (savedState != null) {
            savedState.b();
        }
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int z1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        if (this.f6535s == 0) {
            return 0;
        }
        return C2(i8, uVar, yVar);
    }
}
