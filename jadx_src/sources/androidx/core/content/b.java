package androidx.core.content;

import android.content.Context;
import android.os.Process;
import androidx.core.app.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static int a(Context context, String str, int i8, int i9, String str2) {
        if (context.checkPermission(str, i8, i9) == -1) {
            return -1;
        }
        String c9 = f.c(str);
        if (c9 == null) {
            return 0;
        }
        if (str2 == null) {
            String[] packagesForUid = context.getPackageManager().getPackagesForUid(i9);
            if (packagesForUid == null || packagesForUid.length <= 0) {
                return -1;
            }
            str2 = packagesForUid[0];
        }
        return (Process.myUid() == i9 && androidx.core.util.c.a(context.getPackageName(), str2) ? f.a(context, i9, c9, str2) : f.b(context, c9, str2)) == 0 ? 0 : -2;
    }

    public static int b(Context context, String str) {
        return a(context, str, Process.myPid(), Process.myUid(), context.getPackageName());
    }
}
