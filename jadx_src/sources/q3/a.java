package q3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import kotlin.jvm.internal.p;
import m3.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements p3.a {

    /* renamed from: a  reason: collision with root package name */
    private final int f22525a;

    /* renamed from: b  reason: collision with root package name */
    private final String f22526b;

    /* renamed from: c  reason: collision with root package name */
    private final Bitmap.CompressFormat f22527c;

    public a(int i8) {
        this.f22525a = i8;
        int type = getType();
        this.f22526b = type != 1 ? type != 3 ? "jpeg" : "webp" : "png";
        int type2 = getType();
        this.f22527c = type2 != 1 ? type2 != 3 ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.WEBP : Bitmap.CompressFormat.PNG;
    }

    private final byte[] c(byte[] bArr, int i8, int i9, int i10, int i11, int i12) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = i12;
        if (Build.VERSION.SDK_INT < 23) {
            options.inDither = true;
        }
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        float width = decodeByteArray.getWidth();
        float height = decodeByteArray.getHeight();
        s3.a.a("src width = " + width);
        s3.a.a("src height = " + height);
        p.b(decodeByteArray);
        float a9 = n3.a.a(decodeByteArray, i8, i9);
        s3.a.a("scale = " + a9);
        float f5 = width / a9;
        float f8 = height / a9;
        s3.a.a("dst width = " + f5);
        s3.a.a("dst height = " + f8);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeByteArray, (int) f5, (int) f8, true);
        p.d(createScaledBitmap, "createScaledBitmap(...)");
        n3.a.f(createScaledBitmap, i11).compress(this.f22527c, i10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        p.d(byteArray, "toByteArray(...)");
        return byteArray;
    }

    @Override // p3.a
    public void a(Context context, byte[] bArr, OutputStream outputStream, int i8, int i9, int i10, int i11, boolean z4, int i12) {
        p.e(context, "context");
        p.e(bArr, "byteArray");
        p.e(outputStream, "outputStream");
        byte[] c9 = c(bArr, i8, i9, i10, i11, i12);
        if (!z4 || this.f22527c != Bitmap.CompressFormat.JPEG) {
            outputStream.write(c9);
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(c9);
        outputStream.write(new b(bArr).c(context, byteArrayOutputStream).toByteArray());
    }

    @Override // p3.a
    public void b(Context context, String str, OutputStream outputStream, int i8, int i9, int i10, int i11, boolean z4, int i12, int i13) {
        p.e(context, "context");
        p.e(str, "path");
        p.e(outputStream, "outputStream");
        if (i13 <= 0) {
            return;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = i12;
            if (Build.VERSION.SDK_INT < 23) {
                options.inDither = true;
            }
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
            p.b(decodeFile);
            byte[] c9 = n3.a.c(decodeFile, i8, i9, i10, i11, getType());
            if (z4) {
                try {
                    if (this.f22527c == Bitmap.CompressFormat.JPEG) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byteArrayOutputStream.write(c9);
                        outputStream.write(new b(str).c(context, byteArrayOutputStream).toByteArray());
                    }
                } catch (OutOfMemoryError unused) {
                    System.gc();
                    b(context, str, outputStream, i8, i9, i10, i11, z4, i12 * 2, i13 - 1);
                    return;
                }
            }
            outputStream.write(c9);
        } catch (OutOfMemoryError unused2) {
        }
    }

    @Override // p3.a
    public int getType() {
        return this.f22525a;
    }
}
