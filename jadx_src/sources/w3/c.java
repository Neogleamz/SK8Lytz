package w3;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Objects;
import w3.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends n {

    /* renamed from: a  reason: collision with root package name */
    private final o f23479a;

    /* renamed from: b  reason: collision with root package name */
    private final String f23480b;

    /* renamed from: c  reason: collision with root package name */
    private final u3.d<?> f23481c;

    /* renamed from: d  reason: collision with root package name */
    private final u3.f<?, byte[]> f23482d;

    /* renamed from: e  reason: collision with root package name */
    private final u3.c f23483e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends n.a {

        /* renamed from: a  reason: collision with root package name */
        private o f23484a;

        /* renamed from: b  reason: collision with root package name */
        private String f23485b;

        /* renamed from: c  reason: collision with root package name */
        private u3.d<?> f23486c;

        /* renamed from: d  reason: collision with root package name */
        private u3.f<?, byte[]> f23487d;

        /* renamed from: e  reason: collision with root package name */
        private u3.c f23488e;

        @Override // w3.n.a
        public n a() {
            o oVar = this.f23484a;
            String str = BuildConfig.FLAVOR;
            if (oVar == null) {
                str = BuildConfig.FLAVOR + " transportContext";
            }
            if (this.f23485b == null) {
                str = str + " transportName";
            }
            if (this.f23486c == null) {
                str = str + " event";
            }
            if (this.f23487d == null) {
                str = str + " transformer";
            }
            if (this.f23488e == null) {
                str = str + " encoding";
            }
            if (str.isEmpty()) {
                return new c(this.f23484a, this.f23485b, this.f23486c, this.f23487d, this.f23488e);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // w3.n.a
        n.a b(u3.c cVar) {
            Objects.requireNonNull(cVar, "Null encoding");
            this.f23488e = cVar;
            return this;
        }

        @Override // w3.n.a
        n.a c(u3.d<?> dVar) {
            Objects.requireNonNull(dVar, "Null event");
            this.f23486c = dVar;
            return this;
        }

        @Override // w3.n.a
        n.a d(u3.f<?, byte[]> fVar) {
            Objects.requireNonNull(fVar, "Null transformer");
            this.f23487d = fVar;
            return this;
        }

        @Override // w3.n.a
        public n.a e(o oVar) {
            Objects.requireNonNull(oVar, "Null transportContext");
            this.f23484a = oVar;
            return this;
        }

        @Override // w3.n.a
        public n.a f(String str) {
            Objects.requireNonNull(str, "Null transportName");
            this.f23485b = str;
            return this;
        }
    }

    private c(o oVar, String str, u3.d<?> dVar, u3.f<?, byte[]> fVar, u3.c cVar) {
        this.f23479a = oVar;
        this.f23480b = str;
        this.f23481c = dVar;
        this.f23482d = fVar;
        this.f23483e = cVar;
    }

    @Override // w3.n
    public u3.c b() {
        return this.f23483e;
    }

    @Override // w3.n
    u3.d<?> c() {
        return this.f23481c;
    }

    @Override // w3.n
    u3.f<?, byte[]> e() {
        return this.f23482d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof n) {
            n nVar = (n) obj;
            return this.f23479a.equals(nVar.f()) && this.f23480b.equals(nVar.g()) && this.f23481c.equals(nVar.c()) && this.f23482d.equals(nVar.e()) && this.f23483e.equals(nVar.b());
        }
        return false;
    }

    @Override // w3.n
    public o f() {
        return this.f23479a;
    }

    @Override // w3.n
    public String g() {
        return this.f23480b;
    }

    public int hashCode() {
        return ((((((((this.f23479a.hashCode() ^ 1000003) * 1000003) ^ this.f23480b.hashCode()) * 1000003) ^ this.f23481c.hashCode()) * 1000003) ^ this.f23482d.hashCode()) * 1000003) ^ this.f23483e.hashCode();
    }

    public String toString() {
        return "SendRequest{transportContext=" + this.f23479a + ", transportName=" + this.f23480b + ", event=" + this.f23481c + ", transformer=" + this.f23482d + ", encoding=" + this.f23483e + "}";
    }
}
