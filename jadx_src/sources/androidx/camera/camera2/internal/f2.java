package androidx.camera.camera2.internal;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f2 {

    /* renamed from: a  reason: collision with root package name */
    private DeferrableSurface f1804a;

    /* renamed from: b  reason: collision with root package name */
    private final SessionConfig f1805b;

    /* renamed from: c  reason: collision with root package name */
    private final b f1806c;

    /* renamed from: d  reason: collision with root package name */
    private final v.p f1807d = new v.p();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Surface f1808a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ SurfaceTexture f1809b;

        a(Surface surface, SurfaceTexture surfaceTexture) {
            this.f1808a = surface;
            this.f1809b = surfaceTexture;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
            this.f1808a.release();
            this.f1809b.release();
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            throw new IllegalStateException("Future should never fail. Did it get completed by GC?", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements androidx.camera.core.impl.v<androidx.camera.core.a3> {
        private final Config A;

        b() {
            androidx.camera.core.impl.n P = androidx.camera.core.impl.n.P();
            P.s(androidx.camera.core.impl.v.f2661p, new e1());
            this.A = P;
        }

        @Override // androidx.camera.core.impl.q
        public Config l() {
            return this.A;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f2(s.y yVar, x1 x1Var) {
        b bVar = new b();
        this.f1806c = bVar;
        SurfaceTexture surfaceTexture = new SurfaceTexture(0);
        Size d8 = d(yVar, x1Var);
        androidx.camera.core.p1.a("MeteringRepeating", "MeteringSession SurfaceTexture size: " + d8);
        surfaceTexture.setDefaultBufferSize(d8.getWidth(), d8.getHeight());
        Surface surface = new Surface(surfaceTexture);
        SessionConfig.b o5 = SessionConfig.b.o(bVar);
        o5.s(1);
        y.h0 h0Var = new y.h0(surface);
        this.f1804a = h0Var;
        a0.f.b(h0Var.i(), new a(surface, surfaceTexture), z.a.a());
        o5.k(this.f1804a);
        this.f1805b = o5.m();
    }

    private Size d(s.y yVar, x1 x1Var) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) yVar.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            androidx.camera.core.p1.c("MeteringRepeating", "Can not retrieve SCALER_STREAM_CONFIGURATION_MAP.");
            return new Size(0, 0);
        }
        Size[] outputSizes = Build.VERSION.SDK_INT < 23 ? streamConfigurationMap.getOutputSizes(SurfaceTexture.class) : streamConfigurationMap.getOutputSizes(34);
        if (outputSizes == null) {
            androidx.camera.core.p1.c("MeteringRepeating", "Can not get output size list.");
            return new Size(0, 0);
        }
        Size[] a9 = this.f1807d.a(outputSizes);
        List asList = Arrays.asList(a9);
        Collections.sort(asList, new Comparator() { // from class: androidx.camera.camera2.internal.e2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int g8;
                g8 = f2.g((Size) obj, (Size) obj2);
                return g8;
            }
        });
        Size d8 = x1Var.d();
        long min = Math.min(d8.getWidth() * d8.getHeight(), 307200L);
        Size size = null;
        int length = a9.length;
        int i8 = 0;
        while (true) {
            if (i8 >= length) {
                break;
            }
            Size size2 = a9[i8];
            int i9 = ((size2.getWidth() * size2.getHeight()) > min ? 1 : ((size2.getWidth() * size2.getHeight()) == min ? 0 : -1));
            if (i9 == 0) {
                return size2;
            }
            if (i9 <= 0) {
                i8++;
                size = size2;
            } else if (size != null) {
                return size;
            }
        }
        return (Size) asList.get(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int g(Size size, Size size2) {
        return Long.signum((size.getWidth() * size.getHeight()) - (size2.getWidth() * size2.getHeight()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        androidx.camera.core.p1.a("MeteringRepeating", "MeteringRepeating clear!");
        DeferrableSurface deferrableSurface = this.f1804a;
        if (deferrableSurface != null) {
            deferrableSurface.c();
        }
        this.f1804a = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String c() {
        return "MeteringRepeating";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SessionConfig e() {
        return this.f1805b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.camera.core.impl.v<?> f() {
        return this.f1806c;
    }
}
