package w;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.core.g0;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.m;
import androidx.camera.core.impl.n;
import androidx.camera.core.impl.o;
import androidx.camera.core.impl.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j implements q {
    private final Config A;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements g0<j> {

        /* renamed from: a  reason: collision with root package name */
        private final n f23373a = n.P();

        public static a e(Config config) {
            a aVar = new a();
            config.c("camera2.captureRequest.option.", new i(aVar, config));
            return aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean f(a aVar, Config config, Config.a aVar2) {
            aVar.a().o(aVar2, config.g(aVar2), config.a(aVar2));
            return true;
        }

        @Override // androidx.camera.core.g0
        public m a() {
            return this.f23373a;
        }

        public j d() {
            return new j(o.N(this.f23373a));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <ValueT> a g(CaptureRequest.Key<ValueT> key, ValueT valuet) {
            this.f23373a.s(r.a.L(key), valuet);
            return this;
        }
    }

    public j(Config config) {
        this.A = config;
    }

    @Override // androidx.camera.core.impl.q
    public Config l() {
        return this.A;
    }
}
