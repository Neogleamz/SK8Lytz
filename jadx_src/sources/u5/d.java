package u5;

import b6.l0;
import java.util.Collections;
import java.util.List;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d implements h {

    /* renamed from: a  reason: collision with root package name */
    private final List<List<p5.b>> f23065a;

    /* renamed from: b  reason: collision with root package name */
    private final List<Long> f23066b;

    public d(List<List<p5.b>> list, List<Long> list2) {
        this.f23065a = list;
        this.f23066b = list2;
    }

    @Override // p5.h
    public int c(long j8) {
        int d8 = l0.d(this.f23066b, Long.valueOf(j8), false, false);
        if (d8 < this.f23066b.size()) {
            return d8;
        }
        return -1;
    }

    @Override // p5.h
    public long f(int i8) {
        b6.a.a(i8 >= 0);
        b6.a.a(i8 < this.f23066b.size());
        return this.f23066b.get(i8).longValue();
    }

    @Override // p5.h
    public List<p5.b> h(long j8) {
        int g8 = l0.g(this.f23066b, Long.valueOf(j8), true, false);
        return g8 == -1 ? Collections.emptyList() : this.f23065a.get(g8);
    }

    @Override // p5.h
    public int i() {
        return this.f23066b.size();
    }
}
