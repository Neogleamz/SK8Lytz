package n4;

import b6.l0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    protected final C0191a f22022a;

    /* renamed from: b  reason: collision with root package name */
    protected final f f22023b;

    /* renamed from: c  reason: collision with root package name */
    protected c f22024c;

    /* renamed from: d  reason: collision with root package name */
    private final int f22025d;

    /* renamed from: n4.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0191a implements z {

        /* renamed from: a  reason: collision with root package name */
        private final d f22026a;

        /* renamed from: b  reason: collision with root package name */
        private final long f22027b;

        /* renamed from: c  reason: collision with root package name */
        private final long f22028c;

        /* renamed from: d  reason: collision with root package name */
        private final long f22029d;

        /* renamed from: e  reason: collision with root package name */
        private final long f22030e;

        /* renamed from: f  reason: collision with root package name */
        private final long f22031f;

        /* renamed from: g  reason: collision with root package name */
        private final long f22032g;

        public C0191a(d dVar, long j8, long j9, long j10, long j11, long j12, long j13) {
            this.f22026a = dVar;
            this.f22027b = j8;
            this.f22028c = j9;
            this.f22029d = j10;
            this.f22030e = j11;
            this.f22031f = j12;
            this.f22032g = j13;
        }

        @Override // n4.z
        public long d() {
            return this.f22027b;
        }

        @Override // n4.z
        public boolean h() {
            return true;
        }

        @Override // n4.z
        public z.a i(long j8) {
            return new z.a(new a0(j8, c.h(this.f22026a.a(j8), this.f22028c, this.f22029d, this.f22030e, this.f22031f, this.f22032g)));
        }

        public long k(long j8) {
            return this.f22026a.a(j8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements d {
        @Override // n4.a.d
        public long a(long j8) {
            return j8;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        private final long f22033a;

        /* renamed from: b  reason: collision with root package name */
        private final long f22034b;

        /* renamed from: c  reason: collision with root package name */
        private final long f22035c;

        /* renamed from: d  reason: collision with root package name */
        private long f22036d;

        /* renamed from: e  reason: collision with root package name */
        private long f22037e;

        /* renamed from: f  reason: collision with root package name */
        private long f22038f;

        /* renamed from: g  reason: collision with root package name */
        private long f22039g;

        /* renamed from: h  reason: collision with root package name */
        private long f22040h;

        protected c(long j8, long j9, long j10, long j11, long j12, long j13, long j14) {
            this.f22033a = j8;
            this.f22034b = j9;
            this.f22036d = j10;
            this.f22037e = j11;
            this.f22038f = j12;
            this.f22039g = j13;
            this.f22035c = j14;
            this.f22040h = h(j9, j10, j11, j12, j13, j14);
        }

        protected static long h(long j8, long j9, long j10, long j11, long j12, long j13) {
            if (j11 + 1 >= j12 || j9 + 1 >= j10) {
                return j11;
            }
            long j14 = ((float) (j8 - j9)) * (((float) (j12 - j11)) / ((float) (j10 - j9)));
            return l0.r(((j14 + j11) - j13) - (j14 / 20), j11, j12 - 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long i() {
            return this.f22039g;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long j() {
            return this.f22038f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long k() {
            return this.f22040h;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long l() {
            return this.f22033a;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long m() {
            return this.f22034b;
        }

        private void n() {
            this.f22040h = h(this.f22034b, this.f22036d, this.f22037e, this.f22038f, this.f22039g, this.f22035c);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void o(long j8, long j9) {
            this.f22037e = j8;
            this.f22039g = j9;
            n();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void p(long j8, long j9) {
            this.f22036d = j8;
            this.f22038f = j9;
            n();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        long a(long j8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: d  reason: collision with root package name */
        public static final e f22041d = new e(-3, -9223372036854775807L, -1);

        /* renamed from: a  reason: collision with root package name */
        private final int f22042a;

        /* renamed from: b  reason: collision with root package name */
        private final long f22043b;

        /* renamed from: c  reason: collision with root package name */
        private final long f22044c;

        private e(int i8, long j8, long j9) {
            this.f22042a = i8;
            this.f22043b = j8;
            this.f22044c = j9;
        }

        public static e d(long j8, long j9) {
            return new e(-1, j8, j9);
        }

        public static e e(long j8) {
            return new e(0, -9223372036854775807L, j8);
        }

        public static e f(long j8, long j9) {
            return new e(-2, j8, j9);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        e a(l lVar, long j8);

        default void b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public a(d dVar, f fVar, long j8, long j9, long j10, long j11, long j12, long j13, int i8) {
        this.f22023b = fVar;
        this.f22025d = i8;
        this.f22022a = new C0191a(dVar, j8, j9, j10, j11, j12, j13);
    }

    protected c a(long j8) {
        return new c(j8, this.f22022a.k(j8), this.f22022a.f22028c, this.f22022a.f22029d, this.f22022a.f22030e, this.f22022a.f22031f, this.f22022a.f22032g);
    }

    public final z b() {
        return this.f22022a;
    }

    public int c(l lVar, y yVar) {
        while (true) {
            c cVar = (c) b6.a.h(this.f22024c);
            long j8 = cVar.j();
            long i8 = cVar.i();
            long k8 = cVar.k();
            if (i8 - j8 <= this.f22025d) {
                e(false, j8);
                return g(lVar, j8, yVar);
            } else if (!i(lVar, k8)) {
                return g(lVar, k8, yVar);
            } else {
                lVar.h();
                e a9 = this.f22023b.a(lVar, cVar.m());
                int i9 = a9.f22042a;
                if (i9 == -3) {
                    e(false, k8);
                    return g(lVar, k8, yVar);
                } else if (i9 == -2) {
                    cVar.p(a9.f22043b, a9.f22044c);
                } else if (i9 != -1) {
                    if (i9 == 0) {
                        i(lVar, a9.f22044c);
                        e(true, a9.f22044c);
                        return g(lVar, a9.f22044c, yVar);
                    }
                    throw new IllegalStateException("Invalid case");
                } else {
                    cVar.o(a9.f22043b, a9.f22044c);
                }
            }
        }
    }

    public final boolean d() {
        return this.f22024c != null;
    }

    protected final void e(boolean z4, long j8) {
        this.f22024c = null;
        this.f22023b.b();
        f(z4, j8);
    }

    protected void f(boolean z4, long j8) {
    }

    protected final int g(l lVar, long j8, y yVar) {
        if (j8 == lVar.getPosition()) {
            return 0;
        }
        yVar.f22152a = j8;
        return 1;
    }

    public final void h(long j8) {
        c cVar = this.f22024c;
        if (cVar == null || cVar.l() != j8) {
            this.f22024c = a(j8);
        }
    }

    protected final boolean i(l lVar, long j8) {
        long position = j8 - lVar.getPosition();
        if (position < 0 || position > 262144) {
            return false;
        }
        lVar.i((int) position);
        return true;
    }
}
