package n6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k0 {

    /* renamed from: f  reason: collision with root package name */
    private static final Uri f22191f = new Uri.Builder().scheme("content").authority("com.google.android.gms.chimera").build();

    /* renamed from: a  reason: collision with root package name */
    private final String f22192a;

    /* renamed from: b  reason: collision with root package name */
    private final String f22193b;

    /* renamed from: c  reason: collision with root package name */
    private final ComponentName f22194c;

    /* renamed from: d  reason: collision with root package name */
    private final int f22195d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f22196e;

    public k0(String str, String str2, int i8, boolean z4) {
        j.f(str);
        this.f22192a = str;
        j.f(str2);
        this.f22193b = str2;
        this.f22194c = null;
        this.f22195d = 4225;
        this.f22196e = z4;
    }

    public final ComponentName a() {
        return this.f22194c;
    }

    public final Intent b(Context context) {
        Bundle bundle;
        if (this.f22192a != null) {
            if (this.f22196e) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("serviceActionBundleKey", this.f22192a);
                try {
                    bundle = context.getContentResolver().call(f22191f, "serviceIntentCall", (String) null, bundle2);
                } catch (IllegalArgumentException e8) {
                    Log.w("ConnectionStatusConfig", "Dynamic intent resolution failed: ".concat(e8.toString()));
                    bundle = null;
                }
                r2 = bundle != null ? (Intent) bundle.getParcelable("serviceResponseIntentKey") : null;
                if (r2 == null) {
                    Log.w("ConnectionStatusConfig", "Dynamic lookup for intent failed for action: ".concat(String.valueOf(this.f22192a)));
                }
            }
            return r2 == null ? new Intent(this.f22192a).setPackage(this.f22193b) : r2;
        }
        return new Intent().setComponent(this.f22194c);
    }

    public final String c() {
        return this.f22193b;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof k0) {
            k0 k0Var = (k0) obj;
            return i.a(this.f22192a, k0Var.f22192a) && i.a(this.f22193b, k0Var.f22193b) && i.a(this.f22194c, k0Var.f22194c) && this.f22196e == k0Var.f22196e;
        }
        return false;
    }

    public final int hashCode() {
        return i.b(this.f22192a, this.f22193b, this.f22194c, 4225, Boolean.valueOf(this.f22196e));
    }

    public final String toString() {
        String str = this.f22192a;
        if (str == null) {
            j.l(this.f22194c);
            return this.f22194c.flattenToString();
        }
        return str;
    }
}
