package androidx.camera.camera2.internal;

import android.graphics.Rect;
import android.hardware.camera2.CaptureResult;
import android.os.Build;
import androidx.camera.core.impl.CameraCaptureMetaData$AeState;
import androidx.camera.core.impl.CameraCaptureMetaData$AfMode;
import androidx.camera.core.impl.CameraCaptureMetaData$AfState;
import androidx.camera.core.impl.CameraCaptureMetaData$AwbState;
import androidx.camera.core.impl.CameraCaptureMetaData$FlashState;
import androidx.camera.core.impl.utils.ExifData;
import java.nio.BufferUnderflowException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e implements y.j {

    /* renamed from: a  reason: collision with root package name */
    private final y.a1 f1782a;

    /* renamed from: b  reason: collision with root package name */
    private final CaptureResult f1783b;

    public e(CaptureResult captureResult) {
        this(y.a1.a(), captureResult);
    }

    public e(y.a1 a1Var, CaptureResult captureResult) {
        this.f1782a = a1Var;
        this.f1783b = captureResult;
    }

    @Override // y.j
    public y.a1 a() {
        return this.f1782a;
    }

    @Override // y.j
    public CameraCaptureMetaData$AwbState b() {
        Integer num = (Integer) this.f1783b.get(CaptureResult.CONTROL_AWB_STATE);
        if (num == null) {
            return CameraCaptureMetaData$AwbState.UNKNOWN;
        }
        int intValue = num.intValue();
        if (intValue != 0) {
            if (intValue != 1) {
                if (intValue != 2) {
                    if (intValue != 3) {
                        androidx.camera.core.p1.c("C2CameraCaptureResult", "Undefined awb state: " + num);
                        return CameraCaptureMetaData$AwbState.UNKNOWN;
                    }
                    return CameraCaptureMetaData$AwbState.LOCKED;
                }
                return CameraCaptureMetaData$AwbState.CONVERGED;
            }
            return CameraCaptureMetaData$AwbState.METERING;
        }
        return CameraCaptureMetaData$AwbState.INACTIVE;
    }

    @Override // y.j
    public void c(ExifData.b bVar) {
        Integer num;
        super.c(bVar);
        Rect rect = (Rect) this.f1783b.get(CaptureResult.SCALER_CROP_REGION);
        if (rect != null) {
            bVar.j(rect.width()).i(rect.height());
        }
        try {
            Integer num2 = (Integer) this.f1783b.get(CaptureResult.JPEG_ORIENTATION);
            if (num2 != null) {
                bVar.m(num2.intValue());
            }
        } catch (BufferUnderflowException unused) {
            androidx.camera.core.p1.k("C2CameraCaptureResult", "Failed to get JPEG orientation.");
        }
        Long l8 = (Long) this.f1783b.get(CaptureResult.SENSOR_EXPOSURE_TIME);
        if (l8 != null) {
            bVar.f(l8.longValue());
        }
        Float f5 = (Float) this.f1783b.get(CaptureResult.LENS_APERTURE);
        if (f5 != null) {
            bVar.l(f5.floatValue());
        }
        Integer num3 = (Integer) this.f1783b.get(CaptureResult.SENSOR_SENSITIVITY);
        if (num3 != null) {
            if (Build.VERSION.SDK_INT >= 24 && (num = (Integer) this.f1783b.get(CaptureResult.CONTROL_POST_RAW_SENSITIVITY_BOOST)) != null) {
                num3 = Integer.valueOf(num3.intValue() * ((int) (num.intValue() / 100.0f)));
            }
            bVar.k(num3.intValue());
        }
        Float f8 = (Float) this.f1783b.get(CaptureResult.LENS_FOCAL_LENGTH);
        if (f8 != null) {
            bVar.h(f8.floatValue());
        }
        Integer num4 = (Integer) this.f1783b.get(CaptureResult.CONTROL_AWB_MODE);
        if (num4 != null) {
            ExifData.WhiteBalanceMode whiteBalanceMode = ExifData.WhiteBalanceMode.AUTO;
            if (num4.intValue() == 0) {
                whiteBalanceMode = ExifData.WhiteBalanceMode.MANUAL;
            }
            bVar.n(whiteBalanceMode);
        }
    }

    @Override // y.j
    public long d() {
        Long l8 = (Long) this.f1783b.get(CaptureResult.SENSOR_TIMESTAMP);
        if (l8 == null) {
            return -1L;
        }
        return l8.longValue();
    }

    @Override // y.j
    public CameraCaptureMetaData$FlashState e() {
        Integer num = (Integer) this.f1783b.get(CaptureResult.FLASH_STATE);
        if (num == null) {
            return CameraCaptureMetaData$FlashState.UNKNOWN;
        }
        int intValue = num.intValue();
        if (intValue == 0 || intValue == 1) {
            return CameraCaptureMetaData$FlashState.NONE;
        }
        if (intValue != 2) {
            if (intValue == 3 || intValue == 4) {
                return CameraCaptureMetaData$FlashState.FIRED;
            }
            androidx.camera.core.p1.c("C2CameraCaptureResult", "Undefined flash state: " + num);
            return CameraCaptureMetaData$FlashState.UNKNOWN;
        }
        return CameraCaptureMetaData$FlashState.READY;
    }

    @Override // y.j
    public CameraCaptureMetaData$AeState f() {
        Integer num = (Integer) this.f1783b.get(CaptureResult.CONTROL_AE_STATE);
        if (num == null) {
            return CameraCaptureMetaData$AeState.UNKNOWN;
        }
        int intValue = num.intValue();
        if (intValue != 0) {
            if (intValue != 1) {
                if (intValue == 2) {
                    return CameraCaptureMetaData$AeState.CONVERGED;
                }
                if (intValue == 3) {
                    return CameraCaptureMetaData$AeState.LOCKED;
                }
                if (intValue == 4) {
                    return CameraCaptureMetaData$AeState.FLASH_REQUIRED;
                }
                if (intValue != 5) {
                    androidx.camera.core.p1.c("C2CameraCaptureResult", "Undefined ae state: " + num);
                    return CameraCaptureMetaData$AeState.UNKNOWN;
                }
            }
            return CameraCaptureMetaData$AeState.SEARCHING;
        }
        return CameraCaptureMetaData$AeState.INACTIVE;
    }

    @Override // y.j
    public CaptureResult g() {
        return this.f1783b;
    }

    @Override // y.j
    public CameraCaptureMetaData$AfState h() {
        Integer num = (Integer) this.f1783b.get(CaptureResult.CONTROL_AF_STATE);
        if (num == null) {
            return CameraCaptureMetaData$AfState.UNKNOWN;
        }
        switch (num.intValue()) {
            case 0:
                return CameraCaptureMetaData$AfState.INACTIVE;
            case 1:
            case 3:
                return CameraCaptureMetaData$AfState.SCANNING;
            case 2:
                return CameraCaptureMetaData$AfState.PASSIVE_FOCUSED;
            case 4:
                return CameraCaptureMetaData$AfState.LOCKED_FOCUSED;
            case 5:
                return CameraCaptureMetaData$AfState.LOCKED_NOT_FOCUSED;
            case 6:
                return CameraCaptureMetaData$AfState.PASSIVE_NOT_FOCUSED;
            default:
                androidx.camera.core.p1.c("C2CameraCaptureResult", "Undefined af state: " + num);
                return CameraCaptureMetaData$AfState.UNKNOWN;
        }
    }

    public CameraCaptureMetaData$AfMode i() {
        Integer num = (Integer) this.f1783b.get(CaptureResult.CONTROL_AF_MODE);
        if (num == null) {
            return CameraCaptureMetaData$AfMode.UNKNOWN;
        }
        int intValue = num.intValue();
        if (intValue != 0) {
            if (intValue == 1 || intValue == 2) {
                return CameraCaptureMetaData$AfMode.ON_MANUAL_AUTO;
            }
            if (intValue == 3 || intValue == 4) {
                return CameraCaptureMetaData$AfMode.ON_CONTINUOUS_AUTO;
            }
            if (intValue != 5) {
                androidx.camera.core.p1.c("C2CameraCaptureResult", "Undefined af mode: " + num);
                return CameraCaptureMetaData$AfMode.UNKNOWN;
            }
        }
        return CameraCaptureMetaData$AfMode.OFF;
    }
}
