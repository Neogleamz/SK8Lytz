package l5;

import b6.l0;
import com.google.android.exoplayer2.w0;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k {

    /* renamed from: a  reason: collision with root package name */
    final i f21697a;

    /* renamed from: b  reason: collision with root package name */
    final long f21698b;

    /* renamed from: c  reason: collision with root package name */
    final long f21699c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends k {

        /* renamed from: d  reason: collision with root package name */
        final long f21700d;

        /* renamed from: e  reason: collision with root package name */
        final long f21701e;

        /* renamed from: f  reason: collision with root package name */
        final List<d> f21702f;

        /* renamed from: g  reason: collision with root package name */
        private final long f21703g;

        /* renamed from: h  reason: collision with root package name */
        private final long f21704h;

        /* renamed from: i  reason: collision with root package name */
        final long f21705i;

        public a(i iVar, long j8, long j9, long j10, long j11, List<d> list, long j12, long j13, long j14) {
            super(iVar, j8, j9);
            this.f21700d = j10;
            this.f21701e = j11;
            this.f21702f = list;
            this.f21705i = j12;
            this.f21703g = j13;
            this.f21704h = j14;
        }

        public long c(long j8, long j9) {
            long g8 = g(j8);
            return g8 != -1 ? g8 : (int) (i((j9 - this.f21704h) + this.f21705i, j8) - d(j8, j9));
        }

        public long d(long j8, long j9) {
            if (g(j8) == -1) {
                long j10 = this.f21703g;
                if (j10 != -9223372036854775807L) {
                    return Math.max(e(), i((j9 - this.f21704h) - j10, j8));
                }
            }
            return e();
        }

        public long e() {
            return this.f21700d;
        }

        public long f(long j8, long j9) {
            if (this.f21702f != null) {
                return -9223372036854775807L;
            }
            long d8 = d(j8, j9) + c(j8, j9);
            return (j(d8) + h(d8, j8)) - this.f21705i;
        }

        public abstract long g(long j8);

        public final long h(long j8, long j9) {
            List<d> list = this.f21702f;
            if (list != null) {
                return (list.get((int) (j8 - this.f21700d)).f21711b * 1000000) / this.f21698b;
            }
            long g8 = g(j9);
            return (g8 == -1 || j8 != (e() + g8) - 1) ? (this.f21701e * 1000000) / this.f21698b : j9 - j(j8);
        }

        public long i(long j8, long j9) {
            long e8 = e();
            long g8 = g(j9);
            if (g8 == 0) {
                return e8;
            }
            if (this.f21702f == null) {
                long j10 = this.f21700d + (j8 / ((this.f21701e * 1000000) / this.f21698b));
                return j10 < e8 ? e8 : g8 == -1 ? j10 : Math.min(j10, (e8 + g8) - 1);
            }
            long j11 = (g8 + e8) - 1;
            long j12 = e8;
            while (j12 <= j11) {
                long j13 = ((j11 - j12) / 2) + j12;
                int i8 = (j(j13) > j8 ? 1 : (j(j13) == j8 ? 0 : -1));
                if (i8 < 0) {
                    j12 = j13 + 1;
                } else if (i8 <= 0) {
                    return j13;
                } else {
                    j11 = j13 - 1;
                }
            }
            return j12 == e8 ? j12 : j11;
        }

        public final long j(long j8) {
            List<d> list = this.f21702f;
            return l0.O0(list != null ? list.get((int) (j8 - this.f21700d)).f21710a - this.f21699c : (j8 - this.f21700d) * this.f21701e, 1000000L, this.f21698b);
        }

        public abstract i k(j jVar, long j8);

        public boolean l() {
            return this.f21702f != null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends a {

        /* renamed from: j  reason: collision with root package name */
        final List<i> f21706j;

        public b(i iVar, long j8, long j9, long j10, long j11, List<d> list, long j12, List<i> list2, long j13, long j14) {
            super(iVar, j8, j9, j10, j11, list, j12, j13, j14);
            this.f21706j = list2;
        }

        @Override // l5.k.a
        public long g(long j8) {
            return this.f21706j.size();
        }

        @Override // l5.k.a
        public i k(j jVar, long j8) {
            return this.f21706j.get((int) (j8 - this.f21700d));
        }

        @Override // l5.k.a
        public boolean l() {
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends a {

        /* renamed from: j  reason: collision with root package name */
        final n f21707j;

        /* renamed from: k  reason: collision with root package name */
        final n f21708k;

        /* renamed from: l  reason: collision with root package name */
        final long f21709l;

        public c(i iVar, long j8, long j9, long j10, long j11, long j12, List<d> list, long j13, n nVar, n nVar2, long j14, long j15) {
            super(iVar, j8, j9, j10, j12, list, j13, j14, j15);
            this.f21707j = nVar;
            this.f21708k = nVar2;
            this.f21709l = j11;
        }

        @Override // l5.k
        public i a(j jVar) {
            n nVar = this.f21707j;
            if (nVar != null) {
                w0 w0Var = jVar.f21684b;
                return new i(nVar.a(w0Var.f11196a, 0L, w0Var.f11203h, 0L), 0L, -1L);
            }
            return super.a(jVar);
        }

        @Override // l5.k.a
        public long g(long j8) {
            List<d> list = this.f21702f;
            if (list != null) {
                return list.size();
            }
            long j9 = this.f21709l;
            if (j9 != -1) {
                return (j9 - this.f21700d) + 1;
            }
            if (j8 != -9223372036854775807L) {
                return b8.a.a(BigInteger.valueOf(j8).multiply(BigInteger.valueOf(this.f21698b)), BigInteger.valueOf(this.f21701e).multiply(BigInteger.valueOf(1000000L)), RoundingMode.CEILING).longValue();
            }
            return -1L;
        }

        @Override // l5.k.a
        public i k(j jVar, long j8) {
            List<d> list = this.f21702f;
            long j9 = list != null ? list.get((int) (j8 - this.f21700d)).f21710a : (j8 - this.f21700d) * this.f21701e;
            n nVar = this.f21708k;
            w0 w0Var = jVar.f21684b;
            return new i(nVar.a(w0Var.f11196a, j8, w0Var.f11203h, j9), 0L, -1L);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        final long f21710a;

        /* renamed from: b  reason: collision with root package name */
        final long f21711b;

        public d(long j8, long j9) {
            this.f21710a = j8;
            this.f21711b = j9;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || d.class != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            return this.f21710a == dVar.f21710a && this.f21711b == dVar.f21711b;
        }

        public int hashCode() {
            return (((int) this.f21710a) * 31) + ((int) this.f21711b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends k {

        /* renamed from: d  reason: collision with root package name */
        final long f21712d;

        /* renamed from: e  reason: collision with root package name */
        final long f21713e;

        public e() {
            this(null, 1L, 0L, 0L, 0L);
        }

        public e(i iVar, long j8, long j9, long j10, long j11) {
            super(iVar, j8, j9);
            this.f21712d = j10;
            this.f21713e = j11;
        }

        public i c() {
            long j8 = this.f21713e;
            if (j8 <= 0) {
                return null;
            }
            return new i(null, this.f21712d, j8);
        }
    }

    public k(i iVar, long j8, long j9) {
        this.f21697a = iVar;
        this.f21698b = j8;
        this.f21699c = j9;
    }

    public i a(j jVar) {
        return this.f21697a;
    }

    public long b() {
        return l0.O0(this.f21699c, 1000000L, this.f21698b);
    }
}
