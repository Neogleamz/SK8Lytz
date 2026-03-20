package b6;

import com.google.android.exoplayer2.x1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 implements r {

    /* renamed from: a  reason: collision with root package name */
    private final d f8030a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f8031b;

    /* renamed from: c  reason: collision with root package name */
    private long f8032c;

    /* renamed from: d  reason: collision with root package name */
    private long f8033d;

    /* renamed from: e  reason: collision with root package name */
    private x1 f8034e = x1.f11264d;

    public d0(d dVar) {
        this.f8030a = dVar;
    }

    public void a(long j8) {
        this.f8032c = j8;
        if (this.f8031b) {
            this.f8033d = this.f8030a.b();
        }
    }

    public void b() {
        if (this.f8031b) {
            return;
        }
        this.f8033d = this.f8030a.b();
        this.f8031b = true;
    }

    @Override // b6.r
    public x1 c() {
        return this.f8034e;
    }

    @Override // b6.r
    public void d(x1 x1Var) {
        if (this.f8031b) {
            a(f());
        }
        this.f8034e = x1Var;
    }

    public void e() {
        if (this.f8031b) {
            a(f());
            this.f8031b = false;
        }
    }

    @Override // b6.r
    public long f() {
        long j8 = this.f8032c;
        if (this.f8031b) {
            long b9 = this.f8030a.b() - this.f8033d;
            x1 x1Var = this.f8034e;
            return j8 + (x1Var.f11268a == 1.0f ? l0.C0(b9) : x1Var.b(b9));
        }
        return j8;
    }
}
