package e2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.f;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.EnumMap;
import java.util.Map;
import w9.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    public static final Map<EncodeHintType, Object> f19749a;

    static {
        EnumMap enumMap = new EnumMap(EncodeHintType.class);
        f19749a = enumMap;
        enumMap.put((EnumMap) EncodeHintType.b, (EncodeHintType) "utf-8");
        enumMap.put((EnumMap) EncodeHintType.a, (EncodeHintType) ErrorCorrectionLevel.e);
        enumMap.put((EnumMap) EncodeHintType.f, (EncodeHintType) 0);
    }

    private static Bitmap a(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        float f5 = ((width * 1.0f) / 5.0f) / width2;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.scale(f5, f5, width / 2, height / 2);
            canvas.drawBitmap(bitmap2, (width - width2) / 2, (height - height2) / 2, (Paint) null);
            canvas.save();
            canvas.restore();
            return createBitmap;
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static Bitmap b(String str, int i8, int i9, int i10, Bitmap bitmap) {
        try {
            b a9 = new f().a(str, BarcodeFormat.m, i8, i8, f19749a);
            int[] iArr = new int[i8 * i8];
            for (int i11 = 0; i11 < i8; i11++) {
                for (int i12 = 0; i12 < i8; i12++) {
                    if (a9.f(i12, i11)) {
                        iArr[(i11 * i8) + i12] = i9;
                    } else {
                        iArr[(i11 * i8) + i12] = i10;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i8, i8, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i8, 0, 0, i8, i8);
            return a(createBitmap, bitmap);
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static Bitmap c(String str, int i8, int i9, Bitmap bitmap) {
        return b(str, i8, i9, -1, bitmap);
    }
}
