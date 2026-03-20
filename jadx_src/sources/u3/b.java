package u3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends e {

    /* renamed from: a  reason: collision with root package name */
    private final Integer f22988a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Integer num) {
        this.f22988a = num;
    }

    @Override // u3.e
    public Integer a() {
        return this.f22988a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof e) {
            Integer num = this.f22988a;
            Integer a9 = ((e) obj).a();
            return num == null ? a9 == null : num.equals(a9);
        }
        return false;
    }

    public int hashCode() {
        Integer num = this.f22988a;
        return (num == null ? 0 : num.hashCode()) ^ 1000003;
    }

    public String toString() {
        return "ProductData{productId=" + this.f22988a + "}";
    }
}
