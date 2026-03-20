package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static CameraCaptureSession.CaptureCallback a(y.h hVar) {
        if (hVar == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        b(hVar, arrayList);
        return arrayList.size() == 1 ? (CameraCaptureSession.CaptureCallback) arrayList.get(0) : k0.a(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(y.h hVar, List<CameraCaptureSession.CaptureCallback> list) {
        if (hVar instanceof y.i) {
            for (y.h hVar2 : ((y.i) hVar).d()) {
                b(hVar2, list);
            }
        } else if (hVar instanceof n1) {
            list.add(((n1) hVar).e());
        } else {
            list.add(new m1(hVar));
        }
    }
}
