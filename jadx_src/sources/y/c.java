package y;

import android.os.Handler;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends t {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f24287a;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f24288b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Executor executor, Handler handler) {
        Objects.requireNonNull(executor, "Null cameraExecutor");
        this.f24287a = executor;
        Objects.requireNonNull(handler, "Null schedulerHandler");
        this.f24288b = handler;
    }

    @Override // y.t
    public Executor b() {
        return this.f24287a;
    }

    @Override // y.t
    public Handler c() {
        return this.f24288b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof t) {
            t tVar = (t) obj;
            return this.f24287a.equals(tVar.b()) && this.f24288b.equals(tVar.c());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f24287a.hashCode() ^ 1000003) * 1000003) ^ this.f24288b.hashCode();
    }

    public String toString() {
        return "CameraThreadConfig{cameraExecutor=" + this.f24287a + ", schedulerHandler=" + this.f24288b + "}";
    }
}
