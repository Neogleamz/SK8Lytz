package x;

import androidx.camera.core.ImageCaptureException;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* renamed from: a  reason: collision with root package name */
    private final List<androidx.camera.core.impl.f> f23717a;

    /* renamed from: b  reason: collision with root package name */
    private final g0 f23718b;

    public i(List<androidx.camera.core.impl.f> list, g0 g0Var) {
        this.f23717a = list;
        this.f23718b = g0Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<androidx.camera.core.impl.f> a() {
        return this.f23717a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ImageCaptureException imageCaptureException) {
        androidx.camera.core.impl.utils.m.a();
        this.f23718b.b(imageCaptureException);
    }
}
