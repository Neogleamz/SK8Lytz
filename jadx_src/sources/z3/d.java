package z3;

import com.google.firebase.encoders.proto.Protobuf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: c  reason: collision with root package name */
    private static final d f24567c = new a().a();

    /* renamed from: a  reason: collision with root package name */
    private final long f24568a;

    /* renamed from: b  reason: collision with root package name */
    private final long f24569b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private long f24570a = 0;

        /* renamed from: b  reason: collision with root package name */
        private long f24571b = 0;

        a() {
        }

        public d a() {
            return new d(this.f24570a, this.f24571b);
        }

        public a b(long j8) {
            this.f24570a = j8;
            return this;
        }

        public a c(long j8) {
            this.f24571b = j8;
            return this;
        }
    }

    d(long j8, long j9) {
        this.f24568a = j8;
        this.f24569b = j9;
    }

    public static a c() {
        return new a();
    }

    @Protobuf(tag = 1)
    public long a() {
        return this.f24568a;
    }

    @Protobuf(tag = 2)
    public long b() {
        return this.f24569b;
    }
}
