package x3;

import android.content.Context;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends f {

    /* renamed from: a  reason: collision with root package name */
    private final Context f23776a;

    /* renamed from: b  reason: collision with root package name */
    private final g4.a f23777b;

    /* renamed from: c  reason: collision with root package name */
    private final g4.a f23778c;

    /* renamed from: d  reason: collision with root package name */
    private final String f23779d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context, g4.a aVar, g4.a aVar2, String str) {
        Objects.requireNonNull(context, "Null applicationContext");
        this.f23776a = context;
        Objects.requireNonNull(aVar, "Null wallClock");
        this.f23777b = aVar;
        Objects.requireNonNull(aVar2, "Null monotonicClock");
        this.f23778c = aVar2;
        Objects.requireNonNull(str, "Null backendName");
        this.f23779d = str;
    }

    @Override // x3.f
    public Context b() {
        return this.f23776a;
    }

    @Override // x3.f
    public String c() {
        return this.f23779d;
    }

    @Override // x3.f
    public g4.a d() {
        return this.f23778c;
    }

    @Override // x3.f
    public g4.a e() {
        return this.f23777b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof f) {
            f fVar = (f) obj;
            return this.f23776a.equals(fVar.b()) && this.f23777b.equals(fVar.e()) && this.f23778c.equals(fVar.d()) && this.f23779d.equals(fVar.c());
        }
        return false;
    }

    public int hashCode() {
        return ((((((this.f23776a.hashCode() ^ 1000003) * 1000003) ^ this.f23777b.hashCode()) * 1000003) ^ this.f23778c.hashCode()) * 1000003) ^ this.f23779d.hashCode();
    }

    public String toString() {
        return "CreationContext{applicationContext=" + this.f23776a + ", wallClock=" + this.f23777b + ", monotonicClock=" + this.f23778c + ", backendName=" + this.f23779d + "}";
    }
}
