package androidx.camera.core.impl;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.camera.core.p1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CameraValidator {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class CameraIdListIncorrectException extends Exception {
        public CameraIdListIncorrectException(String str, Throwable th) {
            super(str, th);
        }
    }

    public static void a(Context context, y.r rVar, androidx.camera.core.t tVar) {
        Integer d8;
        if (tVar != null) {
            try {
                d8 = tVar.d();
                if (d8 == null) {
                    p1.k("CameraValidator", "No lens facing info in the availableCamerasSelector, don't verify the camera lens facing.");
                    return;
                }
            } catch (IllegalStateException e8) {
                p1.d("CameraValidator", "Cannot get lens facing from the availableCamerasSelector don't verify the camera lens facing.", e8);
                return;
            }
        } else {
            d8 = null;
        }
        p1.a("CameraValidator", "Verifying camera lens facing on " + Build.DEVICE + ", lensFacingInteger: " + d8);
        PackageManager packageManager = context.getPackageManager();
        try {
            if (packageManager.hasSystemFeature("android.hardware.camera") && (tVar == null || d8.intValue() == 1)) {
                androidx.camera.core.t.f2811c.e(rVar.a());
            }
            if (packageManager.hasSystemFeature("android.hardware.camera.front")) {
                if (tVar == null || d8.intValue() == 0) {
                    androidx.camera.core.t.f2810b.e(rVar.a());
                }
            }
        } catch (IllegalArgumentException e9) {
            p1.c("CameraValidator", "Camera LensFacing verification failed, existing cameras: " + rVar.a());
            throw new CameraIdListIncorrectException("Expected camera missing from device.", e9);
        }
    }
}
