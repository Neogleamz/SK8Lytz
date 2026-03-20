package androidx.camera.core;

import android.graphics.Matrix;
import androidx.camera.core.impl.utils.ExifData;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface i1 {
    y.a1 a();

    int b();

    void c(ExifData.b bVar);

    long d();

    default Matrix e() {
        return new Matrix();
    }
}
