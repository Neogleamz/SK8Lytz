package n6;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f0 {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f22179a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static boolean f22180b;

    /* renamed from: c  reason: collision with root package name */
    private static String f22181c;

    /* renamed from: d  reason: collision with root package name */
    private static int f22182d;

    public static int a(Context context) {
        b(context);
        return f22182d;
    }

    private static void b(Context context) {
        Bundle bundle;
        synchronized (f22179a) {
            if (f22180b) {
                return;
            }
            f22180b = true;
            try {
                bundle = w6.c.a(context).c(context.getPackageName(), RecognitionOptions.ITF).metaData;
            } catch (PackageManager.NameNotFoundException e8) {
                Log.wtf("MetadataValueReader", "This should never happen.", e8);
            }
            if (bundle == null) {
                return;
            }
            f22181c = bundle.getString("com.google.app.id");
            f22182d = bundle.getInt("com.google.android.gms.version");
        }
    }
}
