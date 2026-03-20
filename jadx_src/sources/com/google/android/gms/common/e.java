package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;
import com.google.errorprone.annotations.RestrictedInheritance;
@RestrictedInheritance(allowedOnPath = ".*java.*/com/google/android/gms/common/testing/.*", explanation = "Sub classing of GMS Core's APIs are restricted to testing fakes.", link = "go/gmscore-restrictedinheritance")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: c  reason: collision with root package name */
    private static e f11746c;

    /* renamed from: a  reason: collision with root package name */
    private final Context f11747a;

    /* renamed from: b  reason: collision with root package name */
    private volatile String f11748b;

    public e(Context context) {
        this.f11747a = context.getApplicationContext();
    }

    public static e a(Context context) {
        n6.j.l(context);
        synchronized (e.class) {
            if (f11746c == null) {
                r.d(context);
                f11746c = new e(context);
            }
        }
        return f11746c;
    }

    static final n d(PackageInfo packageInfo, n... nVarArr) {
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr != null) {
            if (signatureArr.length != 1) {
                Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
                return null;
            }
            o oVar = new o(packageInfo.signatures[0].toByteArray());
            for (int i8 = 0; i8 < nVarArr.length; i8++) {
                if (nVarArr[i8].equals(oVar)) {
                    return nVarArr[i8];
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0048 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final boolean e(android.content.pm.PackageInfo r4, boolean r5) {
        /*
            r0 = 1
            r1 = 0
            if (r5 == 0) goto L2a
            if (r4 == 0) goto L28
            java.lang.String r2 = r4.packageName
            java.lang.String r3 = "com.android.vending"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L1a
            java.lang.String r2 = r4.packageName
            java.lang.String r3 = "com.google.android.gms"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L2a
        L1a:
            android.content.pm.ApplicationInfo r5 = r4.applicationInfo
            if (r5 != 0) goto L20
        L1e:
            r5 = r1
            goto L2a
        L20:
            int r5 = r5.flags
            r5 = r5 & 129(0x81, float:1.81E-43)
            if (r5 == 0) goto L1e
            r5 = r0
            goto L2a
        L28:
            r2 = 0
            goto L2b
        L2a:
            r2 = r4
        L2b:
            if (r4 == 0) goto L49
            android.content.pm.Signature[] r4 = r2.signatures
            if (r4 == 0) goto L49
            if (r5 == 0) goto L3a
            com.google.android.gms.common.n[] r4 = com.google.android.gms.common.q.f11933a
            com.google.android.gms.common.n r4 = d(r2, r4)
            goto L46
        L3a:
            com.google.android.gms.common.n[] r4 = new com.google.android.gms.common.n[r0]
            com.google.android.gms.common.n[] r5 = com.google.android.gms.common.q.f11933a
            r5 = r5[r1]
            r4[r1] = r5
            com.google.android.gms.common.n r4 = d(r2, r4)
        L46:
            if (r4 == 0) goto L49
            return r0
        L49:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.e.e(android.content.pm.PackageInfo, boolean):boolean");
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    private final w f(String str, boolean z4, boolean z8) {
        w wVar;
        ApplicationInfo applicationInfo;
        String str2 = "null pkg";
        if (str == null) {
            return w.c("null pkg");
        }
        if (str.equals(this.f11748b)) {
            return w.b();
        }
        if (r.e()) {
            wVar = r.b(str, d.e(this.f11747a), false, false);
        } else {
            try {
                PackageInfo packageInfo = this.f11747a.getPackageManager().getPackageInfo(str, 64);
                boolean e8 = d.e(this.f11747a);
                if (packageInfo != null) {
                    Signature[] signatureArr = packageInfo.signatures;
                    if (signatureArr == null || signatureArr.length != 1) {
                        str2 = "single cert required";
                    } else {
                        o oVar = new o(packageInfo.signatures[0].toByteArray());
                        String str3 = packageInfo.packageName;
                        w a9 = r.a(str3, oVar, e8, false);
                        if (!a9.f11995a || (applicationInfo = packageInfo.applicationInfo) == null || (applicationInfo.flags & 2) == 0 || !r.a(str3, oVar, false, true).f11995a) {
                            wVar = a9;
                        } else {
                            str2 = "debuggable release cert app rejected";
                        }
                    }
                }
                wVar = w.c(str2);
            } catch (PackageManager.NameNotFoundException e9) {
                return w.d("no pkg ".concat(str), e9);
            }
        }
        if (wVar.f11995a) {
            this.f11748b = str;
        }
        return wVar;
    }

    public boolean b(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (e(packageInfo, false)) {
            return true;
        }
        if (e(packageInfo, true)) {
            if (d.e(this.f11747a)) {
                return true;
            }
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return false;
    }

    public boolean c(int i8) {
        w c9;
        int length;
        String[] packagesForUid = this.f11747a.getPackageManager().getPackagesForUid(i8);
        if (packagesForUid != null && (length = packagesForUid.length) != 0) {
            c9 = null;
            int i9 = 0;
            while (true) {
                if (i9 >= length) {
                    n6.j.l(c9);
                    break;
                }
                c9 = f(packagesForUid[i9], false, false);
                if (c9.f11995a) {
                    break;
                }
                i9++;
            }
        } else {
            c9 = w.c("no pkgs");
        }
        c9.e();
        return c9.f11995a;
    }
}
