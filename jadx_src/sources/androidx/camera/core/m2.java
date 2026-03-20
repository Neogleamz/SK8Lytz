package androidx.camera.core;

import android.view.Surface;
import androidx.camera.core.h0;
import java.util.concurrent.Executor;
import y.g0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m2 implements y.g0 {

    /* renamed from: d  reason: collision with root package name */
    private final y.g0 f2724d;

    /* renamed from: e  reason: collision with root package name */
    private final Surface f2725e;

    /* renamed from: f  reason: collision with root package name */
    private h0.a f2726f;

    /* renamed from: a  reason: collision with root package name */
    private final Object f2721a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private int f2722b = 0;

    /* renamed from: c  reason: collision with root package name */
    private boolean f2723c = false;

    /* renamed from: g  reason: collision with root package name */
    private final h0.a f2727g = new h0.a() { // from class: androidx.camera.core.k2
        @Override // androidx.camera.core.h0.a
        public final void b(l1 l1Var) {
            m2.this.i(l1Var);
        }
    };

    public m2(y.g0 g0Var) {
        this.f2724d = g0Var;
        this.f2725e = g0Var.getSurface();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void i(l1 l1Var) {
        h0.a aVar;
        synchronized (this.f2721a) {
            int i8 = this.f2722b - 1;
            this.f2722b = i8;
            if (this.f2723c && i8 == 0) {
                close();
            }
            aVar = this.f2726f;
        }
        if (aVar != null) {
            aVar.b(l1Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j(g0.a aVar, y.g0 g0Var) {
        aVar.a(this);
    }

    private l1 m(l1 l1Var) {
        if (l1Var != null) {
            this.f2722b++;
            p2 p2Var = new p2(l1Var);
            p2Var.a(this.f2727g);
            return p2Var;
        }
        return null;
    }

    @Override // y.g0
    public void a(final g0.a aVar, Executor executor) {
        synchronized (this.f2721a) {
            this.f2724d.a(new g0.a() { // from class: androidx.camera.core.l2
                @Override // y.g0.a
                public final void a(y.g0 g0Var) {
                    m2.this.j(aVar, g0Var);
                }
            }, executor);
        }
    }

    @Override // y.g0
    public l1 acquireLatestImage() {
        l1 m8;
        synchronized (this.f2721a) {
            m8 = m(this.f2724d.acquireLatestImage());
        }
        return m8;
    }

    @Override // y.g0
    public int c() {
        int c9;
        synchronized (this.f2721a) {
            c9 = this.f2724d.c();
        }
        return c9;
    }

    @Override // y.g0
    public void close() {
        synchronized (this.f2721a) {
            Surface surface = this.f2725e;
            if (surface != null) {
                surface.release();
            }
            this.f2724d.close();
        }
    }

    @Override // y.g0
    public void d() {
        synchronized (this.f2721a) {
            this.f2724d.d();
        }
    }

    @Override // y.g0
    public int e() {
        int e8;
        synchronized (this.f2721a) {
            e8 = this.f2724d.e();
        }
        return e8;
    }

    @Override // y.g0
    public l1 f() {
        l1 m8;
        synchronized (this.f2721a) {
            m8 = m(this.f2724d.f());
        }
        return m8;
    }

    @Override // y.g0
    public int getHeight() {
        int height;
        synchronized (this.f2721a) {
            height = this.f2724d.getHeight();
        }
        return height;
    }

    @Override // y.g0
    public Surface getSurface() {
        Surface surface;
        synchronized (this.f2721a) {
            surface = this.f2724d.getSurface();
        }
        return surface;
    }

    @Override // y.g0
    public int getWidth() {
        int width;
        synchronized (this.f2721a) {
            width = this.f2724d.getWidth();
        }
        return width;
    }

    public int h() {
        int e8;
        synchronized (this.f2721a) {
            e8 = this.f2724d.e() - this.f2722b;
        }
        return e8;
    }

    public void k() {
        synchronized (this.f2721a) {
            this.f2723c = true;
            this.f2724d.d();
            if (this.f2722b == 0) {
                close();
            }
        }
    }

    public void l(h0.a aVar) {
        synchronized (this.f2721a) {
            this.f2726f = aVar;
        }
    }
}
