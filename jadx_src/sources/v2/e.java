package v2;

import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private final String f23168a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23169b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f23170c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f23171d;

    public e(String str, int i8, boolean z4, boolean z8) {
        p.e(str, "name");
        this.f23168a = str;
        this.f23169b = i8;
        this.f23170c = z4;
        this.f23171d = z8;
    }

    public final String a() {
        return this.f23168a;
    }

    public final int b() {
        return this.f23169b;
    }

    public final boolean c() {
        return this.f23170c;
    }

    public final boolean d() {
        return this.f23171d;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            return p.a(this.f23168a, eVar.f23168a) && this.f23169b == eVar.f23169b && this.f23170c == eVar.f23170c && this.f23171d == eVar.f23171d;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        int hashCode = ((this.f23168a.hashCode() * 31) + Integer.hashCode(this.f23169b)) * 31;
        boolean z4 = this.f23170c;
        int i8 = z4;
        if (z4 != 0) {
            i8 = 1;
        }
        int i9 = (hashCode + i8) * 31;
        boolean z8 = this.f23171d;
        return i9 + (z8 ? 1 : z8 ? 1 : 0);
    }

    public String toString() {
        return "TypefaceKey(name=" + this.f23168a + ", weight=" + this.f23169b + ", isBold=" + this.f23170c + ", isItalic=" + this.f23171d + ')';
    }
}
