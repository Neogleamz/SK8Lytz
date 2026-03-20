package n6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final List f22186a;

        /* renamed from: b  reason: collision with root package name */
        private final Object f22187b;

        /* synthetic */ a(Object obj, g0 g0Var) {
            j.l(obj);
            this.f22187b = obj;
            this.f22186a = new ArrayList();
        }

        public a a(String str, Object obj) {
            j.l(str);
            String valueOf = String.valueOf(obj);
            this.f22186a.add(str + "=" + valueOf);
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(100);
            sb.append(this.f22187b.getClass().getSimpleName());
            sb.append('{');
            int size = this.f22186a.size();
            for (int i8 = 0; i8 < size; i8++) {
                sb.append((String) this.f22186a.get(i8));
                if (i8 < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static boolean a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int b(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static a c(Object obj) {
        return new a(obj, null);
    }
}
