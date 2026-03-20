package n4;

import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 {

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22058a = new byte[10];

    /* renamed from: b  reason: collision with root package name */
    private boolean f22059b;

    /* renamed from: c  reason: collision with root package name */
    private int f22060c;

    /* renamed from: d  reason: collision with root package name */
    private long f22061d;

    /* renamed from: e  reason: collision with root package name */
    private int f22062e;

    /* renamed from: f  reason: collision with root package name */
    private int f22063f;

    /* renamed from: g  reason: collision with root package name */
    private int f22064g;

    public void a(b0 b0Var, b0.a aVar) {
        if (this.f22060c > 0) {
            b0Var.d(this.f22061d, this.f22062e, this.f22063f, this.f22064g, aVar);
            this.f22060c = 0;
        }
    }

    public void b() {
        this.f22059b = false;
        this.f22060c = 0;
    }

    public void c(b0 b0Var, long j8, int i8, int i9, int i10, b0.a aVar) {
        b6.a.g(this.f22064g <= i9 + i10, "TrueHD chunk samples must be contiguous in the sample queue.");
        if (this.f22059b) {
            int i11 = this.f22060c;
            int i12 = i11 + 1;
            this.f22060c = i12;
            if (i11 == 0) {
                this.f22061d = j8;
                this.f22062e = i8;
                this.f22063f = 0;
            }
            this.f22063f += i9;
            this.f22064g = i10;
            if (i12 >= 16) {
                a(b0Var, aVar);
            }
        }
    }

    public void d(l lVar) {
        if (this.f22059b) {
            return;
        }
        lVar.k(this.f22058a, 0, 10);
        lVar.h();
        if (k4.b.j(this.f22058a) == 0) {
            return;
        }
        this.f22059b = true;
    }
}
