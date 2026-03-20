package w5;

import android.text.Layout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g {

    /* renamed from: a  reason: collision with root package name */
    private String f23646a;

    /* renamed from: b  reason: collision with root package name */
    private int f23647b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f23648c;

    /* renamed from: d  reason: collision with root package name */
    private int f23649d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23650e;

    /* renamed from: k  reason: collision with root package name */
    private float f23656k;

    /* renamed from: l  reason: collision with root package name */
    private String f23657l;

    /* renamed from: o  reason: collision with root package name */
    private Layout.Alignment f23660o;

    /* renamed from: p  reason: collision with root package name */
    private Layout.Alignment f23661p;

    /* renamed from: r  reason: collision with root package name */
    private b f23662r;

    /* renamed from: f  reason: collision with root package name */
    private int f23651f = -1;

    /* renamed from: g  reason: collision with root package name */
    private int f23652g = -1;

    /* renamed from: h  reason: collision with root package name */
    private int f23653h = -1;

    /* renamed from: i  reason: collision with root package name */
    private int f23654i = -1;

    /* renamed from: j  reason: collision with root package name */
    private int f23655j = -1;

    /* renamed from: m  reason: collision with root package name */
    private int f23658m = -1;

    /* renamed from: n  reason: collision with root package name */
    private int f23659n = -1;
    private int q = -1;

    /* renamed from: s  reason: collision with root package name */
    private float f23663s = Float.MAX_VALUE;

    private g r(g gVar, boolean z4) {
        int i8;
        Layout.Alignment alignment;
        Layout.Alignment alignment2;
        String str;
        if (gVar != null) {
            if (!this.f23648c && gVar.f23648c) {
                w(gVar.f23647b);
            }
            if (this.f23653h == -1) {
                this.f23653h = gVar.f23653h;
            }
            if (this.f23654i == -1) {
                this.f23654i = gVar.f23654i;
            }
            if (this.f23646a == null && (str = gVar.f23646a) != null) {
                this.f23646a = str;
            }
            if (this.f23651f == -1) {
                this.f23651f = gVar.f23651f;
            }
            if (this.f23652g == -1) {
                this.f23652g = gVar.f23652g;
            }
            if (this.f23659n == -1) {
                this.f23659n = gVar.f23659n;
            }
            if (this.f23660o == null && (alignment2 = gVar.f23660o) != null) {
                this.f23660o = alignment2;
            }
            if (this.f23661p == null && (alignment = gVar.f23661p) != null) {
                this.f23661p = alignment;
            }
            if (this.q == -1) {
                this.q = gVar.q;
            }
            if (this.f23655j == -1) {
                this.f23655j = gVar.f23655j;
                this.f23656k = gVar.f23656k;
            }
            if (this.f23662r == null) {
                this.f23662r = gVar.f23662r;
            }
            if (this.f23663s == Float.MAX_VALUE) {
                this.f23663s = gVar.f23663s;
            }
            if (z4 && !this.f23650e && gVar.f23650e) {
                u(gVar.f23649d);
            }
            if (z4 && this.f23658m == -1 && (i8 = gVar.f23658m) != -1) {
                this.f23658m = i8;
            }
        }
        return this;
    }

    public g A(String str) {
        this.f23657l = str;
        return this;
    }

    public g B(boolean z4) {
        this.f23654i = z4 ? 1 : 0;
        return this;
    }

    public g C(boolean z4) {
        this.f23651f = z4 ? 1 : 0;
        return this;
    }

    public g D(Layout.Alignment alignment) {
        this.f23661p = alignment;
        return this;
    }

    public g E(int i8) {
        this.f23659n = i8;
        return this;
    }

    public g F(int i8) {
        this.f23658m = i8;
        return this;
    }

    public g G(float f5) {
        this.f23663s = f5;
        return this;
    }

    public g H(Layout.Alignment alignment) {
        this.f23660o = alignment;
        return this;
    }

    public g I(boolean z4) {
        this.q = z4 ? 1 : 0;
        return this;
    }

    public g J(b bVar) {
        this.f23662r = bVar;
        return this;
    }

    public g K(boolean z4) {
        this.f23652g = z4 ? 1 : 0;
        return this;
    }

    public g a(g gVar) {
        return r(gVar, true);
    }

    public int b() {
        if (this.f23650e) {
            return this.f23649d;
        }
        throw new IllegalStateException("Background color has not been defined.");
    }

    public int c() {
        if (this.f23648c) {
            return this.f23647b;
        }
        throw new IllegalStateException("Font color has not been defined.");
    }

    public String d() {
        return this.f23646a;
    }

    public float e() {
        return this.f23656k;
    }

    public int f() {
        return this.f23655j;
    }

    public String g() {
        return this.f23657l;
    }

    public Layout.Alignment h() {
        return this.f23661p;
    }

    public int i() {
        return this.f23659n;
    }

    public int j() {
        return this.f23658m;
    }

    public float k() {
        return this.f23663s;
    }

    public int l() {
        int i8 = this.f23653h;
        if (i8 == -1 && this.f23654i == -1) {
            return -1;
        }
        return (i8 == 1 ? 1 : 0) | (this.f23654i == 1 ? 2 : 0);
    }

    public Layout.Alignment m() {
        return this.f23660o;
    }

    public boolean n() {
        return this.q == 1;
    }

    public b o() {
        return this.f23662r;
    }

    public boolean p() {
        return this.f23650e;
    }

    public boolean q() {
        return this.f23648c;
    }

    public boolean s() {
        return this.f23651f == 1;
    }

    public boolean t() {
        return this.f23652g == 1;
    }

    public g u(int i8) {
        this.f23649d = i8;
        this.f23650e = true;
        return this;
    }

    public g v(boolean z4) {
        this.f23653h = z4 ? 1 : 0;
        return this;
    }

    public g w(int i8) {
        this.f23647b = i8;
        this.f23648c = true;
        return this;
    }

    public g x(String str) {
        this.f23646a = str;
        return this;
    }

    public g y(float f5) {
        this.f23656k = f5;
        return this;
    }

    public g z(int i8) {
        this.f23655j = i8;
        return this;
    }
}
