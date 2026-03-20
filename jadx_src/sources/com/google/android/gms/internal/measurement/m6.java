package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.common.base.Optional;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m6 {

    /* renamed from: a  reason: collision with root package name */
    private static volatile Optional<Boolean> f12342a = Optional.a();

    /* renamed from: b  reason: collision with root package name */
    private static final Object f12343b = new Object();

    private static boolean a(Context context) {
        return (context.getPackageManager().getApplicationInfo("com.google.android.gms", 0).flags & 129) != 0;
    }

    public static boolean b(Context context, Uri uri) {
        boolean z4;
        String authority = uri.getAuthority();
        boolean z8 = false;
        if (!"com.google.android.gms.phenotype".equals(authority)) {
            Log.e("PhenotypeClientHelper", authority + " is an unsupported authority. Only com.google.android.gms.phenotype authority is supported.");
            return false;
        }
        if (!f12342a.c()) {
            synchronized (f12343b) {
                if (f12342a.c()) {
                    return f12342a.b().booleanValue();
                }
                if (!"com.google.android.gms".equals(context.getPackageName())) {
                    ProviderInfo resolveContentProvider = context.getPackageManager().resolveContentProvider("com.google.android.gms.phenotype", Build.VERSION.SDK_INT < 29 ? 0 : 268435456);
                    if (resolveContentProvider == null || !"com.google.android.gms".equals(resolveContentProvider.packageName)) {
                        z4 = false;
                        if (z4 && a(context)) {
                            z8 = true;
                        }
                        f12342a = Optional.d(Boolean.valueOf(z8));
                    }
                }
                z4 = true;
                if (z4) {
                    z8 = true;
                }
                f12342a = Optional.d(Boolean.valueOf(z8));
            }
        }
        return f12342a.b().booleanValue();
    }
}
