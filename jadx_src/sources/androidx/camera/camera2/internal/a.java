package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Range;
import androidx.camera.camera2.internal.j3;
import androidx.camera.core.CameraControl;
import androidx.concurrent.futures.c;
import r.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements j3.b {

    /* renamed from: a  reason: collision with root package name */
    private final s.y f1708a;

    /* renamed from: b  reason: collision with root package name */
    private final Range<Float> f1709b;

    /* renamed from: d  reason: collision with root package name */
    private c.a<Void> f1711d;

    /* renamed from: c  reason: collision with root package name */
    private float f1710c = 1.0f;

    /* renamed from: e  reason: collision with root package name */
    private float f1712e = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(s.y yVar) {
        this.f1708a = yVar;
        this.f1709b = (Range) yVar.a(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE);
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void a(TotalCaptureResult totalCaptureResult) {
        if (this.f1711d != null) {
            CaptureRequest request = totalCaptureResult.getRequest();
            Float f5 = request == null ? null : (Float) request.get(CaptureRequest.CONTROL_ZOOM_RATIO);
            if (f5 == null) {
                return;
            }
            if (this.f1712e == f5.floatValue()) {
                this.f1711d.c(null);
                this.f1711d = null;
            }
        }
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void b(a.C0201a c0201a) {
        c0201a.e(CaptureRequest.CONTROL_ZOOM_RATIO, Float.valueOf(this.f1710c));
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void c(float f5, c.a<Void> aVar) {
        this.f1710c = f5;
        c.a<Void> aVar2 = this.f1711d;
        if (aVar2 != null) {
            aVar2.f(new CameraControl.OperationCanceledException("There is a new zoomRatio being set"));
        }
        this.f1712e = this.f1710c;
        this.f1711d = aVar;
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public float d() {
        return this.f1709b.getUpper().floatValue();
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public float e() {
        return this.f1709b.getLower().floatValue();
    }

    @Override // androidx.camera.camera2.internal.j3.b
    public void f() {
        this.f1710c = 1.0f;
        c.a<Void> aVar = this.f1711d;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
            this.f1711d = null;
        }
    }
}
