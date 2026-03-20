package androidx.media2.session;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.media.d;
import androidx.media2.common.SessionPlayer;
import java.io.Closeable;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaSession implements Closeable {

    /* renamed from: b  reason: collision with root package name */
    private static final Object f6188b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private static final HashMap<String, MediaSession> f6189c = new HashMap<>();

    /* renamed from: a  reason: collision with root package name */
    private final c f6190a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class CommandButton implements y1.b {

        /* renamed from: a  reason: collision with root package name */
        SessionCommand f6191a;

        /* renamed from: b  reason: collision with root package name */
        int f6192b;

        /* renamed from: c  reason: collision with root package name */
        CharSequence f6193c;

        /* renamed from: d  reason: collision with root package name */
        Bundle f6194d;

        /* renamed from: e  reason: collision with root package name */
        boolean f6195e;
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class a {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final int f6196a;

        /* renamed from: b  reason: collision with root package name */
        private final d.b f6197b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f6198c;

        /* renamed from: d  reason: collision with root package name */
        private final a f6199d;

        /* renamed from: e  reason: collision with root package name */
        private final Bundle f6200e;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(d.b bVar, int i8, boolean z4, a aVar, Bundle bundle) {
            this.f6197b = bVar;
            this.f6196a = i8;
            this.f6198c = z4;
            if (bundle == null || h.c(bundle)) {
                this.f6200e = null;
            } else {
                this.f6200e = bundle;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static b a() {
            return new b(new d.b("android.media.session.MediaController", -1, -1), -1, false, null, null);
        }

        public boolean equals(Object obj) {
            if (obj instanceof b) {
                if (this == obj) {
                    return true;
                }
                b bVar = (b) obj;
                a aVar = bVar.f6199d;
                return this.f6197b.equals(bVar.f6197b);
            }
            return false;
        }

        public int hashCode() {
            return androidx.core.util.c.b(this.f6199d, this.f6197b);
        }

        public String toString() {
            return "ControllerInfo {pkg=" + this.f6197b.a() + ", uid=" + this.f6197b.b() + "})";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c extends Closeable {
        SessionPlayer L();

        String e();

        void e0(androidx.media2.session.a aVar, int i8, String str, int i9, int i10, Bundle bundle);

        IBinder h1();

        boolean isClosed();

        MediaSessionCompat j1();

        Uri v();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MediaSession b(Uri uri) {
        synchronized (f6188b) {
            for (MediaSession mediaSession : f6189c.values()) {
                if (androidx.core.util.c.a(mediaSession.v(), uri)) {
                    return mediaSession;
                }
            }
            return null;
        }
    }

    private Uri v() {
        return this.f6190a.v();
    }

    public SessionPlayer L() {
        return this.f6190a.L();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder a() {
        return this.f6190a.h1();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(androidx.media2.session.a aVar, int i8, String str, int i9, int i10, Bundle bundle) {
        this.f6190a.e0(aVar, i8, str, i9, i10, bundle);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            synchronized (f6188b) {
                f6189c.remove(this.f6190a.e());
            }
            this.f6190a.close();
        } catch (Exception unused) {
        }
    }

    public String e() {
        return this.f6190a.e();
    }

    public boolean isClosed() {
        return this.f6190a.isClosed();
    }

    public MediaSessionCompat j1() {
        return this.f6190a.j1();
    }
}
