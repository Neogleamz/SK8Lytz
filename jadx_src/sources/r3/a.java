package r3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import c1.d;
import java.io.File;
import java.io.OutputStream;
import kj.f;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements p3.a {
    private final void c(String str, int i8, int i9, int i10, int i11, int i12, String str2) {
        Bitmap decodeFile = BitmapFactory.decodeFile(str, f(i12));
        p.b(decodeFile);
        e(decodeFile, i8, i9, i11, str2, i10);
    }

    private final void d(byte[] bArr, int i8, int i9, int i10, int i11, int i12, String str) {
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, f(i12));
        p.b(decodeByteArray);
        e(decodeByteArray, i8, i9, i11, str, i10);
    }

    private final void e(Bitmap bitmap, int i8, int i9, int i10, String str, int i11) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        s3.a.a("src width = " + width);
        s3.a.a("src height = " + height);
        float a9 = n3.a.a(bitmap, i8, i9);
        s3.a.a("scale = " + a9);
        float f5 = width / a9;
        float f8 = height / a9;
        s3.a.a("dst width = " + f5);
        s3.a.a("dst height = " + f8);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) f5, (int) f8, true);
        p.d(createScaledBitmap, "createScaledBitmap(...)");
        Bitmap f9 = n3.a.f(createScaledBitmap, i10);
        d a10 = new d.b(str, f9.getWidth(), f9.getHeight(), 2).c(i11).b(1).a();
        a10.i();
        a10.a(f9);
        a10.j(5000L);
        a10.close();
    }

    private final BitmapFactory.Options f(int i8) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = i8;
        if (Build.VERSION.SDK_INT < 23) {
            options.inDither = true;
        }
        return options;
    }

    @Override // p3.a
    public void a(Context context, byte[] bArr, OutputStream outputStream, int i8, int i9, int i10, int i11, boolean z4, int i12) {
        p.e(context, "context");
        p.e(bArr, "byteArray");
        p.e(outputStream, "outputStream");
        File a9 = t3.a.f22848a.a(context);
        String absolutePath = a9.getAbsolutePath();
        p.d(absolutePath, "getAbsolutePath(...)");
        d(bArr, i8, i9, i10, i11, i12, absolutePath);
        outputStream.write(f.a(a9));
    }

    @Override // p3.a
    public void b(Context context, String str, OutputStream outputStream, int i8, int i9, int i10, int i11, boolean z4, int i12, int i13) {
        p.e(context, "context");
        p.e(str, "path");
        p.e(outputStream, "outputStream");
        File a9 = t3.a.f22848a.a(context);
        String absolutePath = a9.getAbsolutePath();
        p.d(absolutePath, "getAbsolutePath(...)");
        c(str, i8, i9, i10, i11, i12, absolutePath);
        outputStream.write(f.a(a9));
    }

    @Override // p3.a
    public int getType() {
        return 2;
    }
}
