package j5;

import a6.x;
import com.google.android.exoplayer2.w0;
import j5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends a {

    /* renamed from: o  reason: collision with root package name */
    private final int f20776o;

    /* renamed from: p  reason: collision with root package name */
    private final long f20777p;
    private final g q;

    /* renamed from: r  reason: collision with root package name */
    private long f20778r;

    /* renamed from: s  reason: collision with root package name */
    private volatile boolean f20779s;

    /* renamed from: t  reason: collision with root package name */
    private boolean f20780t;

    public k(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, long j8, long j9, long j10, long j11, long j12, int i9, long j13, g gVar) {
        super(hVar, aVar, w0Var, i8, obj, j8, j9, j10, j11, j12);
        this.f20776o = i9;
        this.f20777p = j13;
        this.q = gVar;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void a() {
        if (this.f20778r == 0) {
            c j8 = j();
            j8.b(this.f20777p);
            g gVar = this.q;
            g.b l8 = l(j8);
            long j9 = this.f20714k;
            long j10 = j9 == -9223372036854775807L ? -9223372036854775807L : j9 - this.f20777p;
            long j11 = this.f20715l;
            gVar.b(l8, j10, j11 == -9223372036854775807L ? -9223372036854775807L : j11 - this.f20777p);
        }
        try {
            com.google.android.exoplayer2.upstream.a e8 = this.f20742b.e(this.f20778r);
            x xVar = this.f20749i;
            n4.e eVar = new n4.e(xVar, e8.f10948g, xVar.x(e8));
            while (!this.f20779s && this.q.a(eVar)) {
            }
            this.f20778r = eVar.getPosition() - this.f20742b.f10948g;
            a6.j.a(this.f20749i);
            this.f20780t = !this.f20779s;
        } catch (Throwable th) {
            a6.j.a(this.f20749i);
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void c() {
        this.f20779s = true;
    }

    @Override // j5.n
    public long g() {
        return this.f20787j + this.f20776o;
    }

    @Override // j5.n
    public boolean h() {
        return this.f20780t;
    }

    protected g.b l(c cVar) {
        return cVar;
    }
}
