package w4;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import n4.b0;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements k {

    /* renamed from: d  reason: collision with root package name */
    public static final p f23552d = c.b;

    /* renamed from: a  reason: collision with root package name */
    private m f23553a;

    /* renamed from: b  reason: collision with root package name */
    private i f23554b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f23555c;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] d() {
        return new k[]{new d()};
    }

    private static z f(z zVar) {
        zVar.U(0);
        return zVar;
    }

    private boolean h(l lVar) {
        i hVar;
        f fVar = new f();
        if (fVar.a(lVar, true) && (fVar.f23562b & 2) == 2) {
            int min = Math.min(fVar.f23569i, 8);
            z zVar = new z(min);
            lVar.k(zVar.e(), 0, min);
            if (b.p(f(zVar))) {
                hVar = new b();
            } else if (j.r(f(zVar))) {
                hVar = new j();
            } else if (h.o(f(zVar))) {
                hVar = new h();
            }
            this.f23554b = hVar;
            return true;
        }
        return false;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f23553a = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        i iVar = this.f23554b;
        if (iVar != null) {
            iVar.m(j8, j9);
        }
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        b6.a.h(this.f23553a);
        if (this.f23554b == null) {
            if (!h(lVar)) {
                throw ParserException.a("Failed to determine bitstream type", null);
            }
            lVar.h();
        }
        if (!this.f23555c) {
            b0 e8 = this.f23553a.e(0, 1);
            this.f23553a.o();
            this.f23554b.d(this.f23553a, e8);
            this.f23555c = true;
        }
        return this.f23554b.g(lVar, yVar);
    }

    @Override // n4.k
    public boolean g(l lVar) {
        try {
            return h(lVar);
        } catch (ParserException unused) {
            return false;
        }
    }

    @Override // n4.k
    public void release() {
    }
}
