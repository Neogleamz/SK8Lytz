package x4;

import b6.a;
import b6.z;
import com.google.android.exoplayer2.w0;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r implements m {

    /* renamed from: b  reason: collision with root package name */
    private n4.b0 f24083b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f24084c;

    /* renamed from: e  reason: collision with root package name */
    private int f24086e;

    /* renamed from: f  reason: collision with root package name */
    private int f24087f;

    /* renamed from: a  reason: collision with root package name */
    private final z f24082a = new z(10);

    /* renamed from: d  reason: collision with root package name */
    private long f24085d = -9223372036854775807L;

    @Override // x4.m
    public void b(z zVar) {
        a.h(this.f24083b);
        if (this.f24084c) {
            int a9 = zVar.a();
            int i8 = this.f24087f;
            if (i8 < 10) {
                int min = Math.min(a9, 10 - i8);
                System.arraycopy(zVar.e(), zVar.f(), this.f24082a.e(), this.f24087f, min);
                if (this.f24087f + min == 10) {
                    this.f24082a.U(0);
                    if (73 != this.f24082a.H() || 68 != this.f24082a.H() || 51 != this.f24082a.H()) {
                        b6.p.i("Id3Reader", "Discarding invalid ID3 tag");
                        this.f24084c = false;
                        return;
                    }
                    this.f24082a.V(3);
                    this.f24086e = this.f24082a.G() + 10;
                }
            }
            int min2 = Math.min(a9, this.f24086e - this.f24087f);
            this.f24083b.b(zVar, min2);
            this.f24087f += min2;
        }
    }

    @Override // x4.m
    public void c() {
        this.f24084c = false;
        this.f24085d = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        n4.b0 e8 = mVar.e(dVar.c(), 5);
        this.f24083b = e8;
        e8.f(new w0.b().U(dVar.b()).g0("application/id3").G());
    }

    @Override // x4.m
    public void e() {
        int i8;
        a.h(this.f24083b);
        if (this.f24084c && (i8 = this.f24086e) != 0 && this.f24087f == i8) {
            long j8 = this.f24085d;
            if (j8 != -9223372036854775807L) {
                this.f24083b.d(j8, 1, i8, 0, null);
            }
            this.f24084c = false;
        }
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if ((i8 & 4) == 0) {
            return;
        }
        this.f24084c = true;
        if (j8 != -9223372036854775807L) {
            this.f24085d = j8;
        }
        this.f24086e = 0;
        this.f24087f = 0;
    }
}
