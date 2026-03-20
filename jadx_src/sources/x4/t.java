package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import k4.u;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t implements m {

    /* renamed from: a  reason: collision with root package name */
    private final z f24108a;

    /* renamed from: b  reason: collision with root package name */
    private final u.a f24109b;

    /* renamed from: c  reason: collision with root package name */
    private final String f24110c;

    /* renamed from: d  reason: collision with root package name */
    private n4.b0 f24111d;

    /* renamed from: e  reason: collision with root package name */
    private String f24112e;

    /* renamed from: f  reason: collision with root package name */
    private int f24113f;

    /* renamed from: g  reason: collision with root package name */
    private int f24114g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f24115h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f24116i;

    /* renamed from: j  reason: collision with root package name */
    private long f24117j;

    /* renamed from: k  reason: collision with root package name */
    private int f24118k;

    /* renamed from: l  reason: collision with root package name */
    private long f24119l;

    public t() {
        this(null);
    }

    public t(String str) {
        this.f24113f = 0;
        z zVar = new z(4);
        this.f24108a = zVar;
        zVar.e()[0] = -1;
        this.f24109b = new u.a();
        this.f24119l = -9223372036854775807L;
        this.f24110c = str;
    }

    private void a(z zVar) {
        byte[] e8 = zVar.e();
        int g8 = zVar.g();
        for (int f5 = zVar.f(); f5 < g8; f5++) {
            boolean z4 = (e8[f5] & 255) == 255;
            boolean z8 = this.f24116i && (e8[f5] & 224) == 224;
            this.f24116i = z4;
            if (z8) {
                zVar.U(f5 + 1);
                this.f24116i = false;
                this.f24108a.e()[1] = e8[f5];
                this.f24114g = 2;
                this.f24113f = 1;
                return;
            }
        }
        zVar.U(g8);
    }

    private void g(z zVar) {
        int min = Math.min(zVar.a(), this.f24118k - this.f24114g);
        this.f24111d.b(zVar, min);
        int i8 = this.f24114g + min;
        this.f24114g = i8;
        int i9 = this.f24118k;
        if (i8 < i9) {
            return;
        }
        long j8 = this.f24119l;
        if (j8 != -9223372036854775807L) {
            this.f24111d.d(j8, 1, i9, 0, null);
            this.f24119l += this.f24117j;
        }
        this.f24114g = 0;
        this.f24113f = 0;
    }

    private void h(z zVar) {
        int min = Math.min(zVar.a(), 4 - this.f24114g);
        zVar.l(this.f24108a.e(), this.f24114g, min);
        int i8 = this.f24114g + min;
        this.f24114g = i8;
        if (i8 < 4) {
            return;
        }
        this.f24108a.U(0);
        if (!this.f24109b.a(this.f24108a.q())) {
            this.f24114g = 0;
            this.f24113f = 1;
            return;
        }
        u.a aVar = this.f24109b;
        this.f24118k = aVar.f21023c;
        if (!this.f24115h) {
            this.f24117j = (aVar.f21027g * 1000000) / aVar.f21024d;
            this.f24111d.f(new w0.b().U(this.f24112e).g0(this.f24109b.f21022b).Y(RecognitionOptions.AZTEC).J(this.f24109b.f21025e).h0(this.f24109b.f21024d).X(this.f24110c).G());
            this.f24115h = true;
        }
        this.f24108a.U(0);
        this.f24111d.b(this.f24108a, 4);
        this.f24113f = 2;
    }

    @Override // x4.m
    public void b(z zVar) {
        a.h(this.f24111d);
        while (zVar.a() > 0) {
            int i8 = this.f24113f;
            if (i8 == 0) {
                a(zVar);
            } else if (i8 == 1) {
                h(zVar);
            } else if (i8 != 2) {
                throw new IllegalStateException();
            } else {
                g(zVar);
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f24113f = 0;
        this.f24114g = 0;
        this.f24116i = false;
        this.f24119l = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f24112e = dVar.b();
        this.f24111d = mVar.e(dVar.c(), 1);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f24119l = j8;
        }
    }
}
