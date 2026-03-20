package androidx.core.app;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import d.a;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n {

    /* renamed from: d  reason: collision with root package name */
    private static String f4562d;

    /* renamed from: g  reason: collision with root package name */
    private static d f4565g;

    /* renamed from: a  reason: collision with root package name */
    private final Context f4566a;

    /* renamed from: b  reason: collision with root package name */
    private final NotificationManager f4567b;

    /* renamed from: c  reason: collision with root package name */
    private static final Object f4561c = new Object();

    /* renamed from: e  reason: collision with root package name */
    private static Set<String> f4563e = new HashSet();

    /* renamed from: f  reason: collision with root package name */
    private static final Object f4564f = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements e {

        /* renamed from: a  reason: collision with root package name */
        final String f4568a;

        /* renamed from: b  reason: collision with root package name */
        final int f4569b;

        /* renamed from: c  reason: collision with root package name */
        final String f4570c;

        /* renamed from: d  reason: collision with root package name */
        final boolean f4571d;

        a(String str) {
            this.f4568a = str;
            this.f4569b = 0;
            this.f4570c = null;
            this.f4571d = true;
        }

        a(String str, int i8, String str2) {
            this.f4568a = str;
            this.f4569b = i8;
            this.f4570c = str2;
            this.f4571d = false;
        }

        @Override // androidx.core.app.n.e
        public void a(d.a aVar) {
            if (this.f4571d) {
                aVar.M(this.f4568a);
            } else {
                aVar.b1(this.f4568a, this.f4569b, this.f4570c);
            }
        }

        public String toString() {
            return "CancelTask[packageName:" + this.f4568a + ", id:" + this.f4569b + ", tag:" + this.f4570c + ", all:" + this.f4571d + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements e {

        /* renamed from: a  reason: collision with root package name */
        final String f4572a;

        /* renamed from: b  reason: collision with root package name */
        final int f4573b;

        /* renamed from: c  reason: collision with root package name */
        final String f4574c;

        /* renamed from: d  reason: collision with root package name */
        final Notification f4575d;

        b(String str, int i8, String str2, Notification notification) {
            this.f4572a = str;
            this.f4573b = i8;
            this.f4574c = str2;
            this.f4575d = notification;
        }

        @Override // androidx.core.app.n.e
        public void a(d.a aVar) {
            aVar.R1(this.f4572a, this.f4573b, this.f4574c, this.f4575d);
        }

        public String toString() {
            return "NotifyTask[packageName:" + this.f4572a + ", id:" + this.f4573b + ", tag:" + this.f4574c + "]";
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {

        /* renamed from: a  reason: collision with root package name */
        final ComponentName f4576a;

        /* renamed from: b  reason: collision with root package name */
        final IBinder f4577b;

        c(ComponentName componentName, IBinder iBinder) {
            this.f4576a = componentName;
            this.f4577b = iBinder;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d implements Handler.Callback, ServiceConnection {

        /* renamed from: a  reason: collision with root package name */
        private final Context f4578a;

        /* renamed from: b  reason: collision with root package name */
        private final HandlerThread f4579b;

        /* renamed from: c  reason: collision with root package name */
        private final Handler f4580c;

        /* renamed from: d  reason: collision with root package name */
        private final Map<ComponentName, a> f4581d = new HashMap();

        /* renamed from: e  reason: collision with root package name */
        private Set<String> f4582e = new HashSet();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a {

            /* renamed from: a  reason: collision with root package name */
            final ComponentName f4583a;

            /* renamed from: c  reason: collision with root package name */
            d.a f4585c;

            /* renamed from: b  reason: collision with root package name */
            boolean f4584b = false;

            /* renamed from: d  reason: collision with root package name */
            ArrayDeque<e> f4586d = new ArrayDeque<>();

            /* renamed from: e  reason: collision with root package name */
            int f4587e = 0;

            a(ComponentName componentName) {
                this.f4583a = componentName;
            }
        }

        d(Context context) {
            this.f4578a = context;
            HandlerThread handlerThread = new HandlerThread("NotificationManagerCompat");
            this.f4579b = handlerThread;
            handlerThread.start();
            this.f4580c = new Handler(handlerThread.getLooper(), this);
        }

        private boolean a(a aVar) {
            if (aVar.f4584b) {
                return true;
            }
            boolean bindService = this.f4578a.bindService(new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL").setComponent(aVar.f4583a), this, 33);
            aVar.f4584b = bindService;
            if (bindService) {
                aVar.f4587e = 0;
            } else {
                Log.w("NotifManCompat", "Unable to bind to listener " + aVar.f4583a);
                this.f4578a.unbindService(this);
            }
            return aVar.f4584b;
        }

        private void b(a aVar) {
            if (aVar.f4584b) {
                this.f4578a.unbindService(this);
                aVar.f4584b = false;
            }
            aVar.f4585c = null;
        }

        private void c(e eVar) {
            j();
            for (a aVar : this.f4581d.values()) {
                aVar.f4586d.add(eVar);
                g(aVar);
            }
        }

        private void d(ComponentName componentName) {
            a aVar = this.f4581d.get(componentName);
            if (aVar != null) {
                g(aVar);
            }
        }

        private void e(ComponentName componentName, IBinder iBinder) {
            a aVar = this.f4581d.get(componentName);
            if (aVar != null) {
                aVar.f4585c = a.AbstractBinderC0162a.d(iBinder);
                aVar.f4587e = 0;
                g(aVar);
            }
        }

        private void f(ComponentName componentName) {
            a aVar = this.f4581d.get(componentName);
            if (aVar != null) {
                b(aVar);
            }
        }

        private void g(a aVar) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Processing component " + aVar.f4583a + ", " + aVar.f4586d.size() + " queued tasks");
            }
            if (aVar.f4586d.isEmpty()) {
                return;
            }
            if (!a(aVar) || aVar.f4585c == null) {
                i(aVar);
                return;
            }
            while (true) {
                e peek = aVar.f4586d.peek();
                if (peek == null) {
                    break;
                }
                try {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        Log.d("NotifManCompat", "Sending task " + peek);
                    }
                    peek.a(aVar.f4585c);
                    aVar.f4586d.remove();
                } catch (DeadObjectException unused) {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        Log.d("NotifManCompat", "Remote service has died: " + aVar.f4583a);
                    }
                } catch (RemoteException e8) {
                    Log.w("NotifManCompat", "RemoteException communicating with " + aVar.f4583a, e8);
                }
            }
            if (aVar.f4586d.isEmpty()) {
                return;
            }
            i(aVar);
        }

        private void i(a aVar) {
            if (this.f4580c.hasMessages(3, aVar.f4583a)) {
                return;
            }
            int i8 = aVar.f4587e + 1;
            aVar.f4587e = i8;
            if (i8 <= 6) {
                int i9 = (1 << (i8 - 1)) * 1000;
                if (Log.isLoggable("NotifManCompat", 3)) {
                    Log.d("NotifManCompat", "Scheduling retry for " + i9 + " ms");
                }
                this.f4580c.sendMessageDelayed(this.f4580c.obtainMessage(3, aVar.f4583a), i9);
                return;
            }
            Log.w("NotifManCompat", "Giving up on delivering " + aVar.f4586d.size() + " tasks to " + aVar.f4583a + " after " + aVar.f4587e + " retries");
            aVar.f4586d.clear();
        }

        private void j() {
            Set<String> f5 = n.f(this.f4578a);
            if (f5.equals(this.f4582e)) {
                return;
            }
            this.f4582e = f5;
            List<ResolveInfo> queryIntentServices = this.f4578a.getPackageManager().queryIntentServices(new Intent().setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 0);
            HashSet<ComponentName> hashSet = new HashSet();
            for (ResolveInfo resolveInfo : queryIntentServices) {
                if (f5.contains(resolveInfo.serviceInfo.packageName)) {
                    ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                    ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                    if (resolveInfo.serviceInfo.permission != null) {
                        Log.w("NotifManCompat", "Permission present on component " + componentName + ", not adding listener record.");
                    } else {
                        hashSet.add(componentName);
                    }
                }
            }
            for (ComponentName componentName2 : hashSet) {
                if (!this.f4581d.containsKey(componentName2)) {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        Log.d("NotifManCompat", "Adding listener record for " + componentName2);
                    }
                    this.f4581d.put(componentName2, new a(componentName2));
                }
            }
            Iterator<Map.Entry<ComponentName, a>> it = this.f4581d.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ComponentName, a> next = it.next();
                if (!hashSet.contains(next.getKey())) {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        Log.d("NotifManCompat", "Removing listener record for " + next.getKey());
                    }
                    b(next.getValue());
                    it.remove();
                }
            }
        }

        public void h(e eVar) {
            this.f4580c.obtainMessage(0, eVar).sendToTarget();
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i8 = message.what;
            if (i8 == 0) {
                c((e) message.obj);
                return true;
            } else if (i8 == 1) {
                c cVar = (c) message.obj;
                e(cVar.f4576a, cVar.f4577b);
                return true;
            } else if (i8 == 2) {
                f((ComponentName) message.obj);
                return true;
            } else if (i8 != 3) {
                return false;
            } else {
                d((ComponentName) message.obj);
                return true;
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Connected to service " + componentName);
            }
            this.f4580c.obtainMessage(1, new c(componentName, iBinder)).sendToTarget();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Disconnected from service " + componentName);
            }
            this.f4580c.obtainMessage(2, componentName).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a(d.a aVar);
    }

    private n(Context context) {
        this.f4566a = context;
        this.f4567b = (NotificationManager) context.getSystemService("notification");
    }

    public static n e(Context context) {
        return new n(context);
    }

    public static Set<String> f(Context context) {
        Set<String> set;
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        synchronized (f4561c) {
            if (string != null) {
                if (!string.equals(f4562d)) {
                    String[] split = string.split(":", -1);
                    HashSet hashSet = new HashSet(split.length);
                    for (String str : split) {
                        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                        if (unflattenFromString != null) {
                            hashSet.add(unflattenFromString.getPackageName());
                        }
                    }
                    f4563e = hashSet;
                    f4562d = string;
                }
            }
            set = f4563e;
        }
        return set;
    }

    private void j(e eVar) {
        synchronized (f4564f) {
            if (f4565g == null) {
                f4565g = new d(this.f4566a.getApplicationContext());
            }
            f4565g.h(eVar);
        }
    }

    private static boolean k(Notification notification) {
        Bundle a9 = k.a(notification);
        return a9 != null && a9.getBoolean("android.support.useSideChannel");
    }

    public boolean a() {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 24) {
            return this.f4567b.areNotificationsEnabled();
        }
        if (i8 >= 19) {
            AppOpsManager appOpsManager = (AppOpsManager) this.f4566a.getSystemService("appops");
            ApplicationInfo applicationInfo = this.f4566a.getApplicationInfo();
            String packageName = this.f4566a.getApplicationContext().getPackageName();
            int i9 = applicationInfo.uid;
            try {
                Class<?> cls = Class.forName(AppOpsManager.class.getName());
                Class<?> cls2 = Integer.TYPE;
                return ((Integer) cls.getMethod("checkOpNoThrow", cls2, cls2, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(i9), packageName)).intValue() == 0;
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | RuntimeException | InvocationTargetException unused) {
                return true;
            }
        }
        return true;
    }

    public void b(int i8) {
        c(null, i8);
    }

    public void c(String str, int i8) {
        this.f4567b.cancel(str, i8);
        if (Build.VERSION.SDK_INT <= 19) {
            j(new a(this.f4566a.getPackageName(), i8, str));
        }
    }

    public void d() {
        this.f4567b.cancelAll();
        if (Build.VERSION.SDK_INT <= 19) {
            j(new a(this.f4566a.getPackageName()));
        }
    }

    public List<NotificationChannel> g() {
        return Build.VERSION.SDK_INT >= 26 ? this.f4567b.getNotificationChannels() : Collections.emptyList();
    }

    public void h(int i8, Notification notification) {
        i(null, i8, notification);
    }

    public void i(String str, int i8, Notification notification) {
        if (!k(notification)) {
            this.f4567b.notify(str, i8, notification);
            return;
        }
        j(new b(this.f4566a.getPackageName(), i8, str, notification));
        this.f4567b.cancel(str, i8);
    }
}
