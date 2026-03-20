package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import androidx.camera.core.impl.CameraCaptureFailure;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m1 extends CameraCaptureSession.CaptureCallback {

    /* renamed from: a  reason: collision with root package name */
    private final y.h f1950a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m1(y.h hVar) {
        Objects.requireNonNull(hVar, "cameraCaptureCallback is null");
        this.f1950a = hVar;
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
        y.a1 a9;
        super.onCaptureCompleted(cameraCaptureSession, captureRequest, totalCaptureResult);
        Object tag = captureRequest.getTag();
        if (tag != null) {
            androidx.core.util.h.b(tag instanceof y.a1, "The tagBundle object from the CaptureResult is not a TagBundle object.");
            a9 = (y.a1) tag;
        } else {
            a9 = y.a1.a();
        }
        this.f1950a.b(new e(a9, totalCaptureResult));
    }

    @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
    public void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
        super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
        this.f1950a.c(new CameraCaptureFailure(CameraCaptureFailure.Reason.ERROR));
    }
}
