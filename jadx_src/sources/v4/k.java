package v4;

import android.util.Pair;
import b6.l0;
import b6.u;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.mp4.MotionPhotoMetadata;
import com.google.android.exoplayer2.w0;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import n4.b0;
import n4.c0;
import n4.v;
import n4.y;
import n4.z;
import v4.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements n4.k, z {

    /* renamed from: y  reason: collision with root package name */
    public static final n4.p f23258y = j.b;

    /* renamed from: a  reason: collision with root package name */
    private final int f23259a;

    /* renamed from: b  reason: collision with root package name */
    private final b6.z f23260b;

    /* renamed from: c  reason: collision with root package name */
    private final b6.z f23261c;

    /* renamed from: d  reason: collision with root package name */
    private final b6.z f23262d;

    /* renamed from: e  reason: collision with root package name */
    private final b6.z f23263e;

    /* renamed from: f  reason: collision with root package name */
    private final ArrayDeque<a.C0216a> f23264f;

    /* renamed from: g  reason: collision with root package name */
    private final m f23265g;

    /* renamed from: h  reason: collision with root package name */
    private final List<Metadata.Entry> f23266h;

    /* renamed from: i  reason: collision with root package name */
    private int f23267i;

    /* renamed from: j  reason: collision with root package name */
    private int f23268j;

    /* renamed from: k  reason: collision with root package name */
    private long f23269k;

    /* renamed from: l  reason: collision with root package name */
    private int f23270l;

    /* renamed from: m  reason: collision with root package name */
    private b6.z f23271m;

    /* renamed from: n  reason: collision with root package name */
    private int f23272n;

    /* renamed from: o  reason: collision with root package name */
    private int f23273o;

    /* renamed from: p  reason: collision with root package name */
    private int f23274p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private n4.m f23275r;

    /* renamed from: s  reason: collision with root package name */
    private a[] f23276s;

    /* renamed from: t  reason: collision with root package name */
    private long[][] f23277t;

    /* renamed from: u  reason: collision with root package name */
    private int f23278u;

    /* renamed from: v  reason: collision with root package name */
    private long f23279v;

    /* renamed from: w  reason: collision with root package name */
    private int f23280w;

    /* renamed from: x  reason: collision with root package name */
    private MotionPhotoMetadata f23281x;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final o f23282a;

        /* renamed from: b  reason: collision with root package name */
        public final r f23283b;

        /* renamed from: c  reason: collision with root package name */
        public final b0 f23284c;

        /* renamed from: d  reason: collision with root package name */
        public final c0 f23285d;

        /* renamed from: e  reason: collision with root package name */
        public int f23286e;

        public a(o oVar, r rVar, b0 b0Var) {
            this.f23282a = oVar;
            this.f23283b = rVar;
            this.f23284c = b0Var;
            this.f23285d = "audio/true-hd".equals(oVar.f23304f.f11207m) ? new c0() : null;
        }
    }

    public k() {
        this(0);
    }

    public k(int i8) {
        this.f23259a = i8;
        this.f23267i = (i8 & 4) != 0 ? 3 : 0;
        this.f23265g = new m();
        this.f23266h = new ArrayList();
        this.f23263e = new b6.z(16);
        this.f23264f = new ArrayDeque<>();
        this.f23260b = new b6.z(u.f8109a);
        this.f23261c = new b6.z(4);
        this.f23262d = new b6.z();
        this.f23272n = -1;
        this.f23275r = n4.m.S;
        this.f23276s = new a[0];
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0108  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean A(n4.l r9) {
        /*
            Method dump skipped, instructions count: 271
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.k.A(n4.l):boolean");
    }

    private boolean B(n4.l lVar, y yVar) {
        boolean z4;
        long j8 = this.f23269k - this.f23270l;
        long position = lVar.getPosition() + j8;
        b6.z zVar = this.f23271m;
        if (zVar != null) {
            lVar.readFully(zVar.e(), this.f23270l, (int) j8);
            if (this.f23268j == 1718909296) {
                this.f23280w = x(zVar);
            } else if (!this.f23264f.isEmpty()) {
                this.f23264f.peek().e(new a.b(this.f23268j, zVar));
            }
        } else if (j8 >= 262144) {
            yVar.f22152a = lVar.getPosition() + j8;
            z4 = true;
            v(position);
            return (z4 || this.f23267i == 2) ? false : true;
        } else {
            lVar.i((int) j8);
        }
        z4 = false;
        v(position);
        if (z4) {
        }
    }

    private int C(n4.l lVar, y yVar) {
        int i8;
        y yVar2;
        long position = lVar.getPosition();
        if (this.f23272n == -1) {
            int q = q(position);
            this.f23272n = q;
            if (q == -1) {
                return -1;
            }
        }
        a aVar = this.f23276s[this.f23272n];
        b0 b0Var = aVar.f23284c;
        int i9 = aVar.f23286e;
        r rVar = aVar.f23283b;
        long j8 = rVar.f23334c[i9];
        int i10 = rVar.f23335d[i9];
        c0 c0Var = aVar.f23285d;
        long j9 = (j8 - position) + this.f23273o;
        if (j9 < 0) {
            i8 = 1;
            yVar2 = yVar;
        } else if (j9 < 262144) {
            if (aVar.f23282a.f23305g == 1) {
                j9 += 8;
                i10 -= 8;
            }
            lVar.i((int) j9);
            o oVar = aVar.f23282a;
            if (oVar.f23308j == 0) {
                if ("audio/ac4".equals(oVar.f23304f.f11207m)) {
                    if (this.f23274p == 0) {
                        k4.c.a(i10, this.f23262d);
                        b0Var.b(this.f23262d, 7);
                        this.f23274p += 7;
                    }
                    i10 += 7;
                } else if (c0Var != null) {
                    c0Var.d(lVar);
                }
                while (true) {
                    int i11 = this.f23274p;
                    if (i11 >= i10) {
                        break;
                    }
                    int c9 = b0Var.c(lVar, i10 - i11, false);
                    this.f23273o += c9;
                    this.f23274p += c9;
                    this.q -= c9;
                }
            } else {
                byte[] e8 = this.f23261c.e();
                e8[0] = 0;
                e8[1] = 0;
                e8[2] = 0;
                int i12 = aVar.f23282a.f23308j;
                int i13 = 4 - i12;
                while (this.f23274p < i10) {
                    int i14 = this.q;
                    if (i14 == 0) {
                        lVar.readFully(e8, i13, i12);
                        this.f23273o += i12;
                        this.f23261c.U(0);
                        int q8 = this.f23261c.q();
                        if (q8 < 0) {
                            throw ParserException.a("Invalid NAL length", null);
                        }
                        this.q = q8;
                        this.f23260b.U(0);
                        b0Var.b(this.f23260b, 4);
                        this.f23274p += 4;
                        i10 += i13;
                    } else {
                        int c10 = b0Var.c(lVar, i14, false);
                        this.f23273o += c10;
                        this.f23274p += c10;
                        this.q -= c10;
                    }
                }
            }
            int i15 = i10;
            r rVar2 = aVar.f23283b;
            long j10 = rVar2.f23337f[i9];
            int i16 = rVar2.f23338g[i9];
            if (c0Var != null) {
                c0Var.c(b0Var, j10, i16, i15, 0, null);
                if (i9 + 1 == aVar.f23283b.f23333b) {
                    c0Var.a(b0Var, null);
                }
            } else {
                b0Var.d(j10, i16, i15, 0, null);
            }
            aVar.f23286e++;
            this.f23272n = -1;
            this.f23273o = 0;
            this.f23274p = 0;
            this.q = 0;
            return 0;
        } else {
            yVar2 = yVar;
            i8 = 1;
        }
        yVar2.f22152a = j8;
        return i8;
    }

    private int D(n4.l lVar, y yVar) {
        int c9 = this.f23265g.c(lVar, yVar, this.f23266h);
        if (c9 == 1 && yVar.f22152a == 0) {
            n();
        }
        return c9;
    }

    private static boolean E(int i8) {
        return i8 == 1836019574 || i8 == 1953653099 || i8 == 1835297121 || i8 == 1835626086 || i8 == 1937007212 || i8 == 1701082227 || i8 == 1835365473;
    }

    private static boolean F(int i8) {
        return i8 == 1835296868 || i8 == 1836476516 || i8 == 1751411826 || i8 == 1937011556 || i8 == 1937011827 || i8 == 1937011571 || i8 == 1668576371 || i8 == 1701606260 || i8 == 1937011555 || i8 == 1937011578 || i8 == 1937013298 || i8 == 1937007471 || i8 == 1668232756 || i8 == 1953196132 || i8 == 1718909296 || i8 == 1969517665 || i8 == 1801812339 || i8 == 1768715124;
    }

    private void G(a aVar, long j8) {
        r rVar = aVar.f23283b;
        int a9 = rVar.a(j8);
        if (a9 == -1) {
            a9 = rVar.b(j8);
        }
        aVar.f23286e = a9;
    }

    private static int l(int i8) {
        if (i8 != 1751476579) {
            return i8 != 1903435808 ? 0 : 1;
        }
        return 2;
    }

    private static long[][] m(a[] aVarArr) {
        long[][] jArr = new long[aVarArr.length];
        int[] iArr = new int[aVarArr.length];
        long[] jArr2 = new long[aVarArr.length];
        boolean[] zArr = new boolean[aVarArr.length];
        for (int i8 = 0; i8 < aVarArr.length; i8++) {
            jArr[i8] = new long[aVarArr[i8].f23283b.f23333b];
            jArr2[i8] = aVarArr[i8].f23283b.f23337f[0];
        }
        long j8 = 0;
        int i9 = 0;
        while (i9 < aVarArr.length) {
            long j9 = Long.MAX_VALUE;
            int i10 = -1;
            for (int i11 = 0; i11 < aVarArr.length; i11++) {
                if (!zArr[i11] && jArr2[i11] <= j9) {
                    j9 = jArr2[i11];
                    i10 = i11;
                }
            }
            int i12 = iArr[i10];
            jArr[i10][i12] = j8;
            j8 += aVarArr[i10].f23283b.f23335d[i12];
            int i13 = i12 + 1;
            iArr[i10] = i13;
            if (i13 < jArr[i10].length) {
                jArr2[i10] = aVarArr[i10].f23283b.f23337f[i13];
            } else {
                zArr[i10] = true;
                i9++;
            }
        }
        return jArr;
    }

    private void n() {
        this.f23267i = 0;
        this.f23270l = 0;
    }

    private static int p(r rVar, long j8) {
        int a9 = rVar.a(j8);
        return a9 == -1 ? rVar.b(j8) : a9;
    }

    private int q(long j8) {
        int i8 = -1;
        int i9 = -1;
        int i10 = 0;
        long j9 = Long.MAX_VALUE;
        boolean z4 = true;
        long j10 = Long.MAX_VALUE;
        boolean z8 = true;
        long j11 = Long.MAX_VALUE;
        while (true) {
            a[] aVarArr = this.f23276s;
            if (i10 >= aVarArr.length) {
                break;
            }
            a aVar = aVarArr[i10];
            int i11 = aVar.f23286e;
            r rVar = aVar.f23283b;
            if (i11 != rVar.f23333b) {
                long j12 = rVar.f23334c[i11];
                long j13 = ((long[][]) l0.j(this.f23277t))[i10][i11];
                long j14 = j12 - j8;
                boolean z9 = j14 < 0 || j14 >= 262144;
                if ((!z9 && z8) || (z9 == z8 && j14 < j11)) {
                    z8 = z9;
                    j11 = j14;
                    i9 = i10;
                    j10 = j13;
                }
                if (j13 < j9) {
                    z4 = z9;
                    i8 = i10;
                    j9 = j13;
                }
            }
            i10++;
        }
        return (j9 == Long.MAX_VALUE || !z4 || j10 < j9 + 10485760) ? i9 : i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ o r(o oVar) {
        return oVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] s() {
        return new n4.k[]{new k()};
    }

    private static long t(r rVar, long j8, long j9) {
        int p8 = p(rVar, j8);
        return p8 == -1 ? j9 : Math.min(rVar.f23334c[p8], j9);
    }

    private void u(n4.l lVar) {
        this.f23262d.Q(8);
        lVar.k(this.f23262d.e(), 0, 8);
        b.e(this.f23262d);
        lVar.i(this.f23262d.f());
        lVar.h();
    }

    private void v(long j8) {
        while (!this.f23264f.isEmpty() && this.f23264f.peek().f23174b == j8) {
            a.C0216a pop = this.f23264f.pop();
            if (pop.f23173a == 1836019574) {
                y(pop);
                this.f23264f.clear();
                this.f23267i = 2;
            } else if (!this.f23264f.isEmpty()) {
                this.f23264f.peek().d(pop);
            }
        }
        if (this.f23267i != 2) {
            n();
        }
    }

    private void w() {
        if (this.f23280w != 2 || (this.f23259a & 2) == 0) {
            return;
        }
        this.f23275r.e(0, 4).f(new w0.b().Z(this.f23281x == null ? null : new Metadata(this.f23281x)).G());
        this.f23275r.o();
        this.f23275r.m(new z.b(-9223372036854775807L));
    }

    private static int x(b6.z zVar) {
        zVar.U(8);
        int l8 = l(zVar.q());
        if (l8 != 0) {
            return l8;
        }
        zVar.V(4);
        while (zVar.a() > 0) {
            int l9 = l(zVar.q());
            if (l9 != 0) {
                return l9;
            }
        }
        return 0;
    }

    private void y(a.C0216a c0216a) {
        Metadata metadata;
        Metadata metadata2;
        List<r> list;
        int i8;
        int i9;
        ArrayList arrayList = new ArrayList();
        boolean z4 = this.f23280w == 1;
        v vVar = new v();
        a.b g8 = c0216a.g(1969517665);
        if (g8 != null) {
            Pair<Metadata, Metadata> B = b.B(g8);
            Metadata metadata3 = (Metadata) B.first;
            Metadata metadata4 = (Metadata) B.second;
            if (metadata3 != null) {
                vVar.c(metadata3);
            }
            metadata = metadata4;
            metadata2 = metadata3;
        } else {
            metadata = null;
            metadata2 = null;
        }
        a.C0216a f5 = c0216a.f(1835365473);
        long j8 = -9223372036854775807L;
        Metadata n8 = f5 != null ? b.n(f5) : null;
        List<r> A = b.A(c0216a, vVar, -9223372036854775807L, null, (this.f23259a & 1) != 0, z4, i.a);
        int size = A.size();
        long j9 = -9223372036854775807L;
        int i10 = 0;
        int i11 = -1;
        while (i10 < size) {
            r rVar = A.get(i10);
            if (rVar.f23333b == 0) {
                list = A;
                i8 = size;
            } else {
                o oVar = rVar.f23332a;
                list = A;
                i8 = size;
                long j10 = oVar.f23303e;
                if (j10 == j8) {
                    j10 = rVar.f23339h;
                }
                long max = Math.max(j9, j10);
                a aVar = new a(oVar, rVar, this.f23275r.e(i10, oVar.f23300b));
                int i12 = "audio/true-hd".equals(oVar.f23304f.f11207m) ? rVar.f23336e * 16 : rVar.f23336e + 30;
                w0.b b9 = oVar.f23304f.b();
                b9.Y(i12);
                if (oVar.f23300b == 2 && j10 > 0 && (i9 = rVar.f23333b) > 1) {
                    b9.R(i9 / (((float) j10) / 1000000.0f));
                }
                h.k(oVar.f23300b, vVar, b9);
                int i13 = oVar.f23300b;
                Metadata[] metadataArr = new Metadata[2];
                metadataArr[0] = metadata;
                metadataArr[1] = this.f23266h.isEmpty() ? null : new Metadata(this.f23266h);
                h.l(i13, metadata2, n8, b9, metadataArr);
                aVar.f23284c.f(b9.G());
                if (oVar.f23300b == 2 && i11 == -1) {
                    i11 = arrayList.size();
                }
                arrayList.add(aVar);
                j9 = max;
            }
            i10++;
            A = list;
            size = i8;
            j8 = -9223372036854775807L;
        }
        this.f23278u = i11;
        this.f23279v = j9;
        a[] aVarArr = (a[]) arrayList.toArray(new a[0]);
        this.f23276s = aVarArr;
        this.f23277t = m(aVarArr);
        this.f23275r.o();
        this.f23275r.m(this);
    }

    private void z(long j8) {
        if (this.f23268j == 1836086884) {
            int i8 = this.f23270l;
            this.f23281x = new MotionPhotoMetadata(0L, j8, -9223372036854775807L, j8 + i8, this.f23269k - i8);
        }
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23275r = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        a[] aVarArr;
        this.f23264f.clear();
        this.f23270l = 0;
        this.f23272n = -1;
        this.f23273o = 0;
        this.f23274p = 0;
        this.q = 0;
        if (j8 == 0) {
            if (this.f23267i != 3) {
                n();
                return;
            }
            this.f23265g.g();
            this.f23266h.clear();
            return;
        }
        for (a aVar : this.f23276s) {
            G(aVar, j9);
            c0 c0Var = aVar.f23285d;
            if (c0Var != null) {
                c0Var.b();
            }
        }
    }

    @Override // n4.z
    public long d() {
        return this.f23279v;
    }

    @Override // n4.k
    public int e(n4.l lVar, y yVar) {
        while (true) {
            int i8 = this.f23267i;
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        if (i8 == 3) {
                            return D(lVar, yVar);
                        }
                        throw new IllegalStateException();
                    }
                    return C(lVar, yVar);
                } else if (B(lVar, yVar)) {
                    return 1;
                }
            } else if (!A(lVar)) {
                return -1;
            }
        }
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        return n.d(lVar, (this.f23259a & 2) != 0);
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        return o(j8, -1);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public n4.z.a o(long r17, int r19) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r3 = r19
            v4.k$a[] r4 = r0.f23276s
            int r5 = r4.length
            if (r5 != 0) goto L13
            n4.z$a r1 = new n4.z$a
            n4.a0 r2 = n4.a0.f22045c
            r1.<init>(r2)
            return r1
        L13:
            r5 = -1
            r7 = -1
            if (r3 == r7) goto L1a
            r8 = r3
            goto L1c
        L1a:
            int r8 = r0.f23278u
        L1c:
            r9 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            if (r8 == r7) goto L58
            r4 = r4[r8]
            v4.r r4 = r4.f23283b
            int r8 = p(r4, r1)
            if (r8 != r7) goto L35
            n4.z$a r1 = new n4.z$a
            n4.a0 r2 = n4.a0.f22045c
            r1.<init>(r2)
            return r1
        L35:
            long[] r11 = r4.f23337f
            r12 = r11[r8]
            long[] r11 = r4.f23334c
            r14 = r11[r8]
            int r11 = (r12 > r1 ? 1 : (r12 == r1 ? 0 : -1))
            if (r11 >= 0) goto L5e
            int r11 = r4.f23333b
            int r11 = r11 + (-1)
            if (r8 >= r11) goto L5e
            int r1 = r4.b(r1)
            if (r1 == r7) goto L5e
            if (r1 == r8) goto L5e
            long[] r2 = r4.f23337f
            r5 = r2[r1]
            long[] r2 = r4.f23334c
            r1 = r2[r1]
            goto L60
        L58:
            r14 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r12 = r1
        L5e:
            r1 = r5
            r5 = r9
        L60:
            if (r3 != r7) goto L7f
            r3 = 0
        L63:
            v4.k$a[] r4 = r0.f23276s
            int r7 = r4.length
            if (r3 >= r7) goto L7f
            int r7 = r0.f23278u
            if (r3 == r7) goto L7c
            r4 = r4[r3]
            v4.r r4 = r4.f23283b
            long r14 = t(r4, r12, r14)
            int r7 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1))
            if (r7 == 0) goto L7c
            long r1 = t(r4, r5, r1)
        L7c:
            int r3 = r3 + 1
            goto L63
        L7f:
            n4.a0 r3 = new n4.a0
            r3.<init>(r12, r14)
            int r4 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1))
            if (r4 != 0) goto L8e
            n4.z$a r1 = new n4.z$a
            r1.<init>(r3)
            return r1
        L8e:
            n4.a0 r4 = new n4.a0
            r4.<init>(r5, r1)
            n4.z$a r1 = new n4.z$a
            r1.<init>(r3, r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.k.o(long, int):n4.z$a");
    }

    @Override // n4.k
    public void release() {
    }
}
