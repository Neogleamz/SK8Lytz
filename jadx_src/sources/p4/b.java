package p4;

import b6.p;
import b6.t;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.d3;
import java.util.ArrayList;
import n4.b0;
import n4.i;
import n4.k;
import n4.l;
import n4.m;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements k {

    /* renamed from: c  reason: collision with root package name */
    private int f22328c;

    /* renamed from: e  reason: collision with root package name */
    private p4.c f22330e;

    /* renamed from: h  reason: collision with root package name */
    private long f22333h;

    /* renamed from: i  reason: collision with root package name */
    private e f22334i;

    /* renamed from: m  reason: collision with root package name */
    private int f22338m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f22339n;

    /* renamed from: a  reason: collision with root package name */
    private final z f22326a = new z(12);

    /* renamed from: b  reason: collision with root package name */
    private final c f22327b = new c();

    /* renamed from: d  reason: collision with root package name */
    private m f22329d = new i();

    /* renamed from: g  reason: collision with root package name */
    private e[] f22332g = new e[0];

    /* renamed from: k  reason: collision with root package name */
    private long f22336k = -1;

    /* renamed from: l  reason: collision with root package name */
    private long f22337l = -1;

    /* renamed from: j  reason: collision with root package name */
    private int f22335j = -1;

    /* renamed from: f  reason: collision with root package name */
    private long f22331f = -9223372036854775807L;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: p4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0195b implements n4.z {

        /* renamed from: a  reason: collision with root package name */
        private final long f22340a;

        public C0195b(long j8) {
            this.f22340a = j8;
        }

        @Override // n4.z
        public long d() {
            return this.f22340a;
        }

        @Override // n4.z
        public boolean h() {
            return true;
        }

        @Override // n4.z
        public z.a i(long j8) {
            z.a i8 = b.this.f22332g[0].i(j8);
            for (int i9 = 1; i9 < b.this.f22332g.length; i9++) {
                z.a i10 = b.this.f22332g[i9].i(j8);
                if (i10.f22153a.f22047b < i8.f22153a.f22047b) {
                    i8 = i10;
                }
            }
            return i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {

        /* renamed from: a  reason: collision with root package name */
        public int f22342a;

        /* renamed from: b  reason: collision with root package name */
        public int f22343b;

        /* renamed from: c  reason: collision with root package name */
        public int f22344c;

        private c() {
        }

        public void a(b6.z zVar) {
            this.f22342a = zVar.u();
            this.f22343b = zVar.u();
            this.f22344c = 0;
        }

        public void b(b6.z zVar) {
            a(zVar);
            if (this.f22342a == 1414744396) {
                this.f22344c = zVar.u();
                return;
            }
            throw ParserException.a("LIST expected, found: " + this.f22342a, null);
        }
    }

    private static void d(l lVar) {
        if ((lVar.getPosition() & 1) == 1) {
            lVar.i(1);
        }
    }

    private e f(int i8) {
        e[] eVarArr;
        for (e eVar : this.f22332g) {
            if (eVar.j(i8)) {
                return eVar;
            }
        }
        return null;
    }

    private void h(b6.z zVar) {
        f c9 = f.c(1819436136, zVar);
        if (c9.getType() != 1819436136) {
            throw ParserException.a("Unexpected header list type " + c9.getType(), null);
        }
        p4.c cVar = (p4.c) c9.b(p4.c.class);
        if (cVar == null) {
            throw ParserException.a("AviHeader not found", null);
        }
        this.f22330e = cVar;
        this.f22331f = cVar.f22347c * cVar.f22345a;
        ArrayList arrayList = new ArrayList();
        d3<p4.a> it = c9.f22367a.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            p4.a next = it.next();
            if (next.getType() == 1819440243) {
                int i9 = i8 + 1;
                e k8 = k((f) next, i8);
                if (k8 != null) {
                    arrayList.add(k8);
                }
                i8 = i9;
            }
        }
        this.f22332g = (e[]) arrayList.toArray(new e[0]);
        this.f22329d.o();
    }

    private void i(b6.z zVar) {
        long j8 = j(zVar);
        while (zVar.a() >= 16) {
            int u8 = zVar.u();
            int u9 = zVar.u();
            long u10 = zVar.u() + j8;
            zVar.u();
            e f5 = f(u8);
            if (f5 != null) {
                if ((u9 & 16) == 16) {
                    f5.b(u10);
                }
                f5.k();
            }
        }
        for (e eVar : this.f22332g) {
            eVar.c();
        }
        this.f22339n = true;
        this.f22329d.m(new C0195b(this.f22331f));
    }

    private long j(b6.z zVar) {
        if (zVar.a() < 16) {
            return 0L;
        }
        int f5 = zVar.f();
        zVar.V(8);
        long j8 = this.f22336k;
        long j9 = ((long) zVar.u()) <= j8 ? 8 + j8 : 0L;
        zVar.U(f5);
        return j9;
    }

    private e k(f fVar, int i8) {
        String str;
        d dVar = (d) fVar.b(d.class);
        g gVar = (g) fVar.b(g.class);
        if (dVar == null) {
            str = "Missing Stream Header";
        } else if (gVar != null) {
            long a9 = dVar.a();
            w0 w0Var = gVar.f22369a;
            w0.b b9 = w0Var.b();
            b9.T(i8);
            int i9 = dVar.f22354f;
            if (i9 != 0) {
                b9.Y(i9);
            }
            h hVar = (h) fVar.b(h.class);
            if (hVar != null) {
                b9.W(hVar.f22370a);
            }
            int k8 = t.k(w0Var.f11207m);
            if (k8 == 1 || k8 == 2) {
                b0 e8 = this.f22329d.e(i8, k8);
                e8.f(b9.G());
                e eVar = new e(i8, k8, a9, dVar.f22353e, e8);
                this.f22331f = a9;
                return eVar;
            }
            return null;
        } else {
            str = "Missing Stream Format";
        }
        p.i("AviExtractor", str);
        return null;
    }

    private int l(l lVar) {
        if (lVar.getPosition() >= this.f22337l) {
            return -1;
        }
        e eVar = this.f22334i;
        if (eVar == null) {
            d(lVar);
            lVar.k(this.f22326a.e(), 0, 12);
            this.f22326a.U(0);
            int u8 = this.f22326a.u();
            if (u8 == 1414744396) {
                this.f22326a.U(8);
                lVar.i(this.f22326a.u() != 1769369453 ? 8 : 12);
                lVar.h();
                return 0;
            }
            int u9 = this.f22326a.u();
            if (u8 == 1263424842) {
                this.f22333h = lVar.getPosition() + u9 + 8;
                return 0;
            }
            lVar.i(8);
            lVar.h();
            e f5 = f(u8);
            if (f5 == null) {
                this.f22333h = lVar.getPosition() + u9;
                return 0;
            }
            f5.n(u9);
            this.f22334i = f5;
        } else if (eVar.m(lVar)) {
            this.f22334i = null;
        }
        return 0;
    }

    private boolean m(l lVar, y yVar) {
        boolean z4;
        if (this.f22333h != -1) {
            long position = lVar.getPosition();
            long j8 = this.f22333h;
            if (j8 < position || j8 > 262144 + position) {
                yVar.f22152a = j8;
                z4 = true;
                this.f22333h = -1L;
                return z4;
            }
            lVar.i((int) (j8 - position));
        }
        z4 = false;
        this.f22333h = -1L;
        return z4;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f22328c = 0;
        this.f22329d = mVar;
        this.f22333h = -1L;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f22333h = -1L;
        this.f22334i = null;
        for (e eVar : this.f22332g) {
            eVar.o(j8);
        }
        if (j8 != 0) {
            this.f22328c = 6;
        } else if (this.f22332g.length == 0) {
            this.f22328c = 0;
        } else {
            this.f22328c = 3;
        }
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        if (m(lVar, yVar)) {
            return 1;
        }
        switch (this.f22328c) {
            case 0:
                if (g(lVar)) {
                    lVar.i(12);
                    this.f22328c = 1;
                    return 0;
                }
                throw ParserException.a("AVI Header List not found", null);
            case 1:
                lVar.readFully(this.f22326a.e(), 0, 12);
                this.f22326a.U(0);
                this.f22327b.b(this.f22326a);
                c cVar = this.f22327b;
                if (cVar.f22344c == 1819436136) {
                    this.f22335j = cVar.f22343b;
                    this.f22328c = 2;
                    return 0;
                }
                throw ParserException.a("hdrl expected, found: " + this.f22327b.f22344c, null);
            case 2:
                int i8 = this.f22335j - 4;
                b6.z zVar = new b6.z(i8);
                lVar.readFully(zVar.e(), 0, i8);
                h(zVar);
                this.f22328c = 3;
                return 0;
            case 3:
                if (this.f22336k != -1) {
                    long position = lVar.getPosition();
                    long j8 = this.f22336k;
                    if (position != j8) {
                        this.f22333h = j8;
                        return 0;
                    }
                }
                lVar.k(this.f22326a.e(), 0, 12);
                lVar.h();
                this.f22326a.U(0);
                this.f22327b.a(this.f22326a);
                int u8 = this.f22326a.u();
                int i9 = this.f22327b.f22342a;
                if (i9 == 1179011410) {
                    lVar.i(12);
                    return 0;
                } else if (i9 != 1414744396 || u8 != 1769369453) {
                    this.f22333h = lVar.getPosition() + this.f22327b.f22343b + 8;
                    return 0;
                } else {
                    long position2 = lVar.getPosition();
                    this.f22336k = position2;
                    this.f22337l = position2 + this.f22327b.f22343b + 8;
                    if (!this.f22339n) {
                        if (((p4.c) b6.a.e(this.f22330e)).a()) {
                            this.f22328c = 4;
                            this.f22333h = this.f22337l;
                            return 0;
                        }
                        this.f22329d.m(new z.b(this.f22331f));
                        this.f22339n = true;
                    }
                    this.f22333h = lVar.getPosition() + 12;
                    this.f22328c = 6;
                    return 0;
                }
            case 4:
                lVar.readFully(this.f22326a.e(), 0, 8);
                this.f22326a.U(0);
                int u9 = this.f22326a.u();
                int u10 = this.f22326a.u();
                if (u9 == 829973609) {
                    this.f22328c = 5;
                    this.f22338m = u10;
                } else {
                    this.f22333h = lVar.getPosition() + u10;
                }
                return 0;
            case 5:
                b6.z zVar2 = new b6.z(this.f22338m);
                lVar.readFully(zVar2.e(), 0, this.f22338m);
                i(zVar2);
                this.f22328c = 6;
                this.f22333h = this.f22336k;
                return 0;
            case 6:
                return l(lVar);
            default:
                throw new AssertionError();
        }
    }

    @Override // n4.k
    public boolean g(l lVar) {
        lVar.k(this.f22326a.e(), 0, 12);
        this.f22326a.U(0);
        if (this.f22326a.u() != 1179011410) {
            return false;
        }
        this.f22326a.V(4);
        return this.f22326a.u() == 541677121;
    }

    @Override // n4.k
    public void release() {
    }
}
