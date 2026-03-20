package e3;

import android.content.Context;
import io.flutter.plugin.common.c;
import io.flutter.plugin.common.j;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements yf.a {

    /* renamed from: a  reason: collision with root package name */
    private j f19750a;

    private void a(c cVar, Context context) {
        this.f19750a = new j(cVar, "flutter_native_image");
        this.f19750a.e(new b(context));
    }

    private void b() {
        this.f19750a.e((j.c) null);
        this.f19750a = null;
    }

    public void onAttachedToEngine(a.b bVar) {
        a(bVar.d().j(), bVar.a());
    }

    public void onDetachedFromEngine(a.b bVar) {
        b();
    }
}
