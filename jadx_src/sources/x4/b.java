package x4;

import b6.z;
import n4.z;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements n4.k {

    /* renamed from: d  reason: collision with root package name */
    public static final n4.p f23813d = a.b;

    /* renamed from: a  reason: collision with root package name */
    private final c f23814a = new c();

    /* renamed from: b  reason: collision with root package name */
    private final z f23815b = new z(2786);

    /* renamed from: c  reason: collision with root package name */
    private boolean f23816c;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] d() {
        return new n4.k[]{new b()};
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23814a.d(mVar, new i0.d(0, 1));
        mVar.o();
        mVar.m(new z.b(-9223372036854775807L));
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f23816c = false;
        this.f23814a.c();
    }

    @Override // n4.k
    public int e(n4.l lVar, n4.y yVar) {
        int read = lVar.read(this.f23815b.e(), 0, 2786);
        if (read == -1) {
            return -1;
        }
        this.f23815b.U(0);
        this.f23815b.T(read);
        if (!this.f23816c) {
            this.f23814a.f(0L, 4);
            this.f23816c = true;
        }
        this.f23814a.b(this.f23815b);
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
            lVar.k(zVar.e(), 0, 6);
            zVar.U(0);
            if (zVar.N() != 2935) {
                lVar.h();
                i10++;
                if (i10 - i8 >= 8192) {
                    return false;
                }
                lVar.f(i10);
                i9 = 0;
            } else {
                i9++;
                if (i9 >= 4) {
                    return true;
                }
                int g8 = k4.b.g(zVar.e());
                if (g8 == -1) {
                    return false;
                }
                lVar.f(g8 - 6);
            }
        }
    }

    @Override // n4.k
    public void release() {
    }
}
