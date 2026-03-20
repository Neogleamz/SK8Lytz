package androidx.browser.customtabs;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.browser.customtabs.a;
import androidx.core.app.g;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    public final Intent f1677a;

    /* renamed from: b  reason: collision with root package name */
    public final Bundle f1678b;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static String a() {
            LocaleList adjustedDefault = LocaleList.getAdjustedDefault();
            if (adjustedDefault.size() > 0) {
                return adjustedDefault.get(0).toLanguageTag();
            }
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: c  reason: collision with root package name */
        private ArrayList<Bundle> f1681c;

        /* renamed from: d  reason: collision with root package name */
        private Bundle f1682d;

        /* renamed from: e  reason: collision with root package name */
        private ArrayList<Bundle> f1683e;

        /* renamed from: f  reason: collision with root package name */
        private SparseArray<Bundle> f1684f;

        /* renamed from: g  reason: collision with root package name */
        private Bundle f1685g;

        /* renamed from: a  reason: collision with root package name */
        private final Intent f1679a = new Intent("android.intent.action.VIEW");

        /* renamed from: b  reason: collision with root package name */
        private final a.C0014a f1680b = new a.C0014a();

        /* renamed from: h  reason: collision with root package name */
        private int f1686h = 0;

        /* renamed from: i  reason: collision with root package name */
        private boolean f1687i = true;

        private void b() {
            String a9 = a.a();
            if (TextUtils.isEmpty(a9)) {
                return;
            }
            Bundle bundleExtra = this.f1679a.hasExtra("com.android.browser.headers") ? this.f1679a.getBundleExtra("com.android.browser.headers") : new Bundle();
            if (bundleExtra.containsKey("Accept-Language")) {
                return;
            }
            bundleExtra.putString("Accept-Language", a9);
            this.f1679a.putExtra("com.android.browser.headers", bundleExtra);
        }

        private void c(IBinder iBinder, PendingIntent pendingIntent) {
            Bundle bundle = new Bundle();
            g.b(bundle, "android.support.customtabs.extra.SESSION", iBinder);
            if (pendingIntent != null) {
                bundle.putParcelable("android.support.customtabs.extra.SESSION_ID", pendingIntent);
            }
            this.f1679a.putExtras(bundle);
        }

        public d a() {
            if (!this.f1679a.hasExtra("android.support.customtabs.extra.SESSION")) {
                c(null, null);
            }
            ArrayList<Bundle> arrayList = this.f1681c;
            if (arrayList != null) {
                this.f1679a.putParcelableArrayListExtra("android.support.customtabs.extra.MENU_ITEMS", arrayList);
            }
            ArrayList<Bundle> arrayList2 = this.f1683e;
            if (arrayList2 != null) {
                this.f1679a.putParcelableArrayListExtra("android.support.customtabs.extra.TOOLBAR_ITEMS", arrayList2);
            }
            this.f1679a.putExtra("android.support.customtabs.extra.EXTRA_ENABLE_INSTANT_APPS", this.f1687i);
            this.f1679a.putExtras(this.f1680b.a().a());
            Bundle bundle = this.f1685g;
            if (bundle != null) {
                this.f1679a.putExtras(bundle);
            }
            if (this.f1684f != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putSparseParcelableArray("androidx.browser.customtabs.extra.COLOR_SCHEME_PARAMS", this.f1684f);
                this.f1679a.putExtras(bundle2);
            }
            this.f1679a.putExtra("androidx.browser.customtabs.extra.SHARE_STATE", this.f1686h);
            if (Build.VERSION.SDK_INT >= 24) {
                b();
            }
            return new d(this.f1679a, this.f1682d);
        }

        public b d(boolean z4) {
            this.f1679a.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", z4 ? 1 : 0);
            return this;
        }
    }

    d(Intent intent, Bundle bundle) {
        this.f1677a = intent;
        this.f1678b = bundle;
    }

    public void a(Context context, Uri uri) {
        this.f1677a.setData(uri);
        androidx.core.content.a.l(context, this.f1677a, this.f1678b);
    }
}
