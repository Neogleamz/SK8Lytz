package androidx.browser.trusted;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import androidx.browser.trusted.d;
import androidx.core.app.n;
import c.b;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class TrustedWebActivityService extends Service {

    /* renamed from: a  reason: collision with root package name */
    private NotificationManager f1694a;

    /* renamed from: b  reason: collision with root package name */
    int f1695b = -1;

    /* renamed from: c  reason: collision with root package name */
    private final b.a f1696c = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends b.a {
        a() {
        }

        private void d() {
            TrustedWebActivityService trustedWebActivityService = TrustedWebActivityService.this;
            if (trustedWebActivityService.f1695b == -1) {
                trustedWebActivityService.getPackageManager().getPackagesForUid(Binder.getCallingUid());
                TrustedWebActivityService.this.c().a();
                TrustedWebActivityService.this.getPackageManager();
            }
            if (TrustedWebActivityService.this.f1695b != Binder.getCallingUid()) {
                throw new SecurityException("Caller is not verified as Trusted Web Activity provider.");
            }
        }

        @Override // c.b
        public int S0() {
            d();
            return TrustedWebActivityService.this.j();
        }

        @Override // c.b
        public Bundle W0() {
            d();
            return TrustedWebActivityService.this.i();
        }

        @Override // c.b
        public Bundle c1(Bundle bundle) {
            d();
            return new d.e(TrustedWebActivityService.this.d(d.c.a(bundle).f1702a)).a();
        }

        @Override // c.b
        public Bundle j0(String str, Bundle bundle, IBinder iBinder) {
            d();
            return TrustedWebActivityService.this.f(str, bundle, c.a(iBinder));
        }

        @Override // c.b
        public void p1(Bundle bundle) {
            d();
            d.b a9 = d.b.a(bundle);
            TrustedWebActivityService.this.e(a9.f1700a, a9.f1701b);
        }

        @Override // c.b
        public Bundle r() {
            d();
            return new d.a(TrustedWebActivityService.this.g()).a();
        }

        @Override // c.b
        public Bundle r1(Bundle bundle) {
            d();
            d.C0015d a9 = d.C0015d.a(bundle);
            return new d.e(TrustedWebActivityService.this.k(a9.f1703a, a9.f1704b, a9.f1705c, a9.f1706d)).a();
        }
    }

    private static String a(String str) {
        return str.toLowerCase(Locale.ROOT).replace(' ', '_') + "_channel_id";
    }

    private void b() {
        if (this.f1694a == null) {
            throw new IllegalStateException("TrustedWebActivityService has not been properly initialized. Did onCreate() call super.onCreate()?");
        }
    }

    public abstract p.b c();

    public boolean d(String str) {
        b();
        if (n.e(this).a()) {
            if (Build.VERSION.SDK_INT < 26) {
                return true;
            }
            return b.b(this.f1694a, a(str));
        }
        return false;
    }

    public void e(String str, int i8) {
        b();
        this.f1694a.cancel(str, i8);
    }

    public Bundle f(String str, Bundle bundle, c cVar) {
        return null;
    }

    public Parcelable[] g() {
        b();
        if (Build.VERSION.SDK_INT >= 23) {
            return androidx.browser.trusted.a.a(this.f1694a);
        }
        throw new IllegalStateException("onGetActiveNotifications cannot be called pre-M.");
    }

    public Bundle i() {
        int j8 = j();
        Bundle bundle = new Bundle();
        if (j8 == -1) {
            return bundle;
        }
        bundle.putParcelable("android.support.customtabs.trusted.SMALL_ICON_BITMAP", BitmapFactory.decodeResource(getResources(), j8));
        return bundle;
    }

    public int j() {
        try {
            Bundle bundle = getPackageManager().getServiceInfo(new ComponentName(this, getClass()), RecognitionOptions.ITF).metaData;
            if (bundle == null) {
                return -1;
            }
            return bundle.getInt("android.support.customtabs.trusted.SMALL_ICON", -1);
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    public boolean k(String str, int i8, Notification notification, String str2) {
        b();
        if (n.e(this).a()) {
            if (Build.VERSION.SDK_INT >= 26) {
                String a9 = a(str2);
                notification = b.a(this, this.f1694a, notification, a9, str2);
                if (!b.b(this.f1694a, a9)) {
                    return false;
                }
            }
            this.f1694a.notify(str, i8, notification);
            return true;
        }
        return false;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return this.f1696c;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f1694a = (NotificationManager) getSystemService("notification");
    }

    @Override // android.app.Service
    public final boolean onUnbind(Intent intent) {
        this.f1695b = -1;
        return super.onUnbind(intent);
    }
}
