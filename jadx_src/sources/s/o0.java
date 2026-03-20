package s;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.core.util.h;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import s.l0;
import s.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o0 implements l0.b {

    /* renamed from: a  reason: collision with root package name */
    final CameraManager f22753a;

    /* renamed from: b  reason: collision with root package name */
    final Object f22754b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final Map<CameraManager.AvailabilityCallback, l0.a> f22755a = new HashMap();

        /* renamed from: b  reason: collision with root package name */
        final Handler f22756b;

        a(Handler handler) {
            this.f22756b = handler;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o0(Context context, Object obj) {
        this.f22753a = (CameraManager) context.getSystemService("camera");
        this.f22754b = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static o0 g(Context context, Handler handler) {
        return new o0(context, new a(handler));
    }

    @Override // s.l0.b
    public void a(Executor executor, CameraManager.AvailabilityCallback availabilityCallback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor was null");
        }
        l0.a aVar = null;
        a aVar2 = (a) this.f22754b;
        if (availabilityCallback != null) {
            synchronized (aVar2.f22755a) {
                aVar = aVar2.f22755a.get(availabilityCallback);
                if (aVar == null) {
                    aVar = new l0.a(executor, availabilityCallback);
                    aVar2.f22755a.put(availabilityCallback, aVar);
                }
            }
        }
        this.f22753a.registerAvailabilityCallback(aVar, aVar2.f22756b);
    }

    @Override // s.l0.b
    public void b(CameraManager.AvailabilityCallback availabilityCallback) {
        l0.a aVar;
        if (availabilityCallback != null) {
            a aVar2 = (a) this.f22754b;
            synchronized (aVar2.f22755a) {
                aVar = aVar2.f22755a.remove(availabilityCallback);
            }
        } else {
            aVar = null;
        }
        if (aVar != null) {
            aVar.g();
        }
        this.f22753a.unregisterAvailabilityCallback(aVar);
    }

    @Override // s.l0.b
    public CameraCharacteristics c(String str) {
        try {
            return this.f22753a.getCameraCharacteristics(str);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }

    @Override // s.l0.b
    public void e(String str, Executor executor, CameraDevice.StateCallback stateCallback) {
        h.h(executor);
        h.h(stateCallback);
        try {
            this.f22753a.openCamera(str, new z.b(executor, stateCallback), ((a) this.f22754b).f22756b);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }

    @Override // s.l0.b
    public String[] f() {
        try {
            return this.f22753a.getCameraIdList();
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        }
    }
}
