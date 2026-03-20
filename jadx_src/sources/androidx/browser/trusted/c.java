package androidx.browser.trusted;

import android.os.IBinder;
import c.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    private final c.a f1698a;

    private c(c.a aVar) {
        this.f1698a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c a(IBinder iBinder) {
        c.a d8 = iBinder == null ? null : a.AbstractBinderC0094a.d(iBinder);
        if (d8 == null) {
            return null;
        }
        return new c(d8);
    }
}
