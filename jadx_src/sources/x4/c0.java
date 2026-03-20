package x4;

import b6.l0;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 implements i0 {

    /* renamed from: a  reason: collision with root package name */
    private final b0 f23829a;

    /* renamed from: b  reason: collision with root package name */
    private final z f23830b = new z(32);

    /* renamed from: c  reason: collision with root package name */
    private int f23831c;

    /* renamed from: d  reason: collision with root package name */
    private int f23832d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23833e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f23834f;

    public c0(b0 b0Var) {
        this.f23829a = b0Var;
    }

    @Override // x4.i0
    public void a(b6.h0 h0Var, n4.m mVar, i0.d dVar) {
        this.f23829a.a(h0Var, mVar, dVar);
        this.f23834f = true;
    }

    @Override // x4.i0
    public void b(z zVar, int i8) {
        boolean z4 = (i8 & 1) != 0;
        int f5 = z4 ? zVar.f() + zVar.H() : -1;
        if (this.f23834f) {
            if (!z4) {
                return;
            }
            this.f23834f = false;
            zVar.U(f5);
            this.f23832d = 0;
        }
        while (zVar.a() > 0) {
            int i9 = this.f23832d;
            if (i9 < 3) {
                if (i9 == 0) {
                    int H = zVar.H();
                    zVar.U(zVar.f() - 1);
                    if (H == 255) {
                        this.f23834f = true;
                        return;
                    }
                }
                int min = Math.min(zVar.a(), 3 - this.f23832d);
                zVar.l(this.f23830b.e(), this.f23832d, min);
                int i10 = this.f23832d + min;
                this.f23832d = i10;
                if (i10 == 3) {
                    this.f23830b.U(0);
                    this.f23830b.T(3);
                    this.f23830b.V(1);
                    int H2 = this.f23830b.H();
                    int H3 = this.f23830b.H();
                    this.f23833e = (H2 & RecognitionOptions.ITF) != 0;
                    this.f23831c = (((H2 & 15) << 8) | H3) + 3;
                    int b9 = this.f23830b.b();
                    int i11 = this.f23831c;
                    if (b9 < i11) {
                        this.f23830b.c(Math.min(4098, Math.max(i11, this.f23830b.b() * 2)));
                    }
                }
            } else {
                int min2 = Math.min(zVar.a(), this.f23831c - this.f23832d);
                zVar.l(this.f23830b.e(), this.f23832d, min2);
                int i12 = this.f23832d + min2;
                this.f23832d = i12;
                int i13 = this.f23831c;
                if (i12 != i13) {
                    continue;
                } else {
                    if (!this.f23833e) {
                        this.f23830b.T(i13);
                    } else if (l0.t(this.f23830b.e(), 0, this.f23831c, -1) != 0) {
                        this.f23834f = true;
                        return;
                    } else {
                        this.f23830b.T(this.f23831c - 4);
                    }
                    this.f23830b.U(0);
                    this.f23829a.b(this.f23830b);
                    this.f23832d = 0;
                }
            }
        }
    }

    @Override // x4.i0
    public void c() {
        this.f23834f = true;
    }
}
