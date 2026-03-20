package androidx.exifinterface.media;

import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.Os;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(FileDescriptor fileDescriptor) {
            Os.close(fileDescriptor);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static FileDescriptor b(FileDescriptor fileDescriptor) {
            return Os.dup(fileDescriptor);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static long c(FileDescriptor fileDescriptor, long j8, int i8) {
            return Os.lseek(fileDescriptor, j8, i8);
        }
    }

    /* renamed from: androidx.exifinterface.media.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0053b {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static void a(MediaMetadataRetriever mediaMetadataRetriever, MediaDataSource mediaDataSource) {
            mediaMetadataRetriever.setDataSource(mediaDataSource);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(FileDescriptor fileDescriptor) {
        String str;
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                a.a(fileDescriptor);
                return;
            } catch (Exception unused) {
                str = "Error closing fd.";
            }
        } else {
            str = "closeFileDescriptor is called in API < 21, which must be wrong.";
        }
        Log.e("ExifInterfaceUtils", str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e8) {
                throw e8;
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long[] c(Object obj) {
        if (!(obj instanceof int[])) {
            if (obj instanceof long[]) {
                return (long[]) obj;
            }
            return null;
        }
        int[] iArr = (int[]) obj;
        long[] jArr = new long[iArr.length];
        for (int i8 = 0; i8 < iArr.length; i8++) {
            jArr[i8] = iArr[i8];
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[8192];
        int i8 = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return i8;
            }
            i8 += read;
            outputStream.write(bArr, 0, read);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void e(InputStream inputStream, OutputStream outputStream, int i8) {
        byte[] bArr = new byte[8192];
        while (i8 > 0) {
            int min = Math.min(i8, 8192);
            int read = inputStream.read(bArr, 0, min);
            if (read != min) {
                throw new IOException("Failed to copy the given amount of bytes from the inputstream to the output stream.");
            }
            i8 -= read;
            outputStream.write(bArr, 0, read);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i8 = 0; i8 < bArr2.length; i8++) {
            if (bArr[i8] != bArr2[i8]) {
                return false;
            }
        }
        return true;
    }
}
