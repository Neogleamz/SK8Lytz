package z6;

import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends a implements f {
    /* JADX INFO: Access modifiers changed from: package-private */
    public d(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
    }

    @Override // z6.f
    public final String a() {
        Parcel e8 = e(1, d());
        String readString = e8.readString();
        e8.recycle();
        return readString;
    }

    @Override // z6.f
    public final boolean u(boolean z4) {
        Parcel d8 = d();
        c.a(d8, true);
        Parcel e8 = e(2, d8);
        boolean b9 = c.b(e8);
        e8.recycle();
        return b9;
    }
}
