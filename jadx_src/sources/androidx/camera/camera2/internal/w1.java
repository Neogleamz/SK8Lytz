package androidx.camera.camera2.internal;

import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import androidx.camera.camera2.internal.j3;
import androidx.camera.core.CameraControl;
import androidx.concurrent.futures.c;
import r.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w1 implements j3.b {

    /* renamed from: a  reason: collision with root package name */
    private final s.y f2153a;

    /* renamed from: c  reason: collision with root package name */
    private c.a<Void> f2155c;

    /* renamed from: b  reason: collision with root package name */
    private Rect f2154b = null;

    /* renamed from: d  reason: collision with root package name */
    private Rect f2156d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w1(s.y yVar) {
        this.f2153a = yVar;
    }

    private static Rect g(Rect rect, float f5) {
        float width = rect.width() / f5;
        float height = rect.height() / f5;
        float width2 = (rect.width() - width) / 2.0f;
        float height2 = (rect.height() - height) / 2.0f;
        return new Rect((int) width2, (int) height2, (int) (width2 + width), (int) (height2 + height));
    }

    private Rect h() {
        return (Rect) androidx.core.util.h.h((Rect) this.f2153a.a(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void a(TotalCaptureResult totalCaptureResult) {
        if (this.f2155c != null) {
            CaptureRequest request = totalCaptureResult.getRequest();
            Rect rect = request == null ? null : (Rect) request.get(CaptureRequest.SCALER_CROP_REGION);
            Rect rect2 = this.f2156d;
            if (rect2 == null || !rect2.equals(rect)) {
                return;
            }
            this.f2155c.c(null);
            this.f2155c = null;
            this.f2156d = null;
        }
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void b(a.C0201a c0201a) {
        Rect rect = this.f2154b;
        if (rect != null) {
            c0201a.e(CaptureRequest.SCALER_CROP_REGION, rect);
        }
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void c(float f5, c.a<Void> aVar) {
        this.f2154b = g(h(), f5);
        c.a<Void> aVar2 = this.f2155c;
        if (aVar2 != null) {
            aVar2.f(new CameraControl.OperationCanceledException("There is a new zoomRatio being set"));
        }
        this.f2156d = this.f2154b;
        this.f2155c = aVar;
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public float d() {
        Float f5 = (Float) this.f2153a.a(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        if (f5 == null) {
            return 1.0f;
        }
        return f5.floatValue() < e() ? e() : f5.floatValue();
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public float e() {
        return 1.0f;
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void f() {
        this.f2156d = null;
        this.f2154b = null;
        c.a<Void> aVar = this.f2155c;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
            this.f2155c = null;
        }
    }
}
