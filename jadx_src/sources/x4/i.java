package x4;

import b6.a;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import java.util.Collections;
import k4.a;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements m {

    /* renamed from: v  reason: collision with root package name */
    private static final byte[] f23906v = {73, 68, 51};

    /* renamed from: a  reason: collision with root package name */
    private final boolean f23907a;

    /* renamed from: b  reason: collision with root package name */
    private final b6.y f23908b;

    /* renamed from: c  reason: collision with root package name */
    private final z f23909c;

    /* renamed from: d  reason: collision with root package name */
    private final String f23910d;

    /* renamed from: e  reason: collision with root package name */
    private String f23911e;

    /* renamed from: f  reason: collision with root package name */
    private n4.b0 f23912f;

    /* renamed from: g  reason: collision with root package name */
    private n4.b0 f23913g;

    /* renamed from: h  reason: collision with root package name */
    private int f23914h;

    /* renamed from: i  reason: collision with root package name */
    private int f23915i;

    /* renamed from: j  reason: collision with root package name */
    private int f23916j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f23917k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f23918l;

    /* renamed from: m  reason: collision with root package name */
    private int f23919m;

    /* renamed from: n  reason: collision with root package name */
    private int f23920n;

    /* renamed from: o  reason: collision with root package name */
    private int f23921o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f23922p;
    private long q;

    /* renamed from: r  reason: collision with root package name */
    private int f23923r;

    /* renamed from: s  reason: collision with root package name */
    private long f23924s;

    /* renamed from: t  reason: collision with root package name */
    private n4.b0 f23925t;

    /* renamed from: u  reason: collision with root package name */
    private long f23926u;

    public i(boolean z4) {
        this(z4, null);
    }

    public i(boolean z4, String str) {
        this.f23908b = new b6.y(new byte[7]);
        this.f23909c = new z(Arrays.copyOf(f23906v, 10));
        s();
        this.f23919m = -1;
        this.f23920n = -1;
        this.q = -9223372036854775807L;
        this.f23924s = -9223372036854775807L;
        this.f23907a = z4;
        this.f23910d = str;
    }

    private void a() {
        a.e(this.f23912f);
        l0.j(this.f23925t);
        l0.j(this.f23913g);
    }

    private void g(z zVar) {
        if (zVar.a() == 0) {
            return;
        }
        this.f23908b.f8152a[0] = zVar.e()[zVar.f()];
        this.f23908b.p(2);
        int h8 = this.f23908b.h(4);
        int i8 = this.f23920n;
        if (i8 != -1 && h8 != i8) {
            q();
            return;
        }
        if (!this.f23918l) {
            this.f23918l = true;
            this.f23919m = this.f23921o;
            this.f23920n = h8;
        }
        t();
    }

    private boolean h(z zVar, int i8) {
        zVar.U(i8 + 1);
        if (w(zVar, this.f23908b.f8152a, 1)) {
            this.f23908b.p(4);
            int h8 = this.f23908b.h(1);
            int i9 = this.f23919m;
            if (i9 == -1 || h8 == i9) {
                if (this.f23920n != -1) {
                    if (!w(zVar, this.f23908b.f8152a, 1)) {
                        return true;
                    }
                    this.f23908b.p(2);
                    if (this.f23908b.h(4) != this.f23920n) {
                        return false;
                    }
                    zVar.U(i8 + 2);
                }
                if (w(zVar, this.f23908b.f8152a, 4)) {
                    this.f23908b.p(14);
                    int h9 = this.f23908b.h(13);
                    if (h9 < 7) {
                        return false;
                    }
                    byte[] e8 = zVar.e();
                    int g8 = zVar.g();
                    int i10 = i8 + h9;
                    if (i10 >= g8) {
                        return true;
                    }
                    if (e8[i10] == -1) {
                        int i11 = i10 + 1;
                        if (i11 == g8) {
                            return true;
                        }
                        return l((byte) -1, e8[i11]) && ((e8[i11] & 8) >> 3) == h8;
                    } else if (e8[i10] != 73) {
                        return false;
                    } else {
                        int i12 = i10 + 1;
                        if (i12 == g8) {
                            return true;
                        }
                        if (e8[i12] != 68) {
                            return false;
                        }
                        int i13 = i10 + 2;
                        return i13 == g8 || e8[i13] == 51;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean i(z zVar, byte[] bArr, int i8) {
        int min = Math.min(zVar.a(), i8 - this.f23915i);
        zVar.l(bArr, this.f23915i, min);
        int i9 = this.f23915i + min;
        this.f23915i = i9;
        return i9 == i8;
    }

    private void j(z zVar) {
        int i8;
        byte[] e8 = zVar.e();
        int f5 = zVar.f();
        int g8 = zVar.g();
        while (f5 < g8) {
            int i9 = f5 + 1;
            int i10 = e8[f5] & 255;
            if (this.f23916j == 512 && l((byte) -1, (byte) i10) && (this.f23918l || h(zVar, i9 - 2))) {
                this.f23921o = (i10 & 8) >> 3;
                this.f23917k = (i10 & 1) == 0;
                if (this.f23918l) {
                    t();
                } else {
                    r();
                }
                zVar.U(i9);
                return;
            }
            int i11 = this.f23916j;
            int i12 = i10 | i11;
            if (i12 != 329) {
                if (i12 == 511) {
                    this.f23916j = RecognitionOptions.UPC_A;
                } else if (i12 == 836) {
                    i8 = RecognitionOptions.UPC_E;
                } else if (i12 == 1075) {
                    u();
                    zVar.U(i9);
                    return;
                } else if (i11 != 256) {
                    this.f23916j = RecognitionOptions.QR_CODE;
                    i9--;
                }
                f5 = i9;
            } else {
                i8 = 768;
            }
            this.f23916j = i8;
            f5 = i9;
        }
        zVar.U(f5);
    }

    private boolean l(byte b9, byte b10) {
        return m(((b9 & 255) << 8) | (b10 & 255));
    }

    public static boolean m(int i8) {
        return (i8 & 65526) == 65520;
    }

    private void n() {
        this.f23908b.p(0);
        if (this.f23922p) {
            this.f23908b.r(10);
        } else {
            int h8 = this.f23908b.h(2) + 1;
            if (h8 != 2) {
                b6.p.i("AdtsReader", "Detected audio object type: " + h8 + ", but assuming AAC LC.");
                h8 = 2;
            }
            this.f23908b.r(5);
            byte[] b9 = k4.a.b(h8, this.f23920n, this.f23908b.h(3));
            a.b f5 = k4.a.f(b9);
            w0 G = new w0.b().U(this.f23911e).g0("audio/mp4a-latm").K(f5.f20983c).J(f5.f20982b).h0(f5.f20981a).V(Collections.singletonList(b9)).X(this.f23910d).G();
            this.q = 1024000000 / G.G;
            this.f23912f.f(G);
            this.f23922p = true;
        }
        this.f23908b.r(4);
        int h9 = (this.f23908b.h(13) - 2) - 5;
        if (this.f23917k) {
            h9 -= 2;
        }
        v(this.f23912f, this.q, 0, h9);
    }

    private void o() {
        this.f23913g.b(this.f23909c, 10);
        this.f23909c.U(6);
        v(this.f23913g, 0L, 10, this.f23909c.G() + 10);
    }

    private void p(z zVar) {
        int min = Math.min(zVar.a(), this.f23923r - this.f23915i);
        this.f23925t.b(zVar, min);
        int i8 = this.f23915i + min;
        this.f23915i = i8;
        int i9 = this.f23923r;
        if (i8 == i9) {
            long j8 = this.f23924s;
            if (j8 != -9223372036854775807L) {
                this.f23925t.d(j8, 1, i9, 0, null);
                this.f23924s += this.f23926u;
            }
            s();
        }
    }

    private void q() {
        this.f23918l = false;
        s();
    }

    private void r() {
        this.f23914h = 1;
        this.f23915i = 0;
    }

    private void s() {
        this.f23914h = 0;
        this.f23915i = 0;
        this.f23916j = RecognitionOptions.QR_CODE;
    }

    private void t() {
        this.f23914h = 3;
        this.f23915i = 0;
    }

    private void u() {
        this.f23914h = 2;
        this.f23915i = f23906v.length;
        this.f23923r = 0;
        this.f23909c.U(0);
    }

    private void v(n4.b0 b0Var, long j8, int i8, int i9) {
        this.f23914h = 4;
        this.f23915i = i8;
        this.f23925t = b0Var;
        this.f23926u = j8;
        this.f23923r = i9;
    }

    private boolean w(z zVar, byte[] bArr, int i8) {
        if (zVar.a() < i8) {
            return false;
        }
        zVar.l(bArr, 0, i8);
        return true;
    }

    @Override // x4.m
    public void b(z zVar) {
        a();
        while (zVar.a() > 0) {
            int i8 = this.f23914h;
            if (i8 == 0) {
                j(zVar);
            } else if (i8 == 1) {
                g(zVar);
            } else if (i8 != 2) {
                if (i8 == 3) {
                    if (i(zVar, this.f23908b.f8152a, this.f23917k ? 7 : 5)) {
                        n();
                    }
                } else if (i8 != 4) {
                    throw new IllegalStateException();
                } else {
                    p(zVar);
                }
            } else if (i(zVar, this.f23909c.e(), 10)) {
                o();
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f23924s = -9223372036854775807L;
        q();
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23911e = dVar.b();
        n4.b0 e8 = mVar.e(dVar.c(), 1);
        this.f23912f = e8;
        this.f23925t = e8;
        if (!this.f23907a) {
            this.f23913g = new n4.j();
            return;
        }
        dVar.a();
        n4.b0 e9 = mVar.e(dVar.c(), 5);
        this.f23913g = e9;
        e9.f(new w0.b().U(dVar.b()).g0("application/id3").G());
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f23924s = j8;
        }
    }

    public long k() {
        return this.q;
    }
}
