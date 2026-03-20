package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n1 extends y.h {

    /* renamed from: a  reason: collision with root package name */
    private final CameraCaptureSession.CaptureCallback f1972a;

    private n1(CameraCaptureSession.CaptureCallback captureCallback) {
        Objects.requireNonNull(captureCallback, "captureCallback is null");
        this.f1972a = captureCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static n1 d(CameraCaptureSession.CaptureCallback captureCallback) {
        return new n1(captureCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraCaptureSession.CaptureCallback e() {
        return this.f1972a;
    }
}
