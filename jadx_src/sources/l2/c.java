package l2;

import android.content.Context;
import android.os.Vibrator;
import io.flutter.plugin.common.j;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements yf.a {

    /* renamed from: a  reason: collision with root package name */
    private j f21564a;

    private void a(io.flutter.plugin.common.c cVar, Context context) {
        b bVar = new b(new a((Vibrator) context.getSystemService("vibrator")));
        j jVar = new j(cVar, "vibration");
        this.f21564a = jVar;
        jVar.e(bVar);
    }

    private void b() {
        this.f21564a.e((j.c) null);
        this.f21564a = null;
    }

    public void onAttachedToEngine(a.b bVar) {
        a(bVar.b(), bVar.a());
    }

    public void onDetachedFromEngine(a.b bVar) {
        b();
    }
}
