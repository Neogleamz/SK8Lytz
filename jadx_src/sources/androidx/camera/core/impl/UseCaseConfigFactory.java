package androidx.camera.core.impl;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface UseCaseConfigFactory {

    /* renamed from: a  reason: collision with root package name */
    public static final UseCaseConfigFactory f2523a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum CaptureType {
        IMAGE_CAPTURE,
        PREVIEW,
        IMAGE_ANALYSIS,
        VIDEO_CAPTURE
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements UseCaseConfigFactory {
        a() {
        }

        @Override // androidx.camera.core.impl.UseCaseConfigFactory
        public Config a(CaptureType captureType, int i8) {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        UseCaseConfigFactory a(Context context);
    }

    Config a(CaptureType captureType, int i8);
}
