package androidx.window.layout;

import android.app.Activity;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface WindowBackend {
    void registerLayoutChangeCallback(Activity activity, Executor executor, androidx.core.util.a<WindowLayoutInfo> aVar);

    void unregisterLayoutChangeCallback(androidx.core.util.a<WindowLayoutInfo> aVar);
}
