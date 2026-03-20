package androidx.camera.core.impl;

import android.graphics.Rect;
import androidx.camera.core.CameraControl;
import androidx.camera.core.impl.SessionConfig;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface CameraControlInternal extends CameraControl {

    /* renamed from: a  reason: collision with root package name */
    public static final CameraControlInternal f2459a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class CameraControlException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        private CameraCaptureFailure f2460a;

        public CameraControlException(CameraCaptureFailure cameraCaptureFailure) {
            this.f2460a = cameraCaptureFailure;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements CameraControlInternal {
        a() {
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public void a(SessionConfig.b bVar) {
        }

        @Override // androidx.camera.core.CameraControl
        public com.google.common.util.concurrent.d<Void> b(float f5) {
            return a0.f.h(null);
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public com.google.common.util.concurrent.d<List<Void>> c(List<f> list, int i8, int i9) {
            return a0.f.h(Collections.emptyList());
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public void d(Config config) {
        }

        @Override // androidx.camera.core.CameraControl
        public com.google.common.util.concurrent.d<Void> e(float f5) {
            return a0.f.h(null);
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public Rect f() {
            return new Rect();
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public void g(int i8) {
        }

        @Override // androidx.camera.core.CameraControl
        public com.google.common.util.concurrent.d<Void> h(boolean z4) {
            return a0.f.h(null);
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public Config i() {
            return null;
        }

        @Override // androidx.camera.core.impl.CameraControlInternal
        public void j() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        void b(List<f> list);
    }

    void a(SessionConfig.b bVar);

    com.google.common.util.concurrent.d<List<Void>> c(List<f> list, int i8, int i9);

    void d(Config config);

    Rect f();

    void g(int i8);

    Config i();

    void j();
}
