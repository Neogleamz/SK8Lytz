package w6;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.Process;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import u6.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    protected final Context f23671a;

    public b(Context context) {
        this.f23671a = context;
    }

    public int a(String str) {
        return this.f23671a.checkCallingOrSelfPermission(str);
    }

    @ResultIgnorabilityUnspecified
    public int b(String str, String str2) {
        return this.f23671a.getPackageManager().checkPermission(str, str2);
    }

    @ResultIgnorabilityUnspecified
    public ApplicationInfo c(String str, int i8) {
        return this.f23671a.getPackageManager().getApplicationInfo(str, i8);
    }

    public CharSequence d(String str) {
        Context context = this.f23671a;
        return context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(str, 0));
    }

    @ResultIgnorabilityUnspecified
    public PackageInfo e(String str, int i8) {
        return this.f23671a.getPackageManager().getPackageInfo(str, i8);
    }

    public boolean f() {
        String nameForUid;
        if (Binder.getCallingUid() == Process.myUid()) {
            return a.a(this.f23671a);
        }
        if (!m.h() || (nameForUid = this.f23671a.getPackageManager().getNameForUid(Binder.getCallingUid())) == null) {
            return false;
        }
        return this.f23671a.getPackageManager().isInstantApp(nameForUid);
    }

    @TargetApi(19)
    public final boolean g(int i8, String str) {
        if (m.d()) {
            try {
                AppOpsManager appOpsManager = (AppOpsManager) this.f23671a.getSystemService("appops");
                if (appOpsManager != null) {
                    appOpsManager.checkPackage(i8, str);
                    return true;
                }
                throw new NullPointerException("context.getSystemService(Context.APP_OPS_SERVICE) is null");
            } catch (SecurityException unused) {
                return false;
            }
        }
        String[] packagesForUid = this.f23671a.getPackageManager().getPackagesForUid(i8);
        if (str != null && packagesForUid != null) {
            for (String str2 : packagesForUid) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
