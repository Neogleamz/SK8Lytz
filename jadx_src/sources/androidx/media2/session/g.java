package androidx.media2.session;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import androidx.media.d;
import androidx.media2.common.MediaParcelUtils;
import androidx.media2.session.MediaSession;
import androidx.media2.session.MediaSessionService;
import androidx.media2.session.c;
import androidx.versionedparcelable.ParcelImpl;
import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements MediaSessionService.a {

    /* renamed from: b  reason: collision with root package name */
    a f6248b;

    /* renamed from: c  reason: collision with root package name */
    MediaSessionService f6249c;

    /* renamed from: e  reason: collision with root package name */
    private e f6251e;

    /* renamed from: a  reason: collision with root package name */
    private final Object f6247a = new Object();

    /* renamed from: d  reason: collision with root package name */
    private Map<String, MediaSession> f6250d = new k0.a();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends c.a implements Closeable {

        /* renamed from: a  reason: collision with root package name */
        final WeakReference<g> f6252a;

        /* renamed from: b  reason: collision with root package name */
        private final Handler f6253b;

        /* renamed from: c  reason: collision with root package name */
        private final androidx.media.d f6254c;

        /* renamed from: androidx.media2.session.g$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0067a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ d.b f6255a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ ConnectionRequest f6256b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ boolean f6257c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ Bundle f6258d;

            /* renamed from: e  reason: collision with root package name */
            final /* synthetic */ androidx.media2.session.a f6259e;

            /* renamed from: f  reason: collision with root package name */
            final /* synthetic */ String f6260f;

            /* renamed from: g  reason: collision with root package name */
            final /* synthetic */ int f6261g;

            /* renamed from: h  reason: collision with root package name */
            final /* synthetic */ int f6262h;

            RunnableC0067a(d.b bVar, ConnectionRequest connectionRequest, boolean z4, Bundle bundle, androidx.media2.session.a aVar, String str, int i8, int i9) {
                this.f6255a = bVar;
                this.f6256b = connectionRequest;
                this.f6257c = z4;
                this.f6258d = bundle;
                this.f6259e = aVar;
                this.f6260f = str;
                this.f6261g = i8;
                this.f6262h = i9;
            }

            /* JADX WARN: Removed duplicated region for block: B:32:0x00ae  */
            /* JADX WARN: Removed duplicated region for block: B:62:? A[RETURN, SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void run() {
                /*
                    r20 = this;
                    r1 = r20
                    java.lang.String r2 = "Notifying the controller of its disconnection"
                    java.lang.String r3 = "MSS2ImplBase"
                    r4 = 0
                    r5 = 1
                    androidx.media2.session.g$a r0 = androidx.media2.session.g.a.this     // Catch: java.lang.Throwable -> Lb7
                    java.lang.ref.WeakReference<androidx.media2.session.g> r0 = r0.f6252a     // Catch: java.lang.Throwable -> Lb7
                    java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> Lb7
                    androidx.media2.session.g r0 = (androidx.media2.session.g) r0     // Catch: java.lang.Throwable -> Lb7
                    if (r0 != 0) goto L22
                    java.lang.String r0 = "ServiceImpl isn't available"
                    android.util.Log.d(r3, r0)     // Catch: java.lang.Throwable -> Lb7
                    android.util.Log.d(r3, r2)
                    androidx.media2.session.a r0 = r1.f6259e     // Catch: android.os.RemoteException -> L21
                    r0.h(r4)     // Catch: android.os.RemoteException -> L21
                L21:
                    return
                L22:
                    androidx.media2.session.MediaSessionService r0 = r0.e()     // Catch: java.lang.Throwable -> Lb7
                    if (r0 != 0) goto L36
                    java.lang.String r0 = "Service isn't available"
                    android.util.Log.d(r3, r0)     // Catch: java.lang.Throwable -> Lb7
                    android.util.Log.d(r3, r2)
                    androidx.media2.session.a r0 = r1.f6259e     // Catch: android.os.RemoteException -> L35
                    r0.h(r4)     // Catch: android.os.RemoteException -> L35
                L35:
                    return
                L36:
                    androidx.media2.session.MediaSession$b r12 = new androidx.media2.session.MediaSession$b     // Catch: java.lang.Throwable -> Lb7
                    androidx.media.d$b r7 = r1.f6255a     // Catch: java.lang.Throwable -> Lb7
                    androidx.media2.session.ConnectionRequest r6 = r1.f6256b     // Catch: java.lang.Throwable -> Lb7
                    int r8 = r6.f()     // Catch: java.lang.Throwable -> Lb7
                    boolean r9 = r1.f6257c     // Catch: java.lang.Throwable -> Lb7
                    r10 = 0
                    android.os.Bundle r11 = r1.f6258d     // Catch: java.lang.Throwable -> Lb7
                    r6 = r12
                    r6.<init>(r7, r8, r9, r10, r11)     // Catch: java.lang.Throwable -> Lb7
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb7
                    r6.<init>()     // Catch: java.lang.Throwable -> Lb7
                    java.lang.String r7 = "Handling incoming connection request from the controller="
                    r6.append(r7)     // Catch: java.lang.Throwable -> Lb7
                    r6.append(r12)     // Catch: java.lang.Throwable -> Lb7
                    java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> Lb7
                    android.util.Log.d(r3, r6)     // Catch: java.lang.Throwable -> Lb7
                    androidx.media2.session.MediaSession r13 = r0.c(r12)     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    if (r13 != 0) goto L80
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    r0.<init>()     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    java.lang.String r6 = "Rejecting incoming connection request from the controller="
                    r0.append(r6)     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    r0.append(r12)     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    android.util.Log.w(r3, r0)     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    android.util.Log.d(r3, r2)
                    androidx.media2.session.a r0 = r1.f6259e     // Catch: android.os.RemoteException -> L7f
                    r0.h(r4)     // Catch: android.os.RemoteException -> L7f
                L7f:
                    return
                L80:
                    r0.a(r13)     // Catch: java.lang.Exception -> La6 java.lang.Throwable -> Lb7
                    androidx.media2.session.a r14 = r1.f6259e     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    androidx.media2.session.ConnectionRequest r0 = r1.f6256b     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    int r15 = r0.f()     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    java.lang.String r0 = r1.f6260f     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    int r5 = r1.f6261g     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    int r6 = r1.f6262h     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    android.os.Bundle r7 = r1.f6258d     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    r16 = r0
                    r17 = r5
                    r18 = r6
                    r19 = r7
                    r13.c(r14, r15, r16, r17, r18, r19)     // Catch: java.lang.Throwable -> La0 java.lang.Exception -> La3
                    r5 = r4
                    goto Lac
                La0:
                    r0 = move-exception
                    r5 = r4
                    goto Lb8
                La3:
                    r0 = move-exception
                    r5 = r4
                    goto La7
                La6:
                    r0 = move-exception
                La7:
                    java.lang.String r6 = "Failed to add a session to session service"
                    android.util.Log.w(r3, r6, r0)     // Catch: java.lang.Throwable -> Lb7
                Lac:
                    if (r5 == 0) goto Lb6
                    android.util.Log.d(r3, r2)
                    androidx.media2.session.a r0 = r1.f6259e     // Catch: android.os.RemoteException -> Lb6
                    r0.h(r4)     // Catch: android.os.RemoteException -> Lb6
                Lb6:
                    return
                Lb7:
                    r0 = move-exception
                Lb8:
                    if (r5 == 0) goto Lc2
                    android.util.Log.d(r3, r2)
                    androidx.media2.session.a r2 = r1.f6259e     // Catch: android.os.RemoteException -> Lc2
                    r2.h(r4)     // Catch: android.os.RemoteException -> Lc2
                Lc2:
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.media2.session.g.a.RunnableC0067a.run():void");
            }
        }

        a(g gVar) {
            this.f6252a = new WeakReference<>(gVar);
            this.f6253b = new Handler(gVar.e().getMainLooper());
            this.f6254c = androidx.media.d.a(gVar.e());
        }

        @Override // androidx.media2.session.c
        public void F0(androidx.media2.session.a aVar, ParcelImpl parcelImpl) {
            if (this.f6252a.get() == null) {
                Log.d("MSS2ImplBase", "ServiceImpl isn't available");
                return;
            }
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            ConnectionRequest connectionRequest = (ConnectionRequest) MediaParcelUtils.a(parcelImpl);
            if (callingPid == 0) {
                callingPid = connectionRequest.e();
            }
            int i8 = callingPid;
            String d8 = parcelImpl == null ? null : connectionRequest.d();
            Bundle c9 = parcelImpl == null ? null : connectionRequest.c();
            d.b bVar = new d.b(d8, i8, callingUid);
            try {
                this.f6253b.post(new RunnableC0067a(bVar, connectionRequest, this.f6254c.b(bVar), c9, aVar, d8, i8, callingUid));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.f6252a.clear();
            this.f6253b.removeCallbacksAndMessages(null);
        }
    }

    @Override // androidx.media2.session.MediaSessionService.a
    public IBinder a(Intent intent) {
        MediaSessionService e8 = e();
        if (e8 == null) {
            Log.w("MSS2ImplBase", "Service hasn't created before onBind()");
            return null;
        }
        String action = intent.getAction();
        action.hashCode();
        if (action.equals("androidx.media2.session.MediaSessionService")) {
            return f();
        }
        if (action.equals("android.media.browse.MediaBrowserService")) {
            MediaSession c9 = e8.c(MediaSession.b.a());
            if (c9 == null) {
                Log.d("MSS2ImplBase", "Rejecting incoming connection request from legacy media browsers.");
                return null;
            }
            c(c9);
            return c9.a();
        }
        return null;
    }

    @Override // androidx.media2.session.MediaSessionService.a
    public void b(MediaSessionService mediaSessionService) {
        synchronized (this.f6247a) {
            this.f6249c = mediaSessionService;
            this.f6248b = new a(this);
            this.f6251e = new e(mediaSessionService);
        }
    }

    @Override // androidx.media2.session.MediaSessionService.a
    public void c(MediaSession mediaSession) {
        MediaSession mediaSession2;
        synchronized (this.f6247a) {
            mediaSession2 = this.f6250d.get(mediaSession.e());
            if (mediaSession2 != null && mediaSession2 != mediaSession) {
                throw new IllegalArgumentException("Session ID should be unique");
            }
            this.f6250d.put(mediaSession.e(), mediaSession);
        }
        if (mediaSession2 != null) {
            return;
        }
        synchronized (this.f6247a) {
        }
        mediaSession.L();
        throw null;
    }

    @Override // androidx.media2.session.MediaSessionService.a
    public int d(Intent intent, int i8, int i9) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.MEDIA_BUTTON")) {
                MediaSessionService e8 = e();
                if (e8 == null) {
                    Log.wtf("MSS2ImplBase", "Service hasn't created");
                }
                MediaSession b9 = MediaSession.b(intent.getData());
                if (b9 == null) {
                    b9 = e8.c(MediaSession.b.a());
                }
                if (b9 == null) {
                    Log.d("MSS2ImplBase", "Rejecting wake-up of the service from media key events.");
                } else {
                    KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
                    if (keyEvent != null) {
                        b9.j1().b().a(keyEvent);
                    }
                }
            }
        }
        return 1;
    }

    MediaSessionService e() {
        MediaSessionService mediaSessionService;
        synchronized (this.f6247a) {
            mediaSessionService = this.f6249c;
        }
        return mediaSessionService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder f() {
        IBinder asBinder;
        synchronized (this.f6247a) {
            a aVar = this.f6248b;
            asBinder = aVar != null ? aVar.asBinder() : null;
        }
        return asBinder;
    }

    @Override // androidx.media2.session.MediaSessionService.a
    public void onDestroy() {
        synchronized (this.f6247a) {
            this.f6249c = null;
            a aVar = this.f6248b;
            if (aVar != null) {
                aVar.close();
                this.f6248b = null;
            }
        }
    }
}
