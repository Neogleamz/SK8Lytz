package z3;

import com.google.firebase.encoders.proto.Protobuf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: c  reason: collision with root package name */
    private static final e f24572c = new a().a();

    /* renamed from: a  reason: collision with root package name */
    private final long f24573a;

    /* renamed from: b  reason: collision with root package name */
    private final long f24574b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private long f24575a = 0;

        /* renamed from: b  reason: collision with root package name */
        private long f24576b = 0;

        a() {
        }

        public e a() {
            return new e(this.f24575a, this.f24576b);
        }

        public a b(long j8) {
            this.f24576b = j8;
            return this;
        }

        public a c(long j8) {
            this.f24575a = j8;
            return this;
        }
    }

    e(long j8, long j9) {
        this.f24573a = j8;
        this.f24574b = j9;
    }

    public static a c() {
        return new a();
    }

    @Protobuf(tag = 2)
    public long a() {
        return this.f24574b;
    }

    @Protobuf(tag = 1)
    public long b() {
        return this.f24573a;
    }
}
