package androidx.camera.core.impl.utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static Context a(Context context, String str) {
            return context.createAttributionContext(str);
        }

        static String b(Context context) {
            return context.getAttributionTag();
        }
    }

    public static Context a(Context context) {
        String b9;
        Context applicationContext = context.getApplicationContext();
        return (Build.VERSION.SDK_INT < 30 || (b9 = a.b(context)) == null) ? applicationContext : a.a(applicationContext, b9);
    }

    public static Application b(Context context) {
        for (Context a9 = a(context); a9 instanceof ContextWrapper; a9 = c((ContextWrapper) a9)) {
            if (a9 instanceof Application) {
                return (Application) a9;
            }
        }
        return null;
    }

    public static Context c(ContextWrapper contextWrapper) {
        String b9;
        Context baseContext = contextWrapper.getBaseContext();
        return (Build.VERSION.SDK_INT < 30 || (b9 = a.b(contextWrapper)) == null) ? baseContext : a.a(baseContext, b9);
    }
}
