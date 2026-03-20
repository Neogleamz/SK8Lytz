package x4;

import b6.l0;
import b6.u;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Collections;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q implements m {

    /* renamed from: a  reason: collision with root package name */
    private final d0 f24055a;

    /* renamed from: b  reason: collision with root package name */
    private String f24056b;

    /* renamed from: c  reason: collision with root package name */
    private n4.b0 f24057c;

    /* renamed from: d  reason: collision with root package name */
    private a f24058d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f24059e;

    /* renamed from: l  reason: collision with root package name */
    private long f24066l;

    /* renamed from: f  reason: collision with root package name */
    private final boolean[] f24060f = new boolean[3];

    /* renamed from: g  reason: collision with root package name */
    private final u f24061g = new u(32, RecognitionOptions.ITF);

    /* renamed from: h  reason: collision with root package name */
    private final u f24062h = new u(33, RecognitionOptions.ITF);

    /* renamed from: i  reason: collision with root package name */
    private final u f24063i = new u(34, RecognitionOptions.ITF);

    /* renamed from: j  reason: collision with root package name */
    private final u f24064j = new u(39, RecognitionOptions.ITF);

    /* renamed from: k  reason: collision with root package name */
    private final u f24065k = new u(40, RecognitionOptions.ITF);

    /* renamed from: m  reason: collision with root package name */
    private long f24067m = -9223372036854775807L;

    /* renamed from: n  reason: collision with root package name */
    private final z f24068n = new z();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final n4.b0 f24069a;

        /* renamed from: b  reason: collision with root package name */
        private long f24070b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f24071c;

        /* renamed from: d  reason: collision with root package name */
        private int f24072d;

        /* renamed from: e  reason: collision with root package name */
        private long f24073e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f24074f;

        /* renamed from: g  reason: collision with root package name */
        private boolean f24075g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f24076h;

        /* renamed from: i  reason: collision with root package name */
        private boolean f24077i;

        /* renamed from: j  reason: collision with root package name */
        private boolean f24078j;

        /* renamed from: k  reason: collision with root package name */
        private long f24079k;

        /* renamed from: l  reason: collision with root package name */
        private long f24080l;

        /* renamed from: m  reason: collision with root package name */
        private boolean f24081m;

        public a(n4.b0 b0Var) {
            this.f24069a = b0Var;
        }

        private static boolean b(int i8) {
            return (32 <= i8 && i8 <= 35) || i8 == 39;
        }

        private static boolean c(int i8) {
            return i8 < 32 || i8 == 40;
        }

        private void d(int i8) {
            long j8 = this.f24080l;
            if (j8 == -9223372036854775807L) {
                return;
            }
            boolean z4 = this.f24081m;
            this.f24069a.d(j8, z4 ? 1 : 0, (int) (this.f24070b - this.f24079k), i8, null);
        }

        public void a(long j8, int i8, boolean z4) {
            if (this.f24078j && this.f24075g) {
                this.f24081m = this.f24071c;
                this.f24078j = false;
            } else if (this.f24076h || this.f24075g) {
                if (z4 && this.f24077i) {
                    d(i8 + ((int) (j8 - this.f24070b)));
                }
                this.f24079k = this.f24070b;
                this.f24080l = this.f24073e;
                this.f24081m = this.f24071c;
                this.f24077i = true;
            }
        }

        public void e(byte[] bArr, int i8, int i9) {
            if (this.f24074f) {
                int i10 = this.f24072d;
                int i11 = (i8 + 2) - i10;
                if (i11 >= i9) {
                    this.f24072d = i10 + (i9 - i8);
                    return;
                }
                this.f24075g = (bArr[i11] & 128) != 0;
                this.f24074f = false;
            }
        }

        public void f() {
            this.f24074f = false;
            this.f24075g = false;
            this.f24076h = false;
            this.f24077i = false;
            this.f24078j = false;
        }

        public void g(long j8, int i8, int i9, long j9, boolean z4) {
            boolean z8 = false;
            this.f24075g = false;
            this.f24076h = false;
            this.f24073e = j9;
            this.f24072d = 0;
            this.f24070b = j8;
            if (!c(i9)) {
                if (this.f24077i && !this.f24078j) {
                    if (z4) {
                        d(i8);
                    }
                    this.f24077i = false;
                }
                if (b(i9)) {
                    this.f24076h = !this.f24078j;
                    this.f24078j = true;
                }
            }
            boolean z9 = i9 >= 16 && i9 <= 21;
            this.f24071c = z9;
            if (z9 || i9 <= 9) {
                z8 = true;
            }
            this.f24074f = z8;
        }
    }

    public q(d0 d0Var) {
        this.f24055a = d0Var;
    }

    private void a() {
        b6.a.h(this.f24057c);
        l0.j(this.f24058d);
    }

    private void g(long j8, int i8, int i9, long j9) {
        this.f24058d.a(j8, i8, this.f24059e);
        if (!this.f24059e) {
            this.f24061g.b(i9);
            this.f24062h.b(i9);
            this.f24063i.b(i9);
            if (this.f24061g.c() && this.f24062h.c() && this.f24063i.c()) {
                this.f24057c.f(i(this.f24056b, this.f24061g, this.f24062h, this.f24063i));
                this.f24059e = true;
            }
        }
        if (this.f24064j.b(i9)) {
            u uVar = this.f24064j;
            this.f24068n.S(this.f24064j.f24123d, b6.u.q(uVar.f24123d, uVar.f24124e));
            this.f24068n.V(5);
            this.f24055a.a(j9, this.f24068n);
        }
        if (this.f24065k.b(i9)) {
            u uVar2 = this.f24065k;
            this.f24068n.S(this.f24065k.f24123d, b6.u.q(uVar2.f24123d, uVar2.f24124e));
            this.f24068n.V(5);
            this.f24055a.a(j9, this.f24068n);
        }
    }

    private void h(byte[] bArr, int i8, int i9) {
        this.f24058d.e(bArr, i8, i9);
        if (!this.f24059e) {
            this.f24061g.a(bArr, i8, i9);
            this.f24062h.a(bArr, i8, i9);
            this.f24063i.a(bArr, i8, i9);
        }
        this.f24064j.a(bArr, i8, i9);
        this.f24065k.a(bArr, i8, i9);
    }

    private static w0 i(String str, u uVar, u uVar2, u uVar3) {
        int i8 = uVar.f24124e;
        byte[] bArr = new byte[uVar2.f24124e + i8 + uVar3.f24124e];
        System.arraycopy(uVar.f24123d, 0, bArr, 0, i8);
        System.arraycopy(uVar2.f24123d, 0, bArr, uVar.f24124e, uVar2.f24124e);
        System.arraycopy(uVar3.f24123d, 0, bArr, uVar.f24124e + uVar2.f24124e, uVar3.f24124e);
        u.a h8 = b6.u.h(uVar2.f24123d, 3, uVar2.f24124e);
        return new w0.b().U(str).g0("video/hevc").K(b6.e.c(h8.f8113a, h8.f8114b, h8.f8115c, h8.f8116d, h8.f8117e, h8.f8118f)).n0(h8.f8120h).S(h8.f8121i).c0(h8.f8122j).V(Collections.singletonList(bArr)).G();
    }

    private void j(long j8, int i8, int i9, long j9) {
        this.f24058d.g(j8, i8, i9, j9, this.f24059e);
        if (!this.f24059e) {
            this.f24061g.e(i9);
            this.f24062h.e(i9);
            this.f24063i.e(i9);
        }
        this.f24064j.e(i9);
        this.f24065k.e(i9);
    }

    @Override // x4.m
    public void b(z zVar) {
        a();
        while (zVar.a() > 0) {
            int f5 = zVar.f();
            int g8 = zVar.g();
            byte[] e8 = zVar.e();
            this.f24066l += zVar.a();
            this.f24057c.b(zVar, zVar.a());
            while (f5 < g8) {
                int c9 = b6.u.c(e8, f5, g8, this.f24060f);
                if (c9 == g8) {
                    h(e8, f5, g8);
                    return;
                }
                int e9 = b6.u.e(e8, c9);
                int i8 = c9 - f5;
                if (i8 > 0) {
                    h(e8, f5, c9);
                }
                int i9 = g8 - c9;
                long j8 = this.f24066l - i9;
                g(j8, i9, i8 < 0 ? -i8 : 0, this.f24067m);
                j(j8, i9, e9, this.f24067m);
                f5 = c9 + 3;
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f24066l = 0L;
        this.f24067m = -9223372036854775807L;
        b6.u.a(this.f24060f);
        this.f24061g.d();
        this.f24062h.d();
        this.f24063i.d();
        this.f24064j.d();
        this.f24065k.d();
        a aVar = this.f24058d;
        if (aVar != null) {
            aVar.f();
        }
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f24056b = dVar.b();
        n4.b0 e8 = mVar.e(dVar.c(), 2);
        this.f24057c = e8;
        this.f24058d = new a(e8);
        this.f24055a.b(mVar, dVar);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f24067m = j8;
        }
    }
}
