package com.dexterous.flutterlocalnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.n;
import io.flutter.plugin.common.d;
import io.flutter.view.FlutterCallbackInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import uf.a;
import wf.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionBroadcastReceiver extends BroadcastReceiver {

    /* renamed from: b  reason: collision with root package name */
    private static b f8837b;

    /* renamed from: c  reason: collision with root package name */
    private static io.flutter.embedding.engine.a f8838c;

    /* renamed from: a  reason: collision with root package name */
    t2.a f8839a;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements d.d {

        /* renamed from: a  reason: collision with root package name */
        final List<Map<String, Object>> f8840a;

        /* renamed from: b  reason: collision with root package name */
        private d.b f8841b;

        private b() {
            this.f8840a = new ArrayList();
        }

        public void a(Map<String, Object> map) {
            d.b bVar = this.f8841b;
            if (bVar != null) {
                bVar.success(map);
            } else {
                this.f8840a.add(map);
            }
        }

        public void b(Object obj, d.b bVar) {
            for (Map<String, Object> map : this.f8840a) {
                bVar.success(map);
            }
            this.f8840a.clear();
            this.f8841b = bVar;
        }

        public void d(Object obj) {
            this.f8841b = null;
        }
    }

    private void a(uf.a aVar) {
        new d(aVar.l(), "dexterous.com/flutter/local_notifications/actions").d(f8837b);
    }

    private void b(Context context) {
        if (f8838c != null) {
            Log.e("ActionBroadcastReceiver", "Engine is already initialised");
            return;
        }
        f c9 = sf.a.e().c();
        c9.s(context);
        c9.h(context, (String[]) null);
        f8838c = new io.flutter.embedding.engine.a(context);
        FlutterCallbackInformation d8 = this.f8839a.d();
        if (d8 == null) {
            Log.w("ActionBroadcastReceiver", "Callback information could not be retrieved");
            return;
        }
        uf.a j8 = f8838c.j();
        a(j8);
        j8.j(new a.b(context.getAssets(), c9.j(), d8));
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.dexterous.flutterlocalnotifications.ActionBroadcastReceiver.ACTION_TAPPED".equalsIgnoreCase(intent.getAction())) {
            t2.a aVar = this.f8839a;
            if (aVar == null) {
                aVar = new t2.a(context);
            }
            this.f8839a = aVar;
            Map<String, Object> extractNotificationResponseMap = FlutterLocalNotificationsPlugin.extractNotificationResponseMap(intent);
            if (intent.getBooleanExtra("cancelNotification", false)) {
                int intValue = ((Integer) extractNotificationResponseMap.get("notificationId")).intValue();
                Object obj = extractNotificationResponseMap.get("notificationTag");
                if (obj instanceof String) {
                    n.e(context).c((String) obj, intValue);
                } else {
                    n.e(context).b(intValue);
                }
            }
            if (f8837b == null) {
                f8837b = new b();
            }
            f8837b.a(extractNotificationResponseMap);
            b(context);
        }
    }
}
