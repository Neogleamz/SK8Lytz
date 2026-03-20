package w4;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import n4.l;
import n4.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f {

    /* renamed from: a  reason: collision with root package name */
    public int f23561a;

    /* renamed from: b  reason: collision with root package name */
    public int f23562b;

    /* renamed from: c  reason: collision with root package name */
    public long f23563c;

    /* renamed from: d  reason: collision with root package name */
    public long f23564d;

    /* renamed from: e  reason: collision with root package name */
    public long f23565e;

    /* renamed from: f  reason: collision with root package name */
    public long f23566f;

    /* renamed from: g  reason: collision with root package name */
    public int f23567g;

    /* renamed from: h  reason: collision with root package name */
    public int f23568h;

    /* renamed from: i  reason: collision with root package name */
    public int f23569i;

    /* renamed from: j  reason: collision with root package name */
    public final int[] f23570j = new int[255];

    /* renamed from: k  reason: collision with root package name */
    private final z f23571k = new z(255);

    public boolean a(l lVar, boolean z4) {
        b();
        this.f23571k.Q(27);
        if (n.b(lVar, this.f23571k.e(), 0, 27, z4) && this.f23571k.J() == 1332176723) {
            int H = this.f23571k.H();
            this.f23561a = H;
            if (H != 0) {
                if (z4) {
                    return false;
                }
                throw ParserException.d("unsupported bit stream revision");
            }
            this.f23562b = this.f23571k.H();
            this.f23563c = this.f23571k.v();
            this.f23564d = this.f23571k.x();
            this.f23565e = this.f23571k.x();
            this.f23566f = this.f23571k.x();
            int H2 = this.f23571k.H();
            this.f23567g = H2;
            this.f23568h = H2 + 27;
            this.f23571k.Q(H2);
            if (n.b(lVar, this.f23571k.e(), 0, this.f23567g, z4)) {
                for (int i8 = 0; i8 < this.f23567g; i8++) {
                    this.f23570j[i8] = this.f23571k.H();
                    this.f23569i += this.f23570j[i8];
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public void b() {
        this.f23561a = 0;
        this.f23562b = 0;
        this.f23563c = 0L;
        this.f23564d = 0L;
        this.f23565e = 0L;
        this.f23566f = 0L;
        this.f23567g = 0;
        this.f23568h = 0;
        this.f23569i = 0;
    }

    public boolean c(l lVar) {
        return d(lVar, -1L);
    }

    public boolean d(l lVar, long j8) {
        int i8;
        b6.a.a(lVar.getPosition() == lVar.e());
        this.f23571k.Q(4);
        while (true) {
            i8 = (j8 > (-1L) ? 1 : (j8 == (-1L) ? 0 : -1));
            if ((i8 == 0 || lVar.getPosition() + 4 < j8) && n.b(lVar, this.f23571k.e(), 0, 4, true)) {
                this.f23571k.U(0);
                if (this.f23571k.J() == 1332176723) {
                    lVar.h();
                    return true;
                }
                lVar.i(1);
            }
        }
        do {
            if (i8 != 0 && lVar.getPosition() >= j8) {
                break;
            }
        } while (lVar.a(1) != -1);
        return false;
    }
}
