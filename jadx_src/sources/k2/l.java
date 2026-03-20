package k2;

import android.app.Activity;
import android.content.Context;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l implements j.c {

    /* renamed from: a  reason: collision with root package name */
    private final Context f20961a;

    /* renamed from: b  reason: collision with root package name */
    private final a f20962b;

    /* renamed from: c  reason: collision with root package name */
    private final n f20963c;

    /* renamed from: d  reason: collision with root package name */
    private final p f20964d;

    /* renamed from: e  reason: collision with root package name */
    private Activity f20965e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(Context context, a aVar, n nVar, p pVar) {
        this.f20961a = context;
        this.f20962b = aVar;
        this.f20963c = nVar;
        this.f20964d = pVar;
    }

    public void i(Activity activity) {
        this.f20965e = activity;
    }

    public void onMethodCall(i iVar, j.d dVar) {
        String str = iVar.a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1544053025:
                if (str.equals("checkServiceStatus")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1017315255:
                if (str.equals("shouldShowRequestPermissionRationale")) {
                    c9 = 1;
                    break;
                }
                break;
            case -576207927:
                if (str.equals("checkPermissionStatus")) {
                    c9 = 2;
                    break;
                }
                break;
            case 347240634:
                if (str.equals("openAppSettings")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1669188213:
                if (str.equals("requestPermissions")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                int parseInt = Integer.parseInt(iVar.b.toString());
                p pVar = this.f20964d;
                Context context = this.f20961a;
                Objects.requireNonNull(dVar);
                pVar.a(parseInt, context, new k(dVar), new e(dVar));
                return;
            case 1:
                int parseInt2 = Integer.parseInt(iVar.b.toString());
                n nVar = this.f20963c;
                Activity activity = this.f20965e;
                Objects.requireNonNull(dVar);
                nVar.i(parseInt2, activity, new j(dVar), new g(dVar));
                return;
            case 2:
                int parseInt3 = Integer.parseInt(iVar.b.toString());
                n nVar2 = this.f20963c;
                Context context2 = this.f20961a;
                Objects.requireNonNull(dVar);
                nVar2.c(parseInt3, context2, new h(dVar));
                return;
            case 3:
                a aVar = this.f20962b;
                Context context3 = this.f20961a;
                Objects.requireNonNull(dVar);
                aVar.a(context3, new c(dVar), new f(dVar));
                return;
            case 4:
                n nVar3 = this.f20963c;
                Activity activity2 = this.f20965e;
                Objects.requireNonNull(dVar);
                nVar3.g((List) iVar.b(), activity2, new i(dVar), new d(dVar));
                return;
            default:
                dVar.b();
                return;
        }
    }
}
