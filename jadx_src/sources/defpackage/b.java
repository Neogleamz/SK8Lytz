package defpackage;

import android.util.Log;
import java.util.List;
import kotlin.collections.q;
/* renamed from: b  reason: default package */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    /* JADX INFO: Access modifiers changed from: private */
    public static final List<Object> b(Throwable th) {
        if (th instanceof FlutterError) {
            return q.h(new Object[]{((FlutterError) th).a(), th.getMessage(), ((FlutterError) th).b()});
        }
        return q.h(new String[]{th.getClass().getSimpleName(), th.toString(), "Cause: " + th.getCause() + ", Stacktrace: " + Log.getStackTraceString(th)});
    }
}
