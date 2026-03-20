package n3;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    public static final float a(Bitmap bitmap, int i8, int i9) {
        p.e(bitmap, "<this>");
        float width = bitmap.getWidth() / i8;
        float height = bitmap.getHeight() / i9;
        e("width scale = " + width);
        e("height scale = " + height);
        return Math.max(1.0f, Math.min(width, height));
    }

    public static final void b(Bitmap bitmap, int i8, int i9, int i10, int i11, OutputStream outputStream, int i12) {
        p.e(bitmap, "<this>");
        p.e(outputStream, "outputStream");
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        e("src width = " + width);
        e("src height = " + height);
        float a9 = a(bitmap, i8, i9);
        e("scale = " + a9);
        float f5 = width / a9;
        float f8 = height / a9;
        e("dst width = " + f5);
        e("dst height = " + f8);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) f5, (int) f8, true);
        p.d(createScaledBitmap, "createScaledBitmap(...)");
        f(createScaledBitmap, i11).compress(d(i12), i10, outputStream);
    }

    public static final byte[] c(Bitmap bitmap, int i8, int i9, int i10, int i11, int i12) {
        p.e(bitmap, "<this>");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b(bitmap, i8, i9, i10, i11, byteArrayOutputStream, i12);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        p.d(byteArray, "toByteArray(...)");
        return byteArray;
    }

    public static final Bitmap.CompressFormat d(int i8) {
        return i8 != 1 ? i8 != 3 ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.WEBP : Bitmap.CompressFormat.PNG;
    }

    private static final void e(Object obj) {
        if (k3.a.f20975c.a()) {
            if (obj == null) {
                obj = "null";
            }
            System.out.println(obj);
        }
    }

    public static final Bitmap f(Bitmap bitmap, int i8) {
        p.e(bitmap, "<this>");
        if (i8 % 360 != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(i8);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            p.b(createBitmap);
            return createBitmap;
        }
        return bitmap;
    }
}
