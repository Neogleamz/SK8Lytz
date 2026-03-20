package g0;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.utils.f;
import androidx.camera.core.impl.utils.n;
import androidx.camera.core.l1;
import androidx.core.util.h;
import y.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e<T> {
    public static e<Bitmap> j(Bitmap bitmap, f fVar, Rect rect, int i8, Matrix matrix, j jVar) {
        return new a(bitmap, fVar, 42, new Size(bitmap.getWidth(), bitmap.getHeight()), rect, i8, matrix, jVar);
    }

    public static e<l1> k(l1 l1Var, f fVar, Rect rect, int i8, Matrix matrix, j jVar) {
        if (l1Var.getFormat() == 256) {
            h.i(fVar, "JPEG image must have Exif.");
        }
        return new a(l1Var, fVar, l1Var.getFormat(), new Size(l1Var.getWidth(), l1Var.getHeight()), rect, i8, matrix, jVar);
    }

    public static e<byte[]> l(byte[] bArr, f fVar, int i8, Size size, Rect rect, int i9, Matrix matrix, j jVar) {
        return new a(bArr, fVar, i8, size, rect, i9, matrix, jVar);
    }

    public abstract j a();

    public abstract Rect b();

    public abstract T c();

    public abstract f d();

    public abstract int e();

    public abstract int f();

    public abstract Matrix g();

    public abstract Size h();

    public boolean i() {
        return n.e(b(), h());
    }
}
