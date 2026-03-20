package v4;

import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q {

    /* renamed from: a  reason: collision with root package name */
    public c f23315a;

    /* renamed from: b  reason: collision with root package name */
    public long f23316b;

    /* renamed from: c  reason: collision with root package name */
    public long f23317c;

    /* renamed from: d  reason: collision with root package name */
    public long f23318d;

    /* renamed from: e  reason: collision with root package name */
    public int f23319e;

    /* renamed from: f  reason: collision with root package name */
    public int f23320f;

    /* renamed from: l  reason: collision with root package name */
    public boolean f23326l;

    /* renamed from: n  reason: collision with root package name */
    public p f23328n;

    /* renamed from: p  reason: collision with root package name */
    public boolean f23330p;
    public long q;

    /* renamed from: r  reason: collision with root package name */
    public boolean f23331r;

    /* renamed from: g  reason: collision with root package name */
    public long[] f23321g = new long[0];

    /* renamed from: h  reason: collision with root package name */
    public int[] f23322h = new int[0];

    /* renamed from: i  reason: collision with root package name */
    public int[] f23323i = new int[0];

    /* renamed from: j  reason: collision with root package name */
    public long[] f23324j = new long[0];

    /* renamed from: k  reason: collision with root package name */
    public boolean[] f23325k = new boolean[0];

    /* renamed from: m  reason: collision with root package name */
    public boolean[] f23327m = new boolean[0];

    /* renamed from: o  reason: collision with root package name */
    public final z f23329o = new z();

    public void a(z zVar) {
        zVar.l(this.f23329o.e(), 0, this.f23329o.g());
        this.f23329o.U(0);
        this.f23330p = false;
    }

    public void b(n4.l lVar) {
        lVar.readFully(this.f23329o.e(), 0, this.f23329o.g());
        this.f23329o.U(0);
        this.f23330p = false;
    }

    public long c(int i8) {
        return this.f23324j[i8];
    }

    public void d(int i8) {
        this.f23329o.Q(i8);
        this.f23326l = true;
        this.f23330p = true;
    }

    public void e(int i8, int i9) {
        this.f23319e = i8;
        this.f23320f = i9;
        if (this.f23322h.length < i8) {
            this.f23321g = new long[i8];
            this.f23322h = new int[i8];
        }
        if (this.f23323i.length < i9) {
            int i10 = (i9 * 125) / 100;
            this.f23323i = new int[i10];
            this.f23324j = new long[i10];
            this.f23325k = new boolean[i10];
            this.f23327m = new boolean[i10];
        }
    }

    public void f() {
        this.f23319e = 0;
        this.q = 0L;
        this.f23331r = false;
        this.f23326l = false;
        this.f23330p = false;
        this.f23328n = null;
    }

    public boolean g(int i8) {
        return this.f23326l && this.f23327m[i8];
    }
}
