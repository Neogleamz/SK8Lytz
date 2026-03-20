package y;

import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends d0 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f24291a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Object obj) {
        Objects.requireNonNull(obj, "Null value");
        this.f24291a = obj;
    }

    @Override // y.d0
    public Object b() {
        return this.f24291a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof d0) {
            return this.f24291a.equals(((d0) obj).b());
        }
        return false;
    }

    public int hashCode() {
        return this.f24291a.hashCode() ^ 1000003;
    }

    public String toString() {
        return "Identifier{value=" + this.f24291a + "}";
    }
}
