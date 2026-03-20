package androidx.media2.session;

import android.content.Intent;
import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d extends g {
    @Override // androidx.media2.session.g, androidx.media2.session.MediaSessionService.a
    public IBinder a(Intent intent) {
        return "androidx.media2.session.MediaLibraryService".equals(intent.getAction()) ? f() : super.a(intent);
    }
}
