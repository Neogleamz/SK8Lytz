package w;

import a0.f;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.camera.core.impl.Config;
import androidx.concurrent.futures.c;
import com.google.common.util.concurrent.d;
import java.util.concurrent.Executor;
import r.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: c  reason: collision with root package name */
    private final t f23366c;

    /* renamed from: d  reason: collision with root package name */
    final Executor f23367d;

    /* renamed from: g  reason: collision with root package name */
    c.a<Void> f23370g;

    /* renamed from: a  reason: collision with root package name */
    private boolean f23364a = false;

    /* renamed from: b  reason: collision with root package name */
    private boolean f23365b = false;

    /* renamed from: e  reason: collision with root package name */
    final Object f23368e = new Object();

    /* renamed from: f  reason: collision with root package name */
    private a.C0201a f23369f = new a.C0201a();

    /* renamed from: h  reason: collision with root package name */
    private final t.c f23371h = new a(this);

    public g(t tVar, Executor executor) {
        this.f23366c = tVar;
        this.f23367d = executor;
    }

    private void h(j jVar) {
        synchronized (this.f23368e) {
            for (Config.a<?> aVar : jVar.e()) {
                this.f23369f.a().s(aVar, jVar.a(aVar));
            }
        }
    }

    private void j() {
        synchronized (this.f23368e) {
            this.f23369f = new a.C0201a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object n(c.a aVar) {
        this.f23367d.execute(new e(this, aVar));
        return "addCaptureRequestOptions";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object p(c.a aVar) {
        this.f23367d.execute(new d(this, aVar));
        return "clearCaptureRequestOptions";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ boolean q(android.hardware.camera2.TotalCaptureResult r3) {
        /*
            r2 = this;
            androidx.concurrent.futures.c$a<java.lang.Void> r0 = r2.f23370g
            r1 = 0
            if (r0 == 0) goto L32
            android.hardware.camera2.CaptureRequest r3 = r3.getRequest()
            java.lang.Object r3 = r3.getTag()
            boolean r0 = r3 instanceof y.a1
            if (r0 == 0) goto L32
            y.a1 r3 = (y.a1) r3
            java.lang.String r0 = "Camera2CameraControl"
            java.lang.Object r3 = r3.c(r0)
            java.lang.Integer r3 = (java.lang.Integer) r3
            if (r3 == 0) goto L32
            androidx.concurrent.futures.c$a<java.lang.Void> r0 = r2.f23370g
            int r0 = r0.hashCode()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L32
            androidx.concurrent.futures.c$a<java.lang.Void> r3 = r2.f23370g
            r2.f23370g = r1
            goto L33
        L32:
            r3 = r1
        L33:
            if (r3 == 0) goto L38
            r3.c(r1)
        L38:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: w.g.q(android.hardware.camera2.TotalCaptureResult):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: t */
    public void r(boolean z4) {
        if (this.f23364a == z4) {
            return;
        }
        this.f23364a = z4;
        if (z4) {
            if (this.f23365b) {
                v();
                return;
            }
            return;
        }
        c.a<Void> aVar = this.f23370g;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException("The camera control has became inactive."));
            this.f23370g = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: u */
    public void o(c.a<Void> aVar) {
        this.f23365b = true;
        c.a<Void> aVar2 = this.f23370g;
        if (aVar2 == null) {
            aVar2 = null;
        }
        this.f23370g = aVar;
        if (this.f23364a) {
            v();
        }
        if (aVar2 != null) {
            aVar2.f(new CameraControl.OperationCanceledException("Camera2CameraControl was updated with new options."));
        }
    }

    private void v() {
        this.f23366c.g0();
        this.f23365b = false;
    }

    public d<Void> g(j jVar) {
        h(jVar);
        return f.j(c.a(new c(this)));
    }

    public d<Void> i() {
        j();
        return f.j(c.a(new b(this)));
    }

    public a k() {
        a c9;
        synchronized (this.f23368e) {
            if (this.f23370g != null) {
                this.f23369f.a().s(a.H, Integer.valueOf(this.f23370g.hashCode()));
            }
            c9 = this.f23369f.c();
        }
        return c9;
    }

    public t.c l() {
        return this.f23371h;
    }

    public void s(boolean z4) {
        this.f23367d.execute(new f(this, z4));
    }
}
