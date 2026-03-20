package x;

import androidx.camera.core.l1;
import java.util.Objects;
import x.m;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends m.a {

    /* renamed from: a  reason: collision with root package name */
    private final g0.e<l1> f23707a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23708b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(g0.e<l1> eVar, int i8) {
        Objects.requireNonNull(eVar, "Null packet");
        this.f23707a = eVar;
        this.f23708b = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x.m.a
    public int a() {
        return this.f23708b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x.m.a
    public g0.e<l1> b() {
        return this.f23707a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof m.a) {
            m.a aVar = (m.a) obj;
            return this.f23707a.equals(aVar.b()) && this.f23708b == aVar.a();
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23707a.hashCode() ^ 1000003) * 1000003) ^ this.f23708b;
    }

    public String toString() {
        return "In{packet=" + this.f23707a + ", jpegQuality=" + this.f23708b + "}";
    }
}
