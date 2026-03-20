package f7;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l {

    /* renamed from: a  reason: collision with root package name */
    private final Resources f19847a;

    /* renamed from: b  reason: collision with root package name */
    private final String f19848b;

    public l(Context context, String str) {
        n6.j.l(context);
        this.f19847a = context.getResources();
        if (TextUtils.isEmpty(str)) {
            this.f19848b = a(context);
        } else {
            this.f19848b = str;
        }
    }

    public static String a(Context context) {
        try {
            return context.getResources().getResourcePackageName(j6.c.f20797a);
        } catch (Resources.NotFoundException unused) {
            return context.getPackageName();
        }
    }

    public final String b(String str) {
        int identifier = this.f19847a.getIdentifier(str, "string", this.f19848b);
        if (identifier == 0) {
            return null;
        }
        try {
            return this.f19847a.getString(identifier);
        } catch (Resources.NotFoundException unused) {
            return null;
        }
    }
}
