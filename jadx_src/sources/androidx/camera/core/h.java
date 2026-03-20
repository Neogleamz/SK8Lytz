package androidx.camera.core;

import android.graphics.Matrix;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h extends o1 {

    /* renamed from: a  reason: collision with root package name */
    private final y.a1 f2400a;

    /* renamed from: b  reason: collision with root package name */
    private final long f2401b;

    /* renamed from: c  reason: collision with root package name */
    private final int f2402c;

    /* renamed from: d  reason: collision with root package name */
    private final Matrix f2403d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(y.a1 a1Var, long j8, int i8, Matrix matrix) {
        Objects.requireNonNull(a1Var, "Null tagBundle");
        this.f2400a = a1Var;
        this.f2401b = j8;
        this.f2402c = i8;
        Objects.requireNonNull(matrix, "Null sensorToBufferTransformMatrix");
        this.f2403d = matrix;
    }

    @Override // androidx.camera.core.o1, androidx.camera.core.i1
    public y.a1 a() {
        return this.f2400a;
    }

    @Override // androidx.camera.core.o1, androidx.camera.core.i1
    public int b() {
        return this.f2402c;
    }

    @Override // androidx.camera.core.o1, androidx.camera.core.i1
    public long d() {
        return this.f2401b;
    }

    @Override // androidx.camera.core.o1, androidx.camera.core.i1
    public Matrix e() {
        return this.f2403d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof o1) {
            o1 o1Var = (o1) obj;
            return this.f2400a.equals(o1Var.a()) && this.f2401b == o1Var.d() && this.f2402c == o1Var.b() && this.f2403d.equals(o1Var.e());
        }
        return false;
    }

    public int hashCode() {
        long j8 = this.f2401b;
        return ((((((this.f2400a.hashCode() ^ 1000003) * 1000003) ^ ((int) (j8 ^ (j8 >>> 32)))) * 1000003) ^ this.f2402c) * 1000003) ^ this.f2403d.hashCode();
    }

    public String toString() {
        return "ImmutableImageInfo{tagBundle=" + this.f2400a + ", timestamp=" + this.f2401b + ", rotationDegrees=" + this.f2402c + ", sensorToBufferTransformMatrix=" + this.f2403d + "}";
    }
}
