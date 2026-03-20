package n6;

import android.os.IBinder;
import android.os.Parcel;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n0 extends com.google.android.gms.internal.common.a implements a0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public n0(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICertData");
    }

    @Override // n6.a0
    public final int a() {
        Parcel d8 = d(2, e());
        int readInt = d8.readInt();
        d8.recycle();
        return readInt;
    }

    @Override // n6.a0
    public final x6.a b() {
        Parcel d8 = d(1, e());
        x6.a e8 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e8;
    }
}
