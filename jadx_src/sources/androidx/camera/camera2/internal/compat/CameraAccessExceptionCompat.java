package androidx.camera.camera2.internal.compat;

import android.hardware.camera2.CameraAccessException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CameraAccessExceptionCompat extends Exception {

    /* renamed from: c  reason: collision with root package name */
    static final Set<Integer> f1764c = Collections.unmodifiableSet(new HashSet(Arrays.asList(4, 5, 1, 2, 3)));

    /* renamed from: d  reason: collision with root package name */
    static final Set<Integer> f1765d = Collections.unmodifiableSet(new HashSet(Arrays.asList(10001, 10002)));

    /* renamed from: a  reason: collision with root package name */
    private final int f1766a;

    /* renamed from: b  reason: collision with root package name */
    private final CameraAccessException f1767b;

    public CameraAccessExceptionCompat(int i8, String str, Throwable th) {
        super(a(i8, str), th);
        this.f1766a = i8;
        this.f1767b = f1764c.contains(Integer.valueOf(i8)) ? new CameraAccessException(i8, str, th) : null;
    }

    public CameraAccessExceptionCompat(int i8, Throwable th) {
        super(b(i8), th);
        this.f1766a = i8;
        this.f1767b = f1764c.contains(Integer.valueOf(i8)) ? new CameraAccessException(i8, null, th) : null;
    }

    private CameraAccessExceptionCompat(CameraAccessException cameraAccessException) {
        super(cameraAccessException.getMessage(), cameraAccessException.getCause());
        this.f1766a = cameraAccessException.getReason();
        this.f1767b = cameraAccessException;
    }

    private static String a(int i8, String str) {
        return String.format("%s (%d): %s", c(i8), Integer.valueOf(i8), str);
    }

    private static String b(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 != 3) {
                    if (i8 != 4) {
                        if (i8 != 5) {
                            if (i8 != 10001) {
                                if (i8 != 10002) {
                                    return null;
                                }
                                return "Failed to create CameraCharacteristics.";
                            }
                            return "Some API 28 devices cannot access the camera when the device is in \"Do Not Disturb\" mode. The camera will not be accessible until \"Do Not Disturb\" mode is disabled.";
                        }
                        return "The system-wide limit for number of open cameras has been reached, and more camera devices cannot be opened until previous instances are closed.";
                    }
                    return "The camera device is in use already";
                }
                return "The camera device is currently in the error state; no further calls to it will succeed.";
            }
            return "The camera device is removable and has been disconnected from the Android device, or the camera service has shut down the connection due to a higher-priority access request for the camera device.";
        }
        return "The camera is disabled due to a device policy, and cannot be opened.";
    }

    private static String c(int i8) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? i8 != 4 ? i8 != 5 ? i8 != 1000 ? i8 != 10001 ? i8 != 10002 ? "<UNKNOWN ERROR>" : "CAMERA_CHARACTERISTICS_CREATION_ERROR" : "CAMERA_UNAVAILABLE_DO_NOT_DISTURB" : "CAMERA_DEPRECATED_HAL" : "MAX_CAMERAS_IN_USE" : "CAMERA_IN_USE" : "CAMERA_ERROR" : "CAMERA_DISCONNECTED" : "CAMERA_DISABLED";
    }

    public static CameraAccessExceptionCompat e(CameraAccessException cameraAccessException) {
        Objects.requireNonNull(cameraAccessException, "cameraAccessException should not be null");
        return new CameraAccessExceptionCompat(cameraAccessException);
    }

    public final int d() {
        return this.f1766a;
    }
}
