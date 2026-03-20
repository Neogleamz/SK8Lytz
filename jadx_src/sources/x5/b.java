package x5;

import java.util.Collections;
import java.util.List;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements h {

    /* renamed from: b  reason: collision with root package name */
    public static final b f24156b = new b();

    /* renamed from: a  reason: collision with root package name */
    private final List<p5.b> f24157a;

    private b() {
        this.f24157a = Collections.emptyList();
    }

    public b(p5.b bVar) {
        this.f24157a = Collections.singletonList(bVar);
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
        return j8 >= 0 ? this.f24157a : Collections.emptyList();
    }

    @Override // p5.h
    public int i() {
        return 1;
    }
}
