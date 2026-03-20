package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import k4.c;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f implements m {

    /* renamed from: a  reason: collision with root package name */
    private final b6.y f23845a;

    /* renamed from: b  reason: collision with root package name */
    private final z f23846b;

    /* renamed from: c  reason: collision with root package name */
    private final String f23847c;

    /* renamed from: d  reason: collision with root package name */
    private String f23848d;

    /* renamed from: e  reason: collision with root package name */
    private n4.b0 f23849e;

    /* renamed from: f  reason: collision with root package name */
    private int f23850f;

    /* renamed from: g  reason: collision with root package name */
    private int f23851g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f23852h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f23853i;

    /* renamed from: j  reason: collision with root package name */
    private long f23854j;

    /* renamed from: k  reason: collision with root package name */
    private w0 f23855k;

    /* renamed from: l  reason: collision with root package name */
    private int f23856l;

    /* renamed from: m  reason: collision with root package name */
    private long f23857m;

    public f() {
        this(null);
    }

    public f(String str) {
        b6.y yVar = new b6.y(new byte[16]);
        this.f23845a = yVar;
        this.f23846b = new z(yVar.f8152a);
        this.f23850f = 0;
        this.f23851g = 0;
        this.f23852h = false;
        this.f23853i = false;
        this.f23857m = -9223372036854775807L;
        this.f23847c = str;
    }

    private boolean a(z zVar, byte[] bArr, int i8) {
        int min = Math.min(zVar.a(), i8 - this.f23851g);
        zVar.l(bArr, this.f23851g, min);
        int i9 = this.f23851g + min;
        this.f23851g = i9;
        return i9 == i8;
    }

    private void g() {
        this.f23845a.p(0);
        c.b d8 = k4.c.d(this.f23845a);
        w0 w0Var = this.f23855k;
        if (w0Var == null || d8.f21000c != w0Var.F || d8.f20999b != w0Var.G || !"audio/ac4".equals(w0Var.f11207m)) {
            w0 G = new w0.b().U(this.f23848d).g0("audio/ac4").J(d8.f21000c).h0(d8.f20999b).X(this.f23847c).G();
            this.f23855k = G;
            this.f23849e.f(G);
        }
        this.f23856l = d8.f21001d;
        this.f23854j = (d8.f21002e * 1000000) / this.f23855k.G;
    }

    private boolean h(z zVar) {
        int H;
        while (true) {
            if (zVar.a() <= 0) {
                return false;
            }
            if (this.f23852h) {
                H = zVar.H();
                this.f23852h = H == 172;
                if (H == 64 || H == 65) {
                    break;
                }
            } else {
                this.f23852h = zVar.H() == 172;
            }
        }
        this.f23853i = H == 65;
        return true;
    }

    @Override // x4.m
    public void b(z zVar) {
        a.h(this.f23849e);
        while (zVar.a() > 0) {
            int i8 = this.f23850f;
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 == 2) {
                        int min = Math.min(zVar.a(), this.f23856l - this.f23851g);
                        this.f23849e.b(zVar, min);
                        int i9 = this.f23851g + min;
                        this.f23851g = i9;
                        int i10 = this.f23856l;
                        if (i9 == i10) {
                            long j8 = this.f23857m;
                            if (j8 != -9223372036854775807L) {
                                this.f23849e.d(j8, 1, i10, 0, null);
                                this.f23857m += this.f23854j;
                            }
                            this.f23850f = 0;
                        }
                    }
                } else if (a(zVar, this.f23846b.e(), 16)) {
                    g();
                    this.f23846b.U(0);
                    this.f23849e.b(this.f23846b, 16);
                    this.f23850f = 2;
                }
            } else if (h(zVar)) {
                this.f23850f = 1;
                this.f23846b.e()[0] = -84;
                this.f23846b.e()[1] = (byte) (this.f23853i ? 65 : 64);
                this.f23851g = 2;
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f23850f = 0;
        this.f23851g = 0;
        this.f23852h = false;
        this.f23853i = false;
        this.f23857m = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23848d = dVar.b();
        this.f23849e = mVar.e(dVar.c(), 1);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f23857m = j8;
        }
    }
}
