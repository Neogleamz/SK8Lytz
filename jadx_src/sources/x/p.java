package x;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import androidx.camera.core.ImageCaptureException;
import java.io.IOException;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p implements g0.d<g0.e<byte[]>, g0.e<Bitmap>> {
    private Bitmap b(byte[] bArr, Rect rect) {
        try {
            return BitmapRegionDecoder.newInstance(bArr, 0, bArr.length, false).decodeRegion(rect, new BitmapFactory.Options());
        } catch (IOException e8) {
            throw new ImageCaptureException(1, "Failed to decode JPEG.", e8);
        }
    }

    @Override // g0.d
    /* renamed from: a */
    public g0.e<Bitmap> apply(g0.e<byte[]> eVar) {
        Rect b9 = eVar.b();
        Bitmap b10 = b(eVar.c(), b9);
        androidx.camera.core.impl.utils.f d8 = eVar.d();
        Objects.requireNonNull(d8);
        return g0.e.j(b10, d8, new Rect(0, 0, b10.getWidth(), b10.getHeight()), eVar.f(), androidx.camera.core.impl.utils.n.o(eVar.g(), b9), eVar.a());
    }
}
