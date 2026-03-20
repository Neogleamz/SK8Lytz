package x7;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {

    /* renamed from: a  reason: collision with root package name */
    private final o[] f24236a = new o[4];

    /* renamed from: b  reason: collision with root package name */
    private final Matrix[] f24237b = new Matrix[4];

    /* renamed from: c  reason: collision with root package name */
    private final Matrix[] f24238c = new Matrix[4];

    /* renamed from: d  reason: collision with root package name */
    private final PointF f24239d = new PointF();

    /* renamed from: e  reason: collision with root package name */
    private final Path f24240e = new Path();

    /* renamed from: f  reason: collision with root package name */
    private final Path f24241f = new Path();

    /* renamed from: g  reason: collision with root package name */
    private final o f24242g = new o();

    /* renamed from: h  reason: collision with root package name */
    private final float[] f24243h = new float[2];

    /* renamed from: i  reason: collision with root package name */
    private final float[] f24244i = new float[2];

    /* renamed from: j  reason: collision with root package name */
    private final Path f24245j = new Path();

    /* renamed from: k  reason: collision with root package name */
    private final Path f24246k = new Path();

    /* renamed from: l  reason: collision with root package name */
    private boolean f24247l = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        static final n f24248a = new n();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(o oVar, Matrix matrix, int i8);

        void b(o oVar, Matrix matrix, int i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final m f24249a;

        /* renamed from: b  reason: collision with root package name */
        public final Path f24250b;

        /* renamed from: c  reason: collision with root package name */
        public final RectF f24251c;

        /* renamed from: d  reason: collision with root package name */
        public final b f24252d;

        /* renamed from: e  reason: collision with root package name */
        public final float f24253e;

        c(m mVar, float f5, RectF rectF, b bVar, Path path) {
            this.f24252d = bVar;
            this.f24249a = mVar;
            this.f24253e = f5;
            this.f24251c = rectF;
            this.f24250b = path;
        }
    }

    public n() {
        for (int i8 = 0; i8 < 4; i8++) {
            this.f24236a[i8] = new o();
            this.f24237b[i8] = new Matrix();
            this.f24238c[i8] = new Matrix();
        }
    }

    private float a(int i8) {
        return (i8 + 1) * 90;
    }

    private void b(c cVar, int i8) {
        this.f24243h[0] = this.f24236a[i8].k();
        this.f24243h[1] = this.f24236a[i8].l();
        this.f24237b[i8].mapPoints(this.f24243h);
        Path path = cVar.f24250b;
        float[] fArr = this.f24243h;
        if (i8 == 0) {
            path.moveTo(fArr[0], fArr[1]);
        } else {
            path.lineTo(fArr[0], fArr[1]);
        }
        this.f24236a[i8].d(this.f24237b[i8], cVar.f24250b);
        b bVar = cVar.f24252d;
        if (bVar != null) {
            bVar.b(this.f24236a[i8], this.f24237b[i8], i8);
        }
    }

    private void c(c cVar, int i8) {
        o oVar;
        Matrix matrix;
        Path path;
        int i9 = (i8 + 1) % 4;
        this.f24243h[0] = this.f24236a[i8].i();
        this.f24243h[1] = this.f24236a[i8].j();
        this.f24237b[i8].mapPoints(this.f24243h);
        this.f24244i[0] = this.f24236a[i9].k();
        this.f24244i[1] = this.f24236a[i9].l();
        this.f24237b[i9].mapPoints(this.f24244i);
        float[] fArr = this.f24243h;
        float f5 = fArr[0];
        float[] fArr2 = this.f24244i;
        float max = Math.max(((float) Math.hypot(f5 - fArr2[0], fArr[1] - fArr2[1])) - 0.001f, 0.0f);
        float i10 = i(cVar.f24251c, i8);
        this.f24242g.n(0.0f, 0.0f);
        f j8 = j(i8, cVar.f24249a);
        j8.c(max, i10, cVar.f24253e, this.f24242g);
        this.f24245j.reset();
        this.f24242g.d(this.f24238c[i8], this.f24245j);
        if (this.f24247l && Build.VERSION.SDK_INT >= 19 && (j8.b() || l(this.f24245j, i8) || l(this.f24245j, i9))) {
            Path path2 = this.f24245j;
            path2.op(path2, this.f24241f, Path.Op.DIFFERENCE);
            this.f24243h[0] = this.f24242g.k();
            this.f24243h[1] = this.f24242g.l();
            this.f24238c[i8].mapPoints(this.f24243h);
            Path path3 = this.f24240e;
            float[] fArr3 = this.f24243h;
            path3.moveTo(fArr3[0], fArr3[1]);
            oVar = this.f24242g;
            matrix = this.f24238c[i8];
            path = this.f24240e;
        } else {
            oVar = this.f24242g;
            matrix = this.f24238c[i8];
            path = cVar.f24250b;
        }
        oVar.d(matrix, path);
        b bVar = cVar.f24252d;
        if (bVar != null) {
            bVar.a(this.f24242g, this.f24238c[i8], i8);
        }
    }

    private void f(int i8, RectF rectF, PointF pointF) {
        float f5;
        float f8;
        if (i8 == 1) {
            f5 = rectF.right;
        } else if (i8 != 2) {
            f5 = i8 != 3 ? rectF.right : rectF.left;
            f8 = rectF.top;
            pointF.set(f5, f8);
        } else {
            f5 = rectF.left;
        }
        f8 = rectF.bottom;
        pointF.set(f5, f8);
    }

    private x7.c g(int i8, m mVar) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? mVar.t() : mVar.r() : mVar.j() : mVar.l();
    }

    private d h(int i8, m mVar) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? mVar.s() : mVar.q() : mVar.i() : mVar.k();
    }

    private float i(RectF rectF, int i8) {
        float centerX;
        float f5;
        float[] fArr = this.f24243h;
        o[] oVarArr = this.f24236a;
        fArr[0] = oVarArr[i8].f24256c;
        fArr[1] = oVarArr[i8].f24257d;
        this.f24237b[i8].mapPoints(fArr);
        if (i8 == 1 || i8 == 3) {
            centerX = rectF.centerX();
            f5 = this.f24243h[0];
        } else {
            centerX = rectF.centerY();
            f5 = this.f24243h[1];
        }
        return Math.abs(centerX - f5);
    }

    private f j(int i8, m mVar) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? mVar.o() : mVar.p() : mVar.n() : mVar.h();
    }

    public static n k() {
        return a.f24248a;
    }

    private boolean l(Path path, int i8) {
        this.f24246k.reset();
        this.f24236a[i8].d(this.f24237b[i8], this.f24246k);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        this.f24246k.computeBounds(rectF, true);
        path.op(this.f24246k, Path.Op.INTERSECT);
        path.computeBounds(rectF, true);
        if (rectF.isEmpty()) {
            return rectF.width() > 1.0f && rectF.height() > 1.0f;
        }
        return true;
    }

    private void m(c cVar, int i8) {
        h(i8, cVar.f24249a).b(this.f24236a[i8], 90.0f, cVar.f24253e, cVar.f24251c, g(i8, cVar.f24249a));
        float a9 = a(i8);
        this.f24237b[i8].reset();
        f(i8, cVar.f24251c, this.f24239d);
        Matrix matrix = this.f24237b[i8];
        PointF pointF = this.f24239d;
        matrix.setTranslate(pointF.x, pointF.y);
        this.f24237b[i8].preRotate(a9);
    }

    private void n(int i8) {
        this.f24243h[0] = this.f24236a[i8].i();
        this.f24243h[1] = this.f24236a[i8].j();
        this.f24237b[i8].mapPoints(this.f24243h);
        float a9 = a(i8);
        this.f24238c[i8].reset();
        Matrix matrix = this.f24238c[i8];
        float[] fArr = this.f24243h;
        matrix.setTranslate(fArr[0], fArr[1]);
        this.f24238c[i8].preRotate(a9);
    }

    public void d(m mVar, float f5, RectF rectF, Path path) {
        e(mVar, f5, rectF, null, path);
    }

    public void e(m mVar, float f5, RectF rectF, b bVar, Path path) {
        path.rewind();
        this.f24240e.rewind();
        this.f24241f.rewind();
        this.f24241f.addRect(rectF, Path.Direction.CW);
        c cVar = new c(mVar, f5, rectF, bVar, path);
        for (int i8 = 0; i8 < 4; i8++) {
            m(cVar, i8);
            n(i8);
        }
        for (int i9 = 0; i9 < 4; i9++) {
            b(cVar, i9);
            c(cVar, i9);
        }
        path.close();
        this.f24240e.close();
        if (Build.VERSION.SDK_INT < 19 || this.f24240e.isEmpty()) {
            return;
        }
        path.op(this.f24240e, Path.Op.UNION);
    }
}
