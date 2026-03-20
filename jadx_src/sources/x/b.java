package x;

import android.util.Size;
import java.util.Objects;
import x.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends l.a {

    /* renamed from: c  reason: collision with root package name */
    private final Size f23696c;

    /* renamed from: d  reason: collision with root package name */
    private final int f23697d;

    /* renamed from: e  reason: collision with root package name */
    private final g0.c<a0> f23698e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Size size, int i8, g0.c<a0> cVar) {
        Objects.requireNonNull(size, "Null size");
        this.f23696c = size;
        this.f23697d = i8;
        Objects.requireNonNull(cVar, "Null requestEdge");
        this.f23698e = cVar;
    }

    @Override // x.l.a
    int c() {
        return this.f23697d;
    }

    @Override // x.l.a
    g0.c<a0> d() {
        return this.f23698e;
    }

    @Override // x.l.a
    Size e() {
        return this.f23696c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof l.a) {
            l.a aVar = (l.a) obj;
            return this.f23696c.equals(aVar.e()) && this.f23697d == aVar.c() && this.f23698e.equals(aVar.d());
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f23696c.hashCode() ^ 1000003) * 1000003) ^ this.f23697d) * 1000003) ^ this.f23698e.hashCode();
    }

    public String toString() {
        return "In{size=" + this.f23696c + ", format=" + this.f23697d + ", requestEdge=" + this.f23698e + "}";
    }
}
