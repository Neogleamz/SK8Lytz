package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.a;
import android.support.v4.media.session.b;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.app.g;
import androidx.media.AudioAttributesCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaControllerCompat {

    /* renamed from: a  reason: collision with root package name */
    private final b f287a;

    /* renamed from: b  reason: collision with root package name */
    private final MediaSessionCompat.Token f288b;
    @SuppressLint({"BanConcurrentHashMap"})

    /* renamed from: c  reason: collision with root package name */
    private final ConcurrentHashMap<a, Boolean> f289c = new ConcurrentHashMap<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class MediaControllerImplApi21 implements b {

        /* renamed from: a  reason: collision with root package name */
        protected final MediaController f290a;

        /* renamed from: b  reason: collision with root package name */
        final Object f291b = new Object();

        /* renamed from: c  reason: collision with root package name */
        private final List<a> f292c = new ArrayList();

        /* renamed from: d  reason: collision with root package name */
        private HashMap<a, a> f293d = new HashMap<>();

        /* renamed from: e  reason: collision with root package name */
        final MediaSessionCompat.Token f294e;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class ExtraBinderRequestResultReceiver extends ResultReceiver {

            /* renamed from: a  reason: collision with root package name */
            private WeakReference<MediaControllerImplApi21> f295a;

            ExtraBinderRequestResultReceiver(MediaControllerImplApi21 mediaControllerImplApi21) {
                super(null);
                this.f295a = new WeakReference<>(mediaControllerImplApi21);
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i8, Bundle bundle) {
                MediaControllerImplApi21 mediaControllerImplApi21 = this.f295a.get();
                if (mediaControllerImplApi21 == null || bundle == null) {
                    return;
                }
                synchronized (mediaControllerImplApi21.f291b) {
                    mediaControllerImplApi21.f294e.g(b.a.d(g.a(bundle, "android.support.v4.media.session.EXTRA_BINDER")));
                    mediaControllerImplApi21.f294e.h(y1.a.b(bundle, "android.support.v4.media.session.SESSION_TOKEN2"));
                    mediaControllerImplApi21.b();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a extends a.b {
            a(a aVar) {
                super(aVar);
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void J(Bundle bundle) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void O0(CharSequence charSequence) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void P(List<MediaSessionCompat.QueueItem> list) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void X0() {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void Z0(MediaMetadataCompat mediaMetadataCompat) {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.a.b, android.support.v4.media.session.a
            public void e2(ParcelableVolumeInfo parcelableVolumeInfo) {
                throw new AssertionError();
            }
        }

        MediaControllerImplApi21(Context context, MediaSessionCompat.Token token) {
            this.f294e = token;
            this.f290a = new MediaController(context, (MediaSession.Token) token.f());
            if (token.d() == null) {
                c();
            }
        }

        private void c() {
            d("android.support.v4.media.session.command.GET_EXTRA_BINDER", null, new ExtraBinderRequestResultReceiver(this));
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.b
        public boolean a(KeyEvent keyEvent) {
            return this.f290a.dispatchMediaButtonEvent(keyEvent);
        }

        void b() {
            if (this.f294e.d() == null) {
                return;
            }
            for (a aVar : this.f292c) {
                a aVar2 = new a(aVar);
                this.f293d.put(aVar, aVar2);
                aVar.f297b = aVar2;
                try {
                    this.f294e.d().i(aVar2);
                    aVar.i(13, null, null);
                } catch (RemoteException e8) {
                    Log.e("MediaControllerCompat", "Dead object in registerCallback.", e8);
                }
            }
            this.f292c.clear();
        }

        public void d(String str, Bundle bundle, ResultReceiver resultReceiver) {
            this.f290a.sendCommand(str, bundle, resultReceiver);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a implements IBinder.DeathRecipient {

        /* renamed from: a  reason: collision with root package name */
        final MediaController.Callback f296a;

        /* renamed from: b  reason: collision with root package name */
        android.support.v4.media.session.a f297b;

        /* renamed from: android.support.v4.media.session.MediaControllerCompat$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0004a extends MediaController.Callback {

            /* renamed from: a  reason: collision with root package name */
            private final WeakReference<a> f298a;

            C0004a(a aVar) {
                this.f298a = new WeakReference<>(aVar);
            }

            @Override // android.media.session.MediaController.Callback
            public void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.a(new d(playbackInfo.getPlaybackType(), AudioAttributesCompat.f(playbackInfo.getAudioAttributes()), playbackInfo.getVolumeControl(), playbackInfo.getMaxVolume(), playbackInfo.getCurrentVolume()));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onExtrasChanged(Bundle bundle) {
                MediaSessionCompat.a(bundle);
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.b(bundle);
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onMetadataChanged(MediaMetadata mediaMetadata) {
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.c(MediaMetadataCompat.a(mediaMetadata));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onPlaybackStateChanged(PlaybackState playbackState) {
                a aVar = this.f298a.get();
                if (aVar == null || aVar.f297b != null) {
                    return;
                }
                aVar.d(PlaybackStateCompat.a(playbackState));
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueChanged(List<MediaSession.QueueItem> list) {
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.e(MediaSessionCompat.QueueItem.b(list));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueTitleChanged(CharSequence charSequence) {
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.f(charSequence);
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionDestroyed() {
                a aVar = this.f298a.get();
                if (aVar != null) {
                    aVar.g();
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionEvent(String str, Bundle bundle) {
                MediaSessionCompat.a(bundle);
                a aVar = this.f298a.get();
                if (aVar != null) {
                    if (aVar.f297b == null || Build.VERSION.SDK_INT >= 23) {
                        aVar.h(str, bundle);
                    }
                }
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class b extends a.AbstractBinderC0005a {

            /* renamed from: a  reason: collision with root package name */
            private final WeakReference<a> f299a;

            b(a aVar) {
                this.f299a = new WeakReference<>(aVar);
            }

            @Override // android.support.v4.media.session.a
            public void C() {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(13, null, null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void G0(boolean z4) {
            }

            public void J(Bundle bundle) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(7, bundle, null);
                }
            }

            public void O0(CharSequence charSequence) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(6, charSequence, null);
                }
            }

            public void P(List<MediaSessionCompat.QueueItem> list) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(5, list, null);
                }
            }

            public void X0() {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(8, null, null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void X1(PlaybackStateCompat playbackStateCompat) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(2, playbackStateCompat, null);
                }
            }

            public void Z0(MediaMetadataCompat mediaMetadataCompat) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(3, mediaMetadataCompat, null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void Z1(String str, Bundle bundle) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(1, str, bundle);
                }
            }

            public void e2(ParcelableVolumeInfo parcelableVolumeInfo) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(4, parcelableVolumeInfo != null ? new d(parcelableVolumeInfo.f316a, parcelableVolumeInfo.f317b, parcelableVolumeInfo.f318c, parcelableVolumeInfo.f319d, parcelableVolumeInfo.f320e) : null, null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void l(int i8) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(9, Integer.valueOf(i8), null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void n0(boolean z4) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(11, Boolean.valueOf(z4), null);
                }
            }

            @Override // android.support.v4.media.session.a
            public void w1(int i8) {
                a aVar = this.f299a.get();
                if (aVar != null) {
                    aVar.i(12, Integer.valueOf(i8), null);
                }
            }
        }

        public a() {
            if (Build.VERSION.SDK_INT >= 21) {
                this.f296a = new C0004a(this);
                return;
            }
            this.f296a = null;
            this.f297b = new b(this);
        }

        public void a(d dVar) {
        }

        public void b(Bundle bundle) {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            i(8, null, null);
        }

        public void c(MediaMetadataCompat mediaMetadataCompat) {
        }

        public void d(PlaybackStateCompat playbackStateCompat) {
        }

        public void e(List<MediaSessionCompat.QueueItem> list) {
        }

        public void f(CharSequence charSequence) {
        }

        public void g() {
        }

        public void h(String str, Bundle bundle) {
        }

        void i(int i8, Object obj, Bundle bundle) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface b {
        boolean a(KeyEvent keyEvent);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements b {

        /* renamed from: a  reason: collision with root package name */
        private android.support.v4.media.session.b f300a;

        c(MediaSessionCompat.Token token) {
            this.f300a = b.a.d((IBinder) token.f());
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.b
        public boolean a(KeyEvent keyEvent) {
            if (keyEvent != null) {
                try {
                    this.f300a.E0(keyEvent);
                    return false;
                } catch (RemoteException e8) {
                    Log.e("MediaControllerCompat", "Dead object in dispatchMediaButtonEvent.", e8);
                    return false;
                }
            }
            throw new IllegalArgumentException("event may not be null.");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private final int f301a;

        /* renamed from: b  reason: collision with root package name */
        private final AudioAttributesCompat f302b;

        /* renamed from: c  reason: collision with root package name */
        private final int f303c;

        /* renamed from: d  reason: collision with root package name */
        private final int f304d;

        /* renamed from: e  reason: collision with root package name */
        private final int f305e;

        d(int i8, int i9, int i10, int i11, int i12) {
            this(i8, new AudioAttributesCompat.a().d(i9).a(), i10, i11, i12);
        }

        d(int i8, AudioAttributesCompat audioAttributesCompat, int i9, int i10, int i11) {
            this.f301a = i8;
            this.f302b = audioAttributesCompat;
            this.f303c = i9;
            this.f304d = i10;
            this.f305e = i11;
        }
    }

    public MediaControllerCompat(Context context, MediaSessionCompat.Token token) {
        if (token == null) {
            throw new IllegalArgumentException("sessionToken must not be null");
        }
        this.f288b = token;
        if (Build.VERSION.SDK_INT >= 21) {
            this.f287a = new MediaControllerImplApi21(context, token);
        } else {
            this.f287a = new c(token);
        }
    }

    public boolean a(KeyEvent keyEvent) {
        if (keyEvent != null) {
            return this.f287a.a(keyEvent);
        }
        throw new IllegalArgumentException("KeyEvent may not be null");
    }
}
