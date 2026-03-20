package w4;

import b6.l0;
import java.util.Arrays;
import n4.l;
import n4.q;
import n4.r;
import n4.s;
import n4.t;
import n4.z;
import w4.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends i {

    /* renamed from: n  reason: collision with root package name */
    private t f23546n;

    /* renamed from: o  reason: collision with root package name */
    private a f23547o;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements g {

        /* renamed from: a  reason: collision with root package name */
        private t f23548a;

        /* renamed from: b  reason: collision with root package name */
        private t.a f23549b;

        /* renamed from: c  reason: collision with root package name */
        private long f23550c = -1;

        /* renamed from: d  reason: collision with root package name */
        private long f23551d = -1;

        public a(t tVar, t.a aVar) {
            this.f23548a = tVar;
            this.f23549b = aVar;
        }

        @Override // w4.g
        public long a(l lVar) {
            long j8 = this.f23551d;
            if (j8 >= 0) {
                long j9 = -(j8 + 2);
                this.f23551d = -1L;
                return j9;
            }
            return -1L;
        }

        @Override // w4.g
        public z b() {
            b6.a.f(this.f23550c != -1);
            return new s(this.f23548a, this.f23550c);
        }

        @Override // w4.g
        public void c(long j8) {
            long[] jArr = this.f23549b.f22141a;
            this.f23551d = jArr[l0.i(jArr, j8, true, true)];
        }

        public void d(long j8) {
            this.f23550c = j8;
        }
    }

    private int n(b6.z zVar) {
        int i8 = (zVar.e()[2] & 255) >> 4;
        if (i8 == 6 || i8 == 7) {
            zVar.V(4);
            zVar.O();
        }
        int j8 = q.j(zVar, i8);
        zVar.U(0);
        return j8;
    }

    private static boolean o(byte[] bArr) {
        return bArr[0] == -1;
    }

    public static boolean p(b6.z zVar) {
        return zVar.a() >= 5 && zVar.H() == 127 && zVar.J() == 1179402563;
    }

    @Override // w4.i
    protected long f(b6.z zVar) {
        if (o(zVar.e())) {
            return n(zVar);
        }
        return -1L;
    }

    @Override // w4.i
    protected boolean h(b6.z zVar, long j8, i.b bVar) {
        byte[] e8 = zVar.e();
        t tVar = this.f23546n;
        if (tVar == null) {
            t tVar2 = new t(e8, 17);
            this.f23546n = tVar2;
            bVar.f23588a = tVar2.g(Arrays.copyOfRange(e8, 9, zVar.g()), null);
            return true;
        } else if ((e8[0] & Byte.MAX_VALUE) == 3) {
            t.a f5 = r.f(zVar);
            t b9 = tVar.b(f5);
            this.f23546n = b9;
            this.f23547o = new a(b9, f5);
            return true;
        } else if (o(e8)) {
            a aVar = this.f23547o;
            if (aVar != null) {
                aVar.d(j8);
                bVar.f23589b = this.f23547o;
            }
            b6.a.e(bVar.f23588a);
            return false;
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // w4.i
    public void l(boolean z4) {
        super.l(z4);
        if (z4) {
            this.f23546n = null;
            this.f23547o = null;
        }
    }
}
