package b6;

import android.media.MediaFormat;
import java.nio.ByteBuffer;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s {
    public static void a(MediaFormat mediaFormat, String str, byte[] bArr) {
        if (bArr != null) {
            mediaFormat.setByteBuffer(str, ByteBuffer.wrap(bArr));
        }
    }

    public static void b(MediaFormat mediaFormat, c6.c cVar) {
        if (cVar != null) {
            d(mediaFormat, "color-transfer", cVar.f8340c);
            d(mediaFormat, "color-standard", cVar.f8338a);
            d(mediaFormat, "color-range", cVar.f8339b);
            a(mediaFormat, "hdr-static-info", cVar.f8341d);
        }
    }

    public static void c(MediaFormat mediaFormat, String str, float f5) {
        if (f5 != -1.0f) {
            mediaFormat.setFloat(str, f5);
        }
    }

    public static void d(MediaFormat mediaFormat, String str, int i8) {
        if (i8 != -1) {
            mediaFormat.setInteger(str, i8);
        }
    }

    public static void e(MediaFormat mediaFormat, List<byte[]> list) {
        for (int i8 = 0; i8 < list.size(); i8++) {
            mediaFormat.setByteBuffer("csd-" + i8, ByteBuffer.wrap(list.get(i8)));
        }
    }
}
