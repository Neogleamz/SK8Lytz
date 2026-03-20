package androidx.camera.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Size;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import androidx.camera.core.p1;
import androidx.camera.core.z2;
import androidx.camera.view.PreviewView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: g  reason: collision with root package name */
    private static final PreviewView.ScaleType f3003g = PreviewView.ScaleType.FILL_CENTER;

    /* renamed from: a  reason: collision with root package name */
    private Size f3004a;

    /* renamed from: b  reason: collision with root package name */
    private Rect f3005b;

    /* renamed from: c  reason: collision with root package name */
    private int f3006c;

    /* renamed from: d  reason: collision with root package name */
    private int f3007d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f3008e;

    /* renamed from: f  reason: collision with root package name */
    private PreviewView.ScaleType f3009f = f3003g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3010a;

        static {
            int[] iArr = new int[PreviewView.ScaleType.values().length];
            f3010a = iArr;
            try {
                iArr[PreviewView.ScaleType.FIT_CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3010a[PreviewView.ScaleType.FILL_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3010a[PreviewView.ScaleType.FIT_END.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3010a[PreviewView.ScaleType.FILL_END.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3010a[PreviewView.ScaleType.FIT_START.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3010a[PreviewView.ScaleType.FILL_START.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private static RectF b(RectF rectF, float f5) {
        float f8 = f5 + f5;
        return new RectF(f8 - rectF.right, rectF.top, f8 - rectF.left, rectF.bottom);
    }

    private Size e() {
        return androidx.camera.core.impl.utils.n.f(this.f3006c) ? new Size(this.f3005b.height(), this.f3005b.width()) : new Size(this.f3005b.width(), this.f3005b.height());
    }

    private RectF j(Size size, int i8) {
        androidx.core.util.h.j(k());
        Matrix h8 = h(size, i8);
        RectF rectF = new RectF(0.0f, 0.0f, this.f3004a.getWidth(), this.f3004a.getHeight());
        h8.mapRect(rectF);
        return rectF;
    }

    private boolean k() {
        return (this.f3005b == null || this.f3004a == null || this.f3007d == -1) ? false : true;
    }

    private static void n(Matrix matrix, RectF rectF, RectF rectF2, PreviewView.ScaleType scaleType) {
        Matrix.ScaleToFit scaleToFit;
        switch (a.f3010a[scaleType.ordinal()]) {
            case 1:
            case 2:
                scaleToFit = Matrix.ScaleToFit.CENTER;
                break;
            case 3:
            case 4:
                scaleToFit = Matrix.ScaleToFit.END;
                break;
            case 5:
            case 6:
                scaleToFit = Matrix.ScaleToFit.START;
                break;
            default:
                p1.c("PreviewTransform", "Unexpected crop rect: " + scaleType);
                scaleToFit = Matrix.ScaleToFit.FILL;
                break;
        }
        if (scaleType == PreviewView.ScaleType.FIT_CENTER || scaleType == PreviewView.ScaleType.FIT_START || scaleType == PreviewView.ScaleType.FIT_END) {
            matrix.setRectToRect(rectF, rectF2, scaleToFit);
            return;
        }
        matrix.setRectToRect(rectF2, rectF, scaleToFit);
        matrix.invert(matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap a(Bitmap bitmap, Size size, int i8) {
        if (k()) {
            Matrix i9 = i();
            RectF j8 = j(size, i8);
            Bitmap createBitmap = Bitmap.createBitmap(size.getWidth(), size.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Matrix matrix = new Matrix();
            matrix.postConcat(i9);
            matrix.postScale(j8.width() / this.f3004a.getWidth(), j8.height() / this.f3004a.getHeight());
            matrix.postTranslate(j8.left, j8.top);
            canvas.drawBitmap(bitmap, matrix, new Paint(7));
            return createBitmap;
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matrix c(Size size, int i8) {
        if (k()) {
            Matrix matrix = new Matrix();
            h(size, i8).invert(matrix);
            Matrix matrix2 = new Matrix();
            matrix2.setRectToRect(new RectF(0.0f, 0.0f, this.f3004a.getWidth(), this.f3004a.getHeight()), new RectF(0.0f, 0.0f, 1.0f, 1.0f), Matrix.ScaleToFit.FILL);
            matrix.postConcat(matrix2);
            return matrix;
        }
        return null;
    }

    RectF d(Size size, int i8) {
        RectF rectF = new RectF(0.0f, 0.0f, size.getWidth(), size.getHeight());
        Size e8 = e();
        RectF rectF2 = new RectF(0.0f, 0.0f, e8.getWidth(), e8.getHeight());
        Matrix matrix = new Matrix();
        n(matrix, rectF2, rectF, this.f3009f);
        matrix.mapRect(rectF2);
        return i8 == 1 ? b(rectF2, size.getWidth() / 2.0f) : rectF2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreviewView.ScaleType f() {
        return this.f3009f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect g() {
        return this.f3005b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matrix h(Size size, int i8) {
        androidx.core.util.h.j(k());
        Matrix c9 = androidx.camera.core.impl.utils.n.c(new RectF(this.f3005b), l(size) ? new RectF(0.0f, 0.0f, size.getWidth(), size.getHeight()) : d(size, i8), this.f3006c);
        if (this.f3008e) {
            if (androidx.camera.core.impl.utils.n.f(this.f3006c)) {
                c9.preScale(1.0f, -1.0f, this.f3005b.centerX(), this.f3005b.centerY());
            } else {
                c9.preScale(-1.0f, 1.0f, this.f3005b.centerX(), this.f3005b.centerY());
            }
        }
        return c9;
    }

    Matrix i() {
        androidx.core.util.h.j(k());
        RectF rectF = new RectF(0.0f, 0.0f, this.f3004a.getWidth(), this.f3004a.getHeight());
        return androidx.camera.core.impl.utils.n.c(rectF, rectF, -androidx.camera.core.impl.utils.c.b(this.f3007d));
    }

    boolean l(Size size) {
        return androidx.camera.core.impl.utils.n.g(size, true, e(), false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i8, int i9) {
        this.f3006c = i8;
        this.f3007d = i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(PreviewView.ScaleType scaleType) {
        this.f3009f = scaleType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(z2.g gVar, Size size, boolean z4) {
        p1.a("PreviewTransform", "Transformation info set: " + gVar + " " + size + " " + z4);
        this.f3005b = gVar.a();
        this.f3006c = gVar.b();
        this.f3007d = gVar.c();
        this.f3004a = size;
        this.f3008e = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(Size size, int i8, View view) {
        if (size.getHeight() == 0 || size.getWidth() == 0) {
            p1.k("PreviewTransform", "Transform not applied due to PreviewView size: " + size);
        } else if (k()) {
            if (view instanceof TextureView) {
                ((TextureView) view).setTransform(i());
            } else {
                Display display = view.getDisplay();
                if (display != null && display.getRotation() != this.f3007d) {
                    p1.c("PreviewTransform", "Non-display rotation not supported with SurfaceView / PERFORMANCE mode.");
                }
            }
            RectF j8 = j(size, i8);
            view.setPivotX(0.0f);
            view.setPivotY(0.0f);
            view.setScaleX(j8.width() / this.f3004a.getWidth());
            view.setScaleY(j8.height() / this.f3004a.getHeight());
            view.setTranslationX(j8.left - view.getLeft());
            view.setTranslationY(j8.top - view.getTop());
        }
    }
}
