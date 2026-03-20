package i6;

import android.os.Build;
import android.util.Log;
import com.google.android.gms.cloudmessaging.zze;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends ClassLoader {
    @Override // java.lang.ClassLoader
    protected final Class loadClass(String str, boolean z4) {
        if (str == "com.google.android.gms.iid.MessengerCompat" || (str != null && str.equals("com.google.android.gms.iid.MessengerCompat"))) {
            if (Log.isLoggable("CloudMessengerCompat", 3) || (Build.VERSION.SDK_INT == 23 && Log.isLoggable("CloudMessengerCompat", 3))) {
                Log.d("CloudMessengerCompat", "Using renamed FirebaseIidMessengerCompat class");
                return zze.class;
            }
            return zze.class;
        }
        return super.loadClass(str, z4);
    }
}
