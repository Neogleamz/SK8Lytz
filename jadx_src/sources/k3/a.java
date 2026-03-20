package k3;

import android.content.Context;
import android.os.Build;
import io.flutter.plugin.common.j;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import l3.c;
import l3.e;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements yf.a, j.c {

    /* renamed from: c  reason: collision with root package name */
    public static final C0182a f20975c = new C0182a(null);

    /* renamed from: d  reason: collision with root package name */
    private static boolean f20976d;

    /* renamed from: a  reason: collision with root package name */
    private Context f20977a;

    /* renamed from: b  reason: collision with root package name */
    private j f20978b;

    /* renamed from: k3.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0182a {
        private C0182a() {
        }

        public /* synthetic */ C0182a(i iVar) {
            this();
        }

        public final boolean a() {
            return a.f20976d;
        }
    }

    public a() {
        o3.a aVar = o3.a.f22220a;
        aVar.b(new q3.a(0));
        aVar.b(new q3.a(1));
        aVar.b(new r3.a());
        aVar.b(new q3.a(3));
    }

    private final int b(io.flutter.plugin.common.i iVar) {
        f20976d = p.a((Boolean) iVar.b(), Boolean.TRUE);
        return 1;
    }

    public void onAttachedToEngine(a.b bVar) {
        p.e(bVar, "binding");
        Context a9 = bVar.a();
        p.d(a9, "getApplicationContext(...)");
        this.f20977a = a9;
        j jVar = new j(bVar.b(), "flutter_image_compress");
        this.f20978b = jVar;
        jVar.e(this);
    }

    public void onDetachedFromEngine(a.b bVar) {
        p.e(bVar, "binding");
        j jVar = this.f20978b;
        if (jVar != null) {
            jVar.e((j.c) null);
        }
        this.f20978b = null;
    }

    public void onMethodCall(io.flutter.plugin.common.i iVar, j.d dVar) {
        int i8;
        p.e(iVar, "call");
        p.e(dVar, "result");
        String str = iVar.a;
        if (str != null) {
            Context context = null;
            switch (str.hashCode()) {
                case -129880033:
                    if (str.equals("compressWithFileAndGetFile")) {
                        c cVar = new c(iVar, dVar);
                        Context context2 = this.f20977a;
                        if (context2 == null) {
                            p.t("context");
                        } else {
                            context = context2;
                        }
                        cVar.i(context);
                        return;
                    }
                    break;
                case 86054116:
                    if (str.equals("compressWithFile")) {
                        c cVar2 = new c(iVar, dVar);
                        Context context3 = this.f20977a;
                        if (context3 == null) {
                            p.t("context");
                        } else {
                            context = context3;
                        }
                        cVar2.g(context);
                        return;
                    }
                    break;
                case 86233094:
                    if (str.equals("compressWithList")) {
                        e eVar = new e(iVar, dVar);
                        Context context4 = this.f20977a;
                        if (context4 == null) {
                            p.t("context");
                        } else {
                            context = context4;
                        }
                        eVar.f(context);
                        return;
                    }
                    break;
                case 1262746611:
                    if (str.equals("getSystemVersion")) {
                        i8 = Build.VERSION.SDK_INT;
                        dVar.success(Integer.valueOf(i8));
                        return;
                    }
                    break;
                case 2067272455:
                    if (str.equals("showLog")) {
                        i8 = b(iVar);
                        dVar.success(Integer.valueOf(i8));
                        return;
                    }
                    break;
            }
        }
        dVar.b();
    }
}
