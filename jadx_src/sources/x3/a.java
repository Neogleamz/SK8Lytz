package x3;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
import java.util.Objects;
import x3.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends e {

    /* renamed from: a  reason: collision with root package name */
    private final Iterable<w3.i> f23772a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f23773b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends e.a {

        /* renamed from: a  reason: collision with root package name */
        private Iterable<w3.i> f23774a;

        /* renamed from: b  reason: collision with root package name */
        private byte[] f23775b;

        @Override // x3.e.a
        public e a() {
            Iterable<w3.i> iterable = this.f23774a;
            String str = BuildConfig.FLAVOR;
            if (iterable == null) {
                str = BuildConfig.FLAVOR + " events";
            }
            if (str.isEmpty()) {
                return new a(this.f23774a, this.f23775b);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // x3.e.a
        public e.a b(Iterable<w3.i> iterable) {
            Objects.requireNonNull(iterable, "Null events");
            this.f23774a = iterable;
            return this;
        }

        @Override // x3.e.a
        public e.a c(byte[] bArr) {
            this.f23775b = bArr;
            return this;
        }
    }

    private a(Iterable<w3.i> iterable, byte[] bArr) {
        this.f23772a = iterable;
        this.f23773b = bArr;
    }

    @Override // x3.e
    public Iterable<w3.i> b() {
        return this.f23772a;
    }

    @Override // x3.e
    public byte[] c() {
        return this.f23773b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            if (this.f23772a.equals(eVar.b())) {
                if (Arrays.equals(this.f23773b, eVar instanceof a ? ((a) eVar).f23773b : eVar.c())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23772a.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.f23773b);
    }

    public String toString() {
        return "BackendRequest{events=" + this.f23772a + ", extras=" + Arrays.toString(this.f23773b) + "}";
    }
}
