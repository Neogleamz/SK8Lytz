package x;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Size;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.impl.utils.ExifData;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.camera.core.l1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m implements g0.d<a, g0.e<byte[]>> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static a c(g0.e<l1> eVar, int i8) {
            return new d(eVar, i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract int a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.e<l1> b();
    }

    private static byte[] b(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        byte[] bArr = new byte[position];
        byteBuffer.rewind();
        byteBuffer.get(bArr, 0, position);
        return bArr;
    }

    private static androidx.camera.core.impl.utils.f c(byte[] bArr) {
        try {
            return androidx.camera.core.impl.utils.f.h(new ByteArrayInputStream(bArr));
        } catch (IOException e8) {
            throw new ImageCaptureException(0, "Failed to extract Exif from YUV-generated JPEG", e8);
        }
    }

    private g0.e<byte[]> d(a aVar) {
        g0.e<l1> b9 = aVar.b();
        byte[] h8 = ImageUtil.h(b9.c());
        androidx.camera.core.impl.utils.f d8 = b9.d();
        Objects.requireNonNull(d8);
        return g0.e.l(h8, d8, RecognitionOptions.QR_CODE, b9.h(), b9.b(), b9.f(), b9.g(), b9.a());
    }

    private g0.e<byte[]> e(a aVar) {
        g0.e<l1> b9 = aVar.b();
        l1 c9 = b9.c();
        Rect b10 = b9.b();
        YuvImage yuvImage = new YuvImage(ImageUtil.k(c9), 17, c9.getWidth(), c9.getHeight(), null);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(b10.width() * b10.height() * 2);
        yuvImage.compressToJpeg(b10, aVar.a(), new androidx.camera.core.impl.utils.h(new b0.b(allocateDirect), ExifData.b(c9, b9.f())));
        byte[] b11 = b(allocateDirect);
        return g0.e.l(b11, c(b11), RecognitionOptions.QR_CODE, new Size(b10.width(), b10.height()), new Rect(0, 0, b10.width(), b10.height()), b9.f(), androidx.camera.core.impl.utils.n.o(b9.g(), b10), b9.a());
    }

    @Override // g0.d
    /* renamed from: a */
    public g0.e<byte[]> apply(a aVar) {
        g0.e<byte[]> e8;
        try {
            int e9 = aVar.b().e();
            if (e9 == 35) {
                e8 = e(aVar);
            } else if (e9 != 256) {
                throw new IllegalArgumentException("Unexpected format: " + e9);
            } else {
                e8 = d(aVar);
            }
            return e8;
        } finally {
            aVar.b().c().close();
        }
    }
}
