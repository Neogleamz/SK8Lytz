package x4;

import b6.a;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import k4.b;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements m {

    /* renamed from: a  reason: collision with root package name */
    private final b6.y f23817a;

    /* renamed from: b  reason: collision with root package name */
    private final z f23818b;

    /* renamed from: c  reason: collision with root package name */
    private final String f23819c;

    /* renamed from: d  reason: collision with root package name */
    private String f23820d;

    /* renamed from: e  reason: collision with root package name */
    private n4.b0 f23821e;

    /* renamed from: f  reason: collision with root package name */
    private int f23822f;

    /* renamed from: g  reason: collision with root package name */
    private int f23823g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f23824h;

    /* renamed from: i  reason: collision with root package name */
    private long f23825i;

    /* renamed from: j  reason: collision with root package name */
    private w0 f23826j;

    /* renamed from: k  reason: collision with root package name */
    private int f23827k;

    /* renamed from: l  reason: collision with root package name */
    private long f23828l;

    public c() {
        this(null);
    }

    public c(String str) {
        b6.y yVar = new b6.y(new byte[RecognitionOptions.ITF]);
        this.f23817a = yVar;
        this.f23818b = new z(yVar.f8152a);
        this.f23822f = 0;
        this.f23828l = -9223372036854775807L;
        this.f23819c = str;
    }

    private boolean a(z zVar, byte[] bArr, int i8) {
        int min = Math.min(zVar.a(), i8 - this.f23823g);
        zVar.l(bArr, this.f23823g, min);
        int i9 = this.f23823g + min;
        this.f23823g = i9;
        return i9 == i8;
    }

    private void g() {
        this.f23817a.p(0);
        b.C0184b f5 = k4.b.f(this.f23817a);
        w0 w0Var = this.f23826j;
        if (w0Var == null || f5.f20993d != w0Var.F || f5.f20992c != w0Var.G || !l0.c(f5.f20990a, w0Var.f11207m)) {
            w0.b b02 = new w0.b().U(this.f23820d).g0(f5.f20990a).J(f5.f20993d).h0(f5.f20992c).X(this.f23819c).b0(f5.f20996g);
            if ("audio/ac3".equals(f5.f20990a)) {
                b02.I(f5.f20996g);
            }
            w0 G = b02.G();
            this.f23826j = G;
            this.f23821e.f(G);
        }
        this.f23827k = f5.f20994e;
        this.f23825i = (f5.f20995f * 1000000) / this.f23826j.G;
    }

    private boolean h(z zVar) {
        while (true) {
            boolean z4 = false;
            if (zVar.a() <= 0) {
                return false;
            }
            if (this.f23824h) {
                int H = zVar.H();
                if (H == 119) {
                    this.f23824h = false;
                    return true;
                }
                if (H != 11) {
                    this.f23824h = z4;
                }
                z4 = true;
                this.f23824h = z4;
            } else {
                if (zVar.H() != 11) {
                    this.f23824h = z4;
                }
                z4 = true;
                this.f23824h = z4;
            }
        }
    }

    @Override // x4.m
    public void b(z zVar) {
        a.h(this.f23821e);
        while (zVar.a() > 0) {
            int i8 = this.f23822f;
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 == 2) {
                        int min = Math.min(zVar.a(), this.f23827k - this.f23823g);
                        this.f23821e.b(zVar, min);
                        int i9 = this.f23823g + min;
                        this.f23823g = i9;
                        int i10 = this.f23827k;
                        if (i9 == i10) {
                            long j8 = this.f23828l;
                            if (j8 != -9223372036854775807L) {
                                this.f23821e.d(j8, 1, i10, 0, null);
                                this.f23828l += this.f23825i;
                            }
                            this.f23822f = 0;
                        }
                    }
                } else if (a(zVar, this.f23818b.e(), RecognitionOptions.ITF)) {
                    g();
                    this.f23818b.U(0);
                    this.f23821e.b(this.f23818b, RecognitionOptions.ITF);
                    this.f23822f = 2;
                }
            } else if (h(zVar)) {
                this.f23822f = 1;
                this.f23818b.e()[0] = 11;
                this.f23818b.e()[1] = 119;
                this.f23823g = 2;
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f23822f = 0;
        this.f23823g = 0;
        this.f23824h = false;
        this.f23828l = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23820d = dVar.b();
        this.f23821e = mVar.e(dVar.c(), 1);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f23828l = j8;
        }
    }
}
