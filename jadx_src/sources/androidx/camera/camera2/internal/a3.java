package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.view.Surface;
import androidx.camera.camera2.internal.n2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a3 extends n2.a {

    /* renamed from: a  reason: collision with root package name */
    private final List<n2.a> f1721a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends n2.a {

        /* renamed from: a  reason: collision with root package name */
        private final CameraCaptureSession.StateCallback f1722a;

        a(CameraCaptureSession.StateCallback stateCallback) {
            this.f1722a = stateCallback;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(List<CameraCaptureSession.StateCallback> list) {
            this(h1.a(list));
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void a(n2 n2Var) {
            this.f1722a.onActive(n2Var.f().c());
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void o(n2 n2Var) {
            s.d.b(this.f1722a, n2Var.f().c());
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void p(n2 n2Var) {
            this.f1722a.onClosed(n2Var.f().c());
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void q(n2 n2Var) {
            this.f1722a.onConfigureFailed(n2Var.f().c());
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void r(n2 n2Var) {
            this.f1722a.onConfigured(n2Var.f().c());
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void s(n2 n2Var) {
            this.f1722a.onReady(n2Var.f().c());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.camera.camera2.internal.n2.a
        public void t(n2 n2Var) {
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void u(n2 n2Var, Surface surface) {
            s.b.a(this.f1722a, n2Var.f().c(), surface);
        }
    }

    a3(List<n2.a> list) {
        ArrayList arrayList = new ArrayList();
        this.f1721a = arrayList;
        arrayList.addAll(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static n2.a v(n2.a... aVarArr) {
        return new a3(Arrays.asList(aVarArr));
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void a(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.a(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void o(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.o(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void p(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.p(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void q(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.q(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void r(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.r(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void s(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.s(n2Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.n2.a
    public void t(n2 n2Var) {
        for (n2.a aVar : this.f1721a) {
            aVar.t(n2Var);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void u(n2 n2Var, Surface surface) {
        for (n2.a aVar : this.f1721a) {
            aVar.u(n2Var, surface);
        }
    }
}
