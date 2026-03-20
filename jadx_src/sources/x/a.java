package x;

import android.graphics.Bitmap;
import java.util.Objects;
import x.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends h.a {

    /* renamed from: a  reason: collision with root package name */
    private final g0.e<Bitmap> f23686a;

    /* renamed from: b  reason: collision with root package name */
    private final int f23687b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(g0.e<Bitmap> eVar, int i8) {
        Objects.requireNonNull(eVar, "Null packet");
        this.f23686a = eVar;
        this.f23687b = i8;
    }

    @Override // x.h.a
    int a() {
        return this.f23687b;
    }

    @Override // x.h.a
    g0.e<Bitmap> b() {
        return this.f23686a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof h.a) {
            h.a aVar = (h.a) obj;
            return this.f23686a.equals(aVar.b()) && this.f23687b == aVar.a();
        }
        return false;
    }

    public int hashCode() {
        return ((this.f23686a.hashCode() ^ 1000003) * 1000003) ^ this.f23687b;
    }

    public String toString() {
        return "In{packet=" + this.f23686a + ", jpegQuality=" + this.f23687b + "}";
    }
}
