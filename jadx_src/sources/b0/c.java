package b0;

import android.graphics.Matrix;
import androidx.camera.core.i1;
import androidx.camera.core.impl.utils.ExifData;
import y.a1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements i1 {

    /* renamed from: a  reason: collision with root package name */
    private final y.j f7920a;

    public c(y.j jVar) {
        this.f7920a = jVar;
    }

    @Override // androidx.camera.core.i1
    public a1 a() {
        return this.f7920a.a();
    }

    @Override // androidx.camera.core.i1
    public int b() {
        return 0;
    }

    @Override // androidx.camera.core.i1
    public void c(ExifData.b bVar) {
        this.f7920a.c(bVar);
    }

    @Override // androidx.camera.core.i1
    public long d() {
        return this.f7920a.d();
    }

    @Override // androidx.camera.core.i1
    public Matrix e() {
        return new Matrix();
    }

    public y.j f() {
        return this.f7920a;
    }
}
