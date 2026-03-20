package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements Runnable {

    /* renamed from: e  reason: collision with root package name */
    static final ThreadLocal<k> f6935e = new ThreadLocal<>();

    /* renamed from: f  reason: collision with root package name */
    static Comparator<c> f6936f = new a();

    /* renamed from: b  reason: collision with root package name */
    long f6938b;

    /* renamed from: c  reason: collision with root package name */
    long f6939c;

    /* renamed from: a  reason: collision with root package name */
    ArrayList<RecyclerView> f6937a = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    private ArrayList<c> f6940d = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Comparator<c> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(c cVar, c cVar2) {
            RecyclerView recyclerView = cVar.f6948d;
            if ((recyclerView == null) != (cVar2.f6948d == null)) {
                return recyclerView == null ? 1 : -1;
            }
            boolean z4 = cVar.f6945a;
            if (z4 != cVar2.f6945a) {
                return z4 ? -1 : 1;
            }
            int i8 = cVar2.f6946b - cVar.f6946b;
            if (i8 != 0) {
                return i8;
            }
            int i9 = cVar.f6947c - cVar2.f6947c;
            if (i9 != 0) {
                return i9;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"VisibleForTests"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements RecyclerView.o.c {

        /* renamed from: a  reason: collision with root package name */
        int f6941a;

        /* renamed from: b  reason: collision with root package name */
        int f6942b;

        /* renamed from: c  reason: collision with root package name */
        int[] f6943c;

        /* renamed from: d  reason: collision with root package name */
        int f6944d;

        @Override // androidx.recyclerview.widget.RecyclerView.o.c
        public void a(int i8, int i9) {
            if (i8 < 0) {
                throw new IllegalArgumentException("Layout positions must be non-negative");
            }
            if (i9 < 0) {
                throw new IllegalArgumentException("Pixel distance must be non-negative");
            }
            int i10 = this.f6944d * 2;
            int[] iArr = this.f6943c;
            if (iArr == null) {
                int[] iArr2 = new int[4];
                this.f6943c = iArr2;
                Arrays.fill(iArr2, -1);
            } else if (i10 >= iArr.length) {
                int[] iArr3 = new int[i10 * 2];
                this.f6943c = iArr3;
                System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
            }
            int[] iArr4 = this.f6943c;
            iArr4[i10] = i8;
            iArr4[i10 + 1] = i9;
            this.f6944d++;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void b() {
            int[] iArr = this.f6943c;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            this.f6944d = 0;
        }

        void c(RecyclerView recyclerView, boolean z4) {
            this.f6944d = 0;
            int[] iArr = this.f6943c;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            RecyclerView.o oVar = recyclerView.f6593n;
            if (recyclerView.f6591m == null || oVar == null || !oVar.v0()) {
                return;
            }
            if (z4) {
                if (!recyclerView.f6574d.p()) {
                    oVar.q(recyclerView.f6591m.c(), this);
                }
            } else if (!recyclerView.p0()) {
                oVar.p(this.f6941a, this.f6942b, recyclerView.f6604v0, this);
            }
            int i8 = this.f6944d;
            if (i8 > oVar.f6673m) {
                oVar.f6673m = i8;
                oVar.f6674n = z4;
                recyclerView.f6570b.K();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean d(int i8) {
            if (this.f6943c != null) {
                int i9 = this.f6944d * 2;
                for (int i10 = 0; i10 < i9; i10 += 2) {
                    if (this.f6943c[i10] == i8) {
                        return true;
                    }
                }
            }
            return false;
        }

        void e(int i8, int i9) {
            this.f6941a = i8;
            this.f6942b = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        public boolean f6945a;

        /* renamed from: b  reason: collision with root package name */
        public int f6946b;

        /* renamed from: c  reason: collision with root package name */
        public int f6947c;

        /* renamed from: d  reason: collision with root package name */
        public RecyclerView f6948d;

        /* renamed from: e  reason: collision with root package name */
        public int f6949e;

        c() {
        }

        public void a() {
            this.f6945a = false;
            this.f6946b = 0;
            this.f6947c = 0;
            this.f6948d = null;
            this.f6949e = 0;
        }
    }

    private void b() {
        c cVar;
        int size = this.f6937a.size();
        int i8 = 0;
        for (int i9 = 0; i9 < size; i9++) {
            RecyclerView recyclerView = this.f6937a.get(i9);
            if (recyclerView.getWindowVisibility() == 0) {
                recyclerView.f6603u0.c(recyclerView, false);
                i8 += recyclerView.f6603u0.f6944d;
            }
        }
        this.f6940d.ensureCapacity(i8);
        int i10 = 0;
        for (int i11 = 0; i11 < size; i11++) {
            RecyclerView recyclerView2 = this.f6937a.get(i11);
            if (recyclerView2.getWindowVisibility() == 0) {
                b bVar = recyclerView2.f6603u0;
                int abs = Math.abs(bVar.f6941a) + Math.abs(bVar.f6942b);
                for (int i12 = 0; i12 < bVar.f6944d * 2; i12 += 2) {
                    if (i10 >= this.f6940d.size()) {
                        cVar = new c();
                        this.f6940d.add(cVar);
                    } else {
                        cVar = this.f6940d.get(i10);
                    }
                    int[] iArr = bVar.f6943c;
                    int i13 = iArr[i12 + 1];
                    cVar.f6945a = i13 <= abs;
                    cVar.f6946b = abs;
                    cVar.f6947c = i13;
                    cVar.f6948d = recyclerView2;
                    cVar.f6949e = iArr[i12];
                    i10++;
                }
            }
        }
        Collections.sort(this.f6940d, f6936f);
    }

    private void c(c cVar, long j8) {
        RecyclerView.b0 i8 = i(cVar.f6948d, cVar.f6949e, cVar.f6945a ? Long.MAX_VALUE : j8);
        if (i8 == null || i8.f6629b == null || !i8.s() || i8.t()) {
            return;
        }
        h(i8.f6629b.get(), j8);
    }

    private void d(long j8) {
        for (int i8 = 0; i8 < this.f6940d.size(); i8++) {
            c cVar = this.f6940d.get(i8);
            if (cVar.f6948d == null) {
                return;
            }
            c(cVar, j8);
            cVar.a();
        }
    }

    static boolean e(RecyclerView recyclerView, int i8) {
        int j8 = recyclerView.f6576e.j();
        for (int i9 = 0; i9 < j8; i9++) {
            RecyclerView.b0 i02 = RecyclerView.i0(recyclerView.f6576e.i(i9));
            if (i02.f6630c == i8 && !i02.t()) {
                return true;
            }
        }
        return false;
    }

    private void h(RecyclerView recyclerView, long j8) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.O && recyclerView.f6576e.j() != 0) {
            recyclerView.X0();
        }
        b bVar = recyclerView.f6603u0;
        bVar.c(recyclerView, true);
        if (bVar.f6944d != 0) {
            try {
                androidx.core.os.o.a("RV Nested Prefetch");
                recyclerView.f6604v0.f(recyclerView.f6591m);
                for (int i8 = 0; i8 < bVar.f6944d * 2; i8 += 2) {
                    i(recyclerView, bVar.f6943c[i8], j8);
                }
            } finally {
                androidx.core.os.o.b();
            }
        }
    }

    private RecyclerView.b0 i(RecyclerView recyclerView, int i8, long j8) {
        if (e(recyclerView, i8)) {
            return null;
        }
        RecyclerView.u uVar = recyclerView.f6570b;
        try {
            recyclerView.J0();
            RecyclerView.b0 I = uVar.I(i8, false, j8);
            if (I != null) {
                if (!I.s() || I.t()) {
                    uVar.a(I, false);
                } else {
                    uVar.B(I.f6628a);
                }
            }
            return I;
        } finally {
            recyclerView.L0(false);
        }
    }

    public void a(RecyclerView recyclerView) {
        this.f6937a.add(recyclerView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(RecyclerView recyclerView, int i8, int i9) {
        if (recyclerView.isAttachedToWindow() && this.f6938b == 0) {
            this.f6938b = recyclerView.getNanoTime();
            recyclerView.post(this);
        }
        recyclerView.f6603u0.e(i8, i9);
    }

    void g(long j8) {
        b();
        d(j8);
    }

    public void j(RecyclerView recyclerView) {
        this.f6937a.remove(recyclerView);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            androidx.core.os.o.a("RV Prefetch");
            if (!this.f6937a.isEmpty()) {
                int size = this.f6937a.size();
                long j8 = 0;
                for (int i8 = 0; i8 < size; i8++) {
                    RecyclerView recyclerView = this.f6937a.get(i8);
                    if (recyclerView.getWindowVisibility() == 0) {
                        j8 = Math.max(recyclerView.getDrawingTime(), j8);
                    }
                }
                if (j8 != 0) {
                    g(TimeUnit.MILLISECONDS.toNanos(j8) + this.f6939c);
                }
            }
        } finally {
            this.f6938b = 0L;
            androidx.core.os.o.b();
        }
    }
}
