package w3;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Map;
import java.util.Objects;
import w3.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends i {

    /* renamed from: a  reason: collision with root package name */
    private final String f23467a;

    /* renamed from: b  reason: collision with root package name */
    private final Integer f23468b;

    /* renamed from: c  reason: collision with root package name */
    private final h f23469c;

    /* renamed from: d  reason: collision with root package name */
    private final long f23470d;

    /* renamed from: e  reason: collision with root package name */
    private final long f23471e;

    /* renamed from: f  reason: collision with root package name */
    private final Map<String, String> f23472f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: w3.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0223b extends i.a {

        /* renamed from: a  reason: collision with root package name */
        private String f23473a;

        /* renamed from: b  reason: collision with root package name */
        private Integer f23474b;

        /* renamed from: c  reason: collision with root package name */
        private h f23475c;

        /* renamed from: d  reason: collision with root package name */
        private Long f23476d;

        /* renamed from: e  reason: collision with root package name */
        private Long f23477e;

        /* renamed from: f  reason: collision with root package name */
        private Map<String, String> f23478f;

        @Override // w3.i.a
        public i d() {
            String str = this.f23473a;
            String str2 = BuildConfig.FLAVOR;
            if (str == null) {
                str2 = BuildConfig.FLAVOR + " transportName";
            }
            if (this.f23475c == null) {
                str2 = str2 + " encodedPayload";
            }
            if (this.f23476d == null) {
                str2 = str2 + " eventMillis";
            }
            if (this.f23477e == null) {
                str2 = str2 + " uptimeMillis";
            }
            if (this.f23478f == null) {
                str2 = str2 + " autoMetadata";
            }
            if (str2.isEmpty()) {
                return new b(this.f23473a, this.f23474b, this.f23475c, this.f23476d.longValue(), this.f23477e.longValue(), this.f23478f);
            }
            throw new IllegalStateException("Missing required properties:" + str2);
        }

        @Override // w3.i.a
        protected Map<String, String> e() {
            Map<String, String> map = this.f23478f;
            if (map != null) {
                return map;
            }
            throw new IllegalStateException("Property \"autoMetadata\" has not been set");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // w3.i.a
        public i.a f(Map<String, String> map) {
            Objects.requireNonNull(map, "Null autoMetadata");
            this.f23478f = map;
            return this;
        }

        @Override // w3.i.a
        public i.a g(Integer num) {
            this.f23474b = num;
            return this;
        }

        @Override // w3.i.a
        public i.a h(h hVar) {
            Objects.requireNonNull(hVar, "Null encodedPayload");
            this.f23475c = hVar;
            return this;
        }

        @Override // w3.i.a
        public i.a i(long j8) {
            this.f23476d = Long.valueOf(j8);
            return this;
        }

        @Override // w3.i.a
        public i.a j(String str) {
            Objects.requireNonNull(str, "Null transportName");
            this.f23473a = str;
            return this;
        }

        @Override // w3.i.a
        public i.a k(long j8) {
            this.f23477e = Long.valueOf(j8);
            return this;
        }
    }

    private b(String str, Integer num, h hVar, long j8, long j9, Map<String, String> map) {
        this.f23467a = str;
        this.f23468b = num;
        this.f23469c = hVar;
        this.f23470d = j8;
        this.f23471e = j9;
        this.f23472f = map;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // w3.i
    public Map<String, String> c() {
        return this.f23472f;
    }

    @Override // w3.i
    public Integer d() {
        return this.f23468b;
    }

    @Override // w3.i
    public h e() {
        return this.f23469c;
    }

    public boolean equals(Object obj) {
        Integer num;
        if (obj == this) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            return this.f23467a.equals(iVar.j()) && ((num = this.f23468b) != null ? num.equals(iVar.d()) : iVar.d() == null) && this.f23469c.equals(iVar.e()) && this.f23470d == iVar.f() && this.f23471e == iVar.k() && this.f23472f.equals(iVar.c());
        }
        return false;
    }

    @Override // w3.i
    public long f() {
        return this.f23470d;
    }

    public int hashCode() {
        int hashCode = (this.f23467a.hashCode() ^ 1000003) * 1000003;
        Integer num = this.f23468b;
        int hashCode2 = num == null ? 0 : num.hashCode();
        long j8 = this.f23470d;
        long j9 = this.f23471e;
        return ((((((((hashCode ^ hashCode2) * 1000003) ^ this.f23469c.hashCode()) * 1000003) ^ ((int) (j8 ^ (j8 >>> 32)))) * 1000003) ^ ((int) (j9 ^ (j9 >>> 32)))) * 1000003) ^ this.f23472f.hashCode();
    }

    @Override // w3.i
    public String j() {
        return this.f23467a;
    }

    @Override // w3.i
    public long k() {
        return this.f23471e;
    }

    public String toString() {
        return "EventInternal{transportName=" + this.f23467a + ", code=" + this.f23468b + ", encodedPayload=" + this.f23469c + ", eventMillis=" + this.f23470d + ", uptimeMillis=" + this.f23471e + ", autoMetadata=" + this.f23472f + "}";
    }
}
