package androidx.startup;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import v1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: d  reason: collision with root package name */
    private static volatile a f7290d;

    /* renamed from: e  reason: collision with root package name */
    private static final Object f7291e = new Object();

    /* renamed from: c  reason: collision with root package name */
    final Context f7294c;

    /* renamed from: b  reason: collision with root package name */
    final Set<Class<? extends v1.a<?>>> f7293b = new HashSet();

    /* renamed from: a  reason: collision with root package name */
    final Map<Class<?>, Object> f7292a = new HashMap();

    a(Context context) {
        this.f7294c = context.getApplicationContext();
    }

    private <T> T d(Class<? extends v1.a<?>> cls, Set<Class<?>> set) {
        T t8;
        if (w1.a.h()) {
            try {
                w1.a.c(cls.getSimpleName());
            } finally {
                w1.a.f();
            }
        }
        if (set.contains(cls)) {
            throw new IllegalStateException(String.format("Cannot initialize %s. Cycle detected.", cls.getName()));
        }
        if (this.f7292a.containsKey(cls)) {
            t8 = (T) this.f7292a.get(cls);
        } else {
            set.add(cls);
            v1.a<?> newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            List<Class<? extends v1.a<?>>> a9 = newInstance.a();
            if (!a9.isEmpty()) {
                for (Class<? extends v1.a<?>> cls2 : a9) {
                    if (!this.f7292a.containsKey(cls2)) {
                        d(cls2, set);
                    }
                }
            }
            t8 = (T) newInstance.b(this.f7294c);
            set.remove(cls);
            this.f7292a.put(cls, t8);
        }
        return t8;
    }

    public static a e(Context context) {
        if (f7290d == null) {
            synchronized (f7291e) {
                if (f7290d == null) {
                    f7290d = new a(context);
                }
            }
        }
        return f7290d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        try {
            try {
                w1.a.c("Startup");
                b(this.f7294c.getPackageManager().getProviderInfo(new ComponentName(this.f7294c.getPackageName(), InitializationProvider.class.getName()), RecognitionOptions.ITF).metaData);
            } catch (PackageManager.NameNotFoundException e8) {
                throw new StartupException(e8);
            }
        } finally {
            w1.a.f();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void b(Bundle bundle) {
        String string = this.f7294c.getString(b.f23158a);
        if (bundle != null) {
            try {
                HashSet hashSet = new HashSet();
                for (String str : bundle.keySet()) {
                    if (string.equals(bundle.getString(str, null))) {
                        Class<?> cls = Class.forName(str);
                        if (v1.a.class.isAssignableFrom(cls)) {
                            this.f7293b.add(cls);
                        }
                    }
                }
                for (Class<? extends v1.a<?>> cls2 : this.f7293b) {
                    d(cls2, hashSet);
                }
            } catch (ClassNotFoundException e8) {
                throw new StartupException(e8);
            }
        }
    }

    <T> T c(Class<? extends v1.a<?>> cls) {
        T t8;
        synchronized (f7291e) {
            t8 = (T) this.f7292a.get(cls);
            if (t8 == null) {
                t8 = (T) d(cls, new HashSet());
            }
        }
        return t8;
    }

    public <T> T f(Class<? extends v1.a<T>> cls) {
        return (T) c(cls);
    }

    public boolean g(Class<? extends v1.a<?>> cls) {
        return this.f7293b.contains(cls);
    }
}
