package u6;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.google.android.apps.common.proguard.SideEffectFree;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private static Boolean f23069a;

    /* renamed from: b  reason: collision with root package name */
    private static Boolean f23070b;

    /* renamed from: c  reason: collision with root package name */
    private static Boolean f23071c;

    /* renamed from: d  reason: collision with root package name */
    private static Boolean f23072d;

    public static boolean a(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (f23072d == null) {
            boolean z4 = false;
            if (m.h() && packageManager.hasSystemFeature("android.hardware.type.automotive")) {
                z4 = true;
            }
            f23072d = Boolean.valueOf(z4);
        }
        return f23072d.booleanValue();
    }

    public static boolean b() {
        int i8 = com.google.android.gms.common.d.f11721a;
        return "user".equals(Build.TYPE);
    }

    @SideEffectFree
    @TargetApi(20)
    public static boolean c(Context context) {
        return g(context.getPackageManager());
    }

    @TargetApi(26)
    public static boolean d(Context context) {
        if (!c(context) || m.g()) {
            if (e(context)) {
                return !m.h() || m.k();
            }
            return false;
        }
        return true;
    }

    @TargetApi(21)
    public static boolean e(Context context) {
        if (f23070b == null) {
            boolean z4 = false;
            if (m.f() && context.getPackageManager().hasSystemFeature("cn.google")) {
                z4 = true;
            }
            f23070b = Boolean.valueOf(z4);
        }
        return f23070b.booleanValue();
    }

    public static boolean f(Context context) {
        if (f23071c == null) {
            boolean z4 = true;
            if (!context.getPackageManager().hasSystemFeature("android.hardware.type.iot") && !context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                z4 = false;
            }
            f23071c = Boolean.valueOf(z4);
        }
        return f23071c.booleanValue();
    }

    @SideEffectFree
    @TargetApi(20)
    public static boolean g(PackageManager packageManager) {
        if (f23069a == null) {
            boolean z4 = false;
            if (m.e() && packageManager.hasSystemFeature("android.hardware.type.watch")) {
                z4 = true;
            }
            f23069a = Boolean.valueOf(z4);
        }
        return f23069a.booleanValue();
    }
}
