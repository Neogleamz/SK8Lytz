package i6;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import java.util.List;
import u6.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: a  reason: collision with root package name */
    private final Context f20544a;

    /* renamed from: b  reason: collision with root package name */
    private int f20545b;

    /* renamed from: c  reason: collision with root package name */
    private int f20546c = 0;

    public g(Context context) {
        this.f20544a = context;
    }

    public final synchronized int a() {
        PackageInfo packageInfo;
        if (this.f20545b == 0) {
            try {
                packageInfo = w6.c.a(this.f20544a).e("com.google.android.gms", 0);
            } catch (PackageManager.NameNotFoundException e8) {
                Log.w("Metadata", "Failed to find package ".concat(e8.toString()));
                packageInfo = null;
            }
            if (packageInfo != null) {
                this.f20545b = packageInfo.versionCode;
            }
        }
        return this.f20545b;
    }

    public final synchronized int b() {
        int i8 = this.f20546c;
        if (i8 != 0) {
            return i8;
        }
        Context context = this.f20544a;
        PackageManager packageManager = context.getPackageManager();
        if (w6.c.a(context).b("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
            Log.e("Metadata", "Google Play services missing or without correct permission.");
            return 0;
        }
        if (!m.h()) {
            Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
            intent.setPackage("com.google.android.gms");
            List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
                r2 = 1;
                this.f20546c = r2;
                return r2;
            }
        }
        Intent intent2 = new Intent("com.google.iid.TOKEN_REQUEST");
        intent2.setPackage("com.google.android.gms");
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent2, 0);
        if (queryBroadcastReceivers == null || queryBroadcastReceivers.isEmpty()) {
            Log.w("Metadata", "Failed to resolve IID implementation package, falling back");
            r2 = true != m.h() ? 1 : 2;
            this.f20546c = r2;
            return r2;
        }
        this.f20546c = r2;
        return r2;
    }
}
