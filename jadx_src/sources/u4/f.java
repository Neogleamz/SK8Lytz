package u4;

import b6.l0;
import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import e5.b;
import java.io.EOFException;
import k4.u;
import n4.b0;
import n4.j;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.v;
import n4.w;
import n4.y;
import u4.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f implements k {

    /* renamed from: u  reason: collision with root package name */
    public static final p f22997u = e.b;

    /* renamed from: v  reason: collision with root package name */
    private static final b.a f22998v = d.a;

    /* renamed from: a  reason: collision with root package name */
    private final int f22999a;

    /* renamed from: b  reason: collision with root package name */
    private final long f23000b;

    /* renamed from: c  reason: collision with root package name */
    private final z f23001c;

    /* renamed from: d  reason: collision with root package name */
    private final u.a f23002d;

    /* renamed from: e  reason: collision with root package name */
    private final v f23003e;

    /* renamed from: f  reason: collision with root package name */
    private final w f23004f;

    /* renamed from: g  reason: collision with root package name */
    private final b0 f23005g;

    /* renamed from: h  reason: collision with root package name */
    private m f23006h;

    /* renamed from: i  reason: collision with root package name */
    private b0 f23007i;

    /* renamed from: j  reason: collision with root package name */
    private b0 f23008j;

    /* renamed from: k  reason: collision with root package name */
    private int f23009k;

    /* renamed from: l  reason: collision with root package name */
    private Metadata f23010l;

    /* renamed from: m  reason: collision with root package name */
    private long f23011m;

    /* renamed from: n  reason: collision with root package name */
    private long f23012n;

    /* renamed from: o  reason: collision with root package name */
    private long f23013o;

    /* renamed from: p  reason: collision with root package name */
    private int f23014p;
    private g q;

    /* renamed from: r  reason: collision with root package name */
    private boolean f23015r;

    /* renamed from: s  reason: collision with root package name */
    private boolean f23016s;

    /* renamed from: t  reason: collision with root package name */
    private long f23017t;

    public f() {
        this(0);
    }

    public f(int i8) {
        this(i8, -9223372036854775807L);
    }

    public f(int i8, long j8) {
        this.f22999a = (i8 & 2) != 0 ? i8 | 1 : i8;
        this.f23000b = j8;
        this.f23001c = new z(10);
        this.f23002d = new u.a();
        this.f23003e = new v();
        this.f23011m = -9223372036854775807L;
        this.f23004f = new w();
        j jVar = new j();
        this.f23005g = jVar;
        this.f23008j = jVar;
    }

    private void f() {
        b6.a.h(this.f23007i);
        l0.j(this.f23006h);
    }

    private g h(l lVar) {
        long l8;
        long j8;
        long d8;
        long f5;
        g r4 = r(lVar);
        c q = q(this.f23010l, lVar.getPosition());
        if (this.f23015r) {
            return new g.a();
        }
        if ((this.f22999a & 4) != 0) {
            if (q != null) {
                d8 = q.d();
                f5 = q.f();
            } else if (r4 != null) {
                d8 = r4.d();
                f5 = r4.f();
            } else {
                l8 = l(this.f23010l);
                j8 = -1;
                r4 = new b(l8, lVar.getPosition(), j8);
            }
            j8 = f5;
            l8 = d8;
            r4 = new b(l8, lVar.getPosition(), j8);
        } else if (q != null) {
            r4 = q;
        } else if (r4 == null) {
            r4 = null;
        }
        if (r4 == null || !(r4.h() || (this.f22999a & 1) == 0)) {
            return k(lVar, (this.f22999a & 2) != 0);
        }
        return r4;
    }

    private long i(long j8) {
        return this.f23011m + ((j8 * 1000000) / this.f23002d.f21024d);
    }

    private g k(l lVar, boolean z4) {
        lVar.k(this.f23001c.e(), 0, 4);
        this.f23001c.U(0);
        this.f23002d.a(this.f23001c.q());
        return new a(lVar.b(), lVar.getPosition(), this.f23002d, z4);
    }

    private static long l(Metadata metadata) {
        if (metadata != null) {
            int e8 = metadata.e();
            for (int i8 = 0; i8 < e8; i8++) {
                Metadata.Entry d8 = metadata.d(i8);
                if (d8 instanceof TextInformationFrame) {
                    TextInformationFrame textInformationFrame = (TextInformationFrame) d8;
                    if (textInformationFrame.f10109a.equals("TLEN")) {
                        return l0.C0(Long.parseLong(textInformationFrame.f10122d.get(0)));
                    }
                }
            }
            return -9223372036854775807L;
        }
        return -9223372036854775807L;
    }

    private static int m(z zVar, int i8) {
        if (zVar.g() >= i8 + 4) {
            zVar.U(i8);
            int q = zVar.q();
            if (q == 1483304551 || q == 1231971951) {
                return q;
            }
        }
        if (zVar.g() >= 40) {
            zVar.U(36);
            return zVar.q() == 1447187017 ? 1447187017 : 0;
        }
        return 0;
    }

    private static boolean n(int i8, long j8) {
        return ((long) (i8 & (-128000))) == (j8 & (-128000));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] o() {
        return new k[]{new f()};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean p(int i8, int i9, int i10, int i11, int i12) {
        return (i9 == 67 && i10 == 79 && i11 == 77 && (i12 == 77 || i8 == 2)) || (i9 == 77 && i10 == 76 && i11 == 76 && (i12 == 84 || i8 == 2));
    }

    private static c q(Metadata metadata, long j8) {
        if (metadata != null) {
            int e8 = metadata.e();
            for (int i8 = 0; i8 < e8; i8++) {
                Metadata.Entry d8 = metadata.d(i8);
                if (d8 instanceof MlltFrame) {
                    return c.b(j8, (MlltFrame) d8, l(metadata));
                }
            }
            return null;
        }
        return null;
    }

    private g r(l lVar) {
        z zVar = new z(this.f23002d.f21023c);
        lVar.k(zVar.e(), 0, this.f23002d.f21023c);
        u.a aVar = this.f23002d;
        int i8 = aVar.f21021a & 1;
        int i9 = 21;
        int i10 = aVar.f21025e;
        if (i8 != 0) {
            if (i10 != 1) {
                i9 = 36;
            }
        } else if (i10 == 1) {
            i9 = 13;
        }
        int i11 = i9;
        int m8 = m(zVar, i11);
        if (m8 != 1483304551 && m8 != 1231971951) {
            if (m8 != 1447187017) {
                lVar.h();
                return null;
            }
            h b9 = h.b(lVar.b(), lVar.getPosition(), this.f23002d, zVar);
            lVar.i(this.f23002d.f21023c);
            return b9;
        }
        i b10 = i.b(lVar.b(), lVar.getPosition(), this.f23002d, zVar);
        if (b10 != null && !this.f23003e.a()) {
            lVar.h();
            lVar.f(i11 + 141);
            lVar.k(this.f23001c.e(), 0, 3);
            this.f23001c.U(0);
            this.f23003e.d(this.f23001c.K());
        }
        lVar.i(this.f23002d.f21023c);
        return (b10 == null || b10.h() || m8 != 1231971951) ? b10 : k(lVar, false);
    }

    private boolean s(l lVar) {
        g gVar = this.q;
        if (gVar != null) {
            long f5 = gVar.f();
            if (f5 != -1 && lVar.e() > f5 - 4) {
                return true;
            }
        }
        try {
            return !lVar.d(this.f23001c.e(), 0, 4, true);
        } catch (EOFException unused) {
            return true;
        }
    }

    private int t(l lVar) {
        if (this.f23009k == 0) {
            try {
                v(lVar, false);
            } catch (EOFException unused) {
                return -1;
            }
        }
        if (this.q == null) {
            g h8 = h(lVar);
            this.q = h8;
            this.f23006h.m(h8);
            this.f23008j.f(new w0.b().g0(this.f23002d.f21022b).Y(RecognitionOptions.AZTEC).J(this.f23002d.f21025e).h0(this.f23002d.f21024d).P(this.f23003e.f22145a).Q(this.f23003e.f22146b).Z((this.f22999a & 8) != 0 ? null : this.f23010l).G());
            this.f23013o = lVar.getPosition();
        } else if (this.f23013o != 0) {
            long position = lVar.getPosition();
            long j8 = this.f23013o;
            if (position < j8) {
                lVar.i((int) (j8 - position));
            }
        }
        return u(lVar);
    }

    private int u(l lVar) {
        u.a aVar;
        if (this.f23014p == 0) {
            lVar.h();
            if (s(lVar)) {
                return -1;
            }
            this.f23001c.U(0);
            int q = this.f23001c.q();
            if (!n(q, this.f23009k) || u.j(q) == -1) {
                lVar.i(1);
                this.f23009k = 0;
                return 0;
            }
            this.f23002d.a(q);
            if (this.f23011m == -9223372036854775807L) {
                this.f23011m = this.q.a(lVar.getPosition());
                if (this.f23000b != -9223372036854775807L) {
                    this.f23011m += this.f23000b - this.q.a(0L);
                }
            }
            this.f23014p = this.f23002d.f21023c;
            g gVar = this.q;
            if (gVar instanceof b) {
                b bVar = (b) gVar;
                bVar.c(i(this.f23012n + aVar.f21027g), lVar.getPosition() + this.f23002d.f21023c);
                if (this.f23016s && bVar.b(this.f23017t)) {
                    this.f23016s = false;
                    this.f23008j = this.f23007i;
                }
            }
        }
        int c9 = this.f23008j.c(lVar, this.f23014p, true);
        if (c9 == -1) {
            return -1;
        }
        int i8 = this.f23014p - c9;
        this.f23014p = i8;
        if (i8 > 0) {
            return 0;
        }
        this.f23008j.d(i(this.f23012n), 1, this.f23002d.f21023c, 0, null);
        this.f23012n += this.f23002d.f21027g;
        this.f23014p = 0;
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x009e, code lost:
        if (r13 == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00a0, code lost:
        r12.i(r1 + r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00a5, code lost:
        r12.h();
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00a8, code lost:
        r11.f23009k = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00aa, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean v(n4.l r12, boolean r13) {
        /*
            r11 = this;
            if (r13 == 0) goto L6
            r0 = 32768(0x8000, float:4.5918E-41)
            goto L8
        L6:
            r0 = 131072(0x20000, float:1.83671E-40)
        L8:
            r12.h()
            long r1 = r12.getPosition()
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            r2 = 0
            r3 = 1
            r4 = 0
            if (r1 != 0) goto L42
            int r1 = r11.f22999a
            r1 = r1 & 8
            if (r1 != 0) goto L20
            r1 = r3
            goto L21
        L20:
            r1 = r4
        L21:
            if (r1 == 0) goto L25
            r1 = r2
            goto L27
        L25:
            e5.b$a r1 = u4.f.f22998v
        L27:
            n4.w r5 = r11.f23004f
            com.google.android.exoplayer2.metadata.Metadata r1 = r5.a(r12, r1)
            r11.f23010l = r1
            if (r1 == 0) goto L36
            n4.v r5 = r11.f23003e
            r5.c(r1)
        L36:
            long r5 = r12.e()
            int r1 = (int) r5
            if (r13 != 0) goto L40
            r12.i(r1)
        L40:
            r5 = r4
            goto L44
        L42:
            r1 = r4
            r5 = r1
        L44:
            r6 = r5
            r7 = r6
        L46:
            boolean r8 = r11.s(r12)
            if (r8 == 0) goto L55
            if (r6 <= 0) goto L4f
            goto L9e
        L4f:
            java.io.EOFException r12 = new java.io.EOFException
            r12.<init>()
            throw r12
        L55:
            b6.z r8 = r11.f23001c
            r8.U(r4)
            b6.z r8 = r11.f23001c
            int r8 = r8.q()
            if (r5 == 0) goto L69
            long r9 = (long) r5
            boolean r9 = n(r8, r9)
            if (r9 == 0) goto L70
        L69:
            int r9 = k4.u.j(r8)
            r10 = -1
            if (r9 != r10) goto L90
        L70:
            int r5 = r7 + 1
            if (r7 != r0) goto L7e
            if (r13 == 0) goto L77
            return r4
        L77:
            java.lang.String r12 = "Searched too many bytes."
            com.google.android.exoplayer2.ParserException r12 = com.google.android.exoplayer2.ParserException.a(r12, r2)
            throw r12
        L7e:
            if (r13 == 0) goto L89
            r12.h()
            int r6 = r1 + r5
            r12.f(r6)
            goto L8c
        L89:
            r12.i(r3)
        L8c:
            r6 = r4
            r7 = r5
            r5 = r6
            goto L46
        L90:
            int r6 = r6 + 1
            if (r6 != r3) goto L9b
            k4.u$a r5 = r11.f23002d
            r5.a(r8)
            r5 = r8
            goto Lab
        L9b:
            r8 = 4
            if (r6 != r8) goto Lab
        L9e:
            if (r13 == 0) goto La5
            int r1 = r1 + r7
            r12.i(r1)
            goto La8
        La5:
            r12.h()
        La8:
            r11.f23009k = r5
            return r3
        Lab:
            int r9 = r9 + (-4)
            r12.f(r9)
            goto L46
        */
        throw new UnsupportedOperationException("Method not decompiled: u4.f.v(n4.l, boolean):boolean");
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f23006h = mVar;
        b0 e8 = mVar.e(0, 1);
        this.f23007i = e8;
        this.f23008j = e8;
        this.f23006h.o();
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f23009k = 0;
        this.f23011m = -9223372036854775807L;
        this.f23012n = 0L;
        this.f23014p = 0;
        this.f23017t = j9;
        g gVar = this.q;
        if (!(gVar instanceof b) || ((b) gVar).b(j9)) {
            return;
        }
        this.f23016s = true;
        this.f23008j = this.f23005g;
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        f();
        int t8 = t(lVar);
        if (t8 == -1 && (this.q instanceof b)) {
            long i8 = i(this.f23012n);
            if (this.q.d() != i8) {
                ((b) this.q).e(i8);
                this.f23006h.m(this.q);
            }
        }
        return t8;
    }

    @Override // n4.k
    public boolean g(l lVar) {
        return v(lVar, true);
    }

    public void j() {
        this.f23015r = true;
    }

    @Override // n4.k
    public void release() {
    }
}
