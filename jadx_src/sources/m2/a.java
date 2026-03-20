package m2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import io.flutter.plugin.common.l;
import java.util.HashMap;
import yf.a;
import zf.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements yf.a, j.c, zf.a, l.a {

    /* renamed from: e  reason: collision with root package name */
    private static j f21825e;
    @SuppressLint({"StaticFieldLeak"})

    /* renamed from: f  reason: collision with root package name */
    private static Context f21826f;

    /* renamed from: a  reason: collision with root package name */
    private LocationManager f21827a;

    /* renamed from: b  reason: collision with root package name */
    private ContentResolver f21828b;

    /* renamed from: c  reason: collision with root package name */
    private c f21829c;

    /* renamed from: d  reason: collision with root package name */
    private j.d f21830d;

    private void a(j.d dVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("success", e() ? Boolean.TRUE : Boolean.FALSE);
        dVar.success(hashMap);
    }

    private void b() {
        Location c9 = c();
        HashMap hashMap = new HashMap();
        if (c9 != null) {
            hashMap.put("latitude", Double.valueOf(c9.getLatitude()));
            hashMap.put("longitude", Double.valueOf(c9.getLongitude()));
        } else {
            hashMap.put("error", "error");
        }
        f21825e.c("receiveLocation", hashMap);
    }

    private Location c() {
        Location location = null;
        if (androidx.core.content.a.a(f21826f, "android.permission.ACCESS_FINE_LOCATION") == 0 || androidx.core.content.a.a(f21826f, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            for (String str : this.f21827a.getAllProviders()) {
                Location lastKnownLocation = this.f21827a.getLastKnownLocation(str);
                if (lastKnownLocation != null && (location == null || lastKnownLocation.getAccuracy() < location.getAccuracy())) {
                    location = lastKnownLocation;
                }
            }
            return location;
        }
        return null;
    }

    private void d() {
        if (this.f21827a == null) {
            this.f21827a = (LocationManager) f21826f.getApplicationContext().getSystemService("location");
        }
        if (this.f21828b == null) {
            this.f21828b = f21826f.getApplicationContext().getContentResolver();
        }
    }

    private boolean e() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(this.f21828b, "location_mode") != 0;
            } catch (Settings.SettingNotFoundException e8) {
                e8.printStackTrace();
                return false;
            }
        }
        return !TextUtils.isEmpty(Settings.Secure.getString(this.f21828b, "location_providers_allowed"));
    }

    private void f() {
        this.f21829c.getActivity().startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 1001);
    }

    public boolean onActivityResult(int i8, int i9, Intent intent) {
        Log.d("TAG", "requestCode:" + i8);
        if (i8 == 1001) {
            this.f21830d.success(Boolean.FALSE);
            this.f21830d = null;
            return true;
        }
        return false;
    }

    public void onAttachedToActivity(c cVar) {
        this.f21829c = cVar;
        cVar.a(this);
    }

    public void onAttachedToEngine(a.b bVar) {
        j jVar = new j(bVar.d().j(), "location_service_check");
        f21825e = jVar;
        jVar.e(this);
        f21826f = bVar.a();
    }

    public void onDetachedFromActivity() {
        this.f21829c.d(this);
        this.f21829c = null;
    }

    public void onDetachedFromActivityForConfigChanges() {
    }

    public void onDetachedFromEngine(a.b bVar) {
        f21825e.e((j.c) null);
    }

    public void onMethodCall(i iVar, j.d dVar) {
        d();
        if ("getPlatformVersion".equals(iVar.a)) {
            dVar.success("Android " + Build.VERSION.RELEASE);
        } else if ("checkLocationIsOpen".equals(iVar.a)) {
            a(dVar);
        } else if ("openSetting".equals(iVar.a)) {
            this.f21830d = dVar;
            f();
        } else if ("getLocation".equals(iVar.a)) {
            b();
        } else {
            dVar.b();
        }
    }

    public void onReattachedToActivityForConfigChanges(c cVar) {
    }
}
