package androidx.media2.session;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.media2.session.MediaSession;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class MediaSessionService extends Service {

    /* renamed from: a  reason: collision with root package name */
    private final a f6201a = b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        IBinder a(Intent intent);

        void b(MediaSessionService mediaSessionService);

        void c(MediaSession mediaSession);

        int d(Intent intent, int i8, int i9);

        void onDestroy();
    }

    public final void a(MediaSession mediaSession) {
        Objects.requireNonNull(mediaSession, "session shouldn't be null");
        if (mediaSession.isClosed()) {
            throw new IllegalArgumentException("session is already closed");
        }
        this.f6201a.c(mediaSession);
    }

    a b() {
        return new g();
    }

    public abstract MediaSession c(MediaSession.b bVar);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f6201a.a(intent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f6201a.b(this);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.f6201a.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i8, int i9) {
        return this.f6201a.d(intent, i8, i9);
    }
}
