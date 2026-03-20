package n6;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o0 extends com.google.android.gms.internal.common.g implements a0 {
    public o0() {
        super("com.google.android.gms.common.internal.ICertData");
    }

    public static a0 e(IBinder iBinder) {
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ICertData");
        return queryLocalInterface instanceof a0 ? (a0) queryLocalInterface : new n0(iBinder);
    }

    @Override // com.google.android.gms.internal.common.g
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            x6.a b9 = b();
            parcel2.writeNoException();
            com.google.android.gms.internal.common.h.d(parcel2, b9);
        } else if (i8 != 2) {
            return false;
        } else {
            int a9 = a();
            parcel2.writeNoException();
            parcel2.writeInt(a9);
        }
        return true;
    }
}
