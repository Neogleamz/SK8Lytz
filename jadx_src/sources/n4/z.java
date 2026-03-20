package n4;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface z {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final a0 f22153a;

        /* renamed from: b  reason: collision with root package name */
        public final a0 f22154b;

        public a(a0 a0Var) {
            this(a0Var, a0Var);
        }

        public a(a0 a0Var, a0 a0Var2) {
            this.f22153a = (a0) b6.a.e(a0Var);
            this.f22154b = (a0) b6.a.e(a0Var2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || a.class != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f22153a.equals(aVar.f22153a) && this.f22154b.equals(aVar.f22154b);
        }

        public int hashCode() {
            return (this.f22153a.hashCode() * 31) + this.f22154b.hashCode();
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.f22153a);
            if (this.f22153a.equals(this.f22154b)) {
                str = BuildConfig.FLAVOR;
            } else {
                str = ", " + this.f22154b;
            }
            sb.append(str);
            sb.append("]");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements z {

        /* renamed from: a  reason: collision with root package name */
        private final long f22155a;

        /* renamed from: b  reason: collision with root package name */
        private final a f22156b;

        public b(long j8) {
            this(j8, 0L);
        }

        public b(long j8, long j9) {
            this.f22155a = j8;
            this.f22156b = new a(j9 == 0 ? a0.f22045c : new a0(0L, j9));
        }

        @Override // n4.z
        public long d() {
            return this.f22155a;
        }

        @Override // n4.z
        public boolean h() {
            return false;
        }

        @Override // n4.z
        public a i(long j8) {
            return this.f22156b;
        }
    }

    long d();

    boolean h();

    a i(long j8);
}
