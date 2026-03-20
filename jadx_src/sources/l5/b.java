package l5;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    public final String f21630a;

    /* renamed from: b  reason: collision with root package name */
    public final String f21631b;

    /* renamed from: c  reason: collision with root package name */
    public final int f21632c;

    /* renamed from: d  reason: collision with root package name */
    public final int f21633d;

    public b(String str, String str2, int i8, int i9) {
        this.f21630a = str;
        this.f21631b = str2;
        this.f21632c = i8;
        this.f21633d = i9;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof b) {
            b bVar = (b) obj;
            return this.f21632c == bVar.f21632c && this.f21633d == bVar.f21633d && com.google.common.base.k.a(this.f21630a, bVar.f21630a) && com.google.common.base.k.a(this.f21631b, bVar.f21631b);
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.k.b(this.f21630a, this.f21631b, Integer.valueOf(this.f21632c), Integer.valueOf(this.f21633d));
    }
}
