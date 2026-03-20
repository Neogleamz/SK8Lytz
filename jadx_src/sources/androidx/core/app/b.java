package androidx.core.app;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends androidx.core.content.a {

    /* renamed from: d  reason: collision with root package name */
    private static f f4450d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String[] f4451a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Activity f4452b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f4453c;

        a(String[] strArr, Activity activity, int i8) {
            this.f4451a = strArr;
            this.f4452b = activity;
            this.f4453c = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            int[] iArr = new int[this.f4451a.length];
            PackageManager packageManager = this.f4452b.getPackageManager();
            String packageName = this.f4452b.getPackageName();
            int length = this.f4451a.length;
            for (int i8 = 0; i8 < length; i8++) {
                iArr[i8] = packageManager.checkPermission(this.f4451a[i8], packageName);
            }
            ((e) this.f4452b).onRequestPermissionsResult(this.f4453c, this.f4451a, iArr);
        }
    }

    /* renamed from: androidx.core.app.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0028b {
        static void a(Activity activity) {
            activity.finishAffinity();
        }

        static void b(Activity activity, Intent intent, int i8, Bundle bundle) {
            activity.startActivityForResult(intent, i8, bundle);
        }

        static void c(Activity activity, IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
            activity.startIntentSenderForResult(intentSender, i8, intent, i9, i10, i11, bundle);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c {
        static void a(Activity activity) {
            activity.finishAfterTransition();
        }

        static void b(Activity activity) {
            activity.postponeEnterTransition();
        }

        static void c(Activity activity, SharedElementCallback sharedElementCallback) {
            activity.setEnterSharedElementCallback(sharedElementCallback);
        }

        static void d(Activity activity, SharedElementCallback sharedElementCallback) {
            activity.setExitSharedElementCallback(sharedElementCallback);
        }

        static void e(Activity activity) {
            activity.startPostponedEnterTransition();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d {
        static void a(Object obj) {
            ((SharedElementCallback.OnSharedElementsReadyListener) obj).onSharedElementsReady();
        }

        static void b(Activity activity, String[] strArr, int i8) {
            activity.requestPermissions(strArr, i8);
        }

        static boolean c(Activity activity, String str) {
            return activity.shouldShowRequestPermissionRationale(str);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void onRequestPermissionsResult(int i8, String[] strArr, int[] iArr);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        boolean a(Activity activity, String[] strArr, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void validateRequestPermissionsRequestCode(int i8);
    }

    public static void o(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            C0028b.a(activity);
        } else {
            activity.finish();
        }
    }

    public static void p(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.a(activity);
        } else {
            activity.finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void q(Activity activity) {
        if (activity.isFinishing() || androidx.core.app.d.i(activity)) {
            return;
        }
        activity.recreate();
    }

    public static void r(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.b(activity);
        }
    }

    public static void s(final Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
        } else {
            new Handler(activity.getMainLooper()).post(new Runnable() { // from class: androidx.core.app.a
                @Override // java.lang.Runnable
                public final void run() {
                    b.q(activity);
                }
            });
        }
    }

    public static void t(Activity activity, String[] strArr, int i8) {
        f fVar = f4450d;
        if (fVar == null || !fVar.a(activity, strArr, i8)) {
            for (String str : strArr) {
                if (TextUtils.isEmpty(str)) {
                    throw new IllegalArgumentException("Permission request for permissions " + Arrays.toString(strArr) + " must not contain null or empty values");
                }
            }
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity instanceof g) {
                    ((g) activity).validateRequestPermissionsRequestCode(i8);
                }
                d.b(activity, strArr, i8);
            } else if (activity instanceof e) {
                new Handler(Looper.getMainLooper()).post(new a(strArr, activity, i8));
            }
        }
    }

    public static void u(Activity activity, r rVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.c(activity, null);
        }
    }

    public static void v(Activity activity, r rVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.d(activity, null);
        }
    }

    public static boolean w(Activity activity, String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return d.c(activity, str);
        }
        return false;
    }

    public static void x(Activity activity, Intent intent, int i8, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            C0028b.b(activity, intent, i8, bundle);
        } else {
            activity.startActivityForResult(intent, i8);
        }
    }

    public static void y(Activity activity, IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            C0028b.c(activity, intentSender, i8, intent, i9, i10, i11, bundle);
        } else {
            activity.startIntentSenderForResult(intentSender, i8, intent, i9, i10, i11);
        }
    }

    public static void z(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            c.e(activity);
        }
    }
}
