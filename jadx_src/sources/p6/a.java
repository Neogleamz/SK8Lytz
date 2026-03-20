package p6;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.TelemetryData;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends a7.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public a(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.service.IClientTelemetryService");
    }

    public final void k(TelemetryData telemetryData) {
        Parcel d8 = d();
        a7.c.c(d8, telemetryData);
        g(1, d8);
    }
}
