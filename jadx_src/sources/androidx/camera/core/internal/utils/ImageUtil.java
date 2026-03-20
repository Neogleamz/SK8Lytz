package androidx.camera.core.internal.utils;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.l1;
import androidx.camera.core.p1;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImageUtil {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class CodecFailedException extends Exception {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public enum FailureType {
            ENCODE_FAILED,
            DECODE_FAILED,
            UNKNOWN
        }
    }

    public static Rect a(Size size, Rational rational) {
        int i8;
        if (!e(rational)) {
            p1.k("ImageUtil", "Invalid view ratio.");
            return null;
        }
        int width = size.getWidth();
        int height = size.getHeight();
        float f5 = width;
        float f8 = height;
        int numerator = rational.getNumerator();
        int denominator = rational.getDenominator();
        int i9 = 0;
        if (rational.floatValue() > f5 / f8) {
            int round = Math.round((f5 / numerator) * denominator);
            i8 = (height - round) / 2;
            height = round;
        } else {
            int round2 = Math.round((f8 / denominator) * numerator);
            int i10 = (width - round2) / 2;
            width = round2;
            i8 = 0;
            i9 = i10;
        }
        return new Rect(i9, i8, width + i9, height + i8);
    }

    public static Rect b(Rect rect, int i8, Size size, int i9) {
        Matrix matrix = new Matrix();
        matrix.setRotate(i9 - i8);
        float[] j8 = j(size);
        matrix.mapPoints(j8);
        matrix.postTranslate(-i(j8[0], j8[2], j8[4], j8[6]), -i(j8[1], j8[3], j8[5], j8[7]));
        matrix.invert(matrix);
        RectF rectF = new RectF();
        matrix.mapRect(rectF, new RectF(rect));
        rectF.sort();
        Rect rect2 = new Rect();
        rectF.round(rect2);
        return rect2;
    }

    public static Rational c(int i8, Rational rational) {
        return (i8 == 90 || i8 == 270) ? d(rational) : new Rational(rational.getNumerator(), rational.getDenominator());
    }

    private static Rational d(Rational rational) {
        return rational == null ? rational : new Rational(rational.getDenominator(), rational.getNumerator());
    }

    public static boolean e(Rational rational) {
        return (rational == null || rational.floatValue() <= 0.0f || rational.isNaN()) ? false : true;
    }

    public static boolean f(Size size, Rational rational) {
        return rational != null && rational.floatValue() > 0.0f && g(size, rational) && !rational.isNaN();
    }

    private static boolean g(Size size, Rational rational) {
        int width = size.getWidth();
        int height = size.getHeight();
        float numerator = rational.getNumerator();
        float denominator = rational.getDenominator();
        return (height == Math.round((((float) width) / numerator) * denominator) && width == Math.round((((float) height) / denominator) * numerator)) ? false : true;
    }

    public static byte[] h(l1 l1Var) {
        if (l1Var.getFormat() != 256) {
            throw new IllegalArgumentException("Incorrect image format of the input image proxy: " + l1Var.getFormat());
        }
        ByteBuffer b9 = l1Var.A()[0].b();
        byte[] bArr = new byte[b9.capacity()];
        b9.rewind();
        b9.get(bArr);
        return bArr;
    }

    public static float i(float f5, float f8, float f9, float f10) {
        return Math.min(Math.min(f5, f8), Math.min(f9, f10));
    }

    public static float[] j(Size size) {
        return new float[]{0.0f, 0.0f, size.getWidth(), 0.0f, size.getWidth(), size.getHeight(), 0.0f, size.getHeight()};
    }

    public static byte[] k(l1 l1Var) {
        l1.a aVar = l1Var.A()[0];
        l1.a aVar2 = l1Var.A()[1];
        l1.a aVar3 = l1Var.A()[2];
        ByteBuffer b9 = aVar.b();
        ByteBuffer b10 = aVar2.b();
        ByteBuffer b11 = aVar3.b();
        b9.rewind();
        b10.rewind();
        b11.rewind();
        int remaining = b9.remaining();
        byte[] bArr = new byte[((l1Var.getWidth() * l1Var.getHeight()) / 2) + remaining];
        int i8 = 0;
        for (int i9 = 0; i9 < l1Var.getHeight(); i9++) {
            b9.get(bArr, i8, l1Var.getWidth());
            i8 += l1Var.getWidth();
            b9.position(Math.min(remaining, (b9.position() - l1Var.getWidth()) + aVar.a()));
        }
        int height = l1Var.getHeight() / 2;
        int width = l1Var.getWidth() / 2;
        int a9 = aVar3.a();
        int a10 = aVar2.a();
        int c9 = aVar3.c();
        int c10 = aVar2.c();
        byte[] bArr2 = new byte[a9];
        byte[] bArr3 = new byte[a10];
        for (int i10 = 0; i10 < height; i10++) {
            b11.get(bArr2, 0, Math.min(a9, b11.remaining()));
            b10.get(bArr3, 0, Math.min(a10, b10.remaining()));
            int i11 = 0;
            int i12 = 0;
            for (int i13 = 0; i13 < width; i13++) {
                int i14 = i8 + 1;
                bArr[i8] = bArr2[i11];
                i8 = i14 + 1;
                bArr[i14] = bArr3[i12];
                i11 += c9;
                i12 += c10;
            }
        }
        return bArr;
    }
}
