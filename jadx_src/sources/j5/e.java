package j5;

import android.util.SparseArray;
import b6.l0;
import b6.t;
import com.google.android.exoplayer2.w0;
import j4.t1;
import j5.g;
import java.util.List;
import n4.b0;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements n4.m, g {

    /* renamed from: k  reason: collision with root package name */
    public static final g.a f20723k = d.a;

    /* renamed from: l  reason: collision with root package name */
    private static final y f20724l = new y();

    /* renamed from: a  reason: collision with root package name */
    private final n4.k f20725a;

    /* renamed from: b  reason: collision with root package name */
    private final int f20726b;

    /* renamed from: c  reason: collision with root package name */
    private final w0 f20727c;

    /* renamed from: d  reason: collision with root package name */
    private final SparseArray<a> f20728d = new SparseArray<>();

    /* renamed from: e  reason: collision with root package name */
    private boolean f20729e;

    /* renamed from: f  reason: collision with root package name */
    private g.b f20730f;

    /* renamed from: g  reason: collision with root package name */
    private long f20731g;

    /* renamed from: h  reason: collision with root package name */
    private z f20732h;

    /* renamed from: j  reason: collision with root package name */
    private w0[] f20733j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements b0 {

        /* renamed from: a  reason: collision with root package name */
        private final int f20734a;

        /* renamed from: b  reason: collision with root package name */
        private final int f20735b;

        /* renamed from: c  reason: collision with root package name */
        private final w0 f20736c;

        /* renamed from: d  reason: collision with root package name */
        private final n4.j f20737d = new n4.j();

        /* renamed from: e  reason: collision with root package name */
        public w0 f20738e;

        /* renamed from: f  reason: collision with root package name */
        private b0 f20739f;

        /* renamed from: g  reason: collision with root package name */
        private long f20740g;

        public a(int i8, int i9, w0 w0Var) {
            this.f20734a = i8;
            this.f20735b = i9;
            this.f20736c = w0Var;
        }

        @Override // n4.b0
        public int a(a6.f fVar, int i8, boolean z4, int i9) {
            return ((b0) l0.j(this.f20739f)).c(fVar, i8, z4);
        }

        @Override // n4.b0
        public void d(long j8, int i8, int i9, int i10, b0.a aVar) {
            long j9 = this.f20740g;
            if (j9 != -9223372036854775807L && j8 >= j9) {
                this.f20739f = this.f20737d;
            }
            ((b0) l0.j(this.f20739f)).d(j8, i8, i9, i10, aVar);
        }

        @Override // n4.b0
        public void e(b6.z zVar, int i8, int i9) {
            ((b0) l0.j(this.f20739f)).b(zVar, i8);
        }

        @Override // n4.b0
        public void f(w0 w0Var) {
            w0 w0Var2 = this.f20736c;
            if (w0Var2 != null) {
                w0Var = w0Var.j(w0Var2);
            }
            this.f20738e = w0Var;
            ((b0) l0.j(this.f20739f)).f(this.f20738e);
        }

        public void g(g.b bVar, long j8) {
            if (bVar == null) {
                this.f20739f = this.f20737d;
                return;
            }
            this.f20740g = j8;
            b0 e8 = bVar.e(this.f20734a, this.f20735b);
            this.f20739f = e8;
            w0 w0Var = this.f20738e;
            if (w0Var != null) {
                e8.f(w0Var);
            }
        }
    }

    public e(n4.k kVar, int i8, w0 w0Var) {
        this.f20725a = kVar;
        this.f20726b = i8;
        this.f20727c = w0Var;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ g g(int i8, w0 w0Var, boolean z4, List list, b0 b0Var, t1 t1Var) {
        n4.k gVar;
        String str = w0Var.f11206l;
        if (t.r(str)) {
            return null;
        }
        if (t.q(str)) {
            gVar = new t4.e(1);
        } else {
            gVar = new v4.g(z4 ? 4 : 0, null, null, list, b0Var);
        }
        return new e(gVar, i8, w0Var);
    }

    @Override // j5.g
    public boolean a(n4.l lVar) {
        int e8 = this.f20725a.e(lVar, f20724l);
        b6.a.f(e8 != 1);
        return e8 == 0;
    }

    @Override // j5.g
    public void b(g.b bVar, long j8, long j9) {
        this.f20730f = bVar;
        this.f20731g = j9;
        if (!this.f20729e) {
            this.f20725a.b(this);
            if (j8 != -9223372036854775807L) {
                this.f20725a.c(0L, j8);
            }
            this.f20729e = true;
            return;
        }
        n4.k kVar = this.f20725a;
        if (j8 == -9223372036854775807L) {
            j8 = 0;
        }
        kVar.c(0L, j8);
        for (int i8 = 0; i8 < this.f20728d.size(); i8++) {
            this.f20728d.valueAt(i8).g(bVar, j9);
        }
    }

    @Override // j5.g
    public w0[] c() {
        return this.f20733j;
    }

    @Override // j5.g
    public n4.c d() {
        z zVar = this.f20732h;
        if (zVar instanceof n4.c) {
            return (n4.c) zVar;
        }
        return null;
    }

    @Override // n4.m
    public b0 e(int i8, int i9) {
        a aVar = this.f20728d.get(i8);
        if (aVar == null) {
            b6.a.f(this.f20733j == null);
            aVar = new a(i8, i9, i9 == this.f20726b ? this.f20727c : null);
            aVar.g(this.f20730f, this.f20731g);
            this.f20728d.put(i8, aVar);
        }
        return aVar;
    }

    @Override // n4.m
    public void m(z zVar) {
        this.f20732h = zVar;
    }

    @Override // n4.m
    public void o() {
        w0[] w0VarArr = new w0[this.f20728d.size()];
        for (int i8 = 0; i8 < this.f20728d.size(); i8++) {
            w0VarArr[i8] = (w0) b6.a.h(this.f20728d.valueAt(i8).f20738e);
        }
        this.f20733j = w0VarArr;
    }

    @Override // j5.g
    public void release() {
        this.f20725a.release();
    }
}
