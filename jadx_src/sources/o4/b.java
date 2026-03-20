package o4;

import b6.a;
import b6.l0;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import java.io.EOFException;
import java.util.Arrays;
import n4.b0;
import n4.d;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements k {

    /* renamed from: r  reason: collision with root package name */
    private static final int[] f22223r;

    /* renamed from: u  reason: collision with root package name */
    private static final int f22226u;

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22227a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22228b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f22229c;

    /* renamed from: d  reason: collision with root package name */
    private long f22230d;

    /* renamed from: e  reason: collision with root package name */
    private int f22231e;

    /* renamed from: f  reason: collision with root package name */
    private int f22232f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f22233g;

    /* renamed from: h  reason: collision with root package name */
    private long f22234h;

    /* renamed from: i  reason: collision with root package name */
    private int f22235i;

    /* renamed from: j  reason: collision with root package name */
    private int f22236j;

    /* renamed from: k  reason: collision with root package name */
    private long f22237k;

    /* renamed from: l  reason: collision with root package name */
    private m f22238l;

    /* renamed from: m  reason: collision with root package name */
    private b0 f22239m;

    /* renamed from: n  reason: collision with root package name */
    private z f22240n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f22241o;

    /* renamed from: p  reason: collision with root package name */
    public static final p f22222p = a.b;
    private static final int[] q = {13, 14, 16, 18, 20, 21, 27, 32, 6, 7, 6, 6, 1, 1, 1, 1};

    /* renamed from: s  reason: collision with root package name */
    private static final byte[] f22224s = l0.m0("#!AMR\n");

    /* renamed from: t  reason: collision with root package name */
    private static final byte[] f22225t = l0.m0("#!AMR-WB\n");

    static {
        int[] iArr = {18, 24, 33, 37, 41, 47, 51, 59, 61, 6, 1, 1, 1, 1, 1, 1};
        f22223r = iArr;
        f22226u = iArr[8];
    }

    public b() {
        this(0);
    }

    public b(int i8) {
        this.f22228b = (i8 & 2) != 0 ? i8 | 1 : i8;
        this.f22227a = new byte[1];
        this.f22235i = -1;
    }

    private void d() {
        a.h(this.f22239m);
        l0.j(this.f22238l);
    }

    private static int f(int i8, long j8) {
        return (int) (((i8 * 8) * 1000000) / j8);
    }

    private z h(long j8, boolean z4) {
        return new d(j8, this.f22234h, f(this.f22235i, 20000L), this.f22235i, z4);
    }

    private int i(int i8) {
        if (k(i8)) {
            return this.f22229c ? f22223r[i8] : q[i8];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Illegal AMR ");
        sb.append(this.f22229c ? "WB" : "NB");
        sb.append(" frame type ");
        sb.append(i8);
        throw ParserException.a(sb.toString(), null);
    }

    private boolean j(int i8) {
        return !this.f22229c && (i8 < 12 || i8 > 14);
    }

    private boolean k(int i8) {
        return i8 >= 0 && i8 <= 15 && (l(i8) || j(i8));
    }

    private boolean l(int i8) {
        return this.f22229c && (i8 < 10 || i8 > 13);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] m() {
        return new k[]{new b()};
    }

    private void n() {
        if (this.f22241o) {
            return;
        }
        this.f22241o = true;
        boolean z4 = this.f22229c;
        this.f22239m.f(new w0.b().g0(z4 ? "audio/amr-wb" : "audio/3gpp").Y(f22226u).J(1).h0(z4 ? 16000 : 8000).G());
    }

    private void o(long j8, int i8) {
        z bVar;
        int i9;
        if (this.f22233g) {
            return;
        }
        int i10 = this.f22228b;
        if ((i10 & 1) == 0 || j8 == -1 || !((i9 = this.f22235i) == -1 || i9 == this.f22231e)) {
            bVar = new z.b(-9223372036854775807L);
        } else if (this.f22236j < 20 && i8 != -1) {
            return;
        } else {
            bVar = h(j8, (i10 & 2) != 0);
        }
        this.f22240n = bVar;
        this.f22238l.m(bVar);
        this.f22233g = true;
    }

    private static boolean p(l lVar, byte[] bArr) {
        lVar.h();
        byte[] bArr2 = new byte[bArr.length];
        lVar.k(bArr2, 0, bArr.length);
        return Arrays.equals(bArr2, bArr);
    }

    private int q(l lVar) {
        lVar.h();
        lVar.k(this.f22227a, 0, 1);
        byte b9 = this.f22227a[0];
        if ((b9 & 131) <= 0) {
            return i((b9 >> 3) & 15);
        }
        throw ParserException.a("Invalid padding bits for frame header " + ((int) b9), null);
    }

    private boolean r(l lVar) {
        int length;
        byte[] bArr = f22224s;
        if (p(lVar, bArr)) {
            this.f22229c = false;
            length = bArr.length;
        } else {
            byte[] bArr2 = f22225t;
            if (!p(lVar, bArr2)) {
                return false;
            }
            this.f22229c = true;
            length = bArr2.length;
        }
        lVar.i(length);
        return true;
    }

    private int s(l lVar) {
        if (this.f22232f == 0) {
            try {
                int q8 = q(lVar);
                this.f22231e = q8;
                this.f22232f = q8;
                if (this.f22235i == -1) {
                    this.f22234h = lVar.getPosition();
                    this.f22235i = this.f22231e;
                }
                if (this.f22235i == this.f22231e) {
                    this.f22236j++;
                }
            } catch (EOFException unused) {
                return -1;
            }
        }
        int c9 = this.f22239m.c(lVar, this.f22232f, true);
        if (c9 == -1) {
            return -1;
        }
        int i8 = this.f22232f - c9;
        this.f22232f = i8;
        if (i8 > 0) {
            return 0;
        }
        this.f22239m.d(this.f22237k + this.f22230d, 1, this.f22231e, 0, null);
        this.f22230d += 20000;
        return 0;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f22238l = mVar;
        this.f22239m = mVar.e(0, 1);
        mVar.o();
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f22230d = 0L;
        this.f22231e = 0;
        this.f22232f = 0;
        if (j8 != 0) {
            z zVar = this.f22240n;
            if (zVar instanceof d) {
                this.f22237k = ((d) zVar).c(j8);
                return;
            }
        }
        this.f22237k = 0L;
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        d();
        if (lVar.getPosition() != 0 || r(lVar)) {
            n();
            int s8 = s(lVar);
            o(lVar.b(), s8);
            return s8;
        }
        throw ParserException.a("Could not find AMR header.", null);
    }

    @Override // n4.k
    public boolean g(l lVar) {
        return r(lVar);
    }

    @Override // n4.k
    public void release() {
    }
}
