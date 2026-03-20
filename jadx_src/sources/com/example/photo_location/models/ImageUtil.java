package com.example.photo_location.models;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImageUtil {
    public static Bitmap captureImage(Bitmap bitmap) {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public static List<Bitmap> captureImages(List<Bitmap> list) {
        ArrayList arrayList = new ArrayList();
        for (Bitmap bitmap : list) {
            captureImage(bitmap);
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0045 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.graphics.Bitmap] */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v5, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void saveImageToInternalStorage(android.content.Context r2, android.graphics.Bitmap r3, java.lang.String r4) {
        /*
            java.io.File r2 = r2.getFilesDir()
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            java.lang.String r4 = ".jpg"
            r1.append(r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r2, r4)
            r2 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L31
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L31
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.PNG     // Catch: java.io.IOException -> L2b java.lang.Throwable -> L42
            r0 = 100
            r3.compress(r2, r0, r4)     // Catch: java.io.IOException -> L2b java.lang.Throwable -> L42
            r4.close()     // Catch: java.io.IOException -> L3d
            goto L41
        L2b:
            r2 = move-exception
            goto L34
        L2d:
            r3 = move-exception
            r4 = r2
            r2 = r3
            goto L43
        L31:
            r3 = move-exception
            r4 = r2
            r2 = r3
        L34:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L42
            if (r4 == 0) goto L41
            r4.close()     // Catch: java.io.IOException -> L3d
            goto L41
        L3d:
            r2 = move-exception
            r2.printStackTrace()
        L41:
            return
        L42:
            r2 = move-exception
        L43:
            if (r4 == 0) goto L4d
            r4.close()     // Catch: java.io.IOException -> L49
            goto L4d
        L49:
            r3 = move-exception
            r3.printStackTrace()
        L4d:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.photo_location.models.ImageUtil.saveImageToInternalStorage(android.content.Context, android.graphics.Bitmap, java.lang.String):void");
    }
}
