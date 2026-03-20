package g0;

import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends l {

    /* renamed from: a  reason: collision with root package name */
    private final List<k> f20144a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(List<k> list) {
        Objects.requireNonNull(list, "Null surfaces");
        this.f20144a = list;
    }

    @Override // g0.l
    public List<k> b() {
        return this.f20144a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof l) {
            return this.f20144a.equals(((l) obj).b());
        }
        return false;
    }

    public int hashCode() {
        return this.f20144a.hashCode() ^ 1000003;
    }

    public String toString() {
        return "SurfaceEdge{surfaces=" + this.f20144a + "}";
    }
}
