package f1;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    private final Map<b<?>, Object> f19835a = new LinkedHashMap();

    /* renamed from: f1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0169a extends a {

        /* renamed from: b  reason: collision with root package name */
        public static final C0169a f19836b = new C0169a();

        private C0169a() {
        }

        @Override // f1.a
        public <T> T a(b<T> bVar) {
            p.e(bVar, "key");
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T> {
    }

    public abstract <T> T a(b<T> bVar);

    public final Map<b<?>, Object> b() {
        return this.f19835a;
    }
}
