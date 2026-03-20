package x4;

import b6.l0;
import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f0 {

    /* renamed from: a  reason: collision with root package name */
    private final int f23858a;

    /* renamed from: d  reason: collision with root package name */
    private boolean f23861d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23862e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f23863f;

    /* renamed from: b  reason: collision with root package name */
    private final b6.h0 f23859b = new b6.h0(0);

    /* renamed from: g  reason: collision with root package name */
    private long f23864g = -9223372036854775807L;

    /* renamed from: h  reason: collision with root package name */
    private long f23865h = -9223372036854775807L;

    /* renamed from: i  reason: collision with root package name */
    private long f23866i = -9223372036854775807L;

    /* renamed from: c  reason: collision with root package name */
    private final z f23860c = new z();

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(int i8) {
        this.f23858a = i8;
    }

    private int a(n4.l lVar) {
        this.f23860c.R(l0.f8068f);
        this.f23861d = true;
        lVar.h();
        return 0;
    }

    private int f(n4.l lVar, n4.y yVar, int i8) {
        int min = (int) Math.min(this.f23858a, lVar.b());
        long j8 = 0;
        if (lVar.getPosition() != j8) {
            yVar.f22152a = j8;
            return 1;
        }
        this.f23860c.Q(min);
        lVar.h();
        lVar.k(this.f23860c.e(), 0, min);
        this.f23864g = g(this.f23860c, i8);
        this.f23862e = true;
        return 0;
    }

    private long g(z zVar, int i8) {
        int g8 = zVar.g();
        for (int f5 = zVar.f(); f5 < g8; f5++) {
            if (zVar.e()[f5] == 71) {
                long c9 = j0.c(zVar, f5, i8);
                if (c9 != -9223372036854775807L) {
                    return c9;
                }
            }
        }
        return -9223372036854775807L;
    }

    private int h(n4.l lVar, n4.y yVar, int i8) {
        long b9 = lVar.b();
        int min = (int) Math.min(this.f23858a, b9);
        long j8 = b9 - min;
        if (lVar.getPosition() != j8) {
            yVar.f22152a = j8;
            return 1;
        }
        this.f23860c.Q(min);
        lVar.h();
        lVar.k(this.f23860c.e(), 0, min);
        this.f23865h = i(this.f23860c, i8);
        this.f23863f = true;
        return 0;
    }

    private long i(z zVar, int i8) {
        int f5 = zVar.f();
        int g8 = zVar.g();
        for (int i9 = g8 - 188; i9 >= f5; i9--) {
            if (j0.b(zVar.e(), f5, g8, i9)) {
                long c9 = j0.c(zVar, i9, i8);
                if (c9 != -9223372036854775807L) {
                    return c9;
                }
            }
        }
        return -9223372036854775807L;
    }

    public long b() {
        return this.f23866i;
    }

    public b6.h0 c() {
        return this.f23859b;
    }

    public boolean d() {
        return this.f23861d;
    }

    public int e(n4.l lVar, n4.y yVar, int i8) {
        if (i8 <= 0) {
            return a(lVar);
        }
        if (this.f23863f) {
            if (this.f23865h == -9223372036854775807L) {
                return a(lVar);
            }
            if (this.f23862e) {
                long j8 = this.f23864g;
                if (j8 == -9223372036854775807L) {
                    return a(lVar);
                }
                long b9 = this.f23859b.b(this.f23865h) - this.f23859b.b(j8);
                this.f23866i = b9;
                if (b9 < 0) {
                    b6.p.i("TsDurationReader", "Invalid duration: " + this.f23866i + ". Using TIME_UNSET instead.");
                    this.f23866i = -9223372036854775807L;
                }
                return a(lVar);
            }
            return f(lVar, yVar, i8);
        }
        return h(lVar, yVar, i8);
    }
}
