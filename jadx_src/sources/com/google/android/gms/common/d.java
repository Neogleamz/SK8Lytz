package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import java.util.concurrent.atomic.AtomicBoolean;
import n6.f0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    @Deprecated

    /* renamed from: a  reason: collision with root package name */
    public static final int f11721a = 12451000;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f11723c = false;

    /* renamed from: d  reason: collision with root package name */
    static boolean f11724d = false;
    @Deprecated

    /* renamed from: b  reason: collision with root package name */
    static final AtomicBoolean f11722b = new AtomicBoolean();

    /* renamed from: e  reason: collision with root package name */
    private static final AtomicBoolean f11725e = new AtomicBoolean();

    @Deprecated
    public static int a(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 0;
        }
    }

    @Deprecated
    public static String b(int i8) {
        return ConnectionResult.I0(i8);
    }

    public static Context c(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static Resources d(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static boolean e(Context context) {
        try {
            if (!f11724d) {
                try {
                    PackageInfo e8 = w6.c.a(context).e("com.google.android.gms", 64);
                    e.a(context);
                    if (e8 == null || e.e(e8, false) || !e.e(e8, true)) {
                        f11723c = false;
                    } else {
                        f11723c = true;
                    }
                } catch (PackageManager.NameNotFoundException e9) {
                    Log.w("GooglePlayServicesUtil", "Cannot find Google Play services package name.", e9);
                }
            }
            return f11723c || !u6.h.b();
        } finally {
            f11724d = true;
        }
    }

    @Deprecated
    public static int f(Context context, int i8) {
        String valueOf;
        String str;
        PackageInfo packageInfo;
        try {
            context.getResources().getString(j6.c.f20797a);
        } catch (Throwable unused) {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!"com.google.android.gms".equals(context.getPackageName()) && !f11725e.get()) {
            int a9 = f0.a(context);
            if (a9 == 0) {
                throw new GooglePlayServicesMissingManifestValueException();
            }
            if (a9 != f11721a) {
                throw new GooglePlayServicesIncorrectManifestValueException(a9);
            }
        }
        boolean z4 = (u6.h.d(context) || u6.h.f(context)) ? false : true;
        n6.j.a(i8 >= 0);
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        if (z4) {
            try {
                packageInfo = packageManager.getPackageInfo("com.android.vending", 8256);
            } catch (PackageManager.NameNotFoundException unused2) {
                valueOf = String.valueOf(packageName);
                str = " requires the Google Play Store, but it is missing.";
            }
        } else {
            packageInfo = null;
        }
        try {
            PackageInfo packageInfo2 = packageManager.getPackageInfo("com.google.android.gms", 64);
            e.a(context);
            if (e.e(packageInfo2, true)) {
                if (z4) {
                    n6.j.l(packageInfo);
                    if (!e.e(packageInfo, true)) {
                        valueOf = String.valueOf(packageName);
                        str = " requires Google Play Store, but its signature is invalid.";
                    }
                }
                if (!z4 || packageInfo == null || packageInfo.signatures[0].equals(packageInfo2.signatures[0])) {
                    if (u6.r.a(packageInfo2.versionCode) >= u6.r.a(i8)) {
                        ApplicationInfo applicationInfo = packageInfo2.applicationInfo;
                        if (applicationInfo == null) {
                            try {
                                applicationInfo = packageManager.getApplicationInfo("com.google.android.gms", 0);
                            } catch (PackageManager.NameNotFoundException e8) {
                                Log.wtf("GooglePlayServicesUtil", String.valueOf(packageName).concat(" requires Google Play services, but they're missing when getting application info."), e8);
                                return 1;
                            }
                        }
                        return !applicationInfo.enabled ? 3 : 0;
                    }
                    int i9 = packageInfo2.versionCode;
                    Log.w("GooglePlayServicesUtil", "Google Play services out of date for " + packageName + ".  Requires " + i8 + " but found " + i9);
                    return 2;
                }
                valueOf = String.valueOf(packageName);
                str = " requires Google Play Store, but its signature doesn't match that of Google Play services.";
            } else {
                valueOf = String.valueOf(packageName);
                str = " requires Google Play services, but their signature is invalid.";
            }
            Log.w("GooglePlayServicesUtil", valueOf.concat(str));
            return 9;
        } catch (PackageManager.NameNotFoundException unused3) {
            Log.w("GooglePlayServicesUtil", String.valueOf(packageName).concat(" requires Google Play services, but they are missing."));
            return 1;
        }
    }

    @Deprecated
    public static boolean g(Context context, int i8) {
        if (i8 == 18) {
            return true;
        }
        if (i8 == 1) {
            return k(context, "com.google.android.gms");
        }
        return false;
    }

    @TargetApi(18)
    public static boolean h(Context context) {
        if (u6.m.c()) {
            Object systemService = context.getSystemService("user");
            n6.j.l(systemService);
            Bundle applicationRestrictions = ((UserManager) systemService).getApplicationRestrictions(context.getPackageName());
            return applicationRestrictions != null && "true".equals(applicationRestrictions.getString("restricted_profile"));
        }
        return false;
    }

    @Deprecated
    public static boolean i(int i8) {
        return i8 == 1 || i8 == 2 || i8 == 3 || i8 == 9;
    }

    @TargetApi(19)
    @Deprecated
    public static boolean j(Context context, int i8, String str) {
        return u6.p.b(context, i8, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(21)
    public static boolean k(Context context, String str) {
        ApplicationInfo applicationInfo;
        boolean equals = str.equals("com.google.android.gms");
        if (u6.m.f()) {
            try {
                for (PackageInstaller.SessionInfo sessionInfo : context.getPackageManager().getPackageInstaller().getAllSessions()) {
                    if (str.equals(sessionInfo.getAppPackageName())) {
                        return true;
                    }
                }
            } catch (Exception unused) {
                return false;
            }
        }
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 8192);
        } catch (PackageManager.NameNotFoundException unused2) {
        }
        return equals ? applicationInfo.enabled : applicationInfo.enabled && !h(context);
    }
}
