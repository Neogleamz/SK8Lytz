package j3;

import android.graphics.Paint;
import androidx.core.graphics.d;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.q;
import kotlin.jvm.internal.p;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements yf.a, j.c {

    /* renamed from: a  reason: collision with root package name */
    private j f20635a;

    /* renamed from: b  reason: collision with root package name */
    private final Paint f20636b = new Paint();

    public void onAttachedToEngine(a.b bVar) {
        p.e(bVar, "flutterPluginBinding");
        j jVar = new j(bVar.b(), "emoji_picker_flutter");
        this.f20635a = jVar;
        jVar.e(this);
    }

    public void onDetachedFromEngine(a.b bVar) {
        p.e(bVar, "binding");
        j jVar = this.f20635a;
        if (jVar == null) {
            p.t("channel");
            jVar = null;
        }
        jVar.e((j.c) null);
    }

    public void onMethodCall(i iVar, j.d dVar) {
        ArrayList arrayList;
        p.e(iVar, "call");
        p.e(dVar, "result");
        if (!p.a(iVar.a, "getSupportedEmojis")) {
            dVar.b();
            return;
        }
        List<String> list = (List) iVar.a("source");
        if (list != null) {
            arrayList = new ArrayList(q.m(list, 10));
            for (String str : list) {
                arrayList.add(Boolean.valueOf(d.a(this.f20636b, str)));
            }
        } else {
            arrayList = null;
        }
        dVar.success(arrayList);
    }
}
