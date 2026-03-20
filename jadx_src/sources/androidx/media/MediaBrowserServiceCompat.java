package androidx.media;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import androidx.media.d;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class MediaBrowserServiceCompat extends Service {

    /* renamed from: h  reason: collision with root package name */
    static final boolean f5968h = Log.isLoggable("MBServiceCompat", 3);

    /* renamed from: a  reason: collision with root package name */
    private g f5969a;

    /* renamed from: e  reason: collision with root package name */
    f f5973e;

    /* renamed from: g  reason: collision with root package name */
    MediaSessionCompat.Token f5975g;

    /* renamed from: b  reason: collision with root package name */
    final f f5970b = new f("android.media.session.MediaController", -1, -1, null, null);

    /* renamed from: c  reason: collision with root package name */
    final ArrayList<f> f5971c = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    final k0.a<IBinder, f> f5972d = new k0.a<>();

    /* renamed from: f  reason: collision with root package name */
    final r f5974f = new r();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends m<List<MediaBrowserCompat.MediaItem>> {

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ f f5976f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ String f5977g;

        /* renamed from: h  reason: collision with root package name */
        final /* synthetic */ Bundle f5978h;

        /* renamed from: i  reason: collision with root package name */
        final /* synthetic */ Bundle f5979i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Object obj, f fVar, String str, Bundle bundle, Bundle bundle2) {
            super(obj);
            this.f5976f = fVar;
            this.f5977g = str;
            this.f5978h = bundle;
            this.f5979i = bundle2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.media.MediaBrowserServiceCompat.m
        /* renamed from: h */
        public void d(List<MediaBrowserCompat.MediaItem> list) {
            if (MediaBrowserServiceCompat.this.f5972d.get(this.f5976f.f5994f.asBinder()) != this.f5976f) {
                if (MediaBrowserServiceCompat.f5968h) {
                    Log.d("MBServiceCompat", "Not sending onLoadChildren result for connection that has been disconnected. pkg=" + this.f5976f.f5989a + " id=" + this.f5977g);
                    return;
                }
                return;
            }
            if ((a() & 1) != 0) {
                list = MediaBrowserServiceCompat.this.b(list, this.f5978h);
            }
            try {
                this.f5976f.f5994f.a(this.f5977g, list, this.f5978h, this.f5979i);
            } catch (RemoteException unused) {
                Log.w("MBServiceCompat", "Calling onLoadChildren() failed for id=" + this.f5977g + " package=" + this.f5976f.f5989a);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends m<MediaBrowserCompat.MediaItem> {

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ ResultReceiver f5981f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(Object obj, ResultReceiver resultReceiver) {
            super(obj);
            this.f5981f = resultReceiver;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.media.MediaBrowserServiceCompat.m
        /* renamed from: h */
        public void d(MediaBrowserCompat.MediaItem mediaItem) {
            if ((a() & 2) != 0) {
                this.f5981f.b(-1, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("media_item", mediaItem);
            this.f5981f.b(0, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends m<List<MediaBrowserCompat.MediaItem>> {

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ ResultReceiver f5983f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Object obj, ResultReceiver resultReceiver) {
            super(obj);
            this.f5983f = resultReceiver;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.media.MediaBrowserServiceCompat.m
        /* renamed from: h */
        public void d(List<MediaBrowserCompat.MediaItem> list) {
            if ((a() & 4) != 0 || list == null) {
                this.f5983f.b(-1, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArray("search_results", (Parcelable[]) list.toArray(new MediaBrowserCompat.MediaItem[0]));
            this.f5983f.b(0, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends m<Bundle> {

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ ResultReceiver f5985f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(Object obj, ResultReceiver resultReceiver) {
            super(obj);
            this.f5985f = resultReceiver;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.m
        void c(Bundle bundle) {
            this.f5985f.b(-1, bundle);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.media.MediaBrowserServiceCompat.m
        /* renamed from: h */
        public void d(Bundle bundle) {
            this.f5985f.b(0, bundle);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        private final String f5987a;

        /* renamed from: b  reason: collision with root package name */
        private final Bundle f5988b;

        public e(String str, Bundle bundle) {
            if (str == null) {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead");
            }
            this.f5987a = str;
            this.f5988b = bundle;
        }

        public Bundle c() {
            return this.f5988b;
        }

        public String d() {
            return this.f5987a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements IBinder.DeathRecipient {

        /* renamed from: a  reason: collision with root package name */
        public final String f5989a;

        /* renamed from: b  reason: collision with root package name */
        public final int f5990b;

        /* renamed from: c  reason: collision with root package name */
        public final int f5991c;

        /* renamed from: d  reason: collision with root package name */
        public final d.b f5992d;

        /* renamed from: e  reason: collision with root package name */
        public final Bundle f5993e;

        /* renamed from: f  reason: collision with root package name */
        public final p f5994f;

        /* renamed from: g  reason: collision with root package name */
        public final HashMap<String, List<androidx.core.util.d<IBinder, Bundle>>> f5995g = new HashMap<>();

        /* renamed from: h  reason: collision with root package name */
        public e f5996h;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = f.this;
                MediaBrowserServiceCompat.this.f5972d.remove(fVar.f5994f.asBinder());
            }
        }

        f(String str, int i8, int i9, Bundle bundle, p pVar) {
            this.f5989a = str;
            this.f5990b = i8;
            this.f5991c = i9;
            this.f5992d = new d.b(str, i8, i9);
            this.f5993e = bundle;
            this.f5994f = pVar;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            MediaBrowserServiceCompat.this.f5974f.post(new a());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface g {
        IBinder a(Intent intent);

        void onCreate();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h implements g {

        /* renamed from: a  reason: collision with root package name */
        final List<Bundle> f5999a = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        MediaBrowserService f6000b;

        /* renamed from: c  reason: collision with root package name */
        Messenger f6001c;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends m<List<MediaBrowserCompat.MediaItem>> {

            /* renamed from: f  reason: collision with root package name */
            final /* synthetic */ n f6003f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Object obj, n nVar) {
                super(obj);
                this.f6003f = nVar;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // androidx.media.MediaBrowserServiceCompat.m
            /* renamed from: h */
            public void d(List<MediaBrowserCompat.MediaItem> list) {
                ArrayList arrayList;
                if (list != null) {
                    arrayList = new ArrayList();
                    for (MediaBrowserCompat.MediaItem mediaItem : list) {
                        Parcel obtain = Parcel.obtain();
                        mediaItem.writeToParcel(obtain, 0);
                        arrayList.add(obtain);
                    }
                } else {
                    arrayList = null;
                }
                this.f6003f.b(arrayList);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b extends MediaBrowserService {
            b(Context context) {
                attachBaseContext(context);
            }

            @Override // android.service.media.MediaBrowserService
            @SuppressLint({"SyntheticAccessor"})
            public MediaBrowserService.BrowserRoot onGetRoot(String str, int i8, Bundle bundle) {
                MediaSessionCompat.a(bundle);
                e b9 = h.this.b(str, i8, bundle == null ? null : new Bundle(bundle));
                if (b9 == null) {
                    return null;
                }
                return new MediaBrowserService.BrowserRoot(b9.f5987a, b9.f5988b);
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadChildren(String str, MediaBrowserService.Result<List<MediaBrowser.MediaItem>> result) {
                h.this.c(str, new n<>(result));
            }
        }

        h() {
        }

        @Override // androidx.media.MediaBrowserServiceCompat.g
        public IBinder a(Intent intent) {
            return this.f6000b.onBind(intent);
        }

        public e b(String str, int i8, Bundle bundle) {
            int i9;
            Bundle bundle2;
            if (bundle == null || bundle.getInt("extra_client_version", 0) == 0) {
                i9 = -1;
                bundle2 = null;
            } else {
                bundle.remove("extra_client_version");
                this.f6001c = new Messenger(MediaBrowserServiceCompat.this.f5974f);
                bundle2 = new Bundle();
                bundle2.putInt("extra_service_version", 2);
                androidx.core.app.g.b(bundle2, "extra_messenger", this.f6001c.getBinder());
                MediaSessionCompat.Token token = MediaBrowserServiceCompat.this.f5975g;
                if (token != null) {
                    android.support.v4.media.session.b d8 = token.d();
                    androidx.core.app.g.b(bundle2, "extra_session_binder", d8 == null ? null : d8.asBinder());
                } else {
                    this.f5999a.add(bundle2);
                }
                int i10 = bundle.getInt("extra_calling_pid", -1);
                bundle.remove("extra_calling_pid");
                i9 = i10;
            }
            f fVar = new f(str, i9, i8, bundle, null);
            MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
            mediaBrowserServiceCompat.f5973e = fVar;
            e e8 = mediaBrowserServiceCompat.e(str, i8, bundle);
            MediaBrowserServiceCompat mediaBrowserServiceCompat2 = MediaBrowserServiceCompat.this;
            mediaBrowserServiceCompat2.f5973e = null;
            if (e8 == null) {
                return null;
            }
            if (this.f6001c != null) {
                mediaBrowserServiceCompat2.f5971c.add(fVar);
            }
            if (bundle2 == null) {
                bundle2 = e8.c();
            } else if (e8.c() != null) {
                bundle2.putAll(e8.c());
            }
            return new e(e8.d(), bundle2);
        }

        public void c(String str, n<List<Parcel>> nVar) {
            a aVar = new a(str, nVar);
            MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
            mediaBrowserServiceCompat.f5973e = mediaBrowserServiceCompat.f5970b;
            mediaBrowserServiceCompat.f(str, aVar);
            MediaBrowserServiceCompat.this.f5973e = null;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.g
        public void onCreate() {
            b bVar = new b(MediaBrowserServiceCompat.this);
            this.f6000b = bVar;
            bVar.onCreate();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class i extends h {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends m<MediaBrowserCompat.MediaItem> {

            /* renamed from: f  reason: collision with root package name */
            final /* synthetic */ n f6007f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Object obj, n nVar) {
                super(obj);
                this.f6007f = nVar;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // androidx.media.MediaBrowserServiceCompat.m
            /* renamed from: h */
            public void d(MediaBrowserCompat.MediaItem mediaItem) {
                Parcel obtain;
                n nVar;
                if (mediaItem == null) {
                    nVar = this.f6007f;
                    obtain = null;
                } else {
                    obtain = Parcel.obtain();
                    mediaItem.writeToParcel(obtain, 0);
                    nVar = this.f6007f;
                }
                nVar.b(obtain);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b extends h.b {
            b(Context context) {
                super(context);
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadItem(String str, MediaBrowserService.Result<MediaBrowser.MediaItem> result) {
                i.this.d(str, new n<>(result));
            }
        }

        i() {
            super();
        }

        public void d(String str, n<Parcel> nVar) {
            a aVar = new a(str, nVar);
            MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
            mediaBrowserServiceCompat.f5973e = mediaBrowserServiceCompat.f5970b;
            mediaBrowserServiceCompat.i(str, aVar);
            MediaBrowserServiceCompat.this.f5973e = null;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.h, androidx.media.MediaBrowserServiceCompat.g
        public void onCreate() {
            b bVar = new b(MediaBrowserServiceCompat.this);
            this.f6000b = bVar;
            bVar.onCreate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j extends i {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends m<List<MediaBrowserCompat.MediaItem>> {

            /* renamed from: f  reason: collision with root package name */
            final /* synthetic */ n f6011f;

            /* renamed from: g  reason: collision with root package name */
            final /* synthetic */ Bundle f6012g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Object obj, n nVar, Bundle bundle) {
                super(obj);
                this.f6011f = nVar;
                this.f6012g = bundle;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // androidx.media.MediaBrowserServiceCompat.m
            /* renamed from: h */
            public void d(List<MediaBrowserCompat.MediaItem> list) {
                ArrayList arrayList;
                n nVar;
                if (list == null) {
                    nVar = this.f6011f;
                    arrayList = null;
                } else {
                    if ((a() & 1) != 0) {
                        list = MediaBrowserServiceCompat.this.b(list, this.f6012g);
                    }
                    arrayList = new ArrayList();
                    for (MediaBrowserCompat.MediaItem mediaItem : list) {
                        Parcel obtain = Parcel.obtain();
                        mediaItem.writeToParcel(obtain, 0);
                        arrayList.add(obtain);
                    }
                    nVar = this.f6011f;
                }
                nVar.b(arrayList);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b extends i.b {
            b(Context context) {
                super(context);
            }

            @Override // android.service.media.MediaBrowserService
            public void onLoadChildren(String str, MediaBrowserService.Result<List<MediaBrowser.MediaItem>> result, Bundle bundle) {
                MediaSessionCompat.a(bundle);
                j jVar = j.this;
                MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
                mediaBrowserServiceCompat.f5973e = mediaBrowserServiceCompat.f5970b;
                jVar.e(str, new n<>(result), bundle);
                MediaBrowserServiceCompat.this.f5973e = null;
            }
        }

        j() {
            super();
        }

        public void e(String str, n<List<Parcel>> nVar, Bundle bundle) {
            a aVar = new a(str, nVar, bundle);
            MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
            mediaBrowserServiceCompat.f5973e = mediaBrowserServiceCompat.f5970b;
            mediaBrowserServiceCompat.g(str, aVar, bundle);
            MediaBrowserServiceCompat.this.f5973e = null;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.i, androidx.media.MediaBrowserServiceCompat.h, androidx.media.MediaBrowserServiceCompat.g
        public void onCreate() {
            b bVar = new b(MediaBrowserServiceCompat.this);
            this.f6000b = bVar;
            bVar.onCreate();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class k extends j {
        k() {
            super();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class l implements g {

        /* renamed from: a  reason: collision with root package name */
        private Messenger f6016a;

        l() {
        }

        @Override // androidx.media.MediaBrowserServiceCompat.g
        public IBinder a(Intent intent) {
            if ("android.media.browse.MediaBrowserService".equals(intent.getAction())) {
                return this.f6016a.getBinder();
            }
            return null;
        }

        @Override // androidx.media.MediaBrowserServiceCompat.g
        public void onCreate() {
            this.f6016a = new Messenger(MediaBrowserServiceCompat.this.f5974f);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class m<T> {

        /* renamed from: a  reason: collision with root package name */
        private final Object f6018a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f6019b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f6020c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f6021d;

        /* renamed from: e  reason: collision with root package name */
        private int f6022e;

        m(Object obj) {
            this.f6018a = obj;
        }

        int a() {
            return this.f6022e;
        }

        boolean b() {
            return this.f6019b || this.f6020c || this.f6021d;
        }

        void c(Bundle bundle) {
            throw new UnsupportedOperationException("It is not supported to send an error for " + this.f6018a);
        }

        void d(T t8) {
            throw null;
        }

        public void e(Bundle bundle) {
            if (!this.f6020c && !this.f6021d) {
                this.f6021d = true;
                c(bundle);
                return;
            }
            throw new IllegalStateException("sendError() called when either sendResult() or sendError() had already been called for: " + this.f6018a);
        }

        public void f(T t8) {
            if (!this.f6020c && !this.f6021d) {
                this.f6020c = true;
                d(t8);
                return;
            }
            throw new IllegalStateException("sendResult() called when either sendResult() or sendError() had already been called for: " + this.f6018a);
        }

        void g(int i8) {
            this.f6022e = i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class n<T> {

        /* renamed from: a  reason: collision with root package name */
        MediaBrowserService.Result f6023a;

        n(MediaBrowserService.Result result) {
            this.f6023a = result;
        }

        List<MediaBrowser.MediaItem> a(List<Parcel> list) {
            if (list == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Parcel parcel : list) {
                parcel.setDataPosition(0);
                arrayList.add((MediaBrowser.MediaItem) MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
            return arrayList;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void b(T t8) {
            if (t8 instanceof List) {
                this.f6023a.sendResult(a((List) t8));
            } else if (!(t8 instanceof Parcel)) {
                this.f6023a.sendResult(null);
            } else {
                Parcel parcel = (Parcel) t8;
                parcel.setDataPosition(0);
                this.f6023a.sendResult(MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class o {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6025a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6026b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ int f6027c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ int f6028d;

            /* renamed from: e  reason: collision with root package name */
            final /* synthetic */ Bundle f6029e;

            a(p pVar, String str, int i8, int i9, Bundle bundle) {
                this.f6025a = pVar;
                this.f6026b = str;
                this.f6027c = i8;
                this.f6028d = i9;
                this.f6029e = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                IBinder asBinder = this.f6025a.asBinder();
                MediaBrowserServiceCompat.this.f5972d.remove(asBinder);
                f fVar = new f(this.f6026b, this.f6027c, this.f6028d, this.f6029e, this.f6025a);
                MediaBrowserServiceCompat mediaBrowserServiceCompat = MediaBrowserServiceCompat.this;
                mediaBrowserServiceCompat.f5973e = fVar;
                e e8 = mediaBrowserServiceCompat.e(this.f6026b, this.f6028d, this.f6029e);
                fVar.f5996h = e8;
                MediaBrowserServiceCompat mediaBrowserServiceCompat2 = MediaBrowserServiceCompat.this;
                mediaBrowserServiceCompat2.f5973e = null;
                if (e8 != null) {
                    try {
                        mediaBrowserServiceCompat2.f5972d.put(asBinder, fVar);
                        asBinder.linkToDeath(fVar, 0);
                        if (MediaBrowserServiceCompat.this.f5975g != null) {
                            this.f6025a.c(fVar.f5996h.d(), MediaBrowserServiceCompat.this.f5975g, fVar.f5996h.c());
                            return;
                        }
                        return;
                    } catch (RemoteException unused) {
                        Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + this.f6026b);
                        MediaBrowserServiceCompat.this.f5972d.remove(asBinder);
                        return;
                    }
                }
                Log.i("MBServiceCompat", "No root for client " + this.f6026b + " from service " + getClass().getName());
                try {
                    this.f6025a.b();
                } catch (RemoteException unused2) {
                    Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + this.f6026b);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6031a;

            b(p pVar) {
                this.f6031a = pVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                f remove = MediaBrowserServiceCompat.this.f5972d.remove(this.f6031a.asBinder());
                if (remove != null) {
                    remove.f5994f.asBinder().unlinkToDeath(remove, 0);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class c implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6033a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6034b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ IBinder f6035c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ Bundle f6036d;

            c(p pVar, String str, IBinder iBinder, Bundle bundle) {
                this.f6033a = pVar;
                this.f6034b = str;
                this.f6035c = iBinder;
                this.f6036d = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = MediaBrowserServiceCompat.this.f5972d.get(this.f6033a.asBinder());
                if (fVar != null) {
                    MediaBrowserServiceCompat.this.a(this.f6034b, fVar, this.f6035c, this.f6036d);
                    return;
                }
                Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + this.f6034b);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class d implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6038a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6039b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ IBinder f6040c;

            d(p pVar, String str, IBinder iBinder) {
                this.f6038a = pVar;
                this.f6039b = str;
                this.f6040c = iBinder;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = MediaBrowserServiceCompat.this.f5972d.get(this.f6038a.asBinder());
                if (fVar == null) {
                    Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + this.f6039b);
                } else if (MediaBrowserServiceCompat.this.q(this.f6039b, fVar, this.f6040c)) {
                } else {
                    Log.w("MBServiceCompat", "removeSubscription called for " + this.f6039b + " which is not subscribed");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class e implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6042a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6043b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ ResultReceiver f6044c;

            e(p pVar, String str, ResultReceiver resultReceiver) {
                this.f6042a = pVar;
                this.f6043b = str;
                this.f6044c = resultReceiver;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = MediaBrowserServiceCompat.this.f5972d.get(this.f6042a.asBinder());
                if (fVar != null) {
                    MediaBrowserServiceCompat.this.o(this.f6043b, fVar, this.f6044c);
                    return;
                }
                Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + this.f6043b);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class f implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6046a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ int f6047b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ String f6048c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ int f6049d;

            /* renamed from: e  reason: collision with root package name */
            final /* synthetic */ Bundle f6050e;

            f(p pVar, int i8, String str, int i9, Bundle bundle) {
                this.f6046a = pVar;
                this.f6047b = i8;
                this.f6048c = str;
                this.f6049d = i9;
                this.f6050e = bundle;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar;
                IBinder asBinder = this.f6046a.asBinder();
                MediaBrowserServiceCompat.this.f5972d.remove(asBinder);
                Iterator<f> it = MediaBrowserServiceCompat.this.f5971c.iterator();
                while (true) {
                    fVar = null;
                    if (!it.hasNext()) {
                        break;
                    }
                    f next = it.next();
                    if (next.f5991c == this.f6047b) {
                        if (TextUtils.isEmpty(this.f6048c) || this.f6049d <= 0) {
                            fVar = new f(next.f5989a, next.f5990b, next.f5991c, this.f6050e, this.f6046a);
                        }
                        it.remove();
                    }
                }
                if (fVar == null) {
                    fVar = new f(this.f6048c, this.f6049d, this.f6047b, this.f6050e, this.f6046a);
                }
                MediaBrowserServiceCompat.this.f5972d.put(asBinder, fVar);
                try {
                    asBinder.linkToDeath(fVar, 0);
                } catch (RemoteException unused) {
                    Log.w("MBServiceCompat", "IBinder is already dead.");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class g implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6052a;

            g(p pVar) {
                this.f6052a = pVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                IBinder asBinder = this.f6052a.asBinder();
                f remove = MediaBrowserServiceCompat.this.f5972d.remove(asBinder);
                if (remove != null) {
                    asBinder.unlinkToDeath(remove, 0);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class h implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6054a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6055b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ Bundle f6056c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ ResultReceiver f6057d;

            h(p pVar, String str, Bundle bundle, ResultReceiver resultReceiver) {
                this.f6054a = pVar;
                this.f6055b = str;
                this.f6056c = bundle;
                this.f6057d = resultReceiver;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = MediaBrowserServiceCompat.this.f5972d.get(this.f6054a.asBinder());
                if (fVar != null) {
                    MediaBrowserServiceCompat.this.p(this.f6055b, this.f6056c, fVar, this.f6057d);
                    return;
                }
                Log.w("MBServiceCompat", "search for callback that isn't registered query=" + this.f6055b);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class i implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ p f6059a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ String f6060b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ Bundle f6061c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ ResultReceiver f6062d;

            i(p pVar, String str, Bundle bundle, ResultReceiver resultReceiver) {
                this.f6059a = pVar;
                this.f6060b = str;
                this.f6061c = bundle;
                this.f6062d = resultReceiver;
            }

            @Override // java.lang.Runnable
            public void run() {
                f fVar = MediaBrowserServiceCompat.this.f5972d.get(this.f6059a.asBinder());
                if (fVar != null) {
                    MediaBrowserServiceCompat.this.m(this.f6060b, this.f6061c, fVar, this.f6062d);
                    return;
                }
                Log.w("MBServiceCompat", "sendCustomAction for callback that isn't registered action=" + this.f6060b + ", extras=" + this.f6061c);
            }
        }

        o() {
        }

        public void a(String str, IBinder iBinder, Bundle bundle, p pVar) {
            MediaBrowserServiceCompat.this.f5974f.a(new c(pVar, str, iBinder, bundle));
        }

        public void b(String str, int i8, int i9, Bundle bundle, p pVar) {
            if (MediaBrowserServiceCompat.this.c(str, i9)) {
                MediaBrowserServiceCompat.this.f5974f.a(new a(pVar, str, i8, i9, bundle));
                return;
            }
            throw new IllegalArgumentException("Package/uid mismatch: uid=" + i9 + " package=" + str);
        }

        public void c(p pVar) {
            MediaBrowserServiceCompat.this.f5974f.a(new b(pVar));
        }

        public void d(String str, ResultReceiver resultReceiver, p pVar) {
            if (TextUtils.isEmpty(str) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.f5974f.a(new e(pVar, str, resultReceiver));
        }

        public void e(p pVar, String str, int i8, int i9, Bundle bundle) {
            MediaBrowserServiceCompat.this.f5974f.a(new f(pVar, i9, str, i8, bundle));
        }

        public void f(String str, IBinder iBinder, p pVar) {
            MediaBrowserServiceCompat.this.f5974f.a(new d(pVar, str, iBinder));
        }

        public void g(String str, Bundle bundle, ResultReceiver resultReceiver, p pVar) {
            if (TextUtils.isEmpty(str) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.f5974f.a(new h(pVar, str, bundle, resultReceiver));
        }

        public void h(String str, Bundle bundle, ResultReceiver resultReceiver, p pVar) {
            if (TextUtils.isEmpty(str) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.f5974f.a(new i(pVar, str, bundle, resultReceiver));
        }

        public void i(p pVar) {
            MediaBrowserServiceCompat.this.f5974f.a(new g(pVar));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface p {
        void a(String str, List<MediaBrowserCompat.MediaItem> list, Bundle bundle, Bundle bundle2);

        IBinder asBinder();

        void b();

        void c(String str, MediaSessionCompat.Token token, Bundle bundle);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class q implements p {

        /* renamed from: a  reason: collision with root package name */
        final Messenger f6064a;

        q(Messenger messenger) {
            this.f6064a = messenger;
        }

        private void d(int i8, Bundle bundle) {
            Message obtain = Message.obtain();
            obtain.what = i8;
            obtain.arg1 = 2;
            obtain.setData(bundle);
            this.f6064a.send(obtain);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.p
        public void a(String str, List<MediaBrowserCompat.MediaItem> list, Bundle bundle, Bundle bundle2) {
            Bundle bundle3 = new Bundle();
            bundle3.putString("data_media_item_id", str);
            bundle3.putBundle("data_options", bundle);
            bundle3.putBundle("data_notify_children_changed_options", bundle2);
            if (list != null) {
                bundle3.putParcelableArrayList("data_media_item_list", list instanceof ArrayList ? (ArrayList) list : new ArrayList<>(list));
            }
            d(3, bundle3);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.p
        public IBinder asBinder() {
            return this.f6064a.getBinder();
        }

        @Override // androidx.media.MediaBrowserServiceCompat.p
        public void b() {
            d(2, null);
        }

        @Override // androidx.media.MediaBrowserServiceCompat.p
        public void c(String str, MediaSessionCompat.Token token, Bundle bundle) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putInt("extra_service_version", 2);
            Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", str);
            bundle2.putParcelable("data_media_session_token", token);
            bundle2.putBundle("data_root_hints", bundle);
            d(1, bundle2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class r extends Handler {

        /* renamed from: a  reason: collision with root package name */
        private final o f6065a;

        r() {
            this.f6065a = new o();
        }

        public void a(Runnable runnable) {
            if (Thread.currentThread() == getLooper().getThread()) {
                runnable.run();
            } else {
                post(runnable);
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            switch (message.what) {
                case 1:
                    Bundle bundle = data.getBundle("data_root_hints");
                    MediaSessionCompat.a(bundle);
                    this.f6065a.b(data.getString("data_package_name"), data.getInt("data_calling_pid"), data.getInt("data_calling_uid"), bundle, new q(message.replyTo));
                    return;
                case 2:
                    this.f6065a.c(new q(message.replyTo));
                    return;
                case 3:
                    Bundle bundle2 = data.getBundle("data_options");
                    MediaSessionCompat.a(bundle2);
                    this.f6065a.a(data.getString("data_media_item_id"), androidx.core.app.g.a(data, "data_callback_token"), bundle2, new q(message.replyTo));
                    return;
                case 4:
                    this.f6065a.f(data.getString("data_media_item_id"), androidx.core.app.g.a(data, "data_callback_token"), new q(message.replyTo));
                    return;
                case 5:
                    this.f6065a.d(data.getString("data_media_item_id"), (ResultReceiver) data.getParcelable("data_result_receiver"), new q(message.replyTo));
                    return;
                case 6:
                    Bundle bundle3 = data.getBundle("data_root_hints");
                    MediaSessionCompat.a(bundle3);
                    this.f6065a.e(new q(message.replyTo), data.getString("data_package_name"), data.getInt("data_calling_pid"), data.getInt("data_calling_uid"), bundle3);
                    return;
                case 7:
                    this.f6065a.i(new q(message.replyTo));
                    return;
                case 8:
                    Bundle bundle4 = data.getBundle("data_search_extras");
                    MediaSessionCompat.a(bundle4);
                    this.f6065a.g(data.getString("data_search_query"), bundle4, (ResultReceiver) data.getParcelable("data_result_receiver"), new q(message.replyTo));
                    return;
                case 9:
                    Bundle bundle5 = data.getBundle("data_custom_action_extras");
                    MediaSessionCompat.a(bundle5);
                    this.f6065a.h(data.getString("data_custom_action"), bundle5, (ResultReceiver) data.getParcelable("data_result_receiver"), new q(message.replyTo));
                    return;
                default:
                    Log.w("MBServiceCompat", "Unhandled message: " + message + "\n  Service version: 2\n  Client version: " + message.arg1);
                    return;
            }
        }

        @Override // android.os.Handler
        public boolean sendMessageAtTime(Message message, long j8) {
            Bundle data = message.getData();
            data.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            data.putInt("data_calling_uid", Binder.getCallingUid());
            int callingPid = Binder.getCallingPid();
            if (callingPid <= 0) {
                if (!data.containsKey("data_calling_pid")) {
                    callingPid = -1;
                }
                return super.sendMessageAtTime(message, j8);
            }
            data.putInt("data_calling_pid", callingPid);
            return super.sendMessageAtTime(message, j8);
        }
    }

    void a(String str, f fVar, IBinder iBinder, Bundle bundle) {
        List<androidx.core.util.d<IBinder, Bundle>> list = fVar.f5995g.get(str);
        if (list == null) {
            list = new ArrayList<>();
        }
        for (androidx.core.util.d<IBinder, Bundle> dVar : list) {
            if (iBinder == dVar.f4889a && androidx.media.c.a(bundle, dVar.f4890b)) {
                return;
            }
        }
        list.add(new androidx.core.util.d<>(iBinder, bundle));
        fVar.f5995g.put(str, list);
        n(str, fVar, bundle, null);
        this.f5973e = fVar;
        k(str, bundle);
        this.f5973e = null;
    }

    List<MediaBrowserCompat.MediaItem> b(List<MediaBrowserCompat.MediaItem> list, Bundle bundle) {
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

    boolean c(String str, int i8) {
        if (str == null) {
            return false;
        }
        for (String str2 : getPackageManager().getPackagesForUid(i8)) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void d(String str, Bundle bundle, m<Bundle> mVar) {
        mVar.e(null);
    }

    @Override // android.app.Service
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public abstract e e(String str, int i8, Bundle bundle);

    public abstract void f(String str, m<List<MediaBrowserCompat.MediaItem>> mVar);

    public void g(String str, m<List<MediaBrowserCompat.MediaItem>> mVar, Bundle bundle) {
        mVar.g(1);
        f(str, mVar);
    }

    public void i(String str, m<MediaBrowserCompat.MediaItem> mVar) {
        mVar.g(2);
        mVar.f(null);
    }

    public void j(String str, Bundle bundle, m<List<MediaBrowserCompat.MediaItem>> mVar) {
        mVar.g(4);
        mVar.f(null);
    }

    public void k(String str, Bundle bundle) {
    }

    public void l(String str) {
    }

    void m(String str, Bundle bundle, f fVar, ResultReceiver resultReceiver) {
        d dVar = new d(str, resultReceiver);
        this.f5973e = fVar;
        d(str, bundle, dVar);
        this.f5973e = null;
        if (dVar.b()) {
            return;
        }
        throw new IllegalStateException("onCustomAction must call detach() or sendResult() or sendError() before returning for action=" + str + " extras=" + bundle);
    }

    void n(String str, f fVar, Bundle bundle, Bundle bundle2) {
        a aVar = new a(str, fVar, str, bundle, bundle2);
        this.f5973e = fVar;
        if (bundle == null) {
            f(str, aVar);
        } else {
            g(str, aVar, bundle);
        }
        this.f5973e = null;
        if (aVar.b()) {
            return;
        }
        throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + fVar.f5989a + " id=" + str);
    }

    void o(String str, f fVar, ResultReceiver resultReceiver) {
        b bVar = new b(str, resultReceiver);
        this.f5973e = fVar;
        i(str, bVar);
        this.f5973e = null;
        if (bVar.b()) {
            return;
        }
        throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + str);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f5969a.a(intent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        int i8 = Build.VERSION.SDK_INT;
        this.f5969a = i8 >= 28 ? new k() : i8 >= 26 ? new j() : i8 >= 23 ? new i() : i8 >= 21 ? new h() : new l();
        this.f5969a.onCreate();
    }

    void p(String str, Bundle bundle, f fVar, ResultReceiver resultReceiver) {
        c cVar = new c(str, resultReceiver);
        this.f5973e = fVar;
        j(str, bundle, cVar);
        this.f5973e = null;
        if (cVar.b()) {
            return;
        }
        throw new IllegalStateException("onSearch must call detach() or sendResult() before returning for query=" + str);
    }

    boolean q(String str, f fVar, IBinder iBinder) {
        boolean z4 = false;
        try {
            if (iBinder == null) {
                return fVar.f5995g.remove(str) != null;
            }
            List<androidx.core.util.d<IBinder, Bundle>> list = fVar.f5995g.get(str);
            if (list != null) {
                Iterator<androidx.core.util.d<IBinder, Bundle>> it = list.iterator();
                while (it.hasNext()) {
                    if (iBinder == it.next().f4889a) {
                        it.remove();
                        z4 = true;
                    }
                }
                if (list.size() == 0) {
                    fVar.f5995g.remove(str);
                }
            }
            return z4;
        } finally {
            this.f5973e = fVar;
            l(str);
            this.f5973e = null;
        }
    }
}
