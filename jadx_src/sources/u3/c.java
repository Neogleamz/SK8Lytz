package u3;

import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private final String f22989a;

    private c(String str) {
        Objects.requireNonNull(str, "name is null");
        this.f22989a = str;
    }

    public static c b(String str) {
        return new c(str);
    }

    public String a() {
        return this.f22989a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof c) {
            return this.f22989a.equals(((c) obj).f22989a);
        }
        return false;
    }

    public int hashCode() {
        return this.f22989a.hashCode() ^ 1000003;
    }

    public String toString() {
        return "Encoding{name=\"" + this.f22989a + "\"}";
    }
}
