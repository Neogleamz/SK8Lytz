package x4;

import b6.z;
import com.google.android.exoplayer2.w0;
import java.util.Collections;
import java.util.List;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l implements m {

    /* renamed from: a  reason: collision with root package name */
    private final List<i0.a> f23954a;

    /* renamed from: b  reason: collision with root package name */
    private final n4.b0[] f23955b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f23956c;

    /* renamed from: d  reason: collision with root package name */
    private int f23957d;

    /* renamed from: e  reason: collision with root package name */
    private int f23958e;

    /* renamed from: f  reason: collision with root package name */
    private long f23959f = -9223372036854775807L;

    public l(List<i0.a> list) {
        this.f23954a = list;
        this.f23955b = new n4.b0[list.size()];
    }

    private boolean a(z zVar, int i8) {
        if (zVar.a() == 0) {
            return false;
        }
        if (zVar.H() != i8) {
            this.f23956c = false;
        }
        this.f23957d--;
        return this.f23956c;
    }

    @Override // x4.m
    public void b(z zVar) {
        n4.b0[] b0VarArr;
        if (this.f23956c) {
            if (this.f23957d != 2 || a(zVar, 32)) {
                if (this.f23957d != 1 || a(zVar, 0)) {
                    int f5 = zVar.f();
                    int a9 = zVar.a();
                    for (n4.b0 b0Var : this.f23955b) {
                        zVar.U(f5);
                        b0Var.b(zVar, a9);
                    }
                    this.f23958e += a9;
                }
            }
        }
    }

    @Override // x4.m
    public void c() {
        this.f23956c = false;
        this.f23959f = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        for (int i8 = 0; i8 < this.f23955b.length; i8++) {
            i0.a aVar = this.f23954a.get(i8);
            dVar.a();
            n4.b0 e8 = mVar.e(dVar.c(), 3);
            e8.f(new w0.b().U(dVar.b()).g0("application/dvbsubs").V(Collections.singletonList(aVar.f23929c)).X(aVar.f23927a).G());
            this.f23955b[i8] = e8;
        }
    }

    @Override // x4.m
    public void e() {
        if (this.f23956c) {
            if (this.f23959f != -9223372036854775807L) {
                for (n4.b0 b0Var : this.f23955b) {
                    b0Var.d(this.f23959f, 1, this.f23958e, 0, null);
                }
            }
            this.f23956c = false;
        }
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if ((i8 & 4) == 0) {
            return;
        }
        this.f23956c = true;
        if (j8 != -9223372036854775807L) {
            this.f23959f = j8;
        }
        this.f23958e = 0;
        this.f23957d = 2;
    }
}
