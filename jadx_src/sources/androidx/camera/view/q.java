package androidx.camera.view;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Size;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.p1;
import androidx.camera.core.z2;
import androidx.camera.view.PreviewView;
import androidx.camera.view.k;
import androidx.camera.view.q;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q extends k {

    /* renamed from: e  reason: collision with root package name */
    SurfaceView f3034e;

    /* renamed from: f  reason: collision with root package name */
    final b f3035f;

    /* renamed from: g  reason: collision with root package name */
    private k.a f3036g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {
        static void a(SurfaceView surfaceView, Bitmap bitmap, PixelCopy.OnPixelCopyFinishedListener onPixelCopyFinishedListener, Handler handler) {
            PixelCopy.request(surfaceView, bitmap, onPixelCopyFinishedListener, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements SurfaceHolder.Callback {

        /* renamed from: a  reason: collision with root package name */
        private Size f3037a;

        /* renamed from: b  reason: collision with root package name */
        private z2 f3038b;

        /* renamed from: c  reason: collision with root package name */
        private Size f3039c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f3040d = false;

        b() {
        }

        private boolean b() {
            Size size;
            return (this.f3040d || this.f3038b == null || (size = this.f3037a) == null || !size.equals(this.f3039c)) ? false : true;
        }

        private void c() {
            if (this.f3038b != null) {
                p1.a("SurfaceViewImpl", "Request canceled: " + this.f3038b);
                this.f3038b.y();
            }
        }

        private void d() {
            if (this.f3038b != null) {
                p1.a("SurfaceViewImpl", "Surface invalidated " + this.f3038b);
                this.f3038b.k().c();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(z2.f fVar) {
            p1.a("SurfaceViewImpl", "Safe to release surface.");
            q.this.p();
        }

        private boolean g() {
            Surface surface = q.this.f3034e.getHolder().getSurface();
            if (b()) {
                p1.a("SurfaceViewImpl", "Surface set on Preview.");
                this.f3038b.v(surface, androidx.core.content.a.i(q.this.f3034e.getContext()), new androidx.core.util.a() { // from class: androidx.camera.view.r
                    @Override // androidx.core.util.a
                    public final void accept(Object obj) {
                        q.b.this.e((z2.f) obj);
                    }
                });
                this.f3040d = true;
                q.this.f();
                return true;
            }
            return false;
        }

        void f(z2 z2Var) {
            c();
            this.f3038b = z2Var;
            Size l8 = z2Var.l();
            this.f3037a = l8;
            this.f3040d = false;
            if (g()) {
                return;
            }
            p1.a("SurfaceViewImpl", "Wait for new Surface creation.");
            q.this.f3034e.getHolder().setFixedSize(l8.getWidth(), l8.getHeight());
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i8, int i9, int i10) {
            p1.a("SurfaceViewImpl", "Surface changed. Size: " + i9 + "x" + i10);
            this.f3039c = new Size(i9, i10);
            g();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            p1.a("SurfaceViewImpl", "Surface created.");
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            p1.a("SurfaceViewImpl", "Surface destroyed.");
            if (this.f3040d) {
                d();
            } else {
                c();
            }
            this.f3040d = false;
            this.f3038b = null;
            this.f3039c = null;
            this.f3037a = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(FrameLayout frameLayout, f fVar) {
        super(frameLayout, fVar);
        this.f3035f = new b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void n(int i8) {
        if (i8 == 0) {
            p1.a("SurfaceViewImpl", "PreviewView.SurfaceViewImplementation.getBitmap() succeeded");
            return;
        }
        p1.c("SurfaceViewImpl", "PreviewView.SurfaceViewImplementation.getBitmap() failed with error " + i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(z2 z2Var) {
        this.f3035f.f(z2Var);
    }

    @Override // androidx.camera.view.k
    View b() {
        return this.f3034e;
    }

    @Override // androidx.camera.view.k
    Bitmap c() {
        SurfaceView surfaceView = this.f3034e;
        if (surfaceView == null || surfaceView.getHolder().getSurface() == null || !this.f3034e.getHolder().getSurface().isValid()) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(this.f3034e.getWidth(), this.f3034e.getHeight(), Bitmap.Config.ARGB_8888);
        SurfaceView surfaceView2 = this.f3034e;
        a.a(surfaceView2, createBitmap, new PixelCopy.OnPixelCopyFinishedListener() { // from class: androidx.camera.view.n
            @Override // android.view.PixelCopy.OnPixelCopyFinishedListener
            public final void onPixelCopyFinished(int i8) {
                q.n(i8);
            }
        }, surfaceView2.getHandler());
        return createBitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void d() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void e() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void g(final z2 z2Var, k.a aVar) {
        this.f3020a = z2Var.l();
        this.f3036g = aVar;
        m();
        z2Var.i(androidx.core.content.a.i(this.f3034e.getContext()), new Runnable() { // from class: androidx.camera.view.o
            @Override // java.lang.Runnable
            public final void run() {
                q.this.p();
            }
        });
        this.f3034e.post(new Runnable() { // from class: androidx.camera.view.p
            @Override // java.lang.Runnable
            public final void run() {
                q.this.o(z2Var);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void i(Executor executor, PreviewView.d dVar) {
        throw new IllegalArgumentException("SurfaceView doesn't support frame update listener");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public com.google.common.util.concurrent.d<Void> j() {
        return a0.f.h(null);
    }

    void m() {
        androidx.core.util.h.h(this.f3021b);
        androidx.core.util.h.h(this.f3020a);
        SurfaceView surfaceView = new SurfaceView(this.f3021b.getContext());
        this.f3034e = surfaceView;
        surfaceView.setLayoutParams(new FrameLayout.LayoutParams(this.f3020a.getWidth(), this.f3020a.getHeight()));
        this.f3021b.removeAllViews();
        this.f3021b.addView(this.f3034e);
        this.f3034e.getHolder().addCallback(this.f3035f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        k.a aVar = this.f3036g;
        if (aVar != null) {
            aVar.a();
            this.f3036g = null;
        }
    }
}
