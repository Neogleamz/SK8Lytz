package androidx.camera.lifecycle;

import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.lifecycle.LifecycleCameraRepository;
import androidx.lifecycle.j;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends LifecycleCameraRepository.a {

    /* renamed from: a  reason: collision with root package name */
    private final j f2933a;

    /* renamed from: b  reason: collision with root package name */
    private final CameraUseCaseAdapter.a f2934b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(j jVar, CameraUseCaseAdapter.a aVar) {
        Objects.requireNonNull(jVar, "Null lifecycleOwner");
        this.f2933a = jVar;
        Objects.requireNonNull(aVar, "Null cameraId");
        this.f2934b = aVar;
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraRepository.a
    public CameraUseCaseAdapter.a b() {
        return this.f2934b;
    }

    @Override // androidx.camera.lifecycle.LifecycleCameraRepository.a
    public j c() {
        return this.f2933a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof LifecycleCameraRepository.a) {
            LifecycleCameraRepository.a aVar = (LifecycleCameraRepository.a) obj;
            return this.f2933a.equals(aVar.c()) && this.f2934b.equals(aVar.b());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f2933a.hashCode() ^ 1000003) * 1000003) ^ this.f2934b.hashCode();
    }

    public String toString() {
        return "Key{lifecycleOwner=" + this.f2933a + ", cameraId=" + this.f2934b + "}";
    }
}
