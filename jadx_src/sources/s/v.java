package s;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import androidx.core.util.h;
import java.util.List;
import java.util.concurrent.Executor;
import s.f;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v implements f.a {

    /* renamed from: a  reason: collision with root package name */
    final CameraCaptureSession f22757a;

    /* renamed from: b  reason: collision with root package name */
    final Object f22758b;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Handler f22759a;

        a(Handler handler) {
            this.f22759a = handler;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(CameraCaptureSession cameraCaptureSession, Object obj) {
        this.f22757a = (CameraCaptureSession) h.h(cameraCaptureSession);
        this.f22758b = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f.a d(CameraCaptureSession cameraCaptureSession, Handler handler) {
        return new v(cameraCaptureSession, new a(handler));
    }

    @Override // s.f.a
    public int a(CaptureRequest captureRequest, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) {
        return this.f22757a.setRepeatingRequest(captureRequest, new f.b(executor, captureCallback), ((a) this.f22758b).f22759a);
    }

    @Override // s.f.a
    public CameraCaptureSession b() {
        return this.f22757a;
    }

    @Override // s.f.a
    public int c(List<CaptureRequest> list, Executor executor, CameraCaptureSession.CaptureCallback captureCallback) {
        return this.f22757a.captureBurst(list, new f.b(executor, captureCallback), ((a) this.f22758b).f22759a);
    }
}
