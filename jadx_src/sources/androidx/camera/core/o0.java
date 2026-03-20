package androidx.camera.core;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ImageWriter;
import android.os.Build;
import androidx.camera.core.l0;
import androidx.concurrent.futures.c;
import androidx.core.os.OperationCanceledException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o0 implements g0.a {

    /* renamed from: a  reason: collision with root package name */
    private l0.a f2743a;

    /* renamed from: b  reason: collision with root package name */
    private volatile int f2744b;

    /* renamed from: c  reason: collision with root package name */
    private volatile int f2745c;

    /* renamed from: e  reason: collision with root package name */
    private volatile boolean f2747e;

    /* renamed from: f  reason: collision with root package name */
    private volatile boolean f2748f;

    /* renamed from: g  reason: collision with root package name */
    private Executor f2749g;

    /* renamed from: h  reason: collision with root package name */
    private m2 f2750h;

    /* renamed from: i  reason: collision with root package name */
    private ImageWriter f2751i;

    /* renamed from: n  reason: collision with root package name */
    ByteBuffer f2756n;

    /* renamed from: o  reason: collision with root package name */
    ByteBuffer f2757o;

    /* renamed from: p  reason: collision with root package name */
    ByteBuffer f2758p;
    ByteBuffer q;

    /* renamed from: d  reason: collision with root package name */
    private volatile int f2746d = 1;

    /* renamed from: j  reason: collision with root package name */
    private Rect f2752j = new Rect();

    /* renamed from: k  reason: collision with root package name */
    private Rect f2753k = new Rect();

    /* renamed from: l  reason: collision with root package name */
    private Matrix f2754l = new Matrix();

    /* renamed from: m  reason: collision with root package name */
    private Matrix f2755m = new Matrix();

    /* renamed from: r  reason: collision with root package name */
    private final Object f2759r = new Object();

    /* renamed from: s  reason: collision with root package name */
    protected boolean f2760s = true;

    private void h(l1 l1Var) {
        if (this.f2746d != 1) {
            if (this.f2746d == 2 && this.f2756n == null) {
                this.f2756n = ByteBuffer.allocateDirect(l1Var.getWidth() * l1Var.getHeight() * 4);
                return;
            }
            return;
        }
        if (this.f2757o == null) {
            this.f2757o = ByteBuffer.allocateDirect(l1Var.getWidth() * l1Var.getHeight());
        }
        this.f2757o.position(0);
        if (this.f2758p == null) {
            this.f2758p = ByteBuffer.allocateDirect((l1Var.getWidth() * l1Var.getHeight()) / 4);
        }
        this.f2758p.position(0);
        if (this.q == null) {
            this.q = ByteBuffer.allocateDirect((l1Var.getWidth() * l1Var.getHeight()) / 4);
        }
        this.q.position(0);
    }

    private static m2 i(int i8, int i9, int i10, int i11, int i12) {
        boolean z4 = i10 == 90 || i10 == 270;
        int i13 = z4 ? i9 : i8;
        if (!z4) {
            i8 = i9;
        }
        return new m2(n1.a(i13, i8, i11, i12));
    }

    static Matrix k(int i8, int i9, int i10, int i11, int i12) {
        Matrix matrix = new Matrix();
        if (i12 > 0) {
            matrix.setRectToRect(new RectF(0.0f, 0.0f, i8, i9), androidx.camera.core.impl.utils.n.f2658a, Matrix.ScaleToFit.FILL);
            matrix.postRotate(i12);
            matrix.postConcat(androidx.camera.core.impl.utils.n.b(new RectF(0.0f, 0.0f, i10, i11)));
        }
        return matrix;
    }

    static Rect l(Rect rect, Matrix matrix) {
        RectF rectF = new RectF(rect);
        matrix.mapRect(rectF);
        Rect rect2 = new Rect();
        rectF.round(rect2);
        return rect2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m(l1 l1Var, Matrix matrix, l1 l1Var2, Rect rect, l0.a aVar, c.a aVar2) {
        if (!this.f2760s) {
            aVar2.f(new OperationCanceledException("ImageAnalysis is detached"));
            return;
        }
        n2 n2Var = new n2(l1Var2, o1.f(l1Var.e1().a(), l1Var.e1().d(), this.f2747e ? 0 : this.f2744b, matrix));
        if (!rect.isEmpty()) {
            n2Var.Y0(rect);
        }
        aVar.b(n2Var);
        aVar2.c(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object n(Executor executor, final l1 l1Var, final Matrix matrix, final l1 l1Var2, final Rect rect, final l0.a aVar, final c.a aVar2) {
        executor.execute(new Runnable() { // from class: androidx.camera.core.n0
            @Override // java.lang.Runnable
            public final void run() {
                o0.this.m(l1Var, matrix, l1Var2, rect, aVar, aVar2);
            }
        });
        return "analyzeImage";
    }

    private void p(int i8, int i9, int i10, int i11) {
        Matrix k8 = k(i8, i9, i10, i11, this.f2744b);
        this.f2753k = l(this.f2752j, k8);
        this.f2755m.setConcat(this.f2754l, k8);
    }

    private void q(l1 l1Var, int i8) {
        m2 m2Var = this.f2750h;
        if (m2Var == null) {
            return;
        }
        m2Var.k();
        this.f2750h = i(l1Var.getWidth(), l1Var.getHeight(), i8, this.f2750h.c(), this.f2750h.e());
        if (Build.VERSION.SDK_INT < 23 || this.f2746d != 1) {
            return;
        }
        ImageWriter imageWriter = this.f2751i;
        if (imageWriter != null) {
            c0.a.a(imageWriter);
        }
        this.f2751i = c0.a.c(this.f2750h.getSurface(), this.f2750h.e());
    }

    @Override // y.g0.a
    public void a(y.g0 g0Var) {
        try {
            l1 d8 = d(g0Var);
            if (d8 != null) {
                o(d8);
            }
        } catch (IllegalStateException e8) {
            p1.d("ImageAnalysisAnalyzer", "Failed to acquire image.", e8);
        }
    }

    abstract l1 d(y.g0 g0Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.common.util.concurrent.d<java.lang.Void> e(final androidx.camera.core.l1 r17) {
        /*
            Method dump skipped, instructions count: 203
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.o0.e(androidx.camera.core.l1):com.google.common.util.concurrent.d");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        this.f2760s = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void g();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        this.f2760s = false;
        g();
    }

    abstract void o(l1 l1Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(Executor executor, l0.a aVar) {
        if (aVar == null) {
            g();
        }
        synchronized (this.f2759r) {
            this.f2743a = aVar;
            this.f2749g = executor;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(boolean z4) {
        this.f2748f = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int i8) {
        this.f2746d = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(boolean z4) {
        this.f2747e = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(m2 m2Var) {
        synchronized (this.f2759r) {
            this.f2750h = m2Var;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(int i8) {
        this.f2744b = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(Matrix matrix) {
        synchronized (this.f2759r) {
            this.f2754l = matrix;
            this.f2755m = new Matrix(this.f2754l);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(Rect rect) {
        synchronized (this.f2759r) {
            this.f2752j = rect;
            this.f2753k = new Rect(this.f2752j);
        }
    }
}
