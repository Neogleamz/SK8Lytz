package w3;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.datatransport.Priority;
import java.util.Arrays;
import java.util.Objects;
import w3.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends o {

    /* renamed from: a  reason: collision with root package name */
    private final String f23489a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f23490b;

    /* renamed from: c  reason: collision with root package name */
    private final Priority f23491c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends o.a {

        /* renamed from: a  reason: collision with root package name */
        private String f23492a;

        /* renamed from: b  reason: collision with root package name */
        private byte[] f23493b;

        /* renamed from: c  reason: collision with root package name */
        private Priority f23494c;

        @Override // w3.o.a
        public o a() {
            String str = this.f23492a;
            String str2 = BuildConfig.FLAVOR;
            if (str == null) {
                str2 = BuildConfig.FLAVOR + " backendName";
            }
            if (this.f23494c == null) {
                str2 = str2 + " priority";
            }
            if (str2.isEmpty()) {
                return new d(this.f23492a, this.f23493b, this.f23494c);
            }
            throw new IllegalStateException("Missing required properties:" + str2);
        }

        @Override // w3.o.a
        public o.a b(String str) {
            Objects.requireNonNull(str, "Null backendName");
            this.f23492a = str;
            return this;
        }

        @Override // w3.o.a
        public o.a c(byte[] bArr) {
            this.f23493b = bArr;
            return this;
        }

        @Override // w3.o.a
        public o.a d(Priority priority) {
            Objects.requireNonNull(priority, "Null priority");
            this.f23494c = priority;
            return this;
        }
    }

    private d(String str, byte[] bArr, Priority priority) {
        this.f23489a = str;
        this.f23490b = bArr;
        this.f23491c = priority;
    }

    @Override // w3.o
    public String b() {
        return this.f23489a;
    }

    @Override // w3.o
    public byte[] c() {
        return this.f23490b;
    }

    @Override // w3.o
    public Priority d() {
        return this.f23491c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof o) {
            o oVar = (o) obj;
            if (this.f23489a.equals(oVar.b())) {
                if (Arrays.equals(this.f23490b, oVar instanceof d ? ((d) oVar).f23490b : oVar.c()) && this.f23491c.equals(oVar.d())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f23489a.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.f23490b)) * 1000003) ^ this.f23491c.hashCode();
    }
}
