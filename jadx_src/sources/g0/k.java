package g0;

import a0.f;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.SurfaceOutput;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.utils.m;
import androidx.camera.core.z2;
import androidx.concurrent.futures.c;
import androidx.core.util.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends DeferrableSurface {

    /* renamed from: m  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Surface> f20146m;

    /* renamed from: n  reason: collision with root package name */
    c.a<Surface> f20147n;

    /* renamed from: o  reason: collision with root package name */
    private final Matrix f20148o;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f20149p;
    private final Rect q;

    /* renamed from: r  reason: collision with root package name */
    private final boolean f20150r;

    /* renamed from: s  reason: collision with root package name */
    private final int f20151s;

    /* renamed from: t  reason: collision with root package name */
    private int f20152t;

    /* renamed from: u  reason: collision with root package name */
    private o f20153u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f20154v;

    /* renamed from: w  reason: collision with root package name */
    private boolean f20155w;

    /* renamed from: x  reason: collision with root package name */
    private z2 f20156x;

    public k(int i8, Size size, int i9, Matrix matrix, boolean z4, Rect rect, int i10, boolean z8) {
        super(size, i9);
        this.f20154v = false;
        this.f20155w = false;
        this.f20151s = i8;
        this.f20148o = matrix;
        this.f20149p = z4;
        this.q = rect;
        this.f20152t = i10;
        this.f20150r = z8;
        this.f20146m = androidx.concurrent.futures.c.a(new g(this, size));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void D() {
        o oVar = this.f20153u;
        if (oVar != null) {
            oVar.h();
            this.f20153u = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d E(SurfaceOutput.GlTransformOptions glTransformOptions, Size size, Rect rect, int i8, boolean z4, Surface surface) {
        h.h(surface);
        try {
            j();
            o oVar = new o(surface, C(), x(), B(), glTransformOptions, size, rect, i8, z4);
            oVar.e().c(new i(this), z.a.a());
            this.f20153u = oVar;
            return f.h(oVar);
        } catch (DeferrableSurface.SurfaceClosedException e8) {
            return f.f(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object F(Size size, c.a aVar) {
        this.f20147n = aVar;
        return "SettableFuture size: " + size + " hashCode: " + hashCode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void G(DeferrableSurface deferrableSurface) {
        deferrableSurface.d();
        deferrableSurface.c();
    }

    private void H() {
        z2 z2Var = this.f20156x;
        if (z2Var != null) {
            z2Var.x(z2.g.d(this.q, this.f20152t, -1));
        }
    }

    public Matrix A() {
        return this.f20148o;
    }

    public Size B() {
        return f();
    }

    public int C() {
        return this.f20151s;
    }

    public void I(DeferrableSurface deferrableSurface) {
        m.a();
        J(deferrableSurface.h());
        deferrableSurface.j();
        i().c(new h(deferrableSurface), z.a.a());
    }

    public void J(com.google.common.util.concurrent.d<Surface> dVar) {
        m.a();
        h.k(!this.f20154v, "Provider can only be linked once.");
        this.f20154v = true;
        f.k(dVar, this.f20147n);
    }

    public void K(int i8) {
        m.a();
        if (this.f20152t == i8) {
            return;
        }
        this.f20152t = i8;
        H();
    }

    @Override // androidx.camera.core.impl.DeferrableSurface
    public final void c() {
        super.c();
        z.a.d().execute(new j(this));
    }

    @Override // androidx.camera.core.impl.DeferrableSurface
    protected com.google.common.util.concurrent.d<Surface> n() {
        return this.f20146m;
    }

    public com.google.common.util.concurrent.d<SurfaceOutput> t(SurfaceOutput.GlTransformOptions glTransformOptions, Size size, Rect rect, int i8, boolean z4) {
        m.a();
        h.k(!this.f20155w, "Consumer can only be linked once.");
        this.f20155w = true;
        return f.p(h(), new f(this, glTransformOptions, size, rect, i8, z4), z.a.d());
    }

    public z2 u(CameraInternal cameraInternal) {
        return v(cameraInternal, null);
    }

    public z2 v(CameraInternal cameraInternal, Range<Integer> range) {
        m.a();
        z2 z2Var = new z2(B(), cameraInternal, true, range);
        try {
            I(z2Var.k());
            this.f20156x = z2Var;
            H();
            return z2Var;
        } catch (DeferrableSurface.SurfaceClosedException e8) {
            throw new AssertionError("Surface is somehow already closed", e8);
        }
    }

    public Rect w() {
        return this.q;
    }

    public int x() {
        return g();
    }

    public boolean y() {
        return this.f20150r;
    }

    public int z() {
        return this.f20152t;
    }
}
