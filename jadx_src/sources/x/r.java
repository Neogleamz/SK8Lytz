package x;

import androidx.camera.core.ImageProcessingUtil;
import androidx.camera.core.l1;
import androidx.camera.core.m2;
import androidx.camera.core.n1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r implements g0.d<g0.e<byte[]>, g0.e<l1>> {
    @Override // g0.d
    /* renamed from: a */
    public g0.e<l1> apply(g0.e<byte[]> eVar) {
        m2 m2Var = new m2(n1.a(eVar.h().getWidth(), eVar.h().getHeight(), RecognitionOptions.QR_CODE, 2));
        l1 e8 = ImageProcessingUtil.e(m2Var, eVar.c());
        m2Var.k();
        Objects.requireNonNull(e8);
        androidx.camera.core.impl.utils.f d8 = eVar.d();
        Objects.requireNonNull(d8);
        return g0.e.k(e8, d8, eVar.b(), eVar.f(), eVar.g(), eVar.a());
    }
}
