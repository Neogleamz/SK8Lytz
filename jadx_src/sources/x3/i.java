package x3;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import com.google.android.datatransport.runtime.backends.TransportBackendDiscovery;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class i implements d {

    /* renamed from: a  reason: collision with root package name */
    private final a f23786a;

    /* renamed from: b  reason: collision with root package name */
    private final g f23787b;

    /* renamed from: c  reason: collision with root package name */
    private final Map<String, k> f23788c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {

        /* renamed from: a  reason: collision with root package name */
        private final Context f23789a;

        /* renamed from: b  reason: collision with root package name */
        private Map<String, String> f23790b = null;

        a(Context context) {
            this.f23789a = context;
        }

        private Map<String, String> a(Context context) {
            Bundle d8 = d(context);
            if (d8 == null) {
                Log.w("BackendRegistry", "Could not retrieve metadata, returning empty list of transport backends.");
                return Collections.emptyMap();
            }
            HashMap hashMap = new HashMap();
            for (String str : d8.keySet()) {
                Object obj = d8.get(str);
                if ((obj instanceof String) && str.startsWith("backend:")) {
                    for (String str2 : ((String) obj).split(",", -1)) {
                        String trim = str2.trim();
                        if (!trim.isEmpty()) {
                            hashMap.put(trim, str.substring(8));
                        }
                    }
                }
            }
            return hashMap;
        }

        private Map<String, String> c() {
            if (this.f23790b == null) {
                this.f23790b = a(this.f23789a);
            }
            return this.f23790b;
        }

        private static Bundle d(Context context) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager == null) {
                    Log.w("BackendRegistry", "Context has no PackageManager.");
                    return null;
                }
                ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, TransportBackendDiscovery.class), RecognitionOptions.ITF);
                if (serviceInfo == null) {
                    Log.w("BackendRegistry", "TransportBackendDiscovery has no service info.");
                    return null;
                }
                return serviceInfo.metaData;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("BackendRegistry", "Application info not found.");
                return null;
            }
        }

        c b(String str) {
            String format;
            String format2;
            String str2 = c().get(str);
            if (str2 == null) {
                return null;
            }
            try {
                return (c) Class.forName(str2).asSubclass(c.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassNotFoundException e8) {
                e = e8;
                format2 = String.format("Class %s is not found.", str2);
                Log.w("BackendRegistry", format2, e);
                return null;
            } catch (IllegalAccessException e9) {
                e = e9;
                format2 = String.format("Could not instantiate %s.", str2);
                Log.w("BackendRegistry", format2, e);
                return null;
            } catch (InstantiationException e10) {
                e = e10;
                format2 = String.format("Could not instantiate %s.", str2);
                Log.w("BackendRegistry", format2, e);
                return null;
            } catch (NoSuchMethodException e11) {
                e = e11;
                format = String.format("Could not instantiate %s", str2);
                Log.w("BackendRegistry", format, e);
                return null;
            } catch (InvocationTargetException e12) {
                e = e12;
                format = String.format("Could not instantiate %s", str2);
                Log.w("BackendRegistry", format, e);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(Context context, g gVar) {
        this(new a(context), gVar);
    }

    i(a aVar, g gVar) {
        this.f23788c = new HashMap();
        this.f23786a = aVar;
        this.f23787b = gVar;
    }

    @Override // x3.d
    public synchronized k a(String str) {
        if (this.f23788c.containsKey(str)) {
            return this.f23788c.get(str);
        }
        c b9 = this.f23786a.b(str);
        if (b9 == null) {
            return null;
        }
        k create = b9.create(this.f23787b.a(str));
        this.f23788c.put(str, create);
        return create;
    }
}
