package e0;

import android.media.MediaCodec;
import androidx.camera.core.f3;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.y1;
import d0.f;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f19747a;

    public c() {
        this.f19747a = d0.a.a(f.class) != null;
    }

    private int b(DeferrableSurface deferrableSurface) {
        if (deferrableSurface.e() == MediaCodec.class || deferrableSurface.e() == f3.class) {
            return 2;
        }
        return deferrableSurface.e() == y1.class ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int c(SessionConfig.e eVar, SessionConfig.e eVar2) {
        return b(eVar.d()) - b(eVar2.d());
    }

    public void d(List<SessionConfig.e> list) {
        if (this.f19747a) {
            Collections.sort(list, new b(this));
        }
    }
}
