package androidx.browser.customtabs;

import android.app.PendingIntent;
import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    final b.a f1690a;

    /* renamed from: b  reason: collision with root package name */
    private final PendingIntent f1691b;

    /* renamed from: c  reason: collision with root package name */
    private final b f1692c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends b {
        a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(b.a aVar, PendingIntent pendingIntent) {
        if (aVar == null && pendingIntent == null) {
            throw new IllegalStateException("CustomTabsSessionToken must have either a session id or a callback (or both).");
        }
        this.f1690a = aVar;
        this.f1691b = pendingIntent;
        this.f1692c = aVar == null ? null : new a();
    }

    private IBinder b() {
        b.a aVar = this.f1690a;
        if (aVar != null) {
            return aVar.asBinder();
        }
        throw new IllegalStateException("CustomTabSessionToken must have valid binder or pending session");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder a() {
        b.a aVar = this.f1690a;
        if (aVar == null) {
            return null;
        }
        return aVar.asBinder();
    }

    PendingIntent c() {
        return this.f1691b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof f) {
            f fVar = (f) obj;
            PendingIntent c9 = fVar.c();
            PendingIntent pendingIntent = this.f1691b;
            if ((pendingIntent == null) != (c9 == null)) {
                return false;
            }
            return pendingIntent != null ? pendingIntent.equals(c9) : b().equals(fVar.b());
        }
        return false;
    }

    public int hashCode() {
        PendingIntent pendingIntent = this.f1691b;
        return pendingIntent != null ? pendingIntent.hashCode() : b().hashCode();
    }
}
