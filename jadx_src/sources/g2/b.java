package g2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.ByteArrayOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {
    public static Bitmap a(Context context, int i8) {
        return BitmapFactory.decodeResource(context.getResources(), i8);
    }

    public static Bitmap b(Bitmap bitmap, int i8, int i9) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i8 / width, i9 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static byte[] c(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int d(BitmapFactory.Options options, float f5, float f8) {
        int i8 = options.outHeight;
        int i9 = options.outWidth;
        float f9 = i8;
        if (f9 > f8 || i9 > f5) {
            int round = Math.round(f9 / f8);
            int round2 = Math.round(i9 / f5);
            return round < round2 ? round : round2;
        }
        return 1;
    }

    public static Bitmap e(Context context, Uri uri, float f5, float f8) {
        if ("content".equals(uri.getScheme())) {
            return f(context.getContentResolver().openFileDescriptor(uri, "r"), f5, f8);
        }
        throw new RuntimeException("unSupport uri");
    }

    public static Bitmap f(ParcelFileDescriptor parcelFileDescriptor, float f5, float f8) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor(), null, options);
        options.inSampleSize = d(options, f5, f8);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor(), null, options);
    }

    public static String g(Bitmap bitmap) {
        return c.g(c(bitmap));
    }

    public static final Bitmap h(int i8, int i9, int i10) {
        Bitmap createBitmap = Bitmap.createBitmap(i8, i9, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawColor(i10);
        return createBitmap;
    }

    public static Bitmap i(Bitmap bitmap, float f5) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f5, f5, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static int j(int i8) {
        float[] fArr = new float[3];
        Color.colorToHSV(i8, fArr);
        if (fArr[1] > 0.2f) {
            fArr[1] = d.f(0.2f, 1.0f, 0.8f, 1.0f, fArr[1]);
            if (fArr[1] >= 0.95f) {
                fArr[1] = 1.0f;
            }
        }
        if (fArr[2] < 0.2f) {
            fArr[2] = 0.0f;
        }
        return Color.HSVToColor(fArr);
    }

    public static Bitmap k(Bitmap bitmap, float f5, float f8) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(f5 / width, f8 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
