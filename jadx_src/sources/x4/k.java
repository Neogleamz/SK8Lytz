package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements m {

    /* renamed from: b  reason: collision with root package name */
    private final String f23942b;

    /* renamed from: c  reason: collision with root package name */
    private String f23943c;

    /* renamed from: d  reason: collision with root package name */
    private n4.b0 f23944d;

    /* renamed from: f  reason: collision with root package name */
    private int f23946f;

    /* renamed from: g  reason: collision with root package name */
    private int f23947g;

    /* renamed from: h  reason: collision with root package name */
    private long f23948h;

    /* renamed from: i  reason: collision with root package name */
    private w0 f23949i;

    /* renamed from: j  reason: collision with root package name */
    private int f23950j;

    /* renamed from: a  reason: collision with root package name */
    private final z f23941a = new z(new byte[18]);

    /* renamed from: e  reason: collision with root package name */
    private int f23945e = 0;

    /* renamed from: k  reason: collision with root package name */
    private long f23951k = -9223372036854775807L;

    public k(String str) {
        this.f23942b = str;
    }

    private boolean a(z zVar, byte[] bArr, int i8) {
        int min = Math.min(zVar.a(), i8 - this.f23946f);
        zVar.l(bArr, this.f23946f, min);
        int i9 = this.f23946f + min;
        this.f23946f = i9;
        return i9 == i8;
    }

    private void g() {
        byte[] e8 = this.f23941a.e();
        if (this.f23949i == null) {
            w0 g8 = k4.t.g(e8, this.f23943c, this.f23942b, null);
            this.f23949i = g8;
            this.f23944d.f(g8);
        }
        this.f23950j = k4.t.a(e8);
        this.f23948h = (int) ((k4.t.f(e8) * 1000000) / this.f23949i.G);
    }

    private boolean h(z zVar) {
        while (zVar.a() > 0) {
            int i8 = this.f23947g << 8;
            this.f23947g = i8;
            int H = i8 | zVar.H();
            this.f23947g = H;
            if (k4.t.d(H)) {
                byte[] e8 = this.f23941a.e();
                int i9 = this.f23947g;
                e8[0] = (byte) ((i9 >> 24) & 255);
                e8[1] = (byte) ((i9 >> 16) & 255);
                e8[2] = (byte) ((i9 >> 8) & 255);
                e8[3] = (byte) (i9 & 255);
                this.f23946f = 4;
                this.f23947g = 0;
                return true;
            }
        }
        return false;
    }

    @Override // x4.m
    public void b(z zVar) {
        a.h(this.f23944d);
        while (zVar.a() > 0) {
            int i8 = this.f23945e;
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        throw new IllegalStateException();
                    }
                    int min = Math.min(zVar.a(), this.f23950j - this.f23946f);
                    this.f23944d.b(zVar, min);
                    int i9 = this.f23946f + min;
                    this.f23946f = i9;
                    int i10 = this.f23950j;
                    if (i9 == i10) {
                        long j8 = this.f23951k;
                        if (j8 != -9223372036854775807L) {
                            this.f23944d.d(j8, 1, i10, 0, null);
                            this.f23951k += this.f23948h;
                        }
                        this.f23945e = 0;
                    }
                } else if (a(zVar, this.f23941a.e(), 18)) {
                    g();
                    this.f23941a.U(0);
                    this.f23944d.b(this.f23941a, 18);
                    this.f23945e = 2;
                }
            } else if (h(zVar)) {
                this.f23945e = 1;
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f23945e = 0;
        this.f23946f = 0;
        this.f23947g = 0;
        this.f23951k = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23943c = dVar.b();
        this.f23944d = mVar.e(dVar.c(), 1);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f23951k = j8;
        }
    }
}
