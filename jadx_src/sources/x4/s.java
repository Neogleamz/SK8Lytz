package x4;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Collections;
import k4.a;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s implements m {

    /* renamed from: a  reason: collision with root package name */
    private final String f24088a;

    /* renamed from: b  reason: collision with root package name */
    private final z f24089b;

    /* renamed from: c  reason: collision with root package name */
    private final b6.y f24090c;

    /* renamed from: d  reason: collision with root package name */
    private n4.b0 f24091d;

    /* renamed from: e  reason: collision with root package name */
    private String f24092e;

    /* renamed from: f  reason: collision with root package name */
    private w0 f24093f;

    /* renamed from: g  reason: collision with root package name */
    private int f24094g;

    /* renamed from: h  reason: collision with root package name */
    private int f24095h;

    /* renamed from: i  reason: collision with root package name */
    private int f24096i;

    /* renamed from: j  reason: collision with root package name */
    private int f24097j;

    /* renamed from: k  reason: collision with root package name */
    private long f24098k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f24099l;

    /* renamed from: m  reason: collision with root package name */
    private int f24100m;

    /* renamed from: n  reason: collision with root package name */
    private int f24101n;

    /* renamed from: o  reason: collision with root package name */
    private int f24102o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f24103p;
    private long q;

    /* renamed from: r  reason: collision with root package name */
    private int f24104r;

    /* renamed from: s  reason: collision with root package name */
    private long f24105s;

    /* renamed from: t  reason: collision with root package name */
    private int f24106t;

    /* renamed from: u  reason: collision with root package name */
    private String f24107u;

    public s(String str) {
        this.f24088a = str;
        z zVar = new z((int) RecognitionOptions.UPC_E);
        this.f24089b = zVar;
        this.f24090c = new b6.y(zVar.e());
        this.f24098k = -9223372036854775807L;
    }

    private static long a(b6.y yVar) {
        return yVar.h((yVar.h(2) + 1) * 8);
    }

    private void g(b6.y yVar) {
        if (!yVar.g()) {
            this.f24099l = true;
            l(yVar);
        } else if (!this.f24099l) {
            return;
        }
        if (this.f24100m != 0) {
            throw ParserException.a(null, null);
        }
        if (this.f24101n != 0) {
            throw ParserException.a(null, null);
        }
        k(yVar, j(yVar));
        if (this.f24103p) {
            yVar.r((int) this.q);
        }
    }

    private int h(b6.y yVar) {
        int b9 = yVar.b();
        a.b e8 = a.e(yVar, true);
        this.f24107u = e8.f20983c;
        this.f24104r = e8.f20981a;
        this.f24106t = e8.f20982b;
        return b9 - yVar.b();
    }

    private void i(b6.y yVar) {
        int i8;
        int h8 = yVar.h(3);
        this.f24102o = h8;
        if (h8 == 0) {
            i8 = 8;
        } else if (h8 != 1) {
            if (h8 == 3 || h8 == 4 || h8 == 5) {
                yVar.r(6);
                return;
            } else if (h8 != 6 && h8 != 7) {
                throw new IllegalStateException();
            } else {
                yVar.r(1);
                return;
            }
        } else {
            i8 = 9;
        }
        yVar.r(i8);
    }

    private int j(b6.y yVar) {
        int h8;
        if (this.f24102o == 0) {
            int i8 = 0;
            do {
                h8 = yVar.h(8);
                i8 += h8;
            } while (h8 == 255);
            return i8;
        }
        throw ParserException.a(null, null);
    }

    private void k(b6.y yVar, int i8) {
        int e8 = yVar.e();
        if ((e8 & 7) == 0) {
            this.f24089b.U(e8 >> 3);
        } else {
            yVar.i(this.f24089b.e(), 0, i8 * 8);
            this.f24089b.U(0);
        }
        this.f24091d.b(this.f24089b, i8);
        long j8 = this.f24098k;
        if (j8 != -9223372036854775807L) {
            this.f24091d.d(j8, 1, i8, 0, null);
            this.f24098k += this.f24105s;
        }
    }

    private void l(b6.y yVar) {
        boolean g8;
        int h8 = yVar.h(1);
        int h9 = h8 == 1 ? yVar.h(1) : 0;
        this.f24100m = h9;
        if (h9 != 0) {
            throw ParserException.a(null, null);
        }
        if (h8 == 1) {
            a(yVar);
        }
        if (!yVar.g()) {
            throw ParserException.a(null, null);
        }
        this.f24101n = yVar.h(6);
        int h10 = yVar.h(4);
        int h11 = yVar.h(3);
        if (h10 != 0 || h11 != 0) {
            throw ParserException.a(null, null);
        }
        if (h8 == 0) {
            int e8 = yVar.e();
            int h12 = h(yVar);
            yVar.p(e8);
            byte[] bArr = new byte[(h12 + 7) / 8];
            yVar.i(bArr, 0, h12);
            w0 G = new w0.b().U(this.f24092e).g0("audio/mp4a-latm").K(this.f24107u).J(this.f24106t).h0(this.f24104r).V(Collections.singletonList(bArr)).X(this.f24088a).G();
            if (!G.equals(this.f24093f)) {
                this.f24093f = G;
                this.f24105s = 1024000000 / G.G;
                this.f24091d.f(G);
            }
        } else {
            yVar.r(((int) a(yVar)) - h(yVar));
        }
        i(yVar);
        boolean g9 = yVar.g();
        this.f24103p = g9;
        this.q = 0L;
        if (g9) {
            if (h8 == 1) {
                this.q = a(yVar);
            } else {
                do {
                    g8 = yVar.g();
                    this.q = (this.q << 8) + yVar.h(8);
                } while (g8);
            }
        }
        if (yVar.g()) {
            yVar.r(8);
        }
    }

    private void m(int i8) {
        this.f24089b.Q(i8);
        this.f24090c.n(this.f24089b.e());
    }

    @Override // x4.m
    public void b(z zVar) {
        b6.a.h(this.f24091d);
        while (zVar.a() > 0) {
            int i8 = this.f24094g;
            if (i8 != 0) {
                if (i8 == 1) {
                    int H = zVar.H();
                    if ((H & 224) == 224) {
                        this.f24097j = H;
                        this.f24094g = 2;
                    } else if (H != 86) {
                        this.f24094g = 0;
                    }
                } else if (i8 == 2) {
                    int H2 = ((this.f24097j & (-225)) << 8) | zVar.H();
                    this.f24096i = H2;
                    if (H2 > this.f24089b.e().length) {
                        m(this.f24096i);
                    }
                    this.f24095h = 0;
                    this.f24094g = 3;
                } else if (i8 != 3) {
                    throw new IllegalStateException();
                } else {
                    int min = Math.min(zVar.a(), this.f24096i - this.f24095h);
                    zVar.l(this.f24090c.f8152a, this.f24095h, min);
                    int i9 = this.f24095h + min;
                    this.f24095h = i9;
                    if (i9 == this.f24096i) {
                        this.f24090c.p(0);
                        g(this.f24090c);
                        this.f24094g = 0;
                    }
                }
            } else if (zVar.H() == 86) {
                this.f24094g = 1;
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f24094g = 0;
        this.f24098k = -9223372036854775807L;
        this.f24099l = false;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f24091d = mVar.e(dVar.c(), 1);
        this.f24092e = dVar.b();
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f24098k = j8;
        }
    }
}
