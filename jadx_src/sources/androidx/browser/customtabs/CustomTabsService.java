package androidx.browser.customtabs;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.browser.customtabs.CustomTabsService;
import b.b;
import java.util.List;
import java.util.NoSuchElementException;
import k0.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class CustomTabsService extends Service {

    /* renamed from: a  reason: collision with root package name */
    final g<IBinder, IBinder.DeathRecipient> f1664a = new g<>();

    /* renamed from: b  reason: collision with root package name */
    private b.a f1665b = new a();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends b.a {
        a() {
        }

        private PendingIntent e(Bundle bundle) {
            if (bundle == null) {
                return null;
            }
            PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("android.support.customtabs.extra.SESSION_ID");
            bundle.remove("android.support.customtabs.extra.SESSION_ID");
            return pendingIntent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(f fVar) {
            CustomTabsService.this.a(fVar);
        }

        private boolean g(b.a aVar, PendingIntent pendingIntent) {
            final f fVar = new f(aVar, pendingIntent);
            try {
                IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: androidx.browser.customtabs.e
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        CustomTabsService.a.this.f(fVar);
                    }
                };
                synchronized (CustomTabsService.this.f1664a) {
                    aVar.asBinder().linkToDeath(deathRecipient, 0);
                    CustomTabsService.this.f1664a.put(aVar.asBinder(), deathRecipient);
                }
                return CustomTabsService.this.d(fVar);
            } catch (RemoteException unused) {
                return false;
            }
        }

        @Override // b.b
        public boolean P0(b.a aVar, Uri uri) {
            return CustomTabsService.this.g(new f(aVar, null), uri);
        }

        @Override // b.b
        public Bundle c0(String str, Bundle bundle) {
            return CustomTabsService.this.b(str, bundle);
        }

        @Override // b.b
        public boolean d0(b.a aVar, Uri uri, Bundle bundle, List<Bundle> list) {
            return CustomTabsService.this.c(new f(aVar, e(bundle)), uri, bundle, list);
        }

        @Override // b.b
        public boolean g0(b.a aVar, Uri uri, int i8, Bundle bundle) {
            return CustomTabsService.this.f(new f(aVar, e(bundle)), uri, i8, bundle);
        }

        @Override // b.b
        public boolean l1(b.a aVar, int i8, Uri uri, Bundle bundle) {
            return CustomTabsService.this.j(new f(aVar, e(bundle)), i8, uri, bundle);
        }

        @Override // b.b
        public boolean m1(b.a aVar, Bundle bundle) {
            return CustomTabsService.this.i(new f(aVar, e(bundle)), bundle);
        }

        @Override // b.b
        public boolean n(b.a aVar, Uri uri, Bundle bundle) {
            return CustomTabsService.this.g(new f(aVar, e(bundle)), uri);
        }

        @Override // b.b
        public int o(b.a aVar, String str, Bundle bundle) {
            return CustomTabsService.this.e(new f(aVar, e(bundle)), str, bundle);
        }

        @Override // b.b
        public boolean p0(long j8) {
            return CustomTabsService.this.k(j8);
        }

        @Override // b.b
        public boolean s1(b.a aVar, Bundle bundle) {
            return g(aVar, e(bundle));
        }

        @Override // b.b
        public boolean y0(b.a aVar) {
            return g(aVar, null);
        }
    }

    protected boolean a(f fVar) {
        try {
            synchronized (this.f1664a) {
                IBinder a9 = fVar.a();
                if (a9 == null) {
                    return false;
                }
                a9.unlinkToDeath(this.f1664a.get(a9), 0);
                this.f1664a.remove(a9);
                return true;
            }
        } catch (NoSuchElementException unused) {
            return false;
        }
    }

    protected abstract Bundle b(String str, Bundle bundle);

    protected abstract boolean c(f fVar, Uri uri, Bundle bundle, List<Bundle> list);

    protected abstract boolean d(f fVar);

    protected abstract int e(f fVar, String str, Bundle bundle);

    protected abstract boolean f(f fVar, Uri uri, int i8, Bundle bundle);

    protected abstract boolean g(f fVar, Uri uri);

    protected abstract boolean i(f fVar, Bundle bundle);

    protected abstract boolean j(f fVar, int i8, Uri uri, Bundle bundle);

    protected abstract boolean k(long j8);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f1665b;
    }
}
