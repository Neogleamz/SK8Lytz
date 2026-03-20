package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.concurrent.futures.c;
import java.util.concurrent.Executor;
import r.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y1 {

    /* renamed from: a  reason: collision with root package name */
    private final t f2173a;

    /* renamed from: b  reason: collision with root package name */
    private final z1 f2174b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f2175c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f2176d = false;

    /* renamed from: e  reason: collision with root package name */
    private c.a<Integer> f2177e;

    /* renamed from: f  reason: collision with root package name */
    private t.c f2178f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y1(t tVar, s.y yVar, Executor executor) {
        this.f2173a = tVar;
        this.f2174b = new z1(yVar, 0);
        this.f2175c = executor;
    }

    private void a() {
        c.a<Integer> aVar = this.f2177e;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException("Cancelled by another setExposureCompensationIndex()"));
            this.f2177e = null;
        }
        t.c cVar = this.f2178f;
        if (cVar != null) {
            this.f2173a.Y(cVar);
            this.f2178f = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(boolean z4) {
        if (z4 == this.f2176d) {
            return;
        }
        this.f2176d = z4;
        if (z4) {
            return;
        }
        this.f2174b.b(0);
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(a.C0201a c0201a) {
        c0201a.e(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(this.f2174b.a()));
    }
}
