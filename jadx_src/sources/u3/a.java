package u3;

import com.google.android.datatransport.Priority;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a<T> extends d<T> {

    /* renamed from: a  reason: collision with root package name */
    private final Integer f22984a;

    /* renamed from: b  reason: collision with root package name */
    private final T f22985b;

    /* renamed from: c  reason: collision with root package name */
    private final Priority f22986c;

    /* renamed from: d  reason: collision with root package name */
    private final e f22987d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Integer num, T t8, Priority priority, e eVar) {
        this.f22984a = num;
        Objects.requireNonNull(t8, "Null payload");
        this.f22985b = t8;
        Objects.requireNonNull(priority, "Null priority");
        this.f22986c = priority;
        this.f22987d = eVar;
    }

    @Override // u3.d
    public Integer a() {
        return this.f22984a;
    }

    @Override // u3.d
    public T b() {
        return this.f22985b;
    }

    @Override // u3.d
    public Priority c() {
        return this.f22986c;
    }

    @Override // u3.d
    public e d() {
        return this.f22987d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof d) {
            d dVar = (d) obj;
            Integer num = this.f22984a;
            if (num != null ? num.equals(dVar.a()) : dVar.a() == null) {
                if (this.f22985b.equals(dVar.b()) && this.f22986c.equals(dVar.c())) {
                    e eVar = this.f22987d;
                    e d8 = dVar.d();
                    if (eVar == null) {
                        if (d8 == null) {
                            return true;
                        }
                    } else if (eVar.equals(d8)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Integer num = this.f22984a;
        int hashCode = ((((((num == null ? 0 : num.hashCode()) ^ 1000003) * 1000003) ^ this.f22985b.hashCode()) * 1000003) ^ this.f22986c.hashCode()) * 1000003;
        e eVar = this.f22987d;
        return hashCode ^ (eVar != null ? eVar.hashCode() : 0);
    }

    public String toString() {
        return "Event{code=" + this.f22984a + ", payload=" + this.f22985b + ", priority=" + this.f22986c + ", productData=" + this.f22987d + "}";
    }
}
