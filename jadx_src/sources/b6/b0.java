package b6;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 {

    /* renamed from: c  reason: collision with root package name */
    public static final b0 f8019c = new b0(-1, -1);

    /* renamed from: d  reason: collision with root package name */
    public static final b0 f8020d = new b0(0, 0);

    /* renamed from: a  reason: collision with root package name */
    private final int f8021a;

    /* renamed from: b  reason: collision with root package name */
    private final int f8022b;

    public b0(int i8, int i9) {
        a.a((i8 == -1 || i8 >= 0) && (i9 == -1 || i9 >= 0));
        this.f8021a = i8;
        this.f8022b = i9;
    }

    public int a() {
        return this.f8022b;
    }

    public int b() {
        return this.f8021a;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof b0) {
            b0 b0Var = (b0) obj;
            return this.f8021a == b0Var.f8021a && this.f8022b == b0Var.f8022b;
        }
        return false;
    }

    public int hashCode() {
        int i8 = this.f8022b;
        int i9 = this.f8021a;
        return i8 ^ ((i9 >>> 16) | (i9 << 16));
    }

    public String toString() {
        return this.f8021a + "x" + this.f8022b;
    }
}
