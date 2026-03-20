package y5;

import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements p5.h {

    /* renamed from: a  reason: collision with root package name */
    private final List<p5.b> f24423a;

    public b(List<p5.b> list) {
        this.f24423a = Collections.unmodifiableList(list);
    }

    @Override // p5.h
    public int c(long j8) {
        return j8 < 0 ? 0 : -1;
    }

    @Override // p5.h
    public long f(int i8) {
        b6.a.a(i8 == 0);
        return 0L;
    }

    @Override // p5.h
    public List<p5.b> h(long j8) {
        return j8 >= 0 ? this.f24423a : Collections.emptyList();
    }

    @Override // p5.h
    public int i() {
        return 1;
    }
}
