package androidx.core.util;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d<F, S> {

    /* renamed from: a  reason: collision with root package name */
    public final F f4889a;

    /* renamed from: b  reason: collision with root package name */
    public final S f4890b;

    public d(F f5, S s8) {
        this.f4889a = f5;
        this.f4890b = s8;
    }

    public static <A, B> d<A, B> a(A a9, B b9) {
        return new d<>(a9, b9);
    }

    public boolean equals(Object obj) {
        if (obj instanceof d) {
            d dVar = (d) obj;
            return c.a(dVar.f4889a, this.f4889a) && c.a(dVar.f4890b, this.f4890b);
        }
        return false;
    }

    public int hashCode() {
        F f5 = this.f4889a;
        int hashCode = f5 == null ? 0 : f5.hashCode();
        S s8 = this.f4890b;
        return hashCode ^ (s8 != null ? s8.hashCode() : 0);
    }

    public String toString() {
        return "Pair{" + this.f4889a + " " + this.f4890b + "}";
    }
}
