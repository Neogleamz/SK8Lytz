package x;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Size;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.l1;
import java.io.IOException;
import x.z;
import y.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t implements g0.d<z.b, g0.e<l1>> {
    private static Matrix b(int i8, Size size, int i9) {
        int i10 = i8 - i9;
        Size size2 = androidx.camera.core.impl.utils.n.f(androidx.camera.core.impl.utils.n.p(i10)) ? new Size(size.getHeight(), size.getWidth()) : size;
        return androidx.camera.core.impl.utils.n.c(new RectF(0.0f, 0.0f, size2.getWidth(), size2.getHeight()), new RectF(0.0f, 0.0f, size.getWidth(), size.getHeight()), i10);
    }

    private static Rect c(Rect rect, Matrix matrix) {
        RectF rectF = new RectF(rect);
        matrix.mapRect(rectF);
        Rect rect2 = new Rect();
        rectF.round(rect2);
        return rect2;
    }

    private static Matrix d(Matrix matrix, Matrix matrix2) {
        Matrix matrix3 = new Matrix(matrix);
        matrix3.postConcat(matrix2);
        return matrix3;
    }

    private static boolean e(androidx.camera.core.impl.utils.f fVar, l1 l1Var) {
        return fVar.p() == l1Var.getWidth() && fVar.k() == l1Var.getHeight();
    }

    @Override // g0.d
    /* renamed from: a */
    public g0.e<l1> apply(z.b bVar) {
        androidx.camera.core.impl.utils.f g8;
        Matrix matrix;
        int i8;
        l1 a9 = bVar.a();
        a0 b9 = bVar.b();
        if (a9.getFormat() == 256) {
            try {
                g8 = androidx.camera.core.impl.utils.f.g(a9);
                a9.A()[0].b().rewind();
            } catch (IOException e8) {
                throw new ImageCaptureException(1, "Failed to extract EXIF data.", e8);
            }
        } else {
            g8 = null;
        }
        j f5 = ((b0.c) a9.e1()).f();
        Rect a10 = b9.a();
        Matrix e9 = b9.e();
        int d8 = b9.d();
        if (o.f23735g.b(a9)) {
            androidx.core.util.h.i(g8, "The image must have JPEG exif.");
            androidx.core.util.h.k(e(g8, a9), "Exif size does not match image size.");
            Matrix b10 = b(b9.d(), new Size(g8.p(), g8.k()), g8.n());
            Rect c9 = c(b9.a(), b10);
            matrix = d(b9.e(), b10);
            i8 = g8.n();
            a10 = c9;
        } else {
            matrix = e9;
            i8 = d8;
        }
        return g0.e.k(a9, g8, a10, i8, matrix, f5);
    }
}
