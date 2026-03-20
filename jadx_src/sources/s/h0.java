package s;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.p1;
import androidx.core.util.h;
import java.util.ArrayList;
import java.util.List;
import s.f;
import s.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h0 implements z.a {

    /* renamed from: a  reason: collision with root package name */
    final CameraDevice f22744a;

    /* renamed from: b  reason: collision with root package name */
    final Object f22745b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Handler f22746a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Handler handler) {
            this.f22746a = handler;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h0(CameraDevice cameraDevice, Object obj) {
        this.f22744a = (CameraDevice) h.h(cameraDevice);
        this.f22745b = obj;
    }

    private static void b(CameraDevice cameraDevice, List<t.b> list) {
        String id = cameraDevice.getId();
        for (t.b bVar : list) {
            String c9 = bVar.c();
            if (c9 != null && !c9.isEmpty()) {
                p1.k("CameraDeviceCompat", "Camera " + id + ": Camera doesn't support physicalCameraId " + c9 + ". Ignoring.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(CameraDevice cameraDevice, t.h hVar) {
        h.h(cameraDevice);
        h.h(hVar);
        h.h(hVar.e());
        List<t.b> c9 = hVar.c();
        if (c9 == null) {
            throw new IllegalArgumentException("Invalid output configurations");
        }
        if (hVar.a() == null) {
            throw new IllegalArgumentException("Invalid executor");
        }
        b(cameraDevice, c9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static h0 d(CameraDevice cameraDevice, Handler handler) {
        return new h0(cameraDevice, new a(handler));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Surface> f(List<t.b> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (t.b bVar : list) {
            arrayList.add(bVar.d());
        }
        return arrayList;
    }

    @Override // s.z.a
    public void a(t.h hVar) {
        c(this.f22744a, hVar);
        if (hVar.b() != null) {
            throw new IllegalArgumentException("Reprocessing sessions not supported until API 23");
        }
        if (hVar.d() == 1) {
            throw new IllegalArgumentException("High speed capture sessions not supported until API 23");
        }
        f.c cVar = new f.c(hVar.a(), hVar.e());
        e(this.f22744a, f(hVar.c()), cVar, ((a) this.f22745b).f22746a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(CameraDevice cameraDevice, List<Surface> list, CameraCaptureSession.StateCallback stateCallback, Handler handler) {
        try {
            cameraDevice.createCaptureSession(list, stateCallback, handler);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
