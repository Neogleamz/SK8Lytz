package q4;

import java.util.Objects;
import n4.a;
import n4.l;
import n4.q;
import n4.t;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends n4.a {

    /* renamed from: q4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class C0197b implements a.f {

        /* renamed from: a  reason: collision with root package name */
        private final t f22528a;

        /* renamed from: b  reason: collision with root package name */
        private final int f22529b;

        /* renamed from: c  reason: collision with root package name */
        private final q.a f22530c;

        private C0197b(t tVar, int i8) {
            this.f22528a = tVar;
            this.f22529b = i8;
            this.f22530c = new q.a();
        }

        private long c(l lVar) {
            while (lVar.e() < lVar.b() - 6 && !q.h(lVar, this.f22528a, this.f22529b, this.f22530c)) {
                lVar.f(1);
            }
            if (lVar.e() >= lVar.b() - 6) {
                lVar.f((int) (lVar.b() - lVar.e()));
                return this.f22528a.f22138j;
            }
            return this.f22530c.f22125a;
        }

        @Override // n4.a.f
        public a.e a(l lVar, long j8) {
            long position = lVar.getPosition();
            long c9 = c(lVar);
            long e8 = lVar.e();
            lVar.f(Math.max(6, this.f22528a.f22131c));
            long c10 = c(lVar);
            return (c9 > j8 || c10 <= j8) ? c10 <= j8 ? a.e.f(c10, lVar.e()) : a.e.d(c9, position) : a.e.e(e8);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(t tVar, int i8, long j8, long j9) {
        super(new q4.a(tVar), new C0197b(tVar, i8), tVar.f(), 0L, tVar.f22138j, j8, j9, tVar.d(), Math.max(6, tVar.f22131c));
        Objects.requireNonNull(tVar);
    }
}
