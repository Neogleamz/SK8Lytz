package x4;

import b6.l0;
import b6.z;
import n4.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x extends n4.a {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b implements a.f {

        /* renamed from: a  reason: collision with root package name */
        private final b6.h0 f24140a;

        /* renamed from: b  reason: collision with root package name */
        private final z f24141b;

        private b(b6.h0 h0Var) {
            this.f24140a = h0Var;
            this.f24141b = new z();
        }

        private a.e c(z zVar, long j8, long j9) {
            int i8 = -1;
            int i9 = -1;
            long j10 = -9223372036854775807L;
            while (zVar.a() >= 4) {
                if (x.k(zVar.e(), zVar.f()) != 442) {
                    zVar.V(1);
                } else {
                    zVar.V(4);
                    long l8 = y.l(zVar);
                    if (l8 != -9223372036854775807L) {
                        long b9 = this.f24140a.b(l8);
                        if (b9 > j8) {
                            return j10 == -9223372036854775807L ? a.e.d(b9, j9) : a.e.e(j9 + i9);
                        } else if (100000 + b9 > j8) {
                            return a.e.e(j9 + zVar.f());
                        } else {
                            i9 = zVar.f();
                            j10 = b9;
                        }
                    }
                    d(zVar);
                    i8 = zVar.f();
                }
            }
            return j10 != -9223372036854775807L ? a.e.f(j10, j9 + i8) : a.e.f22041d;
        }

        private static void d(z zVar) {
            int k8;
            int g8 = zVar.g();
            if (zVar.a() < 10) {
                zVar.U(g8);
                return;
            }
            zVar.V(9);
            int H = zVar.H() & 7;
            if (zVar.a() < H) {
                zVar.U(g8);
                return;
            }
            zVar.V(H);
            if (zVar.a() < 4) {
                zVar.U(g8);
                return;
            }
            if (x.k(zVar.e(), zVar.f()) == 443) {
                zVar.V(4);
                int N = zVar.N();
                if (zVar.a() < N) {
                    zVar.U(g8);
                    return;
                }
                zVar.V(N);
            }
            while (zVar.a() >= 4 && (k8 = x.k(zVar.e(), zVar.f())) != 442 && k8 != 441 && (k8 >>> 8) == 1) {
                zVar.V(4);
                if (zVar.a() < 2) {
                    zVar.U(g8);
                    return;
                }
                zVar.U(Math.min(zVar.g(), zVar.f() + zVar.N()));
            }
        }

        @Override // n4.a.f
        public a.e a(n4.l lVar, long j8) {
            long position = lVar.getPosition();
            int min = (int) Math.min(20000L, lVar.b() - position);
            this.f24141b.Q(min);
            lVar.k(this.f24141b.e(), 0, min);
            return c(this.f24141b, j8, position);
        }

        @Override // n4.a.f
        public void b() {
            this.f24141b.R(l0.f8068f);
        }
    }

    public x(b6.h0 h0Var, long j8, long j9) {
        super(new a.b(), new b(h0Var), j8, 0L, j8 + 1, 0L, j9, 188L, 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int k(byte[] bArr, int i8) {
        return (bArr[i8 + 3] & 255) | ((bArr[i8] & 255) << 24) | ((bArr[i8 + 1] & 255) << 16) | ((bArr[i8 + 2] & 255) << 8);
    }
}
