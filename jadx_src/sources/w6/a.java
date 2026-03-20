package w6;

import android.content.Context;
import u6.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private static Context f23669a;

    /* renamed from: b  reason: collision with root package name */
    private static Boolean f23670b;

    public static synchronized boolean a(Context context) {
        Boolean bool;
        Boolean bool2;
        synchronized (a.class) {
            Context applicationContext = context.getApplicationContext();
            Context context2 = f23669a;
            if (context2 != null && (bool2 = f23670b) != null && context2 == applicationContext) {
                return bool2.booleanValue();
            }
            f23670b = null;
            if (!m.h()) {
                try {
                    context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                    f23670b = Boolean.TRUE;
                } catch (ClassNotFoundException unused) {
                    bool = Boolean.FALSE;
                }
                f23669a = applicationContext;
                return f23670b.booleanValue();
            }
            bool = Boolean.valueOf(applicationContext.getPackageManager().isInstantApp());
            f23670b = bool;
            f23669a = applicationContext;
            return f23670b.booleanValue();
        }
    }
}
