package u6;

import android.content.Context;
import android.util.Log;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f23067a = {"android.", "com.android.", "dalvik.", "java.", "javax."};

    @ResultIgnorabilityUnspecified
    public static boolean a(Context context, Throwable th) {
        try {
            n6.j.l(context);
            n6.j.l(th);
            return false;
        } catch (Exception e8) {
            Log.e("CrashUtils", "Error adding exception to DropBox!", e8);
            return false;
        }
    }
}
