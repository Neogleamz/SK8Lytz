package androidx.core.os;

import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: a  reason: collision with root package name */
    public static final c f4767a = new c();

    private c() {
    }

    public static final void a(Bundle bundle, String str, Size size) {
        kotlin.jvm.internal.p.e(bundle, "bundle");
        kotlin.jvm.internal.p.e(str, "key");
        bundle.putSize(str, size);
    }

    public static final void b(Bundle bundle, String str, SizeF sizeF) {
        kotlin.jvm.internal.p.e(bundle, "bundle");
        kotlin.jvm.internal.p.e(str, "key");
        bundle.putSizeF(str, sizeF);
    }
}
