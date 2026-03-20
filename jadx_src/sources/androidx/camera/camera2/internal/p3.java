package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageWriter;
import android.os.Build;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p3 implements l3 {

    /* renamed from: a  reason: collision with root package name */
    private final Map<Integer, Size> f2027a;

    /* renamed from: b  reason: collision with root package name */
    private final s.y f2028b;

    /* renamed from: f  reason: collision with root package name */
    private boolean f2032f;

    /* renamed from: g  reason: collision with root package name */
    androidx.camera.core.m2 f2033g;

    /* renamed from: h  reason: collision with root package name */
    private y.h f2034h;

    /* renamed from: i  reason: collision with root package name */
    private DeferrableSurface f2035i;

    /* renamed from: j  reason: collision with root package name */
    ImageWriter f2036j;

    /* renamed from: d  reason: collision with root package name */
    private boolean f2030d = false;

    /* renamed from: e  reason: collision with root package name */
    private boolean f2031e = false;

    /* renamed from: c  reason: collision with root package name */
    final f0.d f2029c = new f0.d(3, new f0.b() { // from class: androidx.camera.camera2.internal.m3
        @Override // f0.b
        public final void a(Object obj) {
            ((androidx.camera.core.l1) obj).close();
        }
    });

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends CameraCaptureSession.StateCallback {
        a() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            Surface inputSurface = cameraCaptureSession.getInputSurface();
            if (inputSurface != null) {
                p3.this.f2036j = c0.a.c(inputSurface, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p3(s.y yVar) {
        this.f2032f = false;
        this.f2028b = yVar;
        this.f2032f = r3.a(yVar, 4);
        this.f2027a = k(yVar);
    }

    private void j() {
        f0.d dVar = this.f2029c;
        while (!dVar.c()) {
            dVar.a().close();
        }
        DeferrableSurface deferrableSurface = this.f2035i;
        if (deferrableSurface != null) {
            androidx.camera.core.m2 m2Var = this.f2033g;
            if (m2Var != null) {
                deferrableSurface.i().c(new n3(m2Var), z.a.d());
                this.f2033g = null;
            }
            deferrableSurface.c();
            this.f2035i = null;
        }
        ImageWriter imageWriter = this.f2036j;
        if (imageWriter != null) {
            imageWriter.close();
            this.f2036j = null;
        }
    }

    private Map<Integer, Size> k(s.y yVar) {
        int[] inputFormats;
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) yVar.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null || streamConfigurationMap.getInputFormats() == null) {
            return new HashMap();
        }
        HashMap hashMap = new HashMap();
        for (int i8 : streamConfigurationMap.getInputFormats()) {
            Size[] inputSizes = streamConfigurationMap.getInputSizes(i8);
            if (inputSizes != null) {
                Arrays.sort(inputSizes, new androidx.camera.core.impl.utils.d(true));
                hashMap.put(Integer.valueOf(i8), inputSizes[0]);
            }
        }
        return hashMap;
    }

    private boolean l(s.y yVar, int i8) {
        int[] validOutputFormatsForInput;
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) yVar.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null || (validOutputFormatsForInput = streamConfigurationMap.getValidOutputFormatsForInput(i8)) == null) {
            return false;
        }
        for (int i9 : validOutputFormatsForInput) {
            if (i9 == 256) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m(y.g0 g0Var) {
        try {
            androidx.camera.core.l1 acquireLatestImage = g0Var.acquireLatestImage();
            if (acquireLatestImage != null) {
                this.f2029c.d(acquireLatestImage);
            }
        } catch (IllegalStateException e8) {
            androidx.camera.core.p1.c("ZslControlImpl", "Failed to acquire latest image IllegalStateException = " + e8.getMessage());
        }
    }

    @Override // androidx.camera.camera2.internal.l3
    public void a(SessionConfig.b bVar) {
        j();
        if (!this.f2030d && this.f2032f && !this.f2027a.isEmpty() && this.f2027a.containsKey(34) && l(this.f2028b, 34)) {
            Size size = this.f2027a.get(34);
            androidx.camera.core.s1 s1Var = new androidx.camera.core.s1(size.getWidth(), size.getHeight(), 34, 9);
            this.f2034h = s1Var.l();
            this.f2033g = new androidx.camera.core.m2(s1Var);
            s1Var.a(new g0.a() { // from class: androidx.camera.camera2.internal.o3
                @Override // y.g0.a
                public final void a(y.g0 g0Var) {
                    p3.this.m(g0Var);
                }
            }, z.a.c());
            y.h0 h0Var = new y.h0(this.f2033g.getSurface(), new Size(this.f2033g.getWidth(), this.f2033g.getHeight()), 34);
            this.f2035i = h0Var;
            androidx.camera.core.m2 m2Var = this.f2033g;
            com.google.common.util.concurrent.d<Void> i8 = h0Var.i();
            Objects.requireNonNull(m2Var);
            i8.c(new n3(m2Var), z.a.d());
            bVar.k(this.f2035i);
            bVar.d(this.f2034h);
            bVar.j(new a());
            bVar.r(new InputConfiguration(this.f2033g.getWidth(), this.f2033g.getHeight(), this.f2033g.c()));
        }
    }

    @Override // androidx.camera.camera2.internal.l3
    public boolean b() {
        return this.f2030d;
    }

    @Override // androidx.camera.camera2.internal.l3
    public boolean c() {
        return this.f2031e;
    }

    @Override // androidx.camera.camera2.internal.l3
    public void d(boolean z4) {
        this.f2031e = z4;
    }

    @Override // androidx.camera.camera2.internal.l3
    public void e(boolean z4) {
        this.f2030d = z4;
    }

    @Override // androidx.camera.camera2.internal.l3
    public androidx.camera.core.l1 f() {
        try {
            return this.f2029c.a();
        } catch (NoSuchElementException unused) {
            androidx.camera.core.p1.c("ZslControlImpl", "dequeueImageFromBuffer no such element");
            return null;
        }
    }

    @Override // androidx.camera.camera2.internal.l3
    public boolean g(androidx.camera.core.l1 l1Var) {
        ImageWriter imageWriter;
        Image B1 = l1Var.B1();
        if (Build.VERSION.SDK_INT >= 23 && (imageWriter = this.f2036j) != null && B1 != null) {
            try {
                c0.a.e(imageWriter, B1);
                return true;
            } catch (IllegalStateException e8) {
                androidx.camera.core.p1.c("ZslControlImpl", "enqueueImageToImageWriter throws IllegalStateException = " + e8.getMessage());
            }
        }
        return false;
    }
}
