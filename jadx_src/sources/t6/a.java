package t6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import n6.j;
import n6.m0;
import u6.m;
import w6.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: b  reason: collision with root package name */
    private static final Object f22929b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private static volatile a f22930c;

    /* renamed from: a  reason: collision with root package name */
    public final ConcurrentHashMap f22931a = new ConcurrentHashMap();

    private a() {
    }

    public static a b() {
        if (f22930c == null) {
            synchronized (f22929b) {
                if (f22930c == null) {
                    f22930c = new a();
                }
            }
        }
        a aVar = f22930c;
        j.l(aVar);
        return aVar;
    }

    private static void e(Context context, ServiceConnection serviceConnection) {
        try {
            context.unbindService(serviceConnection);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException unused) {
        }
    }

    private final boolean f(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i8, boolean z4, Executor executor) {
        ComponentName component = intent.getComponent();
        if (component != null) {
            String packageName = component.getPackageName();
            "com.google.android.gms".equals(packageName);
            try {
                if ((c.a(context).c(packageName, 0).flags & 2097152) != 0) {
                    Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
                    return false;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        if (g(serviceConnection)) {
            ServiceConnection serviceConnection2 = (ServiceConnection) this.f22931a.putIfAbsent(serviceConnection, serviceConnection);
            if (serviceConnection2 != null && serviceConnection != serviceConnection2) {
                Log.w("ConnectionTracker", String.format("Duplicate binding with the same ServiceConnection: %s, %s, %s.", serviceConnection, str, intent.getAction()));
            }
            try {
                boolean h8 = h(context, intent, serviceConnection, i8, executor);
                if (h8) {
                    return h8;
                }
                return false;
            } finally {
                this.f22931a.remove(serviceConnection, serviceConnection);
            }
        }
        return h(context, intent, serviceConnection, i8, executor);
    }

    private static boolean g(ServiceConnection serviceConnection) {
        return !(serviceConnection instanceof m0);
    }

    private static final boolean h(Context context, Intent intent, ServiceConnection serviceConnection, int i8, Executor executor) {
        if (executor == null) {
            executor = null;
        }
        return (!m.j() || executor == null) ? context.bindService(intent, serviceConnection, i8) : context.bindService(intent, i8, executor, serviceConnection);
    }

    @ResultIgnorabilityUnspecified
    public boolean a(Context context, Intent intent, ServiceConnection serviceConnection, int i8) {
        return f(context, context.getClass().getName(), intent, serviceConnection, i8, true, null);
    }

    public void c(Context context, ServiceConnection serviceConnection) {
        if (!g(serviceConnection) || !this.f22931a.containsKey(serviceConnection)) {
            e(context, serviceConnection);
            return;
        }
        try {
            e(context, (ServiceConnection) this.f22931a.get(serviceConnection));
        } finally {
            this.f22931a.remove(serviceConnection);
        }
    }

    @ResultIgnorabilityUnspecified
    public final boolean d(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i8, Executor executor) {
        return f(context, str, intent, serviceConnection, 4225, true, executor);
    }
}
