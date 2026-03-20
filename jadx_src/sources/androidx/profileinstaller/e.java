package androidx.profileinstaller;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8) {
        return (((i8 + 8) - 1) & (-8)) / 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] b(byte[] bArr) {
        Deflater deflater = new Deflater(1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
            deflaterOutputStream.write(bArr);
            deflaterOutputStream.close();
            deflater.end();
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable th) {
            deflater.end();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RuntimeException c(String str) {
        return new IllegalStateException(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] d(InputStream inputStream, int i8) {
        byte[] bArr = new byte[i8];
        int i9 = 0;
        while (i9 < i8) {
            int read = inputStream.read(bArr, i9, i8 - i9);
            if (read < 0) {
                throw c("Not enough bytes to read: " + i8);
            }
            i9 += read;
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0059, code lost:
        if (r0.finished() == false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005e, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0065, code lost:
        throw c("Inflater did not finish");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static byte[] e(java.io.InputStream r8, int r9, int r10) {
        /*
            java.util.zip.Inflater r0 = new java.util.zip.Inflater
            r0.<init>()
            byte[] r1 = new byte[r10]     // Catch: java.lang.Throwable -> L84
            r2 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L84
            r3 = 0
            r4 = r3
            r5 = r4
        Le:
            boolean r6 = r0.finished()     // Catch: java.lang.Throwable -> L84
            if (r6 != 0) goto L53
            boolean r6 = r0.needsDictionary()     // Catch: java.lang.Throwable -> L84
            if (r6 != 0) goto L53
            if (r4 >= r9) goto L53
            int r6 = r8.read(r2)     // Catch: java.lang.Throwable -> L84
            if (r6 < 0) goto L38
            r0.setInput(r2, r3, r6)     // Catch: java.lang.Throwable -> L84
            int r7 = r10 - r5
            int r7 = r0.inflate(r1, r5, r7)     // Catch: java.util.zip.DataFormatException -> L2e java.lang.Throwable -> L84
            int r5 = r5 + r7
            int r4 = r4 + r6
            goto Le
        L2e:
            r8 = move-exception
            java.lang.String r8 = r8.getMessage()     // Catch: java.lang.Throwable -> L84
            java.lang.RuntimeException r8 = c(r8)     // Catch: java.lang.Throwable -> L84
            throw r8     // Catch: java.lang.Throwable -> L84
        L38:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L84
            r8.<init>()     // Catch: java.lang.Throwable -> L84
            java.lang.String r10 = "Invalid zip data. Stream ended after $totalBytesRead bytes. Expected "
            r8.append(r10)     // Catch: java.lang.Throwable -> L84
            r8.append(r9)     // Catch: java.lang.Throwable -> L84
            java.lang.String r9 = " bytes"
            r8.append(r9)     // Catch: java.lang.Throwable -> L84
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L84
            java.lang.RuntimeException r8 = c(r8)     // Catch: java.lang.Throwable -> L84
            throw r8     // Catch: java.lang.Throwable -> L84
        L53:
            if (r4 != r9) goto L66
            boolean r8 = r0.finished()     // Catch: java.lang.Throwable -> L84
            if (r8 == 0) goto L5f
            r0.end()
            return r1
        L5f:
            java.lang.String r8 = "Inflater did not finish"
            java.lang.RuntimeException r8 = c(r8)     // Catch: java.lang.Throwable -> L84
            throw r8     // Catch: java.lang.Throwable -> L84
        L66:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L84
            r8.<init>()     // Catch: java.lang.Throwable -> L84
            java.lang.String r10 = "Didn't read enough bytes during decompression. expected="
            r8.append(r10)     // Catch: java.lang.Throwable -> L84
            r8.append(r9)     // Catch: java.lang.Throwable -> L84
            java.lang.String r9 = " actual="
            r8.append(r9)     // Catch: java.lang.Throwable -> L84
            r8.append(r4)     // Catch: java.lang.Throwable -> L84
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L84
            java.lang.RuntimeException r8 = c(r8)     // Catch: java.lang.Throwable -> L84
            throw r8     // Catch: java.lang.Throwable -> L84
        L84:
            r8 = move-exception
            r0.end()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.profileinstaller.e.e(java.io.InputStream, int, int):byte[]");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String f(InputStream inputStream, int i8) {
        return new String(d(inputStream, i8), StandardCharsets.UTF_8);
    }

    static long g(InputStream inputStream, int i8) {
        byte[] d8 = d(inputStream, i8);
        long j8 = 0;
        for (int i9 = 0; i9 < i8; i9++) {
            j8 += (d8[i9] & 255) << (i9 * 8);
        }
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h(InputStream inputStream) {
        return (int) g(inputStream, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long i(InputStream inputStream) {
        return g(inputStream, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int j(InputStream inputStream) {
        return (int) g(inputStream, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int k(String str) {
        return str.getBytes(StandardCharsets.UTF_8).length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void l(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[RecognitionOptions.UPC_A];
        while (true) {
            int read = inputStream.read(bArr);
            if (read <= 0) {
                return;
            }
            outputStream.write(bArr, 0, read);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void m(OutputStream outputStream, byte[] bArr) {
        q(outputStream, bArr.length);
        byte[] b9 = b(bArr);
        q(outputStream, b9.length);
        outputStream.write(b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void n(OutputStream outputStream, String str) {
        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
    }

    static void o(OutputStream outputStream, long j8, int i8) {
        byte[] bArr = new byte[i8];
        for (int i9 = 0; i9 < i8; i9++) {
            bArr[i9] = (byte) ((j8 >> (i9 * 8)) & 255);
        }
        outputStream.write(bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void p(OutputStream outputStream, int i8) {
        o(outputStream, i8, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void q(OutputStream outputStream, long j8) {
        o(outputStream, j8, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void r(OutputStream outputStream, int i8) {
        o(outputStream, i8, 1);
    }
}
