package androidx.navigation;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* renamed from: a  reason: collision with root package name */
    private boolean f6411a;

    /* renamed from: b  reason: collision with root package name */
    private int f6412b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f6413c;

    /* renamed from: d  reason: collision with root package name */
    private int f6414d;

    /* renamed from: e  reason: collision with root package name */
    private int f6415e;

    /* renamed from: f  reason: collision with root package name */
    private int f6416f;

    /* renamed from: g  reason: collision with root package name */
    private int f6417g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        boolean f6418a;

        /* renamed from: c  reason: collision with root package name */
        boolean f6420c;

        /* renamed from: b  reason: collision with root package name */
        int f6419b = -1;

        /* renamed from: d  reason: collision with root package name */
        int f6421d = -1;

        /* renamed from: e  reason: collision with root package name */
        int f6422e = -1;

        /* renamed from: f  reason: collision with root package name */
        int f6423f = -1;

        /* renamed from: g  reason: collision with root package name */
        int f6424g = -1;

        public n a() {
            return new n(this.f6418a, this.f6419b, this.f6420c, this.f6421d, this.f6422e, this.f6423f, this.f6424g);
        }

        public a b(int i8) {
            this.f6421d = i8;
            return this;
        }

        public a c(int i8) {
            this.f6422e = i8;
            return this;
        }

        public a d(boolean z4) {
            this.f6418a = z4;
            return this;
        }

        public a e(int i8) {
            this.f6423f = i8;
            return this;
        }

        public a f(int i8) {
            this.f6424g = i8;
            return this;
        }

        public a g(int i8, boolean z4) {
            this.f6419b = i8;
            this.f6420c = z4;
            return this;
        }
    }

    n(boolean z4, int i8, boolean z8, int i9, int i10, int i11, int i12) {
        this.f6411a = z4;
        this.f6412b = i8;
        this.f6413c = z8;
        this.f6414d = i9;
        this.f6415e = i10;
        this.f6416f = i11;
        this.f6417g = i12;
    }

    public int a() {
        return this.f6414d;
    }

    public int b() {
        return this.f6415e;
    }

    public int c() {
        return this.f6416f;
    }

    public int d() {
        return this.f6417g;
    }

    public int e() {
        return this.f6412b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || n.class != obj.getClass()) {
            return false;
        }
        n nVar = (n) obj;
        return this.f6411a == nVar.f6411a && this.f6412b == nVar.f6412b && this.f6413c == nVar.f6413c && this.f6414d == nVar.f6414d && this.f6415e == nVar.f6415e && this.f6416f == nVar.f6416f && this.f6417g == nVar.f6417g;
    }

    public boolean f() {
        return this.f6413c;
    }

    public boolean g() {
        return this.f6411a;
    }

    public int hashCode() {
        return ((((((((((((g() ? 1 : 0) * 31) + e()) * 31) + (f() ? 1 : 0)) * 31) + a()) * 31) + b()) * 31) + c()) * 31) + d();
    }
}
