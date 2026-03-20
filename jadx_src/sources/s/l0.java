package s;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.util.ArrayMap;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.impl.utils.k;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l0 {

    /* renamed from: a  reason: collision with root package name */
    private final b f22747a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, y> f22748b = new ArrayMap(4);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a extends CameraManager.AvailabilityCallback {

        /* renamed from: a  reason: collision with root package name */
        private final Executor f22749a;

        /* renamed from: b  reason: collision with root package name */
        final CameraManager.AvailabilityCallback f22750b;

        /* renamed from: c  reason: collision with root package name */
        private final Object f22751c = new Object();

        /* renamed from: d  reason: collision with root package name */
        private boolean f22752d = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Executor executor, CameraManager.AvailabilityCallback availabilityCallback) {
            this.f22749a = executor;
            this.f22750b = availabilityCallback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void d() {
            e.a(this.f22750b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(String str) {
            this.f22750b.onCameraAvailable(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(String str) {
            this.f22750b.onCameraUnavailable(str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void g() {
            synchronized (this.f22751c) {
                this.f22752d = true;
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAccessPrioritiesChanged() {
            synchronized (this.f22751c) {
                if (!this.f22752d) {
                    this.f22749a.execute(new i0(this));
                }
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAvailable(String str) {
            synchronized (this.f22751c) {
                if (!this.f22752d) {
                    this.f22749a.execute(new j0(this, str));
                }
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraUnavailable(String str) {
            synchronized (this.f22751c) {
                if (!this.f22752d) {
                    this.f22749a.execute(new k0(this, str));
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        static b d(Context context, Handler handler) {
            int i8 = Build.VERSION.SDK_INT;
            return i8 >= 29 ? new n0(context) : i8 >= 28 ? m0.h(context) : o0.g(context, handler);
        }

        void a(Executor executor, CameraManager.AvailabilityCallback availabilityCallback);

        void b(CameraManager.AvailabilityCallback availabilityCallback);

        CameraCharacteristics c(String str);

        void e(String str, Executor executor, CameraDevice.StateCallback stateCallback);

        String[] f();
    }

    private l0(b bVar) {
        this.f22747a = bVar;
    }

    public static l0 a(Context context) {
        return b(context, k.a());
    }

    public static l0 b(Context context, Handler handler) {
        return new l0(b.d(context, handler));
    }

    public y c(String str) {
        y yVar;
        synchronized (this.f22748b) {
            yVar = this.f22748b.get(str);
            if (yVar == null) {
                try {
                    yVar = y.c(this.f22747a.c(str));
                    this.f22748b.put(str, yVar);
                } catch (AssertionError e8) {
                    throw new CameraAccessExceptionCompat(10002, e8.getMessage(), e8);
                }
            }
        }
        return yVar;
    }

    public String[] d() {
        return this.f22747a.f();
    }

    public void e(String str, Executor executor, CameraDevice.StateCallback stateCallback) {
        this.f22747a.e(str, executor, stateCallback);
    }

    public void f(Executor executor, CameraManager.AvailabilityCallback availabilityCallback) {
        this.f22747a.a(executor, availabilityCallback);
    }

    public void g(CameraManager.AvailabilityCallback availabilityCallback) {
        this.f22747a.b(availabilityCallback);
    }
}
