package androidx.camera.view;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.p1;
import androidx.camera.core.z2;
import androidx.camera.view.PreviewView;
import androidx.camera.view.k;
import androidx.camera.view.y;
import androidx.concurrent.futures.c;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y extends k {

    /* renamed from: e  reason: collision with root package name */
    TextureView f3055e;

    /* renamed from: f  reason: collision with root package name */
    SurfaceTexture f3056f;

    /* renamed from: g  reason: collision with root package name */
    com.google.common.util.concurrent.d<z2.f> f3057g;

    /* renamed from: h  reason: collision with root package name */
    z2 f3058h;

    /* renamed from: i  reason: collision with root package name */
    boolean f3059i;

    /* renamed from: j  reason: collision with root package name */
    SurfaceTexture f3060j;

    /* renamed from: k  reason: collision with root package name */
    AtomicReference<c.a<Void>> f3061k;

    /* renamed from: l  reason: collision with root package name */
    k.a f3062l;

    /* renamed from: m  reason: collision with root package name */
    PreviewView.d f3063m;

    /* renamed from: n  reason: collision with root package name */
    Executor f3064n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements TextureView.SurfaceTextureListener {

        /* renamed from: androidx.camera.view.y$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0020a implements a0.c<z2.f> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ SurfaceTexture f3066a;

            C0020a(SurfaceTexture surfaceTexture) {
                this.f3066a = surfaceTexture;
            }

            @Override // a0.c
            /* renamed from: a */
            public void c(z2.f fVar) {
                androidx.core.util.h.k(fVar.a() != 3, "Unexpected result from SurfaceRequest. Surface was provided twice.");
                p1.a("TextureViewImpl", "SurfaceTexture about to manually be destroyed");
                this.f3066a.release();
                y yVar = y.this;
                if (yVar.f3060j != null) {
                    yVar.f3060j = null;
                }
            }

            @Override // a0.c
            public void onFailure(Throwable th) {
                throw new IllegalStateException("SurfaceReleaseFuture did not complete nicely.", th);
            }
        }

        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void b(PreviewView.d dVar, SurfaceTexture surfaceTexture) {
            dVar.a(surfaceTexture.getTimestamp());
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i8, int i9) {
            p1.a("TextureViewImpl", "SurfaceTexture available. Size: " + i8 + "x" + i9);
            y yVar = y.this;
            yVar.f3056f = surfaceTexture;
            if (yVar.f3057g == null) {
                yVar.v();
                return;
            }
            androidx.core.util.h.h(yVar.f3058h);
            p1.a("TextureViewImpl", "Surface invalidated " + y.this.f3058h);
            y.this.f3058h.k().c();
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            y yVar = y.this;
            yVar.f3056f = null;
            com.google.common.util.concurrent.d<z2.f> dVar = yVar.f3057g;
            if (dVar == null) {
                p1.a("TextureViewImpl", "SurfaceTexture about to be destroyed");
                return true;
            }
            a0.f.b(dVar, new C0020a(surfaceTexture), androidx.core.content.a.i(y.this.f3055e.getContext()));
            y.this.f3060j = surfaceTexture;
            return false;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i8, int i9) {
            p1.a("TextureViewImpl", "SurfaceTexture size changed: " + i8 + "x" + i9);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            c.a<Void> andSet = y.this.f3061k.getAndSet(null);
            if (andSet != null) {
                andSet.c(null);
            }
            y yVar = y.this;
            final PreviewView.d dVar = yVar.f3063m;
            Executor executor = yVar.f3064n;
            if (dVar == null || executor == null) {
                return;
            }
            executor.execute(new Runnable() { // from class: androidx.camera.view.x
                @Override // java.lang.Runnable
                public final void run() {
                    y.a.b(PreviewView.d.this, surfaceTexture);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(FrameLayout frameLayout, f fVar) {
        super(frameLayout, fVar);
        this.f3059i = false;
        this.f3061k = new AtomicReference<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p(z2 z2Var) {
        z2 z2Var2 = this.f3058h;
        if (z2Var2 != null && z2Var2 == z2Var) {
            this.f3058h = null;
            this.f3057g = null;
        }
        t();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object q(Surface surface, final c.a aVar) {
        p1.a("TextureViewImpl", "Surface set on Preview.");
        z2 z2Var = this.f3058h;
        Executor a9 = z.a.a();
        Objects.requireNonNull(aVar);
        z2Var.v(surface, a9, new androidx.core.util.a() { // from class: androidx.camera.view.u
            @Override // androidx.core.util.a
            public final void accept(Object obj) {
                c.a.this.c((z2.f) obj);
            }
        });
        return "provideSurface[request=" + this.f3058h + " surface=" + surface + "]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(Surface surface, com.google.common.util.concurrent.d dVar, z2 z2Var) {
        p1.a("TextureViewImpl", "Safe to release surface.");
        t();
        surface.release();
        if (this.f3057g == dVar) {
            this.f3057g = null;
        }
        if (this.f3058h == z2Var) {
            this.f3058h = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object s(c.a aVar) {
        this.f3061k.set(aVar);
        return "textureViewImpl_waitForNextFrame";
    }

    private void t() {
        k.a aVar = this.f3062l;
        if (aVar != null) {
            aVar.a();
            this.f3062l = null;
        }
    }

    private void u() {
        if (!this.f3059i || this.f3060j == null) {
            return;
        }
        SurfaceTexture surfaceTexture = this.f3055e.getSurfaceTexture();
        SurfaceTexture surfaceTexture2 = this.f3060j;
        if (surfaceTexture != surfaceTexture2) {
            this.f3055e.setSurfaceTexture(surfaceTexture2);
            this.f3060j = null;
            this.f3059i = false;
        }
    }

    @Override // androidx.camera.view.k
    View b() {
        return this.f3055e;
    }

    @Override // androidx.camera.view.k
    Bitmap c() {
        TextureView textureView = this.f3055e;
        if (textureView == null || !textureView.isAvailable()) {
            return null;
        }
        return this.f3055e.getBitmap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void d() {
        u();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void e() {
        this.f3059i = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void g(final z2 z2Var, k.a aVar) {
        this.f3020a = z2Var.l();
        this.f3062l = aVar;
        o();
        z2 z2Var2 = this.f3058h;
        if (z2Var2 != null) {
            z2Var2.y();
        }
        this.f3058h = z2Var;
        z2Var.i(androidx.core.content.a.i(this.f3055e.getContext()), new Runnable() { // from class: androidx.camera.view.w
            @Override // java.lang.Runnable
            public final void run() {
                y.this.p(z2Var);
            }
        });
        v();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public void i(Executor executor, PreviewView.d dVar) {
        this.f3063m = dVar;
        this.f3064n = executor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.view.k
    public com.google.common.util.concurrent.d<Void> j() {
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.view.s
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object s8;
                s8 = y.this.s(aVar);
                return s8;
            }
        });
    }

    public void o() {
        androidx.core.util.h.h(this.f3021b);
        androidx.core.util.h.h(this.f3020a);
        TextureView textureView = new TextureView(this.f3021b.getContext());
        this.f3055e = textureView;
        textureView.setLayoutParams(new FrameLayout.LayoutParams(this.f3020a.getWidth(), this.f3020a.getHeight()));
        this.f3055e.setSurfaceTextureListener(new a());
        this.f3021b.removeAllViews();
        this.f3021b.addView(this.f3055e);
    }

    void v() {
        SurfaceTexture surfaceTexture;
        Size size = this.f3020a;
        if (size == null || (surfaceTexture = this.f3056f) == null || this.f3058h == null) {
            return;
        }
        surfaceTexture.setDefaultBufferSize(size.getWidth(), this.f3020a.getHeight());
        final Surface surface = new Surface(this.f3056f);
        final z2 z2Var = this.f3058h;
        final com.google.common.util.concurrent.d<z2.f> a9 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.view.t
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object q;
                q = y.this.q(surface, aVar);
                return q;
            }
        });
        this.f3057g = a9;
        a9.c(new Runnable() { // from class: androidx.camera.view.v
            @Override // java.lang.Runnable
            public final void run() {
                y.this.r(surface, a9, z2Var);
            }
        }, androidx.core.content.a.i(this.f3055e.getContext()));
        f();
    }
}
