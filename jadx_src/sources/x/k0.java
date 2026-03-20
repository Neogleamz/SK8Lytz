package x;

import android.util.Log;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.h0;
import androidx.camera.core.l1;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k0 implements h0.a {

    /* renamed from: b  reason: collision with root package name */
    final o f23720b;

    /* renamed from: c  reason: collision with root package name */
    final n f23721c;

    /* renamed from: d  reason: collision with root package name */
    c0 f23722d;

    /* renamed from: a  reason: collision with root package name */
    final Deque<o0> f23719a = new ArrayDeque();

    /* renamed from: e  reason: collision with root package name */
    boolean f23723e = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Runnable f23724a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ i f23725b;

        a(Runnable runnable, i iVar) {
            this.f23724a = runnable;
            this.f23725b = iVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
            this.f23724a.run();
            k0.this.f23721c.c();
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            if (th instanceof ImageCaptureException) {
                this.f23725b.b((ImageCaptureException) th);
            } else {
                this.f23725b.b(new ImageCaptureException(2, "Failed to submit capture request", th));
            }
            k0.this.f23721c.c();
        }
    }

    public k0(n nVar, o oVar) {
        androidx.camera.core.impl.utils.m.a();
        this.f23721c = nVar;
        this.f23720b = oVar;
        oVar.j(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(a0 a0Var) {
        this.f23720b.i(a0Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void h() {
        this.f23722d = null;
        f();
    }

    private void k(i iVar, Runnable runnable) {
        androidx.camera.core.impl.utils.m.a();
        this.f23721c.b();
        a0.f.b(this.f23721c.a(iVar.a()), new a(runnable, iVar), z.a.d());
    }

    private void l(c0 c0Var) {
        androidx.core.util.h.j(!e());
        this.f23722d = c0Var;
        c0Var.j().c(new i0(this), z.a.a());
    }

    @Override // androidx.camera.core.h0.a
    public void b(l1 l1Var) {
        z.a.d().execute(new h0(this));
    }

    public void d() {
        androidx.camera.core.impl.utils.m.a();
        ImageCaptureException imageCaptureException = new ImageCaptureException(3, "Camera is closed.", null);
        for (o0 o0Var : this.f23719a) {
            o0Var.q(imageCaptureException);
        }
        this.f23719a.clear();
        c0 c0Var = this.f23722d;
        if (c0Var != null) {
            c0Var.h(imageCaptureException);
        }
    }

    boolean e() {
        return this.f23722d != null;
    }

    void f() {
        androidx.camera.core.impl.utils.m.a();
        Log.d("TakePictureManager", "Issue the next TakePictureRequest.");
        if (e()) {
            Log.d("TakePictureManager", "There is already a request in-flight.");
        } else if (this.f23723e) {
            Log.d("TakePictureManager", "The class is paused.");
        } else if (this.f23720b.h() == 0) {
            Log.d("TakePictureManager", "Too many acquire images. Close image to be able to process next.");
        } else {
            o0 poll = this.f23719a.poll();
            if (poll == null) {
                Log.d("TakePictureManager", "No new request.");
                return;
            }
            c0 c0Var = new c0(poll);
            l(c0Var);
            androidx.core.util.d<i, a0> e8 = this.f23720b.e(poll, c0Var);
            i iVar = e8.f4889a;
            Objects.requireNonNull(iVar);
            a0 a0Var = e8.f4890b;
            Objects.requireNonNull(a0Var);
            k(iVar, new j0(this, a0Var));
        }
    }

    public void i() {
        androidx.camera.core.impl.utils.m.a();
        this.f23723e = true;
    }

    public void j() {
        androidx.camera.core.impl.utils.m.a();
        this.f23723e = false;
        f();
    }
}
