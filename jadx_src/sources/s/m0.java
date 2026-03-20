package s;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m0 extends o0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public m0(Context context) {
        super(context, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static m0 h(Context context) {
        return new m0(context);
    }

    private boolean i(Throwable th) {
        return Build.VERSION.SDK_INT == 28 && j(th);
    }

    private static boolean j(Throwable th) {
        StackTraceElement[] stackTrace;
        if (!th.getClass().equals(RuntimeException.class) || (stackTrace = th.getStackTrace()) == null || stackTrace.length < 0) {
            return false;
        }
        return "_enableShutterSound".equals(stackTrace[0].getMethodName());
    }

    private void k(Throwable th) {
        throw new CameraAccessExceptionCompat(10001, th);
    }

    @Override // s.o0, s.l0.b
    public void a(Executor executor, CameraManager.AvailabilityCallback availabilityCallback) {
        this.f22753a.registerAvailabilityCallback(executor, availabilityCallback);
    }

    @Override // s.o0, s.l0.b
    public void b(CameraManager.AvailabilityCallback availabilityCallback) {
        this.f22753a.unregisterAvailabilityCallback(availabilityCallback);
    }

    @Override // s.o0, s.l0.b
    public CameraCharacteristics c(String str) {
        try {
            return super.c(str);
        } catch (RuntimeException e8) {
            if (i(e8)) {
                k(e8);
            }
            throw e8;
        }
    }

    @Override // s.o0, s.l0.b
    public void e(String str, Executor executor, CameraDevice.StateCallback stateCallback) {
        try {
            this.f22753a.openCamera(str, executor, stateCallback);
        } catch (CameraAccessException e8) {
            throw CameraAccessExceptionCompat.e(e8);
        } catch (IllegalArgumentException e9) {
            throw e9;
        } catch (SecurityException e10) {
        } catch (RuntimeException e11) {
            if (i(e11)) {
                k(e11);
            }
            throw e11;
        }
    }
}
