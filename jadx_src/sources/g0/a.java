package g0;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.utils.f;
import java.util.Objects;
import y.j;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a<T> extends e<T> {

    /* renamed from: a  reason: collision with root package name */
    private final T f20136a;

    /* renamed from: b  reason: collision with root package name */
    private final f f20137b;

    /* renamed from: c  reason: collision with root package name */
    private final int f20138c;

    /* renamed from: d  reason: collision with root package name */
    private final Size f20139d;

    /* renamed from: e  reason: collision with root package name */
    private final Rect f20140e;

    /* renamed from: f  reason: collision with root package name */
    private final int f20141f;

    /* renamed from: g  reason: collision with root package name */
    private final Matrix f20142g;

    /* renamed from: h  reason: collision with root package name */
    private final j f20143h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(T t8, f fVar, int i8, Size size, Rect rect, int i9, Matrix matrix, j jVar) {
        Objects.requireNonNull(t8, "Null data");
        this.f20136a = t8;
        this.f20137b = fVar;
        this.f20138c = i8;
        Objects.requireNonNull(size, "Null size");
        this.f20139d = size;
        Objects.requireNonNull(rect, "Null cropRect");
        this.f20140e = rect;
        this.f20141f = i9;
        Objects.requireNonNull(matrix, "Null sensorToBufferTransform");
        this.f20142g = matrix;
        Objects.requireNonNull(jVar, "Null cameraCaptureResult");
        this.f20143h = jVar;
    }

    @Override // g0.e
    public j a() {
        return this.f20143h;
    }

    @Override // g0.e
    public Rect b() {
        return this.f20140e;
    }

    @Override // g0.e
    public T c() {
        return this.f20136a;
    }

    @Override // g0.e
    public f d() {
        return this.f20137b;
    }

    @Override // g0.e
    public int e() {
        return this.f20138c;
    }

    public boolean equals(Object obj) {
        f fVar;
        if (obj == this) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            return this.f20136a.equals(eVar.c()) && ((fVar = this.f20137b) != null ? fVar.equals(eVar.d()) : eVar.d() == null) && this.f20138c == eVar.e() && this.f20139d.equals(eVar.h()) && this.f20140e.equals(eVar.b()) && this.f20141f == eVar.f() && this.f20142g.equals(eVar.g()) && this.f20143h.equals(eVar.a());
        }
        return false;
    }

    @Override // g0.e
    public int f() {
        return this.f20141f;
    }

    @Override // g0.e
    public Matrix g() {
        return this.f20142g;
    }

    @Override // g0.e
    public Size h() {
        return this.f20139d;
    }

    public int hashCode() {
        int hashCode = (this.f20136a.hashCode() ^ 1000003) * 1000003;
        f fVar = this.f20137b;
        return ((((((((((((hashCode ^ (fVar == null ? 0 : fVar.hashCode())) * 1000003) ^ this.f20138c) * 1000003) ^ this.f20139d.hashCode()) * 1000003) ^ this.f20140e.hashCode()) * 1000003) ^ this.f20141f) * 1000003) ^ this.f20142g.hashCode()) * 1000003) ^ this.f20143h.hashCode();
    }

    public String toString() {
        return "Packet{data=" + this.f20136a + ", exif=" + this.f20137b + ", format=" + this.f20138c + ", size=" + this.f20139d + ", cropRect=" + this.f20140e + ", rotationDegrees=" + this.f20141f + ", sensorToBufferTransform=" + this.f20142g + ", cameraCaptureResult=" + this.f20143h + "}";
    }
}
