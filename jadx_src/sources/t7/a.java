package t7;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private static float f22932a = 1.0f;

    public float a(ContentResolver contentResolver) {
        int i8 = Build.VERSION.SDK_INT;
        return i8 >= 17 ? Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f) : i8 == 16 ? Settings.System.getFloat(contentResolver, "animator_duration_scale", 1.0f) : f22932a;
    }
}
