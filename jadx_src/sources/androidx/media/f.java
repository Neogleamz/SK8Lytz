package androidx.media;

import android.content.Context;
import android.media.session.MediaSessionManager;
import androidx.media.d;
import androidx.media.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f extends e {

    /* renamed from: d  reason: collision with root package name */
    MediaSessionManager f6090d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a extends g.a {

        /* renamed from: d  reason: collision with root package name */
        final MediaSessionManager.RemoteUserInfo f6091d;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(String str, int i8, int i9) {
            super(str, i8, i9);
            this.f6091d = new MediaSessionManager.RemoteUserInfo(str, i8, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(Context context) {
        super(context);
        this.f6090d = (MediaSessionManager) context.getSystemService("media_session");
    }

    @Override // androidx.media.e, androidx.media.g, androidx.media.d.a
    public boolean a(d.c cVar) {
        return super.a(cVar);
    }
}
