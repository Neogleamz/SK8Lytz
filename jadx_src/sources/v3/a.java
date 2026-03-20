package v3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends b {

    /* renamed from: a  reason: collision with root package name */
    private final long f23172a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(long j8) {
        this.f23172a = j8;
    }

    @Override // v3.b
    public long c() {
        return this.f23172a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof b) && this.f23172a == ((b) obj).c();
    }

    public int hashCode() {
        long j8 = this.f23172a;
        return ((int) (j8 ^ (j8 >>> 32))) ^ 1000003;
    }

    public String toString() {
        return "LogResponse{nextRequestWaitMillis=" + this.f23172a + "}";
    }
}
