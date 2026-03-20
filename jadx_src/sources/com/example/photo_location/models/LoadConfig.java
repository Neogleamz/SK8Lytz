package com.example.photo_location.models;

import android.content.Context;
import com.google.gson.e;
import java.lang.reflect.Array;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LoadConfig {
    private static final String TAG = "com.example.photo_location.models.LoadConfig";
    private static e gson = new e();

    public static int[][] large(Context context) {
        return readFileWithInvert(context, "large.json");
    }

    public static int[][] medium(Context context) {
        return readFileWithInvert(context, "medium.json");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:60:0x006d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0077 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r4v0, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r4v9, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int[][] readFile(android.content.Context r4, java.lang.String r5) {
        /*
            r0 = 0
            android.content.res.AssetManager r4 = r4.getAssets()     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L48
            java.io.InputStream r4 = r4.open(r5)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L48
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r5]     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L42
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L42
            r1.<init>()     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L42
        L12:
            int r2 = r4.read(r5)     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            r3 = -1
            if (r2 == r3) goto L1e
            r3 = 0
            r1.write(r5, r3, r2)     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            goto L12
        L1e:
            java.lang.String r5 = r1.toString()     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            com.google.gson.e r2 = com.example.photo_location.models.LoadConfig.gson     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            java.lang.Class<int[][]> r3 = int[][].class
            java.lang.Object r5 = r2.l(r5, r3)     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            int[][] r5 = (int[][]) r5     // Catch: java.io.IOException -> L3d java.lang.Throwable -> L69
            r4.close()     // Catch: java.io.IOException -> L30
            goto L34
        L30:
            r4 = move-exception
            r4.printStackTrace()
        L34:
            r1.close()     // Catch: java.io.IOException -> L38
            goto L3c
        L38:
            r4 = move-exception
            r4.printStackTrace()
        L3c:
            return r5
        L3d:
            r5 = move-exception
            goto L4b
        L3f:
            r5 = move-exception
            r1 = r0
            goto L6a
        L42:
            r5 = move-exception
            r1 = r0
            goto L4b
        L45:
            r5 = move-exception
            r1 = r0
            goto L6b
        L48:
            r5 = move-exception
            r4 = r0
            r1 = r4
        L4b:
            java.lang.String r2 = com.example.photo_location.models.LoadConfig.TAG     // Catch: java.lang.Throwable -> L69
            java.lang.String r5 = r5.getMessage()     // Catch: java.lang.Throwable -> L69
            android.util.Log.e(r2, r5)     // Catch: java.lang.Throwable -> L69
            if (r4 == 0) goto L5e
            r4.close()     // Catch: java.io.IOException -> L5a
            goto L5e
        L5a:
            r4 = move-exception
            r4.printStackTrace()
        L5e:
            if (r1 == 0) goto L68
            r1.close()     // Catch: java.io.IOException -> L64
            goto L68
        L64:
            r4 = move-exception
            r4.printStackTrace()
        L68:
            return r0
        L69:
            r5 = move-exception
        L6a:
            r0 = r4
        L6b:
            if (r0 == 0) goto L75
            r0.close()     // Catch: java.io.IOException -> L71
            goto L75
        L71:
            r4 = move-exception
            r4.printStackTrace()
        L75:
            if (r1 == 0) goto L7f
            r1.close()     // Catch: java.io.IOException -> L7b
            goto L7f
        L7b:
            r4 = move-exception
            r4.printStackTrace()
        L7f:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.photo_location.models.LoadConfig.readFile(android.content.Context, java.lang.String):int[][]");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0075 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x007f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v0, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v9, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int[][] readFileWithInvert(android.content.Context r5, java.lang.String r6) {
        /*
            r0 = 0
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L50
            java.io.InputStream r5 = r5.open(r6)     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L50
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r6]     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            r1.<init>()     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
        L12:
            int r2 = r5.read(r6)     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            r3 = -1
            r4 = 0
            if (r2 == r3) goto L1e
            r1.write(r6, r4, r2)     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            goto L12
        L1e:
            java.lang.String r6 = r1.toString()     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            com.google.gson.e r2 = com.example.photo_location.models.LoadConfig.gson     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            java.lang.Class<int[][]> r3 = int[][].class
            java.lang.Object r6 = r2.l(r6, r3)     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            int[][] r6 = (int[][]) r6     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            int r2 = r6.length     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            r3 = r6[r4]     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            int r3 = r3.length     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            int[][] r6 = transpose(r6, r2, r3)     // Catch: java.io.IOException -> L45 java.lang.Throwable -> L71
            r5.close()     // Catch: java.io.IOException -> L38
            goto L3c
        L38:
            r5 = move-exception
            r5.printStackTrace()
        L3c:
            r1.close()     // Catch: java.io.IOException -> L40
            goto L44
        L40:
            r5 = move-exception
            r5.printStackTrace()
        L44:
            return r6
        L45:
            r6 = move-exception
            goto L53
        L47:
            r6 = move-exception
            r1 = r0
            goto L72
        L4a:
            r6 = move-exception
            r1 = r0
            goto L53
        L4d:
            r6 = move-exception
            r1 = r0
            goto L73
        L50:
            r6 = move-exception
            r5 = r0
            r1 = r5
        L53:
            java.lang.String r2 = com.example.photo_location.models.LoadConfig.TAG     // Catch: java.lang.Throwable -> L71
            java.lang.String r6 = r6.getMessage()     // Catch: java.lang.Throwable -> L71
            android.util.Log.e(r2, r6)     // Catch: java.lang.Throwable -> L71
            if (r5 == 0) goto L66
            r5.close()     // Catch: java.io.IOException -> L62
            goto L66
        L62:
            r5 = move-exception
            r5.printStackTrace()
        L66:
            if (r1 == 0) goto L70
            r1.close()     // Catch: java.io.IOException -> L6c
            goto L70
        L6c:
            r5 = move-exception
            r5.printStackTrace()
        L70:
            return r0
        L71:
            r6 = move-exception
        L72:
            r0 = r5
        L73:
            if (r0 == 0) goto L7d
            r0.close()     // Catch: java.io.IOException -> L79
            goto L7d
        L79:
            r5 = move-exception
            r5.printStackTrace()
        L7d:
            if (r1 == 0) goto L87
            r1.close()     // Catch: java.io.IOException -> L83
            goto L87
        L83:
            r5 = move-exception
            r5.printStackTrace()
        L87:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.photo_location.models.LoadConfig.readFileWithInvert(android.content.Context, java.lang.String):int[][]");
    }

    public static int[][] small(Context context) {
        return readFileWithInvert(context, "small.json");
    }

    public static int[][] transpose(int[][] iArr, int i8, int i9) {
        int[][] iArr2 = (int[][]) Array.newInstance(int.class, i9, i8);
        for (int i10 = 0; i10 < i8; i10++) {
            for (int i11 = 0; i11 < i9; i11++) {
                iArr2[i11][i10] = iArr[i10][i11];
            }
        }
        return iArr2;
    }
}
