package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import androidx.camera.core.impl.SessionConfig;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
interface t1 {
    void a();

    com.google.common.util.concurrent.d<Void> b(boolean z4);

    List<androidx.camera.core.impl.f> c();

    void close();

    void d(List<androidx.camera.core.impl.f> list);

    SessionConfig e();

    void f(SessionConfig sessionConfig);

    com.google.common.util.concurrent.d<Void> g(SessionConfig sessionConfig, CameraDevice cameraDevice, z2 z2Var);
}
