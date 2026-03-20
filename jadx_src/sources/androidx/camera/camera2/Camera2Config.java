package androidx.camera.camera2;

import android.content.Context;
import androidx.camera.camera2.internal.c1;
import androidx.camera.camera2.internal.f1;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.InitializationException;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.y;
import java.util.Set;
import q.a;
import q.b;
import q.c;
import y.o;
import y.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Camera2Config {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class DefaultProvider implements y.b {
        @Override // androidx.camera.core.y.b
        public y getCameraXConfig() {
            return Camera2Config.c();
        }
    }

    public static y c() {
        p.a aVar = c.a;
        o.a aVar2 = b.a;
        return new y.a().c(aVar).d(aVar2).g(a.a).a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ o d(Context context, Object obj, Set set) {
        try {
            return new c1(context, obj, set);
        } catch (CameraUnavailableException e8) {
            throw new InitializationException(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ UseCaseConfigFactory e(Context context) {
        return new f1(context);
    }
}
