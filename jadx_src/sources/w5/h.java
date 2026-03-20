package w5;

import b6.l0;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h implements p5.h {

    /* renamed from: a  reason: collision with root package name */
    private final d f23664a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f23665b;

    /* renamed from: c  reason: collision with root package name */
    private final Map<String, g> f23666c;

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, e> f23667d;

    /* renamed from: e  reason: collision with root package name */
    private final Map<String, String> f23668e;

    public h(d dVar, Map<String, g> map, Map<String, e> map2, Map<String, String> map3) {
        this.f23664a = dVar;
        this.f23667d = map2;
        this.f23668e = map3;
        this.f23666c = map != null ? Collections.unmodifiableMap(map) : Collections.emptyMap();
        this.f23665b = dVar.j();
    }

    @Override // p5.h
    public int c(long j8) {
        int e8 = l0.e(this.f23665b, j8, false, false);
        if (e8 < this.f23665b.length) {
            return e8;
        }
        return -1;
    }

    @Override // p5.h
    public long f(int i8) {
        return this.f23665b[i8];
    }

    @Override // p5.h
    public List<p5.b> h(long j8) {
        return this.f23664a.h(j8, this.f23666c, this.f23667d, this.f23668e);
    }

    @Override // p5.h
    public int i() {
        return this.f23665b.length;
    }
}
