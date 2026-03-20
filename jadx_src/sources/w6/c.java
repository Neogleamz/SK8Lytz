package w6;

import android.content.Context;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: b  reason: collision with root package name */
    private static final c f23672b = new c();

    /* renamed from: a  reason: collision with root package name */
    private b f23673a = null;

    public static b a(Context context) {
        return f23672b.b(context);
    }

    public final synchronized b b(Context context) {
        if (this.f23673a == null) {
            if (context.getApplicationContext() != null) {
                context = context.getApplicationContext();
            }
            this.f23673a = new b(context);
        }
        return this.f23673a;
    }
}
