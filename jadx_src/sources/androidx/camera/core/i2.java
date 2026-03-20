package androidx.camera.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
import java.util.concurrent.ScheduledExecutorService;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i2 extends DeferrableSurface {

    /* renamed from: m  reason: collision with root package name */
    final Object f2413m;

    /* renamed from: n  reason: collision with root package name */
    private final g0.a f2414n;

    /* renamed from: o  reason: collision with root package name */
    boolean f2415o;

    /* renamed from: p  reason: collision with root package name */
    private final Size f2416p;
    private final s1 q;

    /* renamed from: r  reason: collision with root package name */
    private final Surface f2417r;

    /* renamed from: s  reason: collision with root package name */
    private final Handler f2418s;

    /* renamed from: t  reason: collision with root package name */
    final androidx.camera.core.impl.g f2419t;

    /* renamed from: u  reason: collision with root package name */
    final y.v f2420u;

    /* renamed from: v  reason: collision with root package name */
    private final y.h f2421v;

    /* renamed from: w  reason: collision with root package name */
    private final DeferrableSurface f2422w;

    /* renamed from: x  reason: collision with root package name */
    private String f2423x;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements a0.c<Surface> {
        a() {
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Surface surface) {
            synchronized (i2.this.f2413m) {
                i2.this.f2420u.a(surface, 1);
            }
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            p1.d("ProcessingSurfaceTextur", "Failed to extract Listenable<Surface>.", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i2(int i8, int i9, int i10, Handler handler, androidx.camera.core.impl.g gVar, y.v vVar, DeferrableSurface deferrableSurface, String str) {
        super(new Size(i8, i9), i10);
        this.f2413m = new Object();
        g0.a aVar = new g0.a() { // from class: androidx.camera.core.h2
            @Override // y.g0.a
            public final void a(y.g0 g0Var) {
                i2.this.u(g0Var);
            }
        };
        this.f2414n = aVar;
        this.f2415o = false;
        Size size = new Size(i8, i9);
        this.f2416p = size;
        if (handler != null) {
            this.f2418s = handler;
        } else {
            Looper myLooper = Looper.myLooper();
            if (myLooper == null) {
                throw new IllegalStateException("Creating a ProcessingSurface requires a non-null Handler, or be created  on a thread with a Looper.");
            }
            this.f2418s = new Handler(myLooper);
        }
        ScheduledExecutorService e8 = z.a.e(this.f2418s);
        s1 s1Var = new s1(i8, i9, i10, 2);
        this.q = s1Var;
        s1Var.a(aVar, e8);
        this.f2417r = s1Var.getSurface();
        this.f2421v = s1Var.l();
        this.f2420u = vVar;
        vVar.d(size);
        this.f2419t = gVar;
        this.f2422w = deferrableSurface;
        this.f2423x = str;
        a0.f.b(deferrableSurface.h(), new a(), z.a.a());
        i().c(new Runnable() { // from class: androidx.camera.core.f2
            @Override // java.lang.Runnable
            public final void run() {
                i2.this.w();
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void u(y.g0 g0Var) {
        synchronized (this.f2413m) {
            t(g0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Surface v(Surface surface) {
        return this.f2417r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w() {
        synchronized (this.f2413m) {
            if (this.f2415o) {
                return;
            }
            this.q.d();
            this.q.close();
            this.f2417r.release();
            this.f2422w.c();
            this.f2415o = true;
        }
    }

    @Override // androidx.camera.core.impl.DeferrableSurface
    public com.google.common.util.concurrent.d<Surface> n() {
        return a0.d.a(this.f2422w.h()).e(new n.a() { // from class: androidx.camera.core.g2
            @Override // n.a
            public final Object apply(Object obj) {
                Surface v8;
                v8 = i2.this.v((Surface) obj);
                return v8;
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y.h s() {
        y.h hVar;
        synchronized (this.f2413m) {
            if (this.f2415o) {
                throw new IllegalStateException("ProcessingSurface already released!");
            }
            hVar = this.f2421v;
        }
        return hVar;
    }

    void t(y.g0 g0Var) {
        if (this.f2415o) {
            return;
        }
        l1 l1Var = null;
        try {
            l1Var = g0Var.f();
        } catch (IllegalStateException e8) {
            p1.d("ProcessingSurfaceTextur", "Failed to acquire next image.", e8);
        }
        if (l1Var == null) {
            return;
        }
        i1 e12 = l1Var.e1();
        if (e12 == null) {
            l1Var.close();
            return;
        }
        Integer num = (Integer) e12.a().c(this.f2423x);
        if (num == null) {
            l1Var.close();
        } else if (this.f2419t.e() != num.intValue()) {
            p1.k("ProcessingSurfaceTextur", "ImageProxyBundle does not contain this id: " + num);
            l1Var.close();
        } else {
            y.x0 x0Var = new y.x0(l1Var, this.f2423x);
            try {
                j();
                this.f2420u.c(x0Var);
                x0Var.c();
                d();
            } catch (DeferrableSurface.SurfaceClosedException unused) {
                p1.a("ProcessingSurfaceTextur", "The ProcessingSurface has been closed. Don't process the incoming image.");
                x0Var.c();
            }
        }
    }
}
