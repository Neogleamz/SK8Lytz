package androidx.media2.session;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.media2.session.MediaSession;
import androidx.media2.session.MediaSessionService;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class MediaLibraryService extends MediaSessionService {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class LibraryParams implements y1.b {

        /* renamed from: a  reason: collision with root package name */
        Bundle f6184a;

        /* renamed from: b  reason: collision with root package name */
        int f6185b;

        /* renamed from: c  reason: collision with root package name */
        int f6186c;

        /* renamed from: d  reason: collision with root package name */
        int f6187d;
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends MediaSession {
    }

    @Override // androidx.media2.session.MediaSessionService
    MediaSessionService.a b() {
        return new d();
    }

    @Override // androidx.media2.session.MediaSessionService
    /* renamed from: d */
    public abstract a c(MediaSession.b bVar);

    @Override // androidx.media2.session.MediaSessionService, android.app.Service
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
