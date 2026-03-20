package x;

import androidx.camera.core.e1;
import java.util.Objects;
import x.q;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends q.a {

    /* renamed from: a  reason: collision with root package name */
    private final g0.e<byte[]> f23709a;

    /* renamed from: b  reason: collision with root package name */
    private final e1.m f23710b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(g0.e<byte[]> eVar, e1.m mVar) {
        Objects.requireNonNull(eVar, "Null packet");
        this.f23709a = eVar;
        Objects.requireNonNull(mVar, "Null outputFileOptions");
        this.f23710b = mVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x.q.a
    public e1.m a() {
        return this.f23710b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x.q.a
    public g0.e<byte[]> b() {
        return this.f23709a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof q.a) {
            q.a aVar = (q.a) obj;
            return this.f23709a.equals(aVar.b()) && this.f23710b.equals(aVar.a());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23709a.hashCode() ^ 1000003) * 1000003) ^ this.f23710b.hashCode();
    }

    public String toString() {
        return "In{packet=" + this.f23709a + ", outputFileOptions=" + this.f23710b + "}";
    }
}
