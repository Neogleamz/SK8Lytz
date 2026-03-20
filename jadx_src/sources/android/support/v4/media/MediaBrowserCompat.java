package android.support.v4.media;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.b;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaBrowserCompat {

    /* renamed from: b  reason: collision with root package name */
    static final boolean f212b = Log.isLoggable("MediaBrowserCompat", 3);

    /* renamed from: a  reason: collision with root package name */
    private final d f213a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class CustomActionResultReceiver extends ResultReceiver {
        @Override // android.support.v4.os.ResultReceiver
        protected void a(int i8, Bundle bundle) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class ItemReceiver extends ResultReceiver {
        @Override // android.support.v4.os.ResultReceiver
        protected void a(int i8, Bundle bundle) {
            if (bundle != null) {
                bundle = MediaSessionCompat.c(bundle);
            }
            if (i8 != 0 || bundle == null || !bundle.containsKey("media_item")) {
                throw null;
            }
            Parcelable parcelable = bundle.getParcelable("media_item");
            if (parcelable != null && !(parcelable instanceof MediaItem)) {
                throw null;
            }
            MediaItem mediaItem = (MediaItem) parcelable;
            throw null;
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class MediaItem implements Parcelable {
        public static final Parcelable.Creator<MediaItem> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private final int f214a;

        /* renamed from: b  reason: collision with root package name */
        private final MediaDescriptionCompat f215b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<MediaItem> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public MediaItem createFromParcel(Parcel parcel) {
                return new MediaItem(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public MediaItem[] newArray(int i8) {
                return new MediaItem[i8];
            }
        }

        MediaItem(Parcel parcel) {
            this.f214a = parcel.readInt();
            this.f215b = MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }

        public MediaItem(MediaDescriptionCompat mediaDescriptionCompat, int i8) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (TextUtils.isEmpty(mediaDescriptionCompat.c())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            }
            this.f214a = i8;
            this.f215b = mediaDescriptionCompat;
        }

        public static MediaItem a(Object obj) {
            if (obj == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            MediaBrowser.MediaItem mediaItem = (MediaBrowser.MediaItem) obj;
            return new MediaItem(MediaDescriptionCompat.a(a.a(mediaItem)), a.b(mediaItem));
        }

        public static List<MediaItem> b(List<?> list) {
            if (list == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList arrayList = new ArrayList(list.size());
            Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(a(it.next()));
            }
            return arrayList;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String toString() {
            return "MediaItem{mFlags=" + this.f214a + ", mDescription=" + this.f215b + '}';
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f214a);
            this.f215b.writeToParcel(parcel, i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class SearchResultReceiver extends ResultReceiver {
        @Override // android.support.v4.os.ResultReceiver
        protected void a(int i8, Bundle bundle) {
            if (bundle != null) {
                bundle = MediaSessionCompat.c(bundle);
            }
            if (i8 != 0 || bundle == null || !bundle.containsKey("search_results")) {
                throw null;
            }
            Parcelable[] parcelableArray = bundle.getParcelableArray("search_results");
            Objects.requireNonNull(parcelableArray);
            ArrayList arrayList = new ArrayList();
            for (Parcelable parcelable : parcelableArray) {
                arrayList.add((MediaItem) parcelable);
            }
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static MediaDescription a(MediaBrowser.MediaItem mediaItem) {
            return mediaItem.getDescription();
        }

        static int b(MediaBrowser.MediaItem mediaItem) {
            return mediaItem.getFlags();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends Handler {

        /* renamed from: a  reason: collision with root package name */
        private final WeakReference<i> f216a;

        /* renamed from: b  reason: collision with root package name */
        private WeakReference<Messenger> f217b;

        b(i iVar) {
            this.f216a = new WeakReference<>(iVar);
        }

        void a(Messenger messenger) {
            this.f217b = new WeakReference<>(messenger);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakReference<Messenger> weakReference = this.f217b;
            if (weakReference == null || weakReference.get() == null || this.f216a.get() == null) {
                return;
            }
            Bundle data = message.getData();
            MediaSessionCompat.a(data);
            i iVar = this.f216a.get();
            Messenger messenger = this.f217b.get();
            try {
                int i8 = message.what;
                if (i8 == 1) {
                    Bundle bundle = data.getBundle("data_root_hints");
                    MediaSessionCompat.a(bundle);
                    iVar.c(messenger, data.getString("data_media_item_id"), (MediaSessionCompat.Token) data.getParcelable("data_media_session_token"), bundle);
                } else if (i8 == 2) {
                    iVar.f(messenger);
                } else if (i8 != 3) {
                    Log.w("MediaBrowserCompat", "Unhandled message: " + message + "\n  Client version: 1\n  Service version: " + message.arg1);
                } else {
                    Bundle bundle2 = data.getBundle("data_options");
                    MediaSessionCompat.a(bundle2);
                    Bundle bundle3 = data.getBundle("data_notify_children_changed_options");
                    MediaSessionCompat.a(bundle3);
                    iVar.d(messenger, data.getString("data_media_item_id"), data.getParcelableArrayList("data_media_item_list"), bundle2, bundle3);
                }
            } catch (BadParcelableException unused) {
                Log.e("MediaBrowserCompat", "Could not unparcel the data.");
                if (message.what == 1) {
                    iVar.f(messenger);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        final MediaBrowser.ConnectionCallback f218a;

        /* renamed from: b  reason: collision with root package name */
        b f219b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private class a extends MediaBrowser.ConnectionCallback {
            a() {
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnected() {
                b bVar = c.this.f219b;
                if (bVar != null) {
                    bVar.onConnected();
                }
                c.this.a();
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnectionFailed() {
                b bVar = c.this.f219b;
                if (bVar != null) {
                    bVar.g();
                }
                c.this.b();
            }

            @Override // android.media.browse.MediaBrowser.ConnectionCallback
            public void onConnectionSuspended() {
                b bVar = c.this.f219b;
                if (bVar != null) {
                    bVar.e();
                }
                c.this.c();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface b {
            void e();

            void g();

            void onConnected();
        }

        public c() {
            this.f218a = Build.VERSION.SDK_INT >= 21 ? new a() : null;
        }

        public void a() {
            throw null;
        }

        public void b() {
            throw null;
        }

        public void c() {
            throw null;
        }

        void d(b bVar) {
            this.f219b = bVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface d {
        void a();

        MediaSessionCompat.Token b();

        void disconnect();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e implements d, i, c.b {

        /* renamed from: a  reason: collision with root package name */
        final Context f221a;

        /* renamed from: b  reason: collision with root package name */
        protected final MediaBrowser f222b;

        /* renamed from: c  reason: collision with root package name */
        protected final Bundle f223c;

        /* renamed from: d  reason: collision with root package name */
        protected final b f224d = new b(this);

        /* renamed from: e  reason: collision with root package name */
        private final k0.a<String, k> f225e = new k0.a<>();

        /* renamed from: f  reason: collision with root package name */
        protected int f226f;

        /* renamed from: g  reason: collision with root package name */
        protected j f227g;

        /* renamed from: h  reason: collision with root package name */
        protected Messenger f228h;

        /* renamed from: i  reason: collision with root package name */
        private MediaSessionCompat.Token f229i;

        /* renamed from: j  reason: collision with root package name */
        private Bundle f230j;

        e(Context context, ComponentName componentName, c cVar, Bundle bundle) {
            this.f221a = context;
            Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
            this.f223c = bundle2;
            bundle2.putInt("extra_client_version", 1);
            bundle2.putInt("extra_calling_pid", Process.myPid());
            cVar.d(this);
            this.f222b = new MediaBrowser(context, componentName, cVar.f218a, bundle2);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public void a() {
            this.f222b.connect();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public MediaSessionCompat.Token b() {
            if (this.f229i == null) {
                this.f229i = MediaSessionCompat.Token.b(this.f222b.getSessionToken());
            }
            return this.f229i;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void c(Messenger messenger, String str, MediaSessionCompat.Token token, Bundle bundle) {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void d(Messenger messenger, String str, List<MediaItem> list, Bundle bundle, Bundle bundle2) {
            if (this.f228h != messenger) {
                return;
            }
            k kVar = this.f225e.get(str);
            if (kVar == null) {
                if (MediaBrowserCompat.f212b) {
                    Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + str);
                    return;
                }
                return;
            }
            l a9 = kVar.a(bundle);
            if (a9 != null) {
                if (bundle == null) {
                    if (list == null) {
                        a9.c(str);
                        return;
                    } else {
                        this.f230j = bundle2;
                        a9.a(str, list);
                    }
                } else if (list == null) {
                    a9.d(str, bundle);
                    return;
                } else {
                    this.f230j = bundle2;
                    a9.b(str, list, bundle);
                }
                this.f230j = null;
            }
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public void disconnect() {
            Messenger messenger;
            j jVar = this.f227g;
            if (jVar != null && (messenger = this.f228h) != null) {
                try {
                    jVar.f(messenger);
                } catch (RemoteException unused) {
                    Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
                }
            }
            this.f222b.disconnect();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c.b
        public void e() {
            this.f227g = null;
            this.f228h = null;
            this.f229i = null;
            this.f224d.a(null);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void f(Messenger messenger) {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c.b
        public void g() {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c.b
        public void onConnected() {
            try {
                Bundle extras = this.f222b.getExtras();
                if (extras == null) {
                    return;
                }
                this.f226f = extras.getInt("extra_service_version", 0);
                IBinder a9 = androidx.core.app.g.a(extras, "extra_messenger");
                if (a9 != null) {
                    this.f227g = new j(a9, this.f223c);
                    Messenger messenger = new Messenger(this.f224d);
                    this.f228h = messenger;
                    this.f224d.a(messenger);
                    try {
                        this.f227g.d(this.f221a, this.f228h);
                    } catch (RemoteException unused) {
                        Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
                    }
                }
                android.support.v4.media.session.b d8 = b.a.d(androidx.core.app.g.a(extras, "extra_session_binder"));
                if (d8 != null) {
                    this.f229i = MediaSessionCompat.Token.c(this.f222b.getSessionToken(), d8);
                }
            } catch (IllegalStateException e8) {
                Log.e("MediaBrowserCompat", "Unexpected IllegalStateException", e8);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends e {
        f(Context context, ComponentName componentName, c cVar, Bundle bundle) {
            super(context, componentName, cVar, bundle);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g extends f {
        g(Context context, ComponentName componentName, c cVar, Bundle bundle) {
            super(context, componentName, cVar, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h implements d, i {

        /* renamed from: a  reason: collision with root package name */
        final Context f231a;

        /* renamed from: b  reason: collision with root package name */
        final ComponentName f232b;

        /* renamed from: c  reason: collision with root package name */
        final c f233c;

        /* renamed from: d  reason: collision with root package name */
        final Bundle f234d;

        /* renamed from: e  reason: collision with root package name */
        final b f235e = new b(this);

        /* renamed from: f  reason: collision with root package name */
        private final k0.a<String, k> f236f = new k0.a<>();

        /* renamed from: g  reason: collision with root package name */
        int f237g = 1;

        /* renamed from: h  reason: collision with root package name */
        c f238h;

        /* renamed from: i  reason: collision with root package name */
        j f239i;

        /* renamed from: j  reason: collision with root package name */
        Messenger f240j;

        /* renamed from: k  reason: collision with root package name */
        private String f241k;

        /* renamed from: l  reason: collision with root package name */
        private MediaSessionCompat.Token f242l;

        /* renamed from: m  reason: collision with root package name */
        private Bundle f243m;

        /* renamed from: n  reason: collision with root package name */
        private Bundle f244n;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                h hVar = h.this;
                if (hVar.f237g == 0) {
                    return;
                }
                hVar.f237g = 2;
                if (MediaBrowserCompat.f212b && hVar.f238h != null) {
                    throw new RuntimeException("mServiceConnection should be null. Instead it is " + h.this.f238h);
                } else if (hVar.f239i != null) {
                    throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + h.this.f239i);
                } else if (hVar.f240j != null) {
                    throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + h.this.f240j);
                } else {
                    Intent intent = new Intent("android.media.browse.MediaBrowserService");
                    intent.setComponent(h.this.f232b);
                    h hVar2 = h.this;
                    hVar2.f238h = new c();
                    boolean z4 = false;
                    try {
                        h hVar3 = h.this;
                        z4 = hVar3.f231a.bindService(intent, hVar3.f238h, 1);
                    } catch (Exception unused) {
                        Log.e("MediaBrowserCompat", "Failed binding to service " + h.this.f232b);
                    }
                    if (!z4) {
                        h.this.g();
                        h.this.f233c.b();
                    }
                    if (MediaBrowserCompat.f212b) {
                        Log.d("MediaBrowserCompat", "connect...");
                        h.this.e();
                    }
                }
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements Runnable {
            b() {
            }

            @Override // java.lang.Runnable
            public void run() {
                h hVar = h.this;
                Messenger messenger = hVar.f240j;
                if (messenger != null) {
                    try {
                        hVar.f239i.c(messenger);
                    } catch (RemoteException unused) {
                        Log.w("MediaBrowserCompat", "RemoteException during connect for " + h.this.f232b);
                    }
                }
                h hVar2 = h.this;
                int i8 = hVar2.f237g;
                hVar2.g();
                if (i8 != 0) {
                    h.this.f237g = i8;
                }
                if (MediaBrowserCompat.f212b) {
                    Log.d("MediaBrowserCompat", "disconnect...");
                    h.this.e();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class c implements ServiceConnection {

            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class a implements Runnable {

                /* renamed from: a  reason: collision with root package name */
                final /* synthetic */ ComponentName f248a;

                /* renamed from: b  reason: collision with root package name */
                final /* synthetic */ IBinder f249b;

                a(ComponentName componentName, IBinder iBinder) {
                    this.f248a = componentName;
                    this.f249b = iBinder;
                }

                @Override // java.lang.Runnable
                public void run() {
                    boolean z4 = MediaBrowserCompat.f212b;
                    if (z4) {
                        Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + this.f248a + " binder=" + this.f249b);
                        h.this.e();
                    }
                    if (c.this.a("onServiceConnected")) {
                        h hVar = h.this;
                        hVar.f239i = new j(this.f249b, hVar.f234d);
                        h.this.f240j = new Messenger(h.this.f235e);
                        h hVar2 = h.this;
                        hVar2.f235e.a(hVar2.f240j);
                        h.this.f237g = 2;
                        if (z4) {
                            try {
                                Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                h.this.e();
                            } catch (RemoteException unused) {
                                Log.w("MediaBrowserCompat", "RemoteException during connect for " + h.this.f232b);
                                if (MediaBrowserCompat.f212b) {
                                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                    h.this.e();
                                    return;
                                }
                                return;
                            }
                        }
                        h hVar3 = h.this;
                        hVar3.f239i.b(hVar3.f231a, hVar3.f240j);
                    }
                }
            }

            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class b implements Runnable {

                /* renamed from: a  reason: collision with root package name */
                final /* synthetic */ ComponentName f251a;

                b(ComponentName componentName) {
                    this.f251a = componentName;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (MediaBrowserCompat.f212b) {
                        Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + this.f251a + " this=" + this + " mServiceConnection=" + h.this.f238h);
                        h.this.e();
                    }
                    if (c.this.a("onServiceDisconnected")) {
                        h hVar = h.this;
                        hVar.f239i = null;
                        hVar.f240j = null;
                        hVar.f235e.a(null);
                        h hVar2 = h.this;
                        hVar2.f237g = 4;
                        hVar2.f233c.c();
                    }
                }
            }

            c() {
            }

            private void b(Runnable runnable) {
                if (Thread.currentThread() == h.this.f235e.getLooper().getThread()) {
                    runnable.run();
                } else {
                    h.this.f235e.post(runnable);
                }
            }

            boolean a(String str) {
                int i8;
                h hVar = h.this;
                if (hVar.f238h != this || (i8 = hVar.f237g) == 0 || i8 == 1) {
                    int i9 = hVar.f237g;
                    if (i9 == 0 || i9 == 1) {
                        return false;
                    }
                    Log.i("MediaBrowserCompat", str + " for " + h.this.f232b + " with mServiceConnection=" + h.this.f238h + " this=" + this);
                    return false;
                }
                return true;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                b(new a(componentName, iBinder));
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                b(new b(componentName));
            }
        }

        public h(Context context, ComponentName componentName, c cVar, Bundle bundle) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (componentName == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (cVar == null) {
                throw new IllegalArgumentException("connection callback must not be null");
            }
            this.f231a = context;
            this.f232b = componentName;
            this.f233c = cVar;
            this.f234d = bundle == null ? null : new Bundle(bundle);
        }

        private static String h(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    if (i8 != 2) {
                        if (i8 != 3) {
                            if (i8 != 4) {
                                return "UNKNOWN/" + i8;
                            }
                            return "CONNECT_STATE_SUSPENDED";
                        }
                        return "CONNECT_STATE_CONNECTED";
                    }
                    return "CONNECT_STATE_CONNECTING";
                }
                return "CONNECT_STATE_DISCONNECTED";
            }
            return "CONNECT_STATE_DISCONNECTING";
        }

        private boolean j(Messenger messenger, String str) {
            int i8;
            if (this.f240j != messenger || (i8 = this.f237g) == 0 || i8 == 1) {
                int i9 = this.f237g;
                if (i9 == 0 || i9 == 1) {
                    return false;
                }
                Log.i("MediaBrowserCompat", str + " for " + this.f232b + " with mCallbacksMessenger=" + this.f240j + " this=" + this);
                return false;
            }
            return true;
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public void a() {
            int i8 = this.f237g;
            if (i8 == 0 || i8 == 1) {
                this.f237g = 2;
                this.f235e.post(new a());
                return;
            }
            throw new IllegalStateException("connect() called while neigther disconnecting nor disconnected (state=" + h(this.f237g) + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public MediaSessionCompat.Token b() {
            if (i()) {
                return this.f242l;
            }
            throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.f237g + ")");
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void c(Messenger messenger, String str, MediaSessionCompat.Token token, Bundle bundle) {
            if (j(messenger, "onConnect")) {
                if (this.f237g != 2) {
                    Log.w("MediaBrowserCompat", "onConnect from service while mState=" + h(this.f237g) + "... ignoring");
                    return;
                }
                this.f241k = str;
                this.f242l = token;
                this.f243m = bundle;
                this.f237g = 3;
                if (MediaBrowserCompat.f212b) {
                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                    e();
                }
                this.f233c.a();
                try {
                    for (Map.Entry<String, k> entry : this.f236f.entrySet()) {
                        String key = entry.getKey();
                        k value = entry.getValue();
                        List<l> b9 = value.b();
                        List<Bundle> c9 = value.c();
                        for (int i8 = 0; i8 < b9.size(); i8++) {
                            this.f239i.a(key, b9.get(i8).f258b, c9.get(i8), this.f240j);
                        }
                    }
                } catch (RemoteException unused) {
                    Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
                }
            }
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void d(Messenger messenger, String str, List<MediaItem> list, Bundle bundle, Bundle bundle2) {
            if (j(messenger, "onLoadChildren")) {
                boolean z4 = MediaBrowserCompat.f212b;
                if (z4) {
                    Log.d("MediaBrowserCompat", "onLoadChildren for " + this.f232b + " id=" + str);
                }
                k kVar = this.f236f.get(str);
                if (kVar == null) {
                    if (z4) {
                        Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + str);
                        return;
                    }
                    return;
                }
                l a9 = kVar.a(bundle);
                if (a9 != null) {
                    if (bundle == null) {
                        if (list == null) {
                            a9.c(str);
                            return;
                        } else {
                            this.f244n = bundle2;
                            a9.a(str, list);
                        }
                    } else if (list == null) {
                        a9.d(str, bundle);
                        return;
                    } else {
                        this.f244n = bundle2;
                        a9.b(str, list, bundle);
                    }
                    this.f244n = null;
                }
            }
        }

        @Override // android.support.v4.media.MediaBrowserCompat.d
        public void disconnect() {
            this.f237g = 0;
            this.f235e.post(new b());
        }

        void e() {
            Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
            Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.f232b);
            Log.d("MediaBrowserCompat", "  mCallback=" + this.f233c);
            Log.d("MediaBrowserCompat", "  mRootHints=" + this.f234d);
            Log.d("MediaBrowserCompat", "  mState=" + h(this.f237g));
            Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.f238h);
            Log.d("MediaBrowserCompat", "  mServiceBinderWrapper=" + this.f239i);
            Log.d("MediaBrowserCompat", "  mCallbacksMessenger=" + this.f240j);
            Log.d("MediaBrowserCompat", "  mRootId=" + this.f241k);
            Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.f242l);
        }

        @Override // android.support.v4.media.MediaBrowserCompat.i
        public void f(Messenger messenger) {
            Log.e("MediaBrowserCompat", "onConnectFailed for " + this.f232b);
            if (j(messenger, "onConnectFailed")) {
                if (this.f237g == 2) {
                    g();
                    this.f233c.b();
                    return;
                }
                Log.w("MediaBrowserCompat", "onConnect from service while mState=" + h(this.f237g) + "... ignoring");
            }
        }

        void g() {
            c cVar = this.f238h;
            if (cVar != null) {
                this.f231a.unbindService(cVar);
            }
            this.f237g = 1;
            this.f238h = null;
            this.f239i = null;
            this.f240j = null;
            this.f235e.a(null);
            this.f241k = null;
            this.f242l = null;
        }

        public boolean i() {
            return this.f237g == 3;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface i {
        void c(Messenger messenger, String str, MediaSessionCompat.Token token, Bundle bundle);

        void d(Messenger messenger, String str, List<MediaItem> list, Bundle bundle, Bundle bundle2);

        void f(Messenger messenger);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j {

        /* renamed from: a  reason: collision with root package name */
        private Messenger f253a;

        /* renamed from: b  reason: collision with root package name */
        private Bundle f254b;

        public j(IBinder iBinder, Bundle bundle) {
            this.f253a = new Messenger(iBinder);
            this.f254b = bundle;
        }

        private void e(int i8, Bundle bundle, Messenger messenger) {
            Message obtain = Message.obtain();
            obtain.what = i8;
            obtain.arg1 = 1;
            obtain.setData(bundle);
            obtain.replyTo = messenger;
            this.f253a.send(obtain);
        }

        void a(String str, IBinder iBinder, Bundle bundle, Messenger messenger) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", str);
            androidx.core.app.g.b(bundle2, "data_callback_token", iBinder);
            bundle2.putBundle("data_options", bundle);
            e(3, bundle2, messenger);
        }

        void b(Context context, Messenger messenger) {
            Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putInt("data_calling_pid", Process.myPid());
            bundle.putBundle("data_root_hints", this.f254b);
            e(1, bundle, messenger);
        }

        void c(Messenger messenger) {
            e(2, null, messenger);
        }

        void d(Context context, Messenger messenger) {
            Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putInt("data_calling_pid", Process.myPid());
            bundle.putBundle("data_root_hints", this.f254b);
            e(6, bundle, messenger);
        }

        void f(Messenger messenger) {
            e(7, null, messenger);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class k {

        /* renamed from: a  reason: collision with root package name */
        private final List<l> f255a = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        private final List<Bundle> f256b = new ArrayList();

        public l a(Bundle bundle) {
            for (int i8 = 0; i8 < this.f256b.size(); i8++) {
                if (androidx.media.c.a(this.f256b.get(i8), bundle)) {
                    return this.f255a.get(i8);
                }
            }
            return null;
        }

        public List<l> b() {
            return this.f255a;
        }

        public List<Bundle> c() {
            return this.f256b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class l {

        /* renamed from: a  reason: collision with root package name */
        final MediaBrowser.SubscriptionCallback f257a;

        /* renamed from: b  reason: collision with root package name */
        final IBinder f258b = new Binder();

        /* renamed from: c  reason: collision with root package name */
        WeakReference<k> f259c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private class a extends MediaBrowser.SubscriptionCallback {
            a() {
            }

            List<MediaItem> a(List<MediaItem> list, Bundle bundle) {
                if (list == null) {
                    return null;
                }
                int i8 = bundle.getInt("android.media.browse.extra.PAGE", -1);
                int i9 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
                if (i8 == -1 && i9 == -1) {
                    return list;
                }
                int i10 = i9 * i8;
                int i11 = i10 + i9;
                if (i8 < 0 || i9 < 1 || i10 >= list.size()) {
                    return Collections.emptyList();
                }
                if (i11 > list.size()) {
                    i11 = list.size();
                }
                return list.subList(i10, i11);
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onChildrenLoaded(String str, List<MediaBrowser.MediaItem> list) {
                WeakReference<k> weakReference = l.this.f259c;
                k kVar = weakReference == null ? null : weakReference.get();
                if (kVar == null) {
                    l.this.a(str, MediaItem.b(list));
                    return;
                }
                List<MediaItem> b9 = MediaItem.b(list);
                List<l> b10 = kVar.b();
                List<Bundle> c9 = kVar.c();
                for (int i8 = 0; i8 < b10.size(); i8++) {
                    Bundle bundle = c9.get(i8);
                    if (bundle == null) {
                        l.this.a(str, b9);
                    } else {
                        l.this.b(str, a(b9, bundle), bundle);
                    }
                }
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onError(String str) {
                l.this.c(str);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private class b extends a {
            b() {
                super();
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onChildrenLoaded(String str, List<MediaBrowser.MediaItem> list, Bundle bundle) {
                MediaSessionCompat.a(bundle);
                l.this.b(str, MediaItem.b(list), bundle);
            }

            @Override // android.media.browse.MediaBrowser.SubscriptionCallback
            public void onError(String str, Bundle bundle) {
                MediaSessionCompat.a(bundle);
                l.this.d(str, bundle);
            }
        }

        public l() {
            int i8 = Build.VERSION.SDK_INT;
            this.f257a = i8 >= 26 ? new b() : i8 >= 21 ? new a() : null;
        }

        public void a(String str, List<MediaItem> list) {
        }

        public void b(String str, List<MediaItem> list, Bundle bundle) {
        }

        public void c(String str) {
        }

        public void d(String str, Bundle bundle) {
        }
    }

    public MediaBrowserCompat(Context context, ComponentName componentName, c cVar, Bundle bundle) {
        int i8 = Build.VERSION.SDK_INT;
        this.f213a = i8 >= 26 ? new g(context, componentName, cVar, bundle) : i8 >= 23 ? new f(context, componentName, cVar, bundle) : i8 >= 21 ? new e(context, componentName, cVar, bundle) : new h(context, componentName, cVar, bundle);
    }

    public void a() {
        Log.d("MediaBrowserCompat", "Connecting to a MediaBrowserService.");
        this.f213a.a();
    }

    public void b() {
        this.f213a.disconnect();
    }

    public MediaSessionCompat.Token c() {
        return this.f213a.b();
    }
}
