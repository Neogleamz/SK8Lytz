package x;

import android.graphics.Bitmap;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h implements g0.d<a, g0.e<byte[]>> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static a c(g0.e<Bitmap> eVar, int i8) {
            return new x.a(eVar, i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract int a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.e<Bitmap> b();
    }

    @Override // g0.d
    /* renamed from: a */
    public g0.e<byte[]> apply(a aVar) {
        g0.e<Bitmap> b9 = aVar.b();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b9.c().compress(Bitmap.CompressFormat.JPEG, aVar.a(), byteArrayOutputStream);
        b9.c().recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        androidx.camera.core.impl.utils.f d8 = b9.d();
        Objects.requireNonNull(d8);
        return g0.e.l(byteArray, d8, RecognitionOptions.QR_CODE, b9.h(), b9.b(), b9.f(), b9.g(), b9.a());
    }
}
