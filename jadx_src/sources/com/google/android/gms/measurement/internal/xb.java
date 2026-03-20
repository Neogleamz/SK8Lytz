package com.google.android.gms.measurement.internal;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class xb {

    /* renamed from: a  reason: collision with root package name */
    private final f6 f17142a;

    public xb(f6 f6Var) {
        this.f17142a = f6Var;
    }

    private final boolean d() {
        return this.f17142a.D().f16623z.a() > 0;
    }

    private final boolean e() {
        return d() && this.f17142a.zzb().a() - this.f17142a.D().f16623z.a() > this.f17142a.x().x(null, c0.V);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.f17142a.l().k();
        if (d()) {
            if (e()) {
                this.f17142a.D().f16622y.b(null);
                Bundle bundle = new Bundle();
                bundle.putString("source", "(not set)");
                bundle.putString("medium", "(not set)");
                bundle.putString("_cis", "intent");
                bundle.putLong("_cc", 1L);
                this.f17142a.F().A0("auto", "_cmpx", bundle);
            } else {
                String a9 = this.f17142a.D().f16622y.a();
                if (TextUtils.isEmpty(a9)) {
                    this.f17142a.i().G().a("Cache still valid but referrer not found");
                } else {
                    long a10 = ((this.f17142a.D().f16623z.a() / 3600000) - 1) * 3600000;
                    Uri parse = Uri.parse(a9);
                    Bundle bundle2 = new Bundle();
                    Pair pair = new Pair(parse.getPath(), bundle2);
                    for (String str : parse.getQueryParameterNames()) {
                        bundle2.putString(str, parse.getQueryParameter(str));
                    }
                    ((Bundle) pair.second).putLong("_cc", a10);
                    Object obj = pair.first;
                    this.f17142a.F().A0(obj == null ? "app" : (String) obj, "_cmp", (Bundle) pair.second);
                }
                this.f17142a.D().f16622y.b(null);
            }
            this.f17142a.D().f16623z.b(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(String str, Bundle bundle) {
        String str2;
        this.f17142a.l().k();
        if (this.f17142a.n()) {
            return;
        }
        if (bundle == null || bundle.isEmpty()) {
            str2 = null;
        } else {
            str = (str == null || str.isEmpty()) ? "auto" : "auto";
            Uri.Builder builder = new Uri.Builder();
            builder.path(str);
            for (String str3 : bundle.keySet()) {
                builder.appendQueryParameter(str3, bundle.getString(str3));
            }
            str2 = builder.build().toString();
        }
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        this.f17142a.D().f16622y.b(str2);
        this.f17142a.D().f16623z.b(this.f17142a.zzb().a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c() {
        if (d() && e()) {
            this.f17142a.D().f16622y.b(null);
        }
    }
}
