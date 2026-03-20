package q5;

import java.util.Collections;
import java.util.List;
import p5.b;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f implements h {

    /* renamed from: a  reason: collision with root package name */
    private final List<b> f22626a;

    public f(List<b> list) {
        this.f22626a = list;
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
    public List<b> h(long j8) {
        return j8 >= 0 ? this.f22626a : Collections.emptyList();
    }

    @Override // p5.h
    public int i() {
        return 1;
    }
}
