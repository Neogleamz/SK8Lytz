package y;

import android.hardware.camera2.CaptureResult;
import androidx.camera.core.impl.CameraCaptureMetaData$AeState;
import androidx.camera.core.impl.CameraCaptureMetaData$AfState;
import androidx.camera.core.impl.CameraCaptureMetaData$AwbState;
import androidx.camera.core.impl.CameraCaptureMetaData$FlashState;
import androidx.camera.core.impl.utils.ExifData;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface j {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements j {
        public static j i() {
            return new a();
        }

        @Override // y.j
        public a1 a() {
            return a1.a();
        }

        @Override // y.j
        public CameraCaptureMetaData$AwbState b() {
            return CameraCaptureMetaData$AwbState.UNKNOWN;
        }

        @Override // y.j
        public long d() {
            return -1L;
        }

        @Override // y.j
        public CameraCaptureMetaData$FlashState e() {
            return CameraCaptureMetaData$FlashState.UNKNOWN;
        }

        @Override // y.j
        public CameraCaptureMetaData$AeState f() {
            return CameraCaptureMetaData$AeState.UNKNOWN;
        }

        @Override // y.j
        public CameraCaptureMetaData$AfState h() {
            return CameraCaptureMetaData$AfState.UNKNOWN;
        }
    }

    a1 a();

    CameraCaptureMetaData$AwbState b();

    default void c(ExifData.b bVar) {
        bVar.g(e());
    }

    long d();

    CameraCaptureMetaData$FlashState e();

    CameraCaptureMetaData$AeState f();

    default CaptureResult g() {
        return a.i().g();
    }

    CameraCaptureMetaData$AfState h();
}
