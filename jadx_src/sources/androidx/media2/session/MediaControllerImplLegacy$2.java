package androidx.media2.session;

import android.os.Bundle;
import android.os.ResultReceiver;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class MediaControllerImplLegacy$2 extends ResultReceiver {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ androidx.concurrent.futures.d f6183a;

    @Override // android.os.ResultReceiver
    protected void onReceiveResult(int i8, Bundle bundle) {
        this.f6183a.y(new SessionResult(i8, bundle));
    }
}
