package u6;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p {
    public static boolean a(Context context, int i8) {
        if (b(context, i8, "com.google.android.gms")) {
            try {
                return com.google.android.gms.common.e.a(context).b(context.getPackageManager().getPackageInfo("com.google.android.gms", 64));
            } catch (PackageManager.NameNotFoundException unused) {
                if (Log.isLoggable("UidVerifier", 3)) {
                    Log.d("UidVerifier", "Package manager can't find google play services package, defaulting to false");
                    return false;
                }
                return false;
            }
        }
        return false;
    }

    @TargetApi(19)
    public static boolean b(Context context, int i8, String str) {
        return w6.c.a(context).g(i8, str);
    }
}
