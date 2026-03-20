package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static Intent a(Activity activity) {
            return activity.getParentActivityIntent();
        }

        static boolean b(Activity activity, Intent intent) {
            return activity.navigateUpTo(intent);
        }

        static boolean c(Activity activity, Intent intent) {
            return activity.shouldUpRecreateTask(intent);
        }
    }

    public static Intent a(Activity activity) {
        Intent a9;
        if (Build.VERSION.SDK_INT < 16 || (a9 = a.a(activity)) == null) {
            String c9 = c(activity);
            if (c9 == null) {
                return null;
            }
            ComponentName componentName = new ComponentName(activity, c9);
            try {
                return d(activity, componentName) == null ? Intent.makeMainActivity(componentName) : new Intent().setComponent(componentName);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("NavUtils", "getParentActivityIntent: bad parentActivityName '" + c9 + "' in manifest");
                return null;
            }
        }
        return a9;
    }

    public static Intent b(Context context, ComponentName componentName) {
        String d8 = d(context, componentName);
        if (d8 == null) {
            return null;
        }
        ComponentName componentName2 = new ComponentName(componentName.getPackageName(), d8);
        return d(context, componentName2) == null ? Intent.makeMainActivity(componentName2) : new Intent().setComponent(componentName2);
    }

    public static String c(Activity activity) {
        try {
            return d(activity, activity.getComponentName());
        } catch (PackageManager.NameNotFoundException e8) {
            throw new IllegalArgumentException(e8);
        }
    }

    public static String d(Context context, ComponentName componentName) {
        String string;
        String str;
        PackageManager packageManager = context.getPackageManager();
        int i8 = Build.VERSION.SDK_INT;
        int i9 = 640;
        if (i8 >= 29) {
            i9 = 269222528;
        } else if (i8 >= 24) {
            i9 = 787072;
        }
        ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, i9);
        if (i8 < 16 || (str = activityInfo.parentActivityName) == null) {
            Bundle bundle = activityInfo.metaData;
            if (bundle == null || (string = bundle.getString("android.support.PARENT_ACTIVITY")) == null) {
                return null;
            }
            if (string.charAt(0) == '.') {
                return context.getPackageName() + string;
            }
            return string;
        }
        return str;
    }

    public static void e(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            a.b(activity, intent);
            return;
        }
        intent.addFlags(67108864);
        activity.startActivity(intent);
        activity.finish();
    }

    public static boolean f(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            return a.c(activity, intent);
        }
        String action = activity.getIntent().getAction();
        return (action == null || action.equals("android.intent.action.MAIN")) ? false : true;
    }
}
