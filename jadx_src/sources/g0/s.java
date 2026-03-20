package g0;

import a0.f;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Size;
import androidx.camera.core.SurfaceOutput;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.utils.m;
import androidx.camera.core.impl.utils.n;
import androidx.camera.core.z2;
import androidx.core.util.h;
import java.util.Collections;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s {

    /* renamed from: a  reason: collision with root package name */
    private final SurfaceOutput.GlTransformOptions f20173a;

    /* renamed from: b  reason: collision with root package name */
    final p f20174b;

    /* renamed from: c  reason: collision with root package name */
    final CameraInternal f20175c;

    /* renamed from: d  reason: collision with root package name */
    private l f20176d;

    /* renamed from: e  reason: collision with root package name */
    private l f20177e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<SurfaceOutput> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ z2 f20178a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ k f20179b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ k f20180c;

        a(z2 z2Var, k kVar, k kVar2) {
            this.f20178a = z2Var;
            this.f20179b = kVar;
            this.f20180c = kVar2;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(SurfaceOutput surfaceOutput) {
            h.h(surfaceOutput);
            s.this.f20174b.b(surfaceOutput);
            s.this.f20174b.a(this.f20178a);
            s.this.h(this.f20179b, this.f20178a, this.f20180c, surfaceOutput);
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f20178a.y();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f20182a;

        static {
            int[] iArr = new int[SurfaceOutput.GlTransformOptions.values().length];
            f20182a = iArr;
            try {
                iArr[SurfaceOutput.GlTransformOptions.APPLY_CROP_ROTATE_AND_MIRRORING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f20182a[SurfaceOutput.GlTransformOptions.USE_SURFACE_TEXTURE_TRANSFORM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public s(CameraInternal cameraInternal, SurfaceOutput.GlTransformOptions glTransformOptions, p pVar) {
        this.f20175c = cameraInternal;
        this.f20173a = glTransformOptions;
        this.f20174b = pVar;
    }

    private k c(k kVar) {
        int i8 = b.f20182a[this.f20173a.ordinal()];
        if (i8 != 1) {
            if (i8 == 2) {
                return new k(kVar.C(), kVar.B(), kVar.x(), kVar.A(), false, kVar.w(), kVar.z(), kVar.y());
            }
            throw new AssertionError("Unknown GlTransformOptions: " + this.f20173a);
        }
        Size B = kVar.B();
        Rect w8 = kVar.w();
        int z4 = kVar.z();
        boolean y8 = kVar.y();
        Size size = n.f(z4) ? new Size(w8.height(), w8.width()) : n.h(w8);
        Matrix matrix = new Matrix(kVar.A());
        matrix.postConcat(n.d(n.m(B), new RectF(w8), z4, y8));
        return new k(kVar.C(), size, kVar.x(), matrix, false, n.k(size), 0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        l lVar = this.f20176d;
        if (lVar != null) {
            for (k kVar : lVar.b()) {
                kVar.c();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void e(SurfaceOutput surfaceOutput, k kVar, k kVar2, z2.g gVar) {
        int b9 = gVar.b() - surfaceOutput.b();
        if (kVar.y()) {
            b9 = -b9;
        }
        kVar2.K(n.p(b9));
    }

    private void g(k kVar, k kVar2) {
        f.b(kVar2.t(this.f20173a, kVar.B(), kVar.w(), kVar.z(), kVar.y()), new a(kVar.u(this.f20175c), kVar, kVar2), z.a.d());
    }

    public void f() {
        this.f20174b.release();
        z.a.d().execute(new r(this));
    }

    void h(k kVar, z2 z2Var, k kVar2, SurfaceOutput surfaceOutput) {
        z2Var.w(z.a.d(), new q(surfaceOutput, kVar, kVar2));
    }

    public l i(l lVar) {
        m.a();
        h.b(lVar.b().size() == 1, "Multiple input stream not supported yet.");
        this.f20177e = lVar;
        k kVar = lVar.b().get(0);
        k c9 = c(kVar);
        g(kVar, c9);
        l a9 = l.a(Collections.singletonList(c9));
        this.f20176d = a9;
        return a9;
    }
}
