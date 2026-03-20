package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private static boolean f8545a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap a(Bitmap bitmap, int i8) {
        float height;
        float width;
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(i8, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        if (i8 == 90) {
            height = bitmap.getHeight();
            width = 0.0f;
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        matrix.postTranslate(height - fArr[2], width - fArr[5]);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, matrix, new Paint());
        return createBitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(MotionEvent motionEvent) {
        float x8 = motionEvent.getX(0) - motionEvent.getX(1);
        float y8 = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((x8 * x8) + (y8 * y8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Rect c(float f5, float f8, float f9, int i8, int i9, int i10, int i11) {
        int i12 = (int) ((i8 * f5) / 2.0f);
        int i13 = (int) ((i9 * f5) / 2.0f);
        int i14 = (int) (((f8 / i10) * 2000.0f) - 1000.0f);
        int i15 = (int) (((f9 / i11) * 2000.0f) - 1000.0f);
        RectF rectF = new RectF(d(i14 - i12, -1000, 1000), d(i15 - i13, -1000, 1000), d(i14 + i12, -1000, 1000), d(i15 + i13, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    static int d(int i8, int i9, int i10) {
        return Math.min(Math.max(i8, i9), i10);
    }

    public static void e(String str) {
        f("BGAQRCode", str);
    }

    public static void f(String str, String str2) {
        if (f8545a) {
            Log.d(str, str2);
        }
    }

    public static int g(Context context, float f5) {
        return (int) TypedValue.applyDimension(1, f5, context.getResources().getDisplayMetrics());
    }

    public static void h(String str) {
        if (f8545a) {
            Log.e("BGAQRCode", str);
        }
    }

    public static Bitmap i(String str) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            int i8 = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            int i9 = options.outHeight / 400;
            if (i9 > 0) {
                i8 = i9;
            }
            options.inSampleSize = i8;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(str, options);
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Point j(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static int k(Context context) {
        int identifier;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{16843277});
        boolean z4 = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        if (!z4 && (identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android")) > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean l() {
        return f8545a;
    }

    public static boolean m(Context context) {
        Point j8 = j(context);
        return j8.y > j8.x;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap n(Bitmap bitmap, int i8) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i8, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static void o(String str, Rect rect) {
        f("BGAQRCodeFocusArea", str + " centerX：" + rect.centerX() + " centerY：" + rect.centerY() + " width：" + rect.width() + " height：" + rect.height() + " rectHalfWidth：" + (rect.width() / 2) + " rectHalfHeight：" + (rect.height() / 2) + " left：" + rect.left + " top：" + rect.top + " right：" + rect.right + " bottom：" + rect.bottom);
    }

    public static int p(Context context, float f5) {
        return (int) TypedValue.applyDimension(2, f5, context.getResources().getDisplayMetrics());
    }
}
