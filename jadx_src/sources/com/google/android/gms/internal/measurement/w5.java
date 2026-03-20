package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w5 {

    /* renamed from: a  reason: collision with root package name */
    private static UserManager f12633a;

    /* renamed from: b  reason: collision with root package name */
    private static volatile boolean f12634b = !a();

    /* renamed from: c  reason: collision with root package name */
    private static boolean f12635c = false;

    private w5() {
    }

    public static boolean a() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean b(Context context) {
        return a() && !d(context);
    }

    public static boolean c(Context context) {
        return !a() || d(context);
    }

    @TargetApi(24)
    private static boolean d(Context context) {
        if (f12634b) {
            return true;
        }
        synchronized (w5.class) {
            if (f12634b) {
                return true;
            }
            boolean e8 = e(context);
            if (e8) {
                f12634b = e8;
            }
            return e8;
        }
    }

    @TargetApi(24)
    private static boolean e(Context context) {
        boolean z4;
        boolean z8 = true;
        int i8 = 1;
        while (true) {
            z4 = false;
            if (i8 > 2) {
                break;
            }
            if (f12633a == null) {
                f12633a = (UserManager) context.getSystemService(UserManager.class);
            }
            UserManager userManager = f12633a;
            if (userManager == null) {
                return true;
            }
            try {
                if (userManager.isUserUnlocked()) {
                    break;
                } else if (userManager.isUserRunning(Process.myUserHandle())) {
                    z8 = false;
                }
            } catch (NullPointerException e8) {
                Log.w("DirectBootUtils", "Failed to check if user is unlocked.", e8);
                f12633a = null;
                i8++;
            }
        }
        z4 = z8;
        if (z4) {
            f12633a = null;
        }
        return z4;
    }
}
