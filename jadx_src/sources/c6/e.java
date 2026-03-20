package c6;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e {

    /* renamed from: c  reason: collision with root package name */
    private boolean f8348c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f8349d;

    /* renamed from: f  reason: collision with root package name */
    private int f8351f;

    /* renamed from: a  reason: collision with root package name */
    private a f8346a = new a();

    /* renamed from: b  reason: collision with root package name */
    private a f8347b = new a();

    /* renamed from: e  reason: collision with root package name */
    private long f8350e = -9223372036854775807L;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private long f8352a;

        /* renamed from: b  reason: collision with root package name */
        private long f8353b;

        /* renamed from: c  reason: collision with root package name */
        private long f8354c;

        /* renamed from: d  reason: collision with root package name */
        private long f8355d;

        /* renamed from: e  reason: collision with root package name */
        private long f8356e;

        /* renamed from: f  reason: collision with root package name */
        private long f8357f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean[] f8358g = new boolean[15];

        /* renamed from: h  reason: collision with root package name */
        private int f8359h;

        private static int c(long j8) {
            return (int) (j8 % 15);
        }

        public long a() {
            long j8 = this.f8356e;
            if (j8 == 0) {
                return 0L;
            }
            return this.f8357f / j8;
        }

        public long b() {
            return this.f8357f;
        }

        public boolean d() {
            long j8 = this.f8355d;
            if (j8 == 0) {
                return false;
            }
            return this.f8358g[c(j8 - 1)];
        }

        public boolean e() {
            return this.f8355d > 15 && this.f8359h == 0;
        }

        public void f(long j8) {
            int i8;
            long j9 = this.f8355d;
            if (j9 == 0) {
                this.f8352a = j8;
            } else if (j9 == 1) {
                long j10 = j8 - this.f8352a;
                this.f8353b = j10;
                this.f8357f = j10;
                this.f8356e = 1L;
            } else {
                long j11 = j8 - this.f8354c;
                int c9 = c(j9);
                if (Math.abs(j11 - this.f8353b) <= 1000000) {
                    this.f8356e++;
                    this.f8357f += j11;
                    boolean[] zArr = this.f8358g;
                    if (zArr[c9]) {
                        zArr[c9] = false;
                        i8 = this.f8359h - 1;
                        this.f8359h = i8;
                    }
                } else {
                    boolean[] zArr2 = this.f8358g;
                    if (!zArr2[c9]) {
                        zArr2[c9] = true;
                        i8 = this.f8359h + 1;
                        this.f8359h = i8;
                    }
                }
            }
            this.f8355d++;
            this.f8354c = j8;
        }

        public void g() {
            this.f8355d = 0L;
            this.f8356e = 0L;
            this.f8357f = 0L;
            this.f8359h = 0;
            Arrays.fill(this.f8358g, false);
        }
    }

    public long a() {
        if (e()) {
            return this.f8346a.a();
        }
        return -9223372036854775807L;
    }

    public float b() {
        if (e()) {
            return (float) (1.0E9d / this.f8346a.a());
        }
        return -1.0f;
    }

    public int c() {
        return this.f8351f;
    }

    public long d() {
        if (e()) {
            return this.f8346a.b();
        }
        return -9223372036854775807L;
    }

    public boolean e() {
        return this.f8346a.e();
    }

    public void f(long j8) {
        this.f8346a.f(j8);
        if (this.f8346a.e() && !this.f8349d) {
            this.f8348c = false;
        } else if (this.f8350e != -9223372036854775807L) {
            if (!this.f8348c || this.f8347b.d()) {
                this.f8347b.g();
                this.f8347b.f(this.f8350e);
            }
            this.f8348c = true;
            this.f8347b.f(j8);
        }
        if (this.f8348c && this.f8347b.e()) {
            a aVar = this.f8346a;
            this.f8346a = this.f8347b;
            this.f8347b = aVar;
            this.f8348c = false;
            this.f8349d = false;
        }
        this.f8350e = j8;
        this.f8351f = this.f8346a.e() ? 0 : this.f8351f + 1;
    }

    public void g() {
        this.f8346a.g();
        this.f8347b.g();
        this.f8348c = false;
        this.f8350e = -9223372036854775807L;
        this.f8351f = 0;
    }
}
