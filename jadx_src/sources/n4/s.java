package n4;

import b6.l0;
import n4.t;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s implements z {

    /* renamed from: a  reason: collision with root package name */
    private final t f22127a;

    /* renamed from: b  reason: collision with root package name */
    private final long f22128b;

    public s(t tVar, long j8) {
        this.f22127a = tVar;
        this.f22128b = j8;
    }

    private a0 b(long j8, long j9) {
        return new a0((j8 * 1000000) / this.f22127a.f22133e, this.f22128b + j9);
    }

    @Override // n4.z
    public long d() {
        return this.f22127a.f();
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        b6.a.h(this.f22127a.f22139k);
        t tVar = this.f22127a;
        t.a aVar = tVar.f22139k;
        long[] jArr = aVar.f22141a;
        long[] jArr2 = aVar.f22142b;
        int i8 = l0.i(jArr, tVar.i(j8), true, false);
        a0 b9 = b(i8 == -1 ? 0L : jArr[i8], i8 != -1 ? jArr2[i8] : 0L);
        if (b9.f22046a == j8 || i8 == jArr.length - 1) {
            return new z.a(b9);
        }
        int i9 = i8 + 1;
        return new z.a(b9, b(jArr[i9], jArr2[i9]));
    }
}
