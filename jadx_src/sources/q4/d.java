package q4;

import b6.a;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.libraries.barhopper.RecognitionOptions;
import n4.b0;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.q;
import n4.r;
import n4.s;
import n4.t;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements k {

    /* renamed from: o  reason: collision with root package name */
    public static final p f22531o = c.b;

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22532a;

    /* renamed from: b  reason: collision with root package name */
    private final z f22533b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f22534c;

    /* renamed from: d  reason: collision with root package name */
    private final q.a f22535d;

    /* renamed from: e  reason: collision with root package name */
    private m f22536e;

    /* renamed from: f  reason: collision with root package name */
    private b0 f22537f;

    /* renamed from: g  reason: collision with root package name */
    private int f22538g;

    /* renamed from: h  reason: collision with root package name */
    private Metadata f22539h;

    /* renamed from: i  reason: collision with root package name */
    private t f22540i;

    /* renamed from: j  reason: collision with root package name */
    private int f22541j;

    /* renamed from: k  reason: collision with root package name */
    private int f22542k;

    /* renamed from: l  reason: collision with root package name */
    private b f22543l;

    /* renamed from: m  reason: collision with root package name */
    private int f22544m;

    /* renamed from: n  reason: collision with root package name */
    private long f22545n;

    public d() {
        this(0);
    }

    public d(int i8) {
        this.f22532a = new byte[42];
        this.f22533b = new z(new byte[RecognitionOptions.TEZ_CODE], 0);
        this.f22534c = (i8 & 1) != 0;
        this.f22535d = new q.a();
        this.f22538g = 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0020, code lost:
        r5.U(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0027, code lost:
        return r4.f22535d.f22125a;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private long d(b6.z r5, boolean r6) {
        /*
            r4 = this;
            n4.t r0 = r4.f22540i
            b6.a.e(r0)
            int r0 = r5.f()
        L9:
            int r1 = r5.g()
            int r1 = r1 + (-16)
            if (r0 > r1) goto L2b
            r5.U(r0)
            n4.t r1 = r4.f22540i
            int r2 = r4.f22542k
            n4.q$a r3 = r4.f22535d
            boolean r1 = n4.q.d(r5, r1, r2, r3)
            if (r1 == 0) goto L28
        L20:
            r5.U(r0)
            n4.q$a r5 = r4.f22535d
            long r5 = r5.f22125a
            return r5
        L28:
            int r0 = r0 + 1
            goto L9
        L2b:
            if (r6 == 0) goto L60
        L2d:
            int r6 = r5.g()
            int r1 = r4.f22541j
            int r6 = r6 - r1
            if (r0 > r6) goto L58
            r5.U(r0)
            r6 = 0
            n4.t r1 = r4.f22540i     // Catch: java.lang.IndexOutOfBoundsException -> L45
            int r2 = r4.f22542k     // Catch: java.lang.IndexOutOfBoundsException -> L45
            n4.q$a r3 = r4.f22535d     // Catch: java.lang.IndexOutOfBoundsException -> L45
            boolean r1 = n4.q.d(r5, r1, r2, r3)     // Catch: java.lang.IndexOutOfBoundsException -> L45
            goto L46
        L45:
            r1 = r6
        L46:
            int r2 = r5.f()
            int r3 = r5.g()
            if (r2 <= r3) goto L51
            goto L52
        L51:
            r6 = r1
        L52:
            if (r6 == 0) goto L55
            goto L20
        L55:
            int r0 = r0 + 1
            goto L2d
        L58:
            int r6 = r5.g()
            r5.U(r6)
            goto L63
        L60:
            r5.U(r0)
        L63:
            r5 = -1
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: q4.d.d(b6.z, boolean):long");
    }

    private void f(l lVar) {
        this.f22542k = r.b(lVar);
        ((m) l0.j(this.f22536e)).m(h(lVar.getPosition(), lVar.b()));
        this.f22538g = 5;
    }

    private n4.z h(long j8, long j9) {
        a.e(this.f22540i);
        t tVar = this.f22540i;
        if (tVar.f22139k != null) {
            return new s(tVar, j8);
        }
        if (j9 == -1 || tVar.f22138j <= 0) {
            return new z.b(tVar.f());
        }
        b bVar = new b(tVar, this.f22542k, j8, j9);
        this.f22543l = bVar;
        return bVar.b();
    }

    private void i(l lVar) {
        byte[] bArr = this.f22532a;
        lVar.k(bArr, 0, bArr.length);
        lVar.h();
        this.f22538g = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] j() {
        return new k[]{new d()};
    }

    private void k() {
        ((b0) l0.j(this.f22537f)).d((this.f22545n * 1000000) / ((t) l0.j(this.f22540i)).f22133e, 1, this.f22544m, 0, null);
    }

    private int l(l lVar, y yVar) {
        boolean z4;
        a.e(this.f22537f);
        a.e(this.f22540i);
        b bVar = this.f22543l;
        if (bVar == null || !bVar.d()) {
            if (this.f22545n == -1) {
                this.f22545n = q.i(lVar, this.f22540i);
                return 0;
            }
            int g8 = this.f22533b.g();
            if (g8 < 32768) {
                int read = lVar.read(this.f22533b.e(), g8, RecognitionOptions.TEZ_CODE - g8);
                z4 = read == -1;
                if (!z4) {
                    this.f22533b.T(g8 + read);
                } else if (this.f22533b.a() == 0) {
                    k();
                    return -1;
                }
            } else {
                z4 = false;
            }
            int f5 = this.f22533b.f();
            int i8 = this.f22544m;
            int i9 = this.f22541j;
            if (i8 < i9) {
                b6.z zVar = this.f22533b;
                zVar.V(Math.min(i9 - i8, zVar.a()));
            }
            long d8 = d(this.f22533b, z4);
            int f8 = this.f22533b.f() - f5;
            this.f22533b.U(f5);
            this.f22537f.b(this.f22533b, f8);
            this.f22544m += f8;
            if (d8 != -1) {
                k();
                this.f22544m = 0;
                this.f22545n = d8;
            }
            if (this.f22533b.a() < 16) {
                int a9 = this.f22533b.a();
                System.arraycopy(this.f22533b.e(), this.f22533b.f(), this.f22533b.e(), 0, a9);
                this.f22533b.U(0);
                this.f22533b.T(a9);
            }
            return 0;
        }
        return this.f22543l.c(lVar, yVar);
    }

    private void m(l lVar) {
        this.f22539h = r.d(lVar, !this.f22534c);
        this.f22538g = 1;
    }

    private void n(l lVar) {
        r.a aVar = new r.a(this.f22540i);
        boolean z4 = false;
        while (!z4) {
            z4 = r.e(lVar, aVar);
            this.f22540i = (t) l0.j(aVar.f22126a);
        }
        a.e(this.f22540i);
        this.f22541j = Math.max(this.f22540i.f22131c, 6);
        ((b0) l0.j(this.f22537f)).f(this.f22540i.g(this.f22532a, this.f22539h));
        this.f22538g = 4;
    }

    private void o(l lVar) {
        r.i(lVar);
        this.f22538g = 3;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f22536e = mVar;
        this.f22537f = mVar.e(0, 1);
        mVar.o();
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        if (j8 == 0) {
            this.f22538g = 0;
        } else {
            b bVar = this.f22543l;
            if (bVar != null) {
                bVar.h(j9);
            }
        }
        this.f22545n = j9 != 0 ? -1L : 0L;
        this.f22544m = 0;
        this.f22533b.Q(0);
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        int i8 = this.f22538g;
        if (i8 == 0) {
            m(lVar);
            return 0;
        } else if (i8 == 1) {
            i(lVar);
            return 0;
        } else if (i8 == 2) {
            o(lVar);
            return 0;
        } else if (i8 == 3) {
            n(lVar);
            return 0;
        } else if (i8 == 4) {
            f(lVar);
            return 0;
        } else if (i8 == 5) {
            return l(lVar, yVar);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override // n4.k
    public boolean g(l lVar) {
        r.c(lVar, false);
        return r.a(lVar);
    }

    @Override // n4.k
    public void release() {
    }
}
