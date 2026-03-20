package y;

import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h0 extends DeferrableSurface {

    /* renamed from: m  reason: collision with root package name */
    private final Surface f24298m;

    public h0(Surface surface) {
        this.f24298m = surface;
    }

    public h0(Surface surface, Size size, int i8) {
        super(size, i8);
        this.f24298m = surface;
    }

    @Override // androidx.camera.core.impl.DeferrableSurface
    public com.google.common.util.concurrent.d<Surface> n() {
        return a0.f.h(this.f24298m);
    }
}
