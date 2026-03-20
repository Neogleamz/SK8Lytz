package y;

import android.util.Range;
import android.util.Size;
import androidx.camera.core.impl.SurfaceConfig;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends a {

    /* renamed from: a  reason: collision with root package name */
    private final SurfaceConfig f24283a;

    /* renamed from: b  reason: collision with root package name */
    private final int f24284b;

    /* renamed from: c  reason: collision with root package name */
    private final Size f24285c;

    /* renamed from: d  reason: collision with root package name */
    private final Range<Integer> f24286d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(SurfaceConfig surfaceConfig, int i8, Size size, Range<Integer> range) {
        Objects.requireNonNull(surfaceConfig, "Null surfaceConfig");
        this.f24283a = surfaceConfig;
        this.f24284b = i8;
        Objects.requireNonNull(size, "Null size");
        this.f24285c = size;
        this.f24286d = range;
    }

    @Override // y.a
    public int b() {
        return this.f24284b;
    }

    @Override // y.a
    public Size c() {
        return this.f24285c;
    }

    @Override // y.a
    public SurfaceConfig d() {
        return this.f24283a;
    }

    @Override // y.a
    public Range<Integer> e() {
        return this.f24286d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof a) {
            a aVar = (a) obj;
            if (this.f24283a.equals(aVar.d()) && this.f24284b == aVar.b() && this.f24285c.equals(aVar.c())) {
                Range<Integer> range = this.f24286d;
                Range<Integer> e8 = aVar.e();
                if (range == null) {
                    if (e8 == null) {
                        return true;
                    }
                } else if (range.equals(e8)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (((((this.f24283a.hashCode() ^ 1000003) * 1000003) ^ this.f24284b) * 1000003) ^ this.f24285c.hashCode()) * 1000003;
        Range<Integer> range = this.f24286d;
        return hashCode ^ (range == null ? 0 : range.hashCode());
    }

    public String toString() {
        return "AttachedSurfaceInfo{surfaceConfig=" + this.f24283a + ", imageFormat=" + this.f24284b + ", size=" + this.f24285c + ", targetFrameRate=" + this.f24286d + "}";
    }
}
