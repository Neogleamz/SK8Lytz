package androidx.media;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.media.d;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements d.a {

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f6092c = d.f6085b;

    /* renamed from: a  reason: collision with root package name */
    Context f6093a;

    /* renamed from: b  reason: collision with root package name */
    ContentResolver f6094b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements d.c {

        /* renamed from: a  reason: collision with root package name */
        private String f6095a;

        /* renamed from: b  reason: collision with root package name */
        private int f6096b;

        /* renamed from: c  reason: collision with root package name */
        private int f6097c;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(String str, int i8, int i9) {
            this.f6095a = str;
            this.f6096b = i8;
            this.f6097c = i9;
        }

        @Override // androidx.media.d.c
        public int a() {
            return this.f6097c;
        }

        @Override // androidx.media.d.c
        public int b() {
            return this.f6096b;
        }

        @Override // androidx.media.d.c
        public String c() {
            return this.f6095a;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                return (this.f6096b < 0 || aVar.f6096b < 0) ? TextUtils.equals(this.f6095a, aVar.f6095a) && this.f6097c == aVar.f6097c : TextUtils.equals(this.f6095a, aVar.f6095a) && this.f6096b == aVar.f6096b && this.f6097c == aVar.f6097c;
            }
            return false;
        }

        public int hashCode() {
            return androidx.core.util.c.b(this.f6095a, Integer.valueOf(this.f6097c));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Context context) {
        this.f6093a = context;
        this.f6094b = context.getContentResolver();
    }

    private boolean d(d.c cVar, String str) {
        return cVar.b() < 0 ? this.f6093a.getPackageManager().checkPermission(str, cVar.c()) == 0 : this.f6093a.checkPermission(str, cVar.b(), cVar.a()) == 0;
    }

    @Override // androidx.media.d.a
    public boolean a(d.c cVar) {
        try {
            if (this.f6093a.getPackageManager().getApplicationInfo(cVar.c(), 0) == null) {
                return false;
            }
            return d(cVar, "android.permission.STATUS_BAR_SERVICE") || d(cVar, "android.permission.MEDIA_CONTENT_CONTROL") || cVar.a() == 1000 || c(cVar);
        } catch (PackageManager.NameNotFoundException unused) {
            if (f6092c) {
                Log.d("MediaSessionManager", "Package " + cVar.c() + " doesn't exist");
            }
            return false;
        }
    }

    public Context b() {
        return this.f6093a;
    }

    boolean c(d.c cVar) {
        String string = Settings.Secure.getString(this.f6094b, "enabled_notification_listeners");
        if (string != null) {
            for (String str : string.split(":")) {
                ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                if (unflattenFromString != null && unflattenFromString.getPackageName().equals(cVar.c())) {
                    return true;
                }
            }
        }
        return false;
    }
}
