package k4;

import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v {
    public static List<byte[]> a(byte[] bArr) {
        long h8 = h(f(bArr));
        long h9 = h(3840L);
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(bArr);
        arrayList.add(b(h8));
        arrayList.add(b(h9));
        return arrayList;
    }

    private static byte[] b(long j8) {
        return ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(j8).array();
    }

    public static int c(byte[] bArr) {
        return bArr[9] & 255;
    }

    private static long d(byte b9, byte b10) {
        int i8;
        int i9 = b9 & 255;
        int i10 = i9 & 3;
        int i11 = 2;
        if (i10 == 0) {
            i11 = 1;
        } else if (i10 != 1 && i10 != 2) {
            i11 = b10 & 63;
        }
        int i12 = i9 >> 3;
        return i11 * (i12 >= 16 ? 2500 << i8 : i12 >= 12 ? ModuleDescriptor.MODULE_VERSION << (i8 & 1) : (i12 & 3) == 3 ? 60000 : ModuleDescriptor.MODULE_VERSION << i8);
    }

    public static long e(byte[] bArr) {
        return d(bArr[0], bArr.length > 1 ? bArr[1] : (byte) 0);
    }

    private static int f(byte[] bArr) {
        return (bArr[10] & 255) | ((bArr[11] & 255) << 8);
    }

    public static int g(ByteBuffer byteBuffer) {
        return (int) ((d(byteBuffer.get(0), byteBuffer.limit() > 1 ? byteBuffer.get(1) : (byte) 0) * 48000) / 1000000);
    }

    private static long h(long j8) {
        return (j8 * 1000000000) / 48000;
    }
}
