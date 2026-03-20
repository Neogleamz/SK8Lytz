package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    public static final int f11718a = d.f11721a;

    /* renamed from: b  reason: collision with root package name */
    private static final b f11719b = new b();

    public static b f() {
        return f11719b;
    }

    public int a(Context context) {
        return d.a(context);
    }

    public Intent b(Context context, int i8, String str) {
        if (i8 != 1 && i8 != 2) {
            if (i8 != 3) {
                return null;
            }
            Uri fromParts = Uri.fromParts("package", "com.google.android.gms", null);
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(fromParts);
            return intent;
        } else if (context != null && u6.h.d(context)) {
            Intent intent2 = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
            intent2.setPackage("com.google.android.wearable.app");
            return intent2;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("gcore_");
            sb.append(f11718a);
            sb.append("-");
            if (!TextUtils.isEmpty(str)) {
                sb.append(str);
            }
            sb.append("-");
            if (context != null) {
                sb.append(context.getPackageName());
            }
            sb.append("-");
            if (context != null) {
                try {
                    sb.append(w6.c.a(context).e(context.getPackageName(), 0).versionCode);
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            String sb2 = sb.toString();
            Intent intent3 = new Intent("android.intent.action.VIEW");
            Uri.Builder appendQueryParameter = Uri.parse("market://details").buildUpon().appendQueryParameter("id", "com.google.android.gms");
            if (!TextUtils.isEmpty(sb2)) {
                appendQueryParameter.appendQueryParameter("pcampaignid", sb2);
            }
            intent3.setData(appendQueryParameter.build());
            intent3.setPackage("com.android.vending");
            intent3.addFlags(524288);
            return intent3;
        }
    }

    public PendingIntent c(Context context, int i8, int i9) {
        return d(context, i8, i9, null);
    }

    public PendingIntent d(Context context, int i8, int i9, String str) {
        Intent b9 = b(context, i8, str);
        if (b9 == null) {
            return null;
        }
        return PendingIntent.getActivity(context, i9, b9, com.google.android.gms.internal.common.i.f12047a | 134217728);
    }

    public String e(int i8) {
        return d.b(i8);
    }

    @ResultIgnorabilityUnspecified
    public int g(Context context) {
        return h(context, f11718a);
    }

    public int h(Context context, int i8) {
        int f5 = d.f(context, i8);
        if (d.g(context, f5)) {
            return 18;
        }
        return f5;
    }

    public boolean i(Context context, String str) {
        return d.k(context, str);
    }

    public boolean j(int i8) {
        return d.i(i8);
    }
}
