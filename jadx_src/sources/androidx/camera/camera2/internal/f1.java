package androidx.camera.camera2.internal;

import android.content.Context;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f1 implements UseCaseConfigFactory {

    /* renamed from: b  reason: collision with root package name */
    final x1 f1802b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f1803a;

        static {
            int[] iArr = new int[UseCaseConfigFactory.CaptureType.values().length];
            f1803a = iArr;
            try {
                iArr[UseCaseConfigFactory.CaptureType.IMAGE_CAPTURE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1803a[UseCaseConfigFactory.CaptureType.PREVIEW.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1803a[UseCaseConfigFactory.CaptureType.IMAGE_ANALYSIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1803a[UseCaseConfigFactory.CaptureType.VIDEO_CAPTURE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public f1(Context context) {
        this.f1802b = x1.b(context);
    }

    @Override // androidx.camera.core.impl.UseCaseConfigFactory
    public Config a(UseCaseConfigFactory.CaptureType captureType, int i8) {
        androidx.camera.core.impl.n P = androidx.camera.core.impl.n.P();
        SessionConfig.b bVar = new SessionConfig.b();
        int[] iArr = a.f1803a;
        int i9 = iArr[captureType.ordinal()];
        if (i9 == 1) {
            bVar.s(i8 == 2 ? 5 : 1);
        } else if (i9 == 2 || i9 == 3) {
            bVar.s(1);
        } else if (i9 == 4) {
            bVar.s(3);
        }
        UseCaseConfigFactory.CaptureType captureType2 = UseCaseConfigFactory.CaptureType.PREVIEW;
        if (captureType == captureType2) {
            v.m.a(bVar);
        }
        P.s(androidx.camera.core.impl.v.f2659n, bVar.m());
        P.s(androidx.camera.core.impl.v.f2661p, e1.f1786a);
        f.a aVar = new f.a();
        int i10 = iArr[captureType.ordinal()];
        if (i10 == 1) {
            aVar.p(i8 != 2 ? 2 : 5);
        } else if (i10 == 2 || i10 == 3) {
            aVar.p(1);
        } else if (i10 == 4) {
            aVar.p(3);
        }
        P.s(androidx.camera.core.impl.v.f2660o, aVar.h());
        P.s(androidx.camera.core.impl.v.q, captureType == UseCaseConfigFactory.CaptureType.IMAGE_CAPTURE ? d2.f1777c : l0.f1946a);
        if (captureType == captureType2) {
            P.s(androidx.camera.core.impl.l.f2581l, this.f1802b.d());
        }
        P.s(androidx.camera.core.impl.l.f2577h, Integer.valueOf(this.f1802b.c().getRotation()));
        if (captureType == UseCaseConfigFactory.CaptureType.VIDEO_CAPTURE) {
            P.s(androidx.camera.core.impl.v.f2665u, Boolean.TRUE);
        }
        return androidx.camera.core.impl.o.N(P);
    }
}
