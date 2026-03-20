package n4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 {

    /* renamed from: c  reason: collision with root package name */
    public static final a0 f22045c = new a0(0, 0);

    /* renamed from: a  reason: collision with root package name */
    public final long f22046a;

    /* renamed from: b  reason: collision with root package name */
    public final long f22047b;

    public a0(long j8, long j9) {
        this.f22046a = j8;
        this.f22047b = j9;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || a0.class != obj.getClass()) {
            return false;
        }
        a0 a0Var = (a0) obj;
        return this.f22046a == a0Var.f22046a && this.f22047b == a0Var.f22047b;
    }

    public int hashCode() {
        return (((int) this.f22046a) * 31) + ((int) this.f22047b);
    }

    public String toString() {
        return "[timeUs=" + this.f22046a + ", position=" + this.f22047b + "]";
    }
}
