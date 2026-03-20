package x4;

import b6.l0;
import b6.z;
import n4.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e0 extends n4.a {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements a.f {

        /* renamed from: a  reason: collision with root package name */
        private final b6.h0 f23841a;

        /* renamed from: b  reason: collision with root package name */
        private final z f23842b = new z();

        /* renamed from: c  reason: collision with root package name */
        private final int f23843c;

        /* renamed from: d  reason: collision with root package name */
        private final int f23844d;

        public a(int i8, b6.h0 h0Var, int i9) {
            this.f23843c = i8;
            this.f23841a = h0Var;
            this.f23844d = i9;
        }

        private a.e c(z zVar, long j8, long j9) {
            int a9;
            int a10;
            int g8 = zVar.g();
            long j10 = -1;
            long j11 = -1;
            long j12 = -9223372036854775807L;
            while (zVar.a() >= 188 && (a10 = (a9 = j0.a(zVar.e(), zVar.f(), g8)) + 188) <= g8) {
                long c9 = j0.c(zVar, a9, this.f23843c);
                if (c9 != -9223372036854775807L) {
                    long b9 = this.f23841a.b(c9);
                    if (b9 > j8) {
                        return j12 == -9223372036854775807L ? a.e.d(b9, j9) : a.e.e(j9 + j11);
                    } else if (100000 + b9 > j8) {
                        return a.e.e(j9 + a9);
                    } else {
                        j11 = a9;
                        j12 = b9;
                    }
                }
                zVar.U(a10);
                j10 = a10;
            }
            return j12 != -9223372036854775807L ? a.e.f(j12, j9 + j10) : a.e.f22041d;
        }

        @Override // n4.a.f
        public a.e a(n4.l lVar, long j8) {
            long position = lVar.getPosition();
            int min = (int) Math.min(this.f23844d, lVar.b() - position);
            this.f23842b.Q(min);
            lVar.k(this.f23842b.e(), 0, min);
            return c(this.f23842b, j8, position);
        }

        @Override // n4.a.f
        public void b() {
            this.f23842b.R(l0.f8068f);
        }
    }

    public e0(b6.h0 h0Var, long j8, long j9, int i8, int i9) {
        super(new a.b(), new a(i8, h0Var, i9), j8, 0L, j8 + 1, 0L, j9, 188L, 940);
    }
}
