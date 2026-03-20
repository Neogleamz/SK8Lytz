package k2;

import android.app.Activity;
import android.content.Context;
import io.flutter.plugin.common.j;
import io.flutter.plugin.common.l;
import yf.a;
import zf.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m implements yf.a, zf.a {

    /* renamed from: a  reason: collision with root package name */
    private final n f20966a = new n();

    /* renamed from: b  reason: collision with root package name */
    private j f20967b;

    /* renamed from: c  reason: collision with root package name */
    private l.c f20968c;

    /* renamed from: d  reason: collision with root package name */
    private c f20969d;

    /* renamed from: e  reason: collision with root package name */
    private l f20970e;

    private void a() {
        c cVar = this.f20969d;
        if (cVar != null) {
            cVar.d(this.f20966a);
            this.f20969d.e(this.f20966a);
        }
    }

    private void b() {
        l.c cVar = this.f20968c;
        if (cVar != null) {
            cVar.a(this.f20966a);
            this.f20968c.b(this.f20966a);
            return;
        }
        c cVar2 = this.f20969d;
        if (cVar2 != null) {
            cVar2.a(this.f20966a);
            this.f20969d.b(this.f20966a);
        }
    }

    private void c(Context context, io.flutter.plugin.common.c cVar) {
        this.f20967b = new j(cVar, "flutter.baseflow.com/permissions/methods");
        l lVar = new l(context, new a(), this.f20966a, new p());
        this.f20970e = lVar;
        this.f20967b.e(lVar);
    }

    private void d(Activity activity) {
        l lVar = this.f20970e;
        if (lVar != null) {
            lVar.i(activity);
        }
    }

    private void e() {
        this.f20967b.e((j.c) null);
        this.f20967b = null;
        this.f20970e = null;
    }

    private void f() {
        l lVar = this.f20970e;
        if (lVar != null) {
            lVar.i(null);
        }
    }

    public void onAttachedToActivity(c cVar) {
        d(cVar.getActivity());
        this.f20969d = cVar;
        b();
    }

    public void onAttachedToEngine(a.b bVar) {
        c(bVar.a(), bVar.b());
    }

    public void onDetachedFromActivity() {
        f();
        a();
    }

    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    public void onDetachedFromEngine(a.b bVar) {
        e();
    }

    public void onReattachedToActivityForConfigChanges(c cVar) {
        onAttachedToActivity(cVar);
    }
}
