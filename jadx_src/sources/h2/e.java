package h2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import io.flutter.plugin.common.d;
import io.flutter.plugin.common.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: a  reason: collision with root package name */
    private final Handler f20237a = new Handler(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void g(j.d dVar, String str, String str2, Object obj) {
        if (dVar != null) {
            dVar.a(str, str2, obj);
        } else {
            Log.w("ffmpeg-kit-flutter", String.format("ResultHandler can not send failure response %s:%s on a null method call result.", str, str2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void h(j.d dVar) {
        if (dVar != null) {
            dVar.b();
        } else {
            Log.w("ffmpeg-kit-flutter", "ResultHandler can not send not implemented response on a null method call result.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void i(j.d dVar, Object obj) {
        if (dVar != null) {
            dVar.success(obj);
        } else {
            Log.w("ffmpeg-kit-flutter", String.format("ResultHandler can not send successful response %s on a null method call result.", obj));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void j(d.b bVar, Object obj) {
        if (bVar != null) {
            bVar.success(obj);
        } else {
            Log.w("ffmpeg-kit-flutter", String.format("ResultHandler can not send event %s on a null event sink.", obj));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(j.d dVar, String str, String str2) {
        f(dVar, str, str2, null);
    }

    void f(j.d dVar, String str, String str2, Object obj) {
        this.f20237a.post(new d(dVar, str, str2, obj));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(j.d dVar) {
        this.f20237a.post(new b(dVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(d.b bVar, Object obj) {
        this.f20237a.post(new a(bVar, obj));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(j.d dVar, Object obj) {
        this.f20237a.post(new c(dVar, obj));
    }
}
