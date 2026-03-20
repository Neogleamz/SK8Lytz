package e6;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import n6.j;
import z6.e;
import z6.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    j6.a f19801a;

    /* renamed from: b  reason: collision with root package name */
    f f19802b;

    /* renamed from: c  reason: collision with root package name */
    boolean f19803c;

    /* renamed from: d  reason: collision with root package name */
    final Object f19804d = new Object();

    /* renamed from: e  reason: collision with root package name */
    c f19805e;

    /* renamed from: f  reason: collision with root package name */
    private final Context f19806f;

    /* renamed from: g  reason: collision with root package name */
    final long f19807g;

    /* renamed from: e6.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0166a {

        /* renamed from: a  reason: collision with root package name */
        private final String f19808a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f19809b;

        @Deprecated
        public C0166a(String str, boolean z4) {
            this.f19808a = str;
            this.f19809b = z4;
        }

        public String a() {
            return this.f19808a;
        }

        public boolean b() {
            return this.f19809b;
        }

        public String toString() {
            String str = this.f19808a;
            boolean z4 = this.f19809b;
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 7);
            sb.append("{");
            sb.append(str);
            sb.append("}");
            sb.append(z4);
            return sb.toString();
        }
    }

    @VisibleForTesting
    public a(Context context, long j8, boolean z4, boolean z8) {
        Context applicationContext;
        j.l(context);
        if (z4 && (applicationContext = context.getApplicationContext()) != null) {
            context = applicationContext;
        }
        this.f19806f = context;
        this.f19803c = false;
        this.f19807g = j8;
    }

    public static C0166a a(Context context) {
        a aVar = new a(context, -1L, true, false);
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            aVar.d(false);
            C0166a f5 = aVar.f(-1);
            aVar.e(f5, true, 0.0f, SystemClock.elapsedRealtime() - elapsedRealtime, BuildConfig.FLAVOR, null);
            return f5;
        } finally {
        }
    }

    public static void b(boolean z4) {
    }

    private final C0166a f(int i8) {
        C0166a c0166a;
        j.k("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (!this.f19803c) {
                synchronized (this.f19804d) {
                    c cVar = this.f19805e;
                    if (cVar == null || !cVar.f19814d) {
                        throw new IOException("AdvertisingIdClient is not connected.");
                    }
                }
                try {
                    d(false);
                    if (!this.f19803c) {
                        throw new IOException("AdvertisingIdClient cannot reconnect.");
                    }
                } catch (Exception e8) {
                    throw new IOException("AdvertisingIdClient cannot reconnect.", e8);
                }
            }
            j.l(this.f19801a);
            j.l(this.f19802b);
            try {
                c0166a = new C0166a(this.f19802b.a(), this.f19802b.u(true));
            } catch (RemoteException e9) {
                Log.i("AdvertisingIdClient", "GMS remote exception ", e9);
                throw new IOException("Remote exception");
            }
        }
        g();
        return c0166a;
    }

    private final void g() {
        synchronized (this.f19804d) {
            c cVar = this.f19805e;
            if (cVar != null) {
                cVar.f19813c.countDown();
                try {
                    this.f19805e.join();
                } catch (InterruptedException unused) {
                }
            }
            long j8 = this.f19807g;
            if (j8 > 0) {
                this.f19805e = new c(this, j8);
            }
        }
    }

    public final void c() {
        j.k("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.f19806f == null || this.f19801a == null) {
                return;
            }
            if (this.f19803c) {
                t6.a.b().c(this.f19806f, this.f19801a);
            }
            this.f19803c = false;
            this.f19802b = null;
            this.f19801a = null;
        }
    }

    @VisibleForTesting
    protected final void d(boolean z4) {
        j.k("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.f19803c) {
                c();
            }
            Context context = this.f19806f;
            try {
                context.getPackageManager().getPackageInfo("com.android.vending", 0);
                int h8 = com.google.android.gms.common.b.f().h(context, com.google.android.gms.common.d.f11721a);
                if (h8 != 0 && h8 != 2) {
                    throw new IOException("Google Play services not available");
                }
                j6.a aVar = new j6.a();
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                if (!t6.a.b().a(context, intent, aVar, 1)) {
                    throw new IOException("Connection failure");
                }
                this.f19801a = aVar;
                try {
                    this.f19802b = e.d(aVar.a(10000L, TimeUnit.MILLISECONDS));
                    this.f19803c = true;
                    if (z4) {
                        g();
                    }
                } catch (InterruptedException unused) {
                    throw new IOException("Interrupted exception");
                } catch (Throwable th) {
                    throw new IOException(th);
                }
            } catch (PackageManager.NameNotFoundException unused2) {
                throw new GooglePlayServicesNotAvailableException(9);
            }
        }
    }

    @VisibleForTesting
    final boolean e(C0166a c0166a, boolean z4, float f5, long j8, String str, Throwable th) {
        if (Math.random() <= 0.0d) {
            HashMap hashMap = new HashMap();
            hashMap.put("app_context", "1");
            if (c0166a != null) {
                hashMap.put("limit_ad_tracking", true != c0166a.b() ? "0" : "1");
                String a9 = c0166a.a();
                if (a9 != null) {
                    hashMap.put("ad_id_size", Integer.toString(a9.length()));
                }
            }
            if (th != null) {
                hashMap.put("error", th.getClass().getName());
            }
            hashMap.put("tag", "AdvertisingIdClient");
            hashMap.put("time_spent", Long.toString(j8));
            new b(this, hashMap).start();
            return true;
        }
        return false;
    }

    protected final void finalize() {
        c();
        super.finalize();
    }
}
