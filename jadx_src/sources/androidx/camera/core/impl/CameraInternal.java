package androidx.camera.core.impl;

import androidx.camera.core.CameraControl;
import androidx.camera.core.a3;
import java.util.Collection;
import y.p0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface CameraInternal extends androidx.camera.core.m, a3.d {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum State {
        PENDING_OPEN(false),
        OPENING(true),
        OPEN(true),
        CLOSING(true),
        CLOSED(false),
        RELEASING(true),
        RELEASED(false);
        

        /* renamed from: a  reason: collision with root package name */
        private final boolean f2469a;

        State(boolean z4) {
            this.f2469a = z4;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean c() {
            return this.f2469a;
        }
    }

    @Override // androidx.camera.core.m
    default CameraControl a() {
        return h();
    }

    @Override // androidx.camera.core.m
    default androidx.camera.core.s b() {
        return m();
    }

    default void e(d dVar) {
    }

    p0<State> g();

    CameraControlInternal h();

    default d i() {
        return y.n.a();
    }

    default void j(boolean z4) {
    }

    void k(Collection<a3> collection);

    void l(Collection<a3> collection);

    y.q m();
}
