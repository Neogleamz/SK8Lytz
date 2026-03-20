package j5;

import com.google.android.exoplayer2.w0;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p extends a {

    /* renamed from: o  reason: collision with root package name */
    private final int f20789o;

    /* renamed from: p  reason: collision with root package name */
    private final w0 f20790p;
    private long q;

    /* renamed from: r  reason: collision with root package name */
    private boolean f20791r;

    public p(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, long j8, long j9, long j10, int i9, w0 w0Var2) {
        super(hVar, aVar, w0Var, i8, obj, j8, j9, -9223372036854775807L, -9223372036854775807L, j10);
        this.f20789o = i9;
        this.f20790p = w0Var2;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void a() {
        c j8 = j();
        j8.b(0L);
        b0 e8 = j8.e(0, this.f20789o);
        e8.f(this.f20790p);
        try {
            long x8 = this.f20749i.x(this.f20742b.e(this.q));
            if (x8 != -1) {
                x8 += this.q;
            }
            n4.e eVar = new n4.e(this.f20749i, this.q, x8);
            for (int i8 = 0; i8 != -1; i8 = e8.c(eVar, Integer.MAX_VALUE, true)) {
                this.q += i8;
            }
            e8.d(this.f20747g, 1, (int) this.q, 0, null);
            a6.j.a(this.f20749i);
            this.f20791r = true;
        } catch (Throwable th) {
            a6.j.a(this.f20749i);
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void c() {
    }

    @Override // j5.n
    public boolean h() {
        return this.f20791r;
    }
}
