package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.EOFException;
import n4.d;
import n4.z;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements n4.k {

    /* renamed from: m  reason: collision with root package name */
    public static final n4.p f23867m = g.b;

    /* renamed from: a  reason: collision with root package name */
    private final int f23868a;

    /* renamed from: b  reason: collision with root package name */
    private final i f23869b;

    /* renamed from: c  reason: collision with root package name */
    private final z f23870c;

    /* renamed from: d  reason: collision with root package name */
    private final z f23871d;

    /* renamed from: e  reason: collision with root package name */
    private final b6.y f23872e;

    /* renamed from: f  reason: collision with root package name */
    private n4.m f23873f;

    /* renamed from: g  reason: collision with root package name */
    private long f23874g;

    /* renamed from: h  reason: collision with root package name */
    private long f23875h;

    /* renamed from: i  reason: collision with root package name */
    private int f23876i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f23877j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f23878k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f23879l;

    public h() {
        this(0);
    }

    public h(int i8) {
        this.f23868a = (i8 & 2) != 0 ? i8 | 1 : i8;
        this.f23869b = new i(true);
        this.f23870c = new z((int) RecognitionOptions.PDF417);
        this.f23876i = -1;
        this.f23875h = -1L;
        z zVar = new z(10);
        this.f23871d = zVar;
        this.f23872e = new b6.y(zVar.e());
    }

    private void d(n4.l lVar) {
        int h8;
        if (this.f23877j) {
            return;
        }
        this.f23876i = -1;
        lVar.h();
        long j8 = 0;
        if (lVar.getPosition() == 0) {
            k(lVar);
        }
        int i8 = 0;
        int i9 = 0;
        do {
            try {
                if (!lVar.d(this.f23871d.e(), 0, 2, true)) {
                    break;
                }
                this.f23871d.U(0);
                if (!i.m(this.f23871d.N())) {
                    break;
                } else if (!lVar.d(this.f23871d.e(), 0, 4, true)) {
                    break;
                } else {
                    this.f23872e.p(14);
                    h8 = this.f23872e.h(13);
                    if (h8 <= 6) {
                        this.f23877j = true;
                        throw ParserException.a("Malformed ADTS stream", null);
                    }
                    j8 += h8;
                    i9++;
                    if (i9 == 1000) {
                        break;
                    }
                }
            } catch (EOFException unused) {
            }
        } while (lVar.j(h8 - 6, true));
        i8 = i9;
        lVar.h();
        if (i8 > 0) {
            this.f23876i = (int) (j8 / i8);
        } else {
            this.f23876i = -1;
        }
        this.f23877j = true;
    }

    private static int f(int i8, long j8) {
        return (int) (((i8 * 8) * 1000000) / j8);
    }

    private n4.z h(long j8, boolean z4) {
        return new d(j8, this.f23875h, f(this.f23876i, this.f23869b.k()), this.f23876i, z4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] i() {
        return new n4.k[]{new h()};
    }

    private void j(long j8, boolean z4) {
        if (this.f23879l) {
            return;
        }
        boolean z8 = (this.f23868a & 1) != 0 && this.f23876i > 0;
        if (z8 && this.f23869b.k() == -9223372036854775807L && !z4) {
            return;
        }
        if (!z8 || this.f23869b.k() == -9223372036854775807L) {
            this.f23873f.m(new z.b(-9223372036854775807L));
        } else {
            this.f23873f.m(h(j8, (this.f23868a & 2) != 0));
        }
        this.f23879l = true;
    }

    private int k(n4.l lVar) {
        int i8 = 0;
        while (true) {
            lVar.k(this.f23871d.e(), 0, 10);
            this.f23871d.U(0);
            if (this.f23871d.K() != 4801587) {
                break;
            }
            this.f23871d.V(3);
            int G = this.f23871d.G();
            i8 += G + 10;
            lVar.f(G);
        }
        lVar.h();
        lVar.f(i8);
        if (this.f23875h == -1) {
            this.f23875h = i8;
        }
        return i8;
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23873f = mVar;
        this.f23869b.d(mVar, new i0.d(0, 1));
        mVar.o();
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f23878k = false;
        this.f23869b.c();
        this.f23874g = j9;
    }

    @Override // n4.k
    public int e(n4.l lVar, n4.y yVar) {
        a.h(this.f23873f);
        long b9 = lVar.b();
        int i8 = this.f23868a;
        if (((i8 & 2) == 0 && ((i8 & 1) == 0 || b9 == -1)) ? false : true) {
            d(lVar);
        }
        int read = lVar.read(this.f23870c.e(), 0, RecognitionOptions.PDF417);
        boolean z4 = read == -1;
        j(b9, z4);
        if (z4) {
            return -1;
        }
        this.f23870c.U(0);
        this.f23870c.T(read);
        if (!this.f23878k) {
            this.f23869b.f(this.f23874g, 4);
            this.f23878k = true;
        }
        this.f23869b.b(this.f23870c);
        return 0;
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        int k8 = k(lVar);
        int i8 = k8;
        int i9 = 0;
        int i10 = 0;
        do {
            lVar.k(this.f23871d.e(), 0, 2);
            this.f23871d.U(0);
            if (i.m(this.f23871d.N())) {
                i9++;
                if (i9 >= 4 && i10 > 188) {
                    return true;
                }
                lVar.k(this.f23871d.e(), 0, 4);
                this.f23872e.p(14);
                int h8 = this.f23872e.h(13);
                if (h8 > 6) {
                    lVar.f(h8 - 6);
                    i10 += h8;
                }
            }
            i8++;
            lVar.h();
            lVar.f(i8);
            i9 = 0;
            i10 = 0;
        } while (i8 - k8 < 8192);
        return false;
    }

    @Override // n4.k
    public void release() {
    }
}
