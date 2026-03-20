package androidx.camera.core;

import android.media.Image;
import android.media.ImageWriter;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import androidx.camera.core.h0;
import java.nio.ByteBuffer;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImageProcessingUtil {

    /* renamed from: a  reason: collision with root package name */
    private static int f2211a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum a {
        UNKNOWN,
        SUCCESS,
        ERROR_CONVERSION
    }

    static {
        System.loadLibrary("image_processing_util_jni");
    }

    public static boolean c(l1 l1Var) {
        String str;
        if (!i(l1Var)) {
            str = "Unsupported format for YUV to RGB";
        } else if (d(l1Var) != a.ERROR_CONVERSION) {
            return true;
        } else {
            str = "One pixel shift for YUV failure";
        }
        p1.c("ImageProcessingUtil", str);
        return false;
    }

    private static a d(l1 l1Var) {
        int width = l1Var.getWidth();
        int height = l1Var.getHeight();
        int a9 = l1Var.A()[0].a();
        int a10 = l1Var.A()[1].a();
        int a11 = l1Var.A()[2].a();
        int c9 = l1Var.A()[0].c();
        int c10 = l1Var.A()[1].c();
        return nativeShiftPixel(l1Var.A()[0].b(), a9, l1Var.A()[1].b(), a10, l1Var.A()[2].b(), a11, c9, c10, width, height, c9, c10, c10) != 0 ? a.ERROR_CONVERSION : a.SUCCESS;
    }

    public static l1 e(y.g0 g0Var, byte[] bArr) {
        androidx.core.util.h.a(g0Var.c() == 256);
        androidx.core.util.h.h(bArr);
        Surface surface = g0Var.getSurface();
        androidx.core.util.h.h(surface);
        if (nativeWriteJpegToSurface(bArr, surface) != 0) {
            p1.c("ImageProcessingUtil", "Failed to enqueue JPEG image.");
            return null;
        }
        l1 acquireLatestImage = g0Var.acquireLatestImage();
        if (acquireLatestImage == null) {
            p1.c("ImageProcessingUtil", "Failed to get acquire JPEG image.");
        }
        return acquireLatestImage;
    }

    public static l1 f(final l1 l1Var, y.g0 g0Var, ByteBuffer byteBuffer, int i8, boolean z4) {
        String str;
        if (i(l1Var)) {
            long currentTimeMillis = System.currentTimeMillis();
            if (!h(i8)) {
                str = "Unsupported rotation degrees for rotate RGB";
            } else if (g(l1Var, g0Var.getSurface(), byteBuffer, i8, z4) == a.ERROR_CONVERSION) {
                str = "YUV to RGB conversion failure";
            } else {
                if (Log.isLoggable("MH", 3)) {
                    p1.a("ImageProcessingUtil", String.format(Locale.US, "Image processing performance profiling, duration: [%d], image count: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(f2211a)));
                    f2211a++;
                }
                final l1 acquireLatestImage = g0Var.acquireLatestImage();
                if (acquireLatestImage != null) {
                    p2 p2Var = new p2(acquireLatestImage);
                    p2Var.a(new h0.a() { // from class: androidx.camera.core.k1
                        @Override // androidx.camera.core.h0.a
                        public final void b(l1 l1Var2) {
                            ImageProcessingUtil.j(l1.this, l1Var, l1Var2);
                        }
                    });
                    return p2Var;
                }
                str = "YUV to RGB acquireLatestImage failure";
            }
        } else {
            str = "Unsupported format for YUV to RGB";
        }
        p1.c("ImageProcessingUtil", str);
        return null;
    }

    private static a g(l1 l1Var, Surface surface, ByteBuffer byteBuffer, int i8, boolean z4) {
        int width = l1Var.getWidth();
        int height = l1Var.getHeight();
        int a9 = l1Var.A()[0].a();
        int a10 = l1Var.A()[1].a();
        int a11 = l1Var.A()[2].a();
        int c9 = l1Var.A()[0].c();
        int c10 = l1Var.A()[1].c();
        return nativeConvertAndroid420ToABGR(l1Var.A()[0].b(), a9, l1Var.A()[1].b(), a10, l1Var.A()[2].b(), a11, c9, c10, surface, byteBuffer, width, height, z4 ? c9 : 0, z4 ? c10 : 0, z4 ? c10 : 0, i8) != 0 ? a.ERROR_CONVERSION : a.SUCCESS;
    }

    private static boolean h(int i8) {
        return i8 == 0 || i8 == 90 || i8 == 180 || i8 == 270;
    }

    private static boolean i(l1 l1Var) {
        return l1Var.getFormat() == 35 && l1Var.A().length == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void j(l1 l1Var, l1 l1Var2, l1 l1Var3) {
        if (l1Var == null || l1Var2 == null) {
            return;
        }
        l1Var2.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void k(l1 l1Var, l1 l1Var2, l1 l1Var3) {
        if (l1Var == null || l1Var2 == null) {
            return;
        }
        l1Var2.close();
    }

    public static l1 l(final l1 l1Var, y.g0 g0Var, ImageWriter imageWriter, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i8) {
        String str;
        if (!i(l1Var)) {
            str = "Unsupported format for rotate YUV";
        } else if (h(i8)) {
            a aVar = a.ERROR_CONVERSION;
            if (((Build.VERSION.SDK_INT < 23 || i8 <= 0) ? aVar : m(l1Var, imageWriter, byteBuffer, byteBuffer2, byteBuffer3, i8)) == aVar) {
                str = "rotate YUV failure";
            } else {
                final l1 acquireLatestImage = g0Var.acquireLatestImage();
                if (acquireLatestImage != null) {
                    p2 p2Var = new p2(acquireLatestImage);
                    p2Var.a(new h0.a() { // from class: androidx.camera.core.j1
                        @Override // androidx.camera.core.h0.a
                        public final void b(l1 l1Var2) {
                            ImageProcessingUtil.k(l1.this, l1Var, l1Var2);
                        }
                    });
                    return p2Var;
                }
                str = "YUV rotation acquireLatestImage failure";
            }
        } else {
            str = "Unsupported rotation degrees for rotate YUV";
        }
        p1.c("ImageProcessingUtil", str);
        return null;
    }

    private static a m(l1 l1Var, ImageWriter imageWriter, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i8) {
        int width = l1Var.getWidth();
        int height = l1Var.getHeight();
        int a9 = l1Var.A()[0].a();
        int a10 = l1Var.A()[1].a();
        int a11 = l1Var.A()[2].a();
        int c9 = l1Var.A()[1].c();
        Image b9 = c0.a.b(imageWriter);
        if (b9 != null && nativeRotateYUV(l1Var.A()[0].b(), a9, l1Var.A()[1].b(), a10, l1Var.A()[2].b(), a11, c9, b9.getPlanes()[0].getBuffer(), b9.getPlanes()[0].getRowStride(), b9.getPlanes()[0].getPixelStride(), b9.getPlanes()[1].getBuffer(), b9.getPlanes()[1].getRowStride(), b9.getPlanes()[1].getPixelStride(), b9.getPlanes()[2].getBuffer(), b9.getPlanes()[2].getRowStride(), b9.getPlanes()[2].getPixelStride(), byteBuffer, byteBuffer2, byteBuffer3, width, height, i8) == 0) {
            c0.a.e(imageWriter, b9);
            return a.SUCCESS;
        }
        return a.ERROR_CONVERSION;
    }

    private static native int nativeConvertAndroid420ToABGR(ByteBuffer byteBuffer, int i8, ByteBuffer byteBuffer2, int i9, ByteBuffer byteBuffer3, int i10, int i11, int i12, Surface surface, ByteBuffer byteBuffer4, int i13, int i14, int i15, int i16, int i17, int i18);

    private static native int nativeRotateYUV(ByteBuffer byteBuffer, int i8, ByteBuffer byteBuffer2, int i9, ByteBuffer byteBuffer3, int i10, int i11, ByteBuffer byteBuffer4, int i12, int i13, ByteBuffer byteBuffer5, int i14, int i15, ByteBuffer byteBuffer6, int i16, int i17, ByteBuffer byteBuffer7, ByteBuffer byteBuffer8, ByteBuffer byteBuffer9, int i18, int i19, int i20);

    private static native int nativeShiftPixel(ByteBuffer byteBuffer, int i8, ByteBuffer byteBuffer2, int i9, ByteBuffer byteBuffer3, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17);

    private static native int nativeWriteJpegToSurface(byte[] bArr, Surface surface);
}
