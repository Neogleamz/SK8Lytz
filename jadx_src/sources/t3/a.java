package t3;

import android.content.Context;
import java.io.File;
import java.util.UUID;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public static final a f22848a = new a();

    private a() {
    }

    public final File a(Context context) {
        p.e(context, "context");
        String uuid = UUID.randomUUID().toString();
        p.d(uuid, "toString(...)");
        return new File(context.getCacheDir(), uuid);
    }
}
