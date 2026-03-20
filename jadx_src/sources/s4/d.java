package s4;

import n4.a0;
import n4.b0;
import n4.m;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements m {

    /* renamed from: a  reason: collision with root package name */
    private final long f22790a;

    /* renamed from: b  reason: collision with root package name */
    private final m f22791b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements z {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ z f22792a;

        a(z zVar) {
            this.f22792a = zVar;
        }

        @Override // n4.z
        public long d() {
            return this.f22792a.d();
        }

        @Override // n4.z
        public boolean h() {
            return this.f22792a.h();
        }

        @Override // n4.z
        public z.a i(long j8) {
            z.a i8 = this.f22792a.i(j8);
            a0 a0Var = i8.f22153a;
            a0 a0Var2 = new a0(a0Var.f22046a, a0Var.f22047b + d.this.f22790a);
            a0 a0Var3 = i8.f22154b;
            return new z.a(a0Var2, new a0(a0Var3.f22046a, a0Var3.f22047b + d.this.f22790a));
        }
    }

    public d(long j8, m mVar) {
        this.f22790a = j8;
        this.f22791b = mVar;
    }

    @Override // n4.m
    public b0 e(int i8, int i9) {
        return this.f22791b.e(i8, i9);
    }

    @Override // n4.m
    public void m(z zVar) {
        this.f22791b.m(new a(zVar));
    }

    @Override // n4.m
    public void o() {
        this.f22791b.o();
    }
}
