package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.view.Surface;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import w.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b1 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static CaptureRequest.Builder a(CameraDevice cameraDevice, TotalCaptureResult totalCaptureResult) {
            return cameraDevice.createReprocessCaptureRequest(totalCaptureResult);
        }
    }

    private static void a(CaptureRequest.Builder builder, Config config) {
        w.j d8 = j.a.e(config).d();
        for (Config.a<?> aVar : d8.e()) {
            CaptureRequest.Key key = (CaptureRequest.Key) aVar.d();
            try {
                builder.set(key, d8.a(aVar));
            } catch (IllegalArgumentException unused) {
                androidx.camera.core.p1.c("Camera2CaptureRequestBuilder", "CaptureRequest.Key is not supported: " + key);
            }
        }
    }

    public static CaptureRequest b(androidx.camera.core.impl.f fVar, CameraDevice cameraDevice, Map<DeferrableSurface, Surface> map) {
        CaptureRequest.Builder createCaptureRequest;
        if (cameraDevice == null) {
            return null;
        }
        List<Surface> d8 = d(fVar.e(), map);
        if (d8.isEmpty()) {
            return null;
        }
        y.j c9 = fVar.c();
        if (Build.VERSION.SDK_INT < 23 || fVar.g() != 5 || c9 == null || !(c9.g() instanceof TotalCaptureResult)) {
            androidx.camera.core.p1.a("Camera2CaptureRequestBuilder", "createCaptureRequest");
            createCaptureRequest = cameraDevice.createCaptureRequest(fVar.g());
        } else {
            androidx.camera.core.p1.a("Camera2CaptureRequestBuilder", "createReprocessCaptureRequest");
            createCaptureRequest = a.a(cameraDevice, (TotalCaptureResult) c9.g());
        }
        a(createCaptureRequest, fVar.d());
        Config d9 = fVar.d();
        Config.a<Integer> aVar = androidx.camera.core.impl.f.f2555h;
        if (d9.b(aVar)) {
            createCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, (Integer) fVar.d().a(aVar));
        }
        Config d10 = fVar.d();
        Config.a<Integer> aVar2 = androidx.camera.core.impl.f.f2556i;
        if (d10.b(aVar2)) {
            createCaptureRequest.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf(((Integer) fVar.d().a(aVar2)).byteValue()));
        }
        for (Surface surface : d8) {
            createCaptureRequest.addTarget(surface);
        }
        createCaptureRequest.setTag(fVar.f());
        return createCaptureRequest.build();
    }

    public static CaptureRequest c(androidx.camera.core.impl.f fVar, CameraDevice cameraDevice) {
        if (cameraDevice == null) {
            return null;
        }
        CaptureRequest.Builder createCaptureRequest = cameraDevice.createCaptureRequest(fVar.g());
        a(createCaptureRequest, fVar.d());
        return createCaptureRequest.build();
    }

    private static List<Surface> d(List<DeferrableSurface> list, Map<DeferrableSurface, Surface> map) {
        ArrayList arrayList = new ArrayList();
        for (DeferrableSurface deferrableSurface : list) {
            Surface surface = map.get(deferrableSurface);
            if (surface == null) {
                throw new IllegalArgumentException("DeferrableSurface not in configuredSurfaceMap");
            }
            arrayList.add(surface);
        }
        return arrayList;
    }
}
