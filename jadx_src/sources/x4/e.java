package x4;

import b6.z;
import n4.z;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements n4.k {

    /* renamed from: d  reason: collision with root package name */
    public static final n4.p f23837d = d.b;

    /* renamed from: a  reason: collision with root package name */
    private final f f23838a = new f();

    /* renamed from: b  reason: collision with root package name */
    private final z f23839b = new z(16384);

    /* renamed from: c  reason: collision with root package name */
    private boolean f23840c;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] d() {
        return new n4.k[]{new e()};
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23838a.d(mVar, new i0.d(0, 1));
        mVar.o();
        mVar.m(new z.b(-9223372036854775807L));
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f23840c = false;
        this.f23838a.c();
    }

    @Override // n4.k
    public int e(n4.l lVar, n4.y yVar) {
        int read = lVar.read(this.f23839b.e(), 0, 16384);
        if (read == -1) {
            return -1;
        }
        this.f23839b.U(0);
        this.f23839b.T(read);
        if (!this.f23840c) {
            this.f23838a.f(0L, 4);
            this.f23840c = true;
        }
        this.f23838a.b(this.f23839b);
        return 0;
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        b6.z zVar = new b6.z(10);
        int i8 = 0;
        while (true) {
            lVar.k(zVar.e(), 0, 10);
            zVar.U(0);
            if (zVar.K() != 4801587) {
                break;
            }
            zVar.V(3);
            int G = zVar.G();
            i8 += G + 10;
            lVar.f(G);
        }
        lVar.h();
        lVar.f(i8);
        int i9 = 0;
        int i10 = i8;
        while (true) {
            lVar.k(zVar.e(), 0, 7);
            zVar.U(0);
            int N = zVar.N();
            if (N == 44096 || N == 44097) {
                i9++;
                if (i9 >= 4) {
                    return true;
                }
                int e8 = k4.c.e(zVar.e(), N);
                if (e8 == -1) {
                    return false;
                }
                lVar.f(e8 - 7);
            } else {
                lVar.h();
                i10++;
                if (i10 - i8 >= 8192) {
                    return false;
                }
                lVar.f(i10);
                i9 = 0;
            }
        }
    }

    @Override // n4.k
    public void release() {
    }
}
