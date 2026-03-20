package x;

import java.util.Objects;
import x.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends z.a {

    /* renamed from: a  reason: collision with root package name */
    private final g0.c<z.b> f23711a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23712b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(g0.c<z.b> cVar, int i8) {
        Objects.requireNonNull(cVar, "Null edge");
        this.f23711a = cVar;
        this.f23712b = i8;
    }

    @Override // x.z.a
    g0.c<z.b> a() {
        return this.f23711a;
    }

    @Override // x.z.a
    int b() {
        return this.f23712b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof z.a) {
            z.a aVar = (z.a) obj;
            return this.f23711a.equals(aVar.a()) && this.f23712b == aVar.b();
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23711a.hashCode() ^ 1000003) * 1000003) ^ this.f23712b;
    }

    public String toString() {
        return "In{edge=" + this.f23711a + ", format=" + this.f23712b + "}";
    }
}
