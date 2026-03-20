package n6;

import android.content.Context;
import android.content.res.Resources;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final Resources f22197a;

    /* renamed from: b  reason: collision with root package name */
    private final String f22198b;

    public l(Context context) {
        j.l(context);
        Resources resources = context.getResources();
        this.f22197a = resources;
        this.f22198b = resources.getResourcePackageName(j6.c.f20797a);
    }

    public String a(String str) {
        int identifier = this.f22197a.getIdentifier(str, "string", this.f22198b);
        if (identifier == 0) {
            return null;
        }
        return this.f22197a.getString(identifier);
    }
}
