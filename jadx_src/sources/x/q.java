package x;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.e1;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class q implements g0.d<a, e1.n> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static a c(g0.e<byte[]> eVar, e1.m mVar) {
            return new e(eVar, mVar);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract e1.m a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.e<byte[]> b();
    }

    private static Uri b(File file, File file2) {
        if (file2.exists()) {
            file2.delete();
        }
        if (file.renameTo(file2)) {
            return Uri.fromFile(file2);
        }
        throw new ImageCaptureException(1, "Failed to overwrite the file: " + file2.getAbsolutePath(), null);
    }

    private static Uri c(File file, e1.m mVar) {
        ContentResolver a9 = mVar.a();
        Objects.requireNonNull(a9);
        ContentValues contentValues = mVar.b() != null ? new ContentValues(mVar.b()) : new ContentValues();
        k(contentValues, 1);
        Uri insert = a9.insert(mVar.f(), contentValues);
        if (insert != null) {
            try {
                try {
                    f(file, insert, a9);
                    return insert;
                } catch (IOException e8) {
                    throw new ImageCaptureException(1, "Failed to write to MediaStore URI: " + insert, e8);
                }
            } finally {
                m(insert, a9, 0);
            }
        }
        throw new ImageCaptureException(1, "Failed to insert a MediaStore URI.", null);
    }

    private static void d(File file, OutputStream outputStream) {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            byte[] bArr = new byte[RecognitionOptions.UPC_E];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    fileInputStream.close();
                    return;
                }
                outputStream.write(bArr, 0, read);
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static Uri e(File file, e1.m mVar) {
        if (i(mVar)) {
            return c(file, mVar);
        }
        if (!j(mVar)) {
            if (h(mVar)) {
                File c9 = mVar.c();
                Objects.requireNonNull(c9);
                return b(file, c9);
            }
            throw new ImageCaptureException(0, "Invalid OutputFileOptions", null);
        }
        try {
            OutputStream e8 = mVar.e();
            Objects.requireNonNull(e8);
            OutputStream outputStream = e8;
            d(file, e8);
            return null;
        } catch (IOException unused) {
            throw new ImageCaptureException(1, "Failed to write to OutputStream.", null);
        }
    }

    private static void f(File file, Uri uri, ContentResolver contentResolver) {
        OutputStream openOutputStream = contentResolver.openOutputStream(uri);
        try {
            if (openOutputStream != null) {
                d(file, openOutputStream);
                openOutputStream.close();
                return;
            }
            throw new FileNotFoundException(uri + " cannot be resolved.");
        } catch (Throwable th) {
            if (openOutputStream != null) {
                try {
                    openOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static File g(e1.m mVar) {
        try {
            File c9 = mVar.c();
            if (c9 != null) {
                String parent = c9.getParent();
                return new File(parent, "CameraX" + UUID.randomUUID().toString() + ".tmp");
            }
            return File.createTempFile("CameraX", ".tmp");
        } catch (IOException e8) {
            throw new ImageCaptureException(1, "Failed to create temp file.", e8);
        }
    }

    private static boolean h(e1.m mVar) {
        return mVar.c() != null;
    }

    private static boolean i(e1.m mVar) {
        return (mVar.f() == null || mVar.a() == null || mVar.b() == null) ? false : true;
    }

    private static boolean j(e1.m mVar) {
        return mVar.e() != null;
    }

    private static void k(ContentValues contentValues, int i8) {
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("is_pending", Integer.valueOf(i8));
        }
    }

    private static void l(File file, androidx.camera.core.impl.utils.f fVar, e1.m mVar, int i8) {
        try {
            androidx.camera.core.impl.utils.f e8 = androidx.camera.core.impl.utils.f.e(file);
            fVar.d(e8);
            if (e8.n() == 0 && i8 != 0) {
                e8.u(i8);
            }
            mVar.d();
            throw null;
        } catch (IOException e9) {
            throw new ImageCaptureException(1, "Failed to update Exif data", e9);
        }
    }

    private static void m(Uri uri, ContentResolver contentResolver, int i8) {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = new ContentValues();
            k(contentValues, i8);
            contentResolver.update(uri, contentValues, null, null);
        }
    }

    private static void n(File file, byte[] bArr) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
        } catch (IOException e8) {
            throw new ImageCaptureException(1, "Failed to write to temp file", e8);
        }
    }

    @Override // g0.d
    /* renamed from: a */
    public e1.n apply(a aVar) {
        g0.e<byte[]> b9 = aVar.b();
        e1.m a9 = aVar.a();
        File g8 = g(a9);
        n(g8, b9.c());
        androidx.camera.core.impl.utils.f d8 = b9.d();
        Objects.requireNonNull(d8);
        l(g8, d8, a9, b9.f());
        return new e1.n(e(g8, a9));
    }
}
