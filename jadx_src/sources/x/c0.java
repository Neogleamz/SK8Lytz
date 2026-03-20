package x;

import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.e1;
import androidx.camera.core.l1;
import androidx.concurrent.futures.c;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c0 implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final o0 f23702a;

    /* renamed from: c  reason: collision with root package name */
    private c.a<Void> f23704c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f23705d = false;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23706e = false;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f23703b = androidx.concurrent.futures.c.a(new b0(this));

    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(o0 o0Var) {
        this.f23702a = o0Var;
    }

    private void i() {
        androidx.core.util.h.k(this.f23703b.isDone(), "onImageCaptured() must be called before onFinalResult()");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object k(c.a aVar) {
        this.f23704c = aVar;
        return "CaptureCompleteFuture";
    }

    private void l() {
        androidx.core.util.h.k(!this.f23705d, "The callback can only complete once.");
        this.f23705d = true;
    }

    private void m(ImageCaptureException imageCaptureException) {
        androidx.camera.core.impl.utils.m.a();
        this.f23702a.q(imageCaptureException);
    }

    @Override // x.g0
    public boolean a() {
        return this.f23706e;
    }

    @Override // x.g0
    public void b(ImageCaptureException imageCaptureException) {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23706e) {
            return;
        }
        l();
        this.f23704c.c(null);
        m(imageCaptureException);
    }

    @Override // x.g0
    public void c() {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23706e) {
            return;
        }
        this.f23704c.c(null);
    }

    @Override // x.g0
    public void d(e1.n nVar) {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23706e) {
            return;
        }
        i();
        l();
        this.f23702a.r(nVar);
    }

    @Override // x.g0
    public void e(ImageCaptureException imageCaptureException) {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23706e) {
            return;
        }
        i();
        l();
        m(imageCaptureException);
    }

    @Override // x.g0
    public void f(l1 l1Var) {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23706e) {
            return;
        }
        i();
        l();
        this.f23702a.s(l1Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(ImageCaptureException imageCaptureException) {
        androidx.camera.core.impl.utils.m.a();
        this.f23706e = true;
        this.f23704c.c(null);
        m(imageCaptureException);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> j() {
        androidx.camera.core.impl.utils.m.a();
        return this.f23703b;
    }
}
