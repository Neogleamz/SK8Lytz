package androidx.camera.core;

import androidx.camera.core.CameraState;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends CameraState {

    /* renamed from: a  reason: collision with root package name */
    private final CameraState.Type f2344a;

    /* renamed from: b  reason: collision with root package name */
    private final CameraState.a f2345b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(CameraState.Type type, CameraState.a aVar) {
        Objects.requireNonNull(type, "Null type");
        this.f2344a = type;
        this.f2345b = aVar;
    }

    @Override // androidx.camera.core.CameraState
    public CameraState.a c() {
        return this.f2345b;
    }

    @Override // androidx.camera.core.CameraState
    public CameraState.Type d() {
        return this.f2344a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CameraState) {
            CameraState cameraState = (CameraState) obj;
            if (this.f2344a.equals(cameraState.d())) {
                CameraState.a aVar = this.f2345b;
                CameraState.a c9 = cameraState.c();
                if (aVar == null) {
                    if (c9 == null) {
                        return true;
                    }
                } else if (aVar.equals(c9)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (this.f2344a.hashCode() ^ 1000003) * 1000003;
        CameraState.a aVar = this.f2345b;
        return hashCode ^ (aVar == null ? 0 : aVar.hashCode());
    }

    public String toString() {
        return "CameraState{type=" + this.f2344a + ", error=" + this.f2345b + "}";
    }
}
