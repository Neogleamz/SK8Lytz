package x7;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {
    @Deprecated

    /* renamed from: a  reason: collision with root package name */
    public float f24254a;
    @Deprecated

    /* renamed from: b  reason: collision with root package name */
    public float f24255b;
    @Deprecated

    /* renamed from: c  reason: collision with root package name */
    public float f24256c;
    @Deprecated

    /* renamed from: d  reason: collision with root package name */
    public float f24257d;
    @Deprecated

    /* renamed from: e  reason: collision with root package name */
    public float f24258e;
    @Deprecated

    /* renamed from: f  reason: collision with root package name */
    public float f24259f;

    /* renamed from: g  reason: collision with root package name */
    private final List<f> f24260g = new ArrayList();

    /* renamed from: h  reason: collision with root package name */
    private final List<g> f24261h = new ArrayList();

    /* renamed from: i  reason: collision with root package name */
    private boolean f24262i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends g {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ List f24263b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Matrix f24264c;

        a(List list, Matrix matrix) {
            this.f24263b = list;
            this.f24264c = matrix;
        }

        @Override // x7.o.g
        public void a(Matrix matrix, w7.a aVar, int i8, Canvas canvas) {
            for (g gVar : this.f24263b) {
                gVar.a(this.f24264c, aVar, i8, canvas);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends g {

        /* renamed from: b  reason: collision with root package name */
        private final d f24266b;

        public b(d dVar) {
            this.f24266b = dVar;
        }

        @Override // x7.o.g
        public void a(Matrix matrix, w7.a aVar, int i8, Canvas canvas) {
            aVar.a(canvas, matrix, new RectF(this.f24266b.k(), this.f24266b.o(), this.f24266b.l(), this.f24266b.j()), i8, this.f24266b.m(), this.f24266b.n());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends g {

        /* renamed from: b  reason: collision with root package name */
        private final e f24267b;

        /* renamed from: c  reason: collision with root package name */
        private final float f24268c;

        /* renamed from: d  reason: collision with root package name */
        private final float f24269d;

        public c(e eVar, float f5, float f8) {
            this.f24267b = eVar;
            this.f24268c = f5;
            this.f24269d = f8;
        }

        @Override // x7.o.g
        public void a(Matrix matrix, w7.a aVar, int i8, Canvas canvas) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) Math.hypot(this.f24267b.f24278c - this.f24269d, this.f24267b.f24277b - this.f24268c), 0.0f);
            Matrix matrix2 = new Matrix(matrix);
            matrix2.preTranslate(this.f24268c, this.f24269d);
            matrix2.preRotate(c());
            aVar.b(canvas, matrix2, rectF, i8);
        }

        float c() {
            return (float) Math.toDegrees(Math.atan((this.f24267b.f24278c - this.f24269d) / (this.f24267b.f24277b - this.f24268c)));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends f {

        /* renamed from: h  reason: collision with root package name */
        private static final RectF f24270h = new RectF();
        @Deprecated

        /* renamed from: b  reason: collision with root package name */
        public float f24271b;
        @Deprecated

        /* renamed from: c  reason: collision with root package name */
        public float f24272c;
        @Deprecated

        /* renamed from: d  reason: collision with root package name */
        public float f24273d;
        @Deprecated

        /* renamed from: e  reason: collision with root package name */
        public float f24274e;
        @Deprecated

        /* renamed from: f  reason: collision with root package name */
        public float f24275f;
        @Deprecated

        /* renamed from: g  reason: collision with root package name */
        public float f24276g;

        public d(float f5, float f8, float f9, float f10) {
            q(f5);
            u(f8);
            r(f9);
            p(f10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float j() {
            return this.f24274e;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float k() {
            return this.f24271b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float l() {
            return this.f24273d;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float m() {
            return this.f24275f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float n() {
            return this.f24276g;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float o() {
            return this.f24272c;
        }

        private void p(float f5) {
            this.f24274e = f5;
        }

        private void q(float f5) {
            this.f24271b = f5;
        }

        private void r(float f5) {
            this.f24273d = f5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void s(float f5) {
            this.f24275f = f5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void t(float f5) {
            this.f24276g = f5;
        }

        private void u(float f5) {
            this.f24272c = f5;
        }

        @Override // x7.o.f
        public void a(Matrix matrix, Path path) {
            Matrix matrix2 = this.f24279a;
            matrix.invert(matrix2);
            path.transform(matrix2);
            RectF rectF = f24270h;
            rectF.set(k(), o(), l(), j());
            path.arcTo(rectF, m(), n(), false);
            path.transform(matrix);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends f {

        /* renamed from: b  reason: collision with root package name */
        private float f24277b;

        /* renamed from: c  reason: collision with root package name */
        private float f24278c;

        @Override // x7.o.f
        public void a(Matrix matrix, Path path) {
            Matrix matrix2 = this.f24279a;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.f24277b, this.f24278c);
            path.transform(matrix);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f {

        /* renamed from: a  reason: collision with root package name */
        protected final Matrix f24279a = new Matrix();

        public abstract void a(Matrix matrix, Path path);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class g {

        /* renamed from: a  reason: collision with root package name */
        static final Matrix f24280a = new Matrix();

        g() {
        }

        public abstract void a(Matrix matrix, w7.a aVar, int i8, Canvas canvas);

        public final void b(w7.a aVar, int i8, Canvas canvas) {
            a(f24280a, aVar, i8, canvas);
        }
    }

    public o() {
        n(0.0f, 0.0f);
    }

    private void b(float f5) {
        if (g() == f5) {
            return;
        }
        float g8 = ((f5 - g()) + 360.0f) % 360.0f;
        if (g8 > 180.0f) {
            return;
        }
        d dVar = new d(i(), j(), i(), j());
        dVar.s(g());
        dVar.t(g8);
        this.f24261h.add(new b(dVar));
        p(f5);
    }

    private void c(g gVar, float f5, float f8) {
        b(f5);
        this.f24261h.add(gVar);
        p(f8);
    }

    private float g() {
        return this.f24258e;
    }

    private float h() {
        return this.f24259f;
    }

    private void p(float f5) {
        this.f24258e = f5;
    }

    private void q(float f5) {
        this.f24259f = f5;
    }

    private void r(float f5) {
        this.f24256c = f5;
    }

    private void s(float f5) {
        this.f24257d = f5;
    }

    private void t(float f5) {
        this.f24254a = f5;
    }

    private void u(float f5) {
        this.f24255b = f5;
    }

    public void a(float f5, float f8, float f9, float f10, float f11, float f12) {
        d dVar = new d(f5, f8, f9, f10);
        dVar.s(f11);
        dVar.t(f12);
        this.f24260g.add(dVar);
        b bVar = new b(dVar);
        float f13 = f11 + f12;
        boolean z4 = f12 < 0.0f;
        if (z4) {
            f11 = (f11 + 180.0f) % 360.0f;
        }
        c(bVar, f11, z4 ? (180.0f + f13) % 360.0f : f13);
        double d8 = f13;
        r(((f5 + f9) * 0.5f) + (((f9 - f5) / 2.0f) * ((float) Math.cos(Math.toRadians(d8)))));
        s(((f8 + f10) * 0.5f) + (((f10 - f8) / 2.0f) * ((float) Math.sin(Math.toRadians(d8)))));
    }

    public void d(Matrix matrix, Path path) {
        int size = this.f24260g.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f24260g.get(i8).a(matrix, path);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e() {
        return this.f24262i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g f(Matrix matrix) {
        b(h());
        return new a(new ArrayList(this.f24261h), new Matrix(matrix));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f24256c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f24257d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float k() {
        return this.f24254a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float l() {
        return this.f24255b;
    }

    public void m(float f5, float f8) {
        e eVar = new e();
        eVar.f24277b = f5;
        eVar.f24278c = f8;
        this.f24260g.add(eVar);
        c cVar = new c(eVar, i(), j());
        c(cVar, cVar.c() + 270.0f, cVar.c() + 270.0f);
        r(f5);
        s(f8);
    }

    public void n(float f5, float f8) {
        o(f5, f8, 270.0f, 0.0f);
    }

    public void o(float f5, float f8, float f9, float f10) {
        t(f5);
        u(f8);
        r(f5);
        s(f8);
        p(f9);
        q((f9 + f10) % 360.0f);
        this.f24260g.clear();
        this.f24261h.clear();
        this.f24262i = false;
    }
}
