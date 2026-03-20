package f7;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.measurement.internal.f6;
import com.google.android.gms.measurement.internal.x4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* renamed from: a  reason: collision with root package name */
    private final a f19844a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(Context context, Intent intent);
    }

    public j(a aVar) {
        n6.j.l(aVar);
        this.f19844a = aVar;
    }

    public final void a(Context context, Intent intent) {
        x4 i8 = f6.a(context, null, null).i();
        if (intent == null) {
            i8.J().a("Receiver called with null intent");
            return;
        }
        String action = intent.getAction();
        i8.I().b("Local receiver got", action);
        if (!"com.google.android.gms.measurement.UPLOAD".equals(action)) {
            if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
                i8.J().a("Install Referrer Broadcasts are deprecated");
                return;
            }
            return;
        }
        Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        i8.I().a("Starting wakeful intent.");
        this.f19844a.a(context, className);
    }
}
