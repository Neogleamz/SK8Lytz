package g0;

import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.Matrix;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.SurfaceOutput;
import androidx.camera.core.impl.utils.n;
import androidx.camera.core.p1;
import androidx.concurrent.futures.c;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o implements SurfaceOutput {

    /* renamed from: b  reason: collision with root package name */
    private final Surface f20158b;

    /* renamed from: c  reason: collision with root package name */
    private final int f20159c;

    /* renamed from: d  reason: collision with root package name */
    private final int f20160d;

    /* renamed from: e  reason: collision with root package name */
    private final Size f20161e;

    /* renamed from: f  reason: collision with root package name */
    private final SurfaceOutput.GlTransformOptions f20162f;

    /* renamed from: g  reason: collision with root package name */
    private final Size f20163g;

    /* renamed from: h  reason: collision with root package name */
    private final Rect f20164h;

    /* renamed from: i  reason: collision with root package name */
    private final int f20165i;

    /* renamed from: j  reason: collision with root package name */
    private final boolean f20166j;

    /* renamed from: l  reason: collision with root package name */
    private androidx.core.util.a<SurfaceOutput.a> f20168l;

    /* renamed from: m  reason: collision with root package name */
    private Executor f20169m;

    /* renamed from: p  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f20172p;
    private c.a<Void> q;

    /* renamed from: a  reason: collision with root package name */
    private final Object f20157a = new Object();

    /* renamed from: k  reason: collision with root package name */
    private final float[] f20167k = new float[16];

    /* renamed from: n  reason: collision with root package name */
    private boolean f20170n = false;

    /* renamed from: o  reason: collision with root package name */
    private boolean f20171o = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(Surface surface, int i8, int i9, Size size, SurfaceOutput.GlTransformOptions glTransformOptions, Size size2, Rect rect, int i10, boolean z4) {
        this.f20158b = surface;
        this.f20159c = i8;
        this.f20160d = i9;
        this.f20161e = size;
        this.f20162f = glTransformOptions;
        this.f20163g = size2;
        this.f20164h = new Rect(rect);
        this.f20166j = z4;
        if (glTransformOptions == SurfaceOutput.GlTransformOptions.APPLY_CROP_ROTATE_AND_MIRRORING) {
            this.f20165i = i10;
            d();
        } else {
            this.f20165i = 0;
        }
        this.f20172p = androidx.concurrent.futures.c.a(new m(this));
    }

    private void d() {
        Matrix.setIdentityM(this.f20167k, 0);
        Matrix.translateM(this.f20167k, 0, 0.0f, 1.0f, 0.0f);
        Matrix.scaleM(this.f20167k, 0, 1.0f, -1.0f, 1.0f);
        androidx.camera.core.impl.utils.l.c(this.f20167k, this.f20165i, 0.5f, 0.5f);
        if (this.f20166j) {
            Matrix.translateM(this.f20167k, 0, 1.0f, 0.0f, 0.0f);
            Matrix.scaleM(this.f20167k, 0, -1.0f, 1.0f, 1.0f);
        }
        Size j8 = n.j(this.f20163g, this.f20165i);
        android.graphics.Matrix d8 = n.d(n.m(this.f20163g), n.m(j8), this.f20165i, this.f20166j);
        RectF rectF = new RectF(this.f20164h);
        d8.mapRect(rectF);
        float width = rectF.left / j8.getWidth();
        float height = ((j8.getHeight() - rectF.height()) - rectF.top) / j8.getHeight();
        float height2 = rectF.height() / j8.getHeight();
        Matrix.translateM(this.f20167k, 0, width, height, 0.0f);
        Matrix.scaleM(this.f20167k, 0, rectF.width() / j8.getWidth(), height2, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object f(c.a aVar) {
        this.q = aVar;
        return "SurfaceOutputImpl close future complete";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(AtomicReference atomicReference) {
        ((androidx.core.util.a) atomicReference.get()).accept(SurfaceOutput.a.c(0, this));
    }

    @Override // androidx.camera.core.SurfaceOutput
    public int b() {
        return this.f20165i;
    }

    public com.google.common.util.concurrent.d<Void> e() {
        return this.f20172p;
    }

    public void h() {
        Executor executor;
        androidx.core.util.a<SurfaceOutput.a> aVar;
        AtomicReference atomicReference = new AtomicReference();
        synchronized (this.f20157a) {
            if (this.f20169m != null && (aVar = this.f20168l) != null) {
                if (!this.f20171o) {
                    atomicReference.set(aVar);
                    executor = this.f20169m;
                    this.f20170n = false;
                }
                executor = null;
            }
            this.f20170n = true;
            executor = null;
        }
        if (executor != null) {
            try {
                executor.execute(new n(this, atomicReference));
            } catch (RejectedExecutionException e8) {
                p1.b("SurfaceOutputImpl", "Processor executor closed. Close request not posted.", e8);
            }
        }
    }
}
