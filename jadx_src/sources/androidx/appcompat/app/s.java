package androidx.appcompat.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.util.Calendar;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class s {

    /* renamed from: d  reason: collision with root package name */
    private static s f717d;

    /* renamed from: a  reason: collision with root package name */
    private final Context f718a;

    /* renamed from: b  reason: collision with root package name */
    private final LocationManager f719b;

    /* renamed from: c  reason: collision with root package name */
    private final a f720c = new a();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        boolean f721a;

        /* renamed from: b  reason: collision with root package name */
        long f722b;

        a() {
        }
    }

    s(Context context, LocationManager locationManager) {
        this.f718a = context;
        this.f719b = locationManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static s a(Context context) {
        if (f717d == null) {
            Context applicationContext = context.getApplicationContext();
            f717d = new s(applicationContext, (LocationManager) applicationContext.getSystemService("location"));
        }
        return f717d;
    }

    @SuppressLint({"MissingPermission"})
    private Location b() {
        Location c9 = androidx.core.content.b.b(this.f718a, "android.permission.ACCESS_COARSE_LOCATION") == 0 ? c("network") : null;
        Location c10 = androidx.core.content.b.b(this.f718a, "android.permission.ACCESS_FINE_LOCATION") == 0 ? c("gps") : null;
        return (c10 == null || c9 == null) ? c10 != null ? c10 : c9 : c10.getTime() > c9.getTime() ? c10 : c9;
    }

    private Location c(String str) {
        try {
            if (this.f719b.isProviderEnabled(str)) {
                return this.f719b.getLastKnownLocation(str);
            }
            return null;
        } catch (Exception e8) {
            Log.d("TwilightManager", "Failed to get last known location", e8);
            return null;
        }
    }

    private boolean e() {
        return this.f720c.f722b > System.currentTimeMillis();
    }

    private void f(Location location) {
        long j8;
        a aVar = this.f720c;
        long currentTimeMillis = System.currentTimeMillis();
        r b9 = r.b();
        b9.a(currentTimeMillis - 86400000, location.getLatitude(), location.getLongitude());
        b9.a(currentTimeMillis, location.getLatitude(), location.getLongitude());
        boolean z4 = b9.f716c == 1;
        long j9 = b9.f715b;
        long j10 = b9.f714a;
        b9.a(currentTimeMillis + 86400000, location.getLatitude(), location.getLongitude());
        long j11 = b9.f715b;
        if (j9 == -1 || j10 == -1) {
            j8 = 43200000 + currentTimeMillis;
        } else {
            j8 = (currentTimeMillis > j10 ? j11 + 0 : currentTimeMillis > j9 ? j10 + 0 : j9 + 0) + 60000;
        }
        aVar.f721a = z4;
        aVar.f722b = j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        a aVar = this.f720c;
        if (e()) {
            return aVar.f721a;
        }
        Location b9 = b();
        if (b9 != null) {
            f(b9);
            return aVar.f721a;
        }
        Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int i8 = Calendar.getInstance().get(11);
        return i8 < 6 || i8 >= 22;
    }
}
