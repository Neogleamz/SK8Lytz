package f0;

import androidx.camera.core.i1;
import androidx.camera.core.impl.CameraCaptureMetaData$AeState;
import androidx.camera.core.impl.CameraCaptureMetaData$AfState;
import androidx.camera.core.impl.CameraCaptureMetaData$AwbState;
import androidx.camera.core.l1;
import y.j;
import y.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends a<l1> {
    public d(int i8, b<l1> bVar) {
        super(i8, bVar);
    }

    private boolean e(i1 i1Var) {
        j a9 = k.a(i1Var);
        return (a9.h() == CameraCaptureMetaData$AfState.LOCKED_FOCUSED || a9.h() == CameraCaptureMetaData$AfState.PASSIVE_FOCUSED) && a9.f() == CameraCaptureMetaData$AeState.CONVERGED && a9.b() == CameraCaptureMetaData$AwbState.CONVERGED;
    }

    public void d(l1 l1Var) {
        if (e(l1Var.e1())) {
            super.b(l1Var);
        } else {
            this.f19830d.a(l1Var);
        }
    }
}
