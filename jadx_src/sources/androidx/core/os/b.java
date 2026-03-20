package androidx.core.os;

import android.os.Bundle;
import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    public static final b f4766a = new b();

    private b() {
    }

    public static final void a(Bundle bundle, String str, IBinder iBinder) {
        kotlin.jvm.internal.p.e(bundle, "bundle");
        kotlin.jvm.internal.p.e(str, "key");
        bundle.putBinder(str, iBinder);
    }
}
