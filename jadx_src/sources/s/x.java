package s;

import android.hardware.camera2.CameraCharacteristics;
import s.y;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class x implements y.a {

    /* renamed from: a  reason: collision with root package name */
    protected final CameraCharacteristics f22760a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(CameraCharacteristics cameraCharacteristics) {
        this.f22760a = cameraCharacteristics;
    }

    @Override // s.y.a
    public <T> T a(CameraCharacteristics.Key<T> key) {
        return (T) this.f22760a.get(key);
    }
}
