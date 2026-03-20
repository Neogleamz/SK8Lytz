package androidx.media.session;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaButtonReceiver extends BroadcastReceiver {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends MediaBrowserCompat.c {

        /* renamed from: c  reason: collision with root package name */
        private final Context f6107c;

        /* renamed from: d  reason: collision with root package name */
        private final Intent f6108d;

        /* renamed from: e  reason: collision with root package name */
        private final BroadcastReceiver.PendingResult f6109e;

        /* renamed from: f  reason: collision with root package name */
        private MediaBrowserCompat f6110f;

        a(Context context, Intent intent, BroadcastReceiver.PendingResult pendingResult) {
            this.f6107c = context;
            this.f6108d = intent;
            this.f6109e = pendingResult;
        }

        private void e() {
            this.f6110f.b();
            this.f6109e.finish();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c
        public void a() {
            new MediaControllerCompat(this.f6107c, this.f6110f.c()).a((KeyEvent) this.f6108d.getParcelableExtra("android.intent.extra.KEY_EVENT"));
            e();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c
        public void b() {
            e();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.c
        public void c() {
            e();
        }

        void f(MediaBrowserCompat mediaBrowserCompat) {
            this.f6110f = mediaBrowserCompat;
        }
    }

    private static ComponentName a(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(str);
        intent.setPackage(context.getPackageName());
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 0);
        if (queryIntentServices.size() == 1) {
            ServiceInfo serviceInfo = queryIntentServices.get(0).serviceInfo;
            return new ComponentName(serviceInfo.packageName, serviceInfo.name);
        } else if (queryIntentServices.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("Expected 1 service that handles " + str + ", found " + queryIntentServices.size());
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !"android.intent.action.MEDIA_BUTTON".equals(intent.getAction()) || !intent.hasExtra("android.intent.extra.KEY_EVENT")) {
            Log.d("MediaButtonReceiver", "Ignore unsupported intent: " + intent);
            return;
        }
        ComponentName a9 = a(context, "android.intent.action.MEDIA_BUTTON");
        if (a9 != null) {
            intent.setComponent(a9);
            androidx.core.content.a.m(context, intent);
            return;
        }
        ComponentName a10 = a(context, "android.media.browse.MediaBrowserService");
        if (a10 == null) {
            throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or implements a media browser service.");
        }
        BroadcastReceiver.PendingResult goAsync = goAsync();
        Context applicationContext = context.getApplicationContext();
        a aVar = new a(applicationContext, intent, goAsync);
        MediaBrowserCompat mediaBrowserCompat = new MediaBrowserCompat(applicationContext, a10, aVar, null);
        aVar.f(mediaBrowserCompat);
        mediaBrowserCompat.a();
    }
}
