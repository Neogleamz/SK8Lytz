package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.net.Uri;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o6 {

    /* renamed from: a  reason: collision with root package name */
    private static final k0.a<String, Uri> f12400a = new k0.a<>();

    public static synchronized Uri a(String str) {
        Uri uri;
        synchronized (o6.class) {
            k0.a<String, Uri> aVar = f12400a;
            uri = aVar.get(str);
            if (uri == null) {
                String encode = Uri.encode(str);
                uri = Uri.parse("content://com.google.android.gms.phenotype/" + encode);
                aVar.put(str, uri);
            }
        }
        return uri;
    }

    public static String b(Context context, String str) {
        if (str.contains("#")) {
            throw new IllegalArgumentException("The passed in package cannot already have a subpackage: " + str);
        }
        String packageName = context.getPackageName();
        return str + "#" + BuildConfig.FLAVOR + packageName;
    }

    public static boolean c(String str, String str2) {
        if (str.equals("eng") || str.equals("userdebug")) {
            return str2.contains("dev-keys") || str2.contains("test-keys");
        }
        return false;
    }
}
