package x;

import androidx.camera.core.l1;
import java.util.Objects;
import x.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends l.b {

    /* renamed from: a  reason: collision with root package name */
    private final g0.c<l1> f23699a;

    /* renamed from: b  reason: collision with root package name */
    private final g0.c<a0> f23700b;

    /* renamed from: c  reason: collision with root package name */
    private final int f23701c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(g0.c<l1> cVar, g0.c<a0> cVar2, int i8) {
        Objects.requireNonNull(cVar, "Null imageEdge");
        this.f23699a = cVar;
        Objects.requireNonNull(cVar2, "Null requestEdge");
        this.f23700b = cVar2;
        this.f23701c = i8;
    }

    @Override // x.l.b
    int a() {
        return this.f23701c;
    }

    @Override // x.l.b
    g0.c<l1> b() {
        return this.f23699a;
    }

    @Override // x.l.b
    g0.c<a0> c() {
        return this.f23700b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof l.b) {
            l.b bVar = (l.b) obj;
            return this.f23699a.equals(bVar.b()) && this.f23700b.equals(bVar.c()) && this.f23701c == bVar.a();
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f23699a.hashCode() ^ 1000003) * 1000003) ^ this.f23700b.hashCode()) * 1000003) ^ this.f23701c;
    }

    public String toString() {
        return "Out{imageEdge=" + this.f23699a + ", requestEdge=" + this.f23700b + ", format=" + this.f23701c + "}";
    }
}
