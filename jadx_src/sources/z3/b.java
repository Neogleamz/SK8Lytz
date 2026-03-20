package z3;

import com.google.firebase.encoders.proto.Protobuf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: b  reason: collision with root package name */
    private static final b f24559b = new a().a();

    /* renamed from: a  reason: collision with root package name */
    private final d f24560a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private d f24561a = null;

        a() {
        }

        public b a() {
            return new b(this.f24561a);
        }

        public a b(d dVar) {
            this.f24561a = dVar;
            return this;
        }
    }

    b(d dVar) {
        this.f24560a = dVar;
    }

    public static a b() {
        return new a();
    }

    @Protobuf(tag = 1)
    public d a() {
        return this.f24560a;
    }
}
