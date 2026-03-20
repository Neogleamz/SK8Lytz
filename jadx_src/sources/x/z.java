package x;

import android.graphics.Bitmap;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.e1;
import androidx.camera.core.l1;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import x.h;
import x.m;
import x.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class z {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f23742a;

    /* renamed from: b  reason: collision with root package name */
    private g0.d<b, g0.e<l1>> f23743b;

    /* renamed from: c  reason: collision with root package name */
    private g0.d<m.a, g0.e<byte[]>> f23744c;

    /* renamed from: d  reason: collision with root package name */
    private g0.d<h.a, g0.e<byte[]>> f23745d;

    /* renamed from: e  reason: collision with root package name */
    private g0.d<q.a, e1.n> f23746e;

    /* renamed from: f  reason: collision with root package name */
    private g0.d<g0.e<byte[]>, g0.e<Bitmap>> f23747f;

    /* renamed from: g  reason: collision with root package name */
    private g0.d<g0.e<l1>, l1> f23748g;

    /* renamed from: h  reason: collision with root package name */
    private g0.d<g0.e<byte[]>, g0.e<l1>> f23749h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static a c(int i8) {
            return new f(new g0.c(), i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.c<b> a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract int b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static b c(a0 a0Var, l1 l1Var) {
            return new g(a0Var, l1Var);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract l1 a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract a0 b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(Executor executor) {
        this.f23742a = executor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j(b bVar) {
        if (bVar.b().h()) {
            return;
        }
        this.f23742a.execute(new v(this, bVar));
    }

    private static void o(a0 a0Var, ImageCaptureException imageCaptureException) {
        z.a.d().execute(new x(a0Var, imageCaptureException));
    }

    l1 k(b bVar) {
        a0 b9 = bVar.b();
        g0.e<l1> apply = this.f23743b.apply(bVar);
        if (apply.e() == 35) {
            apply = this.f23749h.apply(this.f23744c.apply(m.a.c(apply, b9.b())));
        }
        return this.f23748g.apply(apply);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: l */
    public void i(b bVar) {
        ScheduledExecutorService d8;
        y wVar;
        a0 b9 = bVar.b();
        try {
            if (bVar.b().i()) {
                l1 k8 = k(bVar);
                d8 = z.a.d();
                wVar = new y(b9, k8);
            } else {
                e1.n m8 = m(bVar);
                d8 = z.a.d();
                wVar = new w(b9, m8);
            }
            d8.execute(wVar);
        } catch (ImageCaptureException e8) {
            o(b9, e8);
        } catch (RuntimeException e9) {
            o(b9, new ImageCaptureException(0, "Processing failed.", e9));
        }
    }

    e1.n m(b bVar) {
        a0 b9 = bVar.b();
        g0.e<byte[]> apply = this.f23744c.apply(m.a.c(this.f23743b.apply(bVar), b9.b()));
        if (apply.i()) {
            apply = this.f23745d.apply(h.a.c(this.f23747f.apply(apply), b9.b()));
        }
        g0.d<q.a, e1.n> dVar = this.f23746e;
        e1.m c9 = b9.c();
        Objects.requireNonNull(c9);
        return dVar.apply(q.a.c(apply, c9));
    }

    public void n() {
    }

    public Void p(a aVar) {
        aVar.a().a(new u(this));
        this.f23743b = new t();
        this.f23744c = new m();
        this.f23747f = new p();
        this.f23745d = new h();
        this.f23746e = new q();
        this.f23748g = new s();
        if (aVar.b() == 35) {
            this.f23749h = new r();
            return null;
        }
        return null;
    }
}
