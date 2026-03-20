package y;

import androidx.camera.core.impl.CameraCaptureFailure;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends h {

    /* renamed from: a  reason: collision with root package name */
    private final List<h> f24299a;

    @Override // y.h
    public void a() {
        for (h hVar : this.f24299a) {
            hVar.a();
        }
    }

    @Override // y.h
    public void b(j jVar) {
        for (h hVar : this.f24299a) {
            hVar.b(jVar);
        }
    }

    @Override // y.h
    public void c(CameraCaptureFailure cameraCaptureFailure) {
        for (h hVar : this.f24299a) {
            hVar.c(cameraCaptureFailure);
        }
    }

    public List<h> d() {
        return this.f24299a;
    }
}
