package t2;

import android.content.Context;
import android.content.SharedPreferences;
import io.flutter.view.FlutterCallbackInformation;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final String f22845a = "com.dexterous.flutterlocalnotifications.CALLBACK_DISPATCHER_HANDLE_KEY";

    /* renamed from: b  reason: collision with root package name */
    private final String f22846b = "com.dexterous.flutterlocalnotifications.CALLBACK_HANDLE_KEY";

    /* renamed from: c  reason: collision with root package name */
    private final Context f22847c;

    public a(Context context) {
        this.f22847c = context;
    }

    private SharedPreferences a() {
        return this.f22847c.getSharedPreferences("flutter_local_notifications_plugin", 0);
    }

    public Long b() {
        return Long.valueOf(a().getLong("com.dexterous.flutterlocalnotifications.CALLBACK_DISPATCHER_HANDLE_KEY", -1L));
    }

    public Long c() {
        return Long.valueOf(a().getLong("com.dexterous.flutterlocalnotifications.CALLBACK_HANDLE_KEY", -1L));
    }

    public FlutterCallbackInformation d() {
        return FlutterCallbackInformation.lookupCallbackInformation(b().longValue());
    }

    public void e(Long l8, Long l9) {
        a().edit().putLong("com.dexterous.flutterlocalnotifications.CALLBACK_DISPATCHER_HANDLE_KEY", l8.longValue()).apply();
        a().edit().putLong("com.dexterous.flutterlocalnotifications.CALLBACK_HANDLE_KEY", l9.longValue()).apply();
    }
}
