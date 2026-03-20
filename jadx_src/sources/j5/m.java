package j5;

import a6.x;
import com.google.android.exoplayer2.w0;
import j5.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m extends f {

    /* renamed from: j  reason: collision with root package name */
    private final g f20783j;

    /* renamed from: k  reason: collision with root package name */
    private g.b f20784k;

    /* renamed from: l  reason: collision with root package name */
    private long f20785l;

    /* renamed from: m  reason: collision with root package name */
    private volatile boolean f20786m;

    public m(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, g gVar) {
        super(hVar, aVar, 2, w0Var, i8, obj, -9223372036854775807L, -9223372036854775807L);
        this.f20783j = gVar;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void a() {
        if (this.f20785l == 0) {
            this.f20783j.b(this.f20784k, -9223372036854775807L, -9223372036854775807L);
        }
        try {
            com.google.android.exoplayer2.upstream.a e8 = this.f20742b.e(this.f20785l);
            x xVar = this.f20749i;
            n4.e eVar = new n4.e(xVar, e8.f10948g, xVar.x(e8));
            while (!this.f20786m && this.f20783j.a(eVar)) {
            }
            this.f20785l = eVar.getPosition() - this.f20742b.f10948g;
        } finally {
            a6.j.a(this.f20749i);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void c() {
        this.f20786m = true;
    }

    public void g(g.b bVar) {
        this.f20784k = bVar;
    }
}
