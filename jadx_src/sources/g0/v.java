package g0;

import androidx.camera.core.SurfaceOutput;
import androidx.camera.core.q2;
import androidx.camera.core.z2;
import androidx.core.util.h;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v implements p {

    /* renamed from: a  reason: collision with root package name */
    private final q2 f20183a;

    /* renamed from: b  reason: collision with root package name */
    private final Executor f20184b;

    public v(q2 q2Var, Executor executor) {
        h.k(!(q2Var instanceof p), "SurfaceProcessorInternal should always be thread safe. Do not wrap.");
        this.f20183a = q2Var;
        this.f20184b = executor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(z2 z2Var) {
        this.f20183a.a(z2Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(SurfaceOutput surfaceOutput) {
        this.f20183a.b(surfaceOutput);
    }

    @Override // androidx.camera.core.q2
    public void a(z2 z2Var) {
        this.f20184b.execute(new u(this, z2Var));
    }

    @Override // androidx.camera.core.q2
    public void b(SurfaceOutput surfaceOutput) {
        this.f20184b.execute(new t(this, surfaceOutput));
    }

    @Override // g0.p
    public void release() {
    }
}
